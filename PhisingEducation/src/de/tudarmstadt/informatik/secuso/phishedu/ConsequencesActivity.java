package de.tudarmstadt.informatik.secuso.phishedu;

import android.os.Bundle;
import de.tudarmstadt.informatik.secuso.phishedu.backend.BackendController;

public class ConsequencesActivity extends CategorySwipeActivity {
	
	//int level; is used as index for the consequences type
	
	protected static int[][] consequencesLayoutIds = {
		{
			R.layout.consequences_social
		}
	};
	
	int type;
	
	protected int[][] getLayouts(){
		return consequencesLayoutIds;
	}
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.type=getIntent().getIntExtra(Constants.TYPE_EXTRA_STRING,0);
	}
	
	
	@Override
	protected void checkAndHideButtons(int totalPages, int nextPage) {
		super.checkAndHideButtons(totalPages, nextPage);
	}
	
	protected void onStartClick(){
		BackendController.getInstance().startLevel(type+1);
	}

	@Override
	protected int getCategory() {
		return this.type;
	}

	@Override
	protected String startButtonText() {
		return "Weiter zu Level "+(type+1);
	}
	
}
