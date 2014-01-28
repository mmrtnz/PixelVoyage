package com.muhammad.spacegame;

import java.awt.Color;

public class ScoreString
{
	private int value,x,y,yspd,tick;
	private Color col;
	
	public ScoreString(int amt, int x, int y)
	{
		this.x=x;
		this.y=y;
		this.value=amt;
		col=generateColor();
		yspd=(int)(Math.random()*2)*(-2)-1;
		tick=15;
	}
	//Color of string when printing score
	public Color generateColor()
	{
		//Picks a random color from palette
		int num = (int)(Math.random()*4);
		Color col;
		switch(num)
		{
		case(0)://Orange
			col = new Color(255,110,3);
			break;
		case(1)://Yellow
			col = new Color(255,201,3);
			break;
		case(2)://Hot Pink/ Fuscia
			col = new Color(255,3,168);
			break;
		case(3)://Turquise/Aqua 
			col = new Color(3,255,228);
			break;
		default://White
			col = new Color(255,255,255);
			break;
		}
		return col;
	}
	//Moves
	public void move()
	{
		y+=yspd;
		//Disappears quickly
		if(--tick<=0)
			y=-10;
	}
	//Alternates between color and white
	public Color getColor()
	{
		if(Math.abs(Math.sin((Math.PI*tick)/10.0))==1)
			return new Color(255,255,255);//White
		return col;
	}
	//Accessors
 	public int getX()
 	{return x;}
 	public int getY()
 	{return y;}
 	public int getValue()
 	{return value;}
}
