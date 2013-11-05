package de.tudarmstadt.informatik.secuso.phishedu.levelintros;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import de.tudarmstadt.informatik.secuso.phishedu.SwipeActivity;

public class Level2WebAddressesActivity extends SwipeActivity {
	public static final int LEVEL_ID=2;
	
	@Override
	protected int getPageCount() {
		return GeneralLevelIntros.levelLayoutIds[LEVEL_ID].length;
	}

	@Override
	protected View getPage(int level, LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(GeneralLevelIntros.levelLayoutIds[LEVEL_ID][level],	container, false);
	}

}
