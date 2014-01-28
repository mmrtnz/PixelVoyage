package com.muhammad.spacegame;

import java.io.IOException;

import com.muhammad.navigation.GamePanel;

public class Planet extends Laser 
{
	public boolean doneMoving;
	
	//Default
	public Planet(int type) throws IOException 
	{
		super(0,-400, 1, 0,"Visual/Shop/World-"+type+"/planet-1.png");
		doneMoving=false;
	}
	
 	
 	public void move(){
 		if(super.y < GamePanel.HEIGHT*0.1)
 			super.y += super.speed;
 		else
 			doneMoving=true;
 	}
	
	//ToString
	public String toString()
	{return this.getClass().getSimpleName();}	
}
