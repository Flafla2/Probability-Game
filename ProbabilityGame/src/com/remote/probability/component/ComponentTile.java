package com.remote.probability.component;

import com.remote.probability.world.GameStatistics;
import com.remote.probability.world.MapGenerator;
import com.remote.probability.world.Tile;
import com.remote.remote2d.engine.art.Renderer;
import com.remote.remote2d.engine.art.ResourceLoader;
import com.remote.remote2d.engine.art.Texture;
import com.remote.remote2d.engine.entity.component.Component;
import com.remote.remote2d.engine.logic.Vector2;

public class ComponentTile extends Component {
	
	private static Texture shadowNorth;
	private static Texture shadowWest;
	private static Texture shadowNE;
	private static Texture shadowNW;
	
	@Override
	public void init() {
		
	}

	@Override
	public void onEntitySpawn() {
		
	}

	@Override
	public void renderAfter(boolean editor, float interpolation) {
		if(shadowNorth == null) shadowNorth = ResourceLoader.getTexture("res/art/tile/shadow_north.png");
		if(shadowWest == null) shadowWest = ResourceLoader.getTexture("res/art/tile/shadow_west.png");
		if(shadowNE == null) shadowNE = ResourceLoader.getTexture("res/art/tile/shadow_north_east.png");
		if(shadowNW == null) shadowNW = ResourceLoader.getTexture("res/art/tile/shadow_north_west.png");
		
		int x = (int) (entity.pos.x/MapGenerator.DEFAULT_TILE_SIZE);
		int y = (int) ((entity.pos.y+entity.dim.y-entity.dim.x)/MapGenerator.DEFAULT_TILE_SIZE);
		
		if(x < 0 || y < 0 || x >= GameStatistics.map.length || y >= GameStatistics.map.length || !Tile.tiles[GameStatistics.map[x][y]].getWalkable())
			return;
		
		if(x != 0 && !Tile.tiles[GameStatistics.map[x-1][y]].getWalkable())
		{
			Renderer.drawRect(entity.pos, new Vector2(8,entity.dim.y), shadowWest, 0xffffff, 1);
			if(y != 0 && !Tile.tiles[GameStatistics.map[x][y-1]].getWalkable())
				Renderer.drawRect(entity.pos, new Vector2(8,16), shadowNW, 0xffffff, 1);
		}
		
		if(x < GameStatistics.map.length && !Tile.tiles[GameStatistics.map[x+1][y]].getWalkable())
		{
			Renderer.drawRect(entity.pos.add(new Vector2(entity.dim.x-8,0)), new Vector2(8,entity.dim.y), new Vector2(1), new Vector2(-1), shadowWest, 0xffffff, 1);
			if(y < GameStatistics.map.length && !Tile.tiles[GameStatistics.map[x][y+1]].getWalkable())
				Renderer.drawRect(entity.pos, new Vector2(entity.dim.x-8,16), shadowNE, 0xffffff, 1);
		}
		
		if(y != 0 && !Tile.tiles[GameStatistics.map[x][y-1]].getWalkable())
			Renderer.drawRect(entity.pos, new Vector2(entity.dim.x,8), shadowNorth, 0xffffff, 1);
	}

	@Override
	public void renderBefore(boolean editor, float interpolation) {
		
	}

	@Override
	public void tick(int i, int j, int k) {
		
	}

	@Override
	public void apply() {
		
	}

}
