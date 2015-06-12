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

package de.tudarmstadt.informatik.secuso.phishedu2.backend.attacks;

import de.tudarmstadt.informatik.secuso.phishedu2.backend.AbstractUrlDecorator;
import de.tudarmstadt.informatik.secuso.phishedu2.backend.PhishAttackType;
import de.tudarmstadt.informatik.secuso.phishedu2.backend.PhishResult;
import de.tudarmstadt.informatik.secuso.phishedu2.backend.PhishURL;

/**
 * This is the abstract implementation for following Attacks.
 * @author Clemens Bergmann <cbergmann@schuhklassert.de>
 *
 */
public abstract class AbstractAttack extends AbstractUrlDecorator {
	
	/**
	 * To build an attack we need a url to decorate
	 * @param object the decorated URL
	 */
	public AbstractAttack(PhishURL object){
		super(object);
		this.object=object;
	}
	
	@Override
	public abstract PhishAttackType getAttackType();
	
	@Override
	public PhishResult getResult(boolean acceptance) {
		if(acceptance){
			return PhishResult.Phish_NotDetected;
		}else{
			return PhishResult.Phish_Detected;
		}
	}
	
}
