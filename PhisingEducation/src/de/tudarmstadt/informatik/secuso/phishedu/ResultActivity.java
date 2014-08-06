package de.tudarmstadt.informatik.secuso.phishedu;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.style.BackgroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import de.tudarmstadt.informatik.secuso.phishedu.backend.BackendControllerImpl;
import de.tudarmstadt.informatik.secuso.phishedu.backend.PhishAttackType;
import de.tudarmstadt.informatik.secuso.phishedu.backend.PhishResult;

public class ResultActivity extends SwipeActivity {
	public static final int RESULT_GUESSED = PhishResult.getMax() + 1;
	protected static int[] reminderIDs = { R.string.level_04_reminder,
		R.string.level_04_a_reminder, R.string.level_03_reminder, R.string.level_05_reminder,
		R.string.level_06_reminder, R.string.level_07_reminder, //typo
		R.string.level_08_reminder, R.string.level_10_reminder, R.string.level_12_reminder, R.string.level_13_reminder };

	protected static int[] resultTextIDs;
	protected static int[] resultSmileyIDs;
	private int result = PhishResult.Phish_Detected.getValue();

	@Override
	public void onSwitchTo() {
		this.setResult(getArguments().getInt(Constants.ARG_RESULT));
		super.onSwitchTo();
	}

	public ResultActivity() {
		// We need one layout for each PhishResult + You guessed
		resultTextIDs = new int[PhishResult.values().length + 2];
		
		resultTextIDs[PhishResult.Phish_Detected.getValue()] = R.string.you_found_the_phish;
		resultTextIDs[PhishResult.NoPhish_Detected.getValue()] = R.string.you_are_correct;
		resultTextIDs[PhishResult.Phish_NotDetected.getValue()] = R.string.you_are_wrong;
		resultTextIDs[PhishResult.NoPhish_NotDetected.getValue()] = R.string.oversafe_text;
		resultTextIDs[RESULT_GUESSED] = R.string.you_guessed;

		resultSmileyIDs = new int[PhishResult.values().length + 2];
		resultSmileyIDs[PhishResult.Phish_Detected.getValue()] = R.drawable.small_smiley_smile;
		resultSmileyIDs[PhishResult.NoPhish_Detected.getValue()] = R.drawable.small_smiley_smile;
		resultSmileyIDs[PhishResult.Phish_NotDetected.getValue()] = R.drawable.small_smiley_not_smile;
		resultSmileyIDs[PhishResult.NoPhish_NotDetected.getValue()] = R.drawable.small_smiley_o;
		resultSmileyIDs[RESULT_GUESSED] = R.drawable.small_smiley_you_guessed;
	}

	protected void onStartClick() {
		//We might have failed the level
		//Either by going out of URLs or by going out of options to detect phish
		switch (BackendControllerImpl.getInstance().getLevelState()) {
		case failed:
			this.levelFailed();
			break;
		case finished:
			switchToFragment(LevelFinishedActivity.class);
			break;
		default:
			switchToFragment(URLTaskActivity.class);
			break;
		}
	}

	private void levelFailed() {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
	protected View getPage(int page, LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.result, container, false);
		int level = BackendControllerImpl.getInstance().getLevel(); 
		boolean show_proof = BackendControllerImpl.getInstance().showProof();

		TextView text1 = (TextView) view.findViewById(R.id.result_text1);
		if(level == 2){
			text1.setText(getLevel2Texts(result));
		}else if(level == 10){
			text1.setText(getLevel10Texts(result));
		}else if(result == PhishResult.Phish_Detected.getValue() && !show_proof){
			text1.setText(R.string.you_found_the_phish_noproof);
		}else{
			text1.setText(resultTextIDs[result]);
		}

		ImageView smiley = (ImageView) view.findViewById(R.id.result_smiley);
		if(level == 2 && this.result == RESULT_GUESSED){
			smiley.setImageResource(R.drawable.small_smiley_not_smile);
		}else{
			smiley.setImageResource(resultSmileyIDs[result]);
		}

		TextView text2 = (TextView) view.findViewById(R.id.result_text2);
		text2.setVisibility(View.GONE);
		if(this.result == PhishResult.NoPhish_NotDetected.getValue()){
			text2.setText(R.string.oversafe_02);
			text2.setVisibility(View.VISIBLE);
		}else if (this.result == PhishResult.Phish_NotDetected.getValue() || result == RESULT_GUESSED) {
			int remindertext = getReminderText(BackendControllerImpl.getInstance().getUrl().getAttackType(), level);
			if (remindertext > 0) {
				text2.setText(remindertext);
				text2.setVisibility(View.VISIBLE);
			}
		}

		TextView urlText = (TextView) view.findViewById(R.id.url);
		setUrlText(urlText);
		urlText.setTextSize(25);
		
		return view;
	}

	public void setResult(int result){
		this.result=result;
	}

	private int getReminderText(PhishAttackType attack_type, int level) {
		int indexReminder = attack_type.getValue() - 3;

		// level 10 reminders need to be set specifically
		// this can be reached in level 10 either by not being an attack at all
		// (attacktype = nophish) or by being an attacktype of http
		// http + legitimate url
		if (level == 10) {
			if (attack_type == PhishAttackType.NoPhish || attack_type == PhishAttackType.HTTP) {
				return R.string.level_10_reminder_http_legitimate;
			} else {
				// in level 10 different texts are shown
				String urlScheme = BackendControllerImpl.getInstance().getUrl()
						.getParts()[0];
				if (urlScheme.equals("http:")) {
					return R.string.level_10_reminder_http_phish;
				} else {
					return R.string.level_10_reminder_https_phish;
				}
			}
		} else if (indexReminder >= 0) {
			if (attack_type == PhishAttackType.Misleading || attack_type == PhishAttackType.Typo) {
				// typo and misleading are in one level (7) (noch)
				indexReminder = 4;
			}
			return reminderIDs[indexReminder];
		}

		return 0;
	}
	
	boolean enableRestartButton(){return true;};

	@Override
	int getTitle() {
		return BackendControllerImpl.getInstance().getLevelInfo().titleId;
	}

	@Override
	int getSubTitle() {
		if (this.result == PhishResult.Phish_Detected.getValue()
				|| this.result == PhishResult.NoPhish_Detected.getValue()) {
			return R.string.correct;
		} else {
			return R.string.wrong;
		}
	}

	@Override
	int getIcon() {
		return R.drawable.desktop;
	}

	@Override
	protected void setUrlText(TextView urlText) {
		String urlParts[] = BackendControllerImpl.getInstance().getUrl().getParts();
		int domainPart = BackendControllerImpl.getInstance().getUrl().getDomainPart();
		// at start clear string builder
		SpannableStringBuilder strBuilder = new SpannableStringBuilder();
		for (int i = 0; i < urlParts.length; i++) {

			String part = urlParts[i];
			// 0 at the beginning
			int wordStart = strBuilder.length();
			int wordEnd = wordStart + part.length();
			strBuilder.append(part);

			BackgroundColorSpan bgc=null;
			if(i==0 && getLevel() == 10){
				if(part.equals("https:")){
					bgc = new BackgroundColorSpan(getResources().getColor(R.color.nophish_domain));
				}else{
					bgc = new BackgroundColorSpan(getResources().getColor(R.color.phish_domain));
				}
			}else if(i==domainPart) {
				// make attacked part background blue
				bgc = new BackgroundColorSpan(getResources().getColor(R.color.domain));
			}
			if(bgc!=null){
				strBuilder.setSpan(bgc, wordStart, wordEnd, 0);
			}

		}
		if (urlText != null) {
			urlText.setText(strBuilder);
		}
	}

	private int getLevel2Texts(int result){
		if (this.result == RESULT_GUESSED) {
			return R.string.level_02_you_are_wrong;
		} else if (this.result == PhishResult.Phish_Detected.getValue()) {
			return R.string.level_02_you_are_correct;
		} else {
			return resultTextIDs[result];
		}
	}

	private int getLevel10Texts(int result){
		if (this.result == PhishResult.NoPhish_Detected.getValue()) {
			return R.string.level_10_you_are_correct;
		} else if (this.result == PhishResult.Phish_NotDetected.getValue()) {
			return R.string.level_10_you_are_wrong;
		} else if (this.result == PhishResult.Phish_Detected.getValue()){
			return R.string.level_10_you_are_correct_phish;
		} else {
			return resultTextIDs[result];
		}
	}

}
