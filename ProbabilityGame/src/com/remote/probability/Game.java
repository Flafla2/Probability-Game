package com.remote.probability;

import com.remote.probability.gui.GuiMainMenu;
import com.remote.remote2d.engine.Remote2D;
import com.remote.remote2d.engine.Remote2DGame;
import com.remote.remote2d.engine.StretchType;
import com.remote.remote2d.engine.art.Fonts;

public class Game extends Remote2DGame {
	
	public static void main(String[] args)
	{
		Remote2D.startRemote2D(new Game());
	}

	@Override
	public void initGame() {
		
		Fonts.add("Jungle", "res/fonts/jungle.ttf", false);
		
		Remote2D.guiList.push(new GuiMainMenu());
		
	}
	
	@Override
	public StretchType getDefaultStretchType()
	{
		return StretchType.NONE;
	}
	
}
