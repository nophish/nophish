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

package de.tudarmstadt.informatik.secuso.phishedu2.backend;

import java.util.List;

/**
 * This is the Interface for all Phishing URLs.
 * This is needed because I want to implement the Attacks as decorators.
 * @author Clemens Bergmann <cbergmann@schuhklassert.de>
 *
 */
public interface PhishURL{
	
	/**
	 * The default points the user gets for correctly identifying URLS
	 */
	public static final int DEFAULT_CORRECT_POINTS = 15;
	/**
	 * The default points the user gets for falsly identifiying URLS
	 */
	public static final int DEFAULT_INCORRECT_POINTS = -10;
	/**
	 * The default points the user gets for timing out
	 */
	public static final int DEFAULT_TIMEOUT_POINTS = 0;
	
	/**
	 * Return the parts of the URL.
	 * When concardinatend they build up the whole url.
	 * The first 4 Parts are always defined as follows:
	 * 0: The scheme (eg. "https:")
	 * 1: "//"
	 * 2: <subdomain(s) string> (might be the empty string)
	 * 3: domain
	 * 4: "/"
	 * 5-: path
	 * 
	 * @return String parts of a url.
	 */
	public String[] getParts();
	/**
	 * Return what type of Site the url is supposed to link to.
	 * @return The linked Site type
	 */
	public PhishSiteType getSiteType();
	/**
	 * What type of attack was applyed to the URL
	 * @return The type of attack was applyed
	 */
	public PhishAttackType getAttackType();
	
	/**
	 * Get the points resulting in his selection
	 * @param result the result returned from {@link #getResult(boolean)} 
	 * @return the points the user gets for his selection
	 */
	public int getPoints(PhishResult result);
	
	/**
	 * Get the result if the user selected or did not select the URL
	 * @param acceptance true of the user clicked the checkmark. Otherwise false
	 * @return Result depending on the acceptance
	 */
	public PhishResult getResult(boolean acceptance);
	
	/**
	 * As this URL is serialized we implement a way of checking if the deserialized version is correct in respect of our current data model.
	 * @return true if this is a valid URL, false otherwise;
	 */
	public boolean validate();
	
	/**
	 * Get the parts of this URL that is part of an Attack.
	 * @return The attacked parts
	 */
	public List<Integer> getAttackParts();
	
	/**
	 * Which part is the domain.
	 * This might change if the attack prefixes something to the domain.
	 * @return The index of the domain in the {@link #getParts()} Array.
	 */
	public int getDomainPart();
	
	/**
	 * Clone the current phishURL object deeply
	 * @return deep clone
	 */
	public PhishURL clone();
	
	/**
	 * get the name of the provider
	 * @return Provider name
	 */
	public String getProviderName();
}