package de.tudarmstadt.informatik.secuso.phishedu;

import android.content.Intent;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.text.SpannableStringBuilder;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import de.tudarmstadt.informatik.secuso.phishedu.backend.BackendController;

/**
 * 
 * @author Gamze Canova This class covers the awareness part of the app This
 *         Activity should only be invoked if the user has not done this part
 *         before
 */
public class LevelIntroActivity extends SwipeActivity {
	private ActionBar ab;

	SpannableStringBuilder strBuilder = new SpannableStringBuilder();
	int wordStart, wordEnd;
	protected static String[][] exampleUrlPartId = {
			// level 3
			// TODO: auslagern in strings.xml
			{ "http://", "google.com.", "phishers-seite.de", "/search" },
			{ "http://", "", "192.168.160.02", "/secure-login" },
			{ "https://", "secure-login.mail.google.com.", "hsezis.de",
					"/update-account", "http://",
					"secure-login.mail.google.com.", "badcat.com", "/login" },
			{ "https://", "microsoft.com.", "security-update.de", "/update" }

	};

	protected static int[][] levelLayoutIds = {
			{ R.layout.level_00_intro_00, R.layout.level_00_intro_01 },
			{ R.layout.level_01_splash, R.layout.level_01_intro_00,
					R.layout.level_01_intro_01, R.layout.level_01_intro_02,
					R.layout.level_01_intro_03 },
			{ R.layout.level_02_splash, R.layout.level_02_intro_00,
					R.layout.level_02_intro_01, R.layout.level_02_intro_02,
					R.layout.level_02_intro_03, R.layout.level_02_intro_04,
					R.layout.level_02_intro_05, R.layout.level_02_intro_06,
					R.layout.level_02_intro_07, R.layout.level_02_intro_08,
					R.layout.level_02_intro_09, R.layout.level_02_intro_10, },

			{ R.layout.level_03_splash, R.layout.level_03_intro_00,
					R.layout.level_03_intro_01, R.layout.level_03_intro_02,
					R.layout.level_03_intro_03 },
			{ R.layout.level_04_splash, R.layout.level_04_intro_00,
					R.layout.level_04_intro_01, R.layout.level_04_intro_02,
					R.layout.level_04_intro_03 },
			{ R.layout.level_05_splash, R.layout.level_05_intro_00,
					R.layout.level_05_intro_01, R.layout.level_05_intro_02 },
			{ R.layout.level_06_splash, R.layout.level_06_intro_00,
					R.layout.level_06_intro_01, R.layout.level_06_intro_02 },
			{ R.layout.level_07_splash, R.layout.level_07_intro_00,
					R.layout.level_07_intro_01, R.layout.level_07_intro_02,
					R.layout.level_07_intro_03, R.layout.level_07_intro_04,
					R.layout.level_07_intro_05, R.layout.level_07_intro_06 },
			{ R.layout.level_08_splash, R.layout.level_08_intro_00,
					R.layout.level_08_intro_01, R.layout.level_08_intro_02 },
			{ R.layout.level_09_splash, R.layout.level_09_intro_00,
					R.layout.level_09_intro_01, R.layout.level_09_intro_02,
					R.layout.level_09_intro_03 },
			{ R.layout.level_10_splash, R.layout.level_10_intro_00,
					R.layout.level_10_intro_01, R.layout.level_10_intro_02,
					R.layout.level_10_intro_03, R.layout.level_10_intro_04,
					R.layout.level_10_intro_05, R.layout.level_10_intro_06,
					R.layout.level_10_intro_07, R.layout.level_10_intro_08,
					R.layout.level_10_intro_09, } };

	public int real_level = 0;
	public int index_level = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		this.real_level = getIntent().getIntExtra(Constants.EXTRA_LEVEL, 0);
		this.index_level = Math.min(this.real_level, levelLayoutIds.length - 1);

		super.onCreate(savedInstanceState);
	}

	public void onStartClick(View view) {
		this.onStartClick();
	}

	protected void onStartClick() {
		Class next_activity = URLTaskActivity.class;
		if (this.real_level == 0) {
			next_activity = AwarenessActivity.class;
		} else if (this.real_level == 1) {
			next_activity = FindAddressBarActivity.class;
		}
		Intent levelIntent = new Intent(this, next_activity);
		levelIntent.putExtra(Constants.EXTRA_LEVEL, this.real_level);
		startActivity(levelIntent);
	}

	@Override
	protected String startButtonText() {
		return "Start Level";
	}

	@Override
	protected int getPageCount() {
		return this.levelLayoutIds[this.index_level].length;
	}

	@Override
	protected View getPage(int page, LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {

		View view = inflater.inflate(
				this.levelLayoutIds[this.index_level][page], container, false);

		// when example screen is showns
		if (view.findViewById(R.id.recognize_attack) != null) {
			setExampleSpans(view);
		}
		setTitles();

		return view;
	}

	private void setExampleSpans(View view) {
		buildColoredSpan(view);
	}

	private void setTitles() {
		ab = getSupportActionBar();
		String title = getString(Constants.levelTitlesIds[this.real_level]);
		String subtitle = getString(Constants.levelSubtitlesIds[this.real_level]);

		if (!title.equals(subtitle)) {
			// if subtitle and title are different, subtitle is set
			ab.setSubtitle(subtitle);
		}
		// title is set in anyway
		ab.setTitle(title);

		// if awareness is shown - no icon change
		if (this.real_level > 0) {
			ab.setIcon(getResources().getDrawable(R.drawable.emblem_library));
		}

	}

	/**
	 * User is getting back to the main menu from the introductionary texts.
	 */
	@Override
	public void onBackPressed() {
		NavUtils.navigateUpFromSameTask(this);
		return;
	}

	private void buildColoredSpan(View view) {

		int exampleIndex = BackendController.getInstance().getLevel() - 3;
		String[] url = exampleUrlPartId[exampleIndex];
		setSpans(url, view);

	}

	private void setSpans(String[] url, View view) {
		int exampleIndex = BackendController.getInstance().getLevel() - 3;

		// at start clear string builder
		strBuilder.clear();
		for (int i = 0; i < url.length; i++) {

			int spanIndex = (i % 4);
			if (spanIndex == 0) {
				strBuilder.clear();
				strBuilder.clearSpans();
			}

			String part = url[i];
			// 0 at the beginning
			wordStart = strBuilder.length();
			wordEnd = wordStart + part.length();
			strBuilder.append(part);

			if (spanIndex == 1 && exampleIndex == 0) {
				// we are in level 3, subdomain needs to be marked
				// make background light red
				final BackgroundColorSpan bgc = new BackgroundColorSpan(
						Color.rgb(255, 178, 170));
				strBuilder.setSpan(bgc, wordStart, wordEnd, 0);
			} else if (spanIndex == 2) {
				// make background red
				final BackgroundColorSpan bgc = new BackgroundColorSpan(
						Color.rgb(255, 102, 102));
				strBuilder.setSpan(bgc, wordStart, wordEnd, 0);
			} else if (spanIndex == 0 || spanIndex == 3) {
				// make foregroundcolor grey
				final ForegroundColorSpan fgc = new ForegroundColorSpan(
						Color.rgb(204, 204, 204));
				strBuilder.setSpan(fgc, wordStart, wordEnd, 0);

			} else if (spanIndex == 1) {
				final ForegroundColorSpan fgc = new ForegroundColorSpan(
						Color.BLACK);
				strBuilder.setSpan(fgc, wordStart, wordEnd, 0);
			}

			// example 1 needs to be set
			if (i == 3) {
				TextView tv1 = (TextView) view.findViewById(R.id.example_01);
				if (tv1 != null) {
					tv1.setText(strBuilder);
				}
			}
			if (i == 7) {
				// example 2 needs to be set
				TextView tv2 = (TextView) view.findViewById(R.id.example_02);
				if (tv2 != null) {
					tv2.setText(strBuilder);
				}
			}
		}
	}

	private void setLevel3Spans(String[] url, View view) {
		for (int i = 0; i < url.length; i++) {

			String part = url[i];
			// 0 at the beginning
			wordStart = strBuilder.length();
			wordEnd = wordStart + part.length();
			strBuilder.append(part);

			if (i == 1) {
				// make background light red
				final BackgroundColorSpan bgc = new BackgroundColorSpan(
						Color.rgb(255, 178, 170));
				strBuilder.setSpan(bgc, wordStart, wordEnd, 0);
			} else if (i == 2) {
				// make background red
				final BackgroundColorSpan bgc = new BackgroundColorSpan(
						Color.rgb(255, 102, 102));
				strBuilder.setSpan(bgc, wordStart, wordEnd, 0);
			} else {
				// make foregroundcolor grey
				final ForegroundColorSpan fgc = new ForegroundColorSpan(
						Color.rgb(204, 204, 204));
				strBuilder.setSpan(fgc, wordStart, wordEnd, 0);

			}
		}

		TextView tv1 = (TextView) view.findViewById(R.id.example_01);
		if (tv1 != null) {
			tv1.setText(strBuilder);
		}
	}
}
