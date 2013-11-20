package de.tudarmstadt.informatik.secuso.phishedu.backend;

import java.io.InputStream;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;

import java.util.Random;


import com.google.android.gms.appstate.AppStateClient;
import com.google.android.gms.games.GamesClient;
import com.google.example.games.basegameutils.GameHelper;
import com.google.gson.Gson;

import de.tudarmstadt.informatik.secuso.phishedu.backend.attacks.IPAttack;
import de.tudarmstadt.informatik.secuso.phishedu.backend.attacks.Level2Attack;
import de.tudarmstadt.informatik.secuso.phishedu.backend.attacks.PhishTankURLAttack;
import de.tudarmstadt.informatik.secuso.phishedu.backend.generator.KeepGenerator;
import de.tudarmstadt.informatik.secuso.phishedu.backend.generator.SudomainGenerator;
import de.tudarmstadt.informatik.secuso.phishedu.backend.networkTasks.*;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;

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
	private static final String LEVEL1_URL = "https://pages.no-phish.de/level1.php#bottom";
	private static final PhishAttackType[] CACHE_TYPES = {PhishAttackType.AnyPhish, PhishAttackType.NoPhish};
	//For each level we can define what Attacks are applied
	//LEVEL 0-1 are empty because they don't 
	@SuppressWarnings("rawtypes")
	private static Class[][] ATTACK_TYPES_PER_LEVEL = {
		{}, //Level0: Awareness
		{}, //Level1: Find URLBar in Browser
		{Level2Attack.class}, //Level2: Select Domain name
		{IPAttack.class},
		{IPAttack.class, PhishTankURLAttack.class},
	};
	@SuppressWarnings("rawtypes")
	private static Class[] GENERATORS = {
		SudomainGenerator.class, KeepGenerator.class, //Currently we use the same generators for all levels
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
		return urlCache[type.getValue()][random.nextInt(urlCache[type.getValue()].length)];
	}

	/**
	 * This holds the current URL returned by the last {@link BackendControllerInterface}{@link #getNextUrl()} call
	 */
	private PhishURLInterface current_url;

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
	public static BackendController getInstance(){
		return instance;
	}

	/**
	 * Check if the singleton is inited. If not it will throw a IllegalStateException;
	 */
	private static void checkinited(){
		if(instance == null || !(instance.inited)){
			throw new IllegalStateException("initialize me first! Call backendcontroller.init()");
		}
	}

	public void init(FrontendControllerInterface frontend, GameHelper gamehelper){
		this.frontend=frontend;
		this.gamehelper=gamehelper;
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

	private void checkInitDone(){
		//This means we already are initialized
		if(this.inited){
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

	@Override
	public void startLevel(int level) {
		checkinited();
		this.progress.setLevel(level);
		this.frontend.onLevelChange(this.progress.getLevel());
	}
	
	@Override
	public void restartLevel(){
		this.startLevel(this.getLevel());
	}

	@Override
	public void redirectToLevel1URL(){
		this.frontend.startBrowser(Uri.parse(LEVEL1_URL));
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public String[] getNextUrl() {
		checkinited();
		if(getLevel() <= 1){
			//Level 0 and 1 do not have repeats
			throw new IllegalStateException("This function is not defined for level 0 and 1 as these do not need URLs");
		}
		int remaining_urls = this.levelURLs()-this.progress.getDoneUrls();
		int remaining_phish = this.levelPhishes()-this.progress.getPresentedPhish();
		int remaining_phish_to_level = this.nextLevelPhishes() - this.progress.getDetectedPhish();
		int remaining_repeats = this.levelRepeats() - this.progress.getPresentedRepeats();
		//We might have failed the level
		//Either by going out of URLs or by going out of options to detect phish 
		if( remaining_urls <= 0 || remaining_phish<remaining_phish_to_level){
			this.frontend.levelFailed(this.getLevel());
		}

		//we have to decide whether we want to present a phish or not
		boolean present_phish=false;
		if(remaining_phish > 0){
			present_phish=random.nextBoolean();
		}
		//we save up one phish for the last two URLs
		if(remaining_phish == 1 && remaining_urls>2){
			present_phish=false;
		}
		//if we did not display the saved phish in the second last try we have to do it in the last
		if(remaining_phish == 1 && remaining_urls == 1){
			present_phish=true;
		}
		
		//First we choose a random start URL
		PhishURLInterface base_url=getPhishURL(PhishAttackType.NoPhish);
		//then we decorate the URL with a generator
		base_url=decorateUrl(base_url, GENERATORS[random.nextInt(GENERATORS.length)], getLevel());
		//Lastly we might apply a attack
		if(present_phish){
			boolean present_repeat = false;
			if(remaining_urls <= remaining_repeats){
				//we have to present a repeat
				present_repeat = true;
			}else if( remaining_repeats > 0 ){
				//we might present a repeat
				present_repeat = random.nextBoolean();
			}
			Class<? extends AbstractUrlDecorator> attack;
			int index_level = Math.min(getLevel(), ATTACK_TYPES_PER_LEVEL.length-1);
			if(present_repeat){
				index_level = random.nextInt(index_level-1);
				//level 0 and 1 do not have attacks
				index_level = Math.max(2, index_level);
			}
			Class<? extends AbstractUrlDecorator>[] level_attacks = ATTACK_TYPES_PER_LEVEL[index_level];
			attack=level_attacks[random.nextInt(level_attacks.length)];
			
			base_url=decorateUrl(base_url, attack, getLevel());
		}

		this.current_url=base_url;
		return getUrl();
	}
	
	
	private int levelRepeats(){
		return (int) Math.ceil(this.levelPhishes()/3);
	}

	@Override
	public String[] getUrl(){
		return (String[]) this.current_url.getParts();
	}

	private PhishURLInterface decorateUrl(PhishURLInterface base_url, Class<? extends AbstractUrlDecorator> decorator, int level){
		try {
			base_url=decorator.getConstructor(PhishURLInterface.class).newInstance(base_url);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return base_url;
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
			this.changePoints(result);
		}
		return result;
	}

	private void changePoints(PhishResult result){
		this.progress.addResult(result);
		int offset=this.current_url.getPoints(result);
		//with this function we ensure that the user gets more points per level
		//This ensures that there is no point in running the same level multiple times to collect points
		offset*=Math.pow(LEVEL_DISTANCE, this.getLevel());
		this.progress.setPoints(this.getPoints()+offset);
	}

	/**
	 * This is only for testing
	 * @param level
	 */
	public void setLevel(int level){
		this.progress.setLevel(level);
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
			changePoints(PhishResult.Phish_Detected);
			if(this.foundPhishes()>= this.nextLevelPhishes()){
				this.frontend.levelFinished(this.getLevel());
			}
		}else{
			changePoints(PhishResult.Phish_NotDetected);
		}
		return clickedright;
	}

	public int getPoints(){
		checkinited();
		return this.progress.getPoints();
	}

	public void onUrlReceive(Uri data){
		checkinited();
		if(data == null){
			return;
		}
		String host = data.getHost();
		if(host.equals("maillink")){
			this.frontend.levelFinished(0);
		}else if(host.equals("level1finished")){
			this.frontend.levelFinished(1);
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
		this.gamehelper.beginUserInitiatedSignIn();
	}

	@Override
	public void signOut() {
		checkinited();
		this.gamehelper.signOut();
	}

	@Override
	public int levelURLs() {
		checkinited();
		return 10+(2*this.getLevel());
	}

	@Override
	public int levelPhishes() {
		checkinited();
		return levelURLs()/2;
	}

	@Override
	public int doneURLs() {
		checkinited();
		return this.progress.getDoneUrls();
	}

	@Override
	public int foundPhishes() {
		checkinited();
		return this.progress.getDetectedPhish();
	}

	@Override
	public int nextLevelPhishes() {
		checkinited();
		return levelPhishes()-2;
	}
}
