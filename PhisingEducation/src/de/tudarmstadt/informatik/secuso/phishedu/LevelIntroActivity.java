package de.tudarmstadt.informatik.secuso.phishedu;

import android.content.Intent;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import de.tudarmstadt.informatik.secuso.phishedu.R;

/**
 * 
 * @author Gamze Canova This class covers the awareness part of the app This
 *         Activity should only be invoked if the user has not done this part
 *         before
 */
public class LevelIntroActivity extends SwipeActivity {
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
	
	@Override
	protected void checkAndHideButtons(int totalPages, int nextPage) {
		super.checkAndHideButtons(totalPages, nextPage);
		if (nextPage == totalPages - 1) {
			bStartLevel.setVisibility(View.VISIBLE);
		}
	}
	
	public int level=1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.level=getIntent().getIntExtra(Constants.LEVEL_EXTRA_STRING,0);
		bStartLevel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onStartClick();				
			}
		});
	}
	
	protected void onStartClick(){
		Intent levelIntent = new Intent(this, URLTaskActivity.class);
		levelIntent.putExtra(Constants.LEVEL_EXTRA_STRING, level);
		startActivity(levelIntent);
	}
	
	protected int getLayoutLevel(){
		return Math.min(this.level, this.getLayouts().length-1);
	}
	
	@Override
	protected int getPageCount() {
		return this.getLayouts()[getLayoutLevel()].length;
	}

	@Override
	protected View getPage(int page, LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(this.getLayouts()[getLayoutLevel()][page],	container, false);
	}

}