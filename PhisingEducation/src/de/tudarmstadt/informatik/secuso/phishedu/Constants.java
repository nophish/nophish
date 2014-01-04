package de.tudarmstadt.informatik.secuso.phishedu;


public class Constants {
	/**
	 * Game configuration
	 */
	public static final boolean UNLOCK_ALL_LEVELS = false;

	public static final String ARG_PAGE_NUMBER = "page";
	public static final String ARG_RESULT = "result";
	public static final String ARG_LEVEL = "level";
	public static final String ARG_ENABLE_HOME = "enable_home";
	
	/**
	 * How many URLs do we try to get new urls after giving up.
	 */
	public static final int ATTACK_RETRY_URLS = 10;
}
