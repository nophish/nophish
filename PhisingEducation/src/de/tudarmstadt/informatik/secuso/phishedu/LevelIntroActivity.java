package de.tudarmstadt.informatik.secuso.phishedu;

import android.content.Intent;

import android.os.Bundle;
import de.tudarmstadt.informatik.secuso.phishedu.R;

/**
 * 
 * @author Gamze Canova This class covers the awareness part of the app This
 *         Activity should only be invoked if the user has not done this part
 *         before
 */
public class LevelIntroActivity extends CategorySwipeActivity {
	protected static int[][] levelLayoutIds = {
		{}, //level0 does not have standard layouts
		{
			R.layout.level_01_intro_00,
			R.layout.level_01_intro_01
		},{
			R.layout.level_02_intro_00,
			R.layout.level_02_intro_01,
			R.layout.level_02_intro_02,
			R.layout.level_02_intro_03,
			R.layout.level_02_intro_04,
			R.layout.level_02_intro_05,
			R.layout.level_02_intro_06,
			R.layout.level_02_intro_07,
			R.layout.level_02_intro_08,
			R.layout.level_02_intro_09,
			R.layout.level_02_intro_10,
			R.layout.level_02_intro_11 
		},{
			R.layout.level_03_intro_00,
			R.layout.level_03_intro_01,
			R.layout.level_03_intro_02
		},{
			R.layout.level_04_intro_00, 
			R.layout.level_04_intro_01,
			R.layout.level_04_intro_02
		}
	};
	
	protected int[][] getLayouts(){
		return levelLayoutIds;
	}
	
	public int level=1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.level=getIntent().getIntExtra(Constants.LEVEL_EXTRA_STRING,0);
	}

	@Override
	protected int getCategory() {
		return this.level;
	}
	
	protected void onStartClick(){
		Intent levelIntent = new Intent(this, URLTaskActivity.class);
		levelIntent.putExtra(Constants.LEVEL_EXTRA_STRING, this.getCategory());
		startActivity(levelIntent);
	}

	@Override
	protected String startButtonText() {
		return "Start Level";
	}
	
}