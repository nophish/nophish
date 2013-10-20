package de.tudarmstadt.informatik.secuso.phishedu.backend;

import java.io.InputStream;
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
	private static final String PREFS_NAME = "PhisheduState";
	private static final String URL_CACHE_NAME ="urlcache";
	private static final String LEVEL1_URL = "https://pages.no-phish.de/level1.php#bottom";
	private static final PhishAttackType[] CACHE_TYPES = {PhishAttackType.AnyPhish, PhishAttackType.NoPhish};
	//the probability that we apply an Attack on each round
	private static final double ATTACK_THRESHOULD = 0.5;
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
	private static Class[][] GENERATORS_PER_LEVEL = {
		{SudomainGenerator.class, KeepGenerator.class}, //Currently we use the same generators for all levels
	};
	private static final int POINTS_PER_LEVEL = 100;
	private static final int URL_CACHE_SIZE = 100;
	
	private static PhishURL[] deserializeURLs(String serialized){
		return new Gson().fromJson(serialized, PhishURL[].class);
	}
	
	private static String serializeURLs(PhishURLInterface[] object){
		return new Gson().toJson(object);
	}
	
	private static BackendController instance = new BackendController();
	
	private FrontendControllerInterface frontend;
	private GameHelper gamehelper;
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
		  this.urlCache[type.getValue()]=loadUrls(url_cache, type);
		}
	}
	
	private void CacheUrls(SharedPreferences cache, PhishAttackType type, PhishURLInterface[] urls){
		SharedPreferences.Editor editor = cache.edit();
		editor.putString(type.toString(), serializeURLs(urls));
		editor.commit();
	}
	
	private PhishURLInterface[] loadUrls(SharedPreferences cache, PhishAttackType type){
		//first we load the cached value if available
		ArrayList<PhishURLInterface> result = new ArrayList<PhishURLInterface>();
		PhishURL[] urls=deserializeURLs(cache.getString(type.toString(), "[]"));
		//If the values are still empty we load the factory defaults 
		if(urls.length==0){
			int resource = frontend.getContext().getResources().getIdentifier(type.toString().toLowerCase(Locale.US), "raw", frontend.getContext().getPackageName());
			InputStream input = frontend.getContext().getResources().openRawResource(resource);
			String json = new Scanner(input,"UTF-8").useDelimiter("\\A").next();
			urls = (new Gson()).fromJson(json, PhishURL[].class);
		}
		for (PhishURL phishURL : urls) {
			result.add(phishURL);
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
		//First we choose a random start URL
		PhishURLInterface base_url=getPhishURL(PhishAttackType.NoPhish);
		//then we decorate the URL with a generator
		base_url=decorateUrl(base_url, GENERATORS_PER_LEVEL, getLevel());
		//Lastly we might apply a attack
		if(new Random().nextFloat()>ATTACK_THRESHOULD){
			base_url=decorateUrl(base_url, ATTACK_TYPES_PER_LEVEL, getLevel());
		}
		
		this.current_url=base_url;
		return (String[]) this.current_url.getParts();
	}
	
	private PhishURLInterface decorateUrl(PhishURLInterface base_url, Class<? extends AbstractUrlDecorator>[][] options, int level){
		Random rand=new Random();
		int use_level=Math.min(level, options.length-1);
		Class<? extends AbstractUrlDecorator>[] attacks = options[use_level];
		Class<? extends AbstractUrlDecorator> attack = attacks[rand.nextInt(attacks.length)];
		try {
			base_url=attack.getConstructor(PhishURLInterface.class).newInstance(base_url);
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
	
	public void onUrlReceive(Uri data){
		if(data == null){
			return;
		}
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
}
