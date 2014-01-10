package com.remote.probability.gui;

import com.remote.remote2d.engine.world.Map;

public class GuiInGame extends com.remote.remote2d.engine.gui.GuiInGame {
	
	public static DebugModule module;

	public GuiInGame(Map map) {
		super(map);
	}
	
	@Override
	public void render(float interpolation)
	{
		super.render(interpolation);
		
		if(module != null)
			module.renderDebug(interpolation);
	}
	
	public static interface DebugModule
	{
		public void renderDebug(float interpolation);
	}

}
