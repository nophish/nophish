package de.tudarmstadt.informatik.secuso.phishedu.backend;

import com.google.android.gms.games.GamesClient;

import android.content.SharedPreferences;

public class GameProgress {
	private SharedPreferences local_store;
	private GamesClient remote_store;
	private static final String DETECTED_PHISH =  "detectedPhish";
	private static final String POINTS =  "points";
	private static final String LEVEL =  "level";
	private static final String FIRST_START =  "firstStart";

	public GameProgress(SharedPreferences local_store, GamesClient remote_store) {
		this.local_store=local_store;
		this.remote_store=remote_store;
	}

	public int DetectedPhish(){
		return this.local_store.getInt(DETECTED_PHISH, 0);
	}

	public void IncDetectedPhish(){
		this.local_store.edit().putInt(DETECTED_PHISH, DetectedPhish()+1).commit();
	}
	
	public int getPoints(){
		return this.local_store.getInt(POINTS, 0);
	}
	public void setPoints(int points){
		this.local_store.edit().putInt(POINTS, points).commit();
	}
	
	public int getLevel() {
		return this.local_store.getInt(LEVEL, 1);
	}
	public void setLevel(int level){
		this.local_store.edit().putInt(LEVEL, level).commit();
	}

	public void StartFinished(){
		if (this.local_store.getBoolean(FIRST_START, true)){
		  this.local_store.edit().putBoolean(FIRST_START, false);
		  saveOnline();
		}
	}
	
	public void saveOnline(){
		if(this.remote_store.isConnected()){
			
			//welcome
			//Search and rescue (level1 finished)
			//know your poison (level2 finished)
			
		}
	}
}
