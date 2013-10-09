package de.tudarmstadt.informatik.secuso.phishedu;

import android.app.ActionBar;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Menu;

public class IntroGameActivity extends FragmentActivity {

	MyAdapter mAdapter;
	ViewPager mPager;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.blank_layout);
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		ActionBar ab = getActionBar();
		ab.setIcon(R.drawable.emblem_library);

		mAdapter = new MyAdapter(getSupportFragmentManager());
		mPager = (ViewPager) findViewById(R.id.pager);
		mPager.setAdapter(mAdapter);
	}

	public static class MyAdapter extends FragmentPagerAdapter {
		private static final int ITEMS = 4;

		public MyAdapter(FragmentManager fragmentManager) {
			super(fragmentManager);
		}

		@Override
		public int getCount() {
			return ITEMS;
		}

		@Override
		public Fragment getItem(int position) {
			switch (position) {
			case 0: // Fragment # 0 - This will show image
				return IntroGameFragment.init(position);
			case 1: // Fragment # 1 - This will show image
				return IntroGameWhatIsPhishingFragment.init(position);
			case 2:
				return IntroGameSecurityComicFragment.init(position);
			default:// Fragment # 3 - Will show image with button
				return IntroGameLetsStartFragment.init(position);
			}
		}
	}


}
