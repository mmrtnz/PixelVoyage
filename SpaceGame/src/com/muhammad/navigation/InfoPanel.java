package com.muhammad.navigation;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import com.muhammad.utilities.AudioTool;
import com.muhammad.utilities.PngJButton;
import com.muhammad.utilities.ResourceLoader;

/*
 * An InfoPanel displays a non-interactive, static JPanel. An InfoPanel 
 * displays a message as the background and contains a back button.
 */
public class InfoPanel extends JPanel implements State 
{
	private static final long serialVersionUID = 1L;
	private Image deselect, select;
	protected PngJButton backbutton;
	protected String name;
	protected Image overlay;
	
	public InfoPanel(String name) throws IOException 
	{
		this.name = name;
		overlay = null;
		//Initializing Back Button
		deselect  = ResourceLoader.loadVisual("Visual/Buttons/10.png");
		select  = ResourceLoader.loadVisual("Visual/Buttons/11.png");
		backbutton = new PngJButton(deselect);
		backbutton.setPressedIcon(new ImageIcon(select));
		backbutton.setAlignmentX(CENTER_ALIGNMENT);
		backbutton.setAlignmentY(BOTTOM_ALIGNMENT);
		backbutton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e) 
			{ 
				AudioTool.playSFX("Audio/select.au");
				Game.returnHome();
			}
		});
		/**
		 * An explanation as to why this works: ImageIcon invokes a 
		 * mediatracker to keep track of animated gifs. Other methods 
		 * include the inserting the ImageIcon into a JLabel but it 
		 * only produced a static image. In my original SpaceGame, a 
		 * mediatracker was manually created and used. I am not sure 
		 * if this will properlly tranfer into a jar file.
		 * 
		 * http://stackoverflow.com/questions/10836832/show-an-animated-bg-in-swing
		 */
	}	
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);		
		//Background
		g.drawImage(MenuPanel.bg, 0, 0, this.getSize().width, this.getSize().height, this);
		g.drawImage(MenuPanel.bgA, 0, 0, this.getSize().width, this.getSize().height, this);
		//Overlay
		if(overlay!=null)
			g.drawImage(overlay, 0, 0, this.getSize().width, this.getSize().height, this);
		g.setFont(Game.gameFont.deriveFont(20f));
		g.setColor(new Color(255,78,38));
		//Top Center
		g.drawString(""+name, (int)(this.getSize().width*0.5)-name.length()*10, (int)(this.getSize().height*0.1));
	}	
	
	public void tick(){}
	public void render() 
	{this.repaint();}
	public void reset() {}
	public String toString()
	{return name;}
}
