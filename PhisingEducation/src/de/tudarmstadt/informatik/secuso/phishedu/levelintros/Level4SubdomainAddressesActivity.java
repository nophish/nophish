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
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;
import de.tudarmstadt.informatik.secuso.phishedu.R;
import de.tudarmstadt.informatik.secuso.phishedu.common.Constants;

public class Level4SubdomainAddressesActivity extends FragmentActivity {

	private MySubdomainAdapter mAdapter;
	private ViewPager mPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		setContentView(R.layout.level_4_splash);
		ActionBar ab = getActionBar();

		ab.setIcon(R.drawable.emblem_library);

		new CountDownTimer(Constants.MILLIS_IN_FUTURE,
				Constants.COUNT_DOWN_INTERVAL) {
			@Override
			public void onTick(long millisUntilFinished) {
			}

			@Override
			public void onFinish() {

				setContentView(R.layout.fragment_pager);
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

				mAdapter = new MySubdomainAdapter(getSupportFragmentManager());
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

	private void nextPage() {
		int currentPage = mPager.getCurrentItem();
		int totalPages = mPager.getAdapter().getCount();

		int nextPage = currentPage + 1;
		if (nextPage >= totalPages) {
			// We can't go forward anymore.
			// Loop to the first page. If you don't want looping just
			// return here.
			nextPage = currentPage;
			Toast.makeText(getApplicationContext(),
					getString(R.string.last_page), Toast.LENGTH_SHORT).show();
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
			Toast.makeText(getApplicationContext(),
					getString(R.string.first_page), Toast.LENGTH_SHORT).show();
		}

		mPager.setCurrentItem(previousPage, true);
	}

	public static class MySubdomainAdapter extends FragmentPagerAdapter {
		private static final int ITEMS = 3;

		public MySubdomainAdapter(FragmentManager fragmentManager) {
			super(fragmentManager);
		}

		@Override
		public int getCount() {
			return ITEMS;
		}

		@Override
		public Fragment getItem(int position) {

			return Level4InfoFragment
					.init(GeneralLevelIntros.level4LayoutIds[position]);

		}
	}

}
