package de.tudarmstadt.informatik.secuso.phishedu;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.games.GamesClient;
import com.google.example.games.basegameutils.GameHelper.GameHelperListener;

import de.tudarmstadt.informatik.secuso.phishedu.backend.BackendControllerImpl;

public class GooglePlusActivity extends PhishBaseActivity implements View.OnClickListener, GameHelperListener {

	private void showPlusButtons(View v){
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

	private void hidePlusButtons(View v){
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

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = super.onCreateView(inflater, container, savedInstanceState);

		if (this.getGamesClient().isConnected()) {
			showPlusButtons(v);
		} else {
			hidePlusButtons(v);
		}
		return v;
	}

	public void onSignInFailed() {
		hidePlusButtons(getView());
	}

	public void onSignInSucceeded() {
		showPlusButtons(getView());
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.sign_in_button:
			// start the asynchronous sign in flow
			BackendControllerImpl.getInstance().signIn();
			break;
		case R.id.sign_out_button:
			// sign out.
			BackendControllerImpl.getInstance().signOut();
			hidePlusButtons(getView());
			break;
		case R.id.button_show_leaderboard_rate:
			showLeaderboardRate(view);
			break;
		case R.id.button_show_leaderboard_total:
			showLeaderboardTotal(view);
			break;
		case R.id.button_show_leaderboard_total_points:
			showLeaderboardTotalPoints(view);
			break;
		case R.id.button_show_online_achievement:
			showAchievments(view);
			break;
		}
	}


	private GamesClient getGamesClient() {
		return BackendControllerImpl.getInstance().getGameHelper().getGamesClient();
	}
	
	public void showLeaderboardRate(View view) {
		if (this.getGamesClient().isConnected()) {
			startActivityForResult(
					getGamesClient().getLeaderboardIntent(
							getResources().getString(
									R.string.leaderboard_detection_rate)), 1);
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
		}

	}

	public void showLeaderboardTotalPoints(View view) {
		if (this.getGamesClient().isConnected()) {
			startActivityForResult(
					getGamesClient().getLeaderboardIntent(
							getResources().getString(
									R.string.leaderboard_total_points)), 1);
		}

	}

	public void showAchievments(View view) {
		if (this.getGamesClient().isConnected()) {
			startActivityForResult(getGamesClient().getAchievementsIntent(), 0);
		}
	}

	public void deleteRemoteData(View view){
		if (this.getGamesClient().isConnected()) {
			BackendControllerImpl.getInstance().deleteRemoteData();
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
}
