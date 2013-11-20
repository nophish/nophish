package de.tudarmstadt.informatik.secuso.phishedu;

import de.tudarmstadt.informatik.secuso.phishedu.backend.BackendController;
import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.view.Menu;
import android.view.View;

public class FindAddressBarActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.level_01_task);
		setTitles();
	}

	private void setTitles() {
		ActionBar ab = getActionBar();

		ab.setTitle(Constants.levelTitlesIds[BackendController.getInstance()
				.getLevel()]);
		ab.setSubtitle(getString(R.string.exercise));
		ab.setIcon(getResources().getDrawable(R.drawable.desktop));		
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
