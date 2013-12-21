package de.tudarmstadt.informatik.secuso.phishedu;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.games.GamesClient;
import de.tudarmstadt.informatik.secuso.phishedu.backend.BackendController;
import de.tudarmstadt.informatik.secuso.phishedu.backend.FrontendControllerInterface;

/**
 * 
 * @author Gamze Canova this activity respresents the start screen of the game a
 *         splash screen and afterwards a menu is displayed if the user wants to
 *         store his/her score online he/she has to sign into google+
 */
public class StartMenuActivity extends PhishBaseActivity implements
		FrontendControllerInterface, View.OnClickListener {

	public StartMenuActivity() {
		// request AppStateClient and GamesClient
		super();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		BackendController.getInstance().init(this);

		setContentView(R.layout.start_menu);
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		checkPlayOn();

		BackendController.getInstance().onUrlReceive(getIntent().getData());
	}
	
	private void checkPlayOn(){
		if (BackendController.getInstance().getMaxUnlockedLevel() > 0) {
			TextView startbutton = (TextView) findViewById(R.id.menu_button_play);
			startbutton.setText(R.string.button_play_on);
		}
	}

	public void showLevelOverview(View view) {
		Intent levelGridIntent = new Intent(this, LevelSelectorActivity.class);
		startActivity(levelGridIntent);
	}

	public void goToGooglePlay(View view) {
		setContentView(R.layout.google_plus);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);

		findViewById(R.id.sign_in_button).setOnClickListener(this);
		findViewById(R.id.sign_out_button).setOnClickListener(this);

		if (BackendController.getInstance().getGameHelper().isSignedIn()) {
			// show sign-in button, hide the sign-out button
			findViewById(R.id.sign_in_button).setVisibility(View.GONE);
			findViewById(R.id.sign_out_button).setVisibility(View.VISIBLE);

			// findViewById(R.id.button_show_leaderboard_rate).setVisibility(View.VISIBLE);
			// findViewById(R.id.button_show_leaderboard_total).setVisibility(View.VISIBLE);
			findViewById(R.id.button_show_leaderboard_total_points)
					.setVisibility(View.VISIBLE);
			findViewById(R.id.button_show_online_achievement).setVisibility(
					View.VISIBLE);
		} else {
			// show sign-in button, hide the sign-out button
			findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
			findViewById(R.id.sign_out_button).setVisibility(View.GONE);

			// findViewById(R.id.button_show_leaderboard_rate).setVisibility(View.GONE);
			// findViewById(R.id.button_show_leaderboard_total).setVisibility(View.GONE);
			findViewById(R.id.button_show_leaderboard_total_points)
					.setVisibility(View.GONE);
			findViewById(R.id.button_show_online_achievement).setVisibility(
					View.GONE);
		}
	}

	public void goToStartMenu() {
		setContentView(R.layout.start_menu);
		getSupportActionBar().setHomeButtonEnabled(false);
		getSupportActionBar().setDisplayHomeAsUpEnabled(false);
		checkPlayOn();
	}

	public void showMoreInfo(View view) {
		// start Activity showing the list view
		Intent moreInfoIntent = new Intent(this, MoreInfoActivity.class);
		startActivity(moreInfoIntent);

	}

	public void showAbout(View view) {
		// start Activity showing the list view
		Intent aboutIntent = new Intent(this, AboutActivity.class);
		startActivity(aboutIntent);

	}

	/**
	 * initially game is started from awareness-part when game has been started
	 * once - Button text should change to Continue game state should be loaded
	 */
	public void startGame(View view) {
		BackendController.getInstance().startLevel(
				BackendController.getInstance().getMaxUnlockedLevel());
	}

	/*
	 * Use these as examples for later implementation
	 */

	@Override
	public void displayToast(String message) {
		Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT)
				.show();
	}

	@Override
	public void displayToast(int message) {
		Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT)
				.show();
	}

	@Override
	public void initDone() {
		// displayToast("we are finished with initialization!");
	}

	@Override
	public Context getContext() {
		return getApplicationContext();
	}

	@Override
	public void initProgress(int percent) {
		if (percent % 10 == 0) {
			displayToast("init Progress:" + percent);
		}
	}

	@Override
	public void startBrowser(Uri url) {
		Intent browserIntent = new Intent(Intent.ACTION_VIEW, url);
		this.startActivity(browserIntent);
	}

	@Override
	public void onLevelChange(int level) {
		Intent levelIntent = new Intent(this, LevelIntroActivity.class);
		levelIntent.putExtra(Constants.EXTRA_LEVEL, level);
		ResultActivity.resetState();
		startActivity(levelIntent);
	}

	@Override
	public void onSignInFailed() {
		if (!onGooglePlus()) {
			return; // we are not in googleplus view
		}

		// Sign in has failed. So show the user the sign-in button.
		findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
		findViewById(R.id.sign_out_button).setVisibility(View.GONE);

		// findViewById(R.id.button_show_leaderboard_rate).setVisibility(View.GONE);
		// findViewById(R.id.button_show_leaderboard_total).setVisibility(View.GONE);
		findViewById(R.id.button_show_leaderboard_total_points).setVisibility(
				View.GONE);
		findViewById(R.id.button_show_online_achievement).setVisibility(
				View.GONE);
	}

	public void onSignInSucceeded() {
		if (!onGooglePlus()) {
			return; // we are not in googleplus view
		}
		// show sign-out button, hide the sign-in button
		findViewById(R.id.sign_in_button).setVisibility(View.GONE);
		findViewById(R.id.sign_out_button).setVisibility(View.VISIBLE);

		// findViewById(R.id.button_show_leaderboard_rate).setVisibility(View.VISIBLE);
		// findViewById(R.id.button_show_leaderboard_total).setVisibility(View.VISIBLE);
		findViewById(R.id.button_show_leaderboard_total_points).setVisibility(
				View.VISIBLE);
		findViewById(R.id.button_show_online_achievement).setVisibility(
				View.VISIBLE);

		// (your code here: update UI, enable functionality that depends on sign
		// in, etc)
	}

	private boolean onGooglePlus() {
		return findViewById(R.id.sign_in_button) != null;
	}

	@Override
	public void onClick(View view) {
		if (view.getId() == R.id.sign_in_button) {
			// start the asynchronous sign in flow
			BackendController.getInstance().signIn();
		} else if (view.getId() == R.id.sign_out_button) {
			// sign out.
			BackendController.getInstance().signOut();

			// show sign-in button, hide the sign-out button
			findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
			findViewById(R.id.sign_out_button).setVisibility(View.GONE);

			// findViewById(R.id.button_show_leaderboard_rate).setVisibility(View.GONE);
			// findViewById(R.id.button_show_leaderboard_total).setVisibility(View.GONE);
			findViewById(R.id.button_show_leaderboard_total_points)
					.setVisibility(View.GONE);
			findViewById(R.id.button_show_online_achievement).setVisibility(
					View.GONE);
		}
	}

	private GamesClient getGamesClient() {
		return BackendController.getInstance().getGameHelper().getGamesClient();
	}

	public void showLeaderboardRate(View view) {
		if (this.getGamesClient().isConnected()) {
			startActivityForResult(
					getGamesClient().getLeaderboardIntent(
							getResources().getString(
									R.string.leaderboard_detection_rate)), 1);
		} else {
			displayToast(R.string.not_connected);
		}
	}

	public void showLeaderboardTotal(View view) {
		if (this.getGamesClient().isConnected()) {
			startActivityForResult(
					getGamesClient()
							.getLeaderboardIntent(
									getResources()
											.getString(
													R.string.leaderboard_detected_phishing_urls)),
					1);
		} else {
			displayToast(R.string.not_connected);
		}

	}

	public void showLeaderboardTotalPoints(View view) {
		if (this.getGamesClient().isConnected()) {
			startActivityForResult(
					getGamesClient().getLeaderboardIntent(
							getResources().getString(
									R.string.leaderboard_total_points)), 1);
		} else {
			displayToast(R.string.not_connected);
		}

	}

	public void showAchievments(View view) {
		if (this.getGamesClient().isConnected()) {
			startActivityForResult(getGamesClient().getAchievementsIntent(), 0);
		} else {
			displayToast(R.string.not_connected);
		}
	}

	@Override
	public void onBackPressed() {
		if (onGooglePlus()) {
			goToStartMenu();
		} else {
			// show alert dialog first, before exiting the app...
			showExitPopup();
			// finish();
		}
	}

	private void showExitPopup() {
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

		// Setting Dialog Title
		alertDialog.setTitle(getString(R.string.end_app));

		// Setting Dialog Message
		alertDialog.setMessage(getString(R.string.end_app_text));

		alertDialog.setPositiveButton(R.string.end_app_yes,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						StartMenuActivity.this.finish();
					}
				});

		alertDialog.setNegativeButton(R.string.end_app_no,
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
	public void levelFinished(int level) {
		Intent levelIntent = new Intent(this, LevelFinishedActivity.class);
		levelIntent.putExtra(Constants.EXTRA_LEVEL, level);
		startActivity(levelIntent);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		// Respond to the action bar's Up/Home button
		case android.R.id.home:
			goToStartMenu();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void levelFailed(int level) {
	}

	@Override
	public void displayToastScore(int score) {
		LayoutInflater inflater = getLayoutInflater();
		View layout = inflater.inflate(R.layout.fragment_toast_score,
				(ViewGroup) findViewById(R.id.toast_layout_root));

		String scoreString = Integer.toString(score);

		TextView text = (TextView) layout.findViewById(R.id.text);
		if (score < 0) {
			// red
			text.setTextColor(Color.rgb(135, 0, 0));
		} else {
			// green
			text.setTextColor(Color.rgb(0, 135, 0));
			scoreString = "+ " + score;
		}

		text.setText(scoreString + " Punkte");
		text.setTypeface(Typeface.DEFAULT_BOLD);
		Toast toast = new Toast(getApplicationContext());
		toast.setDuration(Toast.LENGTH_SHORT);
		toast.setView(layout);
		toast.show();
	}

	@Override
	public Activity getBaseActivity() {
		return this;
	}

}
