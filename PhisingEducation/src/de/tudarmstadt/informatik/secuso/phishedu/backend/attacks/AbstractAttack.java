package de.tudarmstadt.informatik.secuso.phishedu.backend.attacks;

import de.tudarmstadt.informatik.secuso.phishedu.backend.PhishAttackType;
import de.tudarmstadt.informatik.secuso.phishedu.backend.PhishResult;
import de.tudarmstadt.informatik.secuso.phishedu.backend.PhishSiteType;
import de.tudarmstadt.informatik.secuso.phishedu.backend.PhishURLInterface;

/**
 * This is the abstract implementation for following Attacks.
 * @author Clemens Bergmann <cbergmann@schuhklassert.de>
 *
 */
public abstract class AbstractAttack implements PhishURLInterface {
	private PhishURLInterface object;
	
	/**
	 * To build an attack we need a url to decorate
	 * @param object the decorated URL
	 */
	public AbstractAttack(PhishURLInterface object){
		this.object=object;
	}

	@Override
	public String[] getParts() {
		return this.object.getParts();
	}

	@Override
	public PhishSiteType getSiteType() {
		return this.object.getSiteType();
	}

	@Override
	public abstract PhishAttackType getAttackType();

	@Override
	public int getPoints(PhishResult result) {
		return this.object.getPoints(result);
	}

	@Override
	public PhishResult getResult(boolean acceptance) {
		return this.object.getResult(acceptance);
	}
	
	@Override
	public boolean partClicked(int part) {
		return this.object.partClicked(part);
	}

}
