package com.remote.probability.world;

import java.util.Random;

import com.remote.remote2d.engine.world.Map;

public class MapGeneratorSimple extends MapGenerator {

	@Override
	public Map generateTiledMap(int width, int height, long seed, ProgressMeter progress) {
		Random rand = new Random(seed);
		byte[][] ret = new byte[width][height];
		
		long num = 0;
		progress.tileGenProgress = 0;
		progress.stage = "Generating tiles...";
		
		for(int x=0;x<width;x++)
		{
			for(int y=0;y<height;y++)
			{
				if(x == 0 || y == 0 || x == width-1 || y == height-1)
					ret[x][y] = Tile.WALL.getID();
				else if(rand.nextInt(100) < 5)
					ret[x][y] = Tile.ROCK.getID();
				else
					ret[x][y] = Tile.GROUND.getID();
				
				num++;
				progress.tileGenProgress = ((double)num)/(width*height);
			}
		}
		
		ret[width/2][height/2] = Tile.GROUND.getID();
		Map m = createMapFromTiles(ret,width/2,height/2, rand, progress);
		
		return m;
	}
	
}
