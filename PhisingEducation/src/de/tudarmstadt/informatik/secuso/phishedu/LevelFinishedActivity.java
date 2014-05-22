package de.tudarmstadt.informatik.secuso.phishedu;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import de.tudarmstadt.informatik.secuso.phishedu.backend.BackendControllerImpl;
import de.tudarmstadt.informatik.secuso.phishedu.backend.NoPhishLevelInfo;

public class LevelFinishedActivity extends SwipeActivity {
	protected void onStartClick() {
		int level = BackendControllerImpl.getInstance().getLevel();
		int levelCount = BackendControllerImpl.getInstance().getLevelCount();
		if(level == levelCount-3 && Constants.SKIP_HTTPS){
			switchToFragment(AppEndActivity.class);
		}else{
			BackendControllerImpl.getInstance().startLevel(getLevel() + 1);
		}
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
		if(BackendControllerImpl.getInstance().getLevel() == BackendControllerImpl.getInstance().getLevelCount()-3){
			return "Weiter";
		}
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
		NoPhishLevelInfo levelinfo = BackendControllerImpl.getInstance().getLevelInfo(getLevel());
		View view = inflater.inflate(levelinfo.finishedLayouts[page], container, false);
		setScoreText(view);

		TextView outroText = (TextView) view.findViewById(R.id.level_outro_text);
		if(outroText != null){
			if(levelinfo.outroId > 0){
				outroText.setText(levelinfo.outroId);
				outroText.setVisibility(View.VISIBLE);
			}else{
				outroText.setVisibility(View.GONE);	
			}

		}

		return view;
	}

	private void setScoreText(View view) {
		if (getLevel() > 1) {
			((TextView) view.findViewById(R.id.level_score)).setText(Integer.toString(BackendControllerImpl.getInstance().getLevelPoints()));
			((TextView) view.findViewById(R.id.total_score)).setText(Integer.toString(BackendControllerImpl.getInstance().getTotalPoints()));
			((TextView) view.findViewById(R.id.level_max_score)).setText(Integer.toString(BackendControllerImpl.getInstance().getLevelmaxPoints()));
		}
	}

	@Override
	boolean enableHomeButton() {
		return getEnableHome();
	}

}
