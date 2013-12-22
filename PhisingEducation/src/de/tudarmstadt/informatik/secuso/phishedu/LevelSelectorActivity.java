package de.tudarmstadt.informatik.secuso.phishedu;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import de.tudarmstadt.informatik.secuso.phishedu.backend.BackendController;
import de.tudarmstadt.informatik.secuso.phishedu.backend.NoPhishLevelInfo;

public class LevelSelectorActivity extends SwipeActivity implements
		ViewPager.OnPageChangeListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		getSupportActionBar().setTitle("Level Überblick");
	}

	@Override
	protected int getPageCount() {
		return BackendController.getInstance().getLevelCount();
	}

	@Override
	protected View getPage(int level, LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		NoPhishLevelInfo level_info = BackendController.getInstance().getLevelInfo(level);
		View layoutView = inflater.inflate(R.layout.level_overview_template,
				container, false);
		int userlevel = BackendController.getInstance().getMaxUnlockedLevel();
		View button = layoutView.findViewById(R.id.levelbutton);
		if (level < userlevel) {
			button.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.levelicon_active_bg));
			layoutView.findViewById(R.id.levelbutton_padlock).setVisibility(
					View.INVISIBLE);
			layoutView.findViewById(R.id.levelbutton_tick).setVisibility(
					View.VISIBLE);
		} else if (level == userlevel) {
			button.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.levelicon_active_bg));
			layoutView.findViewById(R.id.levelbutton_padlock).setVisibility(
					View.INVISIBLE);
			layoutView.findViewById(R.id.levelbutton_tick).setVisibility(
					View.INVISIBLE);
		} else {
			button.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.levelicon_inactive_bg));
			layoutView.findViewById(R.id.levelbutton_padlock).setVisibility(
					View.VISIBLE);
			layoutView.findViewById(R.id.levelbutton_tick).setVisibility(
					View.INVISIBLE);
		}
		TextView levelnumber = (TextView) layoutView
				.findViewById(R.id.levelbutton_text);
		levelnumber.setText(Integer.toString(level));
		TextView levelTitle = (TextView) layoutView
				.findViewById(R.id.level_title);
		levelTitle.setText(level_info.titleId);
		TextView levelDescription = (TextView) layoutView
				.findViewById(R.id.level_description);
		levelDescription.setText(level_info.subTitleId);

		return layoutView;
	}

	@Override
	public void onClickPage(int page) {
		if (page <= BackendController.getInstance().getMaxUnlockedLevel()) {
			if (page == 0 && BackendController.getInstance().getLevel()>0 && !Constants.ALLOW_LEVEL0_REPLAY) {
				// level 0 cannot be replayed
				Toast.makeText(getApplicationContext(),
						getString(R.string.cannot_replay_level_0),
						Toast.LENGTH_LONG).show();
			} else {
				BackendController.getInstance().startLevel(page);
			}
		}
	};
}
