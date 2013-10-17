package de.tudarmstadt.informatik.secuso.phishedu;

import com.google.example.games.basegameutils.BaseGameActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

public class GooglePlusSignInActivity extends Activity {

	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.google_plus_sign_in);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.google_plus_sign_in, menu);
		return true;
	}


}
