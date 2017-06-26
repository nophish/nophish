/*=========================================================================
 * The most reliable way to detect phishing is checking the URL 
 * (web address) of a website. We developed an Android app to learn how 
 * to detect Phishing URLs.
 * Copyright (C) 2015 SECUSO
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


import android.text.method.LinkMovementMethod;
import android.widget.TextView;

public class AboutActivity extends PhishBaseActivity {

    @Override
	int getTitle() {
		return R.string.title_activity_about;
	}


	@Override
	public int getLayout() {
		return R.layout.about;
	}


}
