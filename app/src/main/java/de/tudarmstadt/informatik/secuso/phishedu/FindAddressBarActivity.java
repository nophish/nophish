package de.tudarmstadt.informatik.secuso.phishedu;

import android.view.View;
import de.tudarmstadt.informatik.secuso.phishedu.backend.BackendControllerImpl;

public class FindAddressBarActivity extends PhishBaseActivity {

	@Override
	int getIcon() {
		return R.drawable.desktop;
	}
	
	@Override
	int getTitle() {
		return BackendControllerImpl.getInstance().getLevelInfo().titleId;
	}
	
	@Override
	public int[] getClickables() {
		return new int[]{
				R.id.level_01_exercise_button
		};
	}
	
	@Override
	public void onClick(View view) {
		int id = view.getId();
		if (id == R.id.level_01_exercise_button) {
			BackendControllerImpl.getInstance().redirectToLevel1URL();
		}
	}
	
	/**
	 * Going back not possible, only cancel level
	 */
	@Override
	public void onBackPressed() {
		levelCanceldWarning();
	}
	
	@Override
	public int getLayout() {

        BackendControllerImpl.getInstance().startLevel(2);

		return R.layout.level_01_task;
	}
	
	@Override
	int getSubTitle() {
		return R.string.exercise;
	}
}
