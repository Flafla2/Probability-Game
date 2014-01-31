package com.remote.probability.gui;

import com.remote.probability.world.MapGenerator;
import com.remote.probability.world.MapGenerator.ProgressMeter;
import com.remote.remote2d.engine.Remote2D;
import com.remote.remote2d.engine.art.Fonts;
import com.remote.remote2d.engine.art.Renderer;
import com.remote.remote2d.engine.art.ResourceLoader;
import com.remote.remote2d.engine.art.Texture;
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
		
		backgroundColor = 0x2c1e23;
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
				Remote2D.guiList.push(Remote2D.getGame().getNewInGameGui(map));
			}
		});
		
		loadThread.start();
	}
	
	@Override
	public void renderBackground(float interpolation)
	{
		super.renderBackground(interpolation);
		
		Texture tex = ResourceLoader.getTexture("res/gui/title_screen.png");
		Vector2 dim = new Vector2(tex.getImage().getWidth(),tex.getImage().getHeight());
		float ratio = ((float)screenWidth())/dim.x;
		dim.x *= ratio;
		dim.y *= ratio;
		
		if(dim.y >= screenHeight())
			Renderer.drawRect(new Vector2(0, -dim.y+screenHeight()), dim, tex, 0xffffff, 1);
		else
			Renderer.drawRect(new Vector2(0), dim, tex, 0xffffff, 1);
		
		Fonts.get("Jungle").drawCenteredString("LOADING...", 10, 70, 0xffffff);
	}
	
	@Override
	public void render(float interpolation)
	{
		super.render(interpolation);		
		Renderer.drawLineRect(new Vector2(screenWidth()/2-300,screenHeight()/2-20), new Vector2(600,40), 0xffffff, 1.0f);
		Renderer.drawRect(new Vector2(screenWidth()/2-300,screenHeight()/2-20), new Vector2((float)(600*progress.getProgress()),40), 0xffffff, 1.0f);
	}
	
}
