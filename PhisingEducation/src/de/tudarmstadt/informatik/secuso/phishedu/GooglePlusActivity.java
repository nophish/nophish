package de.tudarmstadt.informatik.secuso.phishedu;

import com.google.android.gms.games.GamesClient;
import com.google.example.games.basegameutils.BaseGameActivity;
import com.google.example.games.basegameutils.GameHelper;

import de.tudarmstadt.informatik.secuso.phishedu.backend.BackendController;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

public class GooglePlusActivity extends BaseGameActivity implements View.OnClickListener  {
	
	private static GooglePlusActivity instance = new GooglePlusActivity();
	
	public static GooglePlusActivity getInstance(){
		return instance;
	}
	
	public GameHelper getGameHelper(){
		return this.mHelper;
	}
	
	public GooglePlusActivity() {
		// request AppStateClient and GamesClient
		super(BaseGameActivity.CLIENT_APPSTATE | BaseGameActivity.CLIENT_GAMES);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.google_plus);
		
		findViewById(R.id.sign_in_button).setOnClickListener(this);
		findViewById(R.id.sign_out_button).setOnClickListener(this); 

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.google_plus_sign_in, menu);
		return true;
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
	
	public void displayToast(String message) {
		Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT)
				.show();
	}

	@Override
	public void onSignInFailed() {
		// Sign in has failed. So show the user the sign-in button.
		findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
		findViewById(R.id.sign_out_button).setVisibility(View.GONE);
		
		findViewById(R.id.button_show_leaderboard_rate).setVisibility(View.GONE);
		findViewById(R.id.button_show_leaderboard_total).setVisibility(View.GONE);
		findViewById(R.id.button_show_online_achievement).setVisibility(View.GONE);
	}

	public void onSignInSucceeded() {
		// show sign-out button, hide the sign-in button
		findViewById(R.id.sign_in_button).setVisibility(View.GONE);
		findViewById(R.id.sign_out_button).setVisibility(View.VISIBLE);
		
		findViewById(R.id.button_show_leaderboard_rate).setVisibility(View.VISIBLE);
		findViewById(R.id.button_show_leaderboard_total).setVisibility(View.VISIBLE);
		findViewById(R.id.button_show_online_achievement).setVisibility(View.VISIBLE);

		// (your code here: update UI, enable functionality that depends on sign in, etc)
	}


}
