package de.tudarmstadt.informatik.secuso.phishedu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

public class IpNonsendeUrlExerciseSolutionActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ip_nonsende_url_exercise_solution);
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.ip_nonsende_url_exercise_solution,
				menu);
		return true;
	}

	public void correctlyClicked(View view){
		Intent intent = new Intent(this, IpNonsenseUrlExerciseSecondSolution.class);
		startActivity(intent);
	}
	
	public void wronglyClicked(View view){
		Intent intent = new Intent(this, IpNonsendeUrlExWrongActivity.class);
		startActivity(intent);
	}
}
