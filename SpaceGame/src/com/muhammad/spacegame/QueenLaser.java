package com.muhammad.spacegame;

import java.io.IOException;

import javax.swing.ImageIcon;

import com.muhammad.utilities.AudioTool;

/**
 * A queen laser follows the spaceship.
 */
public class QueenLaser extends Laser
{
	private Spaceship ss;
	private int xspd, yspd, ssWidth;
	
	public QueenLaser(int x, int y, double pow, String path, Spaceship ss) throws IOException
	{
		super(x,y,0,pow,path); //Ignore spd, we're overriding move anyway.
		this.ss=ss;
		xspd=0;
		yspd=4;
		ssWidth=new ImageIcon(ss.getImage()).getIconWidth();
	}
	
	public void move()
	{
		getDirection();
		x+=xspd;
		y+=yspd;
		if(CollisionDetector.collide(ss, this))
			AudioTool.playSFX("Audio/Gameplay/Queen-Laz-Explode.au");
	}
	
	public void getDirection()
	{
		//If the spaceship is...
		if(x <= ss.getX()) //To the right
			xspd=3; //Laser moves right
		else if (x >= ss.getX()+ssWidth) //To the left
			xspd=-3; //Laser moves left
		else if(x > ss.getX() && x < ss.getX()+ssWidth) //Directly above or below
			xspd=0; //Laser moves neither left or right
		
		//Always moves down
		yspd=3;
	}
}
