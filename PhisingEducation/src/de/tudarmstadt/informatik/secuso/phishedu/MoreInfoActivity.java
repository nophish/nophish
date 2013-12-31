package de.tudarmstadt.informatik.secuso.phishedu;

import java.util.LinkedHashMap;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MoreInfoActivity extends PhishBaseActivity {

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
		entrysForMoreInfo.put(getString(R.string.info_security_comic),new MoreInfo() {public void pressed() {openLink(getString(R.string.url_securitycomic));}});
		entrysForMoreInfo.put(getString(R.string.info_apwg), new MoreInfo(){public void pressed(){openLink(getString(R.string.url_apwg));}});
		entrysForMoreInfo.put(getString(R.string.info_how_phishing_works), new MoreInfo(){public void pressed(){openLink(getString(R.string.url_how_phishing_works));}});
		
		adapterForMoreInfo = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_list_item_1, entrysForMoreInfo.keySet()
						.toArray(new String[0]));

		final ListView listview = (ListView) v.findViewById(R.id.more_info_listview);
		listview.setAdapter(this.adapterForMoreInfo);

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

}
