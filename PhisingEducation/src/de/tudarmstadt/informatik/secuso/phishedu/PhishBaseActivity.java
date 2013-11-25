package de.tudarmstadt.informatik.secuso.phishedu;

import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import de.tudarmstadt.informatik.secuso.phishedu.backend.BackendController;

public class PhishBaseActivity extends ActionBarActivity {
	protected void updateScore(){
    	updateScore(findViewById(R.id.score_relative));
    }
	
	protected void updateScore(View view){
		if(view == null){
			return;
		}
		RelativeLayout scores = (RelativeLayout) view.findViewById(R.id.score_relative);
		if(scores != null){
			TextView urlsText = (TextView) scores.findViewById(R.id.urls);
			TextView urlsGoalText = (TextView) scores.findViewById(R.id.urls_goal);
			TextView phishesText = (TextView) scores.findViewById(R.id.phishes);
			TextView phishesGoalText = (TextView) scores.findViewById(R.id.phishes_goal);
			TextView scoreText = (TextView) scores.findViewById(R.id.score);

			urlsText.setText(Integer.toString(BackendController.getInstance()
					.doneURLs()));
			urlsGoalText.setText(Integer.toString(BackendController.getInstance()
					.levelURLs()));
			phishesText.setText(Integer.toString(BackendController.getInstance()
					.foundPhishes()));
			phishesGoalText.setText(Integer.toString(BackendController
					.getInstance().levelPhishes()));
			scoreText.setText(Integer.toString(BackendController.getInstance().getPoints()));
		}
	}
}
