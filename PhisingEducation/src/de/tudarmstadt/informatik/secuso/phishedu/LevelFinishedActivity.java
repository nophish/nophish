package de.tudarmstadt.informatik.secuso.phishedu;

import de.tudarmstadt.informatik.secuso.phishedu.backend.BackendController;

public class LevelFinishedActivity extends LevelIntroActivity {
	protected static int[][] levelLayoutIds = {
		{
			R.layout.awareness_final_01,
			R.layout.awareness_final_02,
			R.layout.awareness_final_03,
			R.layout.awareness_final_04_lets_start
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
