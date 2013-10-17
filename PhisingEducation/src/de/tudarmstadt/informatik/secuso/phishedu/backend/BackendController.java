package de.tudarmstadt.informatik.secuso.phishedu.backend;

import java.util.ArrayList;
import java.util.Random;


import com.google.gson.Gson;

import de.tudarmstadt.informatik.secuso.phishedu.backend.attacks.IPAttack;
import de.tudarmstadt.informatik.secuso.phishedu.backend.attacks.Level2Attack;
import de.tudarmstadt.informatik.secuso.phishedu.backend.attacks.PhishTankURLAttack;
import de.tudarmstadt.informatik.secuso.phishedu.backend.networkTasks.*;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;

/**
 * This is the main backend logik.
 * It is implemented as static singleton to keep state while changing activities.
 * @author Clemens Bergmann <cbergmann@schuhklassert.de>
 *
 */
public class BackendController extends BroadcastReceiver implements BackendControllerInterface, GameStateLoadedListener, UrlsLoadedListener {
	private static final String PREFS_NAME = "PhisheduState";
	private static final String URL_CACHE_NAME ="urlcache";
	private static final String LEVEL1_URL = "https://pages.no-phish.de/level1.php#bottom";
	private static final PhishAttackType[] CACHE_TYPES = {PhishAttackType.AnyPhish, PhishAttackType.NoPhish};
	//the probability that we apply an Attack on each round
	private static final double ATTACK_THRESHOULD = 0.5;
	//For each level we can define what Attacks are applied
	//LEVEL 0-1 are empty because they don't 
	@SuppressWarnings("rawtypes")
	private static final Class[][] ATTACK_TYPES_PER_LEVEL = {
		{}, //Level0: Awareness
		{}, //Level1: Find URLBar in Browser
		{Level2Attack.class}, //Level2: Select Domain name
		{IPAttack.class},
		{IPAttack.class, PhishTankURLAttack.class},
	};
	private static final int POINTS_PER_LEVEL = 100;
	private static final int URL_CACHE_SIZE = 100;
	
	private static PhishURL deserializeURL(String serialized){
		return new Gson().fromJson(serialized, PhishURL.class);
	}
	
	private static String serializeURL(PhishURLInterface object){
		return new Gson().toJson(object);
	}
	
	private static BackendController instance = new BackendController();
	
	private FrontendControllerInterface frontend;
	private boolean inited = false;
	
	//indexed by UrlType
	private PhishURLInterface[][] urlCache;
	
	private boolean gamestate_loaded = false;
	private GameProgress progress;
	
	/**
	 * This function returns a Phishing url of the given type
	 * @param type Type of Attack for the URL
	 * @return A PhishURL of the given type
	 */
	public PhishURLInterface getPhishURL(PhishAttackType type){
		if(type.getValue() >= this.urlCache.length){
			throw new IllegalArgumentException("This phish type is not cached.");
		}
		Random rand = new Random();
		return urlCache[type.getValue()][rand.nextInt(urlCache[type.getValue()].length)];
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
	
	public void init(FrontendControllerInterface frontend){
		this.frontend=frontend;
		this.progress = new GameProgress(this.frontend.getContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE), this.frontend.getGamesClient(),this.frontend.getAppStateClient(),this);
		SharedPreferences url_cache = this.frontend.getContext().getSharedPreferences(URL_CACHE_NAME, Context.MODE_PRIVATE);
		for(PhishAttackType type: CACHE_TYPES){
		  this.urlCache[type.getValue()]=loadUrls(url_cache, type);
		}
	}
	
	private void CacheUrls(SharedPreferences cache, PhishAttackType type, PhishURLInterface[] urls){
		SharedPreferences.Editor editor = cache.edit();
		for(int i=0;i<urls.length;i++){
			editor.putString(type.toString()+"["+i+"]", serializeURL(urls[i]));
		}
		editor.commit();
	}
	
	private PhishURLInterface[] loadUrls(SharedPreferences cache, PhishAttackType type){
		//first we load the cached value if available
		ArrayList<PhishURLInterface> result = new ArrayList<PhishURLInterface>();
		for(int i=0; cache.contains(type.toString()+"["+i+"]"); i++){
			result.add(deserializeURL(cache.getString(type.toString()+"["+i+"]", "")));
		}
		//then we get the value from the online store
		new GetUrlsTask(this).execute(URL_CACHE_SIZE,type.getValue());
		return result.toArray(new PhishURLInterface[0]);
	}
	
	public void urlsReturned(PhishURLInterface[] urls, PhishAttackType type){
		this.urlCache[type.getValue()]=urls;
		this.CacheUrls(this.frontend.getContext().getSharedPreferences(URL_CACHE_NAME, Context.MODE_PRIVATE),type, this.urlCache[type.getValue()]);
		this.checkInitDone();
	}
	
	public void urlDownloadProgress(int percent){
		this.frontend.initProgress(percent);
	}
	
	private void checkInitDone(){
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
	public void StartLevel(int level) {
		checkinited();
		if(level == 1){
			this.frontend.startBrowser(Uri.parse(LEVEL1_URL));
		}else{
			this.progress.setPoints(0);
			this.progress.setLevel(level);
			this.frontend.onLevelChange(this.progress.getLevel());
		}
	}


	@SuppressWarnings("unchecked")
	@Override
	public String[] getNextUrl() {
		checkinited();
		Random rand=new Random();
		//First we decide if we want to give a phish URL or not
		PhishURLInterface base_url=this.urlCache[PhishAttackType.NoPhish.getValue()][rand.nextInt(this.urlCache[PhishAttackType.NoPhish.getValue()].length)];
		if(rand.nextFloat()>ATTACK_THRESHOULD){
			int use_level=(this.getLevel() < ATTACK_TYPES_PER_LEVEL.length) ? this.getLevel() : ATTACK_TYPES_PER_LEVEL.length-1;
			Class<? extends PhishURLInterface>[] attacks = ATTACK_TYPES_PER_LEVEL[use_level];
			Class<? extends PhishURLInterface> attack = attacks[rand.nextInt(attacks.length)];
			try {
				base_url=attack.getConstructor(PhishURLInterface.class).newInstance(base_url);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		this.current_url=base_url;
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
			this.changePoints(result);
		}
		return result;
	}
	
	private void changePoints(PhishResult result){
		this.progress.addResult(result);
		int offset=this.current_url.getPoints(result);
		this.progress.setPoints(this.getPoints()+offset);
		if(this.progress.getPoints()>=nextLevelPoints()){
			this.proceedlevel();
		}
	}
	
	private void proceedlevel(){
		this.StartLevel(this.getLevel()+1);
	}
	
	/**
	 * This is only for testing
	 * @param level
	 */
	public void setLevel(int level){
		this.progress.setLevel(level);
	}
	
	private int nextLevelPoints(){
		return POINTS_PER_LEVEL;
	}

	@Override
	public PhishSiteType getType() {
		checkinited();
		return this.current_url.getSiteType();
	}

	@Override
	public boolean partClicked(int part) {
		checkinited();
		boolean clickedright = this.current_url.partClicked(part);
		if(clickedright){
			changePoints(PhishResult.Phish_Detected);
		}else{
			changePoints(PhishResult.Phish_NotDetected);
		}
		return clickedright;
	}

	public int getPoints(){
		checkinited();
		return this.progress.getPoints();
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
		Uri data = intent.getData();
		this.onUrlReceive(data);
	}
	
	public void onUrlReceive(Uri data){
		if(data.getHost()=="maillink"){
			this.proceedlevel();
			this.frontend.MailReturned();
		}else if(data.getHost()=="level1finished"){
			this.proceedlevel();
			this.frontend.level1Finished();
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
	
}
