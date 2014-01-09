package com.remote.probability.gui;

import com.remote.probability.world.MapGenerator;
import com.remote.probability.world.MapGenerator.ProgressMeter;
import com.remote.remote2d.engine.Remote2D;
import com.remote.remote2d.engine.art.Fonts;
import com.remote.remote2d.engine.art.Renderer;
import com.remote.remote2d.engine.gui.GuiInGame;
import com.remote.remote2d.engine.gui.GuiMenu;
import com.remote.remote2d.engine.logic.Vector2;
import com.remote.remote2d.engine.world.Map;

public class GuiLoadMap extends GuiMenu {
	
	private ProgressMeter progress;
	private MapGenerator gen;
	private boolean started = false;
	private int width;
	private int height;
	private long seed;
	
	public GuiLoadMap(MapGenerator gen, int width, int height, long seed)
	{
		this.progress = new ProgressMeter();
		this.gen = gen;
		this.width = width;
		this.height = height;
		this.seed = seed;
	}
	
	@Override
	public void tick(int i, int j, int k)
	{
		super.tick(i, j, k);
		
		if(started)
			return;
		started = true;
		
		Thread loadThread = new Thread(new Runnable() {
			@Override
			public void run() {
				Map map = gen.generateTiledMap(width, height, seed, progress);
				Remote2D.guiList.pop();
				Remote2D.guiList.push(new GuiInGame(map));
			}
		});
		
		loadThread.start();
	}
	
	@Override
	public void render(float interpolation)
	{
		super.render(interpolation);
		Fonts.get("Arial").drawCenteredString("Loading: "+progress.getStage(), screenHeight()/2-50, 20, 0x000000);
		
		Renderer.drawLineRect(new Vector2(screenWidth()/2-300,screenHeight()/2-20), new Vector2(600,40), 0x000000, 1.0f);
		Renderer.drawRect(new Vector2(screenWidth()/2-300,screenHeight()/2-20), new Vector2((float)(600*progress.getProgress()),40), 0x000000, 1.0f);
	}
	
}
