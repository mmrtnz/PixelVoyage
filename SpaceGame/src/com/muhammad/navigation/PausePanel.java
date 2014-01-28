package com.muhammad.navigation;

/** Muhammad Martinez */

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.JPanel;

import com.muhammad.utilities.ResourceLoader;

/* A simple panel that is slightly transparent and with an overlay. Simpler
 * than InfoPanel.
 */
public class PausePanel extends JPanel implements State 
{
	private static final long serialVersionUID = 1L;
	private int width, height;
	private Image background;
	
	//Overloaded
	public PausePanel(int width, int height)
	{
		this.width = width;
		this.height = height;
		//Slightly transparent
		setBackground(new Color(0,0,0,64));
		//Overlay
		background = ResourceLoader.loadVisual("Visual/pause.png");
	}

	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		g.drawImage(background, 0, 0, width, height, this);	
	}
	
	public void tick(){}
	public void render(){}
	public void reset(){}
	public String toString()
	{return "Pause";}	
}
