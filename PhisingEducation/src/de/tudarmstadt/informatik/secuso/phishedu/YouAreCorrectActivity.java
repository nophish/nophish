package de.tudarmstadt.informatik.secuso.phishedu;

import de.tudarmstadt.informatik.secuso.phishedu.backend.BackendController;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class YouAreCorrectActivity extends SwipeActivity {
	
	//int level; is used as index for the consequences type
	
	protected static int[] consequencesLayoutIds = {
		R.layout.you_are_correct
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
		TextView urlText = (TextView) view.findViewById(R.id.your_are_correct_02);
		setUrlText(urlText);
		return view;
	}

	private void setUrlText(TextView urlText) {
		String[] urlParts = BackendController.getInstance().getUrl();
		StringBuilder builder = new StringBuilder();
		
		for(int i=0; i< urlParts.length; i++){
			String urlpart = urlParts[i];
			builder.append(urlpart);
		}
		
		urlText.setText(builder.toString());
	}
	
}
