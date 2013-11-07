package de.tudarmstadt.informatik.secuso.phishedu;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import de.tudarmstadt.informatik.secuso.phishedu.backend.BackendController;

public class ConsequencesActivity extends SwipeActivity {
	
	//int level; is used as index for the consequences type
	
	protected static int[][] consequencesLayoutIds = {
		{
			R.layout.consequences_social
		}
	};
	
	int type;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		int want_type = getIntent().getIntExtra(Constants.TYPE_EXTRA_STRING,0);
		this.type=Math.min(want_type,consequencesLayoutIds.length-1);
	}
	
	protected void onStartClick(){
		setResult(RESULT_OK);
		finish();
	}

	@Override
	protected String startButtonText() {
		return "Nächster Versuch";
	}

	@Override
	protected int getPageCount() {
		return consequencesLayoutIds[this.type].length;
	}

	@Override
	protected View getPage(int page, LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(consequencesLayoutIds[this.type][page], container);
	}
	
}
