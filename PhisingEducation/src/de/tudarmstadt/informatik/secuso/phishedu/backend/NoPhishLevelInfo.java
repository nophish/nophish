package de.tudarmstadt.informatik.secuso.phishedu.backend;

import de.tudarmstadt.informatik.secuso.phishedu.Constants;

/**
 * This Class represents the information about a Level
 */
public class NoPhishLevelInfo {
	public final int titleId;
	public final int subTitleId;
	public final int[] introLayouts;
	public final int[] finishedLayouts;
	public final int levelId;
	@SuppressWarnings("rawtypes")
	public final Class[] attackTypes;
	
	public NoPhishLevelInfo(int levelid){
		this.levelId = levelid;
		this.titleId=Constants.slevelTitlesIds[levelid];
		this.subTitleId=Constants.slevelSubtitlesIds[levelid];
		int intro_index = Math.min(levelid, Constants.levelIntroLayoutIds.length-1);
		this.introLayouts =Constants.levelIntroLayoutIds[intro_index];
		int finished_index = Math.min(levelid, Constants.levelFinishedLayoutIds.length-1);
		this.finishedLayouts =Constants.levelFinishedLayoutIds[finished_index];
		int attacktype_index = Math.min(levelid, Constants.levelAttackTypes.length-1);
		this.attackTypes = Constants.levelAttackTypes[attacktype_index];
	}
	
	public int getlevelPoints(){
		return BackendController.getInstance().getLevelPoints(this.levelId);
	}
	
}
