package de.tudarmstadt.informatik.secuso.phishedu;

import de.tudarmstadt.informatik.secuso.phishedu.backend.BackendController;
import android.app.ActionBar;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class OversafeActivity extends SwipeActivity {
	
	//int level; is used as index for the consequences type
	
	protected static int[] consequencesLayoutIds = {
		R.layout.oversafe
	};
	
	protected void onStartClick(){
		setResult(RESULT_OK);
		finish();
	}

	@Override
	protected String startButtonText() {
		return "Nächste URL";
	}

	@Override
	protected int getPageCount() {
		return consequencesLayoutIds.length;
	}

	@Override
	protected View getPage(int page, LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		View view =inflater.inflate(consequencesLayoutIds[page], container, false);
		TextView urlText = (TextView) view.findViewById(R.id.oversafe_url);
		setTitle();
		setUrlText(urlText);
		return view;	}
	

	private void setTitle(){
		ActionBar ab = getActionBar();
		ab.setTitle(getString(R.string.oversafe));
		ab.setSubtitle(getString(R.string.no_phish));
		ab.setIcon(getResources().getDrawable(R.drawable.desktop));
	}
}
