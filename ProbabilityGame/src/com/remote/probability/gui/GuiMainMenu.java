package com.remote.probability.gui;

import org.lwjgl.input.Keyboard;

import com.remote.probability.AudioSwitcher;
import com.remote.probability.AudioSwitcher.SoundMode;
import com.remote.probability.world.GameStatistics;
import com.remote.probability.world.MapGenerator;
import com.remote.probability.world.MapGeneratorSimple;
import com.remote.remote2d.editor.GuiEditor;
import com.remote.remote2d.engine.Remote2D;
import com.remote.remote2d.engine.StretchType;
import com.remote.remote2d.engine.art.Fonts;
import com.remote.remote2d.engine.art.Renderer;
import com.remote.remote2d.engine.art.ResourceLoader;
import com.remote.remote2d.engine.art.Texture;
import com.remote.remote2d.engine.gui.GuiButton;
import com.remote.remote2d.engine.gui.GuiMenu;
import com.remote.remote2d.engine.io.R2DFileUtility;
import com.remote.remote2d.engine.logic.Vector2;

public class GuiMainMenu extends GuiMenu{
	
	public GuiMainMenu()
	{
		super();
		backgroundColor = 0x2c1e23;
	}
	
	@Override
	public void initGui()
	{
		final int buttonWidth = 500;
		final int buttonHeight = 40;
		final int buttonx = screenWidth()/2-buttonWidth/2;
		final int numButtons = 3;
		int buttonY = screenHeight()/2-(buttonHeight*numButtons+10*(numButtons-1))/2;
		
		buttonList.clear();
		buttonList.add(new GuiButtonStyled(0,new Vector2(buttonx,buttonY),new Vector2(buttonWidth,buttonHeight),"Play Game"));
		buttonY += 100;
		buttonList.add(new GuiButtonStyled(1,new Vector2(buttonx,buttonY),new Vector2(buttonWidth,buttonHeight),"Open Editor"));
		buttonY += 100;
		buttonList.add(new GuiButtonStyled(3,new Vector2(buttonx,buttonY),new Vector2(buttonWidth,buttonHeight),"Credits"));
		buttonY += 100;
		buttonList.add(new GuiButtonStyled(2,new Vector2(buttonx,buttonY),new Vector2(buttonWidth,buttonHeight),"Quit Game"));
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
		
		Fonts.get("Jungle").drawCenteredString("THE RISK FACTOR", 20, 100, 0xffffff);
	}
	
	@Override
	public void actionPerformed(GuiButton button)
	{
		if(button.id == 0)
		{
			GameStatistics.reset();
			
			MapGenerator gen = new MapGeneratorSimple();
			Remote2D.guiList.push(new GuiLoadMap(gen, 40, 40, 1337));
		} else if(button.id == 1)
			Remote2D.guiList.push(new GuiEditor());
		else if(button.id == 2)
			Remote2D.running = false;
		else if(button.id == 3)
			Remote2D.guiList.push(new GuiCredits());
	}
	
	@Override
	public void tick(int i, int j, int k)
	{
		super.tick(i, j, k);
		if(AudioSwitcher.getSoundMode() != SoundMode.TITLE)
			AudioSwitcher.setSoundMode(SoundMode.TITLE);
		
		if(Remote2D.getIntegerKeyboardList().contains(Keyboard.KEY_R))
		{
			Remote2D.guiList.push(new GuiRevive());
		}
	}
	
}
