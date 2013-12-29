package de.tudarmstadt.informatik.secuso.phishedu;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import de.tudarmstadt.informatik.secuso.phishedu.backend.BackendControllerImpl;

public class FindAddressBarActivity extends PhishBaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.level_01_task);
		setTitles();
	}

	private void setTitles() {
		android.support.v7.app.ActionBar ab = getSupportActionBar();

		ab.setTitle(BackendControllerImpl.getInstance().getLevelInfo().titleId);
		ab.setSubtitle(getString(R.string.exercise));		
	}


	public void startBrowser(View view){
		BackendControllerImpl.getInstance().redirectToLevel1URL();
	}
	
	/**
	 * Going back not possible, only cancel level
	 */
	@Override
	public void onBackPressed() {
		levelCanceldWarning();
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		// Respond to the action bar's Up/Home button
		case android.R.id.home:
			levelCanceldWarning();
			return true;
		case R.id.restart_level:
			levelRestartWarning();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
