package de.tudarmstadt.informatik.secuso.phishedu;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.HorizontalScrollView;
import android.widget.TextView;

import de.tudarmstadt.informatik.secuso.phishedu.backend.BackendControllerImpl;
import de.tudarmstadt.informatik.secuso.phishedu.backend.PhishURL;

public class URLTaskActivity extends PhishBaseActivity {

	private TextView urlText;
	private TextView countdownText;
	private TextView providerText;
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
		this.providerText = (TextView) v.findViewById(R.id.url_task_providername);
		this.countdownText = (TextView) v.findViewById(R.id.time_couter);
		
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
		PhishURL url = BackendControllerImpl.getInstance().getUrl();
		String[] urlArray = url.getParts();

		// build string from array
		StringBuilder sb = new StringBuilder();
		// adds 9 character string at beginning
		for (int i = 0; i < urlArray.length; i++) {
			sb.append(urlArray[i]);
		}

		urlText.setText(sb.toString());
		
		String providertext = getResources().getString(R.string.url_task_providername);
		this.providerText.setText(providertext.replace("${providername}", url.getProviderName()));
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
		BackendControllerImpl.getInstance().userClicked(acceptance);
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
	
	@Override
	public void updateUI() {
		super.updateUI();
		int remaining_seconds = BackendControllerImpl.getInstance().remainingSeconds();
		if(remaining_seconds>=0){
			countdownText.setText(Integer.toString(remaining_seconds));
            if(remaining_seconds<=10){
                Animation anim = new AlphaAnimation(0.0f, 1.0f);
                anim.setDuration(250); //You can manage the time of the blink with this parameter
                anim.setStartOffset(20);
                anim.setRepeatMode(Animation.REVERSE);
                anim.setRepeatCount(Animation.INFINITE);
                countdownText.startAnimation(anim);
                countdownText.setTextColor(Color.RED);
            }else{
                countdownText.clearAnimation();
                countdownText.setTextColor(Color.BLACK);
            }
			countdownText.setVisibility(View.VISIBLE);
		}else{
			countdownText.setVisibility(View.INVISIBLE);
		}
		
	}

    @Override
    public void onPause() {
        super.onPause();
        BackendControllerImpl.getInstance().pauseTimer();
    }

    @Override
    public void onStart() {
        super.onStart();
        BackendControllerImpl.getInstance().resumeTimer();
    }
}
