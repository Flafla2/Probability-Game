package com.remote.probability.gui;

import com.remote.probability.AudioSwitcher;
import com.remote.probability.AudioSwitcher.SoundMode;
import com.remote.remote2d.engine.Remote2D;
import com.remote.remote2d.engine.art.Fonts;
import com.remote.remote2d.engine.art.Renderer;
import com.remote.remote2d.engine.art.ResourceLoader;
import com.remote.remote2d.engine.art.Texture;
import com.remote.remote2d.engine.gui.GuiButton;
import com.remote.remote2d.engine.gui.GuiMenu;
import com.remote.remote2d.engine.logic.Vector2;

public class GuiCredits extends GuiMenu {
	
	public GuiCredits()
	{
		super();
		backgroundColor = 0x2c1e23;
	}
	
	@Override
	public void initGui()
	{
		buttonList.clear();
		buttonList.add(new GuiButtonStyled(0,new Vector2(screenWidth()/2-300,screenHeight()-40),new Vector2(600,40),"Back"));
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
	}
	
	@Override
	public void render(float interpolation)
	{
		super.render(interpolation);
		
		Fonts.get("Jungle").drawCenteredString("CREDITS", 20, 100, 0xffffff);
		
		Fonts.get("Pixel_Arial").drawCenteredString("Concept and Design", 150, 20, 0xbbbbbb);
		Fonts.get("Pixel_Arial").drawCenteredString("Adrian Biagioli", 180, 20, 0xffffff);
		Fonts.get("Pixel_Arial").drawCenteredString("Erik Petersen", 210, 20, 0xffffff);
		
		Fonts.get("Pixel_Arial").drawCenteredString("Game Programming and Design", 250, 20, 0xbbbbbb);
		Fonts.get("Pixel_Arial").drawCenteredString("Adrian Biagioli", 280, 20, 0xffffff);
		
		Renderer.drawLine(new Vector2(0,310), new Vector2(screenWidth(),310), 0xffffff, 1);
		
		Fonts.get("Pixel_Arial").drawCenteredString("All art, music, and non-programming assets,", 320, 20, 0xffffff);
		Fonts.get("Pixel_Arial").drawCenteredString("excluding those for the Remote2D engine,", 350, 20, 0xffffff);
		Fonts.get("Pixel_Arial").drawCenteredString("were created by Mojang AB and the open source community", 380, 20, 0xffffff);
		Fonts.get("Pixel_Arial").drawCenteredString("in Maescool's \"Catacomb Snatch\" game project.", 410, 20, 0xffffff);
		Fonts.get("Pixel_Arial").drawCenteredString("These assets are NOT owned by the creators of \"THE RISK FACTOR\",", 440, 20, 0xffffff);
		Fonts.get("Pixel_Arial").drawCenteredString("but are open source and available to the community.", 470, 20, 0xffffff);
		Fonts.get("Pixel_Arial").drawCenteredString("http://www.mojang.com", 500, 20, 0xbbbbbb);
		Fonts.get("Pixel_Arial").drawCenteredString("http://www.catacombsnatch.net", 530, 20, 0xbbbbbb);
		
		Fonts.get("Pixel_Arial").drawCenteredString("Music by C418 and Anosou", 570, 20, 0xffffff);
		
		Renderer.drawLine(new Vector2(0,600), new Vector2(screenWidth(),600), 0xffffff, 1);
		
		Fonts.get("Logo").drawCenteredString("POWERED BY REMOTE2D", 610, 32, 0xff0000);
		Fonts.get("Logo").drawCenteredString("www.github.com/flafla2/remote2d-engine", 645, 32, 0xaa0000);
		Fonts.get("Logo").drawCenteredString("www.remotesoftworks.com", 670, 32, 0xaa0000);
	}
	
	@Override
	public void actionPerformed(GuiButton button)
	{
		if(button.id == 0)
			Remote2D.guiList.pop();
	}
	
	@Override
	public void tick(int i, int j, int k)
	{
		super.tick(i, j, k);
		if(AudioSwitcher.getSoundMode() != SoundMode.WIN)
			AudioSwitcher.setSoundMode(SoundMode.WIN);
	}

}
