package de.tudarmstadt.informatik.secuso.phishedu.backend;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.SharedPreferences;
import android.content.res.Resources;

import com.google.android.gms.appstate.OnStateLoadedListener;
import com.google.android.gms.games.GamesClient;

import com.google.android.gms.appstate.AppStateClient;

import de.tudarmstadt.informatik.secuso.phishedu.R;

public class GameProgress implements OnStateLoadedListener{
	private SharedPreferences local_store;
	private GamesClient game_store;
	private AppStateClient remote_store;
	private static final int REMOTE_STORE_SLOT = 0;
	private static final String LOCAL_STORE_KEY = "gamestate";
	
	private static final String LEVEL =  "level";
	private static final String APP_STARTED =  "appStarted";
	private static final String PHISH_BEHIND = "phishBehind";
	private static final String VALUE_PREFIX =  "value_";
	private int[] results;
	private int level = 0;
	private int detected_phish_behind = 0;
	private boolean app_started = false;
	//Points is not saved to persistent state because the user has to start at the beginnig of the level each time the app starts.
	private int points = 0;
	private GameStateLoadedListener listener;
		

	public GameProgress(SharedPreferences local_store, GamesClient game_store, AppStateClient remote_store, GameStateLoadedListener listener) {
		this.local_store=local_store;
		this.game_store=game_store;
		this.remote_store=remote_store;
		this.listener = listener;
		this.loadState(this.local_store.getString(LOCAL_STORE_KEY, "{}"));
		if(this.remote_store.isConnected()){
		  this.remote_store.loadState(this, REMOTE_STORE_SLOT);
		}
	}
	
	public boolean waitForLoad(){
		return this.remote_store.isConnected();
	}
	
	private void loadState(String state){
		this.results = new int[PhishResult.values().length];
		try {
			JSONObject restore =  new JSONObject(state);
			for(PhishResult value: PhishResult.values()){
				this.results[value.getValue()] = restore.getInt(VALUE_PREFIX+value.name());
			}
			this.level = restore.getInt(LEVEL);
			this.app_started = restore.getBoolean(APP_STARTED);
			this.detected_phish_behind = restore.getInt(PHISH_BEHIND);
		} catch (JSONException e) {
			//This means the state was not saved before. We stay with the defaults.ö
		}
	}

	public int DetectedPhish(){
		return this.results[PhishResult.Phish_Detected.getValue()];
	}
	
	public int getResult(PhishResult result){
		return this.results[result.getValue()];
	}
	
	public void addResult(PhishResult result){
		this.results[result.getValue()]+=1;
		//update Leaderboards
		if(game_store.isConnected()){
			if(result == PhishResult.Phish_Detected){
				game_store.submitScore(Resources.getSystem().getString(R.string.leaderboard_detected_phishing_urls),this.results[result.getValue()] );
				game_store.incrementAchievement(Resources.getSystem().getString(R.string.achievement_plakton),this.detected_phish_behind+1);
				game_store.incrementAchievement(Resources.getSystem().getString(R.string.achievement_anchovy),this.detected_phish_behind+1);
				game_store.incrementAchievement(Resources.getSystem().getString(R.string.achievement_trout),this.detected_phish_behind+1);
				game_store.incrementAchievement(Resources.getSystem().getString(R.string.achievement_tuna),this.detected_phish_behind+1);
				game_store.incrementAchievement(Resources.getSystem().getString(R.string.achievement_whale_shark),this.detected_phish_behind+1);
				this.detected_phish_behind=0;
			}
			if(result == PhishResult.Phish_Detected || result ==  PhishResult.Phish_NotDetected){
				//(1) always > 0 because at least one is 1 because of the if condition 
				int total_phish = this.results[PhishResult.Phish_NotDetected.getValue()] + this.results[PhishResult.Phish_Detected.getValue()];
				int detected_phish = this.results[PhishResult.Phish_Detected.getValue()];
				//(2) No divByZero possible because of (1)
				long rate = detected_phish / total_phish;
				game_store.submitScore(Resources.getSystem().getString(R.string.leaderboard_detection_rate),rate );
			}
		}else{
			this.detected_phish_behind+=1;
		}
		this.saveState();
	}
	
	public int getPoints(){
		return this.points;
	}
	public void setPoints(int points){
		this.points=points;
	}
	
	public int getLevel() {
		return this.level;
	}
	public void setLevel(int level){
		this.level=level;
		this.saveState();
	}

	public void StartFinished(){
		this.app_started = true;
		saveState();
	}
	
	private void unlockAchievements(){
		//unlock Achievements
		if(this.app_started){
			game_store.unlockAchievement(Resources.getSystem().getString(R.string.achievement_welcome));
		}
		if(this.level>1){
			game_store.unlockAchievement(Resources.getSystem().getString(R.string.achievement_search_and_rescue));
		}
		if(this.level>2){
			game_store.unlockAchievement(Resources.getSystem().getString(R.string.achievement_know_your_poison));
		}
	}
	
	private String serializeState(){
		JSONObject result = new JSONObject();
		try {
			result.put(APP_STARTED, this.app_started);
			result.put(LEVEL, this.level);
			result.put(PHISH_BEHIND, this.detected_phish_behind);
			for(PhishResult value: PhishResult.values()){
				result.put(VALUE_PREFIX+value.name(), this.results[value.getValue()]);
			}
			return result.toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return "{}";
	}
	
	public void saveState(){
		String serialized = this.serializeState();
		this.local_store.edit().putString(LOCAL_STORE_KEY, serialized).commit();
		
		if(this.game_store.isConnected()){
			unlockAchievements();
		}
		
		if(this.remote_store.isConnected()){
			this.remote_store.updateState(REMOTE_STORE_SLOT, serialized.getBytes());
		}
	}

	@Override
	public void onStateConflict(int stateKey, String resolvedVersion,
			byte[] localData, byte[] serverData) {
		if(stateKey != REMOTE_STORE_SLOT){
			return;
		}
		try {
			JSONObject local_game = new JSONObject(new String(localData));
			JSONObject server_game = new JSONObject(new String(serverData));
			
			//Current resolving strategy: get the most of all values
			JSONObject resolved_game = new JSONObject();
			resolved_game.put(LEVEL, Math.max(local_game.getInt(LEVEL), server_game.getInt(LEVEL)));
			resolved_game.put(APP_STARTED, local_game.getBoolean(APP_STARTED) || server_game.getBoolean(APP_STARTED));
			for(PhishResult value: PhishResult.values()){
				resolved_game.put(VALUE_PREFIX+value.name(),Math.max(local_game.getInt(VALUE_PREFIX+value.name()), server_game.getInt(VALUE_PREFIX+value.name())) );
			}
			this.remote_store.resolveState(this, REMOTE_STORE_SLOT, resolvedVersion, resolved_game.toString().getBytes());
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onStateLoaded(int statusCode, int stateKey, byte[] localData) {
		if(stateKey != REMOTE_STORE_SLOT){
			return;
		}
		String data = new String(localData);
		if (statusCode == AppStateClient.STATUS_OK) {
            // successfully loaded/saved data
			this.loadState(data);
		}else if (statusCode == AppStateClient.STATUS_NETWORK_ERROR_STALE_DATA) {
            // could not connect to get fresh data,
            // but loaded (possibly out-of-sync) cached data instead
			this.loadState(data);
        } else if(statusCode == AppStateClient.STATUS_STATE_KEY_NOT_FOUND) {
        	// this error can be ignored because we have the local store.
        	// The next time we save this will be fixed.
        } else {
        	// handle error
        }
		this.listener.onGameStateLoaded();
	}
}
