package de.tudarmstadt.informatik.secuso.phishedu;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.ActionBar;
import android.text.SpannableStringBuilder;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import de.tudarmstadt.informatik.secuso.phishedu.backend.BackendController;
import de.tudarmstadt.informatik.secuso.phishedu.backend.BackendControllerImpl;
import de.tudarmstadt.informatik.secuso.phishedu.backend.PhishAttackType;
import de.tudarmstadt.informatik.secuso.phishedu.backend.PhishResult;

public class ResultActivity extends SwipeActivity {
	public static final int RESULT_GUESSED = PhishResult.getMax() + 1;
	SpannableStringBuilder strBuilder = new SpannableStringBuilder();
	int wordStart, wordEnd;
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
	private int level = 0;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.result = getIntent().getIntExtra(Constants.EXTRA_RESULT, 0);
		this.site_type = getIntent().getIntExtra(Constants.EXTRA_SITE_TYPE, 0);
		this.attack_type = getIntent().getIntExtra(Constants.EXTRA_ATTACK_TYPE,
				0);
		this.level = getIntent().getIntExtra(Constants.EXTRA_LEVEL, 0);
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
		// We might have failed the level
		// Either by going out of URLs or by going out of options to detect
		// phish
		switch (BackendControllerImpl.getInstance().getLevelState()) {
		case failed:
			this.levelFailed();
			break;
		case finished:
			this.levelFinished();
			break;
		default:
			nextURL();
			break;
		}
	}

	private void levelFinished() {
		Intent levelIntent = new Intent(this, LevelFinishedActivity.class);
		levelIntent.putExtra(Constants.EXTRA_LEVEL, level);
		startActivity(levelIntent);
	}

	private void levelFailed() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.level_failed_title);
		builder.setMessage(R.string.level_failed_text);
		builder.setNeutralButton(R.string.level_failed_button,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						BackendControllerImpl.getInstance().restartLevel();
					}
				});
		builder.show();
	}

	@Override
	protected String startButtonText() {
		return "Weiter";
	}

	@Override
	protected int getPageCount() {
		return 1;
	}

	/**
	 * Going back not possible, only cancel level
	 */
	@Override
	public void onBackPressed() {
		levelCanceldWarning();
	}

	@Override
	protected View getPage(int page, LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(resultLayoutIDs[this.result], container,
				false);
		// if result == result_nophish_notdetected -> virbration feedback
		if (this.result == PhishResult.Phish_NotDetected.getValue()) {
			setReminderText(view);
		}
		if (level == 2) {
			setLevel2Texts(view);
		} else if (level == 10) {
			setLevel10Texts(view);
		}
		TextView urlText = (TextView) view.findViewById(R.id.url);
		setUrlText(urlText);
		urlText.setTextSize(25);
		return view;
	}

	private void setLevel10Texts(View view) {
		TextView resultText;
		if (this.result == PhishResult.NoPhish_Detected.getValue()) {
			// change text
			resultText = (TextView) view.findViewById(R.id.your_are_correct_01);
			resultText.setText(R.string.level_10_you_are_correct);
			// make second text disappear

		} else if (this.result == PhishResult.Phish_NotDetected.getValue()) {
			resultText = (TextView) view
					.findViewById(R.id.phish_not_detected_text);
			resultText.setText(R.string.level_10_you_are_wrong);
		} else if (this.result == PhishResult.Phish_Detected.getValue()) {
			resultText = (TextView) view.findViewById(R.id.your_are_correct_01);
			resultText.setText(R.string.level_10_you_are_correct_phish);
		}
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

		// level 10 reminders need to be set specifically
		// this can be reached in level 10 either by not being an attack at all
		// (attacktype = nophish) or by being an attacktype of http
		// http + legitimate url
		if (level == 10) {
			if (attack_type == PhishAttackType.NoPhish.getValue()
					|| attack_type == PhishAttackType.HTTP.getValue()) {
				reminderText
						.setText(R.string.level_10_reminder_http_legitimate);
			} else {
				// in level 10 different texts are shown
				String urlScheme = BackendControllerImpl.getInstance().getUrl()
						.getParts()[0];
				if (urlScheme.equals("http:")) {
					reminderText.setText(R.string.level_10_reminder_http_phish);
				} else {
					reminderText
							.setText(R.string.level_10_reminder_https_phish);
				}
			}
		} else if (indexReminder >= 0) {
			if (indexReminder == 8) {
				// typo and misleading are in one level (7)
				indexReminder = 4;
			}
			reminderText.setText(reminderIDs[indexReminder]);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu items for use in the action bar
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.urltask_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		// Respond to the action bar's Up/Home button
		case android.R.id.home:
			levelCanceldWarning();
			return true;
		case R.id.restart_level:
			levelRestartWarning();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void setTitle() {
		ActionBar ab = getSupportActionBar();
		if (this.result == PhishResult.Phish_Detected.getValue()
				|| this.result == PhishResult.NoPhish_Detected.getValue()) {
			ab.setTitle(getString(R.string.correct));
		} else {
			ab.setTitle(getString(R.string.wrong));
		}
		if (this.level == 2) {
			// no subtitle in level2;
			ab.setSubtitle(null);
		} else if (this.result == PhishResult.Phish_Detected.getValue()
				|| this.result == PhishResult.Phish_NotDetected.getValue()) {
			ab.setSubtitle(getString(R.string.phish));
		} else if (this.result == RESULT_GUESSED) {
			ab.setSubtitle(R.string.marked_domain_wrongly);
		} else {
			ab.setSubtitle(getString(R.string.no_phish));
		}

		ab.setIcon(getResources().getDrawable(R.drawable.desktop));
	}

	@Override
	protected void setUrlText(TextView urlText) {
		String urlParts[] = BackendControllerImpl.getInstance().getUrl()
				.getParts();
		int domainPart = BackendControllerImpl.getInstance().getUrl()
				.getDomainPart();
		// at start clear string builder
		for (int i = 0; i < urlParts.length; i++) {

			String part = urlParts[i];
			// 0 at the beginning
			wordStart = strBuilder.length();
			wordEnd = wordStart + part.length();
			strBuilder.append(part);

			BackgroundColorSpan bgc = null;
			if (i == 0 && level == 10) {
				if (part.equals("https:")) {
					bgc = new BackgroundColorSpan(getResources().getColor(
							R.color.nophish_domain));
				} else {
					bgc = new BackgroundColorSpan(getResources().getColor(
							R.color.phish_domain));
				}
			} else if (i == domainPart) {
				// make attacked part background red
				if (level == 2) {
					bgc = new BackgroundColorSpan(getResources().getColor(
							R.color.domain));
				} else {
					if (attack_type == PhishAttackType.NoPhish.getValue()
							|| attack_type == PhishAttackType.HTTP.getValue()) {
						bgc = new BackgroundColorSpan(getResources().getColor(
								R.color.nophish_domain));
					} else {
						bgc = new BackgroundColorSpan(getResources().getColor(
								R.color.phish_domain));
					}
				}
			}
			if (bgc != null) {
				strBuilder.setSpan(bgc, wordStart, wordEnd, 0);
			}

		}
		if (urlText != null) {
			urlText.setText(strBuilder);
		}
	}

	private void nextURL() {
		Intent levelIntent = new Intent(this, URLTaskActivity.class);
		levelIntent.putExtra(Constants.EXTRA_LEVEL, this.level);
		startActivity(levelIntent);
	}

}
