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
	private TextView question;
	private TextView cross;
	private TextView checkmark;

	boolean enableRestartButton() {
		return true;
	};

	@Override
	public View getLayout(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.urltask_task, container, false);
		this.urlText = (TextView) v.findViewById(R.id.url_task_url);
		urlText.setTextSize(BackendControllerImpl.getInstance().getLevelInfo()
				.getURLTextsize());

		nextURL();

		if (getLevel() == 10) {
			this.question = (TextView) v
					.findViewById(R.id.url_task_description);
			this.cross = (TextView) v.findViewById(R.id.cross_text);
			this.checkmark = (TextView) v.findViewById(R.id.tik_text);
			// adjust texts
			if (question != null) {
				this.question.setText(getResources().getText(
						R.string.url_task_desciription_level_10));
			}
			if (cross != null) {
				this.cross.setText(getResources().getText(R.string.no));
			}
			if (checkmark != null) {
				this.checkmark.setText(getResources().getText(R.string.yes));
			}
		}

		final HorizontalScrollView scroll = (HorizontalScrollView) v
				.findViewById(R.id.url_horizintal_sv);
		scroll.post(new Runnable() {
			@Override
			public void run() {
				scroll.fullScroll(View.FOCUS_RIGHT);
			}
		});

		this.v = v;

		return v;
	}

	int getTitle() {
		return BackendControllerImpl.getInstance().getLevelInfo(getLevel()).titleId;
	};

	int getIcon() {
		return R.drawable.desktop;
	}

	@Override
	int getSubTitle() {
		return R.string.exercise;
	}

	private void nextURL() {
		BackendControllerImpl.getInstance().nextUrl();
		String[] urlArray = BackendControllerImpl.getInstance().getUrl()
				.getParts();

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
		return new int[] { R.id.url_task_check_mark, R.id.url_task_cross, };
	}

	@Override
	public void onClick(View view) {
		int id = view.getId();
		if (id == R.id.url_task_check_mark) {
			clicked(true);
		} else if (id == R.id.url_task_cross) {
			clicked(false);
		}
	}

	private void clicked(boolean acceptance) {
		PhishResult result = BackendControllerImpl.getInstance().userClicked(
				acceptance);
		Class followActivity = ResultActivity.class;
		// In Level 10 (HTTP) we don't show proof activity.
		boolean show_proof = BackendControllerImpl.getInstance().showProof();
		if (result == PhishResult.Phish_Detected) {
			if (show_proof) {
				followActivity = ProofActivity.class;
			}
		}
		Bundle args = new Bundle();
		args.putInt(Constants.ARG_RESULT, result.getValue());
		((MainActivity) getActivity()).switchToFragment(followActivity, args);
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
