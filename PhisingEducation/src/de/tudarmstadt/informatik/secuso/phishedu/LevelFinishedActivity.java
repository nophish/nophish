package de.tudarmstadt.informatik.secuso.phishedu;

import de.tudarmstadt.informatik.secuso.phishedu.backend.BackendController;

public class LevelFinishedActivity extends LevelIntroActivity {
	protected static int[][] levelLayoutIds = {
		{
			R.layout.level_00_finish_00,
			R.layout.level_00_finish_01,
			R.layout.level_00_finish_02,
			R.layout.level_00_finish_03
		}
	};
	
	protected int[][] getLayouts(){
		return levelLayoutIds;
	}
	
	@Override
	protected void checkAndHideButtons(int totalPages, int nextPage) {
		super.checkAndHideButtons(totalPages, nextPage);
		bStartLevel.setText("Weiter zu Level "+(level+1));
	}
	
	protected void onStartClick(){
		BackendController.getInstance().startLevel(level+1);
	}
	
}
