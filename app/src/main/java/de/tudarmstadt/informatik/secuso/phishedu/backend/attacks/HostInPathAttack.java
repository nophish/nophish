package de.tudarmstadt.informatik.secuso.phishedu.backend.attacks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import de.tudarmstadt.informatik.secuso.phishedu.backend.BackendControllerImpl;
import de.tudarmstadt.informatik.secuso.phishedu.backend.PhishAttackType;
import de.tudarmstadt.informatik.secuso.phishedu.backend.PhishURL;

/**
 * This Attack replaces the whole URL by a replace from phishtank
 * 
 * @author Clemens Bergmann <cbergmann@schuhklassert.de>
 * 
 */
public class HostInPathAttack extends AbstractAttack {

	protected static final String[] PHISHER_DOMAINS = SubdomainAttack.PHISHER_DOMAINS;

	/**
	 * To build an attack we need a url to decorate
	 * 
	 * @param object
	 *            the decorated URL
	 */
	public HostInPathAttack(PhishURL object) {
		super(object);
		// if phish_domain = PHISHER_DOMAINS.length --> IP Adress attack with
		// host in path
		phish_domain = BackendControllerImpl.getInstance().getRandom()
				.nextInt(PHISHER_DOMAINS.length + 10);
		ip = getRandomIP();
	}

	@Override
	public PhishAttackType getAttackType() {
		return PhishAttackType.HostInPath;
	}

	private int phish_domain = -1;
	private String ip;
	@Override
	public String[] getParts() {
		String[] parts = super.getParts().clone();
		
		ArrayList<String> adder = new ArrayList<String>();
		//host to be added to path
		adder.add(parts[0]);
		adder.add(parts[1]);
		adder.add("");
		if (phish_domain < PHISHER_DOMAINS.length) {
			//a real domain is picked, no IP
			adder.add(PHISHER_DOMAINS[phish_domain]);
		}else{
			//IP Address to be generated for parts[3]
			adder.add(ip);
		}
		adder.add("/");
		adder.addAll(Arrays.asList(parts));
		return adder.toArray(new String[0]);
	}
	
	
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
	public List<Integer> getAttackParts() {
		List<Integer> result = super.getAttackParts();
		result.add(5);
		return result;
	}
}
