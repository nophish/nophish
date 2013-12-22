package de.tudarmstadt.informatik.secuso.phishedu;

import android.os.Bundle;

import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import de.tudarmstadt.informatik.secuso.phishedu.backend.BackendController;
import de.tudarmstadt.informatik.secuso.phishedu.backend.NoPhishLevelInfo;

public class LevelFinishedActivity extends SwipeActivity {
	int level = 0;

	protected void onCreate(Bundle savedInstanceState) {
		this.level = getIntent().getIntExtra(Constants.EXTRA_LEVEL, 0);

		super.onCreate(savedInstanceState);
	}

	protected void onStartClick() {
		BackendController.getInstance().startLevel(level + 1);
	}

	@Override
	protected String startButtonText() {
		return "Weiter zu Level " + (level + 1);
	}

	@Override
	protected int getPageCount() {
		return BackendController.getInstance().getLevelInfo(level).finishedLayouts.length;
	}

	@Override
	protected View getPage(int page, LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		setTitles();
		View view = inflater.inflate(BackendController.getInstance().getLevelInfo(level).finishedLayouts[page],
				container, false);
		setScoreText(view);
		return view;
	}

	private void setScoreText(View view) {
		if (level > 1) {
			((TextView) view.findViewById(R.id.level_score)).setText(Integer.toString(BackendController.getInstance().getLevelPoints()));
			((TextView) view.findViewById(R.id.total_score)).setText(Integer.toString(BackendController.getInstance().getTotalPoints()));
		}
	}

	private void setTitles() {
		ActionBar ab = getSupportActionBar();
		NoPhishLevelInfo level_info = BackendController.getInstance().getLevelInfo();
		String title = getString(level_info.titleId);
		String subtitle = getString(level_info.subTitleId);

		if (!title.equals(subtitle)) {
			// if subtitle and title are different, subtitle is set
			ab.setSubtitle(subtitle);
		}
		// title is set in anyway
		ab.setTitle(title);
	}

	/**
	 * after finishing a level the user is not allowed to go back
	 */
	@Override
	public void onBackPressed() {
		return;
	}
}
