package de.tudarmstadt.informatik.secuso.phishedu;

import android.app.ActionBar;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import de.tudarmstadt.informatik.secuso.phishedu.backend.BackendController;

public class LevelFinishedActivity extends SwipeActivity {
	ActionBar ab;
	protected static int[][] levelLayoutIds = {
		{
			R.layout.level_00_finish_00,
			R.layout.level_00_finish_06,
			R.layout.level_00_finish_07,
			R.layout.level_00_finish_08
		}
	};
	
	int real_level=0;
	int index_level=0;
	
	protected void onCreate(Bundle savedInstanceState) {
		this.real_level = getIntent().getIntExtra(Constants.LEVEL_EXTRA_STRING,0);
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
		setPageTitle(page);
		return inflater.inflate(this.levelLayoutIds[index_level][page], container, false);
	}
	
	private void setPageTitle(int page) {
		ab = getActionBar();
		String title; 
		switch (index_level) {
		case 0:
			title = getString(R.string.title_anti_phishing);
			break;
		default:
			title = "BLUBB";
			break;

		}
		
		ab.setTitle(title);
	}
	
}
