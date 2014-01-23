package com.remote.probability.component;

import java.awt.Color;

import com.remote.probability.Game;
import com.remote.probability.component.ComponentPlayer.Direction;
import com.remote.probability.world.GameStatistics;
import com.remote.probability.world.Tile;
import com.remote.remote2d.engine.art.Animation;
import com.remote.remote2d.engine.art.Fonts;
import com.remote.remote2d.engine.art.Renderer;
import com.remote.remote2d.engine.entity.Entity;
import com.remote.remote2d.engine.entity.EntityList;
import com.remote.remote2d.engine.entity.component.Component;
import com.remote.remote2d.engine.logic.Collider;
import com.remote.remote2d.engine.logic.ColliderBox;
import com.remote.remote2d.engine.logic.Vector2;

public class ComponentEnemy extends Component {
	
	public Entity player;
	
	public int detectionDistance;
	public double maxHealth = 100;
	public int attackFrequency = 500;
	public int damage = 5;
	
	public Animation northAnimation;
	public Animation southAnimation;
	public Animation eastAnimation;
		
	public Vector2 hitboxPos;
	public Vector2 hitboxDim;
	
	private boolean flipped = false;
	private boolean moving = false;
	private Direction direction = Direction.SOUTH;
	private long nextWanderCalc = -1;
	private long lastAttackTime = -1;
	private Vector2 hitbackVelocity = new Vector2(0,0);
	private long lastHitTime = -1;
	private double health = 1.0;
	private boolean detected = false;
	
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
		
		if(System.currentTimeMillis()-lastHitTime < 5000)
		{
			Vector2 pos = new Vector2(entity.pos.x+hitboxPos.x+hitboxDim.x/2-50,entity.pos.y+hitboxPos.y-20);
			Renderer.drawRect(pos, new Vector2(100,10), 0x880000, 1);
			Renderer.drawRect(pos, new Vector2((float)health*100,10), 0xff0000, 1);
		}
		
		if(detected)
			Fonts.get("Arial").drawString("!", entity.pos.x+entity.dim.x/2, entity.pos.y-20, 20, 0x00ff00);
	}

	@Override
	public void renderBefore(boolean arg0, float arg1) {
		float hitGradient = (float) (Math.min(500d,(double)(System.currentTimeMillis()-lastHitTime))/500f);
		entity.material.setColor(new Color(1,hitGradient,hitGradient,1));
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
		if(hitbackVelocity.x > 0.1)
			hitbackVelocity.x -= hitbackVelocity.x/4;
		else hitbackVelocity.x = 0;
		if(hitbackVelocity.y > 0.1)
			hitbackVelocity.y -= hitbackVelocity.y/4;
		else hitbackVelocity.y = 0;
		
		
		if(player != null)
		{
			boolean canAttack = System.currentTimeMillis()-lastAttackTime > attackFrequency;
			ComponentPlayer comp = player.getComponentsOfType(ComponentPlayer.class).get(0);
			ColliderBox playerHitbox = player.pos.add(comp.hitboxPos).getColliderWithDim(comp.hitboxDim);
			ColliderBox enemyHitbox = entity.pos.add(hitboxPos).getColliderWithDim(hitboxDim);
			if(Collider.collides(playerHitbox,enemyHitbox))
			{
				comp.hit(velocity.normalize(), canAttack ? damage : 0);
				lastAttackTime = System.currentTimeMillis();
			}
		}
		
		if(!goingIntoWallTile(entity.pos.add(velocity)))
			entity.pos = entity.pos.add(velocity);
		else if(!detected)
		{
			moving = false;
			nextWanderCalc = System.currentTimeMillis()+Game.random.nextInt(1000);
		}
		
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
	
	private boolean tickAI()
	{
		final long time = System.currentTimeMillis();
		if(player == null)
			return false;
		
		final Vector2 playerCenter = 	new Vector2(player.pos.x+player.dim.x/2,player.pos.y+player.dim.y/2);
		final Vector2 enemyCenter =		new Vector2(entity.pos.x+entity.dim.x/2,entity.pos.y+entity.dim.y/2);
		final Vector2 distanceVector = playerCenter.subtract(enemyCenter).abs();
		final int distanceSquared = (int) (distanceVector.x*distanceVector.x+distanceVector.y*distanceVector.y);
		detected = distanceSquared < detectionDistance*detectionDistance;
		
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
			nextWanderCalc = -1;
			final Vector2 noAbsDistance = playerCenter.subtract(enemyCenter);
			if(distanceVector.x-10 > distanceVector.y)
			{
				if(noAbsDistance.x > 0)
					direction = Direction.EAST;
				else
					direction = Direction.WEST;
			} else if(distanceVector.x < distanceVector.y-10)
			{
				if(noAbsDistance.y > 0)
					direction = Direction.SOUTH;
				else
					direction = Direction.NORTH;
			}
		}
		
		return detected;
	}
	
	public void hit(Vector2 normal, int damage)
	{
		hitbackVelocity.x = normal.x*20;
		hitbackVelocity.y = normal.y*20;
		lastHitTime = System.currentTimeMillis();
		
		health -= ((double)damage)/maxHealth;
		if(health <= 0)
		{
			EntityList list = entity.getMap().getEntityList();
			Entity explosion = list.instantiatePrefab("res/entity/effects/explosion_big.entity.xml", list.indexOf(entity));
			explosion.pos.x = entity.pos.x+entity.dim.x/2-explosion.dim.x/2;
			explosion.pos.y = entity.pos.y+entity.dim.y-explosion.dim.y;
			
			entity.getMap().getEntityList().removeEntityFromList(entity);
		}
	}
	
	public double getHealth()
	{
		return health;
	}
	
	public void setHealth(double health)
	{
		this.health = health;
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
