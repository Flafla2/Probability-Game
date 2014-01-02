package com.remote.probability.world;

public class Tile {
	
	public static Tile[] tiles = new Tile[256];
	public static final Tile GROUND = 	new Tile(b(0), "Ground", "res/entity/tile/ground.entity");
	public static final Tile WALL = 	new Tile(b(1), "Wall", "res/entity/tile/wall.entity");
	public static final Tile ROCK = 	new Tile(b(2), "Rock", "res/entity/tile/rock.entity");
	
	private String name;
	private String prefab;
	private byte id;
	
	public Tile(byte id, String name, String prefab)
	{
		this.name = name;
		this.prefab = prefab;
		this.id = id;
		tiles[id] = this;
	}
	
	public byte getID()
	{
		return id;
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
