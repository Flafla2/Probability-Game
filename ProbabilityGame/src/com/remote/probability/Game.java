package com.remote.probability;

import java.util.Random;

import com.esotericsoftware.minlog.Log;
import com.remote.probability.component.ComponentBullet;
import com.remote.probability.component.ComponentCoin;
import com.remote.probability.component.ComponentEnemy;
import com.remote.probability.component.ComponentExplosion;
import com.remote.probability.component.ComponentPlayer;
import com.remote.probability.component.ComponentTile;
import com.remote.probability.gui.GuiInGame;
import com.remote.probability.gui.GuiLoadGame;
import com.remote.probability.gui.GuiMainMenu;
import com.remote.remote2d.engine.Remote2D;
import com.remote.remote2d.engine.Remote2DGame;
import com.remote.remote2d.engine.StretchType;
import com.remote.remote2d.engine.art.Fonts;
import com.remote.remote2d.engine.entity.InsertableComponentList;
import com.remote.remote2d.engine.gui.GuiMenu;
import com.remote.remote2d.engine.world.Map;

public class Game extends Remote2DGame {
	
	public static final double ONE_OVER_SQRT2 = 0.70710678118;
	public static final Random random = new Random();
	
	public static void main(String[] args)
	{
		Remote2D.startRemote2D(new Game());
	}

	@Override
	public void initGame() {
		//Remote2D.MAX_UPDATES_BEFORE_RENDER = 1;
		
		Remote2D.setTargetFPS(60);
		Remote2D.setTargetTPS(30);
		
		Log.DEBUG();
		Fonts.add("Jungle", "res/fonts/jungle.ttf", false);
		
		InsertableComponentList.addInsertableComponent("Player", ComponentPlayer.class);
		InsertableComponentList.addInsertableComponent("Enemy", ComponentEnemy.class);
		InsertableComponentList.addInsertableComponent("Bullet", ComponentBullet.class);
		InsertableComponentList.addInsertableComponent("Explosion", ComponentExplosion.class);
		InsertableComponentList.addInsertableComponent("Coin", ComponentCoin.class);
		InsertableComponentList.addInsertableComponent("Tile", ComponentTile.class);
		
		Remote2D.guiList.push(new GuiLoadGame());
	}
	
	@Override
	public StretchType getDefaultStretchType()
	{
		return StretchType.NONE;
	}
	
	@Override
	public GuiMenu getNewInGameGui(Map map)
	{
		return new GuiInGame(map);
	}
	
}
