package de.tudarmstadt.informatik.secuso.phishedu;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.text.SpannableStringBuilder;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import de.tudarmstadt.informatik.secuso.phishedu.backend.BackendControllerImpl;
import de.tudarmstadt.informatik.secuso.phishedu.backend.NoPhishLevelInfo;

/**
 * 
 * @author Gamze Canova This class covers the awareness part of the app This
 *         Activity should only be invoked if the user has not done this part
 *         before
 */
public class LevelIntroActivity extends SwipeActivity {
	SpannableStringBuilder strBuilder = new SpannableStringBuilder();
	int wordStart, wordEnd;

	protected static String[][] exampleReminderUrlPartId = {
		{ "http://", "google.com.", "phishers-seite.de",
		"/search/online+banking+postbank" },
		{ "http://", "", "192.168.160.02", "/secure-login" },
		{ "https://", "secure-login.mail.google.com.", "hsezis.de",
		"/update-account" },
		{ "https://", "microsoft.com.", "security-update.de", "/update" },
		{ "http://", "www.", "facebook-login.com", "/" },
		{ "https://", "www.", "fracebook.com", "/login" },
		{ "http://", "www.", "mircosoft.com", "/en-us/default.aspx" },
		{ "https://", "www.", "vvetter.com", "/wetter_aktuell/?code=EUDE" },
		{ "http://", "phisher.de", "/mail.", "google.com", "/login" } };
	protected static String[][] exampleUrlPartId = {
		// level 3
		// TODO: auslagern in strings.xml
		{ "http://", "google.com.", "phishers-seite.de",
		"/search/online+banking+postbank" },
		{ "http://", "", "192.168.160.02", "/secure-login" },
		{ "https://", "secure-login.mail.google.com.", "hsezis.de",
			"/update-account", "http://",
			"secure-login.mail.google.com.", "badcat.com", "/login" },
			{ "https://", "microsoft.com.", "security-update.de", "/update" },
			{ "http://", "www.", "facebook-login.com", "/", "http://", "www.",
				"apple-support.com", "/ipodnano/troubleshooting",
				"http://", "www.my.", "ebay-verify.de",
				"/account-verification/user", "https://", "www.",
				"fracebook.com", "/login", "http://", "www.",
				"mircosoft.com", "/en-us/default.aspx" },
				{ "https://", "www.", "vvetter.com", "/wetter_aktuell/?code=EUDE",
					"http://", "www.", "googie.de", "/services/?fg=1",
					"http://", "www.", "paypa1.com",
				"/de/webapps/mpp/privatkunden" },
				{ "http://", "phisher.de", "/mail.", "google.com", "/login" },
				{ "https://", "www.", "deutsche-bank.de", "/index.htm", "https://",
					"www.", "deutsche-bank.de", "/index.htm", "https://",
					"facebook.", "phisher.de", "/secure-login" },
					{ "https://", "www.", "commerzbank.de", "/", "https://", "www.",
						"commerzbanking.de",
						"/P-Portal1/XML/IFILPortal/pgf.html?Tab=3&ifil=coba_pk",
						"https://", "www.", "paypal.com", "/de", "https://",
						"www.", "paypal-viewpoints.com", "/DE-Kontakt" } };
	public int level = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		this.level = getIntent().getIntExtra(Constants.EXTRA_LEVEL, 0);
		super.onCreate(savedInstanceState);
	}
	
	public void onStartClick(View view) {
		this.onStartClick();
	}

	protected void onStartClick() {
		Class next_activity = URLTaskActivity.class;
		if (this.level == 0) {
			next_activity = AwarenessActivity.class;
		} else if (this.level == 1) {
			next_activity = FindAddressBarActivity.class;
		}else if(this.level == BackendControllerImpl.getInstance().getLevelCount()){
			next_activity = AppEndActivity.class;
		}
		Intent levelIntent = new Intent(this, next_activity);
		levelIntent.putExtra(Constants.EXTRA_LEVEL, this.level);
		startActivity(levelIntent);
	}

	@Override
	protected String startButtonText() {
		if (this.level == BackendControllerImpl.getInstance().getLevelCount()) {
			return "Fertig";
		}
		return "Starte "+getResources().getString(BackendControllerImpl.getInstance().getLevelInfo(this.level).titleId);
	}

	@Override
	protected int getPageCount() {
		return BackendControllerImpl.getInstance().getLevelInfo(level).introLayouts.length;
	}

	@Override
	protected View getPage(int page, LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {

		View view = inflater.inflate(BackendControllerImpl.getInstance().getLevelInfo(level).introLayouts[page], container, false);

		// when example screen is showns
		if ((view.findViewById(R.id.recognize_attack) != null)
				|| view.findViewById(R.id.reminder_examples) != null) {
			setExampleSpans(view);
		}
		setTitles();

		return view;
	}

	private void setExampleSpans(View view) {
		buildColoredSpan(view);
	}

	private void setTitles() {
		ActionBar ab = getSupportActionBar();
		NoPhishLevelInfo level_info = BackendControllerImpl.getInstance().getLevelInfo();
		String title = getString(level_info.titleId);
		String subtitle = getString(level_info.titleId);

		if (!title.equals(subtitle)) {
			// if subtitle and title are different, subtitle is set
			ab.setSubtitle(subtitle);
		}
		// title is set in anyway
		ab.setTitle(title);

		// if awareness is shown - no icon change
		if (level_info.levelId > 0) {
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
		int exampleIndex = BackendControllerImpl.getInstance().getLevel() - 3;
		String[] url = exampleUrlPartId[exampleIndex];
		if (view.findViewById(R.id.recognize_attack) != null) {
			setSpans(url, view);
		} else if ((BackendControllerImpl.getInstance().getLevel() > 3)
				&& (view.findViewById(R.id.reminder_examples) != null)) {
			setReminderSpans(view, exampleIndex);
		}
	}

	private void setReminderSpans(View view, int reminderIndex) {

		int level = BackendControllerImpl.getInstance().getLevel();
		if (level > 7) {
			// two more urls displayed from there
			reminderIndex = reminderIndex + 2;
		}

		for (int currentIndex = 0; currentIndex < reminderIndex; currentIndex++) {
			String[] currentUrl = exampleReminderUrlPartId[currentIndex];

			// Example has other spans
			if (level == 10 && currentIndex == 8) {
				strBuilder.clear();
				strBuilder.clearSpans();
				setLevel9Span(currentUrl, view);
				return;
			}

			for (int i = 0; i < currentUrl.length; i++) {

				int spanIndex = (i % 4);
				if (spanIndex == 0) {
					strBuilder.clear();
					strBuilder.clearSpans();
				}

				setSingleSpan(currentUrl, BackendControllerImpl.getInstance()
						.getLevel(), i, spanIndex);

				// example 1 needs to be set
				if (currentIndex == 0) {
					setTextView(view, R.id.example_01);
				} else if (currentIndex == 1) {
					// example 2 needs to be set
					setTextView(view, R.id.example_02);
				} else if (currentIndex == 2) {
					// example 3 needs to be set
					setTextView(view, R.id.example_03);
				} else if (currentIndex == 3) {
					setTextView(view, R.id.example_04);
				} else if (currentIndex == 4) {
					setTextView(view, R.id.example_05);
				} else if (currentIndex == 5) {
					setTextView(view, R.id.example_06);
				} else if (currentIndex == 6) {
					setTextView(view, R.id.example_07);
				} else if (currentIndex == 7) {
					setTextView(view, R.id.example_08);
				}

			}
		}
	}

	private void setSpans(String[] url, View view) {

		// at start clear string builder
		strBuilder.clear();

		// total different span pattern
		int level = BackendControllerImpl.getInstance().getLevel();
		if (level == 9) {
			setLevel9Span(url, view);
			return;
		}
		if (level == 10) {
			setLevel10Span(url, view);
			return;
		}

		for (int i = 0; i < url.length; i++) {

			int spanIndex = (i % 4);
			if (spanIndex == 0) {
				strBuilder.clear();
				strBuilder.clearSpans();
			}

			setSingleSpan(url, level, i, spanIndex);

			// example 1 needs to be set
			if (i == 3) {
				setTextView(view, R.id.example_01);
			}
			if (i == 7) {
				// example 2 needs to be set
				setTextView(view, R.id.example_02);
			}
			if (i == 11) {
				// example 3 needs to be set
				setTextView(view, R.id.example_03);
			}

			// no need to check in other levels
			if (BackendControllerImpl.getInstance().getLevel() == 7
					|| BackendControllerImpl.getInstance().getLevel() == 11) {
				if (i == 15) {
					setTextView(view, R.id.example_04);
				}
				if (i == 19) {
					setTextView(view, R.id.example_05);
				}

			}
		}
	}

	private void setSingleSpan(String[] url, int level, int i, int spanIndex) {
		String part = url[i];
		// 0 at the beginning
		wordStart = strBuilder.length();
		wordEnd = wordStart + part.length();
		strBuilder.append(part);

		if (spanIndex == 1 && level == 3) {
			// we are in level 3, subdomain needs to be marked
			// make background light red
			final BackgroundColorSpan bgc = new BackgroundColorSpan(
					getResources().getColor(R.color.subdomain));
			strBuilder.setSpan(bgc, wordStart, wordEnd, 0);
		} else if (spanIndex == 2) {
			// make background red
			final BackgroundColorSpan bgc = new BackgroundColorSpan(
					getResources().getColor(R.color.domain));
			strBuilder.setSpan(bgc, wordStart, wordEnd, 0);
		} else if ((spanIndex == 0 || spanIndex == 3) && level < 11) {
			// make foregroundcolor grey
			final ForegroundColorSpan fgc = new ForegroundColorSpan(
					getResources().getColor(R.color.grey));
			strBuilder.setSpan(fgc, wordStart, wordEnd, 0);

		} else /* if (spanIndex == 1) */{
			final ForegroundColorSpan fgc = new ForegroundColorSpan(Color.BLACK);
			strBuilder.setSpan(fgc, wordStart, wordEnd, 0);
		}
	}

	private void setLevel10Span(String[] url, View view) {

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

			if (spanIndex == 0) {
				// make background of http yellow
				final BackgroundColorSpan bgc = new BackgroundColorSpan(
						getResources().getColor(R.color.protocol));
				strBuilder.setSpan(bgc, wordStart, wordEnd, 0);

			} else if (spanIndex == 2) {
				// make domain red
				final BackgroundColorSpan bgc = new BackgroundColorSpan(
						getResources().getColor(R.color.domain));
				strBuilder.setSpan(bgc, wordStart, wordEnd, 0);
			} else {
				final ForegroundColorSpan fgc = new ForegroundColorSpan(
						Color.BLACK);
				strBuilder.setSpan(fgc, wordStart, wordEnd, 0);
			}

			// example 1 needs to be set
			if (i == 3) {
				setTextView(view, R.id.example_01);
			} else if (i == 7) {
				setTextView(view, R.id.example_02);
			} else if (i == 11) {
				setTextView(view, R.id.example_03);
			}
		}
	}

	private void setLevel9Span(String[] url, View view) {

		for (int i = 0; i < url.length; i++) {

			String part = url[i];
			// 0 at the beginning
			wordStart = strBuilder.length();
			wordEnd = wordStart + part.length();
			strBuilder.append(part);

			if (i == 1) {
				// background of domain red
				final BackgroundColorSpan bgc = new BackgroundColorSpan(
						getResources().getColor(R.color.domain));
				strBuilder.setSpan(bgc, wordStart, wordEnd, 0);
			} else if (i == 0) {
				// make foregroundcolor of http grey
				final ForegroundColorSpan fgc = new ForegroundColorSpan(
						getResources().getColor(R.color.grey));
				strBuilder.setSpan(fgc, wordStart, wordEnd, 0);

			} else if (i == 2 || i == 4) {
				final ForegroundColorSpan fgc = new ForegroundColorSpan(
						Color.BLACK);
				strBuilder.setSpan(fgc, wordStart, wordEnd, 0);
			} else if (i == 3) {
				// make path part blue
				final BackgroundColorSpan bgc = new BackgroundColorSpan(
						getResources().getColor(R.color.path));
				strBuilder.setSpan(bgc, wordStart, wordEnd, 0);
			}

			if (i == url.length - 1) {
				// example needs to be set
				if (view.findViewById(R.id.recognize_attack) != null) {
					// we are in recognize attack
					setTextView(view, R.id.example_01);

				} else {
					// we are in reminder_examples
					setTextView(view, R.id.example_09);
				}
			}
		}
	}

	private void setTextView(View view, int resourceId) {
		TextView tv = (TextView) view.findViewById(resourceId);
		if (tv != null) {
			tv.setText(strBuilder);
		}
	}
}
