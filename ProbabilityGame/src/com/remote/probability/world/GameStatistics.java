package com.remote.probability.world;

import com.remote.probability.Game;

public class GameStatistics {
	
	public static byte[][] map;
	public static int tileSize;
	
	public static double playerHealth = 1.0;
	public static double playerMoney = 0;
	public static boolean finished = false;
	
	public static int wave = 0;
	
	public static double playerDamageModifier = 1.0;
	public static double playerHealthModifier = 1.0;
	public static double playerSpeedModifier = 1.0;
	public static int bulletBounceNum = 0;
	public static int bombNum = 0;
	
	public static double finalDifficultyModifier = 1;
	public static int riskFactor = 0;
	public static int lives = 0;
	
	public static final int HEALTH_INCREASE_COST = 100;
	public static final int BULLET_BOUNCE_COST = 500;
	public static final int EXTRA_LIFE_COST = 1000;
	public static final int DAMAGE_INCREASE_COST = 300;
	
	public static final int BASE_PLAYER_HEALTH = 100;
	public static final int BASE_PLAYER_DAMAGE = 10;
	
	
	public static float getMoneyMultiplier(int wave, int riskFactor)
	{
		return 1+wave/5+riskFactor/25;
	}
	
	public static int getTrueRiskVariable(int wave, int riskFactor)
	{
		//return Game.random.nextInt(riskFactor*2)-riskFactor;
		return Game.random.nextInt(riskFactor+1)*wave/5;
	}
	
	public static int getRoundProgressionFactor(int wave)
	{
		return wave*20;
	}
	
	public static double setFinalDifficultyModifier(int wave, int riskFactor)
	{
		return finalDifficultyModifier = 1+(double)(getTrueRiskVariable(wave, riskFactor)+getRoundProgressionFactor(wave))/100.0;
	}
	
	public static double maxFinalDifficultyModifier(int wave, int riskFactor)
	{
		return 1+(float)(riskFactor*wave/5+getRoundProgressionFactor(wave));
	}

	public static void reset() {
		playerHealth = 1;
		playerMoney = 0;
		wave = 0;
		playerDamageModifier = 1;
		playerHealthModifier = 1;
		playerSpeedModifier = 1;
		bulletBounceNum = 0;
		bombNum = 0;
		finalDifficultyModifier = 1;
		riskFactor = 0;
		lives = 0;
	}
	
}
