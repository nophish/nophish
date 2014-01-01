package de.tudarmstadt.informatik.secuso.phishedu.backend;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import de.tudarmstadt.informatik.secuso.phishedu.Constants;
import de.tudarmstadt.informatik.secuso.phishedu.GooglePlusActivity;
import de.tudarmstadt.informatik.secuso.phishedu.LevelFinishedActivity;
import de.tudarmstadt.informatik.secuso.phishedu.LevelIntroActivity;
import de.tudarmstadt.informatik.secuso.phishedu.PhishBaseActivity;
import de.tudarmstadt.informatik.secuso.phishedu.R;
import de.tudarmstadt.informatik.secuso.phishedu.StartMenuActivity;
import de.tudarmstadt.informatik.secuso.phishedu.backend.BackendController.BackendInitListener;
import de.tudarmstadt.informatik.secuso.phishedu.backend.BackendController.Levelstate;
import de.tudarmstadt.informatik.secuso.phishedu.backend.BackendController.OnLevelChangeListener;
import de.tudarmstadt.informatik.secuso.phishedu.backend.BackendController.OnLevelstateChangeListener;

public class MainActivity extends PhishBaseGameActivity implements FrontendController, OnLevelChangeListener, BackendInitListener, OnLevelstateChangeListener {
	Map<Class<? extends PhishBaseActivity>, PhishBaseActivity> fragCache = new HashMap<Class<? extends PhishBaseActivity>, PhishBaseActivity>();
	PhishBaseActivity current_frag;
	
	public void switchToFragment(Class<? extends PhishBaseActivity> fragClass) {
		switchToFragment(fragClass, new Bundle());
    }
	
	public void switchToFragment(Class<? extends PhishBaseActivity> fragClass, Bundle arguments) {
		PhishBaseActivity  newFrag;
		if(!fragCache.containsKey(fragClass)){
			try {
				fragCache.put(fragClass, fragClass.newInstance());
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		newFrag = fragCache.get(fragClass);
		newFrag.setArguments(arguments);
		getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, newFrag).commitAllowingStateLoss();
		/**
		 * ensure that we only run onswitchto when attached.
		 * this is also called in PhishBaseActivity.onAttack() 
		 */
		if(newFrag.getActivity()!=null){
			newFrag.onSwitchTo();
		}
		current_frag = (PhishBaseActivity)newFrag;
    }
	
	@Override
	public void onBackPressed() {
		if(current_frag.onBackPressed()){
			super.onBackPressed();
		}
	}
	
	@Override
	protected void onCreate(Bundle b) {
		super.onCreate(b);
		setContentView(R.layout.main);

		if(!BackendControllerImpl.getInstance().isInitDone()){
			BackendControllerImpl.getInstance().init(this,this);
		}
		BackendControllerImpl.getInstance().addOnLevelChangeListener(this);
		BackendControllerImpl.getInstance().addOnLevelstateChangeListener(this);
		
		switchToFragment(StartMenuActivity.class);
		
		BackendControllerImpl.getInstance().onUrlReceive(getIntent().getData());
	}

	@Override
	public void displayToast(String message) {
		Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT)
		.show();
	}

	@Override
	public void displayToast(int message) {
		Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT)
		.show();
	}

	@Override
	public void onInitDone() {
		// displayToast("we are finished with initialization!");
	}

	public Context getContext() {
		return this;
	}

	@Override
	public void initProgress(int percent) {
		if (percent % 10 == 0) {
			displayToast("init Progress:" + percent);
		}
	}

	@Override
	public void startBrowser(Uri url) {
		Intent browserIntent = new Intent(Intent.ACTION_VIEW, url);
		this.startActivity(browserIntent);
	}

	@Override
	public void onLevelChange(int level) {
		switchToFragment(LevelIntroActivity.class);
	}
	@Override
	public void displayToastScore(int score) {
		LayoutInflater inflater = getLayoutInflater();
		View layout = inflater.inflate(R.layout.fragment_toast_score,
				(ViewGroup) findViewById(R.id.toast_layout_root));

		String scoreString = Integer.toString(score);

		TextView text = (TextView) layout.findViewById(R.id.text);
		if (score < 0) {
			// red
			text.setTextColor(Color.rgb(135, 0, 0));
		} else {
			// green
			text.setTextColor(Color.rgb(0, 135, 0));
			scoreString = "+ " + score;
		}

		text.setText(scoreString + " Punkte");
		text.setTypeface(Typeface.DEFAULT_BOLD);
		Toast toast = new Toast(getApplicationContext());
		toast.setDuration(Toast.LENGTH_SHORT);
		toast.setView(layout);
		toast.show();
	}

	@Override
	public Activity getBaseActivity() {
		return this;
	}

	@Override
	public void onSignInFailed() {
		if(fragCache.containsKey(GooglePlusActivity.class)){
			((GooglePlusActivity) fragCache.get(GooglePlusActivity.class)).onSignInFailed();
		}
	}

	@Override
	public void onSignInSucceeded() {
		if(fragCache.containsKey(GooglePlusActivity.class)){
			((GooglePlusActivity) fragCache.get(GooglePlusActivity.class)).onSignInSucceeded();
		}
	}

	@Override
	public void onLevelstateChange(Levelstate new_state, int levelid) {
		//The Intend only works when the user actually is in this activity.
		//This is only the case when he clicked an phishedu URL
		Uri data = getIntent().getData();
		if(new_state == Levelstate.finished && data != null && data.getScheme().equals("phishedu")){
			switchToFragment(LevelFinishedActivity.class);
		}
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return current_frag.onOptionsItemSelected(item);
	}
}
