package de.tudarmstadt.informatik.secuso.phishedu;

import android.app.ActionBar;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import de.tudarmstadt.informatik.secuso.phishedu.backend.BackendController;

public class YouAreWrongActivity extends SwipeActivity {
	
	//int level; is used as index for the consequences type
	
	protected static int[][] consequencesLayoutIds = {
		{
			R.layout.you_are_wrong
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
		
		View view = inflater.inflate(consequencesLayoutIds[this.type][page], container, false);
		
		setTitle();
		
		TextView urlText = (TextView) view.findViewById(R.id.phish_not_detected_url);
		setUrlText(urlText);
		
		TextView reminderText = (TextView) view.findViewById(R.id.phish_not_detected_reminder);
		//TODO: reminderText needs to be set according to attack type
		return view;
	}
	
	private void setTitle(){
		ActionBar ab = getActionBar();
		ab.setTitle(getString(R.string.wrong));
		ab.setSubtitle(getString(R.string.phish));
		ab.setIcon(getResources().getDrawable(R.drawable.desktop));

	}

}
