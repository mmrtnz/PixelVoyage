package com.muhammad.spacegame;

/** 
 * Originally designed by: Kevin Chorath 
 * Modified by: Muhammad Martinez
 */

import java.awt.Image;
import java.io.IOException;

 public interface Enemy extends Collidable
 {
	/* Called with every iteration. It acts as the conductor of all 
	 * other behavior methods such as the shoot and move methods. 
	 * Usually contains a switch statement that determines what to 
	 * do.
	 */
	public void act();
	
 	/* Called when enemy makes contact with the spaceship laser or 
 	 * spaceship itself. Deducts health from ONLY itself. The amount 
 	 * deducted is a percentage determined by 'sspow' -- the spaceships
 	 * power variable. Some change their image. When health reaches 
 	 * 0, a score is shown and added to the spaceship's total score. 
 	 * Enemy then is marked "dead." 
 	 */
 	public void hit(double sspow);
 	
 	/* The enemy's method of combat. Normally it adds a laser to the 
 	 * enemy laser AL and playing its sound effect. Some enemies call
 	 * specific movement patterns.
 	 */
 	public void shoot() throws IOException;
 	
 	/* Occurs when spaceship collides with enemies (except for bosses). 
 	 * Enemy dies instantly but no points are given. 
 	 */
 	public void hide();
 	
 	//Accessors
 	public int getX();
 	public int getY();
 	public double getPow();
 	public Image getImage();
 	public boolean isDead();
 	
 	//Modifiers
 	public void setX(int x);
 	public void setY(int y);
 	
 	//ToString
 	public String toString();
 }