package de.tudarmstadt.informatik.secuso.phishedu;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.view.MenuItem;
import android.view.View;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;

public class AboutActivity extends PhishBaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void openSecusoLink(View view){
		Intent browserIntent = new Intent(Intent.ACTION_VIEW);
		browserIntent.setData(Uri.parse("https://www.secuso.informatik.tu-darmstadt.de"));
		startActivity(browserIntent);
	}

}
