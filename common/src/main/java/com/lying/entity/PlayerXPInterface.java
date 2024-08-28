package com.lying.entity;

public interface PlayerXPInterface
{
	public boolean xpIsManipulated();
	
	public void addStoredXP(int xpIn);
	
	public void addStoredLevel(int levelIn);
	
	public void storeCurrentXP();
	
	public void restoreActualXP();
	
	/** Calculates the total amount of XP for a given level */
	public static int getXPToNextLevel(int levelIn)
	{
		switch(levelIn)
		{
			default:
			case 0:
				return 7 + (levelIn * 2);
			case 15:
				return 37 + (levelIn - 15) * 5;
			case 30:
				return 112 + (levelIn - 30) * 9;
		}
	}
	
	/** Calculates the total XP needed for the given level */
	public static int getTotalXPForLevel(int level)
	{
		int level2 = level * level;
		if(level <= 16)
			return level2 + (6 * level);
		else if(level <= 31)
			return (int)(2.5 * level2 - 40.5 * level + 360);
		else
			return (int)(4.5 * level2 - 162.5 * level + 2220);
	}
	
	/** Returns the XP level conferred by the given total amount of XP */
	public static int xpToLevel(int totalXP)
	{
		int resultingLevel = 0;
		while(totalXP > getTotalXPForLevel(resultingLevel))
			resultingLevel++;
		return resultingLevel;
	}
}
