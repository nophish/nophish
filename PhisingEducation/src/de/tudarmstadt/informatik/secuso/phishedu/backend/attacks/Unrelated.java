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
public class Unrelated extends AbstractAttack {
	int attack_url=-1;
	
	protected static final String[] PHISHER_DOMAINS ={
		"login.com",
		"anmeldung.de",
		"security.com",
		"security-update.com",
		"sicherheit.de",
		"accounts.com",
		"konto.de",
		"verify.com",
		"verification.com",
		"verifizierung.de",
		"signin.com",
		"home.com",
		"update.de",
		"registration.com",
		"registrierung.com",
		"settings.com",
		"einstellungen.de",
		"service.de",
		"support.com",
		"hilfe.de",
		"abo",
		"abonnement"
		
	};
	
	/**
	 * This constructor is required because of the implementation in {@link BackendController#getNextUrl()}
	 * @param object This Parmeter is discarded. It is replaced by a PhishTank URL
	 */
	public Unrelated(PhishURLInterface object) {
		super(object);
		attack_url=new Random().nextInt(PHISHER_DOMAINS.length);
	}

	@Override
	public PhishAttackType getAttackType() {
		return PhishAttackType.Unrelated;
	}
	
	@Override
	public String[] getParts() {
		String[] parts = super.getParts();
		ArrayList<String> adder = new ArrayList<String>(Arrays.asList(parts));
		adder.add(4, "."+PHISHER_DOMAINS[attack_url]);
		return adder.toArray(new String[0]);
	}
	
	@Override
	public boolean partClicked(int part) {
		return part==4;
	}
	
	@Override
	public List<Integer> getAttackParts() {
		List<Integer> result = super.getAttackParts();
		result.add(4);
		return result;
	}
}
