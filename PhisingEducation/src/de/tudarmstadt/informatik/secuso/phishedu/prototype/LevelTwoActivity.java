package de.tudarmstadt.informatik.secuso.phishedu.prototype;

import de.tudarmstadt.informatik.secuso.phishedu.R;

import de.tudarmstadt.informatik.secuso.phishedu.R.layout;
import de.tudarmstadt.informatik.secuso.phishedu.R.menu;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.support.v4.app.NavUtils;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;

public class LevelTwoActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.level_2_splash);
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		// display the logo during 5 secondes,
		new CountDownTimer(5000, 1000) {
			@Override
			public void onTick(long millisUntilFinished) {
			}

			@Override
			public void onFinish() {
				// set the new Content of your activity
				LevelTwoActivity.this
						.setContentView(R.layout.activity_web_address_info);
			}
		}.start();
	}

	
	public void goToNextWebAddressInfoActivity(View view){
//		Intent intent = new Intent(this, WebAddressSecondInfoActivity.class);
//		intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//		startActivity(intent);
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.level_two, menu);
		return true;
	}


}
