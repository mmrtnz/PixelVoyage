package com.muhammad.spacegame;

import java.awt.Image;
import java.io.IOException;

import javax.swing.ImageIcon;

import com.muhammad.navigation.GamePanel;
import com.muhammad.utilities.AudioTool;
import com.muhammad.utilities.ResourceLoader;
import com.muhammad.utilities.Timer;

public class Karmok implements Boss
{
 	private int pow, xspd, yspd, x, y, tick, level, laserSpeed, distance;
 	private double health;
 	private char action;
 	private boolean hit, dead;
 	private Image safeIMG, hitIMG, healthBar, healthSegment;
 	private ImageIcon bossII;
 	private String laz0, lazX, visualAddress;
 	private Timer shootTimer;
 	
 	public Karmok(int level) throws IOException
 	{
 		this.level = level;
 		//Images
 		safeIMG = ResourceLoader.loadVisual("Visual/Gameplay/Boss/"+toString()+"/"+toString()+".png");
 		hitIMG = ResourceLoader.loadVisual("Visual/Gameplay/Boss/"+toString()+"/"+toString()+"-Hit.png");
 		bossII = new ImageIcon(safeIMG);
 		healthBar = ResourceLoader.loadVisual("Visual/Gameplay/Boss/"+toString()+"/Health/Bar.png");
 		healthSegment = ResourceLoader.loadVisual("Visual/Gameplay/Boss/"+toString()+"/Health/green.png");
 		//Integers
 		x = (int)(GamePanel.WIDTH/2)-(int)(bossII.getIconWidth()/2);
 		y = (-1)*bossII.getIconHeight();
 		pow = (int)((2.0*level+30)/10.0);//4+2((x/5)-1)
 		xspd = 2;
 		yspd = 2;
 		tick = 0;
 		hit = false;
 		dead = false;
 		//Visuals
 		laz0 = "Visual/Gameplay/Boss/"+toString()+"/Laz-0.png";
 		lazX = "/Visual/Gameplay/Boss/"+toString()+"/Laz-0.gif";
 		visualAddress = laz0;
 		laserSpeed= new ImageIcon(ResourceLoader.loadVisualGif(lazX)).getIconHeight();//Height of laser
 		//Segments that can fit into the health bar
 		int maxSegments = (int)(new ImageIcon(healthBar).getIconWidth()/new ImageIcon(healthSegment).getIconWidth());
 		health = maxSegments;
 		//Each method assigned a char
 		action = 'I';
 		//Used for shoot(), it's a counter
 		distance = GamePanel.WIDTH-bossII.getIconWidth()-20;
 	}
 	
	public void act()
	{
		tick++;
		if(!dead)
			switch(action)
			{
			case 'I':
				introduce();
				break;
			case 'M':
				move();
				break;
			case 'W':
				sweep();
				break;
			case 'S':
				shoot();
				action='W';
				break;
			case 'X':
				superMove();
				break;
			}
	}
	//I
	public void introduce()
	{
		if(y<=GamePanel.HEIGHT*0.06)//Moving to screen
			y+=4;
		else
			action = 'M';
	}
	//M
	public void move()
	{
		//Borders
		if(x<=0 || x>=(GamePanel.WIDTH-bossII.getIconWidth()-5))
		{	
			xspd*=-1;
			if(Math.random()<=0.5)//50%chance of shooting
				action='W';
		}
		if(y<=GamePanel.HEIGHT*0.05 || y>=(GamePanel.HEIGHT/6))
			yspd*=-1;
		//1% super move
		if(Math.random()<=0.01)
		{
			shootTimer = new Timer(5000);
			action='X';
		}
		//10% normal shoot
		if(Math.abs(Math.sin((Math.PI*tick)/40.0))==1)
		{	
			visualAddress=laz0;
			shoot();
		}
		//Movement
		y+=yspd;
		x+=xspd;
	}
	//S
	public void shoot()
	{
 		Laser laz;
 		int y = this.y + (int)(bossII.getIconHeight()*0.8);
 		int x = this.x + (int)((bossII.getIconWidth())*0.4);
 		try
 		{
 			if(visualAddress.equalsIgnoreCase(lazX))
 				laz = new Laser(x, y, laserSpeed, pow, ResourceLoader.loadVisualGif(visualAddress));
 			else
 				laz = new Laser(x, y, laserSpeed, pow, visualAddress);
	 		GamePanel.stage.addLaser("enemy",laz);
	 		AudioTool.playSFX(toString(), "Laz");
 		}
 		catch(IOException e)
 		{System.err.println("BO_SH");}
	}
	//W - Boss moves from one corner to (just before) the other.
	public void sweep()
	{
		if(distance>0)
		{
			x+=xspd*4;
			distance-=Math.abs(xspd*4);
			if(Math.abs(Math.sin((Math.PI*tick)/4.0))==1)
			{
				visualAddress=laz0;
				shoot();
			}
		}
		else
		{
			//Resets counter so it doesn't get stuck at L&R borders
	 		distance = GamePanel.WIDTH-bossII.getIconWidth()-20;
			action='M';
		}
	}
	//X
	public void superMove()
	{
		if(centerSelf())
		{
			visualAddress=lazX;
			shoot();
			if(shootTimer.done())
				action='M';
		}
	}
	
	public boolean centerSelf()
	{
		//Top Center
		int centerX = (GamePanel.WIDTH/2)-(bossII.getIconWidth()/2);
		int y = (int)(GamePanel.HEIGHT*0.08);//Slightly below top border
		//Finding direction to center
		int xspd=0;
		int yspd=0;
		if(x>centerX)
			xspd=Math.abs(this.xspd)*(-2);
		else if (x<centerX)
			xspd=Math.abs(this.xspd)*2;
		if(this.y>y)
			yspd=Math.abs(yspd)*(-1);
		else
			yspd=Math.abs(yspd);
		//Movement
		x+=xspd;
		y+=yspd;
		//Check if center, give some leeway
		if(this.x<=centerX+xspd && this.x>=centerX-xspd)
			return true;		
		return false;
	}
	
	/* Loses health. When dead it's coordinates are saved before being moved 
	 * off screen. Multiple score strings are then randomly spawned with a 
	 * specific range. It looks really cool. 
	 */
	public void hit(double sspow) 
	{
		hit=true;
		double damage = 1+(sspow*5.0);
		double healthPerSegment = (level/5.0);
		health-=damage/healthPerSegment;
		AudioTool.playSFX(toString(), "Hit");
		if(health<=0)
		{
			int x = this.x;
			int y = this.y;
			AudioTool.playSFX(toString(), "Dead");
			hide();
			//Displays multiple score strings, looks nicer.
			for(int cnt = 0; cnt<20; cnt++)
			{
				int kx = x+(int)(Math.random()*bossII.getIconWidth());
				int ky = y+(int)(Math.random()*bossII.getIconHeight());
				GamePanel.stage.addScoreString(new ScoreString(10,kx,ky));
				GamePanel.spaceship.setScore(GamePanel.spaceship.getScore()+10);
			}
		}
	}
	
 	public void hide()
 	{dead=true; x=GamePanel.WIDTH+10;}
 	
	//Accessors 
	public int getX()
	{return x;}
	public int getY()
	{return y;}
 	public double getPow()
 	{return pow;}
	public double getHealth()
	{return health;}
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
	public Image getHealthBar()
	{return healthBar;}
	public Image getHealthSegment()//Make color vary with each boss
	{return healthSegment;}
	public boolean isDead() 
	{return dead;}
	//Modifiers
	public void setX(int x) 
	{this.x=x;} 
	public void setY(int y)
	{this.y=y;}
 	//ToString
	public String toString()
	{return this.getClass().getSimpleName();}
}
