package de.tudarmstadt.informatik.secuso.phishedu.backend.attacks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import de.tudarmstadt.informatik.secuso.phishedu.backend.BackendControllerImpl;
import de.tudarmstadt.informatik.secuso.phishedu.backend.PhishAttackType;
import de.tudarmstadt.informatik.secuso.phishedu.backend.PhishURLInterface;

/**
 * This Attack replaces the whole URL by a replace from phishtank
 * @author Clemens Bergmann <cbergmann@schuhklassert.de>
 *
 */
public class NonsenseAttack extends AbstractAttack {
	int attack_url=-1;
	
	protected static final String[] PHISHER_DOMAINS ={
		"yxtar.com",
		"badcat.de",
		"myponyfarm.com",
		"kjshkd.de",
		"ddffgh.de",
		"qieoqq.com",
		"events-ma.de",
		"hitrakstaffing.com",
		"friendsoflight.com",
		"comil11cenepa.de",
		"birdsnbeesbaby.com",
		"mabaatiroofings.com",
		"jshk.de"
		
	};
	
	/**
	 * This constructor is required because of the implementation in {@link BackendControllerImpl#getNextUrl()}
	 * @param object This Parmeter is discarded. It is replaced by a PhishTank URL
	 */
	public NonsenseAttack(PhishURLInterface object) {
		super(object);
		attack_url=new Random().nextInt(PHISHER_DOMAINS.length);
	}

	@Override
	public PhishAttackType getAttackType() {
		return PhishAttackType.Nonsense;
	}
	
	@Override
	public String[] getParts() {
		String[] parts = super.getParts();
		ArrayList<String> adder = new ArrayList<String>(Arrays.asList(parts));
		adder.add(4, "."+PHISHER_DOMAINS[attack_url]);
		return adder.toArray(new String[0]);
	}
	
	@Override
	public int getDomainPart() {
		return 4;
	}
	
	@Override
	public List<Integer> getAttackParts() {
		List<Integer> result = super.getAttackParts();
		result.add(4);
		return result;
	}
}
