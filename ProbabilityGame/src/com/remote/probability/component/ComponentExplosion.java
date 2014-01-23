package com.remote.probability.component;

import com.remote.remote2d.engine.entity.component.Component;

public class ComponentExplosion extends Component {
	
	private long dieTime = -1;

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
		if(System.currentTimeMillis() >= dieTime && dieTime != -1)
			entity.material.setAlpha(0);
	}

	@Override
	public void tick(int i, int j, int k) {
		if(dieTime == -1)
			dieTime = System.currentTimeMillis()+(long)entity.material.getAnimation().getFramelength()*(long)entity.material.getAnimation().getFrames().x*(long)entity.material.getAnimation().getFrames().y;
			
		else if(System.currentTimeMillis() >= dieTime)
			entity.getMap().getEntityList().removeEntityFromList(entity);
	}

	@Override
	public void apply() {
		
	}

}
