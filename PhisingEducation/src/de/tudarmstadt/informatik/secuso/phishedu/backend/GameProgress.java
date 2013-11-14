package de.tudarmstadt.informatik.secuso.phishedu.backend;

import android.content.Context;

import android.content.SharedPreferences;

import com.google.android.gms.appstate.OnStateLoadedListener;
import com.google.android.gms.games.GamesClient;
import com.google.android.gms.games.achievement.Achievement;

import com.google.android.gms.appstate.AppStateClient;
import com.google.gson.Gson;

import de.tudarmstadt.informatik.secuso.phishedu.R;

/**
 * This is a internal class for the backend.
 * It holds the current state and is resposible for saving this state to persistent local storage.
 * It also saves the state online if the user is logged in with his google+ account.
 * @author Clemens Bergmann <cbergmann@schuhklassert.de>
 *
 */
public class GameProgress implements OnStateLoadedListener{
	private Context context;
	private SharedPreferences local_store;
	private GamesClient game_store;
	private AppStateClient remote_store;
	private static final int REMOTE_STORE_SLOT = 0;
	private static final String LOCAL_STORE_KEY = "gamestate";

	private class State{
		public State(){
			this.results = new int[4];
		}
		public int[] results = {0,0,0,0};
		public int level = 0;
		public int unlockedLevel = 0;
		public int detected_phish_behind = 0;
		public boolean app_started = false;

		
		/**
		 * This function checks a loaded state for validity
		 * @return if the state is valid true, otherwise false
		 */
		private boolean validate(){
			return state.results != null;
		}	
		
	}
	//Points is not saved to persistent state because the user has to start at the beginnig of the level each time the app starts.
	private int points = 0;
	private GameStateLoadedListener listener;
	private State state = new State();

	/**
	 * This is the default constructor.
	 * @param context We need this for getting the resources for the achievements 
	 * @param local_store This is the local persistent database where we save the gamestate.
	 * @param game_store This is the GamesClient for unlocking Achievements and Leaderboards.
	 * @param remote_store This is the remote persistent database to save the gamestate.
	 * @param listener
	 */
	public GameProgress(Context context, SharedPreferences local_store, GamesClient game_store, AppStateClient remote_store, GameStateLoadedListener listener) {
		this.context=context;
		this.local_store=local_store;
		this.game_store=game_store;
		this.remote_store=remote_store;
		this.listener = listener;
		this.loadState(this.local_store.getString(LOCAL_STORE_KEY, "{}"));
		if(this.remote_store.isConnected()){
			this.remote_store.loadState(this, REMOTE_STORE_SLOT);
		}else{
			//If we are not connected notify the listener that we are finished loading.
			//If we are conneted this is done in onStateLoaded()
			this.listener.onGameStateLoaded();
		}
	}

	private void loadState(String state){
		State newState = this.deserializeState(state);
		if(newState.validate()){
			this.state=newState;
		}
	}

	/**
	 * Add a game Result to the persistend state.
	 * If we have google+ connected the related Achivements and leaderboards are updated as well.
	 * @param result What kind of outcome did the user have.
	 */
	public void addResult(PhishResult result){
		this.state.results[result.getValue()]+=1;
		//update Leaderboards
		if(game_store.isConnected()){
			if(result == PhishResult.Phish_Detected){
				game_store.submitScore(context.getResources().getString(R.string.leaderboard_detected_phishing_urls),this.state.results[result.getValue()] );
				game_store.incrementAchievement(context.getResources().getString(R.string.achievement_plakton),this.state.detected_phish_behind+1);
				game_store.incrementAchievement(context.getResources().getString(R.string.achievement_anchovy),this.state.detected_phish_behind+1);
				game_store.incrementAchievement(context.getResources().getString(R.string.achievement_trout),this.state.detected_phish_behind+1);
				game_store.incrementAchievement(context.getResources().getString(R.string.achievement_tuna),this.state.detected_phish_behind+1);
				game_store.incrementAchievement(context.getResources().getString(R.string.achievement_whale_shark),this.state.detected_phish_behind+1);
				this.state.detected_phish_behind=0;
			}
			if(result == PhishResult.Phish_Detected || result ==  PhishResult.Phish_NotDetected){
				//(1) always > 0 because at least one is 1 because of the if condition 
				int total_phish = this.state.results[PhishResult.Phish_NotDetected.getValue()] + this.state.results[PhishResult.Phish_Detected.getValue()];
				int detected_phish = this.state.results[PhishResult.Phish_Detected.getValue()];
				//(2) No divByZero possible because of (1)
				long rate = detected_phish / total_phish;
				game_store.submitScore(context.getResources().getString(R.string.leaderboard_detection_rate),rate );
			}
		}else{
			this.state.detected_phish_behind+=1;
		}
		this.saveState();
	}

	/**
	 * Gets the currently saved points.
	 * It is worth to mention that these are not persistently saved.
	 * The user has to restart the given level from 0 points each time the app starts.
	 * @return the currently saved points
	 */
	public int getPoints(){
		return this.points;
	}
	/**
	 * This saves the current points.
	 * See the comment of {@link GameProgress#getPoints()} regarding persistence.
	 * @param points the number of points for this level.
	 */
	public void setPoints(int points){
		this.points=points;
	}

	/**
	 * This functions returns the level the user is currently in.
	 * @return the level number.
	 */
	public int getLevel() {
		return this.state.level;
	}
	/**
	 * This Function sets the current level of the user.
	 * @param level The current level
	 */
	public void setLevel(int level){
		this.state.level=level;
		if(level > this.state.unlockedLevel){
			this.state.unlockedLevel=level;
		}
		this.saveState();
	}

	/**
	 * This function is called after the app first starts.
	 * This allows us to react to that event and unlock a {@link Achievement}
	 */
	public void StartFinished(){
		this.state.app_started = true;
		saveState();
	}

	private void unlockAchievements(){
		//unlock Achievements
		if(this.state.app_started){
			game_store.unlockAchievement(context.getResources().getString(R.string.achievement_welcome));
		}
		if(this.state.level>1){
			game_store.unlockAchievement(context.getResources().getString(R.string.achievement_search_and_rescue));
		}
		if(this.state.level>2){
			game_store.unlockAchievement(context.getResources().getString(R.string.achievement_know_your_poison));
		}
	}

	private String serializeState(State state){
		return new Gson().toJson(state);
	}

	private State deserializeState(String state){
		return new Gson().fromJson(state,State.class);
	}

	/**
	 * This function saves the current state persistently.
	 * It is at least saved locally on local_store.
	 * If we are connected to the remote_store we also save it online.
	 */
	public void saveState(){
		String serialized = this.serializeState(this.state);
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
		State local_game = this.deserializeState(new String(localData));
		State server_game = this.deserializeState(new String(serverData));

		//Current resolving strategy: get the most of all values
		State resolved_game = new State();
		resolved_game.level= Math.max(local_game.level, server_game.level);
		resolved_game.app_started = local_game.app_started || server_game.app_started;
		for(PhishResult value: PhishResult.values()){
			resolved_game.results[value.getValue()]=Math.max(local_game.results[value.getValue()], server_game.results[value.getValue()]);
		}
		this.remote_store.resolveState(this, REMOTE_STORE_SLOT, resolvedVersion, resolved_game.toString().getBytes());
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
	
	
	/**
	 * This returns the maximum level the user is able to access
	 * @return the max level
	 */
	public int getMaxUnlockedLevel() {
		return this.state.unlockedLevel;
	}
}
