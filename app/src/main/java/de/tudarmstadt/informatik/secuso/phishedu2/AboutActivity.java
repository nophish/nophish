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
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

public class AboutActivity extends PhishBaseActivity implements OnClickListener {

    @Override
	int getTitle() {
		return R.string.title_activity_about;
	}
	
	@Override
	public int getLayout() {
		return R.layout.about;
	}
	
	@Override
    public void onClick(View view) {
        int id = view.getId();
		if (id == R.id.secuso_logo) {
			Intent browserIntent = new Intent(Intent.ACTION_VIEW);
			browserIntent.setData(Uri.parse("https://www.secuso.informatik.tu-darmstadt.de"));
			startActivity(browserIntent);
		}
    }

	@Override
	public int[] getClickables() {
		return new int[] {
				R.id.secuso_logo
		};
	}

}
