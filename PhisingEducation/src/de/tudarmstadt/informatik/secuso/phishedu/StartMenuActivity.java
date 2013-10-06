package de.tudarmstadt.informatik.secuso.phishedu;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.appstate.AppStateClient;
import com.google.android.gms.games.GamesClient;

import de.tudarmstadt.informatik.secuso.phishedu.backend.BackendController;
import de.tudarmstadt.informatik.secuso.phishedu.backend.FrontendControllerInterface;

/**
 * 
 * @author Gamze Canova this activity respresents the start screen of the game a
 *         splash screen and afterwards a menu is displayed if the user wants to
 *         store his/her score online he/she has to sign into google+
 */
public class StartMenuActivity extends Activity implements
		FrontendControllerInterface {

	private static final int COUNT_DOWN_INTERVAL = 1000;
	private static final int MILLIS_IN_FUTURE = 3000;
	private boolean didAwarenessPart = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// BackendController.getInstance().init(this);

		setContentView(R.layout.splash_screen);
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		Log.d("test", "jgfcjf2");

		// display the logo during 5 seconds
		// setContentView to activity_start_menu when finished
		showSplash();

		// findViewById(R.id.sign_in_button).setOnClickListener(this);
		// findViewById(R.id.sign_out_button).setOnClickListener(this);
	}

	private void showSplash() {
		new CountDownTimer(MILLIS_IN_FUTURE, COUNT_DOWN_INTERVAL) {

			@Override
			public void onTick(long millisUntilFinished) {
			}

			@Override
			public void onFinish() {
				// set the new Content of your activity
				StartMenuActivity.this
						.setContentView(R.layout.start_menu);

			}

		}.start();
	}
	
	
	
	
	
	

	/**
	 * initially game is started from awareness-part when game has been started
	 * once - Button text should change to Continue game state should be loaded
	 */
	public void startGame(View view) {

		if (!didAwarenessPart) {
			Intent intent = new Intent(this, AwarenessActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
			startActivity(intent);
		}
	}

	/*
	 * 
	 */
	protected void mailSendTest() {
		BackendController.getInstance().sendMail("cbergmann@schuhklassert.de",
				"cbergmann@schuhklassert.de", "This is a user message");
	}

	public void leve1Test() {
		BackendController.getInstance().StartLevel1();
	}

	private void displayToast(String message) {
		Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT)
				.show();
	}

	@Override
	public void MailReturned() {
		displayToast("The Mail returned!");
	}

	@Override
	public void level1Finished() {
		displayToast("Level 1 is completed!");
	}

	@Override
	public void initDone() {
		displayToast("we are finished with initialization!");
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
	public GamesClient getGamesClient() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AppStateClient getAppStateClient() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onLevelChange(int level) {
		// TODO Auto-generated method stub

	}
}
