package de.tudarmstadt.informatik.secuso.phishedu;

import com.google.example.games.basegameutils.GameHelper.GameHelperListener;

import de.tudarmstadt.informatik.secuso.phishedu.backend.BackendController;
import android.os.Bundle;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.view.MenuItem;
import android.view.View;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;

public class GooglePlusActivity extends PhishBaseActivity implements View.OnClickListener, GameHelperListener {
	
	private static GooglePlusActivity instance;
	
	public GooglePlusActivity() {
		instance=this;
	}
	
	private void showPlusButtons(){
		// show sign-out button, hide the sign-in button
		findViewById(R.id.sign_in_button).setVisibility(View.GONE);
		findViewById(R.id.sign_out_button).setVisibility(View.VISIBLE);

		// findViewById(R.id.button_show_leaderboard_rate).setVisibility(View.VISIBLE);
		// findViewById(R.id.button_show_leaderboard_total).setVisibility(View.VISIBLE);
		findViewById(R.id.button_show_leaderboard_total_points).setVisibility(
				View.VISIBLE);
		findViewById(R.id.button_show_online_achievement).setVisibility(
				View.VISIBLE);
	}

	private void hidePlusButtons(){
		// show sign-out button, hide the sign-in button
		findViewById(R.id.sign_in_button).setVisibility(View.GONE);
		findViewById(R.id.sign_out_button).setVisibility(View.VISIBLE);

		// findViewById(R.id.button_show_leaderboard_rate).setVisibility(View.VISIBLE);
		// findViewById(R.id.button_show_leaderboard_total).setVisibility(View.VISIBLE);
		findViewById(R.id.button_show_leaderboard_total_points).setVisibility(
				View.VISIBLE);
		findViewById(R.id.button_show_online_achievement).setVisibility(
				View.VISIBLE);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.google_plus);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		getSupportActionBar().setHomeButtonEnabled(true);

		findViewById(R.id.sign_in_button).setOnClickListener(this);
		findViewById(R.id.sign_out_button).setOnClickListener(this);

		if (BackendController.getInstance().getGameHelper().isSignedIn()) {
			showPlusButtons();
		} else {
			hidePlusButtons();
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onSignInFailed() {
		hidePlusButtons();
	}

	public void onSignInSucceeded() {
		showPlusButtons();
	}
	
	public static GooglePlusActivity getInstance(){
		return instance;
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

}
