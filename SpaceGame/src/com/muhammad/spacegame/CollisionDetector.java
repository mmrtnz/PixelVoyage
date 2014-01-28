package com.muhammad.spacegame;

/** Muhammad Martinez */

import javax.swing.ImageIcon;

public class CollisionDetector 
{

	//Calculation
	public static boolean collide(Collidable a, Collidable b)
	{
		//ImageIcon so that we can get their width and height
		ImageIcon aII = new ImageIcon(a.getImage());
		ImageIcon bII = new ImageIcon(b.getImage());
		
		if(a.getX() >= b.getX() && a.getX() <= (b.getX()+bII.getIconWidth()+2)) //In the same X range
			if(a.getY() >= b.getY() && a.getY() <= (b.getY()+bII.getIconHeight()+2)) //In the same Y range
				return true;
		//Switched
		if(b.getX() >= a.getX() && b.getX() <= (a.getX()+aII.getIconWidth()+2)) //In the same X range
			if(b.getY() >= a.getY() && b.getY() <= (a.getY()+aII.getIconHeight()+2)) //In the same Y range
				return true;
		
		return false;
	}
}
