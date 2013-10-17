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
import de.tudarmstadt.informatik.secuso.phishedu.prototype.FindAddressBarExerciseActivity;

/**
 * 
 * @author Gamze Canova this activity respresents the start screen of the game a
 *         splash screen and afterwards a menu is displayed if the user wants to
 *         store his/her score online he/she has to sign into google+
 */
public class StartMenuActivity extends BaseGameActivity implements
		FrontendControllerInterface, View.OnClickListener{

	private boolean didAwarenessPart = false;
	
	public StartMenuActivity() {
		// request AppStateClient and GamesClient
		super(BaseGameActivity.CLIENT_APPSTATE | BaseGameActivity.CLIENT_GAMES);
	}
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		BackendController.getInstance().init(this,this.mHelper);

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
		setContentView(R.layout.google_plus);
		
		findViewById(R.id.sign_in_button).setOnClickListener(this);
		findViewById(R.id.sign_out_button).setOnClickListener(this);
		
		if (this.getGamesClient().isConnected()) {
			// show sign-in button, hide the sign-out button
			findViewById(R.id.sign_in_button).setVisibility(View.GONE);
			findViewById(R.id.sign_out_button).setVisibility(View.VISIBLE);
			
			findViewById(R.id.button_show_leaderboard_rate).setVisibility(View.VISIBLE);
			findViewById(R.id.button_show_leaderboard_total).setVisibility(View.VISIBLE);
			findViewById(R.id.button_show_online_achievement).setVisibility(View.VISIBLE);
		}else{
			// show sign-in button, hide the sign-out button
			findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
			findViewById(R.id.sign_out_button).setVisibility(View.GONE);
			
			findViewById(R.id.button_show_leaderboard_rate).setVisibility(View.GONE);
			findViewById(R.id.button_show_leaderboard_total).setVisibility(View.GONE);
			findViewById(R.id.button_show_online_achievement).setVisibility(View.GONE);
		}
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

	@Override
	public void onSignInFailed() {
		if(!onGooglePlus()){
			return; //we are not in googleplus view
		}
		
		// Sign in has failed. So show the user the sign-in button.
		findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
		findViewById(R.id.sign_out_button).setVisibility(View.GONE);
		
		findViewById(R.id.button_show_leaderboard_rate).setVisibility(View.GONE);
		findViewById(R.id.button_show_leaderboard_total).setVisibility(View.GONE);
		findViewById(R.id.button_show_online_achievement).setVisibility(View.GONE);
	}

	public void onSignInSucceeded() {
		if(!onGooglePlus()){
			return; //we are not in googleplus view
		}
		// show sign-out button, hide the sign-in button
		findViewById(R.id.sign_in_button).setVisibility(View.GONE);
		findViewById(R.id.sign_out_button).setVisibility(View.VISIBLE);
		
		findViewById(R.id.button_show_leaderboard_rate).setVisibility(View.VISIBLE);
		findViewById(R.id.button_show_leaderboard_total).setVisibility(View.VISIBLE);
		findViewById(R.id.button_show_online_achievement).setVisibility(View.VISIBLE);

		// (your code here: update UI, enable functionality that depends on sign in, etc)
	}
	private boolean onGooglePlus(){
		return findViewById(R.id.sign_in_button) != null;
	}
	
	@Override
	public void onClick(View view) {
		if (view.getId() == R.id.sign_in_button) {
			// start the asynchronous sign in flow
			BackendController.getInstance().signIn();
		}
		else if (view.getId() == R.id.sign_out_button) {
			// sign out.
			BackendController.getInstance().signOut();

			// show sign-in button, hide the sign-out button
			findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
			findViewById(R.id.sign_out_button).setVisibility(View.GONE);
			
			findViewById(R.id.button_show_leaderboard_rate).setVisibility(View.GONE);
			findViewById(R.id.button_show_leaderboard_total).setVisibility(View.GONE);
			findViewById(R.id.button_show_online_achievement).setVisibility(View.GONE);
		}
	}
	
	public void showLeaderboardRate() {
		if(this.getGamesClient().isConnected()){
			startActivityForResult(getGamesClient().getLeaderboardIntent(getResources().getString(R.string.leaderboard_detection_rate)), 1);
		}else{
			displayToast("not connected");
		}
	}

	public void showLeaderboardTotal() {
		if(this.getGamesClient().isConnected()){
			startActivityForResult(getGamesClient().getLeaderboardIntent(getResources().getString(R.string.leaderboard_detected_phishing_urls)), 1);
		}else{
			displayToast("not connected");
		}

	}

	public void showAchievments() {
		if(this.getGamesClient().isConnected()){
			startActivityForResult(getGamesClient().getAchievementsIntent(), 0);
		}else{
			displayToast("not connected");
		}
	}
	
	@Override
	public void onBackPressed(){
		if(onGooglePlus()){
			setContentView(R.layout.start_menu);
		}else {
			super.onBackPressed();
		}
	}

}
