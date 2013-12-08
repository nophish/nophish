package de.tudarmstadt.informatik.secuso.phishedu;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
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


	public void startBrowser(View view){
		BackendController.getInstance().redirectToLevel1URL();
	}
}
