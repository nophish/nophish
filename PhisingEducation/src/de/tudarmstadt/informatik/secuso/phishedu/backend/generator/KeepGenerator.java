package de.tudarmstadt.informatik.secuso.phishedu.backend.generator;

import de.tudarmstadt.informatik.secuso.phishedu.backend.PhishAttackType;
import de.tudarmstadt.informatik.secuso.phishedu.backend.PhishURLInterface;

/**
 * This Generator passes the decorated URL without modification. 
 * @author Clemens Bergmann <cbergmann@schuhklassert.de>
 *
 */
public class KeepGenerator extends AbstractGenerator {

	/**
	 * See {@link AbstractGenerator#AbstractGenerator(PhishURLInterface)}
	 * @param object See {@link AbstractGenerator#AbstractGenerator(PhishURLInterface)}
	 */
	public KeepGenerator(PhishURLInterface object) {super(object);}

	@Override
	public PhishAttackType getAttackType() {
		return this.object.getAttackType();
	}
}
