package de.tudarmstadt.informatik.secuso.phishedu.backend.attacks;

import java.util.List;
import java.util.Random;

import de.tudarmstadt.informatik.secuso.phishedu.backend.BackendControllerImpl;
import de.tudarmstadt.informatik.secuso.phishedu.backend.PhishAttackType;
import de.tudarmstadt.informatik.secuso.phishedu.backend.PhishURL;

/**
 * This attack is baesed on using an IP address as hostname. This might confuse
 * the user so he can not be sure that it might not be the correct Host.
 * 
 * @author Clemens Bergmann <cbergmann@schuhklassert.de>
 * 
 */
public class IPAttackNoBrand extends AbstractAttack {

	/**
	 * See {@link AbstractAttack#AbstractAttack(PhishURL)}
	 * 
	 * @param object
	 *            See {@link AbstractAttack#AbstractAttack(PhishURL)}
	 */
	public IPAttackNoBrand(PhishURL object) {
		super(object);
		ip = getRandomIP();
	}

	@Override
	public String[] getParts() {
		String[] parts = this.object.getParts();
		parts[2] = ""; // when using IPs subdomains are not allowed.
		
		String secondLevelDomain = parts[3].split("\\.")[0]; //for later replacement
		parts[3] = ip;

		// replace with "" if path contains domain
		for (int i = 4; i < parts.length; i++) {
			String pathWithoutDomain = parts[i].toLowerCase().replaceAll(secondLevelDomain,
					"");
			parts[i] = pathWithoutDomain;
		}

		return parts;
	}

	private String ip = "";

	private String getRandomIP() {
		Random rand = BackendControllerImpl.getInstance().getRandom();
		String result = "";
		for (int i = 0; i < 4; i++) {
			result += rand.nextInt(255);
			if (i < 3) {
				result += ".";
			}
		}
		return result;
	}

	@Override
	public PhishAttackType getAttackType() {
		return PhishAttackType.IPNoBrand;
	}

	@Override
	public List<Integer> getAttackParts() {
		List<Integer> result = super.getAttackParts();
		result.add(3);
		return result;
	}
}
