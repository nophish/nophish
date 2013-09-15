package de.tudarmstadt.informatik.secuso.phishedu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

public class SolutionGoogleExampleActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_solution_google_example);
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.solution_google_example, menu);
		return true;
	}
	
	public void goToNextExample(View view){
		Intent intent = new Intent(this, WebAddressFaceboookExampleActivity.class);
		startActivity(intent);
	}

}
