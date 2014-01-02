package de.tudarmstadt.informatik.secuso.phishedu;

import java.lang.reflect.Field;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Relation;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import de.tudarmstadt.informatik.secuso.phishedu.backend.BackendControllerImpl;
import de.tudarmstadt.informatik.secuso.phishedu.backend.MainActivity;

public abstract class PhishBaseActivity extends Fragment implements OnClickListener {
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.onSwitchTo();
	}
	
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
		  ((MainActivity)getActivity()).switchToFragment(StartMenuActivity.class);
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
	
	public void onSwitchTo(){};
	
	@Override
	public void onResume() {
		super.onResume();
		updateScore();
		setTitles();
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

		for (int i : getClickables()) {
			View clickview = v.findViewById(i);;
			if(clickview != null){
				clickview.setOnClickListener(this);	
			}
		}
		
		setTitles();

		return v;		 
	}
	
	private void setTitles(){
		android.support.v7.app.ActionBar ab = ((ActionBarActivity)getActivity()).getSupportActionBar();
		ab.setDisplayHomeAsUpEnabled(enableHomeButton());
		ab.setHomeButtonEnabled(enableHomeButton());
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
			ab.setIcon(R.drawable.appicon);
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
			urlsGoalText.setText(Integer.toString(BackendControllerImpl.getInstance().levelCorrectURLs()));
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
				((MainActivity)getActivity()).switchToFragment(StartMenuActivity.class);
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
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			onBackPressed();
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
	
}
