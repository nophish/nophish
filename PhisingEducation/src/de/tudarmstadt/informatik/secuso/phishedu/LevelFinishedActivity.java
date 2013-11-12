package de.tudarmstadt.informatik.secuso.phishedu;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import de.tudarmstadt.informatik.secuso.phishedu.backend.BackendController;

public class LevelFinishedActivity extends SwipeActivity {
	protected static int[][] levelLayoutIds = {
		{
			R.layout.level_00_finish_00,
			R.layout.level_00_finish_01,
			R.layout.level_00_finish_02,
			R.layout.level_00_finish_03
		}
	};
	
	int real_level=0;
	int index_leve=0;
	
	protected void onCreate(Bundle savedInstanceState) {
		this.real_level = getIntent().getIntExtra(Constants.LEVEL_EXTRA_STRING,0);
		this.index_leve=Math.min(real_level,levelLayoutIds.length-1);
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
		return this.levelLayoutIds[index_leve].length;
	}

	@Override
	protected View getPage(int page, LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(this.levelLayoutIds[index_leve][page], container);
	}
	
}
