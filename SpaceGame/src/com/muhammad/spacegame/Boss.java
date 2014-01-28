package com.muhammad.spacegame;

import java.awt.Image;

/**
 * Bosses operate on a switch statement that directs them to the 
 * proper method with each iteration. They have there own health 
 * bar. 
 */
public interface Boss extends Enemy 
{
	/* Moves the boss into view */
	public void introduce();
	/* Need I say more? */
	public void superMove();
	/* The following are accessed by the GamePanel in order to 
	 * display the bosses health.
	 */
	public double getHealth();
	public Image getHealthBar();
	public Image getHealthSegment();
}
