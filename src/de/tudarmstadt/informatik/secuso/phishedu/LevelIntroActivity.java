package de.tudarmstadt.informatik.secuso.phishedu;

import java.util.ArrayList;
import java.util.Arrays;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
	private boolean show_repeat=true;
	SpannableStringBuilder strBuilder = new SpannableStringBuilder();

	public void setShowRepeat(boolean showRepeat){
		this.show_repeat=showRepeat;
		updateUI();
	}
	
	protected static String[][] exampleReminderUrlPartId = {
			{ "http://", "", "95.130.22.98", "/secure-login" },
			{ "https://", "", "security-update.de", "/update" },
			{ "https://", "mail.google.com.", "secure-login.de",
			"/update-account" },
			{ "http://", "account-settings.de/", "", "facebook.com", "/settings" },
			{ "http://", "www.", "facebook-login.com", "/" },
			{ "https://", "www.", "mircosoft.com", "/login" },
			
			{ "https://", "www.", "vvetter.com", "/wetter_aktuell/?code=EUDE" }
			 };
	protected static String[][] exampleUrlPartId = {
			// level 3
			{ "http://", "", "95.130.22.98", "/secure-login" },
			//{ "http://", "google.com.", "phisher-seite.de",
			//"/search/online+banking+postbank" },
			{ "https://", "", "hsfskzis.de",
					"/update-account", "http://", "", "secure-login.com", "/mail/online/login" },		
			{"https://", "mail.google.com.", "badcat.de", "/",
			 "http://", "microsoft.com.", "secure-upate.com", "/windows7"},
			{  "http://", "95.130.22.98/", "search/online+banking+postbank/", "https://www.google.de", "",
			   "http://", "account-settings.de/", "", "facebook.com", "/settings"},			 
			{ "http://", "www.", "facebook-login.com", "/", "http://", "www.",
				"apple-support.com", "/ipodnano/troubleshooting",
				"http://", "www.my.", "ebay-verify.de",
				"/account-verification/user" },
			{"https://", "www.",
					"mircosoft.com", "/login", "http://", "www.",
					"twitetr.com", "/en-us/default.aspx"},	
		//	{ "https://", "microsoft.com.", "security-update.de", "/update" },
			{ "https://", "www.", "vvetter.com", "/wetter_aktuell/?code=EUDE",
					"http://", "www.", "googie.de", "/services/?fg=1",
					"http://", "www.", "paypa1.com",
					"/de/webapps/mpp/privatkunden" },
			{ "https://", "www.", "deutsche-bank.de", "/index.htm", "https://",
					"www.", "deutsche-bank.de", "/index.htm", "https://",
					"facebook.", "phisher.de", "/secure-login" },
			{ "https://", "www.", "commerzbank.de", "/", "https://", "www.",
					"commerzbanking.de",
					"/P-Portal1/XML/IFILPortal/pgf.html?Tab=3&ifil=coba_pk",
					"https://", "www.", "paypal.com", "/de", "https://",
					"www.", "paypal-viewpoints.com", "/DE-Kontakt" } };

	@Override
	int getTitle() {
		return BackendControllerImpl.getInstance().getLevelInfo().titleId;
	};

	@Override
	int getSubTitle() {
		return R.string.intro;
	}

	@Override
	public int[] getClickables() {
		return new int[] { R.id.level_01_intro_00_button_01,
				R.id.level_11_intro_07_text_01 };
	}

	@Override
	public void onClick(View view) {
		int id = view.getId();
		if (id == R.id.level_01_intro_00_button_01) {
			onStartClick();
		} else if (id == R.id.level_11_intro_07_text_01) {
			Intent browserIntent = new Intent(Intent.ACTION_VIEW);
			browserIntent
					.setData(Uri
							.parse("https://www.bsi-fuer-buerger.de/BSIFB/DE/SicherheitImNetz/EinkaufenImInternet/OnlineShoppingbeachten/shopping_was_beachten.html#doc1102038bodyText4"));
			startActivity(browserIntent);
		}
		super.onClick(view);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected void onStartClick() {
		Class next_activity = URLTaskActivity.class;
		if (this.getLevel() == 0) {
			next_activity = AwarenessActivity.class;
		} else if (this.getLevel() == 1) {
			next_activity = FindAddressBarActivity.class;
		} else if (this.getLevel() == BackendControllerImpl.getInstance()
				.getLevelCount() - 1) {
			next_activity = AppEndActivity.class;
		}
		switchToFragment(next_activity);
	}

	@Override
	protected String startButtonText() {
		if (this.getLevel() == BackendControllerImpl.getInstance()
				.getLevelCount() - 1) {
			return "Geschafft";
		}
		return "Starte Übung";
	}

	@Override
	protected int getPageCount() {
		NoPhishLevelInfo info = BackendControllerImpl.getInstance().getLevelInfo(getLevel());
		int pagecount = info.splashLayouts.length+info.introLayouts.length;
		if(show_repeat){
			pagecount+= info.repeatLayouts.length;
		}
		return pagecount;
	}

	@Override
	protected View getPage(int page, LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		
		NoPhishLevelInfo info = BackendControllerImpl.getInstance().getLevelInfo(getLevel());
		ArrayList<Integer> layouts=new ArrayList<Integer>();
		layouts.addAll(Arrays.asList(info.splashLayouts));
		if(this.show_repeat){
		  layouts.addAll(Arrays.asList(info.repeatLayouts));
		}
		layouts.addAll(Arrays.asList(info.introLayouts));

		View view = inflater.inflate(layouts.get(page), container, false);

		// when example screen is showns
		if ((view.findViewById(R.id.recognize_attack) != null)
				|| view.findViewById(R.id.reminder_examples) != null) {
			setExampleSpans(view);
		}
		return view;
	}

	@Override
	int getIcon() {
		if (getLevel() > 0) {
			return R.drawable.emblem_library;
		} else {
			return 0;
		}
	}

	private void setExampleSpans(View view) {
		buildColoredSpan(view);
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
//		if (level > 7) {
//			// two more urls displayed from there
//			 reminderIndex = reminderIndex + 2;
//		}

		for (int currentIndex = 0; currentIndex < reminderIndex; currentIndex++) {
			String[] currentUrl = exampleReminderUrlPartId[currentIndex];

			

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
				} //else if (currentIndex == 7) {
				//	setTextView(view, R.id.example_08);
				//}

				// This example has other spans
				// must stay here, will be overwritten otherwise
				if (level >= 7 && currentIndex == 3) {
					strBuilder.clear();
					strBuilder.clearSpans();
					setLevelHostInPathSpan(currentUrl, view);
					//return;
				}
			}
		}
	}

	private void setSpans(String[] url, View view) {

		// at start clear string builder
		strBuilder.clear();

		// total different span pattern
		int level = BackendControllerImpl.getInstance().getLevel();
		if (level == 6) {
			setLevelHostInPathSpan(url, view);
			return;
		}
		if (level == 10) {
			setSpanLevelHttps(url, view);
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
		int wordStart = strBuilder.length();
		int wordEnd = wordStart + part.length();
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

	private void setSpanLevelHttps(String[] url, View view) {

		for (int i = 0; i < url.length; i++) {

			int spanIndex = (i % 4);
			if (spanIndex == 0) {
				strBuilder.clear();
				strBuilder.clearSpans();
			}

			String part = url[i];
			// 0 at the beginning
			int wordStart = strBuilder.length();
			int wordEnd = wordStart + part.length();
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

	private void setLevelHostInPathSpan(String[] url, View view) {

		for (int i = 0; i < url.length; i++) {

			int spanIndex = (i % 5);
			if (spanIndex == 0) {
				strBuilder.clear();
				strBuilder.clearSpans();
			}
			
			String part = url[i];
			// 0 at the beginning
			int wordStart = strBuilder.length();
			int wordEnd = wordStart + part.length();
			strBuilder.append(part);

			if (spanIndex == 0) {

				// make foregroundcolor of http grey
				final ForegroundColorSpan fgc = new ForegroundColorSpan(
						getResources().getColor(R.color.grey));
				strBuilder.setSpan(fgc, wordStart, wordEnd, 0);

			} else if (spanIndex == 1) {

				// background of domain blue
				final BackgroundColorSpan bgc = new BackgroundColorSpan(
						getResources().getColor(R.color.domain));
				strBuilder.setSpan(bgc, wordStart, wordEnd, 0);

			} else if (spanIndex == 2 || spanIndex == 4) {
				final ForegroundColorSpan fgc = new ForegroundColorSpan(
						Color.BLACK);
				strBuilder.setSpan(fgc, wordStart, wordEnd, 0);
			} else if (spanIndex == 3) {
				// make path part blue
				final BackgroundColorSpan bgc = new BackgroundColorSpan(
						getResources().getColor(R.color.path));
				strBuilder.setSpan(bgc, wordStart, wordEnd, 0);
			}

			if (i == 4) {
				if (view.findViewById(R.id.recognize_attack) != null) {
					// we are in recognize attack
					setTextView(view, R.id.example_01);

				} else if(view.findViewById(R.id.reminder_examples) != null){
					// we are in reminder_examples
					setTextView(view, R.id.example_04);
				}
			} else if (i == 9) {
				setTextView(view, R.id.example_02);
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
