package de.tudarmstadt.informatik.secuso.phishedu;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import de.tudarmstadt.informatik.secuso.phishedu.backend.BackendController;

public class LevelSelectorActivity extends SwipeActivity implements
		ViewPager.OnPageChangeListener {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		ActionBar ab = getActionBar();
		ab.setTitle("Level Überblick");
	}

	@Override
	protected int getPageCount() {
		return Math.min(Constants.levelTitlesIds.length, Constants.levelSubtitlesIds.length);
	}

	@Override
	protected View getPage(int level, LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View layoutView = inflater.inflate(R.layout.level_overview_template,container, false);
		int userlevel = BackendController.getInstance().getMaxUnlockedLevel();
		View button = layoutView.findViewById(R.id.levelbutton);
		if(level< userlevel){
			button.setBackgroundDrawable(getResources().getDrawable(R.drawable.levelicon_active_bg));
			layoutView.findViewById(R.id.levelbutton_padlock).setVisibility(View.INVISIBLE);
			layoutView.findViewById(R.id.levelbutton_tick).setVisibility(View.VISIBLE);
		}else if(level == userlevel) {
			button.setBackgroundDrawable(getResources().getDrawable(R.drawable.levelicon_active_bg));
			layoutView.findViewById(R.id.levelbutton_padlock).setVisibility(View.INVISIBLE);
			layoutView.findViewById(R.id.levelbutton_tick).setVisibility(View.INVISIBLE);
		}else{
			button.setBackgroundDrawable(getResources().getDrawable(R.drawable.levelicon_inactive_bg));
			layoutView.findViewById(R.id.levelbutton_padlock).setVisibility(View.VISIBLE);
			layoutView.findViewById(R.id.levelbutton_tick).setVisibility(View.INVISIBLE);
		}
		TextView levelnumber = (TextView) layoutView.findViewById(R.id.levelbutton_text);
		levelnumber.setText(Integer.toString(level));
		TextView levelTitle = (TextView) layoutView.findViewById(R.id.level_title);
		levelTitle.setText(Constants.levelTitlesIds[level]);
		TextView levelDescription = (TextView) layoutView.findViewById(R.id.level_description);
		levelDescription.setText(Constants.levelSubtitlesIds[level]);
		
		return layoutView;
	}
	
	@Override
	public void onClickPage(int page) {
		if(page <= BackendController.getInstance().getMaxUnlockedLevel()){
			BackendController.getInstance().startLevel(page);	
		}
	};
}
