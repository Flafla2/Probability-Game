package com.remote.probability.world;

public class Tile {
	
	public static Tile[] tiles = new Tile[256];
	public static final Tile GROUND = 	new Tile(b(0), b(1), "Ground", "res/entity/tile/ground.entity.xml");
	public static final Tile WALL = 	new Tile(b(1), b(2), "Wall", "res/entity/tile/wall.entity.xml");
	public static final Tile ROCK = 	new Tile(b(2), b(2), "Rock", "res/entity/tile/rock.entity.xml");
	
	private String name;
	private String prefab;
	private byte id;
	private byte level;
	
	public Tile(byte id, byte level, String name, String prefab)
	{
		this.name = name;
		this.prefab = prefab;
		this.id = id;
		this.level = level;
		tiles[id] = this;
	}
	
	public byte getID()
	{
		return id;
	}
	
	public byte getLevel()
	{
		return level;
	}
	
	public String getName()
	{
		return name;
	}
	
	public String getPrefab()
	{
		return prefab;
	}
	
	private static byte b(int i)
	{
		return ((byte)i);
	}
	
}
