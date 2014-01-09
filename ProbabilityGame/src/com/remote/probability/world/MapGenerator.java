package com.remote.probability.world;

import com.remote.remote2d.engine.entity.Entity;
import com.remote.remote2d.engine.logic.Vector2;
import com.remote.remote2d.engine.world.Map;

public abstract class MapGenerator {
	
	public static int DEFAULT_TILE_SIZE = 64;
	
	public abstract Map generateTiledMap(int width, int height, long seed);
	
	public static Map createMapFromTiles(byte[][] tiles)
	{
		return createMapFromTiles(tiles, DEFAULT_TILE_SIZE);
	}
	
	public static Map createMapFromTiles(byte[][] tiles, int tileWidth)
	{
		Map map = new Map();
		
		for(int x=0;x<tiles.length;x++)
		{
			for(int y=0;y<tiles[x].length;y++)
			{
				Entity tile = map.getEntityList().instantiatePrefab(Tile.tiles[tiles[x][y]].getPrefab());
				tile.pos = new Vector2(x,y).multiply(new Vector2(tileWidth));
				float ratio = tile.dim.y/tile.dim.x;
				tile.dim = new Vector2(tileWidth,((float)tileWidth)*ratio);
				tile.pos.y -= tile.dim.y-tileWidth;
				tile.updatePos();
			}
		}
		
		return map;
	}
	
}
