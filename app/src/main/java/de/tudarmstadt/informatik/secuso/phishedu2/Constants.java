/*=========================================================================
 * The most reliable way to detect phishing is checking the URL
 * (web address) of a website. We developed an Android app to learn how
 * to detect Phishing URLs.
 * Copyright (C) 2015 SECUSO
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

package de.tudarmstadt.informatik.secuso.phishedu2;


public class Constants {
	/**
	 * Game configuration
	 */
	public static final boolean UNLOCK_ALL_LEVELS = false;
	public static final boolean ALLOW_REPEAT_AWARENESS = true;
	public static final boolean ALLOW_SKIP_AWARENESS = true;
    public static final boolean FORCE_SKIP_AWARENESS = true;
	public static final boolean SHOW_GMAIL_BUTTON = false;
	public static final boolean SHOW_STARS = true;
	public static final boolean SKIP_LEVEL1 = false;
	public static final int PROOF_IN_ROW = 6;
	public static final int RANDOM_PROOF_PERCENTAGE = 20;

	public static final String ARG_PAGE_NUMBER = "page";
	public static final String ARG_RESULT = "result";
	public static final String ARG_LEVEL = "level";
	public static final String ARG_ENABLE_HOME = "enable_home";
	
	/**
	 * How many URLs do we try to get new urls after giving up.
	 */
	public static final int ATTACK_RETRY_URLS = 10;
}
