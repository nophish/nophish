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
 * This is the abstract implementation for following Attacks.
 * @author Clemens Bergmann <cbergmann@schuhklassert.de>
 *
 */
public abstract class AbstractUrlDecorator implements PhishURL {
	protected PhishURL object;
	
	/**
	 * To build an attack we need a url to decorate
	 * @param object the decorated URL
	 */
	public AbstractUrlDecorator(PhishURL object){
		this.object=object;
	}

	@Override
	public String[] getParts() {
		return this.object.getParts();
	}

	@Override
	public PhishSiteType getSiteType() {
		return this.object.getSiteType();
	}

	@Override
	public int getPoints(PhishResult result) {
		return this.object.getPoints(result);
	}

	@Override
	public abstract PhishResult getResult(boolean acceptance);
	
	@Override
	public List<Integer> getAttackParts() {
		return this.object.getAttackParts();
	}
	
	/**
	 * We don't allw overwrite on the validate function
	 */
	@Override
	public final boolean validate() {
		return this.object.validate();
	}
	
	@Override
	public PhishURL clone(){
		return decorate(this.object.clone(),this.getClass());
	}
	
	/**
	 * Decorate a Phishing url with a given decorator.
	 * @param base_url The URL to decorate
	 * @param decorator The decorator
	 * @return the base_url wrapped in a new instance of the decorator
	 */
	public static PhishURL decorate(PhishURL base_url, Class<? extends AbstractUrlDecorator> decorator) {
		try {
			base_url=decorator.getConstructor(PhishURL.class).newInstance(base_url);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return base_url;
	}
	
	@Override
	public int getDomainPart() {
		return object.getDomainPart();
	}
	
	public String getProviderName(){
		return object.getProviderName();
	}
	
	@Override
	public String toString() {
		return this.getClass().getSimpleName()+"+"+this.object.toString();
	}
}
