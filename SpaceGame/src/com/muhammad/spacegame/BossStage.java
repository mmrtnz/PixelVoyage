package com.muhammad.spacegame;

import java.io.IOException;
import java.util.ArrayList;

import javax.swing.ImageIcon;

import com.muhammad.navigation.GamePanel;
import com.muhammad.spacegame.Boss;

public class BossStage implements Stage
{
	/**
	 * @param maxEnemies, freq, spread - See LevelStage.
	 * @param clear - When the planet image has arrived and the 
	 * 			boss is dead.
	 * @param boss - The main enemy.
	 * @param planet - Simply an image.
	 * 
	 * For more information about methods see the Stage interface.
	 */
	private int maxEnemies, freq, spread, bossLevel;
	private boolean clear;
	private Spaceship ss;
	private Enemy boss;
	private Planet planet;
	//Arrays
	private ArrayList <Enemy>enemyAL;//Asteroids
	public static ArrayList <ScoreString>enemyScoresAL;
	public static Laser[]enemyLaserA;
	public static Laser[]laserA;
	public static PowerUp[]powUpA;
	
	//Overloaded
	public BossStage(Spaceship spaceship, int level) throws IOException
	{
		ss = spaceship;
		bossLevel = level;
		maxEnemies = level*5;
		freq = 250; //Enemies are asteroids, simple hard code.
		spread = (maxEnemies*freq)+25;
		clear = false;
		//Length roughly depends on how many spaceship laser images long the game panel is.
		laserA = new Laser[(int)(GamePanel.HEIGHT/new ImageIcon(spaceship.getLaserImage()).getIconHeight())];
		for(int k=0;k<laserA.length;k++)
			laserA[k]=new Laser();
		//I don't think the boss would shoot more than 5 times on a screen
		enemyLaserA = new Laser[12+(level*3)];
		for(int k=0;k<enemyLaserA.length;k++)
			enemyLaserA[k]=new Laser();
		//Half of enemies since they'll primarily be rocks
		powUpA = new PowerUp[maxEnemies/2];
		for(int k=0;k<powUpA.length;k++)
			powUpA[k]=new PowerUp();	
		//Boss + Minions
		enemyAL = new ArrayList<Enemy>();
		enemyScoresAL = new ArrayList<ScoreString>();
		//Planet
		planet = new Planet(0);//Only one planet type for now
		
		loadEnemies();
	}
	
	public void loadEnemies() throws IOException 
	{
		//Creating boss
		boss = new Karmok(bossLevel);
		for(int k=0;k<maxEnemies;k++)
		{	
			int randomX = (int)(GamePanel.WIDTH*0.1)*(int)(Math.random()*8)+(int)(GamePanel.WIDTH*0.1);
			int randomY = (int)((-1)*(Math.random()*(spread/freq))*(freq));
			enemyAL.add(new Asteroid(randomX,randomY));
		}	
	}
	
	
	public void update() 
	{
		moveElements();
		checkElementCollisions();
		clear=(planet.doneMoving && boss.isDead());
	}
	
	public void moveElements()
	{
		boss.act();
		planet.move();
		for(Laser k : laserA)
			if(!k.isHiding())
				k.move();
		for(Laser k : enemyLaserA)
			if(!k.isHiding())
				k.move();
		for(Enemy m:enemyAL)
			if(!m.isDead())
				m.act();
		for(PowerUp k : powUpA)
			if(!k.hasBeenActivated())
				k.move();
		for(ScoreString ss : enemyScoresAL)
			if(ss.getY()>0)
				ss.move();
	}

	public void checkElementCollisions()
	{	
		 //Spaceship laser -> Enemies & Boss
		for(Laser k : laserA)
			if(!k.isHiding())
			{	
				for(Enemy j : enemyAL)
					if(!j.isDead() && CollisionDetector.collide(k,j))
					{
						k.hide();
						j.hit(ss.getPow());
					}
				if(CollisionDetector.collide(k,boss))
				{
					k.hide();
					boss.hit(ss.getPow());
				}
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
		//Boss <-> spaceship
		if(CollisionDetector.collide(ss,boss))
			ss.hit(5-ss.getDef());//Constant
		//PowerUps <-> spaceship
		for(PowerUp p : powUpA)
			if(!p.hasBeenActivated() && CollisionDetector.collide(ss,p))
				p.activate();
	}
	
	//Modifiers
	public void addLaser(String str, Laser laz)
	{
		if(str.equalsIgnoreCase("spaceship"))
		{
			for(int k=0;k<laserA.length;k++)
				if(laserA[k].isHiding())
				{	
					laserA[k]=laz;
					break;
				}
		}
		else if (str.equalsIgnoreCase("enemy"))
		{
			for(int k=0;k<enemyLaserA.length;k++)
				if(enemyLaserA[k].isHiding())
				{	
					enemyLaserA[k]=laz;
					break;
				}
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
	public Boss getBoss() //Casted so that health images can be accessed.
	{return (Boss) boss;}
	public Laser getPlanet()
	{return planet;}
	public boolean isClear() 
	{return clear;}
}
