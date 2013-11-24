package de.tudarmstadt.informatik.secuso.phishedu.backend;

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
	 * This function is called from the backend when we change Level.
	 * @param level the new level
	 */
	void onLevelChange(int level);
	
	/**
	 * Start a browser and show the given URL
	 * @param url the url to show
	 */
	void startBrowser(Uri url);
	
	/**
	 * This is for the backend to display a message to the user.
	 * @param message The message to show to the user.
	 */
	void displayToast(String message);
	
	/**
	 * Whenever a level is finished this function is called by the backend.
	 * Whenever you are finished with this task call {@link BackendControllerInterface#startNextLevel()}
	 * @param level the finished level
	 */
	void levelFinished(int level);
	
	/**
	 * This function is called whenever the user failed on a level.
	 * This means he was not able to detect enough Phishing URLs.
	 * The UI should display a message stating this fact and call {@link BackendController#startLevel(int)}
	 * This will (after initialization) result in a new Call to {@link FrontendControllerInterface#onLevelChange(int)} 
	 * @param level
	 */
	void levelFailed(int level);
	
	/**
	 * Ask the user if She wants to finish the level after finding enught fish.
	 * @return True if the user clicked the positive Button. False otherwise.
	 */
	boolean askUserFinish();
}
