package de.tudarmstadt.informatik.secuso.phishedu.backend;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.Scanner;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Vibrator;

import com.google.android.gms.appstate.AppStateClient;
import com.google.android.gms.games.GamesClient;
import com.google.example.games.basegameutils.BaseGameActivity;
import com.google.example.games.basegameutils.GameHelper;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import de.tudarmstadt.informatik.secuso.phishedu.R;
import de.tudarmstadt.informatik.secuso.phishedu.backend.generator.KeepGenerator;
import de.tudarmstadt.informatik.secuso.phishedu.backend.generator.SudomainGenerator;
import de.tudarmstadt.informatik.secuso.phishedu.backend.networkTasks.GetUrlsTask;
import de.tudarmstadt.informatik.secuso.phishedu.backend.networkTasks.SendMailTask;
import de.tudarmstadt.informatik.secuso.phishedu.backend.networkTasks.UrlsLoadedListener;

/**
 * This is the main backend logik.
 * It is implemented as static singleton to keep state while changing activities.
 * @author Clemens Bergmann <cbergmann@schuhklassert.de>
 *
 */
public class BackendControllerImpl implements BackendController, GameStateLoadedListener, UrlsLoadedListener {
	//constants
	private static final String PREFS_NAME = "PhisheduState";
	private static final String URL_CACHE_NAME ="urlcache";
	private static final String LEVEL1_URL = "https://pages.no-phish.de/level1.php";
	private static final int FIRST_REPEAT_LEVEL = 4;
	private static final PhishAttackType[] CACHE_TYPES = {PhishAttackType.AnyPhish, PhishAttackType.NoPhish};
	@SuppressWarnings("rawtypes")
	private static Class[] GENERATORS = {
		//Currently we use the same generators for all levels
		SudomainGenerator.class,
		KeepGenerator.class,
	};
	private static final int URL_CACHE_SIZE = 500;
	private static final double LEVEL_DISTANCE = 1.5;

	private static Random random = new Random();

	//singleton instance
	private static BackendControllerImpl instance = new BackendControllerImpl();

	//instance variables
	private FrontendController frontend;
	private GameHelper gamehelper;
	private boolean inited = false;
	//indexed by UrlType
	private PhishURL[][] urlCache;
	private boolean gamestate_loaded = false;
	private GameProgress progress;
	private List<OnLevelstateChangeListener> onLevelstateChangeListeners=new ArrayList<BackendController.OnLevelstateChangeListener>();
	private List<OnLevelChangeListener> onLevelChangeListeners=new ArrayList<BackendController.OnLevelChangeListener>();
	private BackendInitListener initListener;

	private static BasePhishURL[] deserializeURLs(String serialized){
		BasePhishURL[] result = new BasePhishURL[0];
		try {
			result = (new Gson()).fromJson(serialized, BasePhishURL[].class);
		} catch (JsonSyntaxException e) {
		}
		return result;
	}

	private static String serializeURLs(PhishURL[] object){
		return new Gson().toJson(object);
	}

	/**
	 * This function returns a Phishing url of the given type
	 * @param type Type of Attack for the URL
	 * @return A PhishURL of the given type
	 */
	public PhishURL getPhishURL(PhishAttackType type){
		if(type.getValue() >= this.urlCache.length || this.urlCache[type.getValue()].length == 0){
			throw new IllegalArgumentException("This phish type is not cached.");
		}
		return urlCache[type.getValue()][random.nextInt(urlCache[type.getValue()].length)].clone();
	}

	/**
	 * This holds the current URL returned by the last {@link BackendController}{@link #getNextUrl()} call
	 */
	private PhishURL current_url;

	/**
	 * save the current repeat offset of the attack.
	 * This is needed to reinsert the offset into the {@link #level_repeat_offsets} list on fail.
	 */
	private int current_url_level_offset=0;

	/**
	 * Private constructor for singelton.
	 */
	private BackendControllerImpl() {
		this.urlCache=new PhishURL[CACHE_TYPES.length][];
		for(PhishAttackType type: CACHE_TYPES){
			this.urlCache[type.getValue()]=new PhishURL[0];
		}
	}

	/**
	 * Getter for singleton.
	 * @return The singleton Object of this class
	 */
	public static BackendController getInstance(){
		return instance;
	}

	/**
	 * Check if the singleton is inited. If not it will throw a IllegalStateException;
	 */
	private static void checkinited(){
		if(instance == null || !(instance.isInitDone())){
			throw new IllegalStateException("initialize me first! Call backendcontroller.init()");
		}
	}

	public void init(FrontendController frontend, BackendInitListener initlistener){
		if(this.isInitDone()){
			return;
		}
		this.frontend=frontend;
		this.initListener=initlistener;
		this.gamehelper=new GameHelper(frontend.getBaseActivity());
		this.gamehelper.setup(this,BaseGameActivity.CLIENT_APPSTATE | BaseGameActivity.CLIENT_GAMES);
		SharedPreferences prefs = this.frontend.getContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
		GamesClient gamesclient = this.gamehelper.getGamesClient();
		AppStateClient appstateclient = this.gamehelper.getAppStateClient();
		Context context = this.frontend.getContext();
		this.progress = new GameProgress(context, prefs, gamesclient,appstateclient,this);
		SharedPreferences url_cache = this.frontend.getContext().getSharedPreferences(URL_CACHE_NAME, Context.MODE_PRIVATE);
		for(PhishAttackType type: CACHE_TYPES){
			loadUrls(url_cache, type);
		}
		checkInitDone();
	}

	private void CacheUrls(SharedPreferences cache, PhishAttackType type, PhishURL[] urls){
		SharedPreferences.Editor editor = cache.edit();
		editor.putString(type.toString(), serializeURLs(urls));
		editor.commit();
	}

	private void loadUrls(SharedPreferences cache, PhishAttackType type){
		//first we load the cached value if available
		BasePhishURL[] urls=deserializeURLs(cache.getString(type.toString(), "[]"));
		//If the values are still empty we load the factory defaults 
		if(urls.length==0){
			int resource = frontend.getContext().getResources().getIdentifier(type.toString().toLowerCase(Locale.US), "raw", frontend.getContext().getPackageName());
			InputStream input = frontend.getContext().getResources().openRawResource(resource);
			String json = new Scanner(input,"UTF-8").useDelimiter("\\A").next();
			try {
				urls = (new Gson()).fromJson(json, BasePhishURL[].class);
			} catch (JsonSyntaxException e) {
			}
		}
		this.setURLs(type, urls);
		//then we get the value from the online store
		new GetUrlsTask(this).execute(URL_CACHE_SIZE,type.getValue());
	}

	private void setURLs(PhishAttackType type, PhishURL[] urls){
		ArrayList<PhishURL> result = new ArrayList<PhishURL>();
		for (PhishURL phishURL : urls) {
			if(phishURL.validate()){
				result.add(phishURL);	
			}
		}
		if(result.size()>0){
			this.urlCache[type.getValue()] = result.toArray(new PhishURL[0]);
		}
	}

	public void urlsReturned(PhishURL[] urls, PhishAttackType type){
		if(urls.length > 0){
			this.setURLs(type, urls);
			this.CacheUrls(this.frontend.getContext().getSharedPreferences(URL_CACHE_NAME, Context.MODE_PRIVATE),type, this.urlCache[type.getValue()]);
			this.checkInitDone();
		}
	}

	public void urlDownloadProgress(int percent){
		this.initListener.initProgress(percent);
	}

	public boolean isInitDone(){
		return this.inited;
	}

	private void checkInitDone(){
		//This means we already are initialized
		if(isInitDone()){
			return;
		}
		if (this.urlCache[PhishAttackType.NoPhish.getValue()].length >0 && this.urlCache[PhishAttackType.AnyPhish.getValue()].length > 0 &&  this.gamestate_loaded){
			this.inited=true;
			this.initListener.onInitDone();
			this.progress.StartFinished();	
		}
	}

	public void sendMail(String from, String to, String usermessage){
		checkinited();
		new SendMailTask(from, to, usermessage).execute();
	}

	private ArrayList<Integer> level_repeat_offsets;
	@Override
	public void startLevel(int level) {
		checkinited();
		this.level_repeat_offsets=new ArrayList<Integer>();
		this.current_url_level_offset=0;
		if(getLevel()>=FIRST_REPEAT_LEVEL){
			for(int i=1;i<=getLevel()-(FIRST_REPEAT_LEVEL-1);i++){
				this.level_repeat_offsets.add(i);
			}
			fillLevelRepeats(this.levelRepeats());
		}
		this.progress.setLevel(level);
		for (OnLevelChangeListener listener : onLevelChangeListeners) {
			listener.onLevelChange(level);
		}
		notifyLevelStateChangedListener(Levelstate.running, level);
	}

	/**
	 * fill up the repeats to a given size
	 * @param size The size the {@link #level_repeat_offsets} should have afterwards
	 */
	private void fillLevelRepeats(int size){
		while(this.level_repeat_offsets.size()<size){
			//select a random earlier Level 
			//"-(FIRST_REPEAT_LEVEL-1)" is to prevent levels 0-2 from being repeated
			//"+1" is to prevent "repeating" the current level
			int level_offset = (random.nextInt(getLevel()-(FIRST_REPEAT_LEVEL-1)))+1;
			this.level_repeat_offsets.add(level_offset);
		}
	}

	@Override
	public void restartLevel(){
		this.startLevel(this.getLevel());
	}

	@Override
	public void redirectToLevel1URL(){
		Random random = new Random();
		char[] buf=new char[4];
		for(int i=0;i<buf.length;i++){
			buf[i]=(char) ('a'+random.nextInt(26));
		}
		String random_string=new String(buf);
		this.frontend.startBrowser(Uri.parse(LEVEL1_URL+"?frag="+random_string+"#bottom-"+random_string));
	}

	@SuppressWarnings("unchecked")
	private Class<? extends AbstractUrlDecorator> findAttack(){
		int remaining_urls = levelCorrectURLs() - (this.progress.getLevelResults(PhishResult.Phish_Detected) + this.progress.getLevelResults(PhishResult.NoPhish_Detected));
		int remaining_phish = this.levelPhishes() - this.progress.getLevelResults(PhishResult.Phish_Detected);
		int remaining_repeats = this.levelRepeats() - this.progress.getIdentifiedRepeats();

		//we have to decide whether we want to present a phish or not
		boolean present_phish=false;
		if(remaining_phish > 0){
			if(remaining_urls<=remaining_phish){
				present_phish=true;
			}else{
				present_phish=random.nextBoolean();
			}
		}

		//In Level 2 we always present the phish because The test is implemented as Attack
		if(this.getLevel() == 2){
			present_phish=true;
		}

		current_url_level_offset=0;
		Class<? extends AbstractUrlDecorator> attack = null;
		if(present_phish){
			boolean present_repeat = false;
			if(getLevel() < FIRST_REPEAT_LEVEL){
				//Up until level FIRST_REPEAT_LEVEL we don't repeat because levels 0-2 are special. 
				present_repeat = false;
			}else if(remaining_urls <= remaining_repeats){
				//we have to present a repeat
				present_repeat = true;
			}else if( remaining_repeats > 0 ){
				//we might present a repeat
				present_repeat = random.nextBoolean();
			}

			if(present_repeat){
				//something went wrong and we are out of repeats.
				if(this.level_repeat_offsets.size()==0){
					fillLevelRepeats(1);
				}
				current_url_level_offset=this.level_repeat_offsets.remove(random.nextInt(this.level_repeat_offsets.size()));
			}
			int attack_level= getLevel() - current_url_level_offset;
			NoPhishLevelInfo attack_level_info = BackendControllerImpl.getInstance().getLevelInfo(attack_level);
			//choose a random attack from the selected Level
			Class<? extends AbstractUrlDecorator>[] level_attacks = attack_level_info.attackTypes;
			attack=level_attacks[random.nextInt(level_attacks.length)];
		}
		
		return attack;
	}

	@SuppressWarnings("unchecked")
	public void nextUrl() {
		checkinited();
		if(getLevel() <= 1){
			//Level 0 and 1 do not have repeats
			throw new IllegalStateException("This function is not defined for level 0 and 1 as these do not need URLs");
		}
		
		Class<? extends AbstractUrlDecorator> attack = findAttack();

		PhishURL base_url;
		String before_url = "",after_url = "";
		int tries = 5;
		do{
			//First we choose a random start URL
			base_url=getPhishURL(PhishAttackType.NoPhish);
			//then we decorate the URL with a random generator
			base_url=AbstractUrlDecorator.decorate(base_url, GENERATORS[random.nextInt(GENERATORS.length)]);
			//Lastly we might apply a attack
			if(attack != null){
				//decorate the current url with this attack
				before_url=Arrays.toString(base_url.getParts());
				base_url=AbstractUrlDecorator.decorate(base_url,attack);
				after_url=Arrays.toString(base_url.getParts());
			}
			tries--;
		}while(attack !=null && before_url.equals(after_url) && tries > 0); //The attack might not change the URL so we try again.
		
		if(tries == 0){
			throw new IllegalStateException("Could not find attackable URL. Attack:"+attack.getName());
		}
		
		this.current_url=base_url;
	}

	private int levelRepeats(){
		return (int) Math.floor(this.levelPhishes()/2);
	}

	@Override
	public PhishURL getUrl(){
		return this.current_url;
	}

	@Override
	public int getLevel() {
		checkinited();
		return this.progress.getLevel();
	}

	@Override
	public PhishResult userClicked(boolean acceptance) {
		checkinited();
		PhishResult result=this.current_url.getResult(acceptance);
		//When we are in the HTTPS level we only accept https urls even if these are not attacks.
		if(getLevel() == 10 && !this.getUrl().getParts()[0].equals("https:")){
			if(acceptance){
				result=PhishResult.Phish_NotDetected;
			}else{
				result=PhishResult.Phish_Detected;
			}
		}
		if(result != PhishResult.Phish_Detected || getLevel() == 10){
			this.addResult(result);
		}
		return result;
	}

	private void addResult(PhishResult result){
		Levelstate oldstate = getLevelState();
		this.progress.addResult(result);
		if(result == PhishResult.Phish_NotDetected){
			if(current_url_level_offset>0){
				level_repeat_offsets.add(current_url_level_offset);
			}
			progress.decLives();
			Vibrator v = (Vibrator) frontend.getContext().getSystemService(Context.VIBRATOR_SERVICE);
			v.vibrate(500);
		}else if(result == PhishResult.Phish_Detected && current_url_level_offset > 0){
			this.progress.incIdentifiedRepeats();
		}
		int offset=this.current_url.getPoints(result);
		//with this function we ensure that the user gets more points per level
		//This ensures that there is no point in running the same level multiple times to collect points
		offset*=Math.pow(LEVEL_DISTANCE, this.getLevel());
		this.frontend.displayToastScore(offset);
		this.progress.setLevelPoints(this.getLevelPoints()+offset);

		Levelstate newstate = getLevelState();
		if(oldstate != newstate){
			switch (newstate) {
			case finished:
				levelFinished(this.getLevel());
				break;
			case failed:
				levelFailed(this.getLevel());
				break;
			default:
				break;
			}
		}
	}

	@Override
	public boolean partClicked(int part) {
		checkinited();
		boolean clickedright = part == current_url.getDomainPart();
		if(clickedright){
			addResult(PhishResult.Phish_Detected);
		}else{
			addResult(PhishResult.Phish_NotDetected);
		}
		return clickedright;
	}

	public int getTotalPoints(){
		checkinited();
		return this.progress.getPoints();
	}

	public int getLevelPoints(){
		checkinited();
		return this.progress.getLevelPoints();
	}

	public void onUrlReceive(Uri data){
		checkinited();
		if(data == null){
			return;
		}
		String host = data.getHost();
		if(host.equals("maillink")){
			this.levelFinished(0);
		}else if(host.equals("level1finished")){
			this.levelFinished(1);
		}else if(host.equals("level1failed")){
			this.startLevel(1);
		}
	}

	public void skipLevel0(){
		this.levelFinished(0);
	}

	private void levelFailed(int level){
		notifyLevelStateChangedListener(Levelstate.failed, level);
	}

	private void levelFinished(int level){
		this.progress.commitPoints();
		this.progress.unlockLevel(level+1);
		notifyLevelStateChangedListener(Levelstate.finished, level);
	}

	private void notifyLevelStateChangedListener(Levelstate newstate, int levelid){
		for (OnLevelstateChangeListener listener : onLevelstateChangeListeners) {
			listener.onLevelstateChange(newstate, levelid);
		}
	}

	@Override
	public void onGameStateLoaded() {
		this.gamestate_loaded=true;
		this.checkInitDone();
	}

	@Override
	public int getMaxUnlockedLevel() {
		return this.progress.getMaxUnlockedLevel();
	}

	@Override
	public void signIn() {
		checkinited();
		if(!this.gamehelper.isSignedIn()){
			this.gamehelper.beginUserInitiatedSignIn();
		}
	}

	@Override
	public void signOut() {
		checkinited();
		this.gamehelper.signOut();
	}

	@Override
	public int levelCorrectURLs() {
		if(getLevel()==2){
			return 5;
		}
		return 6+(2*this.getLevel());
	}

	private int levelURLs() {
		checkinited();
		int failed_urls=progress.getLevelResults(PhishResult.Phish_NotDetected)+progress.getLevelResults(PhishResult.NoPhish_NotDetected);
		return levelCorrectURLs()+failed_urls;
	}

	@Override
	public int getCorrectlyFoundURLs() {
		return progress.getLevelResults(PhishResult.Phish_Detected)+progress.getLevelResults(PhishResult.NoPhish_Detected);
	}

	private int levelPhishes() {
		checkinited();
		int base_phishes=0;
		if(this.getLevel()==2){
			base_phishes=levelCorrectURLs();
		}else{
			base_phishes=levelCorrectURLs()/2;
		}
		return base_phishes+progress.getLevelResults(PhishResult.Phish_NotDetected);
	}

	private int doneURLs() {
		checkinited();
		return this.progress.getDoneUrls();
	}

	@Override
	public Levelstate getLevelState() {
		int remaining_urls = levelURLs()-doneURLs();
		Levelstate result;
		if(this.getLifes() <= 0){
			result = Levelstate.failed;
		}else if(remaining_urls <= 0){
			result = Levelstate.finished;
		}else{
			result = Levelstate.running;
		}

		return result;
	}

	@Override
	public int getLifes() {
		checkinited();
		return this.progress.getRemainingLives();
	}

	@Override
	public void onSignInFailed() {
		frontend.onSignInFailed();
	}

	@Override
	public void onSignInSucceeded() {
		progress.loadState();
		gamehelper.getGamesClient().unlockAchievement(frontend.getContext().getResources().getString(R.string.achievement_welcome));
		frontend.onSignInSucceeded();
	}

	@Override
	public GameHelper getGameHelper() {
		return this.gamehelper;
	}

	@Override
	public int getLevelCount() {
		return NoPhishLevelInfo.levelCount();
	}

	@Override
	public NoPhishLevelInfo getLevelInfo(int levelid) {
		if(levelid >= getLevelCount()){
			throw new IllegalArgumentException("Invalid level ID. levelid>= getLevelCount()");
		}
		return new NoPhishLevelInfo(levelid);
	}

	@Override
	public NoPhishLevelInfo getLevelInfo() {
		return getLevelInfo(getLevel());
	}

	@Override
	public int getLevelPoints(int level) {
		return this.progress.getLevelPoints(level);
	}

	@Override
	public void deleteRemoteData() {
		this.progress.deleteRemoteData();
	}

	@Override
	public void addOnLevelstateChangeListener(OnLevelstateChangeListener listener) {
		if(!this.onLevelstateChangeListeners.contains(listener)){
			this.onLevelstateChangeListeners.add(listener);
		}
	}

	@Override
	public void removeOnLevelstateChangeListener(
			OnLevelstateChangeListener listener) {
		this.onLevelstateChangeListeners.remove(listener);
	}

	@Override
	public void addOnLevelChangeListener(OnLevelChangeListener listener) {
		if(!this.onLevelChangeListeners.contains(listener)){
			this.onLevelChangeListeners.add(listener);
		}
	}

	@Override
	public void removeOnLevelChangeListener(
			OnLevelChangeListener listener) {
		this.onLevelChangeListeners.remove(listener);
	}

	@Override
	public boolean getLevelCompleted(int level) {
		return getLevelPoints(level)>0;
	}
}
