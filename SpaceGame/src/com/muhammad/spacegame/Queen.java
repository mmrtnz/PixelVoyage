package com.muhammad.spacegame;

import java.io.IOException;


import com.muhammad.navigation.GamePanel;
import com.muhammad.utilities.AudioTool;
import com.muhammad.utilities.Timer;

public class Queen extends Ray implements Enemy
{	
	private final int MAXSHOTS;
	private final long INTERVAL;
	private int cnt;
	private Spaceship ss;
	
	public Queen(int x, int y, Spaceship ss) throws IOException
	{
		//X, Y, xspd, yspd, pow, hp, value, imga, imgb
		super(x,y,4,3,2,20,60,("Visual/Gameplay/Queen/Queen.png"),("Visual/Gameplay/Queen/Queen-Hit.png"));
		super.laserSpeed=2;
		MAXSHOTS=2;
		INTERVAL=1000;
		cnt=0;
		this.ss=ss;
	}

 	/* Moves diagonally and bounces of an invisible box border */
 	public void move()
 	{	
		//Borders
		if(y >= bottomBorder || y <= topBorder)
			yspd*=(-1);
		if(x >= GamePanel.WIDTH-rayII.getIconWidth() || x <= 0)
			xspd*=(-1);
		//5% shoot chance
		if(Math.random()<=0.05)
			action='S';
		//Move
 		x+=xspd;
		y+=yspd;
 	}
	
 	/* Override, shoots X times at set interval */
 	public void shoot() throws IOException
 	{
 		//Timer check
 		if(shootTimer == null)
 			shootTimer = new Timer(INTERVAL);
 		//Shoots at interval
 		if(shootTimer.done() && cnt++ <= MAXSHOTS)
 		{
 			//Creating queen laser, 2 image types
 			String path = "Visual/Gameplay/"+toString()+"/"+toString()+"-Laz-";
 			QueenLaser ql;
 			if(cnt%2==0)
 				ql = new QueenLaser(x+3,y+rayII.getIconHeight()-2,pow,path+"0.png",ss);
 			else
 				ql = new QueenLaser(x+rayII.getIconWidth()-3, y+rayII.getIconHeight()-3,pow,path+"1.png",ss);
	 		//Add laser
 			GamePanel.stage.addLaser("Enemy", ql);
	 		AudioTool.playSFX(toString(), "Laz");
	 		//Reset timer
	 		shootTimer = new Timer(INTERVAL);
 		}
	 	else if(cnt >= MAXSHOTS)
	 	{
	 		cnt=0;
	 		action='M';
	 	}
 	}
	
 	//ToString
	public String toString()
	{return this.getClass().getSimpleName();}	
}
