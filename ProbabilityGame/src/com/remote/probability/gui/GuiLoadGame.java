package com.remote.probability.gui;

import com.remote.probability.AudioSwitcher;
import com.remote.remote2d.engine.Remote2D;
import com.remote.remote2d.engine.art.Fonts;
import com.remote.remote2d.engine.art.Renderer;
import com.remote.remote2d.engine.art.ResourceLoader;
import com.remote.remote2d.engine.art.Texture;
import com.remote.remote2d.engine.gui.GuiMenu;
import com.remote.remote2d.engine.logic.Vector2;
import com.remote.remote2d.engine.world.Map;

public class GuiLoadGame extends GuiMenu {
	
	boolean started = false;
	boolean finished = false;
	
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
		
		Fonts.get("Jungle").drawCenteredString("LOADING...", screenHeight()/2-35, 70, 0xffffff);
	}
	
	@Override
	public void tick(int i, int j, int k)
	{
		super.tick(i, j, k);
		
		if(finished)
		{
			Remote2D.guiList.pop();
			Remote2D.guiList.push(new GuiMainMenu());
		}
			
		if(started)
			return;
		started = true;
		
		Thread loadThread = new Thread(new Runnable() {
			@Override
			public void run() {
				AudioSwitcher.init();
				finished = true;
			}
		});
		
		loadThread.start();
	}
	
}
