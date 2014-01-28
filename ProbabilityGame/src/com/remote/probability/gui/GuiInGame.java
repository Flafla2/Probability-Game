package com.remote.probability.gui;

import java.awt.Color;

import org.lwjgl.input.Keyboard;

import com.esotericsoftware.minlog.Log;
import com.remote.probability.Game;
import com.remote.probability.world.GameStatistics;
import com.remote.remote2d.engine.Remote2D;
import com.remote.remote2d.engine.art.Fonts;
import com.remote.remote2d.engine.art.Renderer;
import com.remote.remote2d.engine.gui.Gui;
import com.remote.remote2d.engine.logic.Interpolator;
import com.remote.remote2d.engine.logic.Vector2;
import com.remote.remote2d.engine.world.Map;

public class GuiInGame extends com.remote.remote2d.engine.gui.GuiInGame {
	
	public static DebugModule module;
	private int HEALTH_POS = -1;
	private Color prevColor;
	private Color nextColor;
	private long nextColorChange = -1;
	private boolean isBackground = false;

	public GuiInGame(Map map) {
		super(map);
		GameStatistics.playerHealth = 1;
		GameStatistics.finished = false;
		prevColor = new Color(Game.random.nextInt(0xffffff));
		nextColor = new Color(Game.random.nextInt(0xffffff));
	}
	
	@Override
	public void render(float interpolation)
	{
		super.render(interpolation);
		
		if(isBackground)
			return;
		
		if(module != null)
			module.renderDebug(interpolation);
		
		Fonts.get("Jungle").drawString("Health:", 7, 7, 20, 0x777777);
		Fonts.get("Jungle").drawString("Health:", 10, 10, 20, 0xffffff);
		Fonts.get("Jungle").drawString("Difficulty: "+(GameStatistics.finalDifficultyModifier*100), 10, 30, 20, 0xffffff);
		
		if(HEALTH_POS == -1)
			HEALTH_POS = Fonts.get("Jungle").getStringDim("Health:", 20)[0]+10;
		
		Renderer.drawRect(new Vector2(HEALTH_POS,10), new Vector2(200,20), 0x880000, 1);
		Renderer.drawRect(new Vector2(HEALTH_POS,10), new Vector2((float) (200f*GameStatistics.playerHealth),20), 0xff0000, 1);
		
		if(GameStatistics.finished)
		{
			long time = System.currentTimeMillis();
			if(time > nextColorChange)
			{
				nextColorChange = time+500;
				prevColor = nextColor;
				nextColor = new Color(Game.random.nextInt(0xffffff));
			}
			
			double interp = 1-(double)(nextColorChange-time)/500d;
			float[] fcolors = Interpolator.linearInterpolate(prevColor.getRGBComponents(null), nextColor.getRGBComponents(null), interp);
			Color c = new Color(fcolors[0],fcolors[1],fcolors[2]);
			int color = c.getRGB();
			
			Fonts.get("Pixel_Arial").drawCenteredString("Press E to go to Next Level", screenHeight()/2+5, 20, color);
			Fonts.get("Pixel_Arial").drawCenteredString("You Won!!!", screenHeight()/2-45, 40, color);
		}
	}
	
	@Override
	public void tick(int i, int j, int k)
	{
		super.tick(i, j, k);
		if(Remote2D.getIntegerKeyboardList().contains(Keyboard.KEY_F))
			GameStatistics.finished = true;
		
		if(GameStatistics.finished)
		{
			if(Keyboard.isKeyDown(Keyboard.KEY_E))
			{
				Remote2D.guiList.pop();
				map.camera.updatePos();
				GameStatistics.wave++;
				isBackground = true;
				Remote2D.guiList.push(new GuiRiskSelect(this));
			}
		}
	}
	
	public static interface DebugModule
	{
		public void renderDebug(float interpolation);
	}

}
