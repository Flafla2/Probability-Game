package com.remote.probability.gui;

import com.remote.probability.world.GameStatistics;
import com.remote.probability.world.MapGenerator;
import com.remote.probability.world.MapGeneratorSimple;
import com.remote.remote2d.engine.DisplayHandler;
import com.remote.remote2d.engine.Remote2D;
import com.remote.remote2d.engine.art.Fonts;
import com.remote.remote2d.engine.art.Renderer;
import com.remote.remote2d.engine.gui.GuiButton;
import com.remote.remote2d.engine.gui.GuiMenu;
import com.remote.remote2d.engine.logic.Vector2;

public class GuiRiskSelect extends GuiMenu {
	
	private GuiMenu below;
	private GuiSlider slider;
	
	public GuiRiskSelect(GuiMenu below)
	{
		this.below = below;
		slider = new GuiSlider(new Vector2(100,100),new Vector2(screenWidth()-200,96),200,0);
	}
	
	@Override
	public void initGui()
	{
		if(slider != null)
			slider.dim = new Vector2(screenWidth()-200,96);
		
		buttonList.clear();
		buttonList.add(new GuiButtonStyled(0,new Vector2(screenWidth()/2-200,screenHeight()-50),new Vector2(200,40),"Quit"));
		buttonList.add(new GuiButtonStyled(1,new Vector2(screenWidth()/2,screenHeight()-50),new Vector2(200,40),"Continue to wave "+(GameStatistics.wave+1)));
	}
	
	@Override
	public void renderBackground(float interpolation)
	{
		below.render(0);
		Renderer.drawRect(new Vector2(0), DisplayHandler.getDimensions(), 0x000000, 0.5f);
		
		Fonts.get("Jungle").drawCenteredString("CHOOSE YOUR RISK FACTOR", 20, 50, 0xffffff);
		Fonts.get("Arial").drawCenteredString("PROJECTED DIFFICULTY: "+
		GameStatistics.getRoundProgressionFactor(GameStatistics.wave)+"% - "+(int)GameStatistics.maxFinalDifficultyModifier(GameStatistics.wave, (int)(slider.progress*100))+"%", 250, 30, 0xffffff);
		
	}
	
	@Override
	public void render(float interpolation)
	{
		super.render(interpolation);
		slider.render(interpolation);
	}
	
	@Override
	public void actionPerformed(GuiButton button)
	{
		if(button.id == 0)
		{
			while(!(Remote2D.guiList.peek() instanceof GuiMainMenu)) Remote2D.guiList.pop();
		} else if(button.id == 1)
		{
			GameStatistics.setFinalDifficultyModifier(GameStatistics.wave, (int)(slider.progress*100));
			GameStatistics.riskFactor = (int)(slider.progress*100);
			Remote2D.guiList.pop();
			MapGenerator gen = new MapGeneratorSimple();
			Remote2D.guiList.push(new GuiLoadMap(gen, 40, 40, 1337));
		}
	}
	
	@Override
	public void tick(int i, int j, int k)
	{
		super.tick(i, j, k);
		slider.tick(i, j, k);
	}
	
	public static double round(double value, int places) {
	    if (places < 0) throw new IllegalArgumentException();

	    long factor = (long) Math.pow(10, places);
	    value = value * factor;
	    long tmp = Math.round(value);
	    return (double) tmp / factor;
	}
	
}
