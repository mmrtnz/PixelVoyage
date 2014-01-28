package com.muhammad.spacegame;

import java.awt.Image;

import javax.swing.ImageIcon;
import com.muhammad.navigation.GamePanel;
import com.muhammad.utilities.AudioTool;
import com.muhammad.utilities.ResourceLoader;

/* NOTES: 
 * Image method may not work when exporting to JAR
 */
public class PowerUp implements Collidable
{
	/**
	 * @param activated - Determines whether the PU is off screen 
	 * 			e.g. when contacting ship or going off screen. Allows for
	 * 			memory to be recycled.
	 */
	private int x,y,xspd,yspd,type;
	private boolean activated;
	private String visualAddress;
	private ImageIcon powII;
	private Image img;
	
	//Default
	public PowerUp()
	{
		this.x=-52;//Specifically hardcoded for debugging purposes
		this.y=-52;
		xspd=0;
		yspd=0;
		activated=true;
		type=generateType();
		visualAddress="/Visual/Gameplay/"+toString()+"/"+type+".gif";
		//Image
		img = ResourceLoader.loadVisualGif(visualAddress);
		powII = new ImageIcon(img);
	}
	
	//Overloaded
	public PowerUp(int x, int y)
	{
		this.x=x;
		this.y=y;
		xspd=(int)((Math.random()*2)*(-1))+2;
		yspd=(int)(Math.random()*3)+1;
		activated=false;//Has spawned, it's memory cannot be used
		type=generateType();
		visualAddress="/Visual/Gameplay/"+toString()+"/"+type+".gif";
		//Image
		img = ResourceLoader.loadVisualGif(visualAddress);
		powII = new ImageIcon(img);
	}
	
	/*
	 * Generates a random type of image. Least -> Most likely
	 * TODO: Make random selection intelligent and dependent on level.
	 */
	public char generateType()
	{
		if(Math.random()<=0.1)//10%
			return 3;//Bomb
		if(Math.random()<=0.25)//25%
			return 2;//Spitfire
		if(Math.random()<=0.50)//50%
			return 0;//Health
		//Default
		return 1;//Recharge
	}
	
	/*
	 * Updates activate var by checking if on screen, then moves.
	 */
	public void move()
	{
 		if(y<(-1)*powII.getIconHeight() || y>GamePanel.HEIGHT)
 			activated=true;
 		if(x<0 || x>GamePanel.WIDTH)
 			activated=true;
		x+=xspd;
		y+=yspd;
	}
	
	/*
	 * Modifies spaceship depending on type. Updates activated var signaling 
	 * that it is available for relocation. 
	 */
	public void activate()
	{
		switch(type)
		{
		case 0:
			GamePanel.spaceship.heal();
			GamePanel.stage.addScoreString(new ScoreString(1,x,y));
 			GamePanel.spaceship.setScore(GamePanel.spaceship.getScore()+1);
			break;
		case 1:
			GamePanel.spaceship.recharge();
			GamePanel.stage.addScoreString(new ScoreString(1,x,y));
 			GamePanel.spaceship.setScore(GamePanel.spaceship.getScore()+1);
			break;
		case 2:
			GamePanel.spaceship.recharge();
			GamePanel.spaceship.shootConstant();
			GamePanel.stage.addScoreString(new ScoreString(2,x,y));
 			GamePanel.spaceship.setScore(GamePanel.spaceship.getScore()+2);
			break;
		case 3: //Damages all enemies on screen
		{
			for(Enemy e : GamePanel.stage.getEnemies())
				if(!e.isDead() && e.getY()>0)
				{
					img = ResourceLoader.loadVisualGif("/Visual/Gameplay/PowerUp/3X.png");
					powII = new ImageIcon(img);
					e.hit(0.75);
					e.hit(0.75);
				}
			GamePanel.stage.addScoreString(new ScoreString(3,x,y));
 			GamePanel.spaceship.setScore(GamePanel.spaceship.getScore()+3);
			
			break;
		}
	}
		activated=true;
		AudioTool.playSFX("Audio/Gameplay/"+toString()+".au");
		//Hide
		x=GamePanel.WIDTH;
	}
	
 	//Accessors
 	public int getX()
 	{return x;}
 	public int getY()
 	{return y;}
 	public Image getImage()
 	{return img;}
 	public ImageIcon getII()
 	{return powII;}
 	public boolean hasBeenActivated()
 	{return activated;}
 	
	//Modifiers
	public void setX(int newX)
	{x = newX;}
	public void setY(int newY)
	{y = newY;}
	
 	//ToString
	public String toString()
	{return this.getClass().getSimpleName();}	
}
