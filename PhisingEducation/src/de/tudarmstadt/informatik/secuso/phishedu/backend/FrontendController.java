package de.tudarmstadt.informatik.secuso.phishedu.backend;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;

import com.google.example.games.basegameutils.GameHelper.GameHelperListener;

/**
 * This is the Interface that the frontend presents to the backend.
 * It mainly contains callback functions to inform the frontend about backend changes.
 * @author Clemens Bergmann <cbergmann@schuhklassert.de>
 *
 */
public interface FrontendController extends GameHelperListener {
	//Context functions
	/**
	 * For the local store of the game state we need the local store
	 * @return the curren gameContext
	 */
	Context getContext();
	
	/**
	 * Get an Acitivity that events can set theyr base on.
	 * @return the StartMenuActivity
	 */
	Activity getBaseActivity();
	
	//Callback functions
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
	 * This is for the backend to display a message to the user.
	 * @param Rid The message to show to the user. (Resource ID)
	 */
	void displayToast(int Rid);
	
	/**
	 * This is for the backend to display the score in the appropriate color to the user.
	 * @param score the current score
	 */
	void displayToastScore(int score);
}
