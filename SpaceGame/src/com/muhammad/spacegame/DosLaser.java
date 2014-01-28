package com.muhammad.spacegame;

import java.io.IOException;

import com.muhammad.utilities.ResourceLoader;

/** 
 * A dos laser is attached to the enemy. Increasing the enemies size 
 * thus it's chances of colliding.
 */
public class DosLaser extends Laser
{
	private Dos dos;
	private String originalOrientation;
	public DosLaser(int x, int y, Dos dos) throws IOException
	{
		super(x,y,dos.getSpeed(),5.0,ResourceLoader.loadVisualGif("/Visual/Gameplay/Dos/"+dos.getOrientation()+"/Dos-Laz.gif"));
		this.dos=dos;
		originalOrientation=dos.getOrientation();
	}
	
	/* Follows Dos until no longer shooting or orientation switch */
 	public void move()
 	{
 		if(!originalOrientation.equalsIgnoreCase(dos.getOrientation()) || !dos.isShooting())
 			super.hide();
 		
 		if(dos.getOrientation().equalsIgnoreCase("Horizontal"))
 		{
 	 		x=dos.getX()-30;
 	 		y=dos.getY();
 		}
 		else
 		{
 	 		x=dos.getX();
 	 		y=dos.getY()-30;
 		}
 	}
}
