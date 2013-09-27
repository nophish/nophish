package de.tudarmstadt.informatik.secuso.phishedu.backend;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.SortedMap;
import java.util.TreeMap;

import de.tudarmstadt.informatik.secuso.phishedu.backend.networkTasks.*;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;

public class BackendController extends BroadcastReceiver implements BackendControllerInterface {
	private static final String PREFS_NAME = "PhisheduState";
	private static BackendController instance = new BackendController();
	
	private FrontendControllerInterface frontend;
	private boolean inited = false;
	private String[] urlCache;
	private SharedPreferences settings;
	
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
	
	private void setPoints(int points){
		this.settings.edit().putInt("points", points).commit();
	}
	
	private void setLevel(int level){
		this.settings.edit().putInt("level", level).commit();
	}
	
	public void init(FrontendControllerInterface frontend){
		this.frontend=frontend;
		this.settings = this.frontend.getMasterActivity().getSharedPreferences(PREFS_NAME, 0);
		new GetUrlsTask(instance).execute(100);		
	}
	
	public void urlsReturned(String[] urls){
		this.urlCache=urls;
		this.inited=true;
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
		Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://clemens.schuhklassert.de"));
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
		return this.settings.getInt("level", 0);
	}

	@Override
	public PhishResult userClicked(boolean acceptance) {
		checkinited();
		if(this.current_type == PhishType.NoPhish || acceptance){
			return PhishResult.NoPhish_Detected;
		}else if(this.current_type == PhishType.NoPhish || !acceptance){
			return PhishResult.NoPhish_NotDetected;
		}else if(this.current_type != PhishType.NoPhish || acceptance){
			this.setPoints(this.getPoints()+this.current_points[PhishResult.Phish_NotDetected.getValue()]);
			return PhishResult.Phish_NotDetected;
		}else if(this.current_type != PhishType.NoPhish || !acceptance){
			return PhishResult.Phish_Detected;
		}else {
			throw new IllegalStateException("Something went horrably wrong! We should not be here.");
		}
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
			this.setPoints(this.getPoints()+this.current_points[PhishResult.Phish_Detected.getValue()]);
		}
		return clickedright;
	}


	@Override
	public boolean isLevelUp() {
		checkinited();
		// TODO Auto-generated method stub
		return false;
	}

	public int getPoints(){
		checkinited();
		return this.settings.getInt("points", 0);
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
		Uri data = intent.getData();	
		if(data.getHost()=="maillink"){
			this.frontend.MailReturned();
		}else if(data.getHost()=="level1phinished"){
			this.frontend.level1Finished();
		}
	}
	
}
