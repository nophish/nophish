package de.tudarmstadt.informatik.secuso.phishedu;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import de.tudarmstadt.informatik.secuso.phishedu.backend.BackendController;
import de.tudarmstadt.informatik.secuso.phishedu.backend.PhishResult;

public class ProofActivity extends PhishBaseActivity {
	int selectedPart = -1;
	int level = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		setContentView(R.layout.proof);
		updateScore();

		this.level = getIntent().getIntExtra(Constants.EXTRA_LEVEL, 0);

		if (level == 2) {
			TextView text = (TextView) findViewById(R.id.phish_proof_text);
			text.setText(R.string.level_02_task);

			ImageView image = (ImageView) findViewById(R.id.phish_proof_icon);
			image.setVisibility(View.INVISIBLE);
		}

		setTitles();

		String[] urlparts = BackendController.getInstance().getUrl();
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
		url.setMovementMethod(LinkMovementMethod.getInstance());
		url.setText(builder);
		url.setHighlightColor(Color.LTGRAY);
	}

	private void setTitles() {

		ActionBar ab = getActionBar();
		if (level != 2) {
			ab.setTitle(R.string.correct);
			ab.setSubtitle(getString(R.string.phish));
		} else {
			ab.setTitle(Constants.levelTitlesIds[BackendController
					.getInstance().getLevel()]);
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
		}

		@Override
		public void updateDrawState(TextPaint ds) {
			super.updateDrawState(ds);
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
		boolean clicked_right = BackendController.getInstance().partClicked(
				selectedPart);
		int result = PhishResult.Phish_Detected.getValue();
		if (!clicked_right) {
			result = ResultActivity.RESULT_GUESSED;
		}
		Intent levelIntent = new Intent(this, ResultActivity.class);
		levelIntent.putExtra(Constants.EXTRA_RESULT, result);
		levelIntent.putExtra(Constants.EXTRA_LEVEL, this.level);
		levelIntent.putExtra(Constants.EXTRA_SITE_TYPE, BackendController
				.getInstance().getSiteType().getValue());
		levelIntent.putExtra(Constants.EXTRA_ATTACK_TYPE, BackendController
				.getInstance().getAttackType().getValue());
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
