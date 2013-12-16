package de.tudarmstadt.informatik.secuso.phishedu.backend.attacks;

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
	int attack_domain=-1;
	private static final String[] DOMAIN_ADDITIONS={
		"-login",
		"-anmelden",
		"-secure",
		"-sicher",
		"-accounts",
		"-konto",
		"-verify",
		"-verification",
		"-verifizierung",
		"-signin",
		"-com",
		"-de",
		"-home",
		"-update",
		"-registration",
		"-registrierung",
		"-settings",
		"-einstellungen",
		"-service",
		"-support",
		"-hilfe"
	};
	/**
	 * This constructor is required because of the implementation in {@link BackendController#getNextUrl()}
	 * @param object This Parmeter is discarded. It is replaced by a PhishTank URL
	 */
	public MisleadingAttack(PhishURLInterface object) {
		super(object);
		attack_domain=new Random().nextInt(DOMAIN_ADDITIONS.length);
	}

	@Override
	public PhishAttackType getAttackType() {
		return PhishAttackType.Sudomains;
	}
	
	@Override
	public String[] getParts() {
		String[] parts = super.getParts();
		ArrayList<String> adder = new ArrayList<String>(Arrays.asList(parts));
		String hostname=adder.remove(3);
		String[] hostparts = hostname.split("\\.");
		hostparts[hostparts.length-2]+=DOMAIN_ADDITIONS[attack_domain]+".";
		adder.addAll(3, Arrays.asList(hostparts));
		return adder.toArray(new String[0]);
	}
	
	@Override
	public boolean partClicked(int part) {
		return part==3;
	}
	
	@Override
	public List<Integer> getAttackParts() {
		List<Integer> result = super.getAttackParts();
		result.add(3);
		return result;
	}
}
