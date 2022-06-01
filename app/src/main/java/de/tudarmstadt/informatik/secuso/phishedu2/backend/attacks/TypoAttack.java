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
import java.util.Arrays;
import java.util.List;

import de.tudarmstadt.informatik.secuso.phishedu2.backend.BackendControllerImpl;
import de.tudarmstadt.informatik.secuso.phishedu2.backend.PhishAttackType;
import de.tudarmstadt.informatik.secuso.phishedu2.backend.PhishURL;

/**
 * This Attack replaces the whole URL by a replace from phishtank
 * @author Clemens Bergmann <cbergmann@schuhklassert.de>
 *
 */
public class TypoAttack extends AbstractAttack {
	
	/**
	 * This constructor is required because of the implementation in {@link BackendControllerImpl#getNextUrl()}
	 * @param object This Parmeter is discarded. It is replaced by a PhishTank URL
	 */
	public TypoAttack(PhishURL object) {
		super(object);
		attack_type=BackendControllerImpl.getInstance().getRandom().nextInt(2);
		String domain=super.getParts()[3];
		int last_period_pos = domain.lastIndexOf(".");
		do{
			attack_pos=BackendControllerImpl.getInstance().getRandom().nextInt(domain.length()-1);
		}while(domain.charAt(attack_pos)==domain.charAt(attack_pos+1) || attack_pos+1 >= last_period_pos);
	}

	@Override
	public PhishAttackType getAttackType() {
		return PhishAttackType.Typo;
	}
	
	private int attack_pos=-1;
	private int attack_type=-1;
	
	@Override
	public String[] getParts() {
		String[] parts = super.getParts();
		ArrayList<String> adder = new ArrayList<String>(Arrays.asList(parts));
		String domain = adder.remove(3);
		switch (attack_type) {
		case 0:
			//swap
			String find = domain.substring(attack_pos, attack_pos+2);
			String replace = find.charAt(1)+""+find.charAt(0);
			domain=domain.replace(find, replace);
			break;
		default:
			//copy char
			String first = domain.substring(0,attack_pos);
			String last = domain.substring(attack_pos);
			domain=first+last.charAt(0)+last;
			break;
		}
		
		adder.add(3, domain.toString());
		return adder.toArray(new String[0]);
	}
	
	@Override
	public List<Integer> getAttackParts() {
		List<Integer> result = super.getAttackParts();
		result.add(3);
		return result;
	}
}
