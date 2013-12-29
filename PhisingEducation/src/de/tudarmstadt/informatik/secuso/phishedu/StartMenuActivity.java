package de.tudarmstadt.informatik.secuso.phishedu;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
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
import de.tudarmstadt.informatik.secuso.phishedu.backend.BackendController.OnLevelChangeListener;
import de.tudarmstadt.informatik.secuso.phishedu.backend.BackendControllerImpl;
import de.tudarmstadt.informatik.secuso.phishedu.backend.FrontendController;

/**
 * 
 * @author Gamze Canova this activity respresents the start screen of the game a
 *         splash screen and afterwards a menu is displayed if the user wants to
 *         store his/her score online he/she has to sign into google+
 */
public class StartMenuActivity extends PhishBaseActivity implements
		FrontendController, OnLevelChangeListener, BackendInitListener {
	private static Activity context;
	
	public StartMenuActivity(){
		context=this;
	}
	
	public static void onStart(Activity context){
		StartMenuActivity.context=context;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(!BackendControllerImpl.getInstance().isInitDone()){
		  BackendControllerImpl.getInstance().init(this,this);
		}

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
	}
	
	@Override
	protected void onStart() {
		super.onStart();
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
	public void displayToast(String message) {
		Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT)
				.show();
	}

	@Override
	public void displayToast(int message) {
		Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT)
				.show();
	}

	@Override
	public void onInitDone() {
		// displayToast("we are finished with initialization!");
	}

	@Override
	public Context getContext() {
		return StartMenuActivity.context;
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
		Intent levelIntent = new Intent(this, LevelIntroActivity.class);
		levelIntent.putExtra(Constants.EXTRA_LEVEL, level);
		startActivity(levelIntent);
	}

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

	@Override
	public void displayToastScore(int score) {
		LayoutInflater inflater = getLayoutInflater();
		View layout = inflater.inflate(R.layout.fragment_toast_score,
				(ViewGroup) findViewById(R.id.toast_layout_root));

		String scoreString = Integer.toString(score);

		TextView text = (TextView) layout.findViewById(R.id.text);
		if (score < 0) {
			// red
			text.setTextColor(Color.rgb(135, 0, 0));
		} else {
			// green
			text.setTextColor(Color.rgb(0, 135, 0));
			scoreString = "+ " + score;
		}

		text.setText(scoreString + " Punkte");
		text.setTypeface(Typeface.DEFAULT_BOLD);
		Toast toast = new Toast(getApplicationContext());
		toast.setDuration(Toast.LENGTH_SHORT);
		toast.setView(layout);
		toast.show();
	}

	@Override
	public Activity getBaseActivity() {
		return this;
	}

	@Override
	public void onSignInFailed() {
		if(GooglePlusActivity.getInstance() != null){
			GooglePlusActivity.getInstance().onSignInFailed();
		}
	}

	@Override
	public void onSignInSucceeded() {
		if(GooglePlusActivity.getInstance() != null){
			GooglePlusActivity.getInstance().onSignInSucceeded();
		}
	}

}
