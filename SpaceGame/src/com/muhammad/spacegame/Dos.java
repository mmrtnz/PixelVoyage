package com.muhammad.spacegame;

import java.awt.Image;
import java.io.IOException;

import javax.swing.ImageIcon;

import com.muhammad.navigation.GamePanel;
import com.muhammad.utilities.AudioTool;
import com.muhammad.utilities.ResourceLoader;
import com.muhammad.utilities.Timer;

public class Dos implements Enemy
{
	private final double POW,DEF,MAXHP;
	private double health;
 	private int xspd, yspd, x, y, tick, value;
 	private char action;
 	private boolean dead, hit, shooting;
 	private String orientation;
 	private Image safeIMG, hitIMG;
 	private ImageIcon dosII;
 	private Timer shootTimer;
	
	public Dos(int x, int y) throws IOException
	{
 		this.x = x;
 		this.y = y;
 		xspd = 5 + ((int)(Math.random()*2));
 		yspd = xspd;
 		POW = 3;
 		DEF = 5.0;
 		MAXHP = 20.0;
 		health = MAXHP;
 		value = 35;
 		tick=0;
 		//Orientation
 		if(Math.random()>0.5)
 			orientation="Vertical";
 		else
 			orientation="Horizontal";
 		//Images
 		loadImages();
 		//Action
 		action = 'I';
	}
	
	@Override
	public void act() 
	{
		tick++;
		try
		{
			switch(action)
			{
			case 'I':
				introduce();
				break;
			case 'M':
				move();
				break;
			}
			//Imagechek
			if(shootTimer!=null && shootTimer.done())
				{
					loadImages();
					shooting = false;
				}
		}catch(IOException e)
		{e.printStackTrace();}
		//Dead when off screen.
		if(y > GamePanel.HEIGHT)
			dead = true;
	}

 	//Move south to screen. Copied from Ray.
 	public void introduce()
 	{
 		if(y <= GamePanel.HEIGHT*0.15) 
 			y+=yspd;
 		else
 			action='M';
 	}
 	
 	public void move() throws IOException
 	{
		//Borders
		if(yBorderCheck())
			yspd*=(-1);
		if(xBorderCheck())
			xspd*=(-1);
		//Movement depends on orientation
 		if(orientation.equalsIgnoreCase("Horizontal"))
 			y+=yspd;
 		else
 			x+=xspd;
		//4% switch orientation
		if(Math.random()<0.04)
			if(!yBorderCheck() && !xBorderCheck()) //Border checks again
				try 
				{switchOrientation();return;}
				catch (IOException e) 
				{e.printStackTrace();}
 		//Constant shooting rate. 10th tick
 		if(tick%20==0)
 			shoot();
 	}
	

	//Copied from Tripler
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
 			GamePanel.stage.addScoreString(new ScoreString(value,x,y));
 			GamePanel.spaceship.setScore(GamePanel.spaceship.getScore()+value);
 		}
 		else
 			AudioTool.playSFX(toString(), "Dead");
	}

	/* Creates a custom laser */
	public void shoot() throws IOException 
	{
 		GamePanel.stage.addLaser("enemy",new DosLaser(x,y,this));
		AudioTool.playSFX(toString(), "Laz");
		
		shootTimer = new Timer(600);
 		shooting = true;
	}

 	public void hide()
 	{dead=true;}

 	public void switchOrientation() throws IOException
 	{
 		if(orientation.equalsIgnoreCase("Horizontal"))
 			orientation = "Vertical";
 		else
 			orientation = "Horizontal";
 		AudioTool.playSFX(toString(), "Switch");
 		loadImages();
 	}
 	
	public void loadImages() throws IOException
	{
 		safeIMG = ResourceLoader.loadVisual("Visual/Gameplay/"+toString()+"/"+orientation+"/"+toString()+".png");
 		hitIMG = ResourceLoader.loadVisual("Visual/Gameplay/"+toString()+"/"+orientation+"/"+toString()+"-Hit.png");
 		dosII = new ImageIcon(safeIMG);
	}
	
 	//Accessors
 	public int getX()
 	{return x;}
 	public int getY()
 	{return y;}
 	public double getPow()
 	{return POW;}
 	public double getDef()
 	{return DEF;}
 	public int getSpeed()
 	{
 		if(orientation.equalsIgnoreCase("Horizontal"))
 			return yspd;
 		return xspd;
 	}
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
 	public String getOrientation()
 	{return orientation;}
 	public boolean isDead()
 	{return dead;}
 	public boolean isShooting()
 	{return shooting;}
 	//Border checks
 	public boolean yBorderCheck()
 	{return y <= (int)(GamePanel.HEIGHT*0.05);}
 	public boolean xBorderCheck()
 	{return x >= (int)((GamePanel.WIDTH*0.8)-dosII.getIconWidth()) || x <= (int)(GamePanel.WIDTH*0.2);}
 	//Modifiers
	public void setX(int newX)
	{x = newX;}
	public void setY(int newY)
	{y = newY;} 
 	//ToString
	public String toString()
	{return this.getClass().getSimpleName();}
}
