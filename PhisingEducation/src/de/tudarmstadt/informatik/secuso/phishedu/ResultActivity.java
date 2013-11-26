package de.tudarmstadt.informatik.secuso.phishedu;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import de.tudarmstadt.informatik.secuso.phishedu.backend.BackendController;
import de.tudarmstadt.informatik.secuso.phishedu.backend.PhishResult;

public class ResultActivity extends SwipeActivity {
	public static int RESULT_GUESSED = PhishResult.getMax() + 1;
	public static ResultActivity instance = null;

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
		instance=this;
	}

	protected void onStartClick() {
		setResult(RESULT_OK);
		int state = BackendController.getInstance().levelState();
		switch (state) {
		case BackendController.LEVEL_DONE:
			levelDone(BackendController.getInstance().getLevel());
			break;
		case BackendController.LEVEL_FAILED:
			levelFailed(BackendController.getInstance().getLevel());
			break;
		case BackendController.LEVEL_FINISHED:
			BackendController.getInstance().finishLevel();
			break;
		default:
			finish();
			break;
		}
	}
	
	public static boolean user_finish_asked=false;
	public static void resetState(){
		user_finish_asked=false;
	}
	
	public void levelFailed(int level) {
		AlertDialog.Builder builder = new AlertDialog.Builder(instance);
		builder.setTitle(R.string.level_failed_title);
		builder.setMessage(R.string.level_failed_text);
		builder.setNeutralButton(R.string.level_failed_button, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				BackendController.getInstance().restartLevel();
			}
		});
		builder.show();
	}
	
	public void levelDone(int level) {
		if(user_finish_asked){
			instance.finish();
			return;
		}
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(instance);

		// Setting Dialog Title
		alertDialog.setTitle(instance.getString(R.string.level_continue_title));

		// Setting Dialog Message
		alertDialog.setMessage(instance.getString(R.string.level_contiue_text));

		alertDialog.setPositiveButton(R.string.level_continue_positive_button,new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				user_finish_asked=true;
				instance.finish();
			}
		});

		alertDialog.setNegativeButton(R.string.level_continue_negative_button,
				new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				BackendController.getInstance().finishLevel();
			}
		});

		// Showing Alert Message
		alertDialog.show();
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

	private void vibrate() {
		// make phone vibrate
		Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		v.vibrate(500);

	}

	private void setTitle() {
		ActionBar ab = getSupportActionBar();
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
