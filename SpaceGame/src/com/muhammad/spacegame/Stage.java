package com.muhammad.spacegame;

/** Muhammad Martinez */

import java.io.IOException;
import java.util.ArrayList;

public interface Stage
{
	/* Stages have an A of Enemies. The A has a limited number of elements
	 * and each enemy has an equation that determines (based on current level)
	 * how many of that enemy will compose the A. After filling the A, all 
	 * enemies are repositioned at random locations.
	 */
	public void loadEnemies() throws IOException;
	
	/* Called with every iteration. Updates the status of the stage. Calls the 
	 * following two methods below.
	 */
	public void update();
	
	/* Multiple for loops move elements one class at a time. Elements include
	 * enemies, lasers, powerups, and the user spaceship.
	 */
	public void moveElements();
	
	/* Multiple for loops utilize the CollisionDetector class to detect 
	 * collisions. Each collision is handled differently based on the colliding
	 * elements e.g deducting health or activating effects.
	 */
	public void checkElementCollisions();
	
	/* A simple loop that determines if all enemies are dead/off screen. */
	public boolean isClear();
	
	//Modifiers
	public void addLaser(String str, Laser laz);//String determines array
	public void addPowerUp(PowerUp pu);
	public void addScoreString(ScoreString ss);
	
	//Accessors
	public ArrayList<Enemy> getEnemies();
	public Laser[] getShipLasers();
	public Laser[] getEnemyLasers();
	public PowerUp[] getPowerUps();
	public ArrayList<ScoreString> getEnemyValues();
	//May or may not have
	public Boss getBoss();
	public Laser getPlanet();
}
