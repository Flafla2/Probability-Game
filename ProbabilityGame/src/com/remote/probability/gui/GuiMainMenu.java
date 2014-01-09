package com.remote.probability.gui;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import com.remote.probability.world.MapGenerator;
import com.remote.probability.world.MapGeneratorSimple;
import com.remote.remote2d.editor.GuiEditor;
import com.remote.remote2d.engine.Remote2D;
import com.remote.remote2d.engine.StretchType;
import com.remote.remote2d.engine.art.Fonts;
import com.remote.remote2d.engine.gui.Gui;
import com.remote.remote2d.engine.gui.GuiButton;
import com.remote.remote2d.engine.gui.GuiMenu;
import com.remote.remote2d.engine.io.R2DFileUtility;
import com.remote.remote2d.engine.logic.Vector2;

public class GuiMainMenu extends GuiMenu{
	
	public static final int GRADIENT_TOP = 0xffff66;
	public static final int GRADIENT_BOTTOM = 0xaaaa00;
	
	private String message = "";
	private long lastMessageTime = -1;
	private boolean debug = false;
	
	public GuiMainMenu()
	{
		super();
	}
	
	@Override
	public void initGui()
	{
		final int buttonWidth = 500;
		final int buttonHeight = 40;
		final int buttonx = screenWidth()-buttonWidth;
		final int numButtons = 3;
		int buttonY = screenHeight()/2-(buttonHeight*numButtons+10*(numButtons-1))/2;
		
		buttonList.clear();
		buttonList.add(new GuiButton(0,new Vector2(buttonx,buttonY),new Vector2(buttonWidth,buttonHeight),"Play Game"));
		buttonY += 50;
		buttonList.add(new GuiButton(1,new Vector2(buttonx,buttonY),new Vector2(buttonWidth,buttonHeight),"Open Editor (Buggy as all hell!)"));
		buttonY += 50;
		buttonList.add(new GuiButton(2,new Vector2(buttonx,buttonY),new Vector2(buttonWidth,buttonHeight),"Quit Game"));
		
		if(debug)
		{
			buttonList.add(new GuiButton(3,new Vector2(0,screenHeight()-100),new Vector2(300,40),"Convert all to XML"));
		}
	}
	
	@Override
	public void renderBackground(float interpolation)
	{
		super.renderBackground(interpolation);
		
		//EXTREMELY MESSY CODE for the gradient
		float[] top = Gui.getRGB(GRADIENT_TOP);
		float[] bot = Gui.getRGB(GRADIENT_BOTTOM);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glBegin(GL11.GL_QUADS);
			GL11.glColor4f(top[0],top[1],top[2],1);
			GL11.glVertex2f(0, 0);
			GL11.glVertex2f(screenWidth(), 0);
			GL11.glColor4f(bot[0],bot[1],bot[2],1);
			GL11.glVertex2f(screenWidth(), screenHeight());
			GL11.glVertex2f(0, screenHeight());
			GL11.glColor4f(1,1,1,1);
		GL11.glEnd();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
	}
	
	@Override
	public void render(float interpolation)
	{
		super.render(interpolation);
		
		Fonts.get("Jungle").drawString("WORKING TITLE", 10, screenHeight()/2-50, 100, 0x000000);
		
		if(System.currentTimeMillis()-lastMessageTime < 8000)
			Fonts.get("Arial").drawCenteredString(message, 200, 30, 0x000000);
	}
	
	@Override
	public void actionPerformed(GuiButton button)
	{
		if(button.id == 0)
		{
//			message = "Hi, this is part of the game's many DRM features, which renders the game completely "
//					+ "unplayable in case of piracy!  Nice try, pirate!";
//			lastMessageTime = System.currentTimeMillis();
			MapGenerator gen = new MapGeneratorSimple();
			Remote2D.guiList.push(new GuiLoadMap(gen, 100, 100, 1337));
		} else if(button.id == 1)
			Remote2D.guiList.push(new GuiEditor());
		else if(button.id == 2)
			Remote2D.running = false;
		else if(button.id == 3)
			R2DFileUtility.convertFolderToXML("res", true);
	}
	
	@Override
	public void tick(int i, int j, int k)
	{
		super.tick(i, j, k);
		if(Remote2D.getIntegerKeyboardList().contains(Keyboard.KEY_D))
		{
			debug = !debug;
			initGui();
		}
	}
	
	@Override
	public StretchType getOverrideStretchType()
	{
		return StretchType.NONE;
	}
	
}
