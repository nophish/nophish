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

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.LinkedHashMap;

public class QuizActivity extends PhishBaseActivity {

	public interface MoreInfo {
		void pressed();
	}

	private ArrayAdapter<String> adapterForMoreInfo;
	private LinkedHashMap<String, MoreInfo> entrysForMoreInfo;

	@Override
	public View getLayout(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.more_info_list_view, container, false);
		
		entrysForMoreInfo = new LinkedHashMap<String, MoreInfo>();
        entrysForMoreInfo.put(getString(R.string.quiz_1),new MoreInfo() {public void pressed() {openLink(getString(R.string.url_quiz_1));}});
        entrysForMoreInfo.put(getString(R.string.quiz_2),new MoreInfo() {public void pressed() {openLink(getString(R.string.url_quiz_2));}});
        entrysForMoreInfo.put(getString(R.string.quiz_3),new MoreInfo() {public void pressed() {openLink(getString(R.string.url_quiz_3));}});
		
		adapterForMoreInfo = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_list_item_1, entrysForMoreInfo.keySet()
						.toArray(new String[0]));

		final ListView listview = (ListView) v.findViewById(R.id.more_info_listview);
		listview.setAdapter(this.adapterForMoreInfo);

        final TextView text = (TextView) v.findViewById(R.id.more_info_text);
        text.setText(R.string.quiz_text);

		listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, final View view,
					int position, long id) {
				entrysForMoreInfo.get(adapterForMoreInfo.getItem(position))
						.pressed();
			}

		});
		
		return v;
	}
	
	private void openLink(String url) {
		Intent browserIntent = new Intent(Intent.ACTION_VIEW);
		browserIntent.setData(Uri.parse(url));
		startActivity(browserIntent);
	}
	
	@Override
	int getTitle() {
		return R.string.button_quiz;
	}

}
