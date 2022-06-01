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

/**
 * This enum shows what was the outcome of the given click of the user.
 * Based on this the Frontend can show success or fail messages.
 * @author Clemens Bergmann <cbergmann@schuhklassert.de>
 *
 */
public enum PhishResult {
	/**
	 * The user correctly detected and identified a phish
	 */
	Phish_Detected(0),
	/**
	 * The user Detected that it was no phish
	 */
	NoPhish_Detected(1),
	/**
	 * The user could not identify the phish
	 */
	Phish_NotDetected(2),
	/**
	 * The user identified a no-phish URL as phishy
	 */
	NoPhish_NotDetected(3),
	/**
	 * The user did not click in time
	 */
	TimedOut(4),
	/**
	 * The user did not click in time
	 */
	Guessed(5);
	
	private int value;

	private PhishResult(int value) {
		this.value = value;
	}
	
	/**
	 * Get the integer value of the current result.
	 * This is imported when using this enum for indexing arrays.
	 * @return the integer Value of the current result
	 */
	public int getValue() {
        return value;
    }
	
	/**
	 * always gets the maximum of all possible values
	 * @return The maximum of all Values.
	 */
	public static int getMax(){
		int max=-1;
		for (PhishResult this_result : PhishResult.values()) {
			max=Math.max(max, this_result.getValue());
		}
		return max;
	}
}
