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

public class IpNonsenseUrlExerciseHighlightHttpActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ip_nonsense_url_exercise_highlight_http);
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(
				R.menu.ip_nonsense_url_exercise_highlight_http, menu);
		return true;
	}

	public void markSubDomain(View view) {
		Intent intent = new Intent(this,
				IpNonsenseUrlExerciseHighlightSubdomainActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		startActivity(intent);
	}

	public void markDomainName(View view) {
		Intent intent = new Intent(this,
				IpNonsenseUrlExerciseHighlightDomainActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		startActivity(intent);
	}
	
	public void wronglyClicked(View view){
		Intent intent = new Intent(this, IpNonsendeUrlExWrongActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		startActivity(intent);
	}
}
