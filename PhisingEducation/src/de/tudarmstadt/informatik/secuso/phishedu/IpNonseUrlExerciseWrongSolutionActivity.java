package de.tudarmstadt.informatik.secuso.phishedu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

public class IpNonseUrlExerciseWrongSolutionActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ip_nonse_url_exercise_wrong_solution);
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.ip_nonse_url_exercise_wrong_solution,
				menu);
		return true;
	}


	public void correctlyClicked(View view){
		Intent intent = new Intent(this, IpNonsenseUrlExCorrectActivity.class);
		startActivity(intent);
	}
	
	public void wronglyClicked(View view){
		Intent intent = new Intent(this, IpNonsendeUrlExWrongActivity.class);
		startActivity(intent);
		
	}
}
