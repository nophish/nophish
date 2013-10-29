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
import de.tudarmstadt.informatik.secuso.phishedu.R;
import de.tudarmstadt.informatik.secuso.phishedu.common.Constants;

/**
 * 
 * @author Gamze Canova This class covers the awareness part of the app This
 *         Activity should only be invoked if the user has not done this part
 *         before
 */
public class Level1AddressBarActivity extends FragmentActivity implements
		ViewPager.OnPageChangeListener {

	private MyInfoAddressBarAdapter mAdapter;
	private ViewPager mPager;
	private ImageView imgNext;
	private ImageView imgPrevious;
	private ActionBar ab;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		setContentView(R.layout.level_1_splash);

		ab = getActionBar();
		ab.setTitle(R.string.level_1_title);

		new CountDownTimer(Constants.MILLIS_IN_FUTURE,
				Constants.COUNT_DOWN_INTERVAL) {
			@Override
			public void onTick(long millisUntilFinished) {
			}

			@Override
			public void onFinish() {

				ab.setTitle(R.string.level_1);
				ab.setSubtitle(R.string.level_1_address_bar_subtitle_1);
				ab.setIcon(R.drawable.emblem_library);

				setContentView(R.layout.fragment_pager);
				// set the new Content of your activity
				imgPrevious = (ImageView) findViewById(R.id.game_intro_arrow_back);
				imgPrevious.setVisibility(View.INVISIBLE);

				imgPrevious.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						previousPage();
					}
				});

				imgNext = (ImageView) findViewById(R.id.game_intro_arrow_forward);
				imgNext.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						nextPage();
					}
				});

				mAdapter = new MyInfoAddressBarAdapter(
						getSupportFragmentManager());
				mPager = (ViewPager) findViewById(R.id.pager);
				mPager.setAdapter(mAdapter);
				mPager.setOnPageChangeListener(Level1AddressBarActivity.this);
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
		checkAndHidePreviousButton(position);
		checkAndHideNextButton(mPager.getAdapter().getCount(), position);

		// adjust actionbar title
		adjustActionBarTitle(position);
	}

	private void adjustActionBarTitle(int position) {
		ab.setTitle(getString(R.string.level_1));

		switch (position) {
		case 0:
			ab.setSubtitle(R.string.level_1_address_bar_subtitle_1);
			break;
		default:
			ab.setSubtitle(R.string.level_1_address_bar_subtitle_2);
			break;
		}
	}

	private void nextPage() {
		int currentPage = mPager.getCurrentItem();
		int totalPages = mPager.getAdapter().getCount();
		int nextPage = currentPage + 1;

		checkAndHideNextButton(totalPages, nextPage);

		mPager.setCurrentItem(nextPage, true);
	}

	private void previousPage() {
		int currentPage = mPager.getCurrentItem();
		int previousPage = currentPage - 1;

		checkAndHidePreviousButton(previousPage);

		mPager.setCurrentItem(previousPage, true);

	}

	private void checkAndHideNextButton(int totalPages, int nextPage) {
		if (nextPage == totalPages - 1) {
			// We can't go forward anymore.
			// make arrow_next invisible
			makeInvisible(imgNext);
			makeVisible(imgPrevious);

		} else {
			// make arrow_next visible
			makeVisible(imgNext);
		}
	}

	private void checkAndHidePreviousButton(int previousPage) {
		if (previousPage == 0) {
			// We can't go back anymore.
			// make back_arrow invisible
			makeInvisible(imgPrevious);
			makeVisible(imgNext);

		} else {
			makeVisible(imgPrevious);
		}
	}

	private void makeInvisible(ImageView imageView) {
		imageView.setVisibility(View.INVISIBLE);
	}

	private void makeVisible(ImageView imageView) {
		imageView.setVisibility(View.VISIBLE);
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
			return Level1InfoFragment
					.init(GeneralLevelIntros.level1LayoutIds[position]);
		}
	}

}
