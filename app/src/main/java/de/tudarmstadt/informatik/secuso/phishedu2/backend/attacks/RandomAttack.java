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
import de.tudarmstadt.informatik.secuso.phishedu2.backend.BackendControllerImpl;
import de.tudarmstadt.informatik.secuso.phishedu2.backend.PhishAttackType;
import de.tudarmstadt.informatik.secuso.phishedu2.backend.PhishURL;

/**
 * This Attack is a Attack that does no attack.
 * This is for simplyfing the URL generation.
 * @author Clemens Bergmann <cbergmann@schuhklassert.de>
 *
 */
public class RandomAttack extends AbstractAttack {

	/**
	 * See {@link AbstractAttack#AbstractAttack(PhishURL)}
	 * @param object See {@link AbstractAttack#AbstractAttack(PhishURL)}
	 */
	public RandomAttack(PhishURL object) {
		//decorate the object with a random attack before calling the super constructor
		super(AbstractUrlDecorator.decorate(object, getRandomAttack()));
		
	}
	
	private static Class<? extends AbstractAttack> getRandomAttack(){
		PhishAttackType[] types = PhishAttackType.values();
		Class<? extends AbstractAttack> attack;
		do {
			attack=types[BackendControllerImpl.getInstance().getRandom().nextInt(types.length)].getAttackClass();
		} while (attack.equals(RandomAttack.class) || attack.equals(KeepAttack.class));
		return attack;
	}
	
	public PhishAttackType getAttackType() {
		return object.getAttackType();
	}
	
}
