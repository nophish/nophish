package de.tudarmstadt.informatik.secuso.phishedu;

import android.view.MenuItem;
import android.view.View;
import de.tudarmstadt.informatik.secuso.phishedu.backend.BackendControllerImpl;

public class FindAddressBarActivity extends PhishBaseActivity {

	@Override
	int getTitle() {
		return BackendControllerImpl.getInstance().getLevelInfo().titleId;
	}
	
	@Override
	int getSubTitle() {
		return R.string.exercise;
	}
	
	@Override
	public int[] getClickables() {
		return new int[]{
				R.id.level_01_exercise_button
		};
	}
	
	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.level_01_exercise_button:
			BackendControllerImpl.getInstance().redirectToLevel1URL();
			break;
		}
	}
	
	/**
	 * Going back not possible, only cancel level
	 */
	@Override
	public boolean onBackPressed() {
		levelCanceldWarning();
		return false;
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

	@Override
	public int getLayout() {
		return R.layout.level_01_task;
	}
}
