package de.tudarmstadt.informatik.secuso.phishedu.prototype;

import de.tudarmstadt.informatik.secuso.phishedu.R;
import de.tudarmstadt.informatik.secuso.phishedu.R.layout;
import de.tudarmstadt.informatik.secuso.phishedu.R.menu;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

public class IpNonseUrlExerciseWrongSolutionActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ip_nonse_url_exercise_wrong_solution);
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.ip_nonse_url_exercise_wrong_solution,
				menu);
		return true;
	}

	public void goToNextEx(View view) {
		Intent intent = new Intent(this, IpNonsenseUrlExerciseTwoActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		startActivity(intent);
	}

}
