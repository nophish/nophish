package de.tudarmstadt.informatik.secuso.phishedu;

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

public class LevelThreeActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_level_three);
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		// display the logo during 5 secondes,
		new CountDownTimer(5000, 1000) {
			@Override
			public void onTick(long millisUntilFinished) {
			}

			@Override
			public void onFinish() {
				// set the new Content of your activity
				LevelThreeActivity.this
						.setContentView(R.layout.activity_ip_nonsense_url_info);
			}
		}.start();
	}

	
	public void goToIPNonsenseURLSecondInfo(View view){
		Intent intent = new Intent(this, IpNonsenseUrlSecondInfoActivity.class);
		startActivity(intent);
	}
	


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.level_three, menu);
		return true;
	}


}
