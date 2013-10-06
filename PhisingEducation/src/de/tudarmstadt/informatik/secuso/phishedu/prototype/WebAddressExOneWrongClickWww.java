package de.tudarmstadt.informatik.secuso.phishedu.prototype;

import de.tudarmstadt.informatik.secuso.phishedu.R;
import de.tudarmstadt.informatik.secuso.phishedu.R.layout;
import de.tudarmstadt.informatik.secuso.phishedu.R.menu;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.support.v4.app.NavUtils;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;

public class WebAddressExOneWrongClickWww extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_web_address_ex_one_wrong_click_www);
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.web_address_ex_one_wrong_click_www,
				menu);
		return true;
	}

	public void goToViewSolution(View view) {
		Intent intent = new Intent(this, WebAddressExOneSolutionActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		startActivity(intent);
	}

}
