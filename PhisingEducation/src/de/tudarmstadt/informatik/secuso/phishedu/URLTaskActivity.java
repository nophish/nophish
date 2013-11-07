package de.tudarmstadt.informatik.secuso.phishedu;

import de.tudarmstadt.informatik.secuso.phishedu.backend.BackendController;
import de.tudarmstadt.informatik.secuso.phishedu.backend.PhishResult;
import de.tudarmstadt.informatik.secuso.phishedu.backend.networkTasks.GetUrlsTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

public class URLTaskActivity extends Activity {

	private TextView urlText;
	private int level;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.level=getIntent().getIntExtra(Constants.LEVEL_EXTRA_STRING,0);
		
		setContentView(R.layout.urltask_task);

		urlText = (TextView) findViewById(R.id.url_task_url);
		nextURL();
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
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.urltask, menu);
		return true;
	}

	public void clickAccept(View view){
		clicked(true);
	}

	public void clickDecline(View view){
		clicked(false);
	}

	private void clicked(boolean acceptance){
		PhishResult result = BackendController.getInstance().userClicked(acceptance);
		Class followActivity=ConsequencesActivity.class;
		switch(result){
		case NoPhish_Detected:
			followActivity=YouAreCorrectActivity.class;
			break;
		case NoPhish_NotDetected:
			followActivity=OversafeActivity.class;
			break;
		case Phish_Detected:
			break;
		case Phish_NotDetected:
			followActivity=ConsequencesActivity.class;
			break;
		}
		Intent levelIntent = new Intent(this, followActivity);
		levelIntent.putExtra(Constants.LEVEL_EXTRA_STRING, this.level);
		levelIntent.putExtra(Constants.TYPE_EXTRA_STRING, BackendController.getInstance().getType().getValue());
		startActivityForResult(levelIntent, 1);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		nextURL();
	}

}
