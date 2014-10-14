package de.tudarmstadt.informatik.secuso.phishedu;

import de.tudarmstadt.informatik.secuso.phishedu.backend.FrontendController;
import android.view.View;


public class AppEndActivity extends PhishBaseActivity {

	@Override
	public int getLayout() {
		return R.layout.final_app_screen;
	}
	
	@Override
	public int[] getClickables() {
		return new int[]{
				R.id.button1
		};
	}
	
	@Override
	public void onClick(View view) {
		int id = view.getId();
		if (id == R.id.button1) {
			getFrontedController().showMainMenu();
		}
	}
	
	@Override
	int getTitle() {
		return R.string.title_activity_app_end;
	}

}
