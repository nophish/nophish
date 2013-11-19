package de.tudarmstadt.informatik.secuso.phishedu.backend;

import com.google.example.games.basegameutils.GameHelper;

import android.net.Uri;

/**
 * This is the interface that the backend presents to the frontend.
 * @author Clemens Bergmann <cbergmann@schuhklassert.de>
 *
 */
public interface BackendControllerInterface {
	/**
	 * This function must be called directly before the first start of the app.
	 * It will register the caller with the backend for callbacks.
	 * @param frontend
	 * @param gamehelper
	 */
	void init(FrontendControllerInterface frontend, GameHelper gamehelper);
	
	/**
	 * This function sends a Mail to a custom Mail Adress-
	 * @param from The Sender mail address
	 * @param to The receiver mail address
	 * @param usermessage Each send out mail contains a usermessage to maximize the awareness.
	 */
	void sendMail(String from, String to, String usermessage);
	
	/**
	 * This starts a level and initilizes the backend state.
	 * @param level The level you want to start
	 */
	void startLevel(int level);
	
	/**
	 * This function start the browser at the leve1 URL
	 */
	public void redirectToLevel1URL();
	
	/**
	 * Get the next url and save the URL Information internally.
	 * @return A set of strings that (concardinated) make up the URL
	 */
	String[] getNextUrl();
	
	/**
	 * Get the current url returned by the last {@link BackendControllerInterface#getNextUrl()} call.
	 * @return A set of strings that (concardinated) make up the URL
	 */
	String[] getUrl();
	
	/**
	 * Get the current level of the user.
	 * This is the level the user is currently in.
	 * If we later think about allowing the user to select level we have to implement a getUnlockedLevel() Function to get the maximum available level.
	 * @return Current user level
	 */
	int getLevel();
	
	/**
	 * Get the Level the user is able to play.
	 * @return Biggest available level
	 */
	int getMaxUnlockedLevel();
	
	/**
	 * Get how many points in the current level the user has.
	 * 
	 * It is worth to mention that these are not persistently saved.
	 * The user has to restart the given level from 0 points each time the app starts.
	 * @return points in the current level.
	 */
	int getPoints();
	/**
	 * What type of site is this currently
	 * @return sitetype of the current phish
	 */
	PhishSiteType getSiteType();
	
	/**
	 * What type of attack is this currently
	 * @return attacktype of the current phish
	 */
	PhishAttackType getAttackType();
	
	/**
	 * This function is called when the user chooses weather this URL is a phish or not 
	 * @param accptance true of the user thinks this is a phish. false otherwise.
	 * @return a {@link PhishResult} enum representing the state of the phish
	 */
	PhishResult userClicked(boolean accptance);
	/**
	 * This function must be called when the user selects a part of the URL as phishy.
	 * @param part the part that the user suspects to be an attack.
	 * @return true of the user clicked the correct part. False otherwise
	 */
	boolean partClicked(int part);
	
	/**
	 * When the main activity receives an URI via an Indent pass it to this function so that the backend can handle it.
	 * @param data The recept URI
	 */
	void onUrlReceive(Uri data);
	
	/**
	 * The user clicks on the Google+ signin button.
	 */
	void signIn();
	/**
	 * The user clicks on the Google+ signout button.
	 */
	void signOut();
	
	/**
	 * How many URLs must the user answer in this Level
	 * @return An Integer representing the number of URLs in this level
	 */
	public int levelURLs();
	
	/**
	 * How Many Phishes are in this level
	 * @return The number of Phishes in this level
	 */
	public int levelPhishes();
	
	/**
	 * How many phishes do you have to find to get to the next Level
	 * @return needed phishes for level completion
	 */
	public int nextLevelPhishes();
	
	/**
	 * How Many URLs has the user found.
	 * @return how many urls has the user seen.
	 */
	public int doneURLs();
	
	/**
	 * 
	 * @return How many phishes has the user found.
	 */
	public int foundPhishes();
	
	/**
	 * This function restarts the current Level
	 */
	public void restartLevel();
}
