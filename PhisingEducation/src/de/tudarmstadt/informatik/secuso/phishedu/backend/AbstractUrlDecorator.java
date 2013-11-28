package de.tudarmstadt.informatik.secuso.phishedu.backend;

import java.util.List;

import de.tudarmstadt.informatik.secuso.phishedu.backend.BackendController;
import de.tudarmstadt.informatik.secuso.phishedu.backend.PhishAttackType;
import de.tudarmstadt.informatik.secuso.phishedu.backend.PhishResult;
import de.tudarmstadt.informatik.secuso.phishedu.backend.PhishSiteType;
import de.tudarmstadt.informatik.secuso.phishedu.backend.PhishURLInterface;

/**
 * This is the abstract implementation for following Attacks.
 * @author Clemens Bergmann <cbergmann@schuhklassert.de>
 *
 */
public abstract class AbstractUrlDecorator implements PhishURLInterface {
	protected PhishURLInterface object;
	
	/**
	 * This constructor takes a random Valid URL and decorates it.
	 */
	public AbstractUrlDecorator(){
		this.object=BackendController.getInstance().getPhishURL(PhishAttackType.NoPhish);
	}
	
	/**
	 * To build an attack we need a url to decorate
	 * @param object the decorated URL
	 */
	public AbstractUrlDecorator(PhishURLInterface object){
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
	public int getPoints(PhishResult result) {
		return this.object.getPoints(result);
	}

	@Override
	public abstract PhishResult getResult(boolean acceptance);
	
	@Override
	public boolean partClicked(int part) {
		return this.object.partClicked(part);
	}
	
	@Override
	public List<Integer> getAttackParts() {
		return this.object.getAttackParts();
	}
	
	/**
	 * We don't allw overwrite on the validate function
	 */
	@Override
	public final boolean validate() {
		return this.object.validate();
	}

}
