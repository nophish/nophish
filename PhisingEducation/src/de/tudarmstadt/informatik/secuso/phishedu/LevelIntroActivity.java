package de.tudarmstadt.informatik.secuso.phishedu;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import de.tudarmstadt.informatik.secuso.phishedu.R;
import de.tudarmstadt.informatik.secuso.phishedu.backend.BackendController;

/**
 * 
 * @author Gamze Canova This class covers the awareness part of the app This
 *         Activity should only be invoked if the user has not done this part
 *         before
 */
public class LevelIntroActivity extends SwipeActivity {
	private ActionBar ab;
	

	protected static int[][] levelLayoutIds = {
			{ R.layout.level_00_intro_00, 
			  R.layout.level_00_intro_01 
			},
			{ 
			  R.layout.level_01_splash,
			  R.layout.level_01_intro_00, 
			  R.layout.level_01_intro_01 
			},
			{ R.layout.level_02_splash,
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
			},
			
			{ R.layout.level_03_splash,
			  R.layout.level_03_intro_00, 
			  R.layout.level_03_intro_01,
			  R.layout.level_03_intro_02,
			  R.layout.level_03_intro_03
			}, 
			{ R.layout.level_04_splash,
		      R.layout.level_04_intro_00,
			  R.layout.level_04_intro_01, 
			  R.layout.level_04_intro_02, 
			  R.layout.level_04_intro_03
			},
			{
			  R.layout.level_05_intro_00,
			  R.layout.level_05_intro_01,
			  R.layout.level_05_intro_02
			},
			{
			  R.layout.level_06_intro_00,
			  R.layout.level_06_intro_01,
			  R.layout.level_06_intro_02
			}
			};


	public int real_level = 0;
	public int index_level = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		this.real_level = getIntent().getIntExtra(Constants.LEVEL_EXTRA_STRING,
				0);
		this.index_level = Math.min(this.real_level, levelLayoutIds.length - 1);
		
		super.onCreate(savedInstanceState);
	}

	protected void onStartClick() {
		if (this.real_level == 0) {
			Intent levelIntent = new Intent(this, AwarenessActivity.class);
			levelIntent.putExtra(Constants.LEVEL_EXTRA_STRING, this.real_level);
			startActivity(levelIntent);
		} else if (this.real_level == 1) {
			BackendController.getInstance().redirectToLevel1URL();
		} else {
			Intent levelIntent = new Intent(this, URLTaskActivity.class);
			levelIntent.putExtra(Constants.LEVEL_EXTRA_STRING, this.real_level);
			startActivity(levelIntent);
		}
	}

	@Override
	protected String startButtonText() {
		return "Start Level";
	}

	@Override
	protected int getPageCount() {
		return this.levelLayoutIds[this.index_level].length;
	}

	@Override
	protected View getPage(int page, LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {

		setTitles();
		
		return inflater.inflate(this.levelLayoutIds[this.index_level][page],
				container, false);
	}

	
	private void setTitles() {
		ab = getActionBar();
		String title = getString(Constants.levelTitlesIds[this.real_level]);
		String subtitle = getString(Constants.levelSubtitlesIds[this.real_level]);
		
		if(!title.equals(subtitle)){
			//if subtitle and title are different, subtitle is set
			ab.setSubtitle(subtitle);
		}
		//title is set in anyway
		ab.setTitle(title);
	}

}
