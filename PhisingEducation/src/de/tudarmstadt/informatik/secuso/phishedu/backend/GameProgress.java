package de.tudarmstadt.informatik.secuso.phishedu.backend;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.drive.Contents;
import com.google.android.gms.drive.MetadataChangeSet;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.GamesStatusCodes;
import com.google.android.gms.games.achievement.Achievement;
import com.google.android.gms.games.snapshot.Snapshot;
import com.google.android.gms.games.snapshot.SnapshotMetadata;
import com.google.android.gms.games.snapshot.SnapshotMetadataBuffer;
import com.google.android.gms.games.snapshot.SnapshotMetadataChange;
import com.google.android.gms.games.snapshot.SnapshotMetadataChangeCreator;
import com.google.android.gms.games.snapshot.Snapshots;

import de.tudarmstadt.informatik.secuso.phishedu.Constants;
import de.tudarmstadt.informatik.secuso.phishedu.R;

/**
 * This is a internal class for the backend.
 * It holds the current state and is resposible for saving this state to persistent local storage.
 * It also saves the state online if the user is logged in with his google+ account.
 * @author Clemens Bergmann <cbergmann@schuhklassert.de>
 *
 */
public class GameProgress{
	private Context context;
	private SharedPreferences local_store;
	private static final String REMOTE_STORE_GAME = "autosave";
	private static final String LOCAL_STORE_KEY = "gamestate";
	private static final int LIFES_PER_LEVEL = 3;
	private static final int MAX_SNAPSHOT_RESOLVE_RETRIES = 3;

	private int[] level_results = new int[PhishResult.getMax()+1];
	//This is for saving the points per level. 
	//Once the level is done the points get commited to the persistend state.
	private int level_points;
	private int level;
	private int level_lives=LIFES_PER_LEVEL;
	private int proof_right_in_row=0;

	private SaveGame mSaveGame = new SaveGame();

	/**
	 * This is the default constructor.
	 * @param context We need this for getting the resources for the achievements 
	 * @param local_store This is the local persistent database where we save the gamestate.
	 * @param game_store This is the GamesClient for unlocking Achievements and Leaderboards.
	 * @param remote_store This is the remote persistent database to save the gamestate.
	 * @param listener
	 */
	public GameProgress(Context context, SharedPreferences local_store, GoogleApiClient apiClient) {
		this.context=context;
		this.local_store=local_store;
		this.loadLocal();
	}
	
	/**
     * Loads a Snapshot from the user's synchronized storage.
     */
    void loadFromSnapshot() {
        AsyncTask<Void, Void, Integer> task = new AsyncTask<Void, Void, Integer>() {
            @Override
            protected Integer doInBackground(Void... params) {
                Snapshots.OpenSnapshotResult result = Games.Snapshots.open(getApiClient(), REMOTE_STORE_GAME, false).await();

                int status = result.getStatus().getStatusCode();

                if (status == GamesStatusCodes.STATUS_OK) {
                    Snapshot snapshot = result.getSnapshot();
                    if(snapshot.readFully().length>0){
                      SaveGame newSaveGame = new SaveGame(snapshot.readFully());
                      mSaveGame = mSaveGame.unionWith(newSaveGame);
                    }
                }

                return status;
            }

			@Override
            protected void onPostExecute(Integer status){
				if (status == GamesStatusCodes.STATUS_OK ){
					BackendControllerImpl.getInstance().getFrontend().updateUI();
				}else if (status != GamesStatusCodes.STATUS_SNAPSHOT_NOT_FOUND &&  status != GamesStatusCodes.STATUS_SNAPSHOT_CONFLICT) {
					BackendControllerImpl.getInstance().getFrontend().displayToast(BackendControllerImpl.getInstance().getFrontend().getContext().getResources().getString(R.string.google_plus_snapshot_load_problem)+status.toString());
                }
				
            }
        };

        task.execute();
    }
		
    /**
     * Conflict resolution for when Snapshots are opened.
     * @param result The open snapshot result to resolve on open.
     * @return The opened Snapshot on success; otherwise, returns null.
     */
    Snapshot processSnapshotOpenResult(Snapshots.OpenSnapshotResult result, int retryCount){
        int status = result.getStatus().getStatusCode();
        retryCount++;

        if (status == GamesStatusCodes.STATUS_OK) {
            return result.getSnapshot();
        } else if (status == GamesStatusCodes.STATUS_SNAPSHOT_CONTENTS_UNAVAILABLE) {
            return result.getSnapshot();
        } else if (status == GamesStatusCodes.STATUS_SNAPSHOT_CONFLICT){
            Snapshot snapshot = result.getSnapshot();
            Snapshot conflictSnapshot = result.getConflictingSnapshot();
            SaveGame resolved = new SaveGame(snapshot.readFully());
            resolved = resolved.unionWith(new SaveGame(conflictSnapshot.readFully()));
            snapshot.writeBytes(resolved.toBytes());

            Snapshots.OpenSnapshotResult resolveResult = Games.Snapshots.resolveConflict(getApiClient(), result.getConflictId(), snapshot).await();
            
            if (retryCount < MAX_SNAPSHOT_RESOLVE_RETRIES){
                return processSnapshotOpenResult(resolveResult, retryCount);
            }else{
                BackendControllerImpl.getInstance().getFrontend().getBaseActivity().runOnUiThread(new Runnable() {
					@Override
					public void run() {
						String message = "Could not resolve snapshot conflicts";
						BackendControllerImpl.getInstance().getFrontend().displayToast(message);
					}
				});
            }
        }
        // Fail, return null.
        return null;
    }
    
    /**
     * Prepares saving Snapshot to the user's synchronized storage, conditionally resolves errors,
     * and stores the Snapshot.
     */
    void saveSnapshot() {
        AsyncTask<Void, Void, Snapshots.OpenSnapshotResult> task =
                new AsyncTask<Void, Void, Snapshots.OpenSnapshotResult>() {
                    @Override
                    protected Snapshots.OpenSnapshotResult doInBackground(Void... params) {
                        Snapshots.OpenSnapshotResult result = Games.Snapshots.open(getApiClient(),REMOTE_STORE_GAME, true).await();
                        Snapshot toWrite = processSnapshotOpenResult(result,0);
                        if(toWrite != null){
                          writeSnapshot(toWrite);
                        }
                        return result;
                    }
                };

        task.execute();
    }
    
    /**
     * Generates metadata, takes a screenshot, and performs the write operation for saving a
     * snapshot.
     */
    private String writeSnapshot(Snapshot snapshot){
        // Set the data payload for the snapshot.
        snapshot.writeBytes(mSaveGame.toBytes());

        // Save the snapshot.
        SnapshotMetadataChange metadataChange = new SnapshotMetadataChange.Builder()
                .setDescription("The default autosafe for the game.")
                .build();
        Games.Snapshots.commitAndClose(getApiClient(), snapshot, metadataChange);
        return snapshot.toString();
    }
    
    private void loadLocal() {
        SaveGame newSaveGame = new SaveGame(local_store, LOCAL_STORE_KEY);
        mSaveGame = mSaveGame.unionWith(newSaveGame);
    }

    private void saveLocal() {
        mSaveGame.save(local_store, LOCAL_STORE_KEY);
    }

	public void onSignInSucceeded() {
      loadFromSnapshot();
	}
    
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
	 * Add a game Result to the persistend state.
	 * If we have google+ connected the related Achivements and leaderboards are updated as well.
	 * @param result What kind of outcome did the user have.
	 */
	public void addResult(PhishResult result){
		this.level_results[result.getValue()]+=1;
		this.mSaveGame.results[result.getValue()]+=1;
		//update Leaderboards
		if(getApiClient().isConnected()){
			if(result == PhishResult.Phish_Detected && BackendControllerImpl.getInstance().getUrl().getAttackType() != PhishAttackType.NoPhish){
				Games.Leaderboards.submitScore(getApiClient(), context.getResources().getString(R.string.leaderboard_detected_phishing_urls),this.mSaveGame.results[result.getValue()]);
				Games.Achievements.increment(getApiClient(), context.getResources().getString(R.string.achievement_plakton),this.mSaveGame.detected_phish_behind+1);
				Games.Achievements.increment(getApiClient(), context.getResources().getString(R.string.achievement_anchovy),this.mSaveGame.detected_phish_behind+1);
				Games.Achievements.increment(getApiClient(), context.getResources().getString(R.string.achievement_trout),this.mSaveGame.detected_phish_behind+1);
				Games.Achievements.increment(getApiClient(), context.getResources().getString(R.string.achievement_tuna),this.mSaveGame.detected_phish_behind+1);
				Games.Achievements.increment(getApiClient(), context.getResources().getString(R.string.achievement_whale_shark),this.mSaveGame.detected_phish_behind+1);
				this.mSaveGame.detected_phish_behind=0;
			}
		}else{
			if(result == PhishResult.Phish_Detected){
				this.mSaveGame.detected_phish_behind+=1;
			}
		}
		
		this.saveState();
	}
	
	/**
	 * Increment the proof right in row counter
	 */
	public void incProofRightInRow(){
		this.proof_right_in_row++;
	}
	
	/**
	 * reset the proof right in row counter
	 */
	public void resetProofRightInRow(){
		this.proof_right_in_row=0;
	}

	/**
	 * Gets the currently saved points.
	 * These are the total points of the user. They only change when the user successfully finishes a level.
	 * @return the currently saved points
	 */
	public int getTotalPoints(){
		return this.mSaveGame.getPoints();
	}

	/**
	 * This function returns the points the user gained in this level.
	 * @return The Points in this level
	 */
	public int getLevelPoints(){
		return this.level_points;
	}
	
	/**
	 * Return the number of stars the user achieved for a given level.
	 * @param level the level to get the stars for
	 * @return the number of stars
	 */
	public int getLevelStars(int level){
		int levelpoints = getLevelPoints(level);
		int maxlevelpoints = BackendControllerImpl.getInstance().getLevelInfo(level).getLevelmaxPoints();
		int maxstars=3;
		
		return (int) Math.floor(levelpoints/(maxlevelpoints/maxstars));
	}

	/**
	 * Save the level Points to the persistend state.
	 */
	public void commitPoints(){
		if(this.getLevelPoints() > this.mSaveGame.levelPoints[this.getLevel()]){
			this.mSaveGame.levelPoints[this.getLevel()]=this.getLevelPoints();
		}
		if(this.getApiClient().isConnected()){
			Games.Leaderboards.submitScore(getApiClient(), context.getResources().getString(R.string.leaderboard_total_points), this.mSaveGame.getPoints());
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
		return this.level;
	}
	/**
	 * This Function sets the current level of the user.
	 * It also resets level specific counters
	 * @param level The current level
	 */
	public void setLevel(int level){
		if(getMaxUnlockedLevel()<level){
			throw new IllegalStateException("The given level ("+level+") is not unlocked.");
		}
		this.level=level;
		this.level_results=new int[PhishResult.getMax()+1];
		this.level_points=0;
		this.level_lives=LIFES_PER_LEVEL;

		this.saveState();
	}

	/**
	 * Unlock the given Level so that the user is able to play it.
	 * @param level the level to unlock
	 */
	public void finishlevel(int level){
		if(level > this.mSaveGame.finishedLevel){
			this.mSaveGame.finishedLevel=level;
		}
		this.saveState();
	}

	/**
	 * This function is called after the app first starts.
	 * This allows us to react to that event and unlock a {@link Achievement}
	 */
	public void StartFinished(){
		this.mSaveGame.app_started = true;
		saveState();
	}

	private void unlockAchievements(){
		//unlock Achievements
		if(this.level>1){
			Games.Achievements.unlock(getApiClient(), context.getResources().getString(R.string.achievement_search_and_rescue));
		}
		if(this.level>2){
			Games.Achievements.unlock(getApiClient(), context.getResources().getString(R.string.achievement_know_your_poison));
		}
	}

	/**
	 * This function saves the current state persistently.
	 * It is at least saved locally on local_store.
	 * If we are connected to the remote_store we also save it online.
	 */
	public void saveState(){
		saveLocal();
		
		if(this.getApiClient().isConnected()){
			unlockAchievements();
		}

		if(this.getApiClient().isConnected()){
			saveSnapshot();
		}
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
			result = this.getMaxFinishedLevel()+1;
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
			return this.mSaveGame.finishedLevel;
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
		BackendControllerImpl.getInstance().getFrontend().vibrate(500);
	}

	/**
	 * Get the points saved for the given level.
	 * This only changes after finishing a level.
	 * @param level The level you want to get the points for
	 * @return  the points for the given level.
	 */
	public int getLevelPoints(int level){
		return this.mSaveGame.levelPoints[level];
	}
	
	
	
	/**
	 * remove the game Data that was stored in Googles Cloud Save Storage
	 */
	public void deleteRemoteData(){
		AsyncTask<Void, Void, Integer> task = new AsyncTask<Void, Void, Integer>() {
            @Override
            protected Integer doInBackground(Void... params) {
            	Snapshots.LoadSnapshotsResult lresult = Games.Snapshots.load(getApiClient(), true).await();

                int status = lresult.getStatus().getStatusCode();

                if (status == GamesStatusCodes.STATUS_OK){
                    SnapshotMetadataBuffer smdb = lresult.getSnapshots();
                    Iterator<SnapshotMetadata> snapsIt = smdb.iterator();

                    while(snapsIt.hasNext()){
                        Games.Snapshots.delete(getApiClient(), snapsIt.next());
                    }
                    smdb.close();
                }
                
                return status;
            }

			@Override
            protected void onPostExecute(Integer status){
				BackendControllerImpl.getInstance().getFrontend().displayToast(R.string.google_plus_data_deleted);
            }
        };

        task.execute();
	}
	
    private GoogleApiClient getApiClient() {
    	return BackendControllerImpl.getInstance().getGameHelper().getApiClient();
	}
    
    /**
     * How often in a row got the user the proof right.
     * @return number of right proofs in row.
     */
    public int getProofRightInRow(){
    	return this.proof_right_in_row;
    }

}
