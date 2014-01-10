package com.remote.probability.component;

import java.util.Random;

import org.lwjgl.input.Keyboard;

import com.remote.probability.gui.GuiInGame;
import com.remote.probability.gui.GuiInGame.DebugModule;
import com.remote.remote2d.engine.DisplayHandler;
import com.remote.remote2d.engine.art.Animation;
import com.remote.remote2d.engine.art.Renderer;
import com.remote.remote2d.engine.art.Material.RenderType;
import com.remote.remote2d.engine.entity.component.Component;
import com.remote.remote2d.engine.logic.Vector2;

public class ComponentPlayer extends Component {
	
	public Animation northAnimation;
	public Animation southAnimation;
	public Animation eastAnimation;
	public Animation northEastAnimation;
	public Animation southEastAnimation;
	
	private boolean flipped = false;
	private Direction direction = Direction.SOUTH;
	
	private static final float WALK_SPEED = 10;
	private static final float DIAG_WALK_SPEED = (float)Math.sqrt(WALK_SPEED*WALK_SPEED/2);

	@Override
	public void init() {
		
	}

	@Override
	public void onEntitySpawn() {
		
	}

	@Override
	public void renderAfter(boolean arg0, float arg1) {
		
	}

	@Override
	public void renderBefore(boolean arg0, float arg1) {
		
	}
	
	@Override
	public void tick(int i, int j, int k) {
		Vector2 mouseToCenter = new Vector2(i,j).subtract(DisplayHandler.getDimensions().divide(new Vector2(2)));
		double angle = Math.toDegrees(Math.atan2(mouseToCenter.y, mouseToCenter.x));
				
		if(entity.material.getRenderType() != RenderType.ANIM)
			entity.material.setRenderType(RenderType.ANIM);
		
		direction = getDirectionWithAngle(angle);
		
		Animation target = updateTargetAnim();
		
		if(entity.material.getAnimation() == null || !entity.material.getAnimation().equals(target))
			entity.material.setAnimation(target);
		
		boolean w = Keyboard.isKeyDown(Keyboard.KEY_W);
		boolean s = Keyboard.isKeyDown(Keyboard.KEY_S);
		boolean a = Keyboard.isKeyDown(Keyboard.KEY_A);
		boolean d = Keyboard.isKeyDown(Keyboard.KEY_D);
		boolean moving = w || s || a || d;
		
		Vector2 velocity = new Vector2(0,0);
		
		
		if(w && !s && !a && !d)
			velocity.y = -WALK_SPEED;
		else if(!w && s && !a && !d)
			velocity.y = WALK_SPEED;
		else if(!w && !s && a && !d)
			velocity.x = -WALK_SPEED;
		else if(!w && !s && !a && d)
			velocity.x = WALK_SPEED;
		else if(moving)
		{
			if(w) velocity.y = -DIAG_WALK_SPEED;
			if(s) velocity.y = DIAG_WALK_SPEED;
			if(a) velocity.x = -DIAG_WALK_SPEED;
			if(d) velocity.x = DIAG_WALK_SPEED;
		}
		
		entity.pos = entity.pos.add(velocity);		
		if(entity.material.getAnimation() != null)
		{
			entity.material.getAnimation().flippedX = flipped;
			entity.material.getAnimation().animate = moving;
			if(!moving)
				entity.material.getAnimation().setCurrentFrame(0);
		}
		
		entity.getMap().camera.pos = entity.pos.add(entity.dim.divide(new Vector2(2)));
		entity.getMap().camera.updatePos();
	}

	@Override
	public void apply() {
		
	}
	
	private Animation updateTargetAnim()
	{
		Animation target = null;
		switch(direction)
		{
		case EAST:
			target = eastAnimation;
			flipped = false;
			break;
		case WEST:
			target = eastAnimation;
			flipped = true;
			break;
		case NORTH:
			target = northAnimation;
			flipped = false;
			break;
		case SOUTH:
			target = southAnimation;
			flipped = false;
			break;
		case NORTHEAST:
			target = northEastAnimation;
			flipped = false;
			break;
		case NORTHWEST:
			target = northEastAnimation;
			flipped = true;
			break;
		case SOUTHEAST:
			target = southEastAnimation;
			flipped = false;
			break;
		case SOUTHWEST:
			target = southEastAnimation;
			flipped = true;
			break;
		}
		return target;
	}
	
	private Direction getDirectionWithAngle(double angle)
	{
		if(angle > -22.5 && angle <= 22.5)
			return Direction.EAST;
		else if(angle > 22.5 && angle <= 67.5)
			return Direction.SOUTHEAST;
		else if(angle > 67.5 && angle <= 112.5)
			return Direction.SOUTH;
		else if(angle > 112.5 && angle <= 157.5)
			return Direction.SOUTHWEST;
		else if((angle > 157.5 && angle <= 180) || (angle < -157.5 && angle >= -180))
			return Direction.WEST;
		else if(angle < -112.5 && angle >= -157.5)
			return Direction.NORTHWEST;
		else if(angle < -67.5 && angle >= -112.5)
			return Direction.NORTH;
		else if(angle < -22.5 && angle >= -67.5)
			return Direction.NORTHEAST;
		
		return Direction.SOUTH;
	}
	
	private enum Direction
	{
		NORTH, NORTHEAST, EAST, SOUTHEAST, SOUTH, SOUTHWEST, WEST, NORTHWEST;
	}

}
