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
			R.layout.level_1_find_address_bar_info_1,
			R.layout.level_1_find_address_bar_info_2
		},{
			R.layout.level_2_web_addresses_01,
			R.layout.level_2_web_addresses_02,
			R.layout.level_2_web_addresses_03,
			R.layout.level_2_web_addresses_04,
			R.layout.level_2_web_addresses_05,
			R.layout.level_2_web_addresses_06,
			R.layout.level_2_web_addresses_07,
			R.layout.level_2_web_addresses_08,
			R.layout.level_2_web_addresses_09,
			R.layout.level_2_web_addresses_10,
			R.layout.level_2_web_addresses_11_a,
			R.layout.level_2_web_addresses_12_a 
		},{
			R.layout.level_3_ip_nonsense_info_01,
			R.layout.level_3_ip_nonsense_info_02,
			R.layout.level_3_ip_nonsense_info_03
		},{
			R.layout.level_4_subdomain_01, 
			R.layout.level_4_subdomain_02,
			R.layout.level_4_subdomain_03
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