package de.tudarmstadt.informatik.secuso.phishedu.backend;

import java.util.LinkedHashMap;

import de.tudarmstadt.informatik.secuso.phishedu.backend.networkTasks.*;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * This is the main backend logik.
 * It is implemented as static singleton to keep state while changing activities.
 * @author Clemens Bergmann <cbergmann@schuhklassert.de>
 *
 */
public class BackendController extends BroadcastReceiver implements BackendControllerInterface, GameStateLoadedListener, UrlsLoadedListener {
	private static final String PREFS_NAME = "PhisheduState";
	private static final String LEVEL1_URL = "http://api.no-phish.de/level1.php";
	
	private static BackendController instance = new BackendController();
	
	private FrontendControllerInterface frontend;
	private boolean inited = false;
	
	//indexed by PhishUrlsTask constants
	private String[][] phishURLCache = new String[2][];
	
	private boolean gamestate_loaded = false;
	private GameProgress progress;
	
	/**
	 * This field saves the type of Phish for the current url.
	 */
	private PhishType current_type = PhishType.NoPhish;
	/**
	 * This field saves the current URL Parts. The boolean indicates which Part is the one the user should click.
	 */
	private LinkedHashMap<String, Boolean> current_parts = new LinkedHashMap<String, Boolean>();
	/**
	 * This stores the points the user gets for his detection.
	 * It is indexed acording to {@link PhishResult}.
	 */
	private int[] current_points = {15,0,-10,0};
	
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
		new GetUrlsTask(instance).execute(100,GetUrlsTask.PHISH_URLS);		
		new GetUrlsTask(instance).execute(100,GetUrlsTask.VALID_URLS);
	}
	
	public void urlsReturned(String[] urls, int type){
		this.phishURLCache[type]=urls;
		this.checkInitDone();
	}
	
	public void urlDownloadProgress(int percent){
		this.frontend.initProgress(percent);
	}
	
	private void checkInitDone(){
		if(this.phishURLCache[GetUrlsTask.PHISH_URLS] != null && this.phishURLCache[GetUrlsTask.VALID_URLS] != null &&  this.gamestate_loaded){
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
		this.current_parts.clear();
		
		String[] parts = this.phishURLCache[GetUrlsTask.PHISH_URLS][0].split("/");
		for (String part : parts) {
			this.current_parts.put(part, true);
		}
		return (String[]) this.current_parts.keySet().toArray();
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
		if(this.current_type == PhishType.NoPhish || acceptance){
			result =  PhishResult.NoPhish_Detected;
		}else if(this.current_type == PhishType.NoPhish || !acceptance){
			result =  PhishResult.NoPhish_NotDetected;
		}else if(this.current_type != PhishType.NoPhish || acceptance){
			result =  PhishResult.Phish_NotDetected;
		}else if(this.current_type != PhishType.NoPhish || !acceptance){
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
		int offset=this.current_points[result.getValue()];
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
	public PhishType getType() {
		checkinited();
		return this.current_type;
	}

	@Override
	public boolean partClicked(int part) {
		checkinited();
		if(part < 0 || part >= this.current_parts.size()){
			throw new IllegalArgumentException("Invalid part number");
		}
		boolean clickedright = (Boolean) this.current_parts.values().toArray()[part];
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
