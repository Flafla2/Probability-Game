package com.remote.probability;

import com.remote.remote2d.engine.AudioHandler;

public class AudioSwitcher {
	
	static SoundMode soundMode = SoundMode.NONE;
	static String currentSound = null;
	static long soundResetTime = -1;
	
	public static float musicVol = 1;
	
	public static String title = "res/sound/music/ThemeTitle.ogg";
	public static String end = "res/sound/music/ThemeEnd.ogg";
	public static String[] background = {"res/sound/music/Background 1.ogg","res/sound/music/Background 2.ogg","res/sound/music/Background 3.ogg","res/sound/music/Background 4.ogg"};
	public static long[] bgLength = {210000,175000,180000,160000};
	
	public static String[] preloadSounds = {
		"res/sound/fx/coin/coin1.wav",
		"res/sound/fx/coin/coin2.wav",
		"res/sound/fx/coin/coin3.wav",
		"res/sound/fx/coin/coin4.wav",
		"res/sound/fx/coin/coin5.wav",
		"res/sound/fx/coin/Small Coin.wav",
		"res/sound/fx/enemy/death_explosion_0.wav",
		"res/sound/fx/enemy/death_explosion_1.wav",
		"res/sound/fx/player/shoot1.wav",
		"res/sound/fx/player/shoot2.wav",
		"res/sound/fx/player/shoot3.wav",
		"res/sound/fx/player/shoot4.wav",
		"res/sound/fx/player/shoot5.wav",
		"res/sound/fx/player/hit1.wav",
		"res/sound/fx/player/hit2.wav",
		"res/sound/fx/player/hit3.wav",
//		"res/sound/fx/player/Shot 1.wav",
//		"res/sound/fx/player/Shot 2.wav"
	};
	
	public static void init()
	{
		AudioHandler.preloadSound(title);
		AudioHandler.preloadSound(end);
		for(String b : background) AudioHandler.preloadSound(b);
		
		for(String s : preloadSounds) AudioHandler.preloadSound(s);
	}
	
	public static void setMusicVol(float vol)
	{
		if(currentSound == null) return;
		AudioHandler.setVolume(currentSound, vol);
		musicVol = vol;
	}
	
	public static void setSoundMode(SoundMode mode)
	{
		if(AudioHandler.soundSystem == null || AudioHandler.isShutDown())
			return;
		AudioSwitcher.soundMode = mode;
		if(currentSound != null)
		{
			AudioHandler.stop(currentSound);
			currentSound = null;
		}
		
		switch(soundMode)
		{
		case TITLE:
			currentSound = AudioHandler.playSound(title, true, true);
			break;
		case WIN:
			currentSound = AudioHandler.playSound(end, true, true);
			break;
		case INGAME:
			setRandomBGMusic();
			break;
		}
		
		if(currentSound != null) AudioHandler.setVolume(currentSound, musicVol);
	}
	
	public static void tick()
	{
		if(soundMode == SoundMode.INGAME && System.currentTimeMillis()>soundResetTime)
			setRandomBGMusic();
	}
	
	private static void setRandomBGMusic()
	{
		if(currentSound != null)
		{
			AudioHandler.stop(currentSound);
			currentSound = null;
		}
		
		int rand = Game.random.nextInt(background.length);
		currentSound = AudioHandler.playSound(background[rand], true, false);
		soundResetTime = System.currentTimeMillis()+bgLength[rand];
	}
	
	public static SoundMode getSoundMode()
	{
		return soundMode;
	}
	
	public static enum SoundMode
	{
		NONE, TITLE, INGAME, WIN;
	}
	
}
