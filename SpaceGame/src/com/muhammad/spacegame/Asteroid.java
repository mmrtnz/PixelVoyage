package com.muhammad.spacegame;

import java.awt.Image;
import java.io.IOException;

import javax.swing.ImageIcon;

import com.muhammad.navigation.GamePanel;
import com.muhammad.utilities.AudioTool;
import com.muhammad.utilities.ResourceLoader;

/** Originally created by Kevin Chorath 
 *  Modified by Muhammad Martinez;
 */

 public class Asteroid implements Enemy
 {
	private final double POW,SPD;
 	private int health, x, y;
 	private boolean dead;
 	private String visualAddress;
 	private Image rockImage;
 	private ImageIcon rockII;
 	//Overloaded constructor
 	public Asteroid(int x, int y) throws IOException
 	{

 		if(x==0)//Spaceship can't shoot it
 			this.x = (int)(GamePanel.WIDTH*0.1);
 		else
 			this.x = x;
 		this.y = y;
 		health = 2;
 		POW = 0;
 		SPD = (int)(Math.random()*4)+8;
 		dead = false;
 		//Randomly pics (ba-dum tss :D) from selection of rock images
 		int g = (int)(Math.random()*4);
 		visualAddress = "Visual/Gameplay/"+toString()+"/A"+g+".png";
 		rockImage = ResourceLoader.loadVisual(visualAddress);
 		rockII = new ImageIcon(rockImage);
 	}
 	//Moves rock down by a distance of rockSpeed, the speed of the rock
 	public void act()
 	{ 
 		if(y > GamePanel.HEIGHT)
 			dead=true;
 		else
 			y += SPD;
 	}
 	
 	/* Slightly pushes back and changes image. Disregards spaceship attack damage */
 	public void hit(double sspow)
 	{ 
 		if(health == 2)
 		{
 	 		health--;
 	 		AudioTool.playSFX(toString(), "Hit");

 	 		//Slighty pushes back
 	 		y -= (int)(Math.random()*10);
 	 		int g = (int)(Math.random()*4);
 	 		rockImage = ResourceLoader.loadVisual("Visual/Gameplay/"+toString()+"/B"+g+".png");
 	 		//Create a powerup
 	 		if(Math.random()<=0.75)
 	 			GamePanel.stage.addPowerUp(new PowerUp(x+rockII.getIconWidth()/2,y+rockII.getIconHeight()/2));	
 		}
 		else
 		{
 			AudioTool.playSFX(toString(), "Dead");
 			dead = true;
 			GamePanel.stage.addScoreString(new ScoreString(5,x,y));
 			GamePanel.spaceship.setScore(GamePanel.spaceship.getScore()+5);
 		}
 	}

 	
 	public void hide()
 	{dead=true;}
 	
 	//Does nothing
 	public void shoot(){}
 	
 	//Accessors
 	public int getX()
 	{return x;}
 	public int getY()
 	{return y;}
 	public double getPow()
 	{return POW;}
 	public Image getImage()
 	{return rockImage;}
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