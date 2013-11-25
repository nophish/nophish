package de.tudarmstadt.informatik.secuso.phishedu;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import de.tudarmstadt.informatik.secuso.phishedu.backend.BackendController;

public class FindAddressBarActivity extends PhishBaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.level_01_task);
		setTitles();
	}

	private void setTitles() {
		android.support.v7.app.ActionBar ab = getSupportActionBar();

		ab.setTitle(Constants.levelTitlesIds[BackendController.getInstance()
				.getLevel()]);
		ab.setSubtitle(getString(R.string.exercise));		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.find_address_bar, menu);
		return true;
	}

	public void startBrowser(View view){
		BackendController.getInstance().redirectToLevel1URL();
	}
}
