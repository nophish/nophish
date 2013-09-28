package de.tudarmstadt.informatik.secuso.phishedu.backend;

import java.util.LinkedHashMap;

import de.tudarmstadt.informatik.secuso.phishedu.backend.networkTasks.*;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class BackendController extends BroadcastReceiver implements BackendControllerInterface {
	private static final String PREFS_NAME = "PhisheduState";
	private static BackendController instance = new BackendController();
	private static final String LEVEL1_URL = "http://clemens.schuhklassert.de";
	
	private FrontendControllerInterface frontend;
	private boolean inited = false;
	private String[] urlCache;
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
		this.progress = new GameProgress(this.frontend.getMasterActivity().getSharedPreferences(PREFS_NAME, 0), this.frontend.getGamesClient());
		new GetUrlsTask(instance).execute(100);		
	}
	
	public void urlsReturned(String[] urls){
		this.urlCache=urls;
		this.inited=true;
		this.progress.StartFinished();
		this.frontend.initDone();
	}
	
	public void urlDownloadProgress(int percent){
		this.frontend.initProgress(percent);
	}
	
	public void sendMail(String from, String to, String usermessage){
		checkinited();
		new SendMailTask(from, to, usermessage).execute();
	}

	@Override
	public void StartLevel1() {
		checkinited();
		Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(LEVEL1_URL));
		this.frontend.getContext().startActivity(browserIntent);
	}


	@Override
	public String[] getNextUrl() {
		checkinited();
		// TODO We have to implement a smart way of generating the URLs. For now we just give out the first cachced one.
		this.current_parts.clear();
		
		String[] parts = this.urlCache[0].split("/");
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
		if(result == PhishResult.Phish_Detected){
			this.progress.IncDetectedPhish();
		}
		int offset=this.current_points[result.getValue()];
		this.setPoints(this.getPoints()+offset);
	}
	
	private void setPoints(int points){
		this.progress.setPoints(points);
		if(this.progress.getPoints()>=nextLevelPoints()){
			this.progress.setLevel(this.progress.getLevel()+1);
			this.progress.setPoints(0);
			this.frontend.onLevelChange(this.progress.getLevel());
		}
		this.progress.saveOnline();
	}
	
	private void proceedlevel(){
		this.setPoints(nextLevelPoints());
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
			this.frontend.MailReturned();
		}else if(data.getHost()=="level1phinished"){
			this.proceedlevel();
			this.frontend.level1Finished();
		}
	}
	
}
