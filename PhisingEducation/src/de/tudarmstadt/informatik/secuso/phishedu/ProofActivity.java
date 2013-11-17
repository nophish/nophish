package de.tudarmstadt.informatik.secuso.phishedu;

import de.tudarmstadt.informatik.secuso.phishedu.backend.BackendController;
import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

public class ProofActivity extends Activity {
	int selectedPart=0;
	int level=0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.proof);
		setTitles();
		
		this.level=getIntent().getIntExtra(Constants.LEVEL_EXTRA_STRING,0);
		
		String[] urlparts = BackendController.getInstance().getUrl();
		SpannableStringBuilder builder = new SpannableStringBuilder();
		
		for(int i=0; i< urlparts.length; i++){
			String urlpart = urlparts[i];
			int wordstart = builder.length();
			int wordend= wordstart+urlpart.length();
			builder.append(urlpart);
			
			ClickableSpan span = new ClickSpan(this,i);
			builder.setSpan(span, wordstart, wordend, 0);
		}
		TextView url = (TextView) findViewById(R.id.url);
		url.setText(builder);
		url.setMovementMethod(LinkMovementMethod.getInstance());
	}
	
	private void setTitles() {
		ActionBar ab = getActionBar();
		ab.setTitle(R.string.correct);
		ab.setSubtitle(getString(R.string.phish));
		ab.setIcon(getResources().getDrawable(R.drawable.desktop));

	}

	private class ClickSpan extends ClickableSpan{
		int part;
		ProofActivity activity;
		public ClickSpan(ProofActivity activity, int part){
			this.part=part;
			this.activity=activity;
		}
		public void onClick(View widget) {
			this.activity.selectedPart=this.part;
		}
		
		@Override
		public void updateDrawState(TextPaint ds) {
			super.updateDrawState(ds);
			ds.setColor(Color.BLACK);
			ds.setUnderlineText(false);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.proof, menu);
		return true;
	}
	
	public void onDoneClick(View view){
		boolean clicked_right = BackendController.getInstance().partClicked(selectedPart);
		Class followActivity;
		if(clicked_right){
			followActivity=YouAreCorrectActivity.class;
		}else{
			followActivity=YouGuessedActivity.class;
		}
		Intent levelIntent = new Intent(this, followActivity);
		levelIntent.putExtra(Constants.LEVEL_EXTRA_STRING, this.level);
		levelIntent.putExtra(Constants.TYPE_EXTRA_STRING, BackendController.getInstance().getType().getValue());
		startActivityForResult(levelIntent, 1);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		finish();
	}

}
