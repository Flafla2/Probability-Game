package com.remote.probability.gui;

import org.lwjgl.input.Mouse;

import com.remote.remote2d.engine.Remote2D;
import com.remote.remote2d.engine.art.Fonts;
import com.remote.remote2d.engine.art.Renderer;
import com.remote.remote2d.engine.art.ResourceLoader;
import com.remote.remote2d.engine.art.Texture;
import com.remote.remote2d.engine.gui.Gui;
import com.remote.remote2d.engine.logic.Vector2;

public class GuiSlider extends Gui {
	
	public Vector2 pos;
	public Vector2 dim;
	private boolean mouseDown = false;
	private float offset;
	public float buttonWidth = 50;
	public double progress = 0.0;
	public boolean renderNumber = true;
	public int minNumber = 0;
	public int maxNumber = 100;
	
	public GuiSlider(Vector2 pos, Vector2 dim, float buttonWidth, double progress)
	{
		this.pos = pos;
		this.dim = dim;
		this.progress = progress;
		this.buttonWidth = buttonWidth;
	}

	@Override
	public void render(float interpolation) {
		Texture tex = ResourceLoader.getTexture("res/gui/slider.png");
		float side = dim.y*2/3;
		
		Renderer.drawRect(new Vector2(pos.x,pos.y),new Vector2(side,dim.y),new Vector2(0.5f,0),new Vector2(0.5f),tex,0xffffff,1);
		Renderer.drawRect(new Vector2(pos.x+side,pos.y),new Vector2(dim.x-side*2,dim.y),0x000000,1);
		Renderer.drawRect(new Vector2(pos.x+dim.x-side,pos.y),new Vector2(side,dim.y),new Vector2(0.5f),new Vector2(0.5f),tex,0xffffff,1);
		
		float bside = 0.21875f*buttonWidth;
		float bpos = (float) (pos.x+(dim.x-buttonWidth)*progress);
		
		Renderer.drawRect(new Vector2(bpos,pos.y),new Vector2(bside,dim.y),new Vector2(0,0.5f),new Vector2(0.21875f,0.5f),tex,0xffffff,1);
		Renderer.drawRect(new Vector2(bpos+bside,pos.y),new Vector2(buttonWidth-bside*2,dim.y),new Vector2(0.21875f,0.5f),new Vector2(0.0625f,0.5f),tex,0xffffff,1);
		Renderer.drawRect(new Vector2(bpos+buttonWidth-bside,pos.y),new Vector2(bside,dim.y),new Vector2(0.5f-0.21875f,0.5f),new Vector2(0.21875f,0.5f),tex,0xffffff,1);
		
		if(renderNumber)
		{
			String s = ""+(int)(progress*(maxNumber-minNumber)+minNumber);
			int[] size = Fonts.get("Pixel_Arial").getStringDim(s, 20);
			Fonts.get("Pixel_Arial").drawString(s, bpos+buttonWidth/2-size[0]/2, pos.y+dim.y/2-size[1]/2, 20, 0xffffff);
		}
	}

	@Override
	public void tick(int i, int j, int k) {
		Vector2 buttonPos = new Vector2((float) (pos.x+(dim.x-buttonWidth)*progress),pos.y);
		if(buttonPos.getColliderWithDim(new Vector2(buttonWidth,dim.y)).isPointInside(new Vector2(i,j)) && Remote2D.hasMouseBeenPressed())
		{
			mouseDown = true;
			offset = i-buttonPos.x;
		}
		
		if(mouseDown && Mouse.isButtonDown(0))
		{
			float xPos = i-offset+buttonWidth/2-pos.x;
			xPos = Math.max(buttonWidth/2, Math.min(dim.x-buttonWidth/2,xPos))-buttonWidth/2;
			progress = xPos/(dim.x-buttonWidth);
		}
		else if(!Mouse.isButtonDown(0))
			mouseDown = false;
	}
	
}
