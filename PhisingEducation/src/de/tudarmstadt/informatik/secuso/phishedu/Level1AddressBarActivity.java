package de.tudarmstadt.informatik.secuso.phishedu;

import android.app.ActionBar;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * 
 * @author Gamze Canova This class covers the awareness part of the app This
 *         Activity should only be invoked if the user has not done this part
 *         before
 */
public class Level1AddressBarActivity extends FragmentActivity {

	private static final int COUNT_DOWN_INTERVAL = 1000;
	private static final int MILLIS_IN_FUTURE = 3000;
	private MyInfoAddressBarAdapter mAdapter;
	private ViewPager mPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		
		
		
		setContentView(R.layout.level_1_splash);
		ActionBar ab = getActionBar();
		
		
		ab.setTitle(R.string.level_1_address_bar_title);

		new CountDownTimer(MILLIS_IN_FUTURE, COUNT_DOWN_INTERVAL) {
			@Override
			public void onTick(long millisUntilFinished) {
			}

			@Override
			public void onFinish() {

				setContentView(R.layout.blank_layout);
				// set the new Content of your activity
				ImageView imgPrevious = (ImageView) findViewById(R.id.game_intro_arrow_back);
				imgPrevious.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						previousPage();
						// mPager.setCurrentItem(0);
					}
				});

				ImageView imgNext = (ImageView) findViewById(R.id.game_intro_arrow_forward);
				imgNext.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						// mPager.setCurrentItem(ITEMS - 1);
						nextPage();
					}
				});

				mAdapter = new MyInfoAddressBarAdapter(
						getSupportFragmentManager());
				mPager = (ViewPager) findViewById(R.id.pager);
				mPager.setAdapter(mAdapter);
			}
		}.start();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public static class MyInfoAddressBarAdapter extends FragmentPagerAdapter {
		private static final int ITEMS = 2;

		public MyInfoAddressBarAdapter(FragmentManager fragmentManager) {
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
				return Level1InfoFragmentOne.init(position);
			default: // Fragment # 1 - This will show image
				return Level1InfoFragmentTwo.init(position);
			}
		}
	}

	private void nextPage() {
		int currentPage = mPager.getCurrentItem();
		int totalPages = mPager.getAdapter().getCount();

		int nextPage = currentPage + 1;
		if (nextPage >= totalPages) {
			// We can't go forward anymore.
			// Loop to the first page. If you don't want looping just
			// return here.
			nextPage = currentPage;
			Toast.makeText(getApplicationContext(), getString(R.string.last_page),
					Toast.LENGTH_SHORT).show();
		}

		mPager.setCurrentItem(nextPage, true);
	}

	private void previousPage() {
		int currentPage = mPager.getCurrentItem();

		int previousPage = currentPage - 1;
		if (previousPage < 0) {
			// We can't go back anymore.
			// Loop to the last page. If you don't want looping just
			// return here.
			previousPage = currentPage;
			Toast.makeText(getApplicationContext(), getString(R.string.first_page),
					Toast.LENGTH_SHORT).show();
		}

		mPager.setCurrentItem(previousPage, true);
	}

}
