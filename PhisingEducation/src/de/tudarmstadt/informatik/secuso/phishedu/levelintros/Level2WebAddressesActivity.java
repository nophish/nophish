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

public class Level2WebAddressesActivity extends FragmentActivity implements
		ViewPager.OnPageChangeListener {

	private MyWebAddressAdapter mAdapter;
	private ViewPager mPager;
	private ImageView imgPrevious;
	private ImageView imgNext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		setContentView(R.layout.level_2_splash);
		ActionBar ab = getActionBar();

		ab.setTitle(R.string.level_1_web_address_title);
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

				imgPrevious = (ImageView) findViewById(R.id.game_intro_arrow_back);

				imgPrevious.setVisibility(View.INVISIBLE);
				imgPrevious.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						previousPage();
						// mPager.setCurrentItem(0);
					}
				});

				imgNext = (ImageView) findViewById(R.id.game_intro_arrow_forward);
				imgNext.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						// mPager.setCurrentItem(ITEMS - 1);
						nextPage();
					}
				});

				mAdapter = new MyWebAddressAdapter(getSupportFragmentManager(),
						Level2WebAddressesActivity.this);
				mPager = (ViewPager) findViewById(R.id.pager);
				mPager.setAdapter(mAdapter);
				mPager.setOnPageChangeListener(Level2WebAddressesActivity.this);

			}
		}.start();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageSelected(int position) {
		// TODO Auto-generated method stub

		/*
		 * TODO: auslageern
		 */
		if (position == 1) {
			imgPrevious.setVisibility(View.VISIBLE);
		} else if (position == mAdapter.getCount() - 1) {
			imgNext.setVisibility(View.INVISIBLE);
		} else {
			imgPrevious.setVisibility(View.VISIBLE);
			imgNext.setVisibility(View.VISIBLE);
		}

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
			imgPrevious.setVisibility(View.INVISIBLE);

		}

		mPager.setCurrentItem(previousPage, true);
	}

	private void nextPage() {
		int currentPage = mPager.getCurrentItem();

		// make arrow from first page (position 1) visible
		if (currentPage == 1) {
			imgPrevious.setVisibility(View.VISIBLE);
		}

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

	public static class MyWebAddressAdapter extends FragmentPagerAdapter {
		private static final int ITEMS = 12;

		public MyWebAddressAdapter(FragmentManager fragmentManager,
				Level2WebAddressesActivity level2) {
			super(fragmentManager);
		}

		@Override
		public int getCount() {
			return ITEMS;
		}

		@Override
		public Fragment getItem(int position) {

			return Level2InfoFragment
					.init(GeneralLevelIntros.level2LayoutIds[position]);
		}
	}
}
