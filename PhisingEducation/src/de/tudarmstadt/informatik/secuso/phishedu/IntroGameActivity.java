package de.tudarmstadt.informatik.secuso.phishedu;

import android.app.ActionBar;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;

public class IntroGameActivity extends FragmentActivity {

	MyIntroAdapter mAdapter;
	ViewPager mPager;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_pager);
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		ActionBar ab = getActionBar();
		ab.setIcon(R.drawable.emblem_library);

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

		mAdapter = new MyIntroAdapter(getSupportFragmentManager());
		mPager = (ViewPager) findViewById(R.id.pager);
		mPager.setAdapter(mAdapter);
	}

	public static class MyIntroAdapter extends FragmentPagerAdapter {
		private static final int ITEMS = 4;

		public MyIntroAdapter(FragmentManager fragmentManager) {
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
				return IntroGameWhatIsPhishingFragment.init(position);
			case 1: // Fragment # 1 - This will show image
				return IntroGameSecurityComicFragment.init(position);
			case 2:
				return IntroGameFragment.init(position);
			default:// Fragment # 3 - Will show image with button
				return IntroGameLetsStartFragment.init(position);
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
			Toast.makeText(getApplicationContext(), "This is the last page",
					Toast.LENGTH_SHORT).show();
		}

		mPager.setCurrentItem(nextPage, true);
	}

	private void previousPage() {
		int currentPage = mPager.getCurrentItem();
		int totalPages = mPager.getAdapter().getCount();

		int previousPage = currentPage - 1;
		if (previousPage < 0) {
			// We can't go back anymore.
			// Loop to the last page. If you don't want looping just
			// return here.
			previousPage = currentPage;
			Toast.makeText(getApplicationContext(), "This is the first page",
					Toast.LENGTH_SHORT).show();
		}

		mPager.setCurrentItem(previousPage, true);
	}

	public void goToWhatIsPhishing(View view) {
		IntroGameWhatIsPhishingFragment.init(0);
		Log.i("TEST", "TST");
	}

}
