package de.tudarmstadt.informatik.secuso.phishedu;

import android.app.ActionBar;
import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import de.tudarmstadt.informatik.secuso.phishedu.backend.BackendController;
import de.tudarmstadt.informatik.secuso.phishedu.backend.PhishResult;

public class ResultActivity extends SwipeActivity {
	public static int RESULT_GUESSED = PhishResult.getMax() + 1;

	protected static int[] reminderIDs = { R.string.level_03_reminder,
			R.string.level_04_reminder, R.string.level_05_reminder,
			R.string.level_06_reminder, R.string.level_07_reminder,
			R.string.level_08_reminder, R.string.level_09_reminder,
			R.string.level_10_reminder };

	// int level; is used as index for the consequences type

	protected static int[] resultLayoutIDs;
	private int result = PhishResult.Phish_Detected.getValue();
	private int site_type = 0;
	private int attack_type = 0;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.result = getIntent().getIntExtra(Constants.EXTRA_RESULT, 0);
		this.site_type = getIntent().getIntExtra(Constants.EXTRA_SITE_TYPE, 0);
		this.attack_type = getIntent().getIntExtra(Constants.EXTRA_ATTACK_TYPE,
				0);
		setTitle();
	}

	public ResultActivity() {
		// We need one layout for each PhishResult + You guessed
		resultLayoutIDs = new int[PhishResult.values().length + 1];
		resultLayoutIDs[PhishResult.Phish_Detected.getValue()] = R.layout.result_phish_detected;
		resultLayoutIDs[PhishResult.NoPhish_Detected.getValue()] = R.layout.result_nophish_detected;
		resultLayoutIDs[PhishResult.Phish_NotDetected.getValue()] = R.layout.result_phish_notdetected;
		resultLayoutIDs[PhishResult.NoPhish_NotDetected.getValue()] = R.layout.result_nophish_notdetected;
		resultLayoutIDs[RESULT_GUESSED] = R.layout.result_you_guessed;
	}

	protected void onStartClick() {
		setResult(RESULT_OK);
		finish();
	}

	@Override
	protected String startButtonText() {
		return "Nächster Versuch";
	}

	@Override
	protected int getPageCount() {
		return 1;
	}

	/**
	 * Disable back button so he can not guess again.
	 */
	@Override
	public void onBackPressed() {
		return;
	}

	@Override
	protected View getPage(int page, LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(resultLayoutIDs[this.result], container,
				false);
		// if result == result_nophish_notdetected -> virbration feedback
		if (this.result == PhishResult.Phish_NotDetected.getValue()) {
			vibrate();
			setReminderText(view);
		}
		if (BackendController.getInstance().getLevel() == 2) {
			setLevel2Texts(view);
		}
		TextView urlText = (TextView) view.findViewById(R.id.url);
		setUrlText(urlText);
		urlText.setTextSize(25);
		updateScore(view);
		return view;
	}

	private void setLevel2Texts(View view) {
		TextView resultText;
		if (this.result == RESULT_GUESSED) {
			// change text
			resultText = (TextView) view.findViewById(R.id.you_guessed_01);
			resultText.setText(R.string.level_02_you_are_wrong);
			// make second text disappear
			TextView resultTextToHide = (TextView) view
					.findViewById(R.id.you_guessed_02);
			resultTextToHide.setVisibility(View.INVISIBLE);

			vibrate();

			// change smile to not smiling
			ImageView image = (ImageView) view
					.findViewById(R.id.feedback_smiley);
			image.setImageResource(R.drawable.small_smiley_not_smile);
		} else if (this.result == PhishResult.Phish_Detected.getValue()) {
			resultText = (TextView) view.findViewById(R.id.your_are_correct_01);
			resultText.setText(R.string.level_02_you_are_correct);
		}
	}

	private void setReminderText(View view) {
		TextView reminderText = (TextView) view
				.findViewById(R.id.phish_not_detected_reminder);

		int indexReminder = attack_type - 3;
		if (indexReminder == 8) {
			indexReminder = 4;
		}
		if (indexReminder >= 0) {
			reminderText.setText(reminderIDs[indexReminder]);
		}
	}

	private void vibrate() {
		// make phone vibrate
		Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		v.vibrate(500);

	}

	private void setTitle() {
		ActionBar ab = getActionBar();
		if (this.result == PhishResult.Phish_Detected.getValue()
				|| this.result == PhishResult.NoPhish_Detected.getValue()) {
			ab.setTitle(getString(R.string.correct));
		} else {
			ab.setTitle(getString(R.string.wrong));
		}
		if (this.result == PhishResult.Phish_Detected.getValue()
				|| this.result == PhishResult.Phish_NotDetected.getValue()) {
			ab.setSubtitle(getString(R.string.phish));
		} else {
			ab.setSubtitle(getString(R.string.no_phish));
		}

		ab.setIcon(getResources().getDrawable(R.drawable.desktop));
	}
}
