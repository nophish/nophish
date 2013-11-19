package de.tudarmstadt.informatik.secuso.phishedu;

import de.tudarmstadt.informatik.secuso.phishedu.backend.BackendController;
import de.tudarmstadt.informatik.secuso.phishedu.backend.PhishResult;
import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.NavUtils;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class ProofActivity extends Activity {
	int selectedPart=-1;
	int level=0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		setContentView(R.layout.proof);
		setTitles();
		
		this.level=getIntent().getIntExtra(Constants.EXTRA_LEVEL,0);
		
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
		url.setMovementMethod(LinkMovementMethod.getInstance());
		url.setText(builder);
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
			if(this.activity.selectedPart==this.part){
				ds.bgColor=Color.LTGRAY;
			}
		}
	}
	
	public void onDoneClick(View view){
		if(selectedPart==-1){
		  Toast.makeText(getApplicationContext(), getResources().getString(R.string.select_part) , Toast.LENGTH_SHORT).show();
		  return;
		}
		boolean clicked_right = BackendController.getInstance().partClicked(selectedPart);
		int result = PhishResult.Phish_Detected.getValue();
		if(!clicked_right){
			result = ResultActivity.RESULT_GUESSED;
		}
		Intent levelIntent = new Intent(this, ResultActivity.class);
		levelIntent.putExtra(Constants.EXTRA_RESULT, result);
		levelIntent.putExtra(Constants.EXTRA_LEVEL, this.level);
		levelIntent.putExtra(Constants.EXTRA_SITE_TYPE, BackendController.getInstance().getSiteType().getValue());
		levelIntent.putExtra(Constants.EXTRA_ATTACK_TYPE, BackendController.getInstance().getAttackType().getValue());
		startActivityForResult(levelIntent, 1);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		finish();
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
	
	/**
	 * Disable back button so he can not guess again.
	 */
	@Override
	public void onBackPressed() {
		return;
	}
}
