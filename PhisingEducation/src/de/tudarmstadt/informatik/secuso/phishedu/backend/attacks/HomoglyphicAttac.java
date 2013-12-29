package de.tudarmstadt.informatik.secuso.phishedu.backend.attacks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.tudarmstadt.informatik.secuso.phishedu.backend.BackendControllerImpl;
import de.tudarmstadt.informatik.secuso.phishedu.backend.PhishAttackType;
import de.tudarmstadt.informatik.secuso.phishedu.backend.PhishURLInterface;

/**
 * This Attack replaces the whole URL by a replace from phishtank
 * @author Clemens Bergmann <cbergmann@schuhklassert.de>
 *
 */
public class HomoglyphicAttac extends AbstractAttack {
	/**
	 * The replacements.
	 * The form is {match,replace}
	 */
	public static final String[][] REPLACEMENTS={
			{"w","vv"},
			{"vv", "w"},
			{"l","1"},
			{"1", "l"},
			{"l","I"},
			{"I", "l"},
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
	};
	/**
	 * This constructor is required because of the implementation in {@link BackendControllerImpl#getNextUrl()}
	 * @param object This Parmeter is discarded. It is replaced by a PhishTank URL
	 */
	public HomoglyphicAttac(PhishURLInterface object) {
		super(object);
	}

	@Override
	public PhishAttackType getAttackType() {
		return PhishAttackType.Homoglyphic;
	}
	
	@Override
	public String[] getParts() {
		String[] parts = super.getParts();
		ArrayList<String> adder = new ArrayList<String>(Arrays.asList(parts));
		String domain = adder.remove(3);
		String new_domain=domain;
		//with this implementation the string might not be changed if it does not contain a match
		for (String[] replacement : REPLACEMENTS) {
			new_domain=domain.replace(replacement[0], replacement[1]);
			if(!domain.equals(new_domain)){
				break;
			}
		}
		adder.add(3, new_domain);
		return adder.toArray(new String[0]);
	}
	
	@Override
	public List<Integer> getAttackParts() {
		List<Integer> result = super.getAttackParts();
		result.add(3);
		return result;
	}
}
