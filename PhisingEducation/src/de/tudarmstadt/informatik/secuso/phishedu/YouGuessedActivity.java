package de.tudarmstadt.informatik.secuso.phishedu;

import android.os.Bundle;
import de.tudarmstadt.informatik.secuso.phishedu.backend.BackendController;

public class YouGuessedActivity extends CategorySwipeActivity {
	
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
	
	protected void onStartClick(){
		setResult(RESULT_OK);
		finish();
	}

	@Override
	protected int getCategory() {
		return this.type;
	}

	@Override
	protected String startButtonText() {
		return "Nächster Versuch";
	}
	
	/**
	 * Disable back button so he can not guess again.
	 */
	@Override
	public void onBackPressed() {
		return;
	}
	
}
