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

import java.util.ArrayList;
import java.util.List;

import de.tudarmstadt.informatik.secuso.phishedu2.backend.BackendControllerImpl;
import de.tudarmstadt.informatik.secuso.phishedu2.backend.PhishAttackType;
import de.tudarmstadt.informatik.secuso.phishedu2.backend.PhishURL;

/**
 * This Attack replaces the whole URL by a replace from phishtank
 * @author Clemens Bergmann <cbergmann@schuhklassert.de>
 *
 */
public class PhishTankURLAttack extends AbstractAttack {
	
	/**
	 * This constructor is required because of the implementation in {@link BackendControllerImpl#getNextUrl()}
	 * @param object This Parmeter is discarded. It is replaced by a PhishTank URL
	 */
	public PhishTankURLAttack(PhishURL object) {
		super(object);
		this.object=BackendControllerImpl.getInstance().getPhishURL(PhishAttackType.AnyPhish);
	}

	@Override
	public PhishAttackType getAttackType() {
		return PhishAttackType.AnyPhish;
	}	
	
	/**
	 * As we don't know the structure of the url from phishTank. All parts might be phishy.
	 */
	@Override
	public List<Integer> getAttackParts() {
		List<Integer> result = new ArrayList<Integer>();
		for(int i=0; i<getParts().length; i++){
			result.add(i);
		}
		return result;
	}
}
