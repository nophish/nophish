package de.tudarmstadt.informatik.secuso.phishedu;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import de.tudarmstadt.informatik.secuso.phishedu.backend.BackendController.BackendInitListener;
import de.tudarmstadt.informatik.secuso.phishedu.backend.BackendController.Levelstate;
import de.tudarmstadt.informatik.secuso.phishedu.backend.BackendController.OnLevelChangeListener;
import de.tudarmstadt.informatik.secuso.phishedu.backend.BackendController.OnLevelstateChangeListener;
import de.tudarmstadt.informatik.secuso.phishedu.backend.BackendControllerImpl;
import de.tudarmstadt.informatik.secuso.phishedu.backend.FrontendController;

/**
 * 
 * @author Gamze Canova this activity respresents the start screen of the game a
 *         splash screen and afterwards a menu is displayed if the user wants to
 *         store his/her score online he/she has to sign into google+
 */
public class StartMenuActivity extends PhishBaseActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.start_menu);
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		if (BackendControllerImpl.getInstance().getMaxUnlockedLevel() > 0) {
			TextView startbutton = (TextView) findViewById(R.id.menu_button_play);
			startbutton.setText(R.string.button_play_on);
		}
		
		TextView version = (TextView) findViewById(R.id.version);
		PackageInfo pInfo;
		try {
			pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
			version.setText(pInfo.versionName);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		
		BackendControllerImpl.getInstance().onUrlReceive(getIntent().getData());
	}
	
	public void showLevelOverview(View view) {
		Intent levelGridIntent = new Intent(this, LevelSelectorActivity.class);
		startActivity(levelGridIntent);
	}

	public void goToGooglePlay(View view) {
		// start Activity showing the list view
		Intent playIntent = new Intent(this, GooglePlusActivity.class);
		startActivity(playIntent);
	}

	public void showMoreInfo(View view) {
		// start Activity showing the list view
		Intent moreInfoIntent = new Intent(this, MoreInfoActivity.class);
		startActivity(moreInfoIntent);

	}

	public void showAbout(View view) {
		// start Activity showing the list view
		Intent aboutIntent = new Intent(this, AboutActivity.class);
		startActivity(aboutIntent);
	}

	/**
	 * initially game is started from awareness-part when game has been started
	 * once - Button text should change to Continue game state should be loaded
	 */
	public void startGame(View view) {
		BackendControllerImpl.getInstance().startLevel(
				BackendControllerImpl.getInstance().getMaxUnlockedLevel());
	}

	/*
	 * Use these as examples for later implementation
	 */

	

	@Override
	public void onBackPressed() {
		showExitPopup();
	}

	private void showExitPopup() {
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

		// Setting Dialog Title
		alertDialog.setTitle(getString(R.string.end_app));

		// Setting Dialog Message
		alertDialog.setMessage(getString(R.string.end_app_text));

		alertDialog.setPositiveButton(R.string.end_app_yes,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						StartMenuActivity.this.finish();
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

}
