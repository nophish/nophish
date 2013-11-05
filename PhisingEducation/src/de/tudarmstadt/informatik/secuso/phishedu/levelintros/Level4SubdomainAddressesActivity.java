package de.tudarmstadt.informatik.secuso.phishedu.levelintros;

import android.app.ActionBar;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import de.tudarmstadt.informatik.secuso.phishedu.R;
import de.tudarmstadt.informatik.secuso.phishedu.SwipeActivity;
import de.tudarmstadt.informatik.secuso.phishedu.common.Constants;

public class Level4SubdomainAddressesActivity extends SwipeActivity {
	public static final int LEVEL_ID=4;
	
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