package de.tudarmstadt.informatik.secuso.phishedu.backend;

import java.util.Arrays;

import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

class SaveGame{
	// serialization format version
    @SuppressWarnings("unused")
	private static final String SERIAL_VERSION = "1.1";
   
	public int[] results = new int[PhishResult.values().length];
	public int[] levelPoints = new int[BackendControllerImpl.getInstance().getLevelCount()];
	public int finishedLevel = -1;
	public int detected_phish_behind = 0;
	public boolean app_started = false;
		
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
    
	private void loadFromJson(String state){
		SaveGame result = null;
		try {
			result = (new Gson()).fromJson(state,SaveGame.class);
			
			if(result == null){
				return;
			}
			this.app_started = result.app_started;
			this.finishedLevel = result.finishedLevel;
			this.detected_phish_behind = result.detected_phish_behind;
			for(int i=0; i<result.results.length; i++){
				this.results[i]=result.results[i];
			}
			this.levelPoints = result.levelPoints;
		} catch (JsonSyntaxException e) {
			//Json SyntaxException
		}
	}
	
	public int getPoints(){
		int result = 0;
		for (int points : this.levelPoints) {
			result += points;
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
		
		union.app_started = this.app_started || other.app_started;
		union.finishedLevel = Math.max(this.finishedLevel, other.finishedLevel);
		union.detected_phish_behind = Math.max(this.detected_phish_behind, other.detected_phish_behind);
		for(int i=0;i<Math.min(this.results.length,other.results.length);i++){
			union.results[i]=Math.max(this.results[i], other.results[i]);
		}
		for(int level=0;level<BackendControllerImpl.getInstance().getLevelCount();level++){
			union.levelPoints[level]=Math.max(this.levelPoints[level],other.levelPoints[level]);
		}
		
		return union;
	}
	
	/** Returns a clone of this SaveGame object. */
    public SaveGame clone() {
    	SaveGame result = new SaveGame();
    	 
    	result.app_started = this.app_started;
 		for(int i=0; i<this.results.length;i++){
 			result.results[i]=this.results[i];
 		}
 		for(int level=0;level<BackendControllerImpl.getInstance().getLevelCount();level++){
 			result.levelPoints[level]=this.levelPoints[level];
 		}
        
        return result;
    }
	
    /** Resets this SaveGame object to be empty. Empty means no stars on no levels. */
    public void zero() {
    	this.results = new int[PhishResult.values().length];
		this.levelPoints = new int[BackendControllerImpl.getInstance().getLevelCount()];
    }

    /** Returns whether or not this SaveGame is empty. Empty means no stars on no levels. */
    public boolean isZero() {
        return Arrays.equals(this.results, new int[PhishResult.values().length]) && Arrays.equals(this.levelPoints, new int[BackendControllerImpl.getInstance().getLevelCount()]);
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