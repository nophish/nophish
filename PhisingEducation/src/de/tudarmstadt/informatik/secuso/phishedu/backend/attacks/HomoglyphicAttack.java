package de.tudarmstadt.informatik.secuso.phishedu.backend.attacks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.tudarmstadt.informatik.secuso.phishedu.backend.BackendControllerImpl;
import de.tudarmstadt.informatik.secuso.phishedu.backend.PhishAttackType;
import de.tudarmstadt.informatik.secuso.phishedu.backend.PhishURL;

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
			{"w","vv"},
			{"vv", "w"},
			{"d","cl"},
			{"cl","d"}
	};
	/**
	 * This constructor is required because of the implementation in {@link BackendControllerImpl#getNextUrl()}
	 * @param object This Parmeter is discarded. It is replaced by a PhishTank URL
	 */
	int attack_replacement=-1;
	public HomoglyphicAttack(PhishURL object) {
		super(object);
		String domain = getsecondleveldomain();
		//with this implementation the string might not be changed if it does not contain a match
		for (int i=0;i < REPLACEMENTS.length; i++) {
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
		String[] parts = super.getParts();
		ArrayList<String> adder = new ArrayList<String>(Arrays.asList(parts));
		String domain = adder.remove(3);
		String new_domain=domain.replaceFirst(REPLACEMENTS[attack_replacement][0], REPLACEMENTS[attack_replacement][1]);
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
