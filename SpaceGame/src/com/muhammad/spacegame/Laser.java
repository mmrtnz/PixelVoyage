package com.muhammad.spacegame;
import java.awt.Image;
import java.io.IOException;

import javax.swing.ImageIcon;

/** Muhammad Martinez */
import com.muhammad.navigation.GamePanel;
import com.muhammad.utilities.ResourceLoader;

 public class Laser implements Collidable
 {
 	protected int speed,x,y;
 	protected double power;
 	protected Image image;
 	protected ImageIcon ii;
 	private boolean hide;
 	
 	//Default = Planet
  	public Laser() throws IOException
 	{
 		image = ResourceLoader.loadVisual("Visual/Gameplay/defaultLaser.jpg");
 		ii = new ImageIcon(image);
 		x = -10;
 		y = -10;
 		power=0;
 	}
  	
  	//Overloaded
  	public Laser(int x, int y, int spd, double pow, String visAdd) throws IOException
 	{
  		this.speed = spd;
 		this.x = x;
 		this.y = y;
 		this.power = pow;
 		if(!visAdd.substring(visAdd.indexOf('.')).equals(".gif"))
 		{
 			image = ResourceLoader.loadVisual(visAdd);
 			ii = new ImageIcon(image);
 		}
 		else
 		{
 			ii = new ImageIcon(visAdd);
 	 		image = ii.getImage();
 		}
 	}
  	
  	//Animated laser
  	public Laser(int x, int y, int spd, double pow, Image icon) throws IOException
 	{
  		this.speed = spd;
 		this.x = x;
 		this.y = y;
 		this.power = pow;
 		image = icon;
 		ii = new ImageIcon(icon);
 	}
  	
 	//Moves laser up by a distance of speed, the speed of the laser
 	public void move()
 	{
 		if(y < (-1)*ii.getIconHeight() || y > GamePanel.HEIGHT)//Off screen
 			hide = true;
 		else
 		{
 			hide = false;
 			y += speed;
 		}		
 	}
 	//Resets laser position to in front of spaceship
 	public void hide()
 	{hide = true;} 
 	//Accessors
 	public int getX()
 	{return x;}
 	public int getY()
 	{return y;}
 	public double getPow()
 	{return power;}
 	public Image getImage()
 	{return image;}
 	public Image getAnim() //For animated lasers
 	{return ii.getImage();}
 	public boolean isHiding()
 	{return hide;}
 	//Modifiers
 	public void setX(int newX)
	{x = newX;}
	public void setY(int newY)
	{y = newY;} 
	//ToString
	public String toString()
	{return this.getClass().getSimpleName();}	
 }