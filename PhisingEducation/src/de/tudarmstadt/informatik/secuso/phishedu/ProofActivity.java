package de.tudarmstadt.informatik.secuso.phishedu;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import de.tudarmstadt.informatik.secuso.phishedu.backend.BackendControllerImpl;
import de.tudarmstadt.informatik.secuso.phishedu.backend.PhishResult;

public class ProofActivity extends PhishBaseActivity {
	int selectedPart = -1;
	int level = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		setContentView(R.layout.proof);
		updateScore();

		this.level = getIntent().getIntExtra(Constants.EXTRA_LEVEL, 0);

		if (level == 2) {
			TextView text = (TextView) findViewById(R.id.phish_proof_text);
			text.setText(R.string.level_02_task);

			ImageView image = (ImageView) findViewById(R.id.feedback_smiley);
			image.setVisibility(View.INVISIBLE);
		}
		
		setTitles();

		String[] urlparts = BackendControllerImpl.getInstance().getUrl().getParts();
		SpannableStringBuilder builder = new SpannableStringBuilder();

		for (int i = 0; i < urlparts.length; i++) {
			String urlpart = urlparts[i];
			int wordstart = builder.length();
			int wordend = wordstart + urlpart.length();
			builder.append(urlpart);

			ClickableSpan span = new ClickSpan(this, i);
			builder.setSpan(span, wordstart, wordend, 0);
		}
		TextView url = (TextView) findViewById(R.id.url);
		url.setText(builder);
		url.setMovementMethod(LinkMovementMethod.getInstance());
		url.setHighlightColor(Color.LTGRAY);
		url.setTextSize(25);
	}

	private void setTitles() {
		android.support.v7.app.ActionBar ab = getSupportActionBar();
		if (level != 2) {
			ab.setTitle(R.string.correct);
			ab.setSubtitle(getString(R.string.phish));
		} else {
			ab.setTitle(BackendControllerImpl.getInstance().getLevelInfo().titleId);
			ab.setSubtitle(getString(R.string.exercise));
		}
		ab.setIcon(getResources().getDrawable(R.drawable.desktop));
	}

	private class ClickSpan extends ClickableSpan {
		int part;
		ProofActivity activity;

		public ClickSpan(ProofActivity activity, int part) {
			this.part = part;
			this.activity = activity;
		}

		public void onClick(View widget) {
			this.activity.selectedPart = this.part;
			widget.invalidate();
		}

		@Override
		public void updateDrawState(TextPaint ds) {
			super.updateDrawState(ds);
			if(this.activity.selectedPart == this.part){
				ds.bgColor = Color.LTGRAY;
			}
			ds.setColor(Color.BLACK);
			ds.setUnderlineText(false);
		}
	}

	public void onDoneClick(View view) {
		Log.i("SelectedPart", Integer.toString(selectedPart));
		if (selectedPart == -1) {
			Toast.makeText(getApplicationContext(),
					getResources().getString(R.string.select_part),
					Toast.LENGTH_SHORT).show();
			return;
		}
		boolean clicked_right = BackendControllerImpl.getInstance().partClicked(
				selectedPart);
		int result = PhishResult.Phish_Detected.getValue();
		if (!clicked_right) {
			result = ResultActivity.RESULT_GUESSED;
		}
		Intent levelIntent = new Intent(this, ResultActivity.class);
		levelIntent.putExtra(Constants.EXTRA_RESULT, result);
		levelIntent.putExtra(Constants.EXTRA_LEVEL, this.level);
		levelIntent.putExtra(Constants.EXTRA_SITE_TYPE, BackendControllerImpl.getInstance().getUrl().getSiteType().getValue());
		levelIntent.putExtra(Constants.EXTRA_ATTACK_TYPE, BackendControllerImpl.getInstance().getUrl().getAttackType().getValue());
		startActivity(levelIntent);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		// Respond to the action bar's Up/Home button
		case android.R.id.home:
			levelCanceldWarning();
			return true;
		case R.id.restart_level:
			levelRestartWarning();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu items for use in the action bar
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.urltask_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}
	/**
	 * Going back not possible, only cancel level
	 */
	@Override
	public void onBackPressed() {
		levelCanceldWarning();
	}
}
