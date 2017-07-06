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

package de.tudarmstadt.informatik.secuso.phishedu2.backend.networkTasks;

import de.tudarmstadt.informatik.secuso.phishedu2.backend.PhishAttackType;
import de.tudarmstadt.informatik.secuso.phishedu2.backend.PhishURL;

/**
 * This interface is for the BackendController to get notified when the {@link GetUrlsTask} is finished
 * @author Clemens Bergmann <cbergmann@schuhklassert.de>
 *
 */
public interface UrlsLoadedListener {
	/**
	 * Returns the downloaded URLs 
	 * @param urls The downloaded urls
	 * @param type The type of urls
	 */
	public void urlsReturned(PhishURL[] urls, PhishAttackType type);
	/**
	 * Notifies the Listener for progress. This allows us to display a progressbar.
	 * @param percent What is the current progress.
	 */
	public void urlDownloadProgress(int percent);
}
