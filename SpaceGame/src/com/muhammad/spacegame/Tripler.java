package com.muhammad.spacegame;

import java.awt.Image;
import java.io.IOException;

import javax.swing.ImageIcon;

import com.muhammad.navigation.GamePanel;
import com.muhammad.utilities.AudioTool;
import com.muhammad.utilities.ResourceLoader;

/** Kevin Chorath */

 public class Tripler implements Enemy
 {
	private final double POW,DEF,MAXHP;
	private double health;
 	private int tick, xspd, yspd, x, y, counter, laserSpeed;
 	private boolean dead, hit;
 	private String visualAddress;
 	private Image safeIMG, hitIMG;
 	private ImageIcon tripII;
 	static int cnt = 0;
 	
 	//Overloaded constructor
 	public Tripler(int x, int y) throws IOException
 	{
 		this.x = x;
 		this.y = y;
 		xspd = 10 + ((int)(Math.random()*2)*(-20)); //Starts moving either left or right
 		yspd = 10 + (int)(Math.random()*4);
 		POW = 4;
 		DEF = 1;
 		MAXHP = 5.0;
 		health = MAXHP;
 		counter = 0;
 		tick=0;
 		laserSpeed = 10;
 		dead = false;
 		hit = false;
 		//Visual
 		visualAddress = "Visual/Gameplay/"+toString()+"/"+toString()+"-Laz.png";
 		safeIMG = ResourceLoader.loadVisual("Visual/Gameplay/"+toString()+"/"+toString()+".png");
 		hitIMG = ResourceLoader.loadVisual("Visual/Gameplay/"+toString()+"/"+toString()+"-Hit.png");
 		tripII = new ImageIcon(safeIMG);
 	}

 	public void act()
 	{
 		move();
 		int random = ((int)(((Math.random()*100))))+1; //hard coded, change later
 		if(counter%random == 0 && y > 0)
 			if(!dead)
				try 
 					{shoot();}
 				catch (IOException e) 
 					{e.printStackTrace();}
 	}
 	
 	//Moves zigzagging downward
 	public void move()
 	{
 		tick++;
 		xspd = (int)(10*Math.sin((Math.PI*tick)/10.0));
 		//If at left or right edge, changes direction
 		if(x >= GamePanel.WIDTH-tripII.getIconWidth() || x <= 0)
 			xspd*=-1;
 		//Movement
 		x+=xspd;
 		y+=yspd;
 		//Leaves screen
 		if(y > GamePanel.HEIGHT)
 			dead = true;
 	}

 	public void shoot() throws IOException
 	{
 		Laser laz = new Laser(x, y + tripII.getIconHeight(), laserSpeed, POW, visualAddress);
 		GamePanel.stage.addLaser("enemy",laz);
 		AudioTool.playSFX(toString(), "Laz");
 		counter++;
 	}
 	
 	public void hit(double sspow)
 	{
 		hit = true;
 		if(sspow == 0)
 			health--;
 		else
 			health -= MAXHP*sspow;
 		if(health<=0)
 		{
 			AudioTool.playSFX(toString(), "Hit");
 			dead=true;
 			GamePanel.stage.addScoreString(new ScoreString(20,x,y));
 			GamePanel.spaceship.setScore(GamePanel.spaceship.getScore()+20);
 		}
 		else
 			AudioTool.playSFX(toString(), "Dead");
 	} 
 	
 	public void hide()
 	{dead=true;}
 	
 	//Accessors
 	public int getX()
 	{return x;}
 	public int getY()
 	{return y;}
 	public double getPow()
 	{return POW;}
 	public double getDef()
 	{return DEF;}
 	public Image getImage()
 	{
	 	if(hit)
	 	{
	 		if(Math.random()<0.2)
	 			hit = false;
	 		return hitIMG;
	 	}
	 	else
	 		return safeIMG;	
 	}
 	public boolean isDead()
 	{return dead;}
 	//Modifiers
	public void setX(int newX)
	{x = newX;}
	public void setY(int newY)
	{y = newY;} 
 	//ToString
	public String toString()
	{return this.getClass().getSimpleName();}
 }