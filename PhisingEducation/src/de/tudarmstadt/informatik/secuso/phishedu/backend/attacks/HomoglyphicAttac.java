package de.tudarmstadt.informatik.secuso.phishedu.backend.attacks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.tudarmstadt.informatik.secuso.phishedu.backend.BackendController;
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
			{"l","1"},
			{"l","I"},
			{"i","1"},
			{"o","0"},
	};
	/**
	 * This constructor is required because of the implementation in {@link BackendController#getNextUrl()}
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
	public boolean partClicked(int part) {
		return part==3;
	}
	
	@Override
	public List<Integer> getAttackParts() {
		List<Integer> result = super.getAttackParts();
		result.add(3);
		return result;
	}
}
