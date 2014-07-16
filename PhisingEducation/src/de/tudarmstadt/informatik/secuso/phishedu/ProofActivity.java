package de.tudarmstadt.informatik.secuso.phishedu;

import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import de.tudarmstadt.informatik.secuso.phishedu.backend.BackendControllerImpl;
import de.tudarmstadt.informatik.secuso.phishedu.backend.MainActivity;
import de.tudarmstadt.informatik.secuso.phishedu.backend.PhishResult;

public class ProofActivity extends SwipeActivity {
	int selectedPart = -1;
	
	@Override
	public void onSwitchTo() {
		selectedPart=-1;
		super.onSwitchTo();
	}
	
	boolean enableRestartButton(){return true;};

	@Override
	int getTitle(){
		return BackendControllerImpl.getInstance().getLevelInfo().titleId;
	};
	@Override
	int getSubTitle(){
		return R.string.exercise;
	};
	@Override
	int getIcon(){
		return R.drawable.desktop;
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

	/**
	 * Going back not possible, only cancel level
	 */
	@Override
	public void onBackPressed() {
		levelCanceldWarning();
	}

	@Override
	protected int getPageCount() {
		return 1;
	}

	@Override
	protected View getPage(int page, LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.proof, container,false);

		if (getLevel() == 2) {
			TextView text = (TextView) v.findViewById(R.id.phish_proof_text);
			text.setText(R.string.level_02_task);

			ImageView image = (ImageView) v.findViewById(R.id.feedback_smiley);
			image.setVisibility(View.INVISIBLE);
		}

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
		TextView url = (TextView) v.findViewById(R.id.url);
		url.setText(builder);
		url.setMovementMethod(LinkMovementMethod.getInstance());
		url.setHighlightColor(Color.LTGRAY);
		url.setTextSize(25);

		return v;
	}

	@Override
	protected void onStartClick() {
		Log.i("SelectedPart", Integer.toString(selectedPart));
		if (selectedPart == -1) {
			Toast.makeText(getActivity().getApplicationContext(),
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
		Bundle args = new Bundle();
		args.putInt(Constants.ARG_RESULT, result);
		((MainActivity)getActivity()).switchToFragment(ResultActivity.class,args);
	}

	@Override
	protected String startButtonText() {
		return "Überprüfen";
	}
}
