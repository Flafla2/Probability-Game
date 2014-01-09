package com.remote.probability;

import com.esotericsoftware.minlog.Log;
import com.remote.probability.component.ComponentPlayer;
import com.remote.probability.gui.GuiMainMenu;
import com.remote.remote2d.engine.Remote2D;
import com.remote.remote2d.engine.Remote2DGame;
import com.remote.remote2d.engine.StretchType;
import com.remote.remote2d.engine.art.Fonts;
import com.remote.remote2d.engine.entity.InsertableComponentList;

public class Game extends Remote2DGame {
	
	public static void main(String[] args)
	{
		Remote2D.startRemote2D(new Game());
	}

	@Override
	public void initGame() {
		Log.DEBUG();
		Fonts.add("Jungle", "res/fonts/jungle.ttf", false);
		
		InsertableComponentList.addInsertableComponent("Player", ComponentPlayer.class);
		
		Remote2D.guiList.push(new GuiMainMenu());
		
	}
	
	@Override
	public StretchType getDefaultStretchType()
	{
		return StretchType.NONE;
	}
	
}
