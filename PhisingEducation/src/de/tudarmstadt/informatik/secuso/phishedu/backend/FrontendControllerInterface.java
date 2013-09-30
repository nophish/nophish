package de.tudarmstadt.informatik.secuso.phishedu.backend;

import com.google.android.gms.appstate.AppStateClient;
import com.google.android.gms.games.GamesClient;

import android.content.Context;
import android.net.Uri;

/**
 * This is the Interface that the frontend presents to the backend.
 * It mainly contains callback functions to inform the frontend about backend changes.
 * @author Clemens Bergmann <cbergmann@schuhklassert.de>
 *
 */
public interface FrontendControllerInterface {
	//Context functions
	/**
	 * For acchievements and leaderboards we need the gamesClient
	 * @return the gamesclient of the main activity.
	 */
	GamesClient getGamesClient();
	/**
	 * For the remote store of the game state we need the remote store.
	 * @return the client to save gamestate to
	 */
	AppStateClient getAppStateClient();
	/**
	 * For the local store of the game state we need the local store
	 * @return the curren gameContext
	 */
	Context getContext();

	//Callback functions
	/**
	 * This function is called while init continues.
	 * @param percent how far is completion.
	 */
	void initProgress(int percent);
	/**
	 * This function is called when the game can start.
	 */
	void initDone();
	/**
	 * This function is called when we see a click to the mail.
	 */
	void MailReturned();
	/**
	 * this is the last call after the level1is completed
	 */
	void level1Finished();
	/**
	 * This function is called from the backend when we change Level.
	 * @param level the new level
	 */
	void onLevelChange(int level);
	/**
	 * Start a browser and show the given URL
	 * @param url the url to show
	 */
	void startBrowser(Uri url);
}
