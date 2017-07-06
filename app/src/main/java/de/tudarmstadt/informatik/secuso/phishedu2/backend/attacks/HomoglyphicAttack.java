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
import java.util.Random;

import de.tudarmstadt.informatik.secuso.phishedu2.backend.BackendControllerImpl;
import de.tudarmstadt.informatik.secuso.phishedu2.backend.PhishAttackType;
import de.tudarmstadt.informatik.secuso.phishedu2.backend.PhishURL;

/**
 * This Attack replaces the whole URL by a replace from phishtank
 * @author Clemens Bergmann <cbergmann@schuhklassert.de>
 *
 */
public class HomoglyphicAttack extends AbstractAttack {
	/**
	 * The replacements.
	 * The form is {match,replace}
	 */
	public static final String[][] REPLACEMENTS={
			{"l","1"},
			{"1", "l"},
			//These letters are indistinguishable in the default font used on the testing devices.
			//Therefore it might frustrate the user because he is not able to identify a phish even 
			//if he tries hard.
			//{"l","I"}, 
			//{"I", "l"},
			{"i","1"},
			{"1", "i"},
			{"o","0"},
			{"0","o"},
			{"y","v"},
			{"v","y"},
			{"j","i"},
			{"i", "j"},
			{"a","o"},
			{"o", "a"},
			{"w","vv"},
			{"vv", "w"},
			{"d","cl"},
			{"cl","d"}
	};
	
	int attack_replacement=-1;
	
	/**
	 * To build an attack we need a url to decorate
	 * @param object the decorated URL
	 */
	public HomoglyphicAttack(PhishURL object) {
		super(object);
		String domain = getsecondleveldomain();

		Random random = BackendControllerImpl.getInstance().getRandom();
		List<Integer> indexes = new ArrayList<Integer>();
		for (int i = 0; i < REPLACEMENTS.length; i++) {indexes.add(i);}
		
		while(indexes.size()>0){
			int i=indexes.remove(random.nextInt(indexes.size()));
			String[] replacement = REPLACEMENTS[i];
			int location = domain.indexOf(replacement[0]);
			if(location>=0){
				attack_replacement=i;
				break;
			}
		}
		
	}

	private String getsecondleveldomain(){
		String domain = getParts()[3];
		return domain.substring(0, domain.lastIndexOf('.'));
	}
	
	@Override
	public PhishAttackType getAttackType() {
		return PhishAttackType.Homoglyphic;
	}
	
	@Override
	public String[] getParts() {
		String[] parts = super.getParts().clone();
		if(attack_replacement==-1){
			return parts;
		}
		String domain = parts[3];
		String new_domain=domain.replaceFirst(REPLACEMENTS[attack_replacement][0], REPLACEMENTS[attack_replacement][1]);
		parts[3]=new_domain;
		return parts;
	}
	
	@Override
	public List<Integer> getAttackParts() {
		List<Integer> result = super.getAttackParts();
		result.add(3);
		return result;
	}
}
