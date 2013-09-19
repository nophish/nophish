package de.tudarmstadt.informatik.secuso.phishedu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

public class CongratsWebAddressesActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_congrats_web_addresses);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.congrats_web_addresses, menu);
		return true;
	}


	public void goToIPNonsenseURLInfo(View view){
		Intent intent = new Intent(this, IpNonsenseUrlInfoActivity.class);
		startActivity(intent);
	}
}
