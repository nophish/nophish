package de.tudarmstadt.informatik.secuso.phishedu.backend.attacks;

import java.util.Random;

import de.tudarmstadt.informatik.secuso.phishedu.backend.PhishAttackType;
import de.tudarmstadt.informatik.secuso.phishedu.backend.PhishURLInterface;

/**
 * This attack is baesed on using an IP address as hostname.
 * This might confuse the user so he can not be sure that it might not be the correct Host.
 * @author Clemens Bergmann <cbergmann@schuhklassert.de>
 *
 */
public class IPAttack extends AbstractAttack {

	/**
	 * See {@link AbstractAttack#AbstractAttack(PhishURLInterface)}
	 * @param object See {@link AbstractAttack#AbstractAttack(PhishURLInterface)}
	 */
	public IPAttack(PhishURLInterface object) {super(object);}
	
	@Override
	public String[] getParts(){
		String[] parts = this.object.getParts();
		parts[3] = getRandomIP();
		return parts;
	}
	
	private String getRandomIP(){
		Random rand = new Random();
		String result = "";
		for(int i=0; i<4; i++){
			result+=rand.nextInt(255);
			if(i<3){
				result+=".";
			}
		}
		return result;
	}

	@Override
	public PhishAttackType getAttackType() {
		return PhishAttackType.IP;
	}
	
	@Override
	public boolean partClicked(int part) {
		return part == 3;
	}
	

}