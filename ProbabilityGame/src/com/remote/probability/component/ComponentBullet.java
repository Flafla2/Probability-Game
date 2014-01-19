package com.remote.probability.component;

import java.util.ArrayList;

import com.remote.probability.Game;
import com.remote.probability.component.ComponentPlayer.Direction;
import com.remote.remote2d.engine.entity.Entity;
import com.remote.remote2d.engine.entity.component.Component;
import com.remote.remote2d.engine.logic.Collider;
import com.remote.remote2d.engine.logic.ColliderBox;
import com.remote.remote2d.engine.logic.Vector2;

public class ComponentBullet extends Component {
	
	private static final float BULLET_SPEED = 20;
	private static final float BULLET_SPEED_DIAG = (float) (BULLET_SPEED*Game.ONE_OVER_SQRT2);
	private static final long DESPAWN_TIME = 5000;
	
	private Direction direction = Direction.NORTH;
	private long despawnTimer = -1;

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
		
		//TODO: Work out/optimize slow collision
		boolean hits = entity.getMap().collidesWithMap(getCollider());
		
		if((despawnTimer != -1 && System.currentTimeMillis()-despawnTimer >= DESPAWN_TIME) || hits)
		{
			entity.getMap().getEntityList().removeEntityFromList(entity);
		}
		
		for(int x = 0;x<entity.getMap().getEntityList().size();x++)
		{
			Entity e = entity.getMap().getEntityList().get(x);
			Vector2 distance = e.pos.subtract(entity.pos).abs();
			int distSquared = (int) (distance.x*distance.x+distance.y*distance.y);
			if(distSquared > 128) //TODO: Change to calculated distance
				continue;
			
			ArrayList<ComponentEnemy> comps = e.getComponentsOfType(ComponentEnemy.class);
			for(ComponentEnemy comp : comps)
			{
				if(Collider.collides(getCollider(),comp.hitboxPos.add(e.pos).getColliderWithDim(comp.hitboxDim)))
					comp.hit(movement);
			}
		}
	}
	
	private ColliderBox getCollider()
	{
		Vector2 cPos = new Vector2(0,0);
		
		switch(direction)
		{
		case NORTH:
		case SOUTH:
			cPos.x = 4;
			break;
		case EAST:
		case WEST:
			cPos.y = 4;
			break;
		default:
			cPos.x = 2;
			cPos.y = 2;
			break;
		}
		
		Vector2 cDim = entity.dim.subtract(cPos.multiply(new Vector2(2)));
		
		return cPos.add(entity.pos).getColliderWithDim(cDim);
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
