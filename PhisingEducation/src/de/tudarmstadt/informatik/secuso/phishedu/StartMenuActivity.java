package de.tudarmstadt.informatik.secuso.phishedu;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.appstate.AppStateClient;
import com.google.android.gms.games.GamesClient;
import com.google.example.games.basegameutils.BaseGameActivity;
import com.google.example.games.basegameutils.GameHelper;

import de.tudarmstadt.informatik.secuso.phishedu.backend.BackendController;
import de.tudarmstadt.informatik.secuso.phishedu.backend.FrontendControllerInterface;

/**
 * 
 * @author Gamze Canova this activity respresents the start screen of the game a
 *         splash screen and afterwards a menu is displayed if the user wants to
 *         store his/her score online he/she has to sign into google+
 */
public class StartMenuActivity extends Activity implements
		FrontendControllerInterface{

	private boolean didAwarenessPart = false;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
        BackendController.getInstance().init(this,GooglePlusActivity.getInstance().getGameHelper());

		setContentView(R.layout.start_menu);
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		// display the logo during 5 seconds
		// setContentView to activity_start_menu when finished

		BackendController.getInstance().onUrlReceive(getIntent().getData());
	}

	public void showLevelOverview(View view) {
		Intent levelGridIntent = new Intent(this, LevelGridActivity.class);
		startActivity(levelGridIntent);
	}
	
	public void goToGooglePlay(View view) {
		Intent googlePlayIntent = new Intent(this, GooglePlusActivity.class);
		startActivity(googlePlayIntent);
	}

	public void showMoreInfo(View view) {

		// start Activity showing the list view
		Intent moreInfoIntent = new Intent(this, MoreInfoActivity.class);
		startActivity(moreInfoIntent);

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
		} else {
			// go to last screen
			// oder always go to last seen screen
		}
	}

	/*
	 * Use these as examples for later implementation
	 */
	protected void mailSendTest() {
		BackendController.getInstance().sendMail("cbergmann@schuhklassert.de",
				"cbergmann@schuhklassert.de", "This is a user message");
	}

	@Override
	public void displayToast(String message) {
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
	public void onLevelChange(int level) {
		Intent gameIntent = new Intent(this, AwarenessActivity.class);
		startActivity(gameIntent);
	}
}
