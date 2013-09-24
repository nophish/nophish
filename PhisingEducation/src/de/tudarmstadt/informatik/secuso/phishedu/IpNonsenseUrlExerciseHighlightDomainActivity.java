package de.tudarmstadt.informatik.secuso.phishedu;

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

public class IpNonsenseUrlExerciseHighlightDomainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ip_nonsense_url_exercise_highlight_domain);
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(
				R.menu.ip_nonsense_url_exercise_highlight_domain, menu);
		return true;
	}

	public void markHttp(View view){
		Intent intent = new Intent(this, IpNonsenseUrlExerciseHighlightHttpActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		startActivity(intent);
	}
	
	public void markSubDomain(View view){
		Intent intent = new Intent(this, IpNonsenseUrlExerciseHighlightSubdomainActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		startActivity(intent);
		
	}
	
	public void correctlyClicked(View view){
		Intent intent = new Intent(this, IpNonsenseUrlExCorrectActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		startActivity(intent);
	}
	

}
