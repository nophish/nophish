package de.tudarmstadt.informatik.secuso.phishedu;

import de.tudarmstadt.informatik.secuso.phishedu.backend.BackendControllerImpl;
import android.app.Activity;
import android.view.View;

public class GooglePlusActivity extends PhishBaseActivity {
	private boolean showSignIn=true;
	
	public void setShowSignIn(boolean showsignin){
		this.showSignIn=showsignin;
		updateUI();
	}

	private void showPlusButtons(Activity v){
		// show sign-out button, hide the sign-in button
		v.findViewById(R.id.sign_in_button).setVisibility(View.GONE);
		v.findViewById(R.id.sign_out_button).setVisibility(View.VISIBLE);

		// findViewById(R.id.button_show_leaderboard_rate).setVisibility(View.VISIBLE);
		// findViewById(R.id.button_show_leaderboard_total).setVisibility(View.VISIBLE);
		v.findViewById(R.id.button_show_leaderboard_total_points).setVisibility(
				View.VISIBLE);
		v.findViewById(R.id.button_show_online_achievement).setVisibility(
				View.VISIBLE);
		v.findViewById(R.id.button_delete_remote_data).setVisibility(
				View.VISIBLE);
	}

	private void hidePlusButtons(Activity v){
		// show sign-out button, hide the sign-in button
		v.findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
		v.findViewById(R.id.sign_out_button).setVisibility(View.GONE);

		// findViewById(R.id.button_show_leaderboard_rate).setVisibility(View.VISIBLE);
		// findViewById(R.id.button_show_leaderboard_total).setVisibility(View.VISIBLE);
		v.findViewById(R.id.button_show_leaderboard_total_points).setVisibility(
				View.GONE);
		v.findViewById(R.id.button_show_online_achievement).setVisibility(
				View.GONE);
		v.findViewById(R.id.button_delete_remote_data).setVisibility(
				View.GONE);
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
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.sign_in_button:
			BackendControllerImpl.getInstance().signIn();
			break;
		case R.id.sign_out_button:
			BackendControllerImpl.getInstance().signOut();
			setShowSignIn(true);
			break;
		case R.id.button_show_leaderboard_total:
			onShowLeaderboardsRequested(R.string.leaderboard_detected_phishing_urls);
			break;
		case R.id.button_show_leaderboard_total_points:
			onShowLeaderboardsRequested(R.string.leaderboard_total_points);
			break;
		case R.id.button_show_online_achievement:
			if (BackendControllerImpl.getInstance().getGameHelper().isSignedIn()) {
	            startActivityForResult(BackendControllerImpl.getInstance().getGameHelper().getGamesClient().getAchievementsIntent(), 0);
	        }
			break;
		case R.id.button_delete_remote_data:
			BackendControllerImpl.getInstance().deleteRemoteData();
			break;
		}
	}
	
	private void onShowLeaderboardsRequested(int leaderboard) {
		if (BackendControllerImpl.getInstance().getGameHelper().isSignedIn()) {
            startActivityForResult(BackendControllerImpl.getInstance().getGameHelper().getGamesClient().getLeaderboardIntent(getString(leaderboard)), 0);
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
