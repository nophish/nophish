package de.tudarmstadt.informatik.secuso.phishedu.backend.attacks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Vector;

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
		ArrayList<String[]> replacements = new ArrayList<String[]>();
		List<String[]> options = new ArrayList<String[]>(Arrays.asList(REPLACEMENTS));
		Random random = new Random();
		while(options.size()>0){
			replacements.add(options.remove(random.nextInt(options.size())));
		}
		
		//with this implementation the string might not be changed if it does not contain a match
		for (int i=0;i < replacements.size(); i++) {
			int location = domain.indexOf(replacements.get(i)[0]);
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
