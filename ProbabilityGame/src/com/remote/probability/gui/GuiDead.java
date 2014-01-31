package com.remote.probability.gui;

import com.remote.probability.AudioSwitcher;
import com.remote.probability.Game;
import com.remote.probability.AudioSwitcher.SoundMode;
import com.remote.probability.component.ComponentPlayer.Direction;
import com.remote.probability.world.GameStatistics;
import com.remote.probability.world.MapGenerator;
import com.remote.probability.world.MapGeneratorSimple;
import com.remote.remote2d.engine.AudioHandler;
import com.remote.remote2d.engine.Remote2D;
import com.remote.remote2d.engine.art.Animation;
import com.remote.remote2d.engine.art.Fonts;
import com.remote.remote2d.engine.gui.GuiButton;
import com.remote.remote2d.engine.gui.GuiMenu;
import com.remote.remote2d.engine.logic.Collider;
import com.remote.remote2d.engine.logic.ColliderBox;
import com.remote.remote2d.engine.logic.Vector2;

public class GuiDead extends GuiMenu {
	
	public ColliderBox[] outer;
	
	public Vector2 playerPos = new Vector2(10);
	public Vector2 playerDim = new Vector2(128);
	
	private static final float WALK_SPEED = 10;
	private static final float DIAG_WALK_SPEED = (float) (WALK_SPEED*Game.ONE_OVER_SQRT2);
	
	public Direction d = Direction.NORTH;
	private long nextTurn = -1;
	
	private static String[] animPaths = {
		"res/anim/player/lard_north_s.anim.xml",
		"res/anim/player/lard_south_s.anim.xml",
		"res/anim/player/lard_east_s.anim.xml",
		"res/anim/player/lard_west_s.anim.xml",
		"res/anim/player/lard_northeast_s.anim.xml",
		"res/anim/player/lard_northwest_s.anim.xml",
		"res/anim/player/lard_southeast_s.anim.xml",
		"res/anim/player/lard_southwest_s.anim.xml"
	};
	
	private static Animation[] anim;
	
	public GuiDead()
	{
		backgroundColor = 0xaa0000;
	}
	
	@Override
	public void initGui()
	{
		buttonList.clear();
		buttonList.add(new GuiButtonStyled(1,new Vector2(screenWidth()/2-300,screenHeight()-50),new Vector2(600,40),"Quit"));
		
		outer = new ColliderBox[4];
		outer[0] = new ColliderBox(new Vector2(-50),new Vector2(50,screenHeight()+100));
		outer[1] = new ColliderBox(new Vector2(-50),new Vector2(screenWidth()+100,50));
		outer[2] = new ColliderBox(new Vector2(screenWidth(),-50),new Vector2(50,screenHeight()+100));
		outer[3] = new ColliderBox(new Vector2(-50,screenHeight()),new Vector2(screenWidth()+100,50));
	}
	
	private int getDirectionID(Direction d)
	{
		switch(d)
		{
		case NORTH:
			return 0;
		case SOUTH:
			return 1;
		case EAST:
			return 2;
		case WEST:
			return 3;
		case NORTHEAST:
			return 4;
		case NORTHWEST:
			return 5;
		case SOUTHEAST:
			return 6;
		case SOUTHWEST:
			return 7;
		}
		return 0;
	}
	
	@Override
	public void render(float interpolation)
	{
		super.render(interpolation);
		if(anim == null)
			return;
		anim[getDirectionID(d)].render(playerPos, playerDim);
		
		Fonts.get("Jungle").drawCenteredString("YOU LOST!", 20, 100, 0xffffff);
		Fonts.get("Arial").drawCenteredString("Max Health: "+(int)(GameStatistics.playerHealthModifier*GameStatistics.BASE_PLAYER_HEALTH), 210, 30, 0xffffff);
		Fonts.get("Arial").drawCenteredString("Bullet Damage: "+(int)(GameStatistics.playerDamageModifier*GameStatistics.BASE_PLAYER_DAMAGE), 240, 30, 0xffffff);
		Fonts.get("Arial").drawCenteredString("Wave: "+GameStatistics.wave, 150, 30, 0xffffff);
		Fonts.get("Arial").drawCenteredString("Total Loot: "+GameStatistics.totalCoins+" coins", 180, 30, 0xffffff);
	}
	
	@Override
	public void tick(int i, int j, int k)
	{
		super.tick(i, j, k);
		
		if(AudioSwitcher.getSoundMode() != SoundMode.WIN)
			AudioSwitcher.setSoundMode(SoundMode.WIN);
		
		if(anim == null)
		{
			anim = new Animation[8];
			for(int x=0;x<8;x++)
			{
				anim[x] = new Animation(animPaths[x]);
			}
		}
		
		if(nextTurn < System.currentTimeMillis())
		{
			nextTurn = System.currentTimeMillis()+Game.random.nextInt(3000)+1000;
			pickRandomDirection();
		}
		
		if(outer == null)
			return;
		
		Vector2 velocity = new Vector2(0,0);
		boolean collides = collides(velocity);
		do
		{
			switch(d)
			{
			case NORTH:
				velocity.y = -WALK_SPEED;
				break;
			case SOUTH:
				velocity.y = WALK_SPEED;
				break;
			case WEST:
				velocity.x = -WALK_SPEED;
				break;
			case EAST:
				velocity.x = WALK_SPEED;
				break;
			case NORTHEAST:
				velocity.x = DIAG_WALK_SPEED;
				velocity.y = -DIAG_WALK_SPEED;
				break;
			case SOUTHEAST:
				velocity.x = DIAG_WALK_SPEED;
				velocity.y = DIAG_WALK_SPEED;
				break;
			case NORTHWEST:
				velocity.y = -DIAG_WALK_SPEED;
				velocity.x = -DIAG_WALK_SPEED;
				break;
			case SOUTHWEST:
				velocity.y = DIAG_WALK_SPEED;
				velocity.x = -DIAG_WALK_SPEED;
				break;
			}
			collides = collides(velocity);
			if(collides)
			{
				nextTurn = System.currentTimeMillis()+Game.random.nextInt(3000)+1000;
				pickRandomDirection();
			}
		} while(collides);
		
		playerPos = playerPos.add(velocity);
	}
	
	private boolean collides(Vector2 velocity)
	{
		for(ColliderBox c : outer)
			if(Collider.collides(playerPos.add(velocity).getColliderWithDim(playerDim), c))
				return true;
		
		return false;
	}
	
	private void pickRandomDirection()
	{
		int rand = Game.random.nextInt(8);
		switch(rand)
		{
		case 0:
			d = Direction.NORTH;
			break;
		case 1:
			d = Direction.SOUTH;
			break;
		case 2:
			d = Direction.EAST;
			break;
		case 3:
			d = Direction.WEST;
			break;
		case 4:
			d = Direction.NORTHEAST;
			break;
		case 5:
			d = Direction.NORTHWEST;
			break;
		case 6:
			d = Direction.SOUTHEAST;
			break;
		case 7:
			d = Direction.SOUTHWEST;
			break;
		}
	}
	
	@Override
	public void actionPerformed(GuiButton button)
	{
		if(button.id == 0)
		{
			GameStatistics.lives--;
			MapGenerator gen = new MapGeneratorSimple();
			Remote2D.guiList.pop();
			Remote2D.guiList.push(new GuiLoadMap(gen, 40, 40, 1337));
		} else if(button.id == 1)
		{
			while(!(Remote2D.guiList.peek() instanceof GuiMainMenu)) Remote2D.guiList.pop();
		}
	}
	
}
