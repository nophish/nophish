package de.tudarmstadt.informatik.secuso.phishedu.backend;

import java.util.Arrays;

import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

class SaveGame{
	// serialization format version
    private static final String SERIAL_VERSION = "1.1";
   
	public int[] results = {0,0,0,0};
	public int[] levelPoints = new int[BackendControllerImpl.getInstance().getLevelCount()];
	public int level = 0;
	public int finishedLevel = -1;
	public int detected_phish_behind = 0;
	public boolean app_started = false;
	public int points = 0;
		
	public SaveGame(){
		zero();
	}
	/** Constructs a SaveGame object from serialized data. */
    public SaveGame(byte[] data) {
    	super();
        if (data == null || data.length==0) return; // default progress
        loadFromJson(new String(data));
    }

    /** Constructs a SaveGame object from a JSON string. */
    public SaveGame(String json) {
    	super();
        if (json == null) return; // default progress
        loadFromJson(json);
    }

    /** Constructs a SaveGame object by reading from a SharedPreferences. */
    public SaveGame(SharedPreferences sp, String key) {
    	super();
        loadFromJson(sp.getString(key, ""));
    }
    
	private SaveGame loadFromJson(String state){
		SaveGame result = null;
		try {
			result = (new Gson()).fromJson(state,SaveGame.class);
		} catch (JsonSyntaxException e) {
			//Json SyntaxException
		}
		return result;
	}
	
	public byte[] toBytes() {
        return toString().getBytes();
    }
	
	public String toString(){
		return new Gson().toJson(this);
	}
	
	public SaveGame unionWith(SaveGame other) {
		//Current resolving strategy: get the most of all values
		SaveGame union = clone();
		
		union.level= Math.max(this.level, other.level);
		union.app_started = this.app_started || other.app_started;
		union.points = Math.max(this.points, other.points);
		for(PhishResult value: PhishResult.values()){
			union.results[value.getValue()]=Math.max(this.results[value.getValue()], other.results[value.getValue()]);
		}
		for(int level=0;level<BackendControllerImpl.getInstance().getLevelCount();level++){
			union.levelPoints[level]=Math.max(this.levelPoints[level],other.levelPoints[level]);
		}
		
		return union;
	}
	
	/** Returns a clone of this SaveGame object. */
    public SaveGame clone() {
    	 SaveGame result = new SaveGame();
    	 
    	result.level=this.level;
    	result.app_started = this.app_started;
    	result.points = this.points;
 		for(PhishResult value: PhishResult.values()){
 			result.results[value.getValue()]=this.results[value.getValue()];
 		}
 		for(int level=0;level<BackendControllerImpl.getInstance().getLevelCount();level++){
 			result.levelPoints[level]=this.levelPoints[level];
 		}
        
        return result;
    }
	
    /** Resets this SaveGame object to be empty. Empty means no stars on no levels. */
    public void zero() {
    	this.results = new int[4];
		this.levelPoints = new int[BackendControllerImpl.getInstance().getLevelCount()];
    }

    /** Returns whether or not this SaveGame is empty. Empty means no stars on no levels. */
    public boolean isZero() {
        return Arrays.equals(this.results, new int[4]) && Arrays.equals(this.levelPoints, new int[BackendControllerImpl.getInstance().getLevelCount()]);
    }
    
    /** Save this SaveGame object to a SharedPreferences. */
    public void save(SharedPreferences sp, String key) {
        SharedPreferences.Editor spe = sp.edit();
        spe.putString(key, toString());
        spe.commit();
    }	
	
	/**
	 * This function checks a loaded state for validity
	 * @return if the state is valid true, otherwise false
	 */
	boolean validate(){
		return this.results != null && this.levelPoints != null;
	}	


}