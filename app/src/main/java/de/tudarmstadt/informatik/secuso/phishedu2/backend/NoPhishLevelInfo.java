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

import de.tudarmstadt.informatik.secuso.phishedu2.Constants;
import de.tudarmstadt.informatik.secuso.phishedu2.R;
import de.tudarmstadt.informatik.secuso.phishedu2.backend.generator.BaseGenerator;
import de.tudarmstadt.informatik.secuso.phishedu2.backend.generator.KeepGenerator;

/**
 * This Class represents the information about a Level
 */
public class NoPhishLevelInfo {
	private static final double LEVEL_DISTANCE = 1.5;
	public static final int FIRST_REPEAT_LEVEL = 4;

	private static final int[] levelOutros = { 0, 0, R.string.level_02_outro };
	
	public int getLevelTime(){
		return levelTimes[Math.min(this.levelId, levelTimes.length-1)];
	}

	public float getURLTextsize() {
		float textSize = 0;

		switch (this.levelId) {
		case 0:
			// should not reach this code, as urltask is called beginning from
			// level 2
			break;
		case 1:
			// should not reach this code, as urltask is called beginning from
			// level 2
			break;
		case 2:
			textSize = 25;
			break;
		case 3:
			textSize = 25;
			break;
		case 4:
			textSize = 20;
			break;
		default:
			// this is the default for the android browser. We don't go below
			// this.
			textSize = 18;
			break;
		}

		return textSize;
	}
	
	private static final Integer[] levelTimes = {
		0, //not used; intro level
		0, //not used; intro level
		0,
		0,
		0,
		0,
		0,
		0,
		0,
		0,
		0,
		0
	};

	private static final int[] levelTitlesIds = { R.string.level_title_00,
			R.string.level_title_01, R.string.level_title_02,
			R.string.level_title_03, R.string.level_title_04,
			R.string.level_title_05, R.string.level_title_06,
			R.string.level_title_07, R.string.level_title_08,
			R.string.level_title_09, R.string.level_title_10,
			R.string.level_title_11 };

	private static final int[] levelSubtitlesIds = {
			R.string.level_subtitle_00, R.string.level_subtitle_01,
			R.string.level_subtitle_02, R.string.level_subtitle_03,
			R.string.level_subtitle_04, R.string.level_subtitle_05,
			R.string.level_subtitle_06, R.string.level_subtitle_07,
			R.string.level_subtitle_08, R.string.level_subtitle_09,
			R.string.level_subtitle_10, R.string.level_subtitle_11 };

	private static final Integer[][] levelSplashLayoutIds = {
		{ R.layout.level_00_splash},
		{ R.layout.level_01_splash},
		{ R.layout.level_02_splash},
		{ R.layout.level_03_splash},
		{ R.layout.level_04_a_splash},
		{ R.layout.level_04_b_splash},
		{ R.layout.level_05_splash},
		{ R.layout.level_06_splash},
		{ R.layout.level_07_splash},
		{ R.layout.level_08_splash},
		{ R.layout.level_10_splash},
		{ R.layout.level_11_splash_00},
	};
	
	private static final Integer[][] levelRepeatLayoutIds = {
		{},
		{},
		{},
		{R.layout.level_03_intro_02}, //IP Adressen-Level
		{R.layout.level_04_a_intro_01}, // Totally unrelated level: hier muss IPErinnerung rein repeat rein.
		{R.layout.level_04_b_intro_01},	//Erinnerung: Subdomains, IP, totally unrelated
		{R.layout.level_05_intro_01}, 
		{R.layout.level_06_intro_01},
		{R.layout.level_07_intro_01},
		{R.layout.level_08_intro_01},
		{R.layout.level_10_intro_01},
		{}
	};
	
	private static final Integer[][] levelIntroLayoutIds = {
			{ R.layout.level_00_intro_00, R.layout.level_00_intro_01 },
			{R.layout.level_01_intro_00,
					R.layout.level_01_intro_01, R.layout.level_01_intro_02,
					R.layout.level_01_intro_03 ,
                    R.layout.level_01_finish_00 },
			{R.layout.level_02_intro_00,
					R.layout.level_02_intro_01, R.layout.level_02_intro_02,
					R.layout.level_02_intro_03, R.layout.level_02_intro_04,
					R.layout.level_02_intro_05, R.layout.level_02_intro_06,
					R.layout.level_02_intro_07, R.layout.level_02_intro_08,
					R.layout.level_02_intro_09, R.layout.level_02_intro_10, },
			{ R.layout.level_03_intro_00, R.layout.level_03_intro_01,
						R.layout.level_03_intro_03 },
			{ R.layout.level_04_a_intro_00, R.layout.level_04_a_intro_02},
			{ R.layout.level_04_b_intro_00, R.layout.level_04_b_intro_02,
					R.layout.level_04_b_intro_03 },
			{ R.layout.level_05_intro_00, R.layout.level_05_intro_02,
						R.layout.level_05_intro_03 },			
			{ R.layout.level_06_intro_02, R.layout.level_06_intro_03 },
			{ R.layout.level_07_intro_00, R.layout.level_07_intro_02},		//typo
						//R.layout.level_07_intro_02, R.layout.level_06_intro_05,
						//R.layout.level_06_intro_06 },	
			{ R.layout.level_08_intro_00, R.layout.level_08_intro_02 },
			{ R.layout.level_10_intro_00, R.layout.level_10_intro_02,
					R.layout.level_10_intro_03, R.layout.level_10_intro_04,
					R.layout.level_10_intro_05, R.layout.level_10_intro_06,
					R.layout.level_10_intro_07, R.layout.level_10_intro_08,
					R.layout.level_10_intro_09, },
			{ R.layout.level_11_intro_00,
					R.layout.level_11_splash_04, R.layout.level_11_intro_09,
					R.layout.level_11_splash_03, R.layout.level_11_intro_08,
					R.layout.level_11_splash_01, R.layout.level_11_intro_01,
					R.layout.level_11_intro_02, R.layout.level_11_intro_04,
					R.layout.level_11_intro_03, R.layout.level_11_splash_02,
					R.layout.level_11_intro_05, R.layout.level_11_intro_06,
					R.layout.level_11_intro_07}};

	private static final Integer[][] levelFinishedLayoutIds = {
			{ R.layout.level_00_finish_00a, R.layout.level_00_finish_00,
					R.layout.level_00_finish_01, R.layout.level_00_finish_02,
					R.layout.level_00_finish_03, R.layout.level_00_finish_04,
					R.layout.level_00_finish_05, R.layout.level_00_finish_07,
					R.layout.level_00_finish_06, R.layout.level_00_finish_08 },
			{ R.layout.level_01_finish_00, }, { R.layout.level_02_finish_00, } };

	// For each level we can define what Attacks are applied
	// LEVEL 0-1 are empty because they don't
	public static final PhishAttackType[][] levelAttackTypes = { {}, // Level 0:
																		// Awareness
			{}, // Level 1: Find URLBar in Browser
			{ PhishAttackType.FindDomain }, // Level 1
			{ PhishAttackType.IPNoBrand }, // Level 2
			{ PhishAttackType.TotallyUnrelated }, // Level 3
			{ PhishAttackType.Subdomain}, // Level 4
			{ PhishAttackType.HostInPath }, // Level 5
			{ PhishAttackType.Misleading }, // Level 6
			{ PhishAttackType.Typo}, // Level 7
			{ PhishAttackType.Homoglyphic}, // Level 8
			{ PhishAttackType.HTTP } // Level 9
	};

	public boolean hasAttack(PhishAttackType attack) {
		for (PhishAttackType attacktype : this.attackTypes) {
			if (attacktype == attack) {
				return true;
			}
		}
		return false;
	}

	@SuppressWarnings("rawtypes")
	private static Class[][] levelGenerators = {
	// Currently we use the same generators for all levels
	{ KeepGenerator.class }, };

	public static int levelCount() {
		return levelIntroLayoutIds.length;
	}

	public final int titleId;
	public final int subTitleId;
	public final int outroId;
	public final Integer[] splashLayouts;
	public final Integer[] repeatLayouts;
	public final Integer[] introLayouts;
	public final Integer[] finishedLayouts;
	public final int levelId;
	public final PhishAttackType[] attackTypes;
	public final Class<? extends BaseGenerator>[] generators;
	
	public String getLevelNumber(){
		if (this.isIntroLevel()) {
			return "E" + (this.levelId + 1);
		} else {
			return Integer.toString(this.levelId - 1);
		}
	}
	
	public boolean isOutroLevel(){
		return (this.levelId == BackendControllerImpl.getInstance().getLevelCount()-1);
	}
	
	public boolean showStars(){
		return Constants.SHOW_STARS && (! (isIntroLevel() || isOutroLevel()));
	}

	@SuppressWarnings("unchecked")
	public NoPhishLevelInfo(int levelid) {
		this.levelId = levelid;
		this.titleId = levelTitlesIds[levelid];
		this.subTitleId = levelSubtitlesIds[levelid];
		int intro_index = Math.min(levelid, levelIntroLayoutIds.length - 1);
		this.splashLayouts = levelSplashLayoutIds[intro_index];
		this.repeatLayouts = levelRepeatLayoutIds[intro_index];
		this.introLayouts = levelIntroLayoutIds[intro_index];
		int finished_index = Math.min(levelid,
				levelFinishedLayoutIds.length - 1);
		this.finishedLayouts = levelFinishedLayoutIds[finished_index];
		int attacktype_index = Math.min(levelid, levelAttackTypes.length - 1);
		this.attackTypes = levelAttackTypes[attacktype_index];
		
		if (levelid < levelOutros.length) {
			this.outroId = levelOutros[levelid];
		} else {
			this.outroId = 0;
		}
		int geneator_index = Math.min(levelid, levelGenerators.length - 1);
		this.generators = levelGenerators[geneator_index];
	}
	
	public boolean isIntroLevel(){
		return this.levelId < 2;
	}

	public int getLevelmaxPoints(){
		//TODO: DEFAULT_CORRECT_POINTS is the standard value for correctly identified URLs
		//The backend of the program is able to handle different Points per URL.
		//When using this feature this function must be changed accordingly
		return this.weightLevelPoints(PhishURL.DEFAULT_CORRECT_POINTS)*levelCorrectURLs();
	}
	
	public int weightLevelPoints(int base_points) {
		return (int) (base_points * Math.pow(LEVEL_DISTANCE, levelId));
	}

	public int levelCorrectURLs() {
		if (levelId == 2) {
			return 5;
		}
		return 6 + (2 * this.levelId);
	}

	public int levelPhishes() {
		int base_phishes = 0;
		if (this.levelId == 2) {
			base_phishes = levelCorrectURLs();
		} else {
			base_phishes = levelCorrectURLs() / 2;
		}
		return base_phishes;
	}

	public int levelRepeats() {
		int result = 0;
		if (levelId >= FIRST_REPEAT_LEVEL) {
			result = (int) Math.floor(this.levelPhishes() / 2);
		}
		return result;
	}
}
