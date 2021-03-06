package de.tudarmstadt.informatik.secuso.phishedu;

import android.app.Activity;
import android.view.View;

import com.google.android.gms.games.Games;

import de.tudarmstadt.informatik.secuso.phishedu.backend.BackendControllerImpl;

public class GooglePlusActivity extends PhishBaseActivity {
	private boolean showSignIn=true;
	
	public void setShowSignIn(boolean showsignin){
		this.showSignIn=showsignin;
		updateUI();
	}

	void updateUI(Activity v){
		// show sign-out button, hide the sign-in button
		v.findViewById(R.id.sign_in_button).setVisibility(showSignIn ? View.VISIBLE : View.GONE);
		v.findViewById(R.id.sign_out_button).setVisibility(showSignIn ? View.GONE : View.VISIBLE);

		// findViewById(R.id.button_show_leaderboard_rate).setVisibility(View.VISIBLE);
		// findViewById(R.id.button_show_leaderboard_total).setVisibility(View.VISIBLE);
		v.findViewById(R.id.button_show_leaderboard_total_points).setVisibility(
				showSignIn ? View.GONE : View.VISIBLE);
		v.findViewById(R.id.button_show_online_achievement).setVisibility(
				showSignIn ? View.GONE : View.VISIBLE);
		v.findViewById(R.id.button_delete_remote_data).setVisibility(
				showSignIn ? View.GONE : View.VISIBLE);
        v.findViewById(R.id.google_plus_text).setVisibility(
                showSignIn ? View.VISIBLE : View.GONE);
        v.findViewById(R.id.google_plus_disclaimer).setVisibility(
                showSignIn ? View.VISIBLE : View.GONE);
	}

	@Override
	public void onClick(View view) {
		int id = view.getId();
		if (id == R.id.sign_in_button) {
			BackendControllerImpl.getInstance().signIn();
		} else if (id == R.id.sign_out_button) {
			BackendControllerImpl.getInstance().signOut();
			setShowSignIn(true);
		} else if (id == R.id.button_show_leaderboard_total) {
			onShowLeaderboardsRequested(R.string.leaderboard_detected_phishing_urls);
		} else if (id == R.id.button_show_leaderboard_total_points) {
			onShowLeaderboardsRequested(R.string.leaderboard_total_points);
		} else if (id == R.id.button_show_online_achievement) {
			if (BackendControllerImpl.getInstance().getGameHelper().isSignedIn()) {
	            startActivityForResult(Games.Achievements.getAchievementsIntent(BackendControllerImpl.getInstance().getGameHelper().getApiClient()),0);
	        }
		} else if (id == R.id.button_delete_remote_data) {
			BackendControllerImpl.getInstance().deleteRemoteData();
		}
	}
	
	private void onShowLeaderboardsRequested(int leaderboard) {
		if (BackendControllerImpl.getInstance().getGameHelper().isSignedIn()) {
            startActivityForResult(Games.Leaderboards.getLeaderboardIntent(BackendControllerImpl.getInstance().getGameHelper().getApiClient(), getString(leaderboard)), 0);
        }
	}

	@Override
	public int getLayout() {
		return R.layout.google_plus;
	}

	@Override
	public int[] getClickables() {
		return new int[] {
				R.id.sign_in_button,
				R.id.sign_out_button,
				R.id.button_show_online_achievement,
				R.id.button_show_leaderboard_rate,
				R.id.button_show_leaderboard_total,
				R.id.button_show_leaderboard_total_points,
				R.id.button_delete_remote_data
		};
	}
	
	@Override
	int getTitle() {
		return R.string.button_social;
	}
}
