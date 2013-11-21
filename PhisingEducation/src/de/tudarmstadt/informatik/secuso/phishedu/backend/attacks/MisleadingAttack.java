package de.tudarmstadt.informatik.secuso.phishedu.backend.attacks;

import java.security.DomainCombiner;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import de.tudarmstadt.informatik.secuso.phishedu.backend.BackendController;
import de.tudarmstadt.informatik.secuso.phishedu.backend.PhishAttackType;
import de.tudarmstadt.informatik.secuso.phishedu.backend.PhishURLInterface;

/**
 * This Attack replaces the whole URL by a replace from phishtank
 * @author Clemens Bergmann <cbergmann@schuhklassert.de>
 *
 */
public class MisleadingAttack extends AbstractAttack {
	int attackpos=0;
	private static final String[] DOMAIN_ADDITIONS={
		"-login",
		"-secure",
		"-accounts"
	};
	/**
	 * This constructor is required because of the implementation in {@link BackendController#getNextUrl()}
	 * @param object This Parmeter is discarded. It is replaced by a PhishTank URL
	 */
	public MisleadingAttack(PhishURLInterface object) {
		super(object);
	}

	@Override
	public PhishAttackType getAttackType() {
		return PhishAttackType.Sudomains;
	}
	
	@Override
	public String[] getParts() {
		String[] parts = super.getParts();
		List<String> adder = Arrays.asList(parts);
		String hostname=adder.remove(3);
		String[] hostparts = hostname.split("\\.");
		hostparts[hostparts.length-2]+=DOMAIN_ADDITIONS[new Random().nextInt(DOMAIN_ADDITIONS.length)];
		adder.addAll(3, Arrays.asList(hostparts));
		return adder.toArray(new String[0]);
	}
	
	@Override
	public boolean partClicked(int part) {
		return part==3;
	}
	
	
}
