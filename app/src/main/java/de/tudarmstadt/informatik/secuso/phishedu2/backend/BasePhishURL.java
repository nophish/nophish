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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * this is for internally holding the phishing urls
 * @author Clemens Bergmann <cbergmann@schuhklassert.de>
 *
 */
public class BasePhishURL implements PhishURL{
	private String[] parts = new String[0];
	private PhishSiteType siteType = PhishSiteType.AnyPhish;
	private PhishAttackType attackType = PhishAttackType.NoPhish;
	private String providerName = "";
	protected int[] correctparts = new int[0];
	/**
	 * This stores the points the user gets for his detection.
	 * 0: he got it right
	 * 1: he did not get it right
	 */
	private int[] points = {DEFAULT_CORRECT_POINTS, DEFAULT_INCORRECT_POINTS};
	
	public String[] getParts(){return parts;}
	public PhishSiteType getSiteType(){return this.siteType;}
	public PhishAttackType getAttackType(){return this.attackType;}
	public String getProviderName() {return this.providerName;}
		
	public final boolean validate(){
		return this.points[0] > 0; //if we get no points on success we can not progress.
	}
	
	public int getPoints(PhishResult result){
		if(result == PhishResult.TimedOut){
			return DEFAULT_TIMEOUT_POINTS;
		}
		boolean correct = (result == PhishResult.NoPhish_Detected || result==PhishResult.Phish_Detected);
		return points[correct ? 0 : 1];
	}
	
	public PhishResult getResult(boolean acceptance){
		PhishResult result;
		PhishAttackType attacktype = getAttackType();
		if(attacktype == PhishAttackType.NoPhish && acceptance){
			result =  PhishResult.NoPhish_Detected;
		}else if(attacktype == PhishAttackType.NoPhish && !acceptance){
			result =  PhishResult.NoPhish_NotDetected;
		}else if(attacktype != PhishAttackType.NoPhish && acceptance){
			result =  PhishResult.Phish_NotDetected;
		}else if(attacktype != PhishAttackType.NoPhish && !acceptance){
			result =  PhishResult.Phish_Detected;
		}else {
			throw new IllegalStateException("Something went horrably wrong! We should not be here.");
		}
		return result;
	}
	
	@Override
	public List<Integer> getAttackParts() {
		return new ArrayList<Integer>();
	}
	
	@Override
	public PhishURL clone(){
		BasePhishURL clone = new BasePhishURL();
		clone.attackType=this.attackType;
		clone.parts=this.parts.clone();
		clone.points=this.points.clone();
		clone.siteType=this.siteType;
		clone.correctparts=this.correctparts.clone();
		clone.providerName=this.providerName;
		clone.validateProviderName();
		return clone;
	}
	
	@Override
	public int getDomainPart() {
		return 3;
	}
	
	public void validateProviderName() {
		if(this.providerName.length()==0){
			this.providerName = this.getParts()[3].split("\\.")[0];
			this.providerName = this.providerName.substring(0,1).toUpperCase(Locale.GERMANY) + this.providerName.substring(1);
		}
	}
	
	@Override
	public String toString() {
		return Arrays.toString(this.parts);
	}
	
}