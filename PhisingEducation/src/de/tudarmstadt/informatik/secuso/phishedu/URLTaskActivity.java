package de.tudarmstadt.informatik.secuso.phishedu;

import de.tudarmstadt.informatik.secuso.phishedu.backend.BackendController;

import de.tudarmstadt.informatik.secuso.phishedu.backend.PhishResult;
import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class URLTaskActivity extends Activity {

	private TextView urlText;
	private TextView urlsText;
	private TextView urlsGoalText;
	private TextView phishesText;
	private TextView phishesGoalText;
	private int level;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		this.level=getIntent().getIntExtra(Constants.EXTRA_LEVEL,0);
		
		setContentView(R.layout.urltask_task);

		urlText = (TextView) findViewById(R.id.url_task_url);
		urlsText = (TextView) findViewById(R.id.urls);
		urlsGoalText = (TextView) findViewById(R.id.urls_goal);
		phishesText = (TextView) findViewById(R.id.phishes);
		phishesGoalText = (TextView) findViewById(R.id.phishes_goal);
		nextURL();
		setTitles();
	}
	
	private void setTitles() {
		ActionBar ab = getActionBar();
		
		ab.setTitle(Constants.levelTitlesIds[BackendController.getInstance().getLevel()]);
		ab.setSubtitle(getString(R.string.exercise));
		ab.setIcon(getResources().getDrawable(R.drawable.desktop));
	}

	private void nextURL(){
		String[] urlArray = BackendController.getInstance().getNextUrl();

		// build string from array
		StringBuilder sb = new StringBuilder();
		// adds 9 character string at beginning
		for (int i = 0; i < urlArray.length; i++) {
			sb.append(urlArray[i]);
		}

		urlText.setText(sb.toString());
		urlsText.setText(Integer.toString(BackendController.getInstance().doneURLs()));
		urlsGoalText.setText(Integer.toString(BackendController.getInstance().levelURLs()));
		phishesText.setText(Integer.toString(BackendController.getInstance().foundPhishes()));
		phishesGoalText.setText(Integer.toString(BackendController.getInstance().levelPhishes()));
	}

	public void clickAccept(View view){
		clicked(true);
	}

	public void clickDecline(View view){
		clicked(false);
	}

	private void clicked(boolean acceptance){
		PhishResult result = BackendController.getInstance().userClicked(acceptance);
		Class followActivity=ResultActivity.class;
		if(result == PhishResult.Phish_Detected){
			followActivity = ProofActivity.class;
		}
		Intent levelIntent = new Intent(this, followActivity);
		levelIntent.putExtra(Constants.EXTRA_RESULT, result.getValue());
		levelIntent.putExtra(Constants.EXTRA_LEVEL, this.level);
		levelIntent.putExtra(Constants.EXTRA_TYPE, BackendController.getInstance().getType().getValue());
		startActivityForResult(levelIntent, 1);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		nextURL();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    // Respond to the action bar's Up/Home button
	    case android.R.id.home:
	        NavUtils.navigateUpFromSameTask(this);
	        return true;
	    }
	    return super.onOptionsItemSelected(item);
	}

}
