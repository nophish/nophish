package de.tudarmstadt.informatik.secuso.phishedu;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.support.v4.app.NavUtils;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;

public class WebAddressExOneHighlightPath extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_web_address_ex_one_highlight_path);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.web_address_ex_one_highlight_path,
				menu);
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
	
	public void goToWrongClickPath(View view){
		Intent intent = new Intent(this, WebAddressExOneWrongClickPathActivity.class);
		startActivity(intent);
	}
	
}
