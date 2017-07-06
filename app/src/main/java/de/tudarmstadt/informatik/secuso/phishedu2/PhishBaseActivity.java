/*=========================================================================
 * The most reliable way to detect phishing is checking the URL
 * (web address) of a website. We developed an Android app to learn how
 * to detect Phishing URLs.
 * Copyright (C) 2015 SECUSO
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

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.lang.reflect.Field;

import de.tudarmstadt.informatik.secuso.phishedu2.backend.BackendController.Levelstate;
import de.tudarmstadt.informatik.secuso.phishedu2.backend.BackendController.OnLevelChangeListener;
import de.tudarmstadt.informatik.secuso.phishedu2.backend.BackendController.OnLevelstateChangeListener;
import de.tudarmstadt.informatik.secuso.phishedu2.backend.BackendControllerImpl;
import de.tudarmstadt.informatik.secuso.phishedu2.backend.FrontendController;

public abstract class PhishBaseActivity extends Fragment implements OnClickListener, OnLevelChangeListener, OnLevelstateChangeListener {
	
	public void updateUI(){
		if(getActivity()!=null){
			
			for (int i : getClickables()) {
				View clickview = getActivity().findViewById(i);
				if(clickview != null){
					clickview.setOnClickListener(this);	
				}
			}
			
			setTitles();
			updateScore();
			
			updateUI(getActivity());
		}
	}
	
	FrontendController getFrontedController(){
		return (FrontendController) getActivity();
	}
	
	void updateUI(Activity activity){};
	
	/**
	 * Get the id of the Layout of this fragment.
	 * You have to implement one of this and {@link #getLayout(LayoutInflater, ViewGroup, Bundle)}
	 * @return the base layout 
	 */
	public int getLayout(){
		return 0;
	}
	
	public View getLayout(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		return null;
	}
	
	/**
	 * If the fragment wants to react on the backpressed button it can implements this function
	 * @return return true to continue with the back event. False otherwise. 
	 */
	public void onBackPressed(){
		if(enableHomeButton()){
			getFrontedController().showMainMenu();
		}
	};

	/**
	 * If the Fragment wants to set the titles it can overwrite this
	 */
	int getTitle(){
		return 0;
	};
	int getSubTitle(){
		return 0;
	};
	int getIcon(){return 0;}
	boolean enableHomeButton(){return true;};
	boolean enableRestartButton(){return false;};
	
	public void onSwitchTo(){};
	
	@Override
	public void onResume() {
		super.onResume();
		updateUI();
	}
	
	/**
	 * If there are clickable elements on this page you must list them here and implement {@link #onClick(View)}
	 * @return the list of resource IDs of the clickable elements.
	 */
	public int[] getClickables(){return new int[0];};
	public void onClick(View view){};
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v;
		if(getLayout()!=0){
			v = inflater.inflate(getLayout(), container, false);
		}else{
			v = getLayout(inflater, container, savedInstanceState);
		}

//        if(getLayout() == R.layout.about) {
//            ImageView logo = (ImageView) v.findViewById(R.id.secuso_logo);
//            logo.setOnClickListener(new OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent();
//                    intent.setAction(Intent.ACTION_VIEW);
//                    intent.addCategory(Intent.CATEGORY_BROWSABLE);
//                    intent.setData(Uri.parse("http://secuso.org/nophish"));
//                    startActivity(intent);
//                }
//            });
//        }
		
		setHasOptionsMenu(true);

		return v;		 
	}
	
	private void setTitles(){
		android.support.v7.app.ActionBar ab = ((ActionBarActivity)getActivity()).getSupportActionBar();
        //ab.setDisplayUseLogoEnabled(true);
		ab.setDisplayHomeAsUpEnabled(enableHomeButton());
        ab.setDisplayShowHomeEnabled(true);
		ab.setHomeButtonEnabled(true);
        ab.setDisplayShowTitleEnabled(true);
		if(getTitle()!=0){
			ab.setTitle(getTitle());
		}else{
			ab.setTitle(R.string.app_name);
		}
		if(getSubTitle()!=0){
			ab.setSubtitle(getSubTitle());
		}else{
			ab.setSubtitle(null);
		}
		if(getIcon()!=0){
			ab.setIcon(getIcon());
		}else{
			ab.setIcon(R.drawable.appicon_small);
		}

        if(getLevel() == 11) {
            ab.setSubtitle(getString(R.string.level_11_splash_subtitle));
        }
	}
	
	protected void updateScore(){
		Activity view = getActivity();
		if(view != null){
			updateScore(view.findViewById(R.id.score_relative));
		}
	}

	protected void updateScore(View view){
		if(view == null){
			return;
		}
		RelativeLayout scores = (RelativeLayout) view.findViewById(R.id.score_relative);
		if(scores != null){
			TextView urlsText = (TextView) scores.findViewById(R.id.urls);
			TextView urlsGoalText = (TextView) scores.findViewById(R.id.urls_goal);
			ImageView lifeOne = (ImageView) scores.findViewById(R.id.life_1);
			ImageView lifeTwo = (ImageView) scores.findViewById(R.id.life_2);
			ImageView lifeThree = (ImageView) scores.findViewById(R.id.life_3);
			TextView LevelScoreText = (TextView) scores.findViewById(R.id.level_score);

			urlsText.setText(Integer.toString(BackendControllerImpl.getInstance().getCorrectlyFoundURLs()));
			urlsGoalText.setText(Integer.toString(BackendControllerImpl.getInstance().getLevelInfo().levelCorrectURLs()));
			LevelScoreText.setText(Integer.toString(BackendControllerImpl.getInstance().getLevelPoints()));
			
			int remaininLives = BackendControllerImpl.getInstance().getLifes();
			
			//now hide hearts if required
			switch (remaininLives) {
			case 0:
				//hide all hearts
				lifeOne.setVisibility(View.INVISIBLE);
				lifeTwo.setVisibility(View.INVISIBLE);
				lifeThree.setVisibility(View.INVISIBLE);
				break;
			case 1:
				//hide heart 1 and 2
				lifeOne.setVisibility(View.INVISIBLE);
				lifeTwo.setVisibility(View.INVISIBLE);
				break;
			case 2:
				//hide only heart 1
				lifeOne.setVisibility(View.INVISIBLE);
			default:
				break;
			}
		}
	}
	
	
	protected void levelRestartWarning() {
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());

		// Setting Dialog Title
		alertDialog.setTitle(getString(R.string.level_restart_title));

		// Setting Dialog Message
		alertDialog.setMessage(getString(R.string.level_restart_text));

		alertDialog.setPositiveButton(R.string.level_restart_positive_button, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				BackendControllerImpl.getInstance().restartLevel();
			}
		});

		alertDialog.setNegativeButton(R.string.level_restart_negative_button,
				new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});

		// Showing Alert Message
		alertDialog.show();
	}
	
	protected void levelCanceldWarning() {
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());

		// Setting Dialog Title
		alertDialog.setTitle(getString(R.string.level_cancel_title));

		// Setting Dialog Message
		alertDialog.setMessage(getString(R.string.level_cancel_text));

		alertDialog.setPositiveButton(R.string.level_cancel_positive_button, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				BackendControllerImpl.getInstance().abortLevel();
			}
		});

		alertDialog.setNegativeButton(R.string.level_cancel_negative_button,
				new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});

		// Showing Alert Message
		alertDialog.show();
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.urltask_menu, menu);
	}
	
	@Override
	public void onPrepareOptionsMenu(Menu menu) {
		super.onPrepareOptionsMenu(menu);
		MenuItem restart = menu.findItem(R.id.restart_level);
		restart.setVisible(enableRestartButton());
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int itemId = item.getItemId();
		if (itemId == android.R.id.home) {
			onBackPressed();
		} else if (itemId == R.id.restart_level) {
			levelRestartWarning();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	int getLevel(){
		return BackendControllerImpl.getInstance().getLevel();
	}
	
	@Override
	public void onDetach() {
	    super.onDetach();

	    try {
	        Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
	        childFragmentManager.setAccessible(true);
	        childFragmentManager.set(this, null);

	    } catch (NoSuchFieldException e) {
	        throw new RuntimeException(e);
	    } catch (IllegalAccessException e) {
	        throw new RuntimeException(e);
	    }
	}
	
	protected void switchToFragment(Class<?extends PhishBaseActivity> target){
		((MainActivity)getActivity()).switchToFragment(target);
	}
	
	protected void switchToFragment(Class<?extends PhishBaseActivity> target, Bundle args){
		((MainActivity)getActivity()).switchToFragment(target,args);
	}
	
	@Override
	public void onLevelChange(int new_levelid, boolean showRepeat) {}

	@Override
	public void onLevelstateChange(Levelstate new_state, int level) {}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
	    //No call for super(). Bug on API Level > 11.
	}
	
}
