/*=========================================================================
 * The most reliable way to detect phishing is checking the URL
 * (web address) of a website. We developed an Android app to learn how
 * to detect Phishing URLs.
 * Copyright (C) 2015 SecUSo
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *=========================================================================*/

package de.tudarmstadt.informatik.secuso.phishedu2.backend;

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
	
	/**
	 * update the current layout.
	 */
	void updateUI();
	
	/**
	 * Show the result view for the given result
	 */
	void resultView(PhishResult result);
	
	/**
	 * vibrate for milisecods miliseconds
	 * @param miliseconds how long should we vibrate
	 */
	void vibrate(long miliseconds);
	
	void showMainMenu();
	
	void showProofActivity();
}
