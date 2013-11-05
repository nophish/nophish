package de.tudarmstadt.informatik.secuso.phishedu.levelintros;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import de.tudarmstadt.informatik.secuso.phishedu.SwipeActivity;

/**
 * 
 * @author Gamze Canova This class covers the awareness part of the app This
 *         Activity should only be invoked if the user has not done this part
 *         before
 */
public class Level1AddressBarActivity extends SwipeActivity {
	public static final int LEVEL_ID=1;
	
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