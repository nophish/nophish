/*=========================================================================
 * The most reliable way to detect phishing is checking the URL
 * (web address) of a website. We developed an Android app to learn how
 * to detect Phishing URLs.
 * Copyright (C) 2015 SecUSo
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

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import de.tudarmstadt.informatik.secuso.phishedu2.backend.BackendControllerImpl;

/**
 * 
 * @author Gamze Canova this activity respresents the main menu of the app
 */
public class StartMenuActivity extends PhishBaseActivity {
	
	@Override
	public void updateUI() {
		super.updateUI();
		
		TextView startbutton = (TextView) this.getActivity().findViewById(R.id.menu_button_play);
		if (BackendControllerImpl.getInstance().getLevelCompleted(BackendControllerImpl.getInstance().getLevelCount()-1)){
			startbutton.setVisibility(View.GONE);
		}else if (BackendControllerImpl.getInstance().getMaxUnlockedLevel() > 0) {
			startbutton.setText(R.string.button_play_on);
		}
	}
	
	@Override
	public View getLayout(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.start_menu, container,false);
		
		TextView version = (TextView) v.findViewById(R.id.version);
		PackageInfo pInfo;
		try {
			pInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
			version.setText(pInfo.versionName);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		
		return v;
	}
	
	@Override
	public int[] getClickables() {
		return new int[]{
			R.id.menu_button_about,
			R.id.menu_button_level_overview,
			R.id.menu_button_more_info,
			R.id.menu_button_play,
			R.id.menu_button_social
		};
	}
	
	@Override
	public void onClick(View view) {
		int id = view.getId();
		if (id == R.id.menu_button_about) {
			switchToFragment(AboutActivity.class);
		} else if (id == R.id.menu_button_level_overview) {
			switchToFragment(LevelSelectorActivity.class);
		} else if (id == R.id.menu_button_more_info) {
			switchToFragment(MoreInfoActivity.class);
		} else if (id == R.id.menu_button_play) {
			int userlevel = BackendControllerImpl.getInstance().getMaxUnlockedLevel();
			BackendControllerImpl.getInstance().startLevel(userlevel);
		} else if (id == R.id.menu_button_social) {
			switchToFragment(GooglePlusActivity.class);
		}
	}
	
	@Override
	public void onBackPressed() {
		((MainActivity)getActivity()).displayToast("Die App kann über den Home-Button beendet werden.");
		//showExitPopup();
	}

	private void showExitPopup() {
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());

		// Setting Dialog Title
		alertDialog.setTitle(getString(R.string.end_app));

		// Setting Dialog Message
		alertDialog.setMessage(getString(R.string.end_app_text));

		alertDialog.setPositiveButton(R.string.end_app_yes,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						getActivity().finish();
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
	public boolean enableHomeButton() {
		return false;
	}
}