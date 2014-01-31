package com.remote.probability.component;

import java.awt.Color;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import com.remote.probability.Game;
import com.remote.probability.gui.GuiDead;
import com.remote.probability.gui.GuiRevive;
import com.remote.probability.world.GameStatistics;
import com.remote.probability.world.Tile;
import com.remote.remote2d.engine.AudioHandler;
import com.remote.remote2d.engine.DisplayHandler;
import com.remote.remote2d.engine.Remote2D;
import com.remote.remote2d.engine.art.Animation;
import com.remote.remote2d.engine.art.Material.RenderType;
import com.remote.remote2d.engine.entity.Entity;
import com.remote.remote2d.engine.entity.component.Component;
import com.remote.remote2d.engine.logic.Vector2;

public class ComponentPlayer extends Component {
	
	public Animation northAnimation;
	public Animation southAnimation;
	public Animation eastAnimation;
	public Animation northEastAnimation;
	public Animation southEastAnimation;
	
	public String bulletPrefab;
	
	public Vector2 hitboxPos;
	public Vector2 hitboxDim;
	
	public int maxHealth = 100;
	
	private boolean flipped = false;
	private Direction direction = Direction.SOUTH;
	private long bulletTimer = 0;
	
	private Vector2 hitbackVelocity = new Vector2(0,0);
	private long lastHitTime = -1;
	
	private static final float WALK_SPEED = 10;
	private static final float DIAG_WALK_SPEED = (float) (WALK_SPEED*Game.ONE_OVER_SQRT2);
	private static final long BULLET_INTERVAL = 150;

	@Override
	public void init() {
		
	}

	@Override
	public void onEntitySpawn() {
		
	}

	@Override
	public void renderAfter(boolean editor, float interpolation) {
		if(editor)
			entity.pos.add(hitboxPos).getColliderWithDim(hitboxDim).drawCollider(0x00ff00);
	}

	@Override
	public void renderBefore(boolean arg0, float arg1) {
		float hitGradient = (float) (Math.min(500d,(double)(System.currentTimeMillis()-lastHitTime))/500f);
		entity.material.setColor(new Color(1,hitGradient,hitGradient,1));
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
		
		velocity.x += hitbackVelocity.x;
		velocity.y += hitbackVelocity.y;
		if(hitbackVelocity.x > 0.1)
			hitbackVelocity.x -= hitbackVelocity.x/4;
		else hitbackVelocity.x = 0;
		if(hitbackVelocity.y > 0.1)
			hitbackVelocity.y -= hitbackVelocity.y/4;
		else hitbackVelocity.y = 0;
		
		//Vector2 correction = entity.getMap().getCorrection(hitboxPos.add(entity.pos).add(velocity).getColliderWithDim(hitboxDim));
		if(!goingIntoWallTile(entity.pos.add(velocity)))
			entity.pos = entity.pos.add(velocity);
		if(entity.material.getAnimation() != null)
		{
			entity.material.getAnimation().flippedX = flipped;
			entity.material.getAnimation().animate = moving;
			if(!moving)
				entity.material.getAnimation().setCurrentFrame(0);
		}
		
		entity.getMap().camera.pos = entity.pos.add(entity.dim.divide(new Vector2(2)));
		
		if(Mouse.isButtonDown(0))
		{
			if(System.currentTimeMillis()-bulletTimer >= BULLET_INTERVAL)
			{
				boolean behindPlayer = direction == Direction.NORTH || direction == Direction.NORTHEAST || direction == Direction.NORTHWEST;
				int index = entity.getMap().getEntityList().indexOf(entity);
				Entity bullet = entity.getMap().getEntityList().instantiatePrefab(bulletPrefab,behindPlayer ? index : index+1);
				bullet.pos = entity.pos.copy().add(getBulletSpawnPosLocal(bullet.dim));
				bullet.updatePos();
				bulletTimer = System.currentTimeMillis();
				
				ComponentBullet comp = bullet.getComponentsOfType(ComponentBullet.class).get(0);
				comp.setDirection(direction);
				bulletTimer = System.currentTimeMillis();
				String sound = AudioHandler.playSound("res/sound/fx/player/shoot"+(Game.random.nextInt(5)+1)+".wav", false, false);
				AudioHandler.setVolume(sound, 0.5f);
			}
		} else
			bulletTimer = 0;
	}
	
	private boolean goingIntoWallTile(Vector2 pos)
	{
		byte[] ret = new byte[4];
		float x = (pos.x+hitboxPos.x)/((float)GameStatistics.tileSize);
		float y = (pos.y+hitboxPos.y)/((float)GameStatistics.tileSize);
		float xDim = hitboxDim.x/((float)GameStatistics.tileSize);
		float yDim = hitboxDim.y/((float)GameStatistics.tileSize);
		ret[0] = GameStatistics.map[(int)x][(int)y];
		ret[1] = GameStatistics.map[(int)(x+xDim)][(int)y];
		ret[2] = GameStatistics.map[(int)(x+xDim)][(int)(y+yDim)];
		ret[3] = GameStatistics.map[(int)x][(int)(y+yDim)];
		
		for(int i=0;i<ret.length;i++)
			if(!Tile.tiles[ret[i]].getWalkable())
				return true;
		
		return false;
	}
	
	private Vector2 getBulletSpawnPosLocal(Vector2 bulletDim)
	{
		Vector2 halfEntity = hitboxDim.divide(new Vector2(2));
		Vector2 halfBullet = bulletDim.divide(new Vector2(2));
		Vector2 centerBullet = halfEntity.subtract(halfBullet);
		switch(direction)
		{
		case SOUTH:
			return new Vector2(centerBullet.x+6,hitboxDim.y-12).add(hitboxPos);
		case NORTH:
			return new Vector2(centerBullet.x-2,-18).add(hitboxPos);
		case EAST:
			return new Vector2(hitboxDim.x,centerBullet.y+10).add(hitboxPos);
		case WEST:
			return new Vector2(-bulletDim.x,centerBullet.y+10).add(hitboxPos);
		case SOUTHEAST:
			return new Vector2(hitboxDim.x-10,hitboxDim.y-16).add(hitboxPos);
		case SOUTHWEST:
			return new Vector2(-bulletDim.x+10,hitboxDim.y-16).add(hitboxPos);
		case NORTHEAST:
			return new Vector2(hitboxDim.x-4,-bulletDim.y+38).add(hitboxPos);
		case NORTHWEST:
			return new Vector2(-bulletDim.x+4,-bulletDim.y+38).add(hitboxPos);
		default:
			return centerBullet;
		}
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
	
	public void hit(Vector2 normal, float velocity, int damage)
	{
		if(System.currentTimeMillis()-lastHitTime < 500)
			return;
		if(damage != 0)
		{
			hitbackVelocity.x = normal.x*30;
			hitbackVelocity.y = normal.y*30;
		
			lastHitTime = System.currentTimeMillis();
		}
		
		GameStatistics.playerHealth -= ((double)damage)/maxHealth;
		
		if(GameStatistics.playerHealth <= 0)
		{
			Remote2D.guiList.pop();
			if(GameStatistics.lives > 0)
				Remote2D.guiList.push(new GuiRevive());
			else
				Remote2D.guiList.push(new GuiDead());
		}
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
	
	public enum Direction
	{
		NORTH, NORTHEAST, EAST, SOUTHEAST, SOUTH, SOUTHWEST, WEST, NORTHWEST;
	}

}
