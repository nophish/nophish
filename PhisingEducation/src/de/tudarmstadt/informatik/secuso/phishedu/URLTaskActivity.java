package de.tudarmstadt.informatik.secuso.phishedu;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import de.tudarmstadt.informatik.secuso.phishedu.backend.BackendController;
import de.tudarmstadt.informatik.secuso.phishedu.backend.PhishResult;

public class URLTaskActivity extends Activity {

	private TextView urlText;
	private TextView urlsText;
	private TextView urlsGoalText;
	private TextView phishesText;
	private TextView phishesGoalText;
	private int level;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		this.level = getIntent().getIntExtra(Constants.EXTRA_LEVEL, 0);

		setContentView(R.layout.urltask_task);

		urlText = (TextView) findViewById(R.id.url_task_url);
		urlsText = (TextView) findViewById(R.id.urls);
		urlsGoalText = (TextView) findViewById(R.id.urls_goal);
		phishesText = (TextView) findViewById(R.id.phishes);
		phishesGoalText = (TextView) findViewById(R.id.phishes_goal);
		nextURL();
		setTitles();

		// In Level 2 we don't need to check if it is a phish
		if (level == 2) {
			Intent levelIntent = new Intent(this, ProofActivity.class);
			levelIntent.putExtra(Constants.EXTRA_LEVEL, this.level);
			startActivityForResult(levelIntent, 1);
		}

		// set size of shown url depending on level
		setUrlSize();

	}



	private void setUrlSize() {
		TextView url = (TextView) findViewById(R.id.url_task_url);
		float textSize = url.getTextSize();
		
		switch (level) {
		case 0:
			//should not reach this code, as urltask is called beginning from level 2
			break;
		case 1:
			//should not reach this code, as urltask is called beginning from level 2
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
			break;
		}
		url.setTextSize(textSize);
	}



	private void setTitles() {
		ActionBar ab = getActionBar();

		ab.setTitle(Constants.levelTitlesIds[BackendController.getInstance()
				.getLevel()]);
		ab.setSubtitle(getString(R.string.exercise));
		ab.setIcon(getResources().getDrawable(R.drawable.desktop));
	}

	private void nextURL() {
		String[] urlArray = BackendController.getInstance().getNextUrl();

		// build string from array
		StringBuilder sb = new StringBuilder();
		// adds 9 character string at beginning
		for (int i = 0; i < urlArray.length; i++) {
			sb.append(urlArray[i]);
		}

		urlText.setText(sb.toString());
		urlsText.setText(Integer.toString(BackendController.getInstance()
				.doneURLs()));
		urlsGoalText.setText(Integer.toString(BackendController.getInstance()
				.levelURLs()));
		phishesText.setText(Integer.toString(BackendController.getInstance()
				.foundPhishes()));
		phishesGoalText.setText(Integer.toString(BackendController
				.getInstance().levelPhishes()));
	}

	public void clickAccept(View view) {
		clicked(true);
	}

	public void clickDecline(View view) {
		clicked(false);
	}

	private void clicked(boolean acceptance) {
		PhishResult result = BackendController.getInstance().userClicked(
				acceptance);
		Class followActivity = ResultActivity.class;
		if (result == PhishResult.Phish_Detected) {
			followActivity = ProofActivity.class;
		}
		Intent levelIntent = new Intent(this, followActivity);
		levelIntent.putExtra(Constants.EXTRA_RESULT, result.getValue());
		levelIntent.putExtra(Constants.EXTRA_LEVEL, this.level);
		levelIntent.putExtra(Constants.EXTRA_SITE_TYPE, BackendController
				.getInstance().getSiteType().getValue());
		levelIntent.putExtra(Constants.EXTRA_ATTACK_TYPE, BackendController
				.getInstance().getAttackType().getValue());
		startActivityForResult(levelIntent, 1);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		nextURL();
	}

	private void levelRestartWarning() {
		levelCanceldWarning(true);
	}

	private void levelCanceldWarning() {
		levelCanceldWarning(false);
	}

	private class CanceldWarningClickListener implements
			DialogInterface.OnClickListener {
		private boolean restart;

		public CanceldWarningClickListener(boolean restart) {
			this.restart = restart;
		}

		@Override
		public void onClick(DialogInterface dialog, int which) {
			if (this.restart) {
				BackendController.getInstance().restartLevel();
			} else {
				NavUtils.navigateUpFromSameTask(URLTaskActivity.this);
			}
		}
	}

	private void levelCanceldWarning(final boolean restart) {
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

		// Setting Dialog Title
		alertDialog.setTitle(getString(R.string.level_cancel_title));

		// Setting Dialog Message
		alertDialog.setMessage(getString(R.string.level_cancel_text));

		alertDialog.setPositiveButton(R.string.level_cancel_positive_button,
				new CanceldWarningClickListener(restart));

		alertDialog.setNegativeButton(R.string.level_cancel_negative_button,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});

		// Showing Alert Message
		alertDialog.show();
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

	@Override
	public void onBackPressed() {
		levelCanceldWarning();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu items for use in the action bar
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.urltask_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}
}
