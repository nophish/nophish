package de.tudarmstadt.informatik.secuso.phishedu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

public class IpNonsenseUrlExerciseActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ip_nonsense_url_exercise);
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.ip_nonsense_url_exercise, menu);
		return true;
	}

	public void correctlyClicked(View view){
		Intent intent = new Intent(this, IpNonsendeUrlExerciseSolutionActivity.class);
		startActivity(intent);
	}
	
	public void wronglyClicked(View view){
		Intent intent = new Intent(this, IpNonseUrlExerciseWrongSolutionActivity.class);
		startActivity(intent);
	}

}
