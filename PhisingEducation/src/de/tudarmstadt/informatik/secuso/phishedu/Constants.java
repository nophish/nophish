package de.tudarmstadt.informatik.secuso.phishedu;

import de.tudarmstadt.informatik.secuso.phishedu.backend.attacks.HTTPAttack;
import de.tudarmstadt.informatik.secuso.phishedu.backend.attacks.HomoglyphicAttac;
import de.tudarmstadt.informatik.secuso.phishedu.backend.attacks.HostInPathAttack;
import de.tudarmstadt.informatik.secuso.phishedu.backend.attacks.IPAttack;
import de.tudarmstadt.informatik.secuso.phishedu.backend.attacks.Level2Attack;
import de.tudarmstadt.informatik.secuso.phishedu.backend.attacks.MisleadingAttack;
import de.tudarmstadt.informatik.secuso.phishedu.backend.attacks.NonsenseAttack;
import de.tudarmstadt.informatik.secuso.phishedu.backend.attacks.SubdomainAttack;
import de.tudarmstadt.informatik.secuso.phishedu.backend.attacks.TypoAttack;
import de.tudarmstadt.informatik.secuso.phishedu.backend.attacks.Unrelated;

public class Constants {
	/**
	 * Game configuration
	 */

	public static final boolean ALLOW_LEVEL0_REPLAY = true;

	public static final int COUNT_DOWN_INTERVAL = 1000;
	public static final int MILLIS_IN_FUTURE = 2500;
	public static final String EXTRA_LEVEL = "level";
	public static final String EXTRA_SITE_TYPE = "site_type";
	public static final String EXTRA_ATTACK_TYPE = "attack_type";
	public static final String ARG_PAGE_NUMBER = "page";
	public static final String EXTRA_RESULT = "result";

}
