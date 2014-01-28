package com.muhammad.spacegame;

/**Muhammad Martinez*/

import java.io.IOException;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import com.muhammad.navigation.GamePanel;

public class LevelStage implements Stage
{
	/**
	 * @param level - The current level of the game. Used as independent 
	 * 			variable for some equations.
	 * @param maxEnemies = 15+(level^(3/2)). Sets enemyA length.
	 * @param freq = Represents the distance between enemies. Will be used 
	 * 			when positioning enemies. 
	 * @param spread = (maxEnemies*frequency)+25. The furthest y distance 
	 * 			an enemy could be. In order words the height of the 
	 * 			imaginary panel above the screen. 
	 * @param puF - Power up frequency. Determines the odds of a power up 
	 * 			spawning from a rock. Inversely related to the amount of 
	 * 			rocks.
	 * @param ss - The spaceship and users connection to the game. It is 
	 * 			passed around throughout the game and contains data of 
	 * 			the current game. 
	 * @param yFold - see positionEnemies()
	 * 
	 * For more information about methods see the Stage interface.
	 */
	private double freq, spread;
	private int maxEnemies, level;
	public Spaceship ss;
	private int [] yFold;
	//As & ALs for each element
	private ArrayList <Enemy>enemyAL;
	public static ArrayList <ScoreString>enemyScoresAL;
	public static Laser[]enemyLaserA;
	public static Laser[]laserA;
	public static PowerUp[]powUpA;
	
	//Overloaded
	public LevelStage(Spaceship spaceship, int level) throws IOException
	{
		this.level = level;
		ss = spaceship;
		
		//Filling arrays
		enemyScoresAL = new ArrayList<ScoreString>();
		enemyAL = new ArrayList<Enemy>();
		loadEnemies(); //Initializes spread, freq, and maxEnemies
		positionEnemies();
		//Length depends on the amount of spaceship lasers that can fit on screen.
		laserA = new Laser[(int)(GamePanel.HEIGHT/new ImageIcon(spaceship.getLaserImage()).getIconHeight())];
		for(int k=0;k<laserA.length;k++)
			laserA[k]=new Laser();
		//Rough guesstimate
		enemyLaserA = new Laser[maxEnemies*3];
		for(int k=0;k<enemyLaserA.length;k++)
			enemyLaserA[k]=new Laser();
		//Quarter of enemies, plenty of room after a while
		powUpA = new PowerUp[maxEnemies/4];
		for(int k=0;k<powUpA.length;k++)
			powUpA[k]=new PowerUp();
	}
	
	/* The amount for every enemy is determined with individual equations. A 
	 * total max amount is created from their sum. Freq and spread are also 
	 * calculated. In the for loop, an enemy type is randomly selected. If that
	 * type hasn't reached it's max, it is added. Else the counter is -- thus 
	 * canceling out that iteration.
	 */
	public void loadEnemies() throws IOException
	{
		//Max # for each enemy type for stage
		int MAXROCK = (int)(2*level/3.0)+10; //Math.abs(level-10)+10;
		int MAXTRIP = (int)((3*level)/4.0);
		int MAXRAY = 8*(int)((Math.pow(level-2,(5.0/2))/(Math.pow(level,(5.0/2))))); //(int)((12*Math.sqrt(level-5))/Math.sqrt(level));
		int MAXDOS = (int)((2*(level-8))/3.0);//level-10;
		int MAXQUEEN = (3*(int)((Math.pow(level-10, (5.0/3))/(Math.pow(level, (6.0/5))))+1));//(int)(MAXRAY/4);
	
		int[]enemyMaxs = {MAXROCK,MAXTRIP,MAXRAY,MAXDOS,MAXQUEEN};
	
		maxEnemies = MAXROCK + MAXTRIP + MAXRAY + MAXDOS + MAXQUEEN;
		freq = calculateFrequency(level);
		spread = (maxEnemies*freq)+25;

		//Filling AL 
		while(enemyAL.size() < maxEnemies)
		{
			int type = (int)(Math.random()*5);
			//If we haven't reached capacity for that type of enemy.
			if(enemyMaxs[type]>0)
			{
				switch(type)//Add the enemy of said type.
				{
				case 0:
					enemyAL.add(new Asteroid(0,0));
					break;
				case 1:
					enemyAL.add(new Tripler(0,0));
					break;
				case 2:
					enemyAL.add(new Ray(0,0));
					break;
				case 3:
					enemyAL.add(new Dos(0,0));
					break;
				case 4:
					enemyAL.add(new Queen(0,0,ss)); //Spaceship used for laser
					break;
				}
				enemyMaxs[type]=enemyMaxs[type]-1;
			}
		}
	}
	
	public double calculateFrequency(int x)
	{
		int total=0;
		while(x-->=0)
			total+=(5-x);
		return 200-total; 
	}
	
	/* Randomly positioning enemies
	 * @param yFold - specific y-coordinates an enemy can
	 * 					respond in by dividing the y-range (spread) by the
	 * 					spacing between enemies (freq). One of these folds
	 * 					is then randomly selected.
	 * @param randomX - 8 evenly spaced X coordinates.
	 * @param randomY - Selects the following yfold
	 */
	public void positionEnemies()
	{
		yFold = new int[(int)(spread/freq)];
		for(int y = 0; y<yFold.length; y++)
			yFold[y] = (int)(-1*freq*y);
		int cnt=0;
		for(Enemy e: enemyAL)
		{
			//Between 0.3-0.7 of WIDTH
			int randomX = (int)(GamePanel.WIDTH*0.1)*(int)(Math.random()*4)+(int)(GamePanel.WIDTH*0.3);
			e.setX(randomX);
			e.setY(yFold[cnt++]);
			if(cnt>yFold.length)
				cnt=0;
		}
	}

	public void update()
	{
		moveElements();
		checkElementCollisions();
	}

	public void moveElements()
	{
		for(Laser k : laserA)
			if(!k.isHiding())
				k.move();
		for(Enemy m:enemyAL)
			if(!m.isDead())
				m.act();
		for(Laser k : enemyLaserA)
			if(!k.isHiding())
				k.move();
		for(PowerUp k : powUpA)
			if(!k.hasBeenActivated())
				k.move();
		for(ScoreString ss : enemyScoresAL)
			if(ss.getY()>0)
				ss.move();
	}

	public void checkElementCollisions()
	{
		//Spaceship laser -> enemies
		for(Laser k : laserA)
			if(!k.isHiding())
				for(Enemy j : enemyAL)
					if(!j.isDead() && CollisionDetector.collide(k,j))
					{
						k.hide();
						j.hit(ss.getPow());
					}
		//Enemy laser -> spaceship
		for(Laser k : enemyLaserA)
			if(!k.isHiding() && CollisionDetector.collide(ss,k))
			{
				double damage = k.getPow()-ss.getDef();
				if(damage <= 0)
					ss.hit(1);
				else
					ss.hit(damage);
				k.hide();
			}	
		//Enemies <-> spaceship
		for(Enemy j : enemyAL)
			if(!j.isDead() && CollisionDetector.collide(ss,j))
			{	
				j.hide();
				ss.hit(4-ss.getDef());
			}
		//PowerUps <-> spaceship
		for(PowerUp p : powUpA)
			if(!p.hasBeenActivated() && CollisionDetector.collide(ss,p))
				p.activate();
	}

	public boolean isClear()
	{
		int bodyCount = 0;
		for(Enemy m : enemyAL)
			if(m.isDead())
				bodyCount++;
		if(bodyCount == enemyAL.size())
			return true;
		return false;
	}
	
	//Modifiers
	public void addLaser(String str, Laser laz)
	{
		Laser[]lazA=new Laser[1];
		
		if(str.equalsIgnoreCase("spaceship"))
			lazA=laserA;
		else if (str.equalsIgnoreCase("enemy"))
			lazA=enemyLaserA;
		
		for(int k=0;k<lazA.length;k++)
			if(lazA[k].isHiding())
			{	
				lazA[k]=laz;
				break;
			}
	}
	
	public void addPowerUp(PowerUp pu)
	{
		for(int k=0;k<powUpA.length;k++)
			if(powUpA[k].hasBeenActivated())
			{	
				powUpA[k]=pu;
				break;
			}
	}
	
	public void addScoreString(ScoreString ss)
	{enemyScoresAL.add(ss);}
	
	//Accessors
	public ArrayList<Enemy> getEnemies()
	{return enemyAL;}
	public ArrayList<ScoreString>getEnemyValues()
	{return enemyScoresAL;}
	public Laser[] getShipLasers()
	{return laserA;}
	public Laser[] getEnemyLasers()
	{return enemyLaserA;}
	public PowerUp[] getPowerUps()
	{return powUpA;}
	public Boss getBoss() 
	{return null;}
	public Laser getPlanet()
	{return null;}
}
