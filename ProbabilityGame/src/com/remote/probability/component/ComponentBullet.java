package com.remote.probability.component;

import java.util.ArrayList;

import com.remote.probability.Game;
import com.remote.probability.component.ComponentPlayer.Direction;
import com.remote.probability.world.GameStatistics;
import com.remote.remote2d.engine.entity.Entity;
import com.remote.remote2d.engine.entity.component.Component;
import com.remote.remote2d.engine.logic.Collider;
import com.remote.remote2d.engine.logic.ColliderBox;
import com.remote.remote2d.engine.logic.Vector2;

public class ComponentBullet extends Component {
	
	private static final float BULLET_SPEED = 20;
	private static final float BULLET_SPEED_DIAG = (float) (BULLET_SPEED*Game.ONE_OVER_SQRT2);
	private static final long DESPAWN_TIME = 5000;
	
	private Direction direction = Direction.NORTHEAST;
	private long despawnTimer = -1;
	private boolean corrected = false;
	private int bounces = 0;

	@Override
	public void init() {
		entity.material.setUVDim(new Vector2(1f/8f,1));
	}

	@Override
	public void onEntitySpawn() {
		
	}

	@Override
	public void renderAfter(boolean editor, float interpolation) {
		if(editor)
			getCollider().drawCollider(0x00ff00);
	}

	@Override
	public void renderBefore(boolean editor, float interpolation) {
	}

	@Override
	public void tick(int i, int j, int k) {
		if(despawnTimer == -1)
			despawnTimer = System.currentTimeMillis();
		
		Vector2 movement = new Vector2(0,0);
		switch(direction)
		{
		case SOUTH:
			movement.y += BULLET_SPEED;
			break;
		case SOUTHWEST:
			movement.y += BULLET_SPEED_DIAG;
			movement.x -= BULLET_SPEED_DIAG;
			break;
		case WEST:
			movement.x -= BULLET_SPEED;
			break;
		case NORTHWEST:
			movement.x -= BULLET_SPEED_DIAG;
			movement.y -= BULLET_SPEED_DIAG;
			break;
		case NORTH:
			movement.y -= BULLET_SPEED;
			break;
		case NORTHEAST:
			movement.x += BULLET_SPEED_DIAG;
			movement.y -= BULLET_SPEED_DIAG;
			break;
		case EAST:
			movement.x += BULLET_SPEED;
			break;
		case SOUTHEAST:
			movement.x += BULLET_SPEED_DIAG;
			movement.y += BULLET_SPEED_DIAG;
			break;
		}
		
		entity.pos = entity.pos.add(movement);
		
		boolean hits = entity.getMap().collidesWithMap(getCollider());
		if(hits)
		{
			Vector2 correction = entity.getMap().getCorrection(getCollider());
			if(corrected)
			{
				if(bounces < GameStatistics.bulletBounceNum)
				{
					bounces++;
					if(correction.x != 0) flipHorizontal();
					if(correction.y != 0) flipVertical();
					corrected = false;
					entity.pos = entity.pos.subtract(movement);
				} else
				{
					entity.getMap().getEntityList().removeEntityFromList(entity);
					return;
				}
			} else
			{
				entity.pos = entity.pos.add(correction);
				if(!correction.equals(new Vector2(0)))
					corrected = true;
			}
		}
				
		if((despawnTimer != -1 && System.currentTimeMillis()-despawnTimer >= DESPAWN_TIME))
			entity.getMap().getEntityList().removeEntityFromList(entity);
		
		ColliderBox collider = getCollider();
		for(int x = 0;x<entity.getMap().getEntityList().size();x++)
		{
			Entity e = entity.getMap().getEntityList().get(x);
			
			ArrayList<ComponentEnemy> comps = e.getComponentsOfType(ComponentEnemy.class);
			for(ComponentEnemy comp : comps)
			{
				if(Collider.collides(collider,comp.hitboxPos.add(e.pos).getColliderWithDim(comp.hitboxDim)))
				{
					comp.hit(movement.normalize(),(int) (GameStatistics.playerDamageModifier*10d));
					entity.getMap().getEntityList().removeEntityFromList(entity);
					return;
				}
			}
		}
	}
	
	private void flipHorizontal() {
		switch(direction)
		{
		case NORTH:
		case SOUTH:
			break;
		case EAST:
			setDirection(Direction.WEST);
			break;
		case WEST:
			setDirection(Direction.EAST);
			break;
		case NORTHEAST:
			setDirection(Direction.NORTHWEST);
			break;
		case NORTHWEST:
			setDirection(Direction.NORTHEAST);
			break;
		case SOUTHEAST:
			setDirection(Direction.SOUTHWEST);
			break;
		case SOUTHWEST:
			setDirection(Direction.SOUTHEAST);
			break;
		}
	}
	
	private void flipVertical() {
		switch(direction)
		{
		case EAST:
		case WEST:
			break;
		case NORTH:
			setDirection(Direction.SOUTH);
			break;
		case SOUTH:
			setDirection(Direction.NORTH);
			break;
		case NORTHEAST:
			setDirection(Direction.SOUTHEAST);
			break;
		case NORTHWEST:
			setDirection(Direction.SOUTHWEST);
			break;
		case SOUTHEAST:
			setDirection(Direction.NORTHEAST);
			break;
		case SOUTHWEST:
			setDirection(Direction.NORTHWEST);
			break;
		}
	}

	private ColliderBox getCollider()
	{
		Vector2 cPos = new Vector2(0,0);
		Vector2 offset = new Vector2(0,0);
		
		switch(direction)
		{
		case NORTH:
		case SOUTH:
			cPos.x = 12;
			cPos.y = 2;
			break;
		case EAST:
		case WEST:
			cPos.y = 12;
			cPos.x = 2;
			break;
		case NORTHEAST:
			offset.x = 6;
			offset.y = -6;
			cPos.x = 10;
			cPos.y = 10;
			break;
		case SOUTHEAST:
			offset.x = 6;
			offset.y = 6;
			cPos.x = 10;
			cPos.y = 10;
			break;
		case NORTHWEST:
			offset.x = -6;
			offset.y = -6;
			cPos.x = 10;
			cPos.y = 10;
			break;
		case SOUTHWEST:
			offset.x = -6;
			offset.y = 6;
			cPos.x = 10;
			cPos.y = 10;
			break;
		}
		
		Vector2 cDim = entity.dim.subtract(cPos.multiply(new Vector2(2)));
		
		return cPos.add(entity.pos).add(offset).getColliderWithDim(cDim);
	}

	@Override
	public void apply() {
		
	}

	public Direction getDirection() {
		return direction;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
		
		float spriteIndex = 0;
		switch(direction)
		{
		case SOUTH:
			spriteIndex = 0;
			break;
		case SOUTHWEST:
			spriteIndex = 1;
			break;
		case WEST:
			spriteIndex = 2;
			break;
		case NORTHWEST:
			spriteIndex = 3;
			break;
		case NORTH:
			spriteIndex = 4;
			break;
		case NORTHEAST:
			spriteIndex = 5;
			break;
		case EAST:
			spriteIndex = 6;
			break;
		case SOUTHEAST:
			spriteIndex = 7;
			break;
		}
		
		entity.material.setUVPos(new Vector2(spriteIndex/8f,0));
	}

}
