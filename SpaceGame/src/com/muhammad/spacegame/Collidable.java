package com.muhammad.spacegame;

import java.awt.Image;

/**Muhammad Martinez*/

/*
 * Objects that are painted on the GamePanel and use the 
 * CollisionDetector. These include PU's, enemies, projectiles,
 * and the spaceship. This allows all of said elements to be
 * compared with the same collision method.
 */
public interface Collidable 
{
 	public int getX();
 	public int getY();
 	public Image getImage();
}
