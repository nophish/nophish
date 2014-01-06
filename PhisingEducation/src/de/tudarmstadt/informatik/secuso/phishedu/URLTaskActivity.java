package de.tudarmstadt.informatik.secuso.phishedu;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.TextView;
import de.tudarmstadt.informatik.secuso.phishedu.backend.BackendControllerImpl;
import de.tudarmstadt.informatik.secuso.phishedu.backend.MainActivity;
import de.tudarmstadt.informatik.secuso.phishedu.backend.PhishResult;

public class URLTaskActivity extends PhishBaseActivity {

	private TextView urlText;
	private View v;
	
	boolean enableRestartButton(){return true;};

	@Override
	public View getLayout(LayoutInflater inflater, ViewGroup container,	Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.urltask_task, container, false);
		this.urlText = (TextView) v.findViewById(R.id.url_task_url);
		setUrlSize();

		nextURL();

		final HorizontalScrollView scroll = (HorizontalScrollView) v.findViewById(R.id.url_horizintal_sv);
		scroll.post(new Runnable() {            
			@Override
			public void run() {
				scroll.fullScroll(View.FOCUS_RIGHT);              
			}
		});

		this.v=v;

		return v;
	}

	private void setUrlSize() {
		float textSize = urlText.getTextSize();

		switch (getLevel()) {
		case 0:
			// should not reach this code, as urltask is called beginning from
			// level 2
			break;
		case 1:
			// should not reach this code, as urltask is called beginning from
			// level 2
			break;
		case 2:
			textSize = 25;
			break;

		case 3:
			textSize = 25;
			break;
		case 4:
			textSize = 20;
			break;
		case 5:
			textSize = 18;
			break;
		case 6:
			textSize = 15;
			break;
		case 7:
			textSize = 13;
			break;
		default:
			textSize = 13;
			break;
		}
		urlText.setTextSize(textSize);
	}

	int getTitle(){
		return BackendControllerImpl.getInstance().getLevelInfo(getLevel()).titleId;
	};
	int getIcon(){
		return R.drawable.desktop;
	}

	@Override
	int getSubTitle() {
		return R.string.exercise;
	}

	private void nextURL() {
		BackendControllerImpl.getInstance().nextUrl();
		String[] urlArray = BackendControllerImpl.getInstance().getUrl().getParts();

		// build string from array
		StringBuilder sb = new StringBuilder();
		// adds 9 character string at beginning
		for (int i = 0; i < urlArray.length; i++) {
			sb.append(urlArray[i]);
		}

		urlText.setText(sb.toString());
	}

	@Override
	public int[] getClickables() {
		return new int[]{
				R.id.url_task_check_mark,
				R.id.url_task_cross,
		};
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.url_task_check_mark:
			clicked(true);
			break;
		case R.id.url_task_cross:
			clicked(false);
			break;
		}
	}

	private void clicked(boolean acceptance) {
		PhishResult result = BackendControllerImpl.getInstance().userClicked(
				acceptance);
		Class followActivity = ResultActivity.class;
		//In Level 10 (HTTP) we don't show proof activity.
		boolean show_proof = BackendControllerImpl.getInstance().showProof();
		if(result == PhishResult.Phish_Detected ){
			if (show_proof) {
				followActivity = ProofActivity.class;
			}
		}
		Bundle args = new Bundle();
		args.putInt(Constants.ARG_RESULT, result.getValue());
		((MainActivity)getActivity()).switchToFragment(followActivity,args);
	}

	@Override
	public void onBackPressed() {
		levelCanceldWarning();
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if (getLevel() == 2) {
			switchToFragment(ProofActivity.class);
		}
	}
}
