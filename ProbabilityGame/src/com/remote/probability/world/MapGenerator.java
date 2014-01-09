package com.remote.probability.world;

import com.remote.remote2d.engine.entity.Entity;
import com.remote.remote2d.engine.logic.Vector2;
import com.remote.remote2d.engine.world.Map;

public abstract class MapGenerator {
	
	public static int DEFAULT_TILE_SIZE = 64;
	public static final String PLAYER = "res/entity/player/lord_lard.entity.xml";
	
	public abstract Map generateTiledMap(int width, int height, long seed, ProgressMeter progress);
	
	public static Map createMapFromTiles(byte[][] tiles, int px, int py, ProgressMeter progress)
	{
		return createMapFromTiles(tiles, DEFAULT_TILE_SIZE, px, py, progress);
	}
	
	public static Map createMapFromTiles(byte[][] tiles, int tileWidth, int px, int py, ProgressMeter progress)
	{
		Map map = new Map();
		long num = 0;
		progress.mapLoadProgress = 0;
		progress.stage = "Converting Tiles to Entities...";
		
		for(int i=0;i<3;i++)
		{
			for(int x=0;x<tiles.length;x++)
			{
				for(int y=0;y<tiles[x].length;y++)
				{
					if(Tile.tiles[tiles[x][y]].getLevel() != i)
						continue;
					
					Entity tile = map.getEntityList().instantiatePrefab(Tile.tiles[tiles[x][y]].getPrefab());
					tile.pos = new Vector2(x,y).multiply(new Vector2(tileWidth));
					float ratio = tile.dim.y/tile.dim.x;
					tile.dim = new Vector2(tileWidth,((float)tileWidth)*ratio);
					tile.pos.y -= tile.dim.y-tileWidth;
					tile.updatePos();
					
					num ++;
					progress.mapLoadProgress = ((double)num)/(tiles.length*tiles[0].length);
				}
			}
			if(i == 1)
				map.getEntityList().instantiatePrefab(PLAYER).pos = new Vector2(px*DEFAULT_TILE_SIZE,py*DEFAULT_TILE_SIZE);
		}
		
		return map;
	}
	
	public static class ProgressMeter
	{
		protected double mapLoadProgress = 0;
		protected double tileGenProgress = 0;
		protected String stage = "Initializing...";
		
		public double getProgress()
		{
			return mapLoadProgress*0.5+tileGenProgress*0.5;
		}
		
		public String getStage()
		{
			return stage;
		}
	}
	
}
