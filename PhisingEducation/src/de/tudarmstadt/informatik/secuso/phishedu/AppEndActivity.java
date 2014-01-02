package de.tudarmstadt.informatik.secuso.phishedu;

import de.tudarmstadt.informatik.secuso.phishedu.backend.MainActivity;
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
		switch (view.getId()) {
		case R.id.button1:
			((MainActivity)getActivity()).switchToFragment(StartMenuActivity.class);
			break;
		}
	}
	
	@Override
	int getTitle() {
		return R.string.title_activity_app_end;
	}

}
