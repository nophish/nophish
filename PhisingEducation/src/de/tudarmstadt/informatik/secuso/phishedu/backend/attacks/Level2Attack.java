package de.tudarmstadt.informatik.secuso.phishedu.backend.attacks;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.tudarmstadt.informatik.secuso.phishedu.backend.PhishAttackType;
import de.tudarmstadt.informatik.secuso.phishedu.backend.PhishURLInterface;

/**
 * This Attack is special for Level2
 * It will accept the click on the domain name only.
 * @author Clemens Bergmann <cbergmann@schuhklassert.de>
 *
 */
public class Level2Attack extends AbstractAttack {

	/**
	 * See {@link AbstractAttack#AbstractAttack(PhishURLInterface)}
	 * @param object See {@link AbstractAttack#AbstractAttack(PhishURLInterface)}
	 */
	public Level2Attack(PhishURLInterface object) {super(object);}
	
	int correctPart = 0;
	
	@Override
	public String[] getParts() {
		String[] parts = super.getParts();
		ArrayList<String> adder = new ArrayList<String>(Arrays.asList(parts));
		String hostname=adder.remove(3);
		ArrayList<String> hostparts = new ArrayList<String>(Arrays.asList(hostname.split("\\.")));
		hostparts.set(hostparts.size()-2, hostparts.get(hostparts.size()-2)+"."+hostparts.get(hostparts.size()-1));
		hostparts.remove(hostparts.size()-1);
		this.correctPart=2+hostparts.size();
		for(int i=0; i<hostparts.size(); i++){
			String delim = (i<hostparts.size()-1) ? "." : "";
			adder.add(3+i,hostparts.get(i)+delim);
		}
		return adder.toArray(new String[0]);
	}
	
	@Override
	public boolean partClicked(int part) {
		return part == correctPart;
	}
	
	@Override
	public PhishAttackType getAttackType() {
		return PhishAttackType.Level2Attack;
	}
	
	@Override
	public List<Integer> getAttackParts() {
		List<Integer> result = super.getAttackParts();
		result.add(correctPart);
		return result;
	}
}
