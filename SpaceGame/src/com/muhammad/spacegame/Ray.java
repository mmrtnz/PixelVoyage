/**Muhammad Martinez */
package com.muhammad.spacegame; 

import java.awt.Image;
import java.io.IOException;

import javax.swing.ImageIcon;

import com.muhammad.navigation.GamePanel;
import com.muhammad.utilities.AudioTool;
import com.muhammad.utilities.ResourceLoader;
import com.muhammad.utilities.Timer;

 public class Ray implements Enemy
 {
	/**
	 * @param prevTime - Time when shoot() was last called
	 * @param elapseTime - Time since prevTime.
	 * @param totalTime - Sum of elapseTimes.
	 * @param shootTime - Ray keeps shooting for this long. Determined by
	 * 			 totalTime.
	 * @param topBorder & bottomBorder - set boundaries which, when combined
	 * 			with left and right borders, cause Ray to bounce around the 
	 * 			screen.
	 */
	protected double pow, hp, maxhp;
	protected int x, y, xspd, yspd, laserSpeed, value;
 	private boolean dead, hit;
 	protected char action;
 	protected Timer shootTimer;
 	//Between 0.1 and 0.5
 	final int topBorder = (int)(GamePanel.HEIGHT*Math.random()/2.0);
 	//Between 0.1 and 0.5 from top
 	final int bottomBorder = (int)(GamePanel.HEIGHT*Math.random()/2.0)+topBorder;
	//Images
 	private String visualAddress;
 	protected Image safeIMG, hitIMG;
	protected ImageIcon rayII;
	
 	//Overloaded
 	public Ray(int x, int y) throws IOException
 	{
 		this.x = x;
 		this.y = y;
 		xspd = 6;
 		yspd = 7;
 		pow = 1;
 		maxhp = 20.0;
 		hp = maxhp;
 		value = 40;
 		dead = false;
 		hit = false;
 		action = 'I';
 		//Visual
 		visualAddress = "Visual/Gameplay/"+toString()+"/"+((int)(Math.random()*3))+".png";//Random color
 		safeIMG = ResourceLoader.loadVisual("Visual/Gameplay/"+toString()+"/"+toString()+".png");
 		hitIMG = ResourceLoader.loadVisual("Visual/Gameplay/"+toString()+"/"+toString()+"-Hit.png");
 		rayII = new ImageIcon(safeIMG);
 		laserSpeed = (new ImageIcon(ResourceLoader.loadVisual(visualAddress))).getIconHeight();
 	}
 	
 	/* Use when extended. Manually assign laser speed and visual address. */
 	public Ray(int x, int y, int xspd, int yspd, int pow, int maxhp, int value, String imga, String imgb) throws IOException
 	{
 		this.x=x;
 		this.y=y;
 		this.xspd=xspd;
 		this.yspd=yspd;
 		this.pow=pow;
 		this.maxhp=maxhp;
 		this.value=value;
 		hp=maxhp;
 		dead=false;
 		hit=false;
 		action='I';
 		safeIMG=ResourceLoader.loadVisual(imga);
 		hitIMG=ResourceLoader.loadVisual(imgb);
 		rayII=new ImageIcon(safeIMG);
 	}
 	
 	public void act()
 	{
		switch(action)
		{
		case 'I':
			introduce();
			break;
		case 'M':
			move();
			break;
		case 'S':
			try 
			{shoot();}
			catch (IOException e) 
			{e.printStackTrace();}
			break;
		}
 		if(y > GamePanel.HEIGHT)
 			dead = true;
 	}
 	
 	//Move south to screen
 	public void introduce()
 	{
 		if(y <= topBorder*1.25) 
 			y+=6;
 		else
 			action='M';
 	}
 	
 	//Moves diagonally and bounces of an invisible box border
 	public void move()
 	{	
		//Borders
		if(y >= bottomBorder || y <= topBorder)
			yspd*=(-1);
		if(x >= GamePanel.WIDTH-rayII.getIconWidth() || x <= 0)
			xspd*=(-1);
		if(Math.random()<=0.1)//10%
		{
			shootTimer = new Timer(5000);
			action='S';
		}
		//Move
 		x+=xspd;
		y+=yspd;
 	}
 	
 	/* Creates continuous stream of lasers for X seconds */
 	public void shoot() throws IOException
 	{
 		//2nd condition means it is shooting for the first time
 		if(!shootTimer.done())
 		{
	 		int x = this.x+(int)(rayII.getIconWidth()/3);
	 		int y = this.y+(int)(rayII.getIconHeight());
	 		Laser laz = new Laser(x, y, laserSpeed, pow, visualAddress);
	 		GamePanel.stage.addLaser("enemy",laz);
	 		AudioTool.playSFX(toString(), "Laz");
 		}
	 	else
	 		action = 'M';
 	}

 	public void hit(double sspow)
 	{
 		hit = true;
 		if(sspow == 0)
 			hp--;
 		else
 			hp -= maxhp*sspow;
 		if(hp<=0)
 		{
 			AudioTool.playSFX(toString(),"Dead");
 			dead=true;
 			GamePanel.stage.addScoreString(new ScoreString(value,x,y));
 			GamePanel.spaceship.setScore(GamePanel.spaceship.getScore()+value);
 		}
 		else
 			AudioTool.playSFX(toString(),"Hit");
 	}	
 	
 	public void hide()
 	{dead=true;}
  	
 	//Accessors
 	public int getX()
 	{return x;}
 	public int getY()
 	{return y;}
 	public double getPow()
 	{return pow;}
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