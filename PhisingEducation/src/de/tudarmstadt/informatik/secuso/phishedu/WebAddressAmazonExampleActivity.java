package de.tudarmstadt.informatik.secuso.phishedu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

public class WebAddressAmazonExampleActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_web_address_examples);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.web_address_examples, menu);
		return true;
	}
	
	public void goToNextExample(View view){
		Intent intent = new Intent(this, SolutionAmazonExampleActivity.class);
		startActivity(intent);
	}

}
