package com.remote.probability.gui;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import com.remote.probability.AudioSwitcher;
import com.remote.remote2d.engine.DisplayHandler;
import com.remote.remote2d.engine.Remote2D;
import com.remote.remote2d.engine.art.Fonts;
import com.remote.remote2d.engine.art.Renderer;
import com.remote.remote2d.engine.art.ResourceLoader;
import com.remote.remote2d.engine.art.Texture;
import com.remote.remote2d.engine.gui.GuiButton;
import com.remote.remote2d.engine.gui.GuiMenu;
import com.remote.remote2d.engine.logic.Vector2;

public class GuiOptions extends GuiMenu {
	
	public GuiSlider music;
	
	public GuiOptions()
	{
		super();
		backgroundColor = 0x2c1e23;
		music = new GuiSlider(new Vector2(40,200),new Vector2(screenWidth()-80,64),64,AudioSwitcher.musicVol);
		music.renderNumber = true;
	}
	
	@Override
	public void initGui()
	{
		final int buttonWidth = 500;
		final int buttonHeight = 40;
		final int buttonx = screenWidth()/2-buttonWidth/2;
		int buttonY = 300;
		
		buttonList.clear();
		buttonList.add(new GuiButtonStyled(0,new Vector2(buttonx,buttonY),new Vector2(buttonWidth,buttonHeight),"Toggle Fullscreen"));
		buttonY += 50;
		
		buttonList.add(new GuiButtonStyled(1,new Vector2(buttonx,screenHeight()-40),new Vector2(buttonWidth,buttonHeight),"Back"));
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
		
		Fonts.get("Jungle").drawCenteredString("OPTIONS", 20, 100, 0xffffff);
		Fonts.get("Pixel_Arial").drawCenteredString("Music Volume", 150, 20, 0xffffff);
		music.render(interpolation);
	}
	
	@Override
	public void tick(int i, int j, int k)
	{
		super.tick(i, j, k);
		
		music.tick(i, j, k);
		AudioSwitcher.setMusicVol((float) music.progress);
	}
	
	@Override
	public void actionPerformed(GuiButton button)
	{
		if(button.id == 0)
		{
			DisplayMode disp = Display.getDesktopDisplayMode();
			DisplayHandler.setDisplayMode(disp.getWidth(), disp.getHeight(), !DisplayHandler.getFullscreen(), false);
		}
		else if(button.id == 1)
			Remote2D.guiList.pop();
	}
	
}
