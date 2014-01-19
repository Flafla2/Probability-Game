package com.remote.probability.component;

import java.awt.Color;

import com.esotericsoftware.minlog.Log;
import com.remote.probability.Game;
import com.remote.probability.component.ComponentPlayer.Direction;
import com.remote.probability.world.GameStatistics;
import com.remote.probability.world.Tile;
import com.remote.remote2d.engine.art.Animation;
import com.remote.remote2d.engine.entity.Entity;
import com.remote.remote2d.engine.entity.component.Component;
import com.remote.remote2d.engine.logic.Collider;
import com.remote.remote2d.engine.logic.ColliderBox;
import com.remote.remote2d.engine.logic.Vector2;

public class ComponentEnemy extends Component {
	
	public Entity player;
	
	public int detectionDistance;

	public Animation northAnimation;
	public Animation southAnimation;
	public Animation eastAnimation;
		
	public Vector2 hitboxPos;
	public Vector2 hitboxDim;
	
	private boolean flipped = false;
	private boolean moving = false;
	private Direction direction = Direction.SOUTH;
	private long nextWanderCalc = -1;
	private Vector2 hitbackVelocity = new Vector2(0,0);
	private long lastHitTime = -1;
	
	public float walkSpeed = 10;
	
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
		float fade = 1-((float)Math.max(500,lastHitTime-System.currentTimeMillis()))/500f;
		entity.material.setColor(new Color(1,fade,fade,1));
	}
	
	@Override
	public void tick(int i, int j, int k) {
		tickAI();
		
		Animation target = updateTargetAnim();
		
		if(entity.material.getAnimation() == null || !entity.material.getAnimation().equals(target))
			entity.material.setAnimation(target);
		
		Vector2 velocity = new Vector2(0,0);
		
		if(moving)
		{
			switch(direction)
			{
			case NORTH:
				velocity.y = -walkSpeed;
				break;
			case SOUTH:
				velocity.y = walkSpeed;
				break;
			case WEST:
				velocity.x = -walkSpeed;
				break;
			case EAST:
				velocity.x = walkSpeed;
				break;
			default:
				break;
			}
		}
		
		velocity.x += hitbackVelocity.x;
		velocity.y += hitbackVelocity.y;
		hitbackVelocity.x = Math.max(hitbackVelocity.x-5, 0);
		hitbackVelocity.y = Math.max(hitbackVelocity.y-5, 0);
		
		if(player != null)
		{
			ComponentPlayer comp = player.getComponentsOfType(ComponentPlayer.class).get(0);
			ColliderBox playerHitbox = player.pos.add(comp.hitboxPos).getColliderWithDim(comp.hitboxDim);
			ColliderBox enemyHitbox = entity.pos.add(hitboxPos).getColliderWithDim(hitboxDim);
			if(Collider.collides(playerHitbox,enemyHitbox))
			{
				//TODO: Add player hit recoil
			}
		}
		
		Vector2 correction = new Vector2(0,0);
		if(goingIntoWallTile(entity.pos.add(velocity)))
			correction = entity.getMap().getCorrection(hitboxPos.add(entity.pos).getColliderWithDim(hitboxDim), velocity);
		entity.pos = entity.pos.add(velocity.add(correction));		
		if(entity.material.getAnimation() != null)
		{
			entity.material.getAnimation().flippedX = flipped;
			entity.material.getAnimation().animate = moving;
			if(!moving)
				entity.material.getAnimation().setCurrentFrame(0);
		}
	}

	@Override
	public void apply() {
		
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
	
	private void tickAI()
	{
		final long time = System.currentTimeMillis();
		if(player == null)
			return;
		
		final Vector2 playerCenter = 	new Vector2(player.pos.x+player.dim.x/2,player.pos.y+player.dim.y/2);
		final Vector2 enemyCenter =		new Vector2(entity.pos.x+entity.dim.x/2,entity.pos.y+entity.dim.y/2);
		final Vector2 distanceVector = playerCenter.subtract(enemyCenter).abs();
		final int distanceSquared = (int) (distanceVector.x*distanceVector.x+distanceVector.y*distanceVector.y);
		final boolean detected = distanceSquared < detectionDistance*detectionDistance;
		
		if(!detected && time >= nextWanderCalc)
		{
			int randDir = Game.random.nextInt(8);
					if(randDir == 0) direction = Direction.NORTH;
			else 	if(randDir == 1) direction = Direction.SOUTH;
			else 	if(randDir == 2) direction = Direction.EAST;
			else 	if(randDir == 3) direction = Direction.WEST;
			
			moving = Game.random.nextInt(5) != 0;
			nextWanderCalc = System.currentTimeMillis()+Game.random.nextInt(10000)-5000;
		} else if(detected)
		{
			moving = true;
			if(distanceVector.x > distanceVector.y)
			{
				if(distanceVector.x > 0)
					direction = Direction.EAST;
				else
					direction = Direction.WEST;
			} else
			{
				if(distanceVector.y > 0)
					direction = Direction.SOUTH;
				else
					direction = Direction.NORTH;
			}
		}
	}
	
	public void hit(Vector2 velocity)
	{
		hitbackVelocity.x = velocity.x;
		hitbackVelocity.y = velocity.y;
		lastHitTime = System.currentTimeMillis();
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
		default:
			break;
		}
		return target;
	}

}
