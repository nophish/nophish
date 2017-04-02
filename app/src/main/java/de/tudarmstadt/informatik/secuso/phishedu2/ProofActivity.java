/*=========================================================================
 * The most reliable way to detect phishing is checking the URL
 * (web address) of a website. We developed an Android app to learn how
 * to detect Phishing URLs.
 * Copyright (C) 2015 SecUSo
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *=========================================================================*/

package de.tudarmstadt.informatik.secuso.phishedu2;

import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import de.tudarmstadt.informatik.secuso.phishedu2.backend.BackendControllerImpl;

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

			ImageView image = (ImageView) v.findViewById(R.id.spacer);
			image.setVisibility(View.INVISIBLE);
		}

        if (getLevel() == 10) {
            TextView text = (TextView) v.findViewById(R.id.phish_proof_text);
            text.setText(R.string.phish_proof_09);
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
		BackendControllerImpl.getInstance().partClicked(selectedPart);
	}

	@Override
	protected String startButtonText() {
		return "Überprüfen";
	}
}
