package de.tudarmstadt.informatik.secuso.phishedu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

public class WebAddressExerciseOneActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_web_address_exercise_one);
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.web_address_exercise_one, menu);
		return true;
	}

	public void goToHighlightWww(View view){
		Intent intent = new Intent(this, WebAddressExOneHighlightWww.class);
		startActivity(intent);
	}

	public void goToHighlightPaypalCom(View view){
		Intent intent = new Intent(this, WebAddressExOneHighlightPaypalCom.class);
		startActivity(intent);
	}
	
	public void goToHighlightPath(View view){
		Intent intent = new Intent(this, WebAddressExOneHighlightPath.class);
		startActivity(intent);
	}
}
