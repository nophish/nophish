package de.tudarmstadt.informatik.secuso.phishedu.backend.attacks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import de.tudarmstadt.informatik.secuso.phishedu.backend.BackendController;
import de.tudarmstadt.informatik.secuso.phishedu.backend.PhishAttackType;
import de.tudarmstadt.informatik.secuso.phishedu.backend.PhishURLInterface;

/**
 * This Attack replaces the whole URL by a replace from phishtank
 * @author Clemens Bergmann <cbergmann@schuhklassert.de>
 *
 */
public class HostInPathAttack extends SubdomainAttack {
	
	/**
	 * This constructor is required because of the implementation in {@link BackendController#getNextUrl()}
	 * @param object This Parmeter is discarded. It is replaced by a PhishTank URL
	 */
	public HostInPathAttack(PhishURLInterface object) {
		super(object);
		phish_domain=new Random().nextInt(PHISHER_DOMAINS.length);
	}

	@Override
	public PhishAttackType getAttackType() {
		return PhishAttackType.HostInPath;
	}
	private int phish_domain=-1;
	
	@Override
	public String[] getParts() {
		String[] parts = super.getParts();
		String domain = parts[3];
		parts[3]=PHISHER_DOMAINS[phish_domain];
		ArrayList<String> adder = new ArrayList<String>(Arrays.asList(parts));
		adder.add(5,domain);
		return adder.toArray(new String[0]);
	}
	
	@Override
	public List<Integer> getAttackParts() {
		List<Integer> result = super.getAttackParts();
		result.add(5);
		return result;
	}
}
