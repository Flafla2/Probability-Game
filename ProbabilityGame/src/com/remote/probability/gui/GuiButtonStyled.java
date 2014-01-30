package com.remote.probability.gui;

import com.remote.remote2d.engine.Remote2D;
import com.remote.remote2d.engine.art.Fonts;
import com.remote.remote2d.engine.art.Renderer;
import com.remote.remote2d.engine.art.ResourceLoader;
import com.remote.remote2d.engine.art.Texture;
import com.remote.remote2d.engine.gui.GuiButton;
import com.remote.remote2d.engine.logic.Vector2;

public class GuiButtonStyled extends GuiButton {
	
	public static final Vector2 headerDim = new Vector2(16,24);
	public Vector2 pos;//shady hack
	public Vector2 dim;//shady hack

	public GuiButtonStyled(int id, Vector2 pos, Vector2 dim, String text) {
		super(id, pos, dim, text);
		this.pos = pos;
		this.dim = dim;
	}
	
	@Override
	public void render(float interpolation)
	{
		Texture tex = ResourceLoader.getTexture("res/gui/button.png");
		final Vector2 imageDim = new Vector2(tex.getImage().getWidth(),tex.getImage().getHeight());
		final Vector2 glHeaderDim = headerDim.divide(imageDim);
		final float ratio = glHeaderDim.x/glHeaderDim.y;
		final Vector2 trueHeaderDim = new Vector2(dim.y*ratio, dim.y);
		float mode = (pos.getColliderWithDim(dim).isPointInside(Remote2D.getMouseCoords()) ? 1 : 0)/3f;
		Renderer.drawRect(pos, 												trueHeaderDim,
				new Vector2(0,mode), 				glHeaderDim, 									tex, 0xffffff, 1);
		Renderer.drawRect(new Vector2(pos.x+trueHeaderDim.x,pos.y), 		new Vector2(dim.x-trueHeaderDim.x*2,dim.y), 
				new Vector2(glHeaderDim.x,mode),	new Vector2(1-glHeaderDim.x*2,glHeaderDim.y), 	tex, 0xffffff, 1);
		Renderer.drawRect(new Vector2(pos.x+dim.x-trueHeaderDim.x,pos.y), 	trueHeaderDim, 								
				new Vector2(1-glHeaderDim.x,mode), 	glHeaderDim, 									tex, 0xffffff, 1);
		
		int[] strdim = Fonts.get("Pixel_Arial").getStringDim(text, 16);
		Fonts.get("Pixel_Arial").drawString(text, pos.x+dim.x/2-strdim[0]/2, pos.y+dim.y/2-strdim[1]/2, 16, 0xffffff);
	}

}
