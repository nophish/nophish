package de.tudarmstadt.informatik.secuso.phishedu.backend;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.android.gms.appstate.AppStateManager;
import com.google.android.gms.appstate.AppStateManager.StateResult;
import com.google.android.gms.appstate.AppStateStatusCodes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.achievement.Achievement;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import de.tudarmstadt.informatik.secuso.phishedu.Constants;
import de.tudarmstadt.informatik.secuso.phishedu.R;

/**
 * This is a internal class for the backend.
 * It holds the current state and is resposible for saving this state to persistent local storage.
 * It also saves the state online if the user is logged in with his google+ account.
 * @author Clemens Bergmann <cbergmann@schuhklassert.de>
 *
 */
public class GameProgress implements ResultCallback<StateResult>{
	private Context context;
	private SharedPreferences local_store;
	private GoogleApiClient apiClient;
	private static final int REMOTE_STORE_SLOT = 0;
	private static final String LOCAL_STORE_KEY = "gamestate";
	private static final int LIFES_PER_LEVEL = 3;

	private class State{
		public State(){
			this.results = new int[4];
			this.levelPoints = new int[BackendControllerImpl.getInstance().getLevelCount()];
		}
		public int[] results = {0,0,0,0};
		public int[] levelPoints = new int[BackendControllerImpl.getInstance().getLevelCount()];
		public int level = 0;
		public int finishedLevel = -1;
		public int detected_phish_behind = 0;
		public boolean app_started = false;
		public int points = 0;

		/**
		 * This function checks a loaded state for validity
		 * @return if the state is valid true, otherwise false
		 */
		private boolean validate(){
			return this.results != null && this.levelPoints != null;
		}		
	}

	private int[] level_results = {0,0,0,0};
	//This is for saving the points per level. 
	//Once the level is done the points get commited to the persistend state.
	private int level_points;
	private int level_lives=LIFES_PER_LEVEL;

	private GameStateLoadedListener listener;
	private State state = new State();

	/**
	 * Return the number of results of the given type the user had. 
	 * @param type The type of result
	 * @return the number of results
	 */
	public int getLevelResults(PhishResult type){
		return this.level_results[type.getValue()];
	}

	/**
	 * This function returns the total count of phishURLs the User saw.
	 * @return the number of PhishURLs the user saw.
	 */
	public int getPresentedPhish(){
		return this.level_results[PhishResult.Phish_Detected.getValue()]+this.level_results[PhishResult.Phish_NotDetected.getValue()];
	}

	/**
	 * Get the number of URLs the user decided on.
	 * @return Number of URLs the user decided on.
	 */
	public int getDoneUrls(){
		int sum=0;
		for (int result : this.level_results) {
			sum+=result;
		}
		return sum;
	}

	/**
	 * This is the default constructor.
	 * @param context We need this for getting the resources for the achievements 
	 * @param local_store This is the local persistent database where we save the gamestate.
	 * @param game_store This is the GamesClient for unlocking Achievements and Leaderboards.
	 * @param remote_store This is the remote persistent database to save the gamestate.
	 * @param listener
	 */
	public GameProgress(Context context, SharedPreferences local_store, GoogleApiClient apiClient, GameStateLoadedListener listener) {
		this.context=context;
		this.local_store=local_store;
		this.apiClient=apiClient;
		this.listener = listener;
		this.loadState();
	}

	/**
	 * Load the game state.
	 * This function loads the local state and the remote state and tries to get them into sync.
	 */
	public void loadState(){
		this.loadLocalState(this.local_store.getString(LOCAL_STORE_KEY, "{}"));

		if(this.apiClient.isConnected()){
			AppStateManager.load(apiClient, REMOTE_STORE_SLOT).setResultCallback(this);
		}else{
			//If we are not connected notify the listener that we are finished loading.
			//If we are conneted this is done in onStateLoaded()
			this.listener.onGameStateLoaded();
		}
	}

	private void loadLocalState(String state){
		State newState = this.deserializeState(state);
		if(newState != null && newState.validate()){
			this.state=newState;
		}
	}

	/**
	 * Add a game Result to the persistend state.
	 * If we have google+ connected the related Achivements and leaderboards are updated as well.
	 * @param result What kind of outcome did the user have.
	 */
	public void addResult(PhishResult result){
		this.level_results[result.getValue()]+=1;
		this.state.results[result.getValue()]+=1;
		//update Leaderboards
		if(apiClient.isConnected()){
			if(result == PhishResult.Phish_Detected && BackendControllerImpl.getInstance().getUrl().getAttackType() != PhishAttackType.NoPhish){
				Games.Leaderboards.submitScore(apiClient, context.getResources().getString(R.string.leaderboard_detected_phishing_urls),this.state.results[result.getValue()]);
				Games.Achievements.increment(apiClient, context.getResources().getString(R.string.achievement_plakton),this.state.detected_phish_behind+1);
				Games.Achievements.increment(apiClient, context.getResources().getString(R.string.achievement_anchovy),this.state.detected_phish_behind+1);
				Games.Achievements.increment(apiClient, context.getResources().getString(R.string.achievement_trout),this.state.detected_phish_behind+1);
				Games.Achievements.increment(apiClient, context.getResources().getString(R.string.achievement_tuna),this.state.detected_phish_behind+1);
				Games.Achievements.increment(apiClient, context.getResources().getString(R.string.achievement_whale_shark),this.state.detected_phish_behind+1);
				this.state.detected_phish_behind=0;
			}
		}else{
			if(result == PhishResult.Phish_Detected){
				this.state.detected_phish_behind+=1;
			}
		}
		this.saveState();
	}

	/**
	 * Gets the currently saved points.
	 * These are the total points of the user. They only change when the user successfully finishes a level.
	 * @return the currently saved points
	 */
	public int getPoints(){
		return this.state.points;
	}

	/**
	 * This function returns the points the user gained in this level.
	 * @return The Points in this level
	 */
	public int getLevelPoints(){
		return this.level_points;
	}

	/**
	 * Save the level Points to the persistend state.
	 */
	public void commitPoints(){
		this.state.points+=this.level_points;
		if(this.apiClient.isConnected()){
			Games.Leaderboards.submitScore(apiClient, context.getResources().getString(R.string.leaderboard_total_points), this.state.points);
		}
		if(this.getPoints() > this.state.levelPoints[this.getLevel()]){
			this.state.levelPoints[this.getLevel()]=this.getPoints();
		}
		saveState();
	}

	/**
	 * This saves the current points.
	 * See the comment of {@link GameProgress#getPoints()} regarding persistence.
	 * @param points the number of points for this level.
	 */
	public void setLevelPoints(int points){
		//we only allow positive points
		if(points < 0 ){
			points = 0;
		}
		this.level_points=points;
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
	 * It also resets level specific counters
	 * @param level The current level
	 */
	public void setLevel(int level){
		this.state.level=level;
		if(getMaxUnlockedLevel()<level){
			throw new IllegalStateException("The given level ("+level+") is not unlocked.");
		}
		this.level_results=new int[4];
		this.level_points=0;
		this.level_lives=LIFES_PER_LEVEL;

		this.saveState();
	}

	/**
	 * Unlock the given Level so that the user is able to play it.
	 * @param level the level to unlock
	 */
	public void finishlevel(int level){
		if(level > this.state.finishedLevel){
			this.state.finishedLevel=level;
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
		if(this.state.level>1){
			Games.Achievements.unlock(apiClient, context.getResources().getString(R.string.achievement_search_and_rescue));
		}
		if(this.state.level>2){
			Games.Achievements.unlock(apiClient, context.getResources().getString(R.string.achievement_know_your_poison));
		}
	}

	private String serializeState(State state){
		return new Gson().toJson(state);
	}

	private State deserializeState(String state){
		State result = null;
		try {
			result = (new Gson()).fromJson(state,State.class);
		} catch (JsonSyntaxException e) {
			//Json SyntaxException
		}
		return result;
	}

	/**
	 * This function saves the current state persistently.
	 * It is at least saved locally on local_store.
	 * If we are connected to the remote_store we also save it online.
	 */
	public void saveState(){
		String serialized = this.serializeState(this.state);
		this.local_store.edit().putString(LOCAL_STORE_KEY, serialized).commit();

		if(this.apiClient.isConnected()){
			unlockAchievements();
		}

		if(this.apiClient.isConnected()){
			AppStateManager.update(apiClient, REMOTE_STORE_SLOT, serialized.getBytes());
		}
	}

	@Override
	public void onResult(StateResult result) {
		AppStateManager.StateConflictResult conflictResult = result.getConflictResult();
		AppStateManager.StateLoadedResult loadedResult 
		= result.getLoadedResult();
		if (loadedResult != null) {
			processStateLoaded(loadedResult);
		} else if (conflictResult != null) {
			processStateConflict(conflictResult);
		}	
	}


	private void processStateConflict(AppStateManager.StateConflictResult result) {
		if(result.getStateKey() != REMOTE_STORE_SLOT){
			return;
		}
		State local_game = this.deserializeState(new String(result.getLocalData()));
		State server_game = this.deserializeState(new String(result.getServerData()));

		//Current resolving strategy: get the most of all values
		State resolved_game = new State();

		if(local_game == null && server_game != null){
			resolved_game = server_game;
		}else if(server_game == null && local_game != null){
			resolved_game = local_game;
		}else if(server_game != null && local_game != null){
			resolved_game.level= Math.max(local_game.level, server_game.level);
			resolved_game.app_started = local_game.app_started || server_game.app_started;
			resolved_game.points = Math.max(local_game.points, server_game.points);
			for(PhishResult value: PhishResult.values()){
				resolved_game.results[value.getValue()]=Math.max(local_game.results[value.getValue()], server_game.results[value.getValue()]);
			}
			for(int level=0;level<BackendControllerImpl.getInstance().getLevelCount();level++){
				resolved_game.levelPoints[level]=Math.max(local_game.levelPoints[level],server_game.levelPoints[level]);
			}
		}
		AppStateManager.resolve(apiClient, REMOTE_STORE_SLOT, result.getResolvedVersion(), resolved_game.toString().getBytes());
	}

	private void processStateLoaded(AppStateManager.StateLoadedResult result) {
		if(result.getStateKey() != REMOTE_STORE_SLOT){
			return;
		}
		if (result.getStatus().getStatusCode() == AppStateStatusCodes.STATUS_OK) {
			// successfully loaded/saved data
			this.loadLocalState(new String(result.getLocalData()));
		}else if (result.getStatus().getStatusCode() == AppStateStatusCodes.STATUS_NETWORK_ERROR_STALE_DATA) {
			// could not connect to get fresh data,
			// but loaded (possibly out-of-sync) cached data instead
			this.loadLocalState(new String(result.getLocalData()));
		} else if(result.getStatus().getStatusCode() == AppStateStatusCodes.STATUS_STATE_KEY_NOT_FOUND) {
			// this error can be ignored because we have the local store.
			// The next time we save this will be fixed.
		} else {
			// handle error
		}
		this.listener.onGameStateLoaded();
	}


	/**
	 * This returns the maximum level the user is able to access.
	 * @return the max level
	 */
	public int getMaxUnlockedLevel() {
		int result;
		if(Constants.UNLOCK_ALL_LEVELS){
			result = BackendControllerImpl.getInstance().getLevelCount();
		}else{
			result = this.state.finishedLevel+1;
		}
		return Math.min(result, BackendControllerImpl.getInstance().getLevelCount()-1);
	}
	
	/**
	 * This returns the maximum level the user finished
	 * @return the max level
	 */
	public int getMaxFinishedLevel() {
		if(Constants.UNLOCK_ALL_LEVELS){
			return BackendControllerImpl.getInstance().getLevelCount()-1;
		}else{
			return this.state.finishedLevel;
		}
	}

	/**
	 * Get the number of remaining lives for this level.
	 * @return Number of Lives for this level.
	 */
	public int getRemainingLives(){
		return this.level_lives;
	}

	/**
	 * Decrement the number of lives the user has remaining.
	 */
	public void decLives(){
		this.level_lives--;
	}

	/**
	 * Get the points saved for the given level.
	 * This only changes after finishing a level.
	 * @param level The level you want to get the points for
	 * @return  the points for the given level.
	 */
	public int getLevelPoints(int level){
		return this.state.levelPoints[level];
	}

	/**
	 * remove the game Data that was stored in Googles Cloud Save Storage
	 */
	public void deleteRemoteData(){
		if(this.apiClient.isConnected()){
			AppStateManager.update(apiClient, REMOTE_STORE_SLOT, new byte[0]);
		}
	}
}
