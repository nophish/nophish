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
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;

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

public class MainActivity extends ActionBarActivity implements FrontendController, OnLevelChangeListener, BackendInitListener, OnLevelstateChangeListener {
	Map<Class<? extends PhishBaseActivity>, PhishBaseActivity> fragCache = new HashMap<Class<? extends PhishBaseActivity>, PhishBaseActivity>();
	PhishBaseActivity current_frag;

	@Override
	protected void onStart() {
		super.onStart();
		BackendControllerImpl.getInstance().getGameHelper().onStart(this);
	}

	@Override
	protected void onStop() {
		super.onStop();
		BackendControllerImpl.getInstance().getGameHelper().onStop();
	}

	@Override
	protected void onActivityResult(int requestCode, int responseCode, Intent intent) {
		super.onActivityResult(requestCode, responseCode, intent);
		BackendControllerImpl.getInstance().getGameHelper().onActivityResult(requestCode, responseCode, intent);
	}

	public void switchToFragment(Class<? extends PhishBaseActivity> fragClass) {
		switchToFragment(fragClass, new Bundle());
	}

	public void switchToFragment(Class<? extends PhishBaseActivity> fragClass, Bundle arguments) {
		PhishBaseActivity  newFrag;
		try {
			if(!fragCache.containsKey(fragClass)){
				PhishBaseActivity newinstance=fragClass.newInstance();
				BackendControllerImpl.getInstance().addOnLevelChangeListener(newinstance);
				BackendControllerImpl.getInstance().addOnLevelstateChangeListener(newinstance);
				fragCache.put(fragClass, newinstance);
			}
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		newFrag = fragCache.get(fragClass);

		if(newFrag.getArguments()!=null){
			newFrag.getArguments().clear();
			newFrag.getArguments().putAll(arguments);
		}else{
			newFrag.setArguments(arguments);
		}
		getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, newFrag).commit();
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
	public void onAttachFragment(Fragment fragment) {
		super.onAttachFragment(fragment);
		((PhishBaseActivity)fragment).onSwitchTo();
	}

	@Override
	public void onBackPressed() {
		current_frag.onBackPressed();
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

		clearFragCache();

		switchToFragment(StartMenuActivity.class);

		BackendControllerImpl.getInstance().onUrlReceive(getIntent().getData());
	}

	private void clearFragCache(){
		this.fragCache = new HashMap<Class<? extends PhishBaseActivity>, PhishBaseActivity>();
		fragCache.put(GooglePlusActivity.class, new GooglePlusActivity());
		fragCache.put(LevelIntroActivity.class, new LevelIntroActivity());
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
	public void onLevelChange(int level, boolean showRepeat) {
		((LevelIntroActivity)fragCache.get(LevelIntroActivity.class)).setShowRepeat(showRepeat);
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
		((GooglePlusActivity)fragCache.get(GooglePlusActivity.class)).setShowSignIn(true);
	}

	@Override
	public void onSignInSucceeded() {
		((GooglePlusActivity)fragCache.get(GooglePlusActivity.class)).setShowSignIn(false);
		BackendControllerImpl.getInstance().onSignInSucceeded();
		Games.Achievements.unlock(getApiClient(),getString(R.string.achievement_welcome));
	}

	@Override
	public void onLevelstateChange(Levelstate new_state, int level) {
		Uri data = getIntent().getData();
		if(new_state == Levelstate.finished && data != null && data.getScheme().equals("phishedu")){
			Bundle args = new Bundle();
			args.putInt(Constants.ARG_LEVEL, level);
			switchToFragment(LevelFinishedActivity.class, args);
		}
	}

	private GoogleApiClient getApiClient(){
		return BackendControllerImpl.getInstance().getGameHelper().getApiClient();
	}

}
