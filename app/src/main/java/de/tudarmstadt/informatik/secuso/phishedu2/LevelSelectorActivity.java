/*=========================================================================
 * The most reliable way to detect phishing is checking the URL
 * (web address) of a website. We developed an Android app to learn how
 * to detect Phishing URLs.
 * Copyright (C) 2015 SecUSo
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *=========================================================================*/

package de.tudarmstadt.informatik.secuso.phishedu2;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import de.tudarmstadt.informatik.secuso.phishedu2.backend.BackendControllerImpl;
import de.tudarmstadt.informatik.secuso.phishedu2.backend.NoPhishLevelInfo;

public class LevelSelectorActivity extends SwipeActivity implements
ViewPager.OnPageChangeListener {

	@Override
	int getTitle(){
		return R.string.button_level_overview;
	}

	@Override
	protected int getPageCount() {
		int levels=BackendControllerImpl.getInstance().getLevelCount();
		if(Constants.SKIP_LEVEL1){
			levels--;
		}
		return levels;
	}

	@Override
	protected View getPage(int level, LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		if(level>0 && Constants.SKIP_LEVEL1){
			level++;
		}
		NoPhishLevelInfo level_info = BackendControllerImpl.getInstance().getLevelInfo(level);
		View layoutView = inflater.inflate(R.layout.fragment_level_overview_template, container, false);
		int userlevel = BackendControllerImpl.getInstance().getMaxUnlockedLevel();
		View button = layoutView.findViewById(R.id.levelbutton);
		if (level <= userlevel) {
			button.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.levelicon_active_bg));
			layoutView.findViewById(R.id.levelbutton_padlock).setVisibility(
					View.INVISIBLE);
			if(BackendControllerImpl.getInstance().getLevelCompleted(level)){
				layoutView.findViewById(R.id.levelbutton_tick).setVisibility(View.VISIBLE);
				layoutView.findViewById(R.id.levelbutton_points).setVisibility(View.VISIBLE);
				layoutView.findViewById(R.id.levelbutton_points_text).setVisibility(View.VISIBLE);
			}else{
				layoutView.findViewById(R.id.levelbutton_tick).setVisibility(View.INVISIBLE);
				layoutView.findViewById(R.id.levelbutton_points).setVisibility(View.INVISIBLE);
				layoutView.findViewById(R.id.levelbutton_points_text).setVisibility(View.INVISIBLE);
			}
			if(level_info.showStars()){
				showStars(layoutView, BackendControllerImpl.getInstance().getLevelStars(level));
			}else {
				hideStars(layoutView);
			}
		} else {
			button.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.levelicon_inactive_bg));
			layoutView.findViewById(R.id.levelbutton_padlock).setVisibility(
					View.VISIBLE);
			layoutView.findViewById(R.id.levelbutton_tick).setVisibility(
					View.INVISIBLE);
			layoutView.findViewById(R.id.levelbutton_points).setVisibility(
					View.INVISIBLE);
			layoutView.findViewById(R.id.levelbutton_points_text).setVisibility(
					View.INVISIBLE);
			if(level_info.showStars()){
				showStars(layoutView, 0);
			}else {
				hideStars(layoutView);
			}
		}
		TextView levelnumber = (TextView) layoutView.findViewById(R.id.levelbutton_text);
		levelnumber.setText(level_info.getLevelNumber());
		TextView levelTitle = (TextView) layoutView.findViewById(R.id.level_title);
		levelTitle.setText(level_info.titleId);
		TextView levelDescription = (TextView) layoutView.findViewById(R.id.level_description);
		levelDescription.setText(level_info.subTitleId);
		((TextView) layoutView.findViewById(R.id.levelbutton_points)).setText(Integer.toString(BackendControllerImpl.getInstance().getLevelPoints(level)));
		if(level<=1 || level==11){
			layoutView.findViewById(R.id.levelbutton_points).setVisibility(View.INVISIBLE);
			layoutView.findViewById(R.id.levelbutton_points_text).setVisibility(View.INVISIBLE);
		}

		return layoutView;
	}

	@Override
	public void onClickPage(int level) {
		if(level>0 && Constants.SKIP_LEVEL1){
			level++;
		}
		if (level <= BackendControllerImpl.getInstance().getMaxUnlockedLevel()) {
			if (level == 0 && BackendControllerImpl.getInstance().getMaxUnlockedLevel() > 0 && !Constants.ALLOW_REPEAT_AWARENESS) {
				// level 0 cannot be replayed show finished instead.
				Bundle args = new Bundle();
				args.putInt(Constants.ARG_LEVEL, 0);
				args.putBoolean(Constants.ARG_ENABLE_HOME, true);
				switchToFragment(LevelFinishedActivity.class, args);
			} else {
                //Toast.makeText(getActivity(),Integer.toString(level),Toast.LENGTH_LONG).show();
                BackendControllerImpl.getInstance().startLevel(level);
			}
		}
	};
}
