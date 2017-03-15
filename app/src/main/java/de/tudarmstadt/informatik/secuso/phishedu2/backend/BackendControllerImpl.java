/*=========================================================================
 * The most reliable way to detect phishing is checking the URL
 * (web address) of a website. We developed an Android app to learn how
 * to detect Phishing URLs.
 * Copyright (C) 2015 SecUSo
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *=========================================================================*/

package de.tudarmstadt.informatik.secuso.phishedu2.backend;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.CountDownTimer;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.Scanner;
import java.util.Vector;

import de.tudarmstadt.informatik.secuso.phishedu2.Constants;
import de.tudarmstadt.informatik.secuso.phishedu2.backend.generator.BaseGenerator;
import de.tudarmstadt.informatik.secuso.phishedu2.backend.networkTasks.GetUrlsTask;
import de.tudarmstadt.informatik.secuso.phishedu2.backend.networkTasks.SendMailTask;
import de.tudarmstadt.informatik.secuso.phishedu2.backend.networkTasks.UrlsLoadedListener;

/*
import com.google.android.gms.games.Games;
import com.google.example.games.basegameutils.GameHelper;
*/

/**
 * This is the main backend logik.
 * It is implemented as static singleton to keep state while changing activities.
 * @author Clemens Bergmann <cbergmann@schuhklassert.de>
 *
 */
public class BackendControllerImpl implements BackendController, UrlsLoadedListener, SendMailTask.SendMailListener {
	//constants
	private static final String PREFS_NAME = "PhisheduState";
	private static final String URL_CACHE_NAME ="urlcache";
	private static final String KEY_AUTO_SIGN_IN = "KEY_AUTO_SIGN_IN";
	private static final String LEVEL1_URL = "";//"https://pages.no-phish.de/level1.php";
	private static final PhishAttackType[] CACHE_TYPES = {PhishAttackType.NoPhish};
	private static final int URL_CACHE_SIZE = 500;

	private Random random;

    @Override
    public void returnedResult(String result) {
        frontend.displayToast(result);
    }

    private class PhishCountDownTimer extends CountDownTimer{
		public long remaining = 0;

		public PhishCountDownTimer(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
			remaining = millisInFuture;
		}
		
		public void onTick(long millisUntilFinished) {
	    	 this.remaining=millisUntilFinished;
	    	 getFrontend().updateUI();
	     }

	     public void onFinish() {
	    	addResult(PhishResult.TimedOut);
	     }
	}

	//singleton instance
	private static BackendControllerImpl instance = new BackendControllerImpl();

	//instance variables
	private FrontendController frontend;
	//private GameHelper gamehelper;
	private boolean inited = false;
	//indexed by UrlType
	private EnumMap<PhishAttackType, PhishURL[]> urlCache=new EnumMap<PhishAttackType, PhishURL[]>(PhishAttackType.class);
	private GameProgress progress;
	private Vector<OnLevelstateChangeListener> onLevelstateChangeListeners=new Vector<BackendController.OnLevelstateChangeListener>();
	private Vector<OnLevelChangeListener> onLevelChangeListeners=new Vector<BackendController.OnLevelChangeListener>();
	private BackendInitListener initListener;
	
	public static BasePhishURL[] deserializeURLs(String serialized){
		BasePhishURL[] result = new BasePhishURL[0];
		try {
			result = (new Gson()).fromJson(serialized, BasePhishURL[].class);
			for (BasePhishURL url : result) {
				url.validateProviderName();
			}
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
		if(!this.urlCache.containsKey(type)){
			throw new IllegalArgumentException("This phish type is not cached.");
		}
		PhishURL random = urlCache.get(type)[getRandom().nextInt(urlCache.get(type).length)];
		
		PhishURL result = random.clone();
		return result;
	}

	/**
	 * This holds the current URL returned by the last {@link BackendController}#nextUrl call
	 */
	private PhishURL current_url;
	private PhishCountDownTimer current_timer;

	/**
	 * Private constructor for singelton.
	 */
	private BackendControllerImpl() {}

	/**
	 * Getter for singleton.
	 * @return The singleton Object of this class
	 */
	public static BackendControllerImpl getInstance(){
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
		//this.gamehelper=new GameHelper(frontend.getBaseActivity(),GameHelper.CLIENT_GAMES|GameHelper.CLIENT_SNAPSHOT);
		//this.gamehelper.setup(this);
		SharedPreferences prefs = this.getFrontend().getContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
		
		
		//boolean wasSignedIn = prefs.getBoolean(KEY_AUTO_SIGN_IN, false);
	    //this.gamehelper.setConnectOnStart(wasSignedIn);
				
		Context context = this.getFrontend().getContext();
		this.progress = new GameProgress(context, prefs /*, this.gamehelper.getApiClient()*/);
		SharedPreferences url_cache = this.getFrontend().getContext().getSharedPreferences(URL_CACHE_NAME, Context.MODE_PRIVATE);
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
			int resource = getFrontend().getContext().getResources().getIdentifier(type.toString().toLowerCase(Locale.US), "raw", getFrontend().getContext().getPackageName());
			InputStream input = getFrontend().getContext().getResources().openRawResource(resource);
			Scanner scanner = new Scanner(input,"UTF-8");
			scanner.useDelimiter("\\A");
			String json = scanner.next();
			scanner.close();
			urls = deserializeURLs(json);
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
			this.urlCache.put(type, urls);
		}
	}

	public void urlsReturned(PhishURL[] urls, PhishAttackType type){
		if(urls.length > 0){
			this.setURLs(type, urls);
			this.CacheUrls(this.getFrontend().getContext().getSharedPreferences(URL_CACHE_NAME, Context.MODE_PRIVATE),type, this.urlCache.get(type));
			this.checkInitDone();
		}
	}

    public EnumMap<PhishAttackType, PhishURL[]> getURLCache (){
        return this.urlCache;
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
		boolean all_attacks_cached=true;
		for (PhishAttackType attacktype : CACHE_TYPES) {
			all_attacks_cached &= this.urlCache.get(attacktype)!=null && this.urlCache.get(attacktype).length>0; 
		}

		if (all_attacks_cached){
			this.inited=true;
			this.initListener.onInitDone();
			this.progress.StartFinished();	
		}
	}

	public void sendMail(String from, String to, String usermessage){
		checkinited();
        new SendMailTask(from, to, usermessage, this).execute();
	}

	private Vector<PhishAttackType> level_attacks;
	
	@Override
	public void startLevel(int level) {
		startLevel(level, true);
	}
	
	@Override
	public void startLevel(int level, boolean showRepeats) {
		checkinited();
		if(level==1 && Constants.SKIP_LEVEL1){
			this.progress.finishlevel(1);
			level=2;
		}

		this.progress.setLevel(level);
		this.level_attacks=generateLevelAttacks(level);
		for(int i=0; i<onLevelChangeListeners.size();i++){
			onLevelChangeListeners.get(i).onLevelChange(level, showRepeats);
		}
		levelStarted(level);
	}

	private Vector<PhishAttackType> generateLevelAttacks(int level){
		Vector<PhishAttackType> attacks = new Vector<PhishAttackType>();
		NoPhishLevelInfo level_info = getLevelInfo(level);
		int this_level_attacks = level_info.levelPhishes() - level_info.levelRepeats();

		//first add the attacks from this level;
		fillAttacksfromSet(attacks, level_info.attackTypes, this_level_attacks);
		//second add the repeats
		if(level_info.levelRepeats()>0){
			//first add one for each previous Level
			for(int repeat_level=NoPhishLevelInfo.FIRST_REPEAT_LEVEL-1;repeat_level<level && attacks.size() < level_info.levelPhishes(); repeat_level++){
				fillAttacksfromSet(attacks, getLevelInfo(repeat_level).attackTypes, attacks.size()+1, true);
			}
			//then fill up repeats from random previous levels
			while(attacks.size() < level_info.levelPhishes()){
				//select a random earlier Level 
				//"-(FIRST_REPEAT_LEVEL-1)" is to prevent levels 0-2 from being repeated
				//"+1" is to prevent "repeating" the current level
				int random_level_offset = (getRandom().nextInt(level-(NoPhishLevelInfo.FIRST_REPEAT_LEVEL-1)))+1;
				int repeat_level = level-random_level_offset;
				fillAttacksfromSet(attacks, getLevelInfo(repeat_level).attackTypes, attacks.size()+1, true);
			}
		}
		//The rest are valid urls
		while(attacks.size() < level_info.levelCorrectURLs()){
			attacks.add(PhishAttackType.Keep);
		}

		return attacks;
	}

	private void fillAttacksfromSet(List<PhishAttackType> target, PhishAttackType[] set, int target_size){
		fillAttacksfromSet(target, set, target_size,false);
	}

	private void fillAttacksfromSet(List<PhishAttackType> target, PhishAttackType[] set, int target_size, boolean random){
		if(set.length==0){
			return;
		}

		if(random){
			//then fill up randomly
			while(target.size() < target_size){
				target.add(set[getRandom().nextInt(set.length)]);
			}			
		}else{
			//first try to add each once
			for(int i=0; i<set.length && target.size() < target_size; i++){
				target.add(set[i]);
			}
			fillAttacksfromSet(target, set, target_size,true);
		}

	}

	private void cancelTimer(){
		if(this.current_timer!=null){
			this.current_timer.cancel();
		}
	}
	
	@Override
	public void restartLevel(){
		cancelTimer();
		this.startLevel(this.getLevel());
	}
	
	@Override
	public void abortLevel(){
		cancelTimer();
		this.frontend.showMainMenu();
	}

    @Override
    public int getDoneURLs() {
        return this.progress.getDoneUrls();
    }

    @Override
	public void redirectToLevel1URL(){
		Random random = BackendControllerImpl.getInstance().getRandom();
		char[] buf=new char[4];
		for(int i=0;i<buf.length;i++){
			buf[i]=(char) ('a'+random.nextInt(26));
		}
		String random_string=new String(buf);
		this.getFrontend().startBrowser(Uri.parse(LEVEL1_URL+"?frag="+random_string+"#bottom-"+random_string));
	}

	public void nextUrl() {
		checkinited();
		if(getLevel() <= 1){
			//Level 0 and 1 do not have repeats
			throw new IllegalStateException("This function is not defined for level 0 and 1 as these do not need URLs");
		}

		PhishAttackType attack = this.level_attacks.remove(getRandom().nextInt(this.level_attacks.size()));

		PhishURL base_url;
		String before_url = "",after_url = "";
		Class<? extends BaseGenerator>[] level_generators = getLevelInfo().generators;
		int tries = Constants.ATTACK_RETRY_URLS;
		do{
			//First we choose a random start URL
			base_url=getPhishURL(PhishAttackType.NoPhish);
			//then we decorate the URL with a random generator
			Class<? extends BaseGenerator> random_generator=level_generators[getRandom().nextInt(level_generators.length)];
			base_url=AbstractUrlDecorator.decorate(base_url, random_generator);
			//Lastly we decorate the current url with one attack
			before_url=Arrays.toString(base_url.getParts());
			base_url=AbstractUrlDecorator.decorate(base_url,attack.getAttackClass());
			after_url=Arrays.toString(base_url.getParts());
			tries--;
		}while(	before_url.equals(after_url)
				&& tries >= 0
				&& attack != PhishAttackType.Keep
				&& attack != PhishAttackType.FindDomain
				&& attack != PhishAttackType.HTTP
				); //The attack might not change the URL so we try again.

		if(tries == -1){
			throw new IllegalStateException("Could not find attackable URL. Attack:"+attack.getAttackClass().getName());
		}

		this.current_url=base_url;
		
		int countdown = BackendControllerImpl.getInstance().getLevelInfo().getLevelTime();
        this.paused_timer = null;
		if( countdown == 0){
			this.current_timer = null;
		} else {
			this.current_timer = new PhishCountDownTimer(countdown*1000,1000);
			this.current_timer.start();
		}
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
		
		cancelTimer();
		
		PhishResult result=this.current_url.getResult(acceptance);
		//When we are in the HTTPS level we only accept https urls even if these are not attacks.
		if(getLevelInfo().hasAttack(PhishAttackType.HTTP) && !this.getUrl().getParts()[0].equals("https:")){
			if(acceptance){
				result=PhishResult.Phish_NotDetected;
			}else{
				result=PhishResult.Phish_Detected;
			}
		}
		if(showProof(result)){
		    this.frontend.showProofActivity();
		}else{
			this.addResult(result);
		}
		return result;
	}

	private void addResult(PhishResult result){
		this.progress.addResult(result);
		
		if(result == PhishResult.Phish_Detected && !BackendControllerImpl.getInstance().getLevelInfo().hasAttack(PhishAttackType.FindDomain)){
			this.progress.incProofRightInRow();
		}else if(result == PhishResult.Phish_NotDetected){
			this.progress.resetProofRightInRow();
		}
		
		int offset=this.current_url.getPoints(result);
		//with this function we ensure that the user gets more points per level
		//This ensures that there is no point in running the same level multiple times to collect points
		offset=getLevelInfo().weightLevelPoints(offset);
		if(!(offset<=0 && getLevelPoints() <= 0)){
			//don't display toast when not removing points
			this.getFrontend().displayToastScore(offset);
		}
		int new_levelpoints = this.getLevelPoints()+offset;
		if(new_levelpoints<=0){
			new_levelpoints=0;
		}
		this.progress.setLevelPoints(new_levelpoints);

		//if we did not correctly identify we have to readd.
		if(result != PhishResult.Phish_Detected && result != PhishResult.NoPhish_Detected){
			if(result == PhishResult.Phish_NotDetected || result == PhishResult.TimedOut || result == PhishResult.Guessed){
				progress.decLives();
			}
			this.level_attacks.add(current_url.getAttackType());
		}
		this.checkLevelState();
		frontend.resultView(result);
	}

	private void checkLevelState(){
		Levelstate newstate = getLevelState();
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
	
	@Override
	public boolean partClicked(int part) {
		checkinited();
		boolean clickedright = part == current_url.getDomainPart();
		if(clickedright){
			addResult(PhishResult.Phish_Detected);
		}else{
			addResult(PhishResult.Guessed);
		}
		return clickedright;
	}

	public int getTotalPoints(){
		checkinited();
		return this.progress.getTotalPoints();
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
    public void skipLevel1(){
        this.levelFinished(1);
    }

	private void levelFailed(int level){
		notifyLevelStateChangedListener(Levelstate.failed, level);
	}
	
	private void levelStarted(int level){
		notifyLevelStateChangedListener(Levelstate.running, level);
	}

	private void levelFinished(int level){
		if(getLevel()==level){
			this.progress.commitPoints();
		}
		this.progress.finishlevel(level);
		notifyLevelStateChangedListener(Levelstate.finished, level);
	}

	private void notifyLevelStateChangedListener(Levelstate newstate, int levelid){
		for(int i=0; i< onLevelstateChangeListeners.size(); i++){
			onLevelstateChangeListeners.get(i).onLevelstateChange(newstate, levelid);
		}
	}

	@Override
	public int getMaxUnlockedLevel() {
		return this.progress.getMaxUnlockedLevel();
	}
	
	@Override
	public int getMaxFinishedLevel(){
		return this.progress.getMaxFinishedLevel();
	}

	/*
	@Override
	public void signIn() {
		checkinited();

		if(!this.gamehelper.isSignedIn()){
			SharedPreferences.Editor editor = frontend.getBaseActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit();
	        editor.putBoolean(KEY_AUTO_SIGN_IN, true);
	        editor.commit();
			this.gamehelper.beginUserInitiatedSignIn();
		}
	}*/

	/*
	@Override
	public void signOut() {
		checkinited();

		SharedPreferences.Editor editor = frontend.getBaseActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit();
        editor.putBoolean(KEY_AUTO_SIGN_IN, false);
        editor.commit();
        this.gamehelper.signOut();
	}*/

	@Override
	public int getCorrectlyFoundURLs() {
		return progress.getLevelResults(PhishResult.Phish_Detected)+progress.getLevelResults(PhishResult.NoPhish_Detected);
	}

	@Override
	public Levelstate getLevelState() {
		int remaining_urls_to_identify = level_attacks.size();
		Levelstate result;
		if(this.getLifes() <= 0){
			result = Levelstate.failed;
		}else if(remaining_urls_to_identify <= 0){
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

	/*@Override
	public void onSignInFailed() {
		getFrontend().onSignInFailed();
	}
    */
    /*
	@Override
	public void onSignInSucceeded() {
		/*
		progress.onSignInSucceeded();
		getFrontend().onSignInSucceeded();
		Games.Achievements.unlock(gamehelper.getApiClient(), getFrontend().getContext().getResources().getString(R.string.achievement_welcome));
		*/
	//}*/

	/*
	@Override
	public GameHelper getGameHelper() {
		return this.gamehelper;
	}
	*/
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

	/*
	@Override
	public void deleteRemoteData() {
		this.progress.deleteRemoteData();
	}*/

	@Override
	public void addOnLevelstateChangeListener(OnLevelstateChangeListener listener) {
		if(!this.onLevelstateChangeListeners.contains(listener)){
			this.onLevelstateChangeListeners.add(listener);
		}
	}

	@Override
	public void removeOnLevelstateChangeListener(OnLevelstateChangeListener listener) {
		this.onLevelstateChangeListeners.remove(listener);
	}

    @Override
    public void clearOnLevelstateChangeListener() {
        this.onLevelstateChangeListeners.clear();
    }

    @Override
	public void addOnLevelChangeListener(OnLevelChangeListener listener) {
		if(!this.onLevelChangeListeners.contains(listener)){
			this.onLevelChangeListeners.add(listener);
		}
	}

	@Override
	public void removeOnLevelChangeListener(OnLevelChangeListener listener) {
		this.onLevelChangeListeners.remove(listener);
	}

    @Override
    public void clearOnLevelChangeListener() {
        this.onLevelChangeListeners.clear();
    }

    @Override
	public boolean getLevelCompleted(int level) {
		return level<=getMaxFinishedLevel();
	}

	@Override
	public Random getRandom() {
		if(this.random==null){
			this.random=new Random();
		}
		return random;
	}
	
	@Override
	public boolean showProof(PhishResult result) {
		if(getLevelInfo().hasAttack(PhishAttackType.FindDomain)){
			return true;
		}
		
		if(result != PhishResult.Phish_Detected){
			return false;
		}
		
		if(progress.getProofRightInRow() >= Constants.PROOF_IN_ROW){
			return this.random.nextInt(100) < Constants.RANDOM_PROOF_PERCENTAGE;
		}else{
			return true;
		}
	}

	@Override
	public FrontendController getFrontend() {
		return this.frontend;
	}

	@Override
	public void showSaveGames() {
		/*
		 android.content.Intent snapshotIntent = Games.Snapshots.getSelectSnapshotIntent(getGameHelper().getApiClient(), "Select a snap", true, true, 5);
		 getFrontend().getBaseActivity().startActivityForResult(snapshotIntent, 0);
		 */
	}

	@Override
	public int remainingSeconds() {
		if(this.current_timer==null){
			return -1;
		}else{
		  return (int)(this.current_timer.remaining/1000);
		}
	}

    private Long paused_timer=null;
    @Override
    public void pauseTimer() {
        if(this.current_timer!=null) {
            this.paused_timer=this.current_timer.remaining;
            this.current_timer.cancel();
        }
    }

    @Override
    public void resumeTimer() {
        if(this.paused_timer!=null) {
            this.current_timer = new PhishCountDownTimer(paused_timer, 1000);
            this.current_timer.start();
        }
    }

    @Override
	public int getLevelStars(int level) {
		return this.progress.getLevelStars(level);
	}


}
