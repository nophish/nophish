package de.tudarmstadt.informatik.secuso.phishedu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

public class WebAddressExOneWrongClickPathActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_web_address_ex_one_wrong_click_path);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.web_address_ex_one_wrong_click_path,
				menu);
		return true;
	}

	public void goToViewSolution(View view){
		Intent intent = new Intent(this, WebAddressExOneSolutionActivity.class);
		startActivity(intent);
	}


}
