package de.tudarmstadt.informatik.secuso.phishedu;

public class Constants {
	/**
	 * Game configuration
	 */
	
	public static final boolean ALLOW_LEVEL0_REPLAY = true;
	
	public static final int COUNT_DOWN_INTERVAL = 1000;
	public static final int MILLIS_IN_FUTURE = 2500;
	public static final String EXTRA_LEVEL = "level";
	public static final String EXTRA_SITE_TYPE = "site_type";
	public static final String EXTRA_ATTACK_TYPE = "attack_type";
	public static final String ARG_PAGE_NUMBER = "page";
	public static final String EXTRA_RESULT = "result";

	protected static final int[] levelTitlesIds = {
			R.string.app_name, R.string.level_title_01,
			R.string.level_title_02, R.string.level_title_03,
			R.string.level_title_04, R.string.level_title_05,
			R.string.level_title_06, R.string.level_title_07,
			R.string.level_title_08, R.string.level_title_09,
			R.string.level_title_10, R.string.level_title_11 };

	protected static final int[] levelSubtitlesIds = {
			R.string.app_name, R.string.level_subtitle_01,
			R.string.level_subtitle_02, R.string.level_subtitle_03,
			R.string.level_subtitle_04, R.string.level_subtitle_05,
			R.string.level_subtitle_06, R.string.level_subtitle_07,
			R.string.level_subtitle_08, R.string.level_subtitle_09,
			R.string.level_subtitle_10, R.string.level_subtitle_11 };

}
