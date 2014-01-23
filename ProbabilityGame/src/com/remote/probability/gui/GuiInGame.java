package com.remote.probability.gui;

import com.remote.probability.world.GameStatistics;
import com.remote.remote2d.engine.art.Fonts;
import com.remote.remote2d.engine.art.Renderer;
import com.remote.remote2d.engine.logic.Vector2;
import com.remote.remote2d.engine.world.Map;

public class GuiInGame extends com.remote.remote2d.engine.gui.GuiInGame {
	
	public static DebugModule module;
	private int HEALTH_POS = -1;

	public GuiInGame(Map map) {
		super(map);
		GameStatistics.playerHealth = 1;
	}
	
	@Override
	public void render(float interpolation)
	{
		super.render(interpolation);
		
		if(module != null)
			module.renderDebug(interpolation);
		
		Fonts.get("Jungle").drawString("Health:", 7, 7, 20, 0x777777);
		Fonts.get("Jungle").drawString("Health:", 10, 10, 20, 0xffffff);
		
		if(HEALTH_POS == -1)
			HEALTH_POS = Fonts.get("Jungle").getStringDim("Health:", 20)[0]+10;
		
		Renderer.drawRect(new Vector2(HEALTH_POS,10), new Vector2(200,20), 0x880000, 1);
		Renderer.drawRect(new Vector2(HEALTH_POS,10), new Vector2((float) (200f*GameStatistics.playerHealth),20), 0xff0000, 1);
	}
	
	public static interface DebugModule
	{
		public void renderDebug(float interpolation);
	}

}
