package de.tudarmstadt.informatik.secuso.phishedu.backend;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;
import java.util.Scanner;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;

import com.google.android.gms.appstate.AppStateClient;
import com.google.android.gms.games.GamesClient;
import com.google.example.games.basegameutils.BaseGameActivity;
import com.google.example.games.basegameutils.GameHelper;
import com.google.gson.Gson;

import de.tudarmstadt.informatik.secuso.phishedu.Constants;
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
public class BackendController implements BackendControllerInterface, GameStateLoadedListener, UrlsLoadedListener {
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
	private static final int URL_CACHE_SIZE = 100;
	private static final double LEVEL_DISTANCE = 1.5;

	private static Random random = new Random();

	//singleton instance
	private static BackendController instance = new BackendController();

	//instance variables
	private FrontendControllerInterface frontend;
	private GameHelper gamehelper;
	private boolean inited = false;
	//indexed by UrlType
	private PhishURLInterface[][] urlCache;
	private boolean gamestate_loaded = false;
	private GameProgress progress;

	private static PhishURL[] deserializeURLs(String serialized){
		return new Gson().fromJson(serialized, PhishURL[].class);
	}

	private static String serializeURLs(PhishURLInterface[] object){
		return new Gson().toJson(object);
	}

	/**
	 * This function returns a Phishing url of the given type
	 * @param type Type of Attack for the URL
	 * @return A PhishURL of the given type
	 */
	public PhishURLInterface getPhishURL(PhishAttackType type){
		if(type.getValue() >= this.urlCache.length || this.urlCache[type.getValue()].length == 0){
			throw new IllegalArgumentException("This phish type is not cached.");
		}
		return urlCache[type.getValue()][random.nextInt(urlCache[type.getValue()].length)].clone();
	}

	/**
	 * This holds the current URL returned by the last {@link BackendControllerInterface}{@link #getNextUrl()} call
	 */
	private PhishURLInterface current_url;

	/**
	 * save the current repeat offset of the attack.
	 * This is needed to reinsert the offset into the {@link #level_repeat_offsets} list on fail.
	 */
	private int current_url_level_offset=0;

	/**
	 * Private constructor for singelton.
	 */
	private BackendController() {
		this.urlCache=new PhishURLInterface[CACHE_TYPES.length][];
		for(PhishAttackType type: CACHE_TYPES){
			this.urlCache[type.getValue()]=new PhishURLInterface[0];
		}
	}

	/**
	 * Getter for singleton.
	 * @return The singleton Object of this class
	 */
	public static BackendControllerInterface getInstance(){
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

	public void init(FrontendControllerInterface frontend){
		if(this.isInitDone()){
			return;
		}
		this.frontend=frontend;
		this.gamehelper=new GameHelper(frontend.getBaseActivity());
		this.gamehelper.setup(this,BaseGameActivity.CLIENT_APPSTATE | BaseGameActivity.CLIENT_GAMES);
		SharedPreferences prefs = this.frontend.getContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
		GamesClient gamesclient = this.gamehelper.getGamesClient();
		AppStateClient appstateclient = this.gamehelper.getAppStateClient();
		Context context = this.frontend.getContext();
		GameProgress.init(context, prefs, gamesclient,appstateclient,this);
		this.progress = GameProgress.getInstance();
		SharedPreferences url_cache = this.frontend.getContext().getSharedPreferences(URL_CACHE_NAME, Context.MODE_PRIVATE);
		for(PhishAttackType type: CACHE_TYPES){
			loadUrls(url_cache, type);
		}
		checkInitDone();
	}

	private void CacheUrls(SharedPreferences cache, PhishAttackType type, PhishURLInterface[] urls){
		SharedPreferences.Editor editor = cache.edit();
		editor.putString(type.toString(), serializeURLs(urls));
		editor.commit();
	}

	private void loadUrls(SharedPreferences cache, PhishAttackType type){
		//first we load the cached value if available
		PhishURL[] urls=deserializeURLs(cache.getString(type.toString(), "[]"));
		//If the values are still empty we load the factory defaults 
		if(urls.length==0){
			int resource = frontend.getContext().getResources().getIdentifier(type.toString().toLowerCase(Locale.US), "raw", frontend.getContext().getPackageName());
			InputStream input = frontend.getContext().getResources().openRawResource(resource);
			String json = new Scanner(input,"UTF-8").useDelimiter("\\A").next();
			urls = (new Gson()).fromJson(json, PhishURL[].class);
		}
		this.setURLs(type, urls);
		//then we get the value from the online store
		new GetUrlsTask(this).execute(URL_CACHE_SIZE,type.getValue());
	}

	private void setURLs(PhishAttackType type, PhishURLInterface[] urls){
		ArrayList<PhishURLInterface> result = new ArrayList<PhishURLInterface>();
		for (PhishURLInterface phishURL : urls) {
			if(phishURL.validate()){
				result.add(phishURL);	
			}
		}
		if(result.size()>0){
			this.urlCache[type.getValue()] = result.toArray(new PhishURLInterface[0]);
		}
	}

	public void urlsReturned(PhishURLInterface[] urls, PhishAttackType type){
		if(urls.length > 0){
			this.setURLs(type, urls);
			this.CacheUrls(this.frontend.getContext().getSharedPreferences(URL_CACHE_NAME, Context.MODE_PRIVATE),type, this.urlCache[type.getValue()]);
			this.checkInitDone();
		}
	}

	public void urlDownloadProgress(int percent){
		this.frontend.initProgress(percent);
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
			this.frontend.initDone();
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
		if(getLevel()>=FIRST_REPEAT_LEVEL){
			for(int i=1;i<=getLevel()-(FIRST_REPEAT_LEVEL-1);i++){
				this.level_repeat_offsets.add(i);
			}
			while(this.level_repeat_offsets.size()<this.levelRepeats()){
				//select a random earlier Level 
				//"-(FIRST_REPEAT_LEVEL-1)" is to prevent levels 0-2 from being repeated
				//"+1" is to prevent "repeating" the current level
				int index_level = (random.nextInt(getLevel()-(FIRST_REPEAT_LEVEL-1)))+1;
				this.level_repeat_offsets.add(index_level);
			}
		}
		this.progress.setLevel(level);
		this.frontend.onLevelChange(this.progress.getLevel());
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
	public void nextUrl() {
		checkinited();
		if(getLevel() <= 1){
			//Level 0 and 1 do not have repeats
			throw new IllegalStateException("This function is not defined for level 0 and 1 as these do not need URLs");
		}
		int remaining_urls = this.levelURLs()-doneURLs();
		int remaining_phish = this.levelPhishes()-this.progress.getPresentedPhish();
		int remaining_repeats = this.levelRepeats() - this.progress.getPresentedRepeats();
		//We might have failed the level
		//Either by going out of URLs or by going out of options to detect phish
		int state = levelState();
		switch (state) {
		case LEVEL_FAILED:
			this.frontend.levelFailed(getLevel());
			return;
		case LEVEL_FINISHED:
			this.levelFinished(this.getLevel());
			return;
		}

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

		//First we choose a random start URL
		PhishURLInterface base_url=getPhishURL(PhishAttackType.NoPhish);
		//then we decorate the URL with a random generator
		base_url=AbstractUrlDecorator.decorate(base_url, GENERATORS[random.nextInt(GENERATORS.length)]);
		//Lastly we might apply a attack
		current_url_level_offset=0;
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
			Class<? extends AbstractUrlDecorator> attack;
			if(present_repeat){
				this.progress.incPresentedRepeats();
				current_url_level_offset=this.level_repeat_offsets.remove(random.nextInt(this.level_repeat_offsets.size()));
			}
			int attack_level= getLevel() - current_url_level_offset;
			NoPhishLevelInfo attack_level_info = BackendController.getInstance().getLevelInfo(attack_level);
			//choose a random attack from the selected Level
			Class<? extends AbstractUrlDecorator>[] level_attacks = attack_level_info.attackTypes;
			attack=level_attacks[random.nextInt(level_attacks.length)];
			//decorate the current url with this attack
			base_url=AbstractUrlDecorator.decorate(base_url,attack);
		}

		this.current_url=base_url;
	}


	private int levelRepeats(){
		return (int) Math.floor(this.levelPhishes()/2);
	}

	@Override
	public String[] getUrl(){
		return (String[]) this.current_url.getParts();
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
		if(result != PhishResult.Phish_Detected){
			this.addResult(result);
		}
		return result;
	}

	private void addResult(PhishResult result){
		this.progress.addResult(result);
		if(result == PhishResult.Phish_NotDetected){
			if(current_url_level_offset>0){
				level_repeat_offsets.add(current_url_level_offset);
			}
			progress.decLives();
		}
		int offset=this.current_url.getPoints(result);
		//with this function we ensure that the user gets more points per level
		//This ensures that there is no point in running the same level multiple times to collect points
		offset*=Math.pow(LEVEL_DISTANCE, this.getLevel());
		this.frontend.displayToastScore(offset);
		this.progress.setLevelPoints(this.getLevelPoints()+offset);
	}

	@Override
	public PhishSiteType getSiteType() {
		checkinited();
		return this.current_url.getSiteType();
	}

	@Override
	public PhishAttackType getAttackType() {
		checkinited();
		return this.current_url.getAttackType();
	}

	@Override
	public boolean partClicked(int part) {
		checkinited();
		boolean clickedright = this.current_url.partClicked(part);
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

	private void levelFinished(int level){
		this.progress.commitPoints();
		this.progress.unlockLevel(level+1);
		this.frontend.levelFinished(level);
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
	public int levelState() {
		int remaining_urls = levelURLs()-doneURLs();
		int result = 0;
		if(progress.getRemainingLives() <= 0){
			result = LEVEL_FAILED;
		}else if(remaining_urls <= 0){
			result = LEVEL_FINISHED;
		}else{
			result = LEVEL_RUNNING;
		}

		return result;
	}

	@Override
	public Integer[] getAttackParts() {
		return current_url.getAttackParts().toArray(new Integer[0]);
	}

	@Override
	public int getLifes() {
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
}
