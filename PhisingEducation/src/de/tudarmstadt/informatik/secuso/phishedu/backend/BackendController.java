package de.tudarmstadt.informatik.secuso.phishedu.backend;

import java.util.ArrayList;
import java.util.Random;


import com.google.gson.Gson;

import de.tudarmstadt.informatik.secuso.phishedu.backend.attacks.IPAttack;
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
	private static PhishURL deserializeURL(String serialized){
		return new Gson().fromJson(serialized, PhishURL.class);
	}
	
	private static String serializeURL(PhishURL object){
		return new Gson().toJson(object);
	}
	
	private static final String PREFS_NAME = "PhisheduState";
	private static final String URL_CACHE_NAME ="urlcache";
	private static final String LEVEL1_URL = "https://pages.no-phish.de/level1.php";
	private static PhishAttackType[] cacheTypes = {PhishAttackType.AnyPhish, PhishAttackType.NoPhish};
	
	@SuppressWarnings("rawtypes")
	private static final Class[][] phishtypesPerLevel = {
		{IPAttack.class},
		{IPAttack.class, PhishTankURLAttack.class},
	};
	
	private static BackendController instance = new BackendController();
	
	private FrontendControllerInterface frontend;
	private boolean inited = false;
	
	//indexed by UrlType
	private PhishURL[][] urlCache;
	
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
		this.urlCache=new PhishURL[cacheTypes.length][];
		for(PhishAttackType type: cacheTypes){
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
		if(instance == null || !(instance.inited)){
			throw new IllegalStateException("initialize me first! Call backendcontroller.init()");
		}
	}
	
	public void init(FrontendControllerInterface frontend){
		this.frontend=frontend;
		this.progress = new GameProgress(this.frontend.getContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE), this.frontend.getGamesClient(),this.frontend.getAppStateClient(),this);
		SharedPreferences url_cache = this.frontend.getContext().getSharedPreferences(URL_CACHE_NAME, Context.MODE_PRIVATE);
		for(PhishAttackType type: cacheTypes){
		  this.urlCache[type.getValue()]=loadUrls(url_cache, type);
		}
	}
	
	private void CacheUrls(SharedPreferences cache, PhishAttackType type, PhishURL[] urls){
		SharedPreferences.Editor editor = cache.edit();
		for(int i=0;i<urls.length;i++){
			editor.putString(type.toString()+"["+i+"]", serializeURL(urls[i]));
		}
		editor.commit();
	}
	
	private PhishURL[] loadUrls(SharedPreferences cache, PhishAttackType type){
		//first we load the cached value if available
		ArrayList<PhishURL> result = new ArrayList<PhishURL>();
		for(int i=0; cache.contains(type.toString()+"["+i+"]"); i++){
			result.add(deserializeURL(cache.getString(type.toString()+"["+i+"]", "")));
		}
		//then we get the value from the online store
		new GetUrlsTask(this).execute(100,type.getValue());
		return result.toArray(new PhishURL[0]);
	}
	
	public void urlsReturned(String[] urls, PhishAttackType type){
		this.urlCache[type.getValue()]=new PhishURL[urls.length];
		for(int i=0;i<urls.length;i++){
			this.urlCache[type.getValue()][i]=deserializeURL(urls[i]);
		}
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
	public void StartLevel1() {
		checkinited();
		this.frontend.startBrowser(Uri.parse(LEVEL1_URL));
	}


	@SuppressWarnings("unchecked")
	@Override
	public String[] getNextUrl() {
		checkinited();
		Random rand=new Random();
		//First we decide if we want to give a phish URL or not
		PhishURLInterface base_url=this.urlCache[PhishAttackType.NoPhish.getValue()][rand.nextInt(this.urlCache[PhishAttackType.NoPhish.getValue()].length)];
		if(rand.nextFloat()>0.5){
			int use_level=(this.getLevel() < phishtypesPerLevel.length) ? this.getLevel() : phishtypesPerLevel.length-1;
			Class<? extends PhishURLInterface>[] attacks = phishtypesPerLevel[use_level];
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
		this.progress.setPoints(0);
		this.progress.setLevel(this.progress.getLevel()+1);
		this.frontend.onLevelChange(this.progress.getLevel());
	}
	
	private int nextLevelPoints(){
		return 100;
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
	
}
