package de.tudarmstadt.informatik.secuso.phishedu;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import de.tudarmstadt.informatik.secuso.phishedu.backend.BackendControllerImpl;

public class LevelFinishedActivity extends SwipeActivity {
	protected void onStartClick() {
		BackendControllerImpl.getInstance().startLevel(getLevel() + 1);
	}
	
	int level;
	boolean enable_homebutton=false;
	
	@Override
	public void onSwitchTo() {
		if(getArguments().containsKey(Constants.ARG_LEVEL)){
			this.setLevel(getArguments().getInt(Constants.ARG_LEVEL));
		}else{
			this.setLevel(BackendControllerImpl.getInstance().getLevel());
			
		}
		if(getArguments().containsKey(Constants.ARG_ENABLE_HOME)){
			this.enable_homebutton = getArguments().getBoolean(Constants.ARG_ENABLE_HOME);
		}else{
			this.enable_homebutton = false;
		}
		super.onSwitchTo();
	}
	
	private boolean getEnableHome(){
		if(getLevel()==0 && !enable_homebutton){
			return false;
		}else{
			return true;
		}
	}
	
	private void setLevel(int level){
		this.level=level;
	}
	
	int getLevel(){
		return this.level;
	}
	
	@Override
	protected String startButtonText() {
		return "Weiter zu " + getResources().getString(BackendControllerImpl.getInstance().getLevelInfo(this.getLevel()+1).titleId);
	}
	
	@Override
	int getIcon() {
		return R.drawable.desktop;
	}

	@Override
	int getTitle(){
		return BackendControllerImpl.getInstance().getLevelInfo(getLevel()).titleId;
	};
	
	@Override
	int getSubTitle() {
		return R.string.finished;
	}
	
	@Override
	protected int getPageCount() {
		return BackendControllerImpl.getInstance().getLevelInfo(getLevel()).finishedLayouts.length;
	}

	@Override
	protected View getPage(int page, LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(BackendControllerImpl.getInstance().getLevelInfo(getLevel()).finishedLayouts[page],
				container, false);
		setScoreText(view);
		return view;
	}

	private void setScoreText(View view) {
		if (getLevel() > 1) {
			((TextView) view.findViewById(R.id.level_score)).setText(Integer.toString(BackendControllerImpl.getInstance().getLevelPoints()));
			((TextView) view.findViewById(R.id.total_score)).setText(Integer.toString(BackendControllerImpl.getInstance().getTotalPoints()));
		}
	}
	
	@Override
	boolean enableHomeButton() {
		return getEnableHome();
	}
}
