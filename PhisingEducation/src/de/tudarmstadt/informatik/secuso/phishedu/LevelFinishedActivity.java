package de.tudarmstadt.informatik.secuso.phishedu;

import android.app.ActionBar;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import de.tudarmstadt.informatik.secuso.phishedu.backend.BackendController;

public class LevelFinishedActivity extends SwipeActivity {
	ActionBar ab;
	protected static int[][] levelLayoutIds = {
		{
			R.layout.level_00_finish_00,
			R.layout.level_00_finish_01,
			R.layout.level_00_finish_02,
			R.layout.level_00_finish_03,
			R.layout.level_00_finish_04,
			R.layout.level_00_finish_05,
			R.layout.level_00_finish_06,
			R.layout.level_00_finish_07,
			R.layout.level_00_finish_08
		},
		{
			R.layout.level_01_finish_00,
		},
		{
			R.layout.level_02_finish_00,
		}
	};
	
	int real_level=0;
	int index_level=0;
	
	protected void onCreate(Bundle savedInstanceState) {
		this.real_level = getIntent().getIntExtra(Constants.EXTRA_LEVEL,0);
		this.index_level=Math.min(real_level,levelLayoutIds.length-1);
		
		super.onCreate(savedInstanceState);
	}

	protected void onStartClick(){
		BackendController.getInstance().startLevel(real_level+1);
	}

	@Override
	protected String startButtonText() {
		return "Weiter zu Level "+(real_level+1);
	}

	@Override
	protected int getPageCount() {
		return this.levelLayoutIds[index_level].length;
	}

	@Override
	protected View getPage(int page, LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		setTitles();
		return inflater.inflate(this.levelLayoutIds[index_level][page], container, false);
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
