package de.tudarmstadt.informatik.secuso.phishedu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

public class FindAddressBarSecondInfoActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_find_address_bar_second_info);
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.find_address_bar_second_info, menu);
		return true;
	}
	
	public void goToFindAddrBarEx(View view){
		Intent intent = new Intent(this, FindAddressBarExerciseActivity.class);
		startActivity(intent);
	}

}
