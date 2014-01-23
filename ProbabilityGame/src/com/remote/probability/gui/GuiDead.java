package com.remote.probability.gui;

import org.lwjgl.opengl.GL11;

import com.remote.remote2d.engine.Remote2D;
import com.remote.remote2d.engine.art.Fonts;
import com.remote.remote2d.engine.gui.Gui;
import com.remote.remote2d.engine.gui.GuiButton;
import com.remote.remote2d.engine.gui.GuiMenu;
import com.remote.remote2d.engine.logic.Vector2;

public class GuiDead extends GuiMenu {
	
	public static final int GRADIENT_TOP = 0xff0000;
	public static final int GRADIENT_BOTTOM = 0x880000;
	
	@Override
	public void renderBackground(float interpolation)
	{
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
		
		Fonts.get("Jungle").drawCenteredString("DED", 40, 70, 0xffffff);
	}
	
	@Override
	public void initGui()
	{
		buttonList.clear();
		buttonList.add(new GuiButton(0,new Vector2(screenWidth()/2-150, screenHeight()-80),new Vector2(300,40),"Back to Main Menu"));
	}
	
	@Override
	public void actionPerformed(GuiButton button)
	{
		if(button.id == 0)
			Remote2D.guiList.pop();
	}
	
}
