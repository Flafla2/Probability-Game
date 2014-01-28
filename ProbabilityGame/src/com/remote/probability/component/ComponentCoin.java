package com.remote.probability.component;

import com.remote.probability.world.GameStatistics;
import com.remote.remote2d.engine.art.Renderer;
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
	public Vector2 initialVelocity = new Vector2(0,0);
	
	@Override
	public void init() {
		
	}

	@Override
	public void onEntitySpawn() {
		
	}

	@Override
	public void renderAfter(boolean editor, float interpolation) {
		if(editor)	
		{
			Renderer.drawCircleHollow(entity.pos.add(entity.dim.divide(new Vector2(2))), attractDistance, 16, 0x00ff00, 1);
			Renderer.drawLine(entity.pos.add(entity.dim.divide(new Vector2(2))), entity.pos.add(entity.dim.divide(new Vector2(2))).add(new Vector2(maxAttractSpeed,0)), 0xff0000, 1);
		}
	}

	@Override
	public void renderBefore(boolean editor, float interpolation) {
		
	}

	@Override
	public void tick(int i, int j, int k) {
		Vector2 distVec = player.pos.subtract(entity.pos);
		float distSquared = distVec.x*distVec.x+distVec.y*distVec.y;
		Vector2 velocity = new Vector2(0);
		if(distSquared <= attractDistance*attractDistance)
		{
			float dist = (float) Math.sqrt(distSquared);
			float speed = maxAttractSpeed*(1-dist/attractDistance);
			double angle = Math.atan2(distVec.y, distVec.x);
			velocity.x = (float) (speed * Math.cos(angle));
			velocity.y = (float) (speed * Math.sin(angle));
			
			ComponentPlayer comp = player.getComponentsOfType(ComponentPlayer.class).get(0);
			ColliderBox playerCollider = player.pos.add(comp.hitboxPos).getColliderWithDim(comp.hitboxDim);
			ColliderBox thisCollider = entity.pos.getColliderWithDim(entity.dim);
			if(Collider.collides(playerCollider, thisCollider))
			{
				entity.getMap().getEntityList().removeEntityFromList(entity);
				GameStatistics.playerMoney += baseValue*GameStatistics.getMoneyMultiplier(GameStatistics.wave, GameStatistics.riskFactor);
			}
		}
		
		entity.pos = entity.pos.add(velocity.add(initialVelocity));
		
		if(initialVelocity.x != 0)
		{
			if(Math.abs(initialVelocity.x) < 5)
				initialVelocity.x = 0;
			else
				initialVelocity.x -= initialVelocity.x<0?-5:5;
		}
		if(initialVelocity.y != 0)
		{
			if(Math.abs(initialVelocity.y) < 5)
				initialVelocity.y = 0;
			else
				initialVelocity.y -= initialVelocity.y<0?-5:5;
		}
	}

	@Override
	public void apply() {
		
	}

}
