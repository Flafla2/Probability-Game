package com.remote.probability.world;

import java.util.Random;

import com.remote.probability.component.ComponentEnemy;
import com.remote.remote2d.engine.entity.Entity;
import com.remote.remote2d.engine.logic.Vector2;
import com.remote.remote2d.engine.world.Map;

public abstract class MapGenerator {
	
	public static int DEFAULT_TILE_SIZE = 64;
	public static final String PLAYER = "res/entity/player/lord_lard.entity.xml";
	
	public static final int MONSTER_SPAWN_PROBABILITY = 30;
	
	public abstract Map generateTiledMap(int width, int height, long seed, ProgressMeter progress);
	
	public static Map createMapFromTiles(byte[][] tiles, int px, int py, Random random, ProgressMeter progress)
	{
		return createMapFromTiles(tiles, DEFAULT_TILE_SIZE, px, py, random, progress);
	}
	
	public static Map createMapFromTiles(byte[][] tiles, int tileWidth, int px, int py, Random random, ProgressMeter progress)
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
					
					if(tiles[x][y] == Tile.WALL.getID())
						tile.material.setUVPos(new Vector2((float)(random.nextInt(23)+1)/24f,tile.material.getUVPos().y));
					else if(tiles[x][y] == Tile.GROUND.getID())
						tile.material.setUVPos(new Vector2((float)(random.nextInt(3)+1)/4f,tile.material.getUVPos().y));
					
					num ++;
					progress.mapLoadProgress = ((double)num)/(tiles.length*tiles[0].length);
				}
			}
			if(i == 1)
			{
				Entity player = map.getEntityList().instantiatePrefab(PLAYER);
				player.pos = new Vector2(px*tileWidth,py*tileWidth);
				
				for(int x=0;x<tiles.length;x++)
				{
					for(int y=0;y<tiles[x].length;y++)
					{
						if((x == px && y == py) || !Tile.tiles[tiles[x][y]].getWalkable())
							continue;
						
						boolean top = y != 0 && !Tile.tiles[tiles[x][y-1]].getWalkable();
						boolean bot = y != tiles[x].length-1 && !Tile.tiles[tiles[x][y+1]].getWalkable();
						boolean lef = x != 0 && !Tile.tiles[tiles[x-1][y]].getWalkable();
						boolean rig = x != tiles.length-1 && !Tile.tiles[tiles[x+1][y]].getWalkable();
						
						if(top || bot || lef || rig)
							continue;
												
						int rand = random.nextInt((int) (MONSTER_SPAWN_PROBABILITY));///GameStatistics.finalDifficultyModifier));
						Vector2 dist = new Vector2(x-px,y-py).abs();
						float distsq = dist.x*dist.x+dist.y*dist.y;
						if(distsq < tileWidth*3)
							continue;
						if(rand == 0)
						{
							int enemyType = random.nextInt(3);
							Entity e;
							if(enemyType == 0)
								e = map.getEntityList().instantiatePrefab("res/entity/enemy/mummy.entity.xml");
							else if(enemyType == 1)
								e = map.getEntityList().instantiatePrefab("res/entity/enemy/bat.entity.xml");
							else //if(enemyType == 2)
								e = map.getEntityList().instantiatePrefab("res/entity/enemy/scarab.entity.xml");
							ComponentEnemy comp = e.getComponentsOfType(ComponentEnemy.class).get(0);
							e.pos = new Vector2(x*tileWidth-comp.hitboxPos.x,y*tileWidth-comp.hitboxPos.y);
							comp.player = player;
							comp.maxHealth *= GameStatistics.finalDifficultyModifier;
							comp.damage *= GameStatistics.finalDifficultyModifier;
							comp.detectionDistance *= GameStatistics.finalDifficultyModifier;
							//comp.walkSpeed *= GameStatistics.finalDifficultyModifier;
						}
					}
				}
				
			}
		}
		
		GameStatistics.map = tiles;
		GameStatistics.tileSize = tileWidth;
		
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
