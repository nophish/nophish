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

import java.util.List;

import de.tudarmstadt.informatik.secuso.phishedu2.backend.BackendControllerImpl;
import de.tudarmstadt.informatik.secuso.phishedu2.backend.PhishAttackType;
import de.tudarmstadt.informatik.secuso.phishedu2.backend.PhishURL;

/**
 * This Attack replaces the whole URL by a replace from phishtank
 * @author Clemens Bergmann <cbergmann@schuhklassert.de>
 *
 */
public class MisleadingAttack extends AbstractAttack {
	int attackpos=0;
	int attack_domain=-1;
	private static final String[] DOMAIN_ADDITIONS={
		"login",
		"-anmelden",
		"-secure",
		"-sicher",
		"-accounts",
		"-konto",
		"-verify",
		"-verification",
		"verifizierung",
		"-signin",
		"-com",
		"-de",
		"-home",
		"update",
		"-registration",
		"registrierung",
		"-settings",
		"-einstellungen",
		"-service",
		"support",
		"-hilfe",
		"shopping",
	};
	/**
	 * This constructor is required because of the implementation in {@link BackendControllerImpl#getNextUrl()}
	 * @param object This Parmeter is discarded. It is replaced by a PhishTank URL
	 */
	public MisleadingAttack(PhishURL object) {
		super(object);
		attack_domain=BackendControllerImpl.getInstance().getRandom().nextInt(DOMAIN_ADDITIONS.length);
	}

	@Override
	public PhishAttackType getAttackType() {
		return PhishAttackType.Misleading;
	}
	
	@Override
	public String[] getParts() {
		String[] parts = super.getParts().clone();
		String domain = parts[3];
		int period = domain.lastIndexOf(".");
		String before=domain.substring(0,period);
		String after=domain.substring(period);
		domain=before+DOMAIN_ADDITIONS[attack_domain]+after;
		parts[3]=domain;
		return parts;
	}
	
	@Override
	public List<Integer> getAttackParts() {
		List<Integer> result = super.getAttackParts();
		result.add(3);
		return result;
	}
}
