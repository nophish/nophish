package de.tudarmstadt.informatik.secuso.phishedu.backend.generator;

import de.tudarmstadt.informatik.secuso.phishedu.backend.PhishURLInterface;

/**
 * This attack is baesed on using an IP address as hostname.
 * This might confuse the user so he can not be sure that it might not be the correct Host.
 * @author Clemens Bergmann <cbergmann@schuhklassert.de>
 *
 */
public class SudomainGenerator extends BaseGenerator {

	/**
	 * See {@link BaseGenerator#BaseGenerator(PhishURLInterface)}
	 * @param object See {@link BaseGenerator#BaseGenerator(PhishURLInterface)}
	 */
	public SudomainGenerator(PhishURLInterface object) {super(object);}
	
	@Override
	public String[] getParts(){
		String[] parts = this.object.getParts();
		return parts;
	}
	
	@Override
	public boolean partClicked(int part) {
		return part == 3;
	}
	

}
