/*=========================================================================
 * The most reliable way to detect phishing is checking the URL
 * (web address) of a website. We developed an Android app to learn how
 * to detect Phishing URLs.
 * Copyright (C) 2015 SECUSO
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *=========================================================================*/

package de.tudarmstadt.informatik.secuso.phishedu2;

import android.app.Activity;
import android.view.View;

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
        /*
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
		*/
	}
	
	private void onShowLeaderboardsRequested(int leaderboard) {
        /*
		if (BackendControllerImpl.getInstance().getGameHelper().isSignedIn()) {
            startActivityForResult(Games.Leaderboards.getLeaderboardIntent(BackendControllerImpl.getInstance().getGameHelper().getApiClient(), getString(leaderboard)), 0);
        }
        */
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
