package de.tudarmstadt.informatik.secuso.phishedu.backend;

import java.util.ArrayList;

import com.google.gson.Gson;

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
	public interface PhishURLInterface{
		
	}
	/**
	 * this is for internally holding the phishing urls
	 * @author Clemens Bergmann <cbergmann@schuhklassert.de>
	 *
	 */
	private class PhishURL{
		private String[] parts = new String[0];
		private PhishSiteType siteType = PhishSiteType.AnyPhish;
		private PhishAttackType attackType = PhishAttackType.NoPhish;
		private int[] correctparts = new int[0];
		/**
		 * This stores the points the user gets for his detection.
		 * It is indexed acording to {@link PhishResult}.
		 */
		private int[] points = {15,0,-10,0};
	}
	
	
	private static PhishURL deserializeURL(String serialized){
		return new Gson().fromJson(serialized, PhishURL.class);
	}
	
	private static String serializeURL(PhishURL object){
		return new Gson().toJson(object);
	}
	
	private static final String PREFS_NAME = "PhisheduState";
	private static final String URL_CACHE_NAME ="urlcache";
	private static final String LEVEL1_URL = "https://pages.no-phish.de/level1.php";
	
	private static BackendController instance = new BackendController();
	
	private FrontendControllerInterface frontend;
	private boolean inited = false;
	
	//indexed by UrlType
	private PhishURL[][] urlCache = new PhishURL[2][];
	
	private boolean gamestate_loaded = false;
	private GameProgress progress;
	
	/**
	 * This holds the current URL returned by the last {@link BackendControllerInterface}{@link #getNextUrl()} call
	 */
	private PhishURL current_url;
	
	/**
	 * Private constructor for singelton.
	 */
	private BackendController() {}
	
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
		if(instance == null || !(instance.inited)){
			throw new IllegalStateException("initialize me first! Call backendcontroller.init()");
		}
	}
	
	public void init(FrontendControllerInterface frontend){
		this.frontend=frontend;
		this.progress = new GameProgress(this.frontend.getContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE), this.frontend.getGamesClient(),this.frontend.getAppStateClient(),this);
		SharedPreferences url_cache = this.frontend.getContext().getSharedPreferences(URL_CACHE_NAME, Context.MODE_PRIVATE);
		PhishAttackType[] types = {PhishAttackType.AnyPhish, PhishAttackType.NoPhish};
		for(PhishAttackType type: types){
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
		new GetUrlsTask(instance).execute(100,type.getValue());
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
		if (this.urlCache[GetUrlsTask.PHISH_URLS].length >0 && this.urlCache[GetUrlsTask.VALID_URLS].length > 0 &&  this.gamestate_loaded){
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


	@Override
	public String[] getNextUrl() {
		checkinited();
		// TODO We have to implement a smart way of generating the URLs. For now we just give out the first cachced one.
		this.current_url=this.urlCache[GetUrlsTask.PHISH_URLS][0];
		return (String[]) this.current_url.parts;
	}

	@Override
	public int getLevel() {
		checkinited();
		return this.progress.getLevel();
	}

	@Override
	public PhishResult userClicked(boolean acceptance) {
		checkinited();
		PhishResult result;
		if(this.current_url.attackType == PhishAttackType.NoPhish || acceptance){
			result =  PhishResult.NoPhish_Detected;
		}else if(this.current_url.attackType == PhishAttackType.NoPhish || !acceptance){
			result =  PhishResult.NoPhish_NotDetected;
		}else if(this.current_url.attackType != PhishAttackType.NoPhish || acceptance){
			result =  PhishResult.Phish_NotDetected;
		}else if(this.current_url.attackType != PhishAttackType.NoPhish || !acceptance){
			result =  PhishResult.Phish_Detected;
		}else {
			throw new IllegalStateException("Something went horrably wrong! We should not be here.");
		}
		
		if(result != PhishResult.Phish_Detected){
			this.changePoints(result);
		}
		return result;
	}
	
	private void changePoints(PhishResult result){
		this.progress.addResult(result);
		int offset=this.current_url.points[result.getValue()];
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
		return this.current_url.siteType;
	}

	@Override
	public boolean partClicked(int part) {
		checkinited();
		boolean clickedright = false;
		for(int correctpart:  this.current_url.correctparts){
			if(correctpart==part){
				clickedright=true;
				break;
			}
		}
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
