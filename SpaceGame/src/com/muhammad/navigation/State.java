/**
 * Muhammad Martinez
 *
 * @version 1.00 2013/4/7
 */

package com.muhammad.navigation;

public interface State
{
	public void tick(); //Next iteration
	public void render(); //Essentially this just calls the paintcomponent
	public void reset(); //Resets variables
	public String toString(); //Returns name of class
}
