package de.tudarmstadt.informatik.secuso.phishedu;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import de.tudarmstadt.informatik.secuso.phishedu.backend.BackendController;

public class YouGuessedActivity extends SwipeActivity {
	
	//int level; is used as index for the consequences type
	
	protected static int[] consequencesLayoutIds = {
		R.layout.you_guessed
	};
	
	protected void onStartClick(){
		setResult(RESULT_OK);
		finish();
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

	@Override
	protected int getPageCount() {
		return consequencesLayoutIds.length;
	}

	@Override
	protected View getPage(int page, LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(consequencesLayoutIds[page], container);
	}

		
}
