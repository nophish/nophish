package de.tudarmstadt.informatik.secuso.phishedu.backend.attacks;

import java.util.List;

import de.tudarmstadt.informatik.secuso.phishedu.backend.BackendController;
import de.tudarmstadt.informatik.secuso.phishedu.backend.PhishAttackType;
import de.tudarmstadt.informatik.secuso.phishedu.backend.PhishURLInterface;

/**
 * This Attack replaces the whole URL by a replace from phishtank
 * @author Clemens Bergmann <cbergmann@schuhklassert.de>
 *
 */
public class HTTPAttack extends AbstractAttack {
	/**
	 * This constructor is required because of the implementation in {@link BackendController#getNextUrl()}
	 * @param object This Parmeter is discarded. It is replaced by a PhishTank URL
	 */
	public HTTPAttack(PhishURLInterface object) {
		super(object);
	}

	@Override
	public PhishAttackType getAttackType() {
		return PhishAttackType.HTTP;
	}
	
	@Override
	public String[] getParts() {
		String[] parts = super.getParts();
		parts[0]="http";
		return parts;
	}
	
	@Override
	public boolean partClicked(int part) {
		return part==0;
	}

	@Override
	public List<Integer> getAttackParts() {
		List<Integer> result = super.getAttackParts();
		result.add(0);
		return result;
	}
}
