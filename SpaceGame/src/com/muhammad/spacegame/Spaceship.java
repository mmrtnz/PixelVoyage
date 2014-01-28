package com.muhammad.spacegame;

/**Muhammad Martinez*/
 
import java.awt.*;
import java.io.IOException;
import javax.swing.ImageIcon;

import com.muhammad.navigation.GamePanel;
import com.muhammad.utilities.AudioTool;
import com.muhammad.utilities.ResourceLoader;

 public class Spaceship implements Collidable
 {
	final int MAXHEALTH = 12; //12 Halves
 	private int x, y, speed, laserSpeed, energy, score;
 	public double [] heartArray = {2,2,2,2,2,2};
 	private double eps, pow, def, currentHealth, tl; //Energy cost per laser
 	public static boolean hit;
 	public boolean dead, shootConstant;
 	private Image red, redS, redR, redL, white, whiteS, whiteR, whiteL, flyin, laser, energyBar;
 	private ImageIcon redII; //Used for image width and height methods.
 	
 	//Default only
 	public Spaceship() throws IOException
 	{
		//Starting position
		x = (GamePanel.WIDTH/2)-30;
 		y = GamePanel.HEIGHT-100;
 		score = 0;
 		speed = 15;
 		laserSpeed = -25;//Moves up
 		currentHealth = MAXHEALTH;
 		dead = false;
 		shootConstant = false;
 		//Images
 		redS = ResourceLoader.loadVisual("Visual/Gameplay/"+toString()+"/S.png");
 		redR = ResourceLoader.loadVisual("Visual/Gameplay/"+toString()+"/R.png");
 		redL = ResourceLoader.loadVisual("Visual/Gameplay/"+toString()+"/L.png");
 		whiteS = ResourceLoader.loadVisual("Visual/Gameplay/"+toString()+"/SH.png");
 		whiteR = ResourceLoader.loadVisual("Visual/Gameplay/"+toString()+"/RH.png");
 		whiteL = ResourceLoader.loadVisual("Visual/Gameplay/"+toString()+"/LH.png");
 	//	flyin = new ImageIcon("res/Visual/Gameplay/Spaceship/flyIn.gif").getImage();
 		laser = ResourceLoader.loadVisual("Visual/Gameplay/"+toString()+"/Laz.png");
 		energyBar = ResourceLoader.loadVisual("Visual/Gameplay/"+toString()+"/energyBar.png");
 		red = redS;
 		white = whiteS;
 		redII = new ImageIcon(red);
 		//Energy & Other variables
 		energy = (int)(new ImageIcon(energyBar).getIconWidth()/new ImageIcon(laser).getIconWidth());//60
 		eps = 2.0;
 		pow = 1.0;
 		def = 1.0;
 		tl = 0.0;
 	}
  	
 	// Adds laser to laser A if enough energy.
 	public void shoot() throws IOException
 	{
 		String lazVisAddress = "Visual/Gameplay/"+toString()+"/Laz.png";
 		int lazX = (x+((int)redII.getIconWidth()/2))-1; //Centered
 		//Creates a laser at front center of spaceship
 		if(energy>0)
 		{
 			//Laser power not used yet. Spaceship power only.
 			Laser tempLaser = new Laser(lazX, y, laserSpeed, 0, lazVisAddress);
 	 		//Add laser
 	 		GamePanel.stage.addLaser("spaceship", tempLaser);
 	 		AudioTool.playSFX(toString(), "Laz");
 	 		energy = (int)(energy-eps);
 		}
 		if(shootConstant)
 			shootConstant();//Updates
 	}
 	
 	public void hit(double damage)
 	{
 		hit = true;
 		currentHealth -= damage;
 		double tempHealth = currentHealth;
 		int cnt = 0;
 		//Audio
 		AudioTool.playSFX(toString(), "Hit");
 		//Shifts hearts
 		while(cnt <= heartArray.length-1)
 		{
 			if(tempHealth >= 2)
 			{
 				heartArray[cnt] = 2;
 				tempHealth -= 2;
 			}
 			else if(tempHealth==1)
 			{
 				heartArray[cnt] = 1;
 				tempHealth -= 1;
 			}
 			else
 				heartArray[cnt] = 0;
 			

 			cnt++;
 		}
 		//Determining if the spaceship is dead
 		if(heartArray[0] == 0)
 		{
	 		dead = true;
	 		x=GamePanel.WIDTH+10;
	 		AudioTool.playSFX(toString(), "Dead");
 		}	
 	}
 	
 	//Movement
	public void moveUP(){
		if(y > 0)
			y -= speed;
	}
	public void moveDOWN(){
		if(y < GamePanel.HEIGHT - redII.getIconHeight())
			y += speed;
	}
	public void moveLEFT(){
		if(x > 0)
			x -= speed;
		red=redL;
		white=whiteL;
	}
	public void moveRIGHT(){
		if(x < GamePanel.WIDTH - redII.getIconWidth())
			x += speed;
		red=redR;
		white=whiteR;
	}
	public void resetImage(){
		red=redS;
		white=whiteS;
	}
	
	//Accessors
 	public int getX()
 	{return x;}
 	public int getY()
 	{return y;}
 	public int getScore()
 	{return score;}
 	public boolean isHit()
 	{return hit;}
 	public boolean isDead()
 	{return dead;}
 	public Image getImage()
 	{
	 	if(hit)
	 	{
	 		if(Math.random()<0.2)
	 			hit = false;
	 		return white;
	 	}
	 	else
	 		return red;	
 	}
 	public Image getLaserImage()
 	{return laser;}
 	public Image getFlyInImage()
 	{return flyin;}
 	public Image getEBar()
 	{return energyBar;}
 	public int getEnergy()
 	{return energy;}
 	public double getPow()
 	{return pow;}
 	public double getDef()
 	{return def;}
 	public double getEPS()
 	{return eps;}
 	public double getTotalLasers()
 	{return tl;}
 	//Modifiers
 	public void setScore(int score)
 	{this.score = score;}
 	public void recharge()
 	{energy = (int)(new ImageIcon(energyBar).getIconWidth()/new ImageIcon(laser).getIconWidth());}
 	public void heal()
 	{
 		for(int k=0;k<heartArray.length;k++)
 			heartArray[k]=2;
 		currentHealth = MAXHEALTH;
 	}
 	public void shootConstant()
 	{
 		if(getEnergy()>0)
 			shootConstant=true;
 		else
 		{
 			shootConstant=false;
 			recharge();
 		}
 	}
 	//Upgrades
 	public void upgrade(char c)
 	{
 		switch(c)
 		{
 		case 'P'://Power
 			if(pow<0.8)
 				pow+=0.2;
 			break;
 		case 'D'://Defense
 			if(def<4)
 				def++;
 			break;
 		case 'A'://Ammo
 			if(eps > 0.2)
 				eps-=0.4;
 			break;
 		case 'L'://Laser
 			tl++;//Add new laser later.
 			break;
 		}
 	}
 	//ToString
	public String toString()
	{return this.getClass().getSimpleName();}
 }