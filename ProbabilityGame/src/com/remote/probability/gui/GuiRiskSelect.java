package com.remote.probability.gui;

import org.lwjgl.input.Keyboard;

import com.remote.probability.AudioSwitcher;
import com.remote.probability.world.GameStatistics;
import com.remote.probability.world.MapGenerator;
import com.remote.probability.world.MapGeneratorSimple;
import com.remote.remote2d.engine.AudioHandler;
import com.remote.remote2d.engine.DisplayHandler;
import com.remote.remote2d.engine.Remote2D;
import com.remote.remote2d.engine.art.Fonts;
import com.remote.remote2d.engine.art.Renderer;
import com.remote.remote2d.engine.art.ResourceLoader;
import com.remote.remote2d.engine.art.Texture;
import com.remote.remote2d.engine.gui.GuiButton;
import com.remote.remote2d.engine.gui.GuiMenu;
import com.remote.remote2d.engine.logic.Vector2;

public class GuiRiskSelect extends GuiMenu {
	
	private GuiMenu below;
	private GuiSlider slider;
	
	private static final String[] upgradeItems = {"+20 Max Health","+1 Bullets Bounce Off Walls","+1 Extra Life","+10 Bullet Damage"};
	private static final int[] upgradeCost = {GameStatistics.HEALTH_INCREASE_COST, GameStatistics.BULLET_BOUNCE_COST, GameStatistics.EXTRA_LIFE_COST, GameStatistics.BULLET_DAMAGE_COST};
	private static Texture[] icons;
	
	private int[] upgradeWidth;
	private String[] currentValues;
	private int maxUpgradeWidth = 0;
	private int iconSize = 26;
	private long lastMessageTime = -1;
	
	public GuiRiskSelect(GuiMenu below)
	{
		this.below = below;
		
		slider = new GuiSlider(new Vector2(100,100),new Vector2(screenWidth()-200,96),200,0);
		if(icons == null)
		{
			icons = new Texture[4];
			icons[0] = ResourceLoader.getTexture("res/gui/p_heart.png");
			icons[1] = ResourceLoader.getTexture("res/gui/p_bullet.png");
			icons[2] = ResourceLoader.getTexture("res/gui/p_heart.png");
			icons[3] = ResourceLoader.getTexture("res/gui/p_bullet.png");
		}
		calculateCurrentValues();
		
		upgradeWidth = new int[upgradeItems.length];
		for(int x=0;x<upgradeItems.length;x++)
		{
			upgradeWidth[x] = Fonts.get("Pixel_Arial").getStringDim(upgradeItems[x], 20)[0];
			if(upgradeWidth[x] > maxUpgradeWidth)
				maxUpgradeWidth = upgradeWidth[x];
		}
	}
	
	private void calculateCurrentValues()
	{
		if(currentValues == null)
			currentValues = new String[4];
		currentValues[0] = "Current Max Health: "+(int)(GameStatistics.playerHealthModifier*100)+" HP";
		currentValues[1] = "Current: "+(int)GameStatistics.bulletBounceNum+" Bounces";
		currentValues[2] = "Current Extra Lives: "+GameStatistics.lives;
		currentValues[3] = "Current Damage: "+(int)(GameStatistics.playerDamageModifier*10);
	}
	
	@Override
	public void initGui()
	{
		if(slider != null)
			slider.dim = new Vector2(screenWidth()-200,96);
		
		buttonList.clear();
		buttonList.add(new GuiButtonStyled(0,new Vector2(screenWidth()/2-200,screenHeight()-50),new Vector2(200,40),"Quit"));
		buttonList.add(new GuiButtonStyled(1,new Vector2(screenWidth()/2,screenHeight()-50),new Vector2(200,40),"Continue to wave "+(GameStatistics.wave+1)));
		
		if(upgradeWidth == null) upgradeWidth = new int[upgradeItems.length];
		int maxCostWidth = 0;
		for(int x=0;x<upgradeItems.length;x++)
		{
			upgradeWidth[x] = Fonts.get("Pixel_Arial").getStringDim(upgradeItems[x], 20)[0];
			if(upgradeWidth[x] > maxUpgradeWidth)
				maxUpgradeWidth = upgradeWidth[x];
			int costWidth = Fonts.get("Pixel_Arial").getStringDim("Cost: "+upgradeCost[x], 20)[0];
			if(costWidth > maxCostWidth)
				maxCostWidth = costWidth;
		}
		
		int yPos = 400;
		for(int x=0;x<upgradeItems.length;x++)
		{
			buttonList.add(new GuiButtonStyled(x+2,new Vector2(screenWidth()/2+maxUpgradeWidth/2+maxCostWidth-170,yPos+25),new Vector2(200,40),"Buy"));
			yPos += 26+50;
		}
		
	}
	
	@Override
	public void renderBackground(float interpolation)
	{
		below.render(0);
		Renderer.drawRect(new Vector2(0), DisplayHandler.getDimensions(), 0x000000, 0.5f);
	}
	
	@Override
	public void render(float interpolation)
	{
		super.render(interpolation);
		slider.render(interpolation);
		
		Fonts.get("Jungle").drawCenteredString("CHOOSE YOUR RISK FACTOR", 20, 50, 0xffffff);
		Fonts.get("Pixel_Arial").drawCenteredString("PROJECTED DIFFICULTY: "+
				(int)round(GameStatistics.getMaxDifficulty(0)*100,0)+"% - "+(int)round(GameStatistics.getMaxDifficulty((int)(slider.progress*100))*100,0)+"%"
				, 220, 25, 0xffffff);
		Fonts.get("Pixel_Arial").drawCenteredString("TREASURE MULTIPLIER: x"+round(GameStatistics.getMoneyMultiplier((int)(slider.progress*100)),2), 260, 25, 0xffffff);
		Fonts.get("Jungle").drawCenteredString("SHOP", 315, 60, 0xffffff);
		Fonts.get("Pixel_Arial").drawCenteredString("Current Money: "+(int)(GameStatistics.playerMoney), 380, 15, 0x999999);
		int yPos = 400;
		int scrw = screenWidth()/2;
		for(int x=0;x<upgradeItems.length;x++)
		{
			Renderer.drawRect(new Vector2(scrw-maxUpgradeWidth/2-30-iconSize,yPos), new Vector2(iconSize), icons[x], 0xffffff, 1);
			Fonts.get("Pixel_Arial").drawString(upgradeItems[x], scrw-maxUpgradeWidth/2, yPos+3, 20, 0xffffff);
			Fonts.get("Pixel_Arial").drawString("Cost: "+upgradeCost[x], scrw+maxUpgradeWidth/2+30, yPos+3, 20, 0xffffff);
			Fonts.get("Pixel_Arial").drawString(currentValues[x], scrw-maxUpgradeWidth/2, yPos+25, 16, 0x999999);
			yPos += iconSize+50;
		}
		
		if(System.currentTimeMillis()-lastMessageTime < 2000)
			Fonts.get("Pixel_Arial").drawCenteredString("Not Enough Money!", screenHeight()-375, 30, 0xff0000);
	}
	
	@Override
	public void actionPerformed(GuiButton button)
	{
		if(button.id == 0)
		{
			while(!(Remote2D.guiList.peek() instanceof GuiMainMenu)) Remote2D.guiList.pop();
		} else if(button.id == 1)
		{
			GameStatistics.finalDifficultyModifier = GameStatistics.getFinalDifficultyModifier((int)(slider.progress*100));
			GameStatistics.riskFactor = (int)(slider.progress*100);
			Remote2D.guiList.pop();
			MapGenerator gen = new MapGeneratorSimple();
			Remote2D.guiList.push(new GuiLoadMap(gen, 40, 40, 1337));
		} else
		{
			int upgrade = button.id-2;
			if(upgradeCost[upgrade] > GameStatistics.playerMoney)
			{
				AudioHandler.playSound("res/sound/fx/Upgrade_Fail.wav", true, false);
				lastMessageTime = System.currentTimeMillis();
				return;
			}
			AudioHandler.playSound("res/sound/fx/Upgrade.wav", true, false);
			GameStatistics.playerMoney -= upgradeCost[upgrade];
			if(upgrade == 0) // Max Health += 10
				GameStatistics.playerHealthModifier += 0.2;
			else if(upgrade == 1) // Bullets Bounce
				GameStatistics.bulletBounceNum++;
			else if(upgrade == 2) // Extra Life
				GameStatistics.lives++;
			else if(upgrade == 3)
				GameStatistics.playerDamageModifier ++;
			
			calculateCurrentValues();
		}
	}
	
	@Override
	public void tick(int i, int j, int k)
	{
		super.tick(i, j, k);
		slider.tick(i, j, k);
		AudioSwitcher.tick();
		
		if(Remote2D.getIntegerKeyboardList().contains(Keyboard.KEY_M))
		{
			GameStatistics.playerMoney += 500;
			calculateCurrentValues();
		}
	}
	
	public static double round(double value, int places) {
	    if (places < 0) throw new IllegalArgumentException();

	    long factor = (long) Math.pow(10, places);
	    value = value * factor;
	    long tmp = Math.round(value);
	    return (double) tmp / factor;
	}
	
}
