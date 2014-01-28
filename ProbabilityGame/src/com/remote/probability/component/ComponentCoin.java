package com.remote.probability.component;

import com.remote.remote2d.engine.entity.Entity;
import com.remote.remote2d.engine.entity.component.Component;
import com.remote.remote2d.engine.logic.Collider;
import com.remote.remote2d.engine.logic.ColliderBox;
import com.remote.remote2d.engine.logic.Vector2;

public class ComponentCoin extends Component {
	
	public Entity player;
	public int attractDistance;
	public int maxAttractSpeed = 20;
	public int baseValue = 5;

	@Override
	public void init() {
		
	}

	@Override
	public void onEntitySpawn() {
		
	}

	@Override
	public void renderAfter(boolean editor, float interpolation) {
		
	}

	@Override
	public void renderBefore(boolean editor, float interpolation) {
		
	}

	@Override
	public void tick(int i, int j, int k) {
		Vector2 distVec = player.pos.subtract(entity.pos).abs();
		float distSquared = distVec.x*distVec.x+distVec.y*distVec.y;
		if(distSquared <= attractDistance*attractDistance)
		{
			float dist = (float) Math.sqrt(distSquared);
			float speed = maxAttractSpeed*(1-dist/attractDistance);
			double angle = Math.atan2(distVec.y, distVec.x);
			Vector2 velocity = new Vector2(0);
			velocity.x = (float) (speed * Math.cos(angle));
			velocity.y = (float) (speed * Math.sin(angle));
			
			entity.pos = entity.pos.add(velocity);
			
			ComponentPlayer comp = player.getComponentsOfType(ComponentPlayer.class).get(0);
			ColliderBox playerCollider = player.pos.add(comp.hitboxPos).getColliderWithDim(comp.hitboxDim);
			ColliderBox thisCollider = entity.pos.getColliderWithDim(entity.dim);
			if(Collider.collides(playerCollider, thisCollider))
			{
				entity.getMap().getEntityList().removeEntityFromList(entity);
			}
		}
	}

	@Override
	public void apply() {
		
	}

}
