package com.muhammad.navigation;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Insets;
import java.io.IOException;

import javax.swing.ImageIcon;

import com.muhammad.utilities.AudioTool;
import com.muhammad.utilities.PngJButton;
import com.muhammad.utilities.ResourceLoader;

public class OptionsPanel extends InfoPanel implements State
{
	private static final long serialVersionUID = 1L;
	private PngJButton music, sfx;
	Image background;
	String name = "OPTIONS";
	
	public OptionsPanel() throws IOException 
	{
		super("OPTIONS");
		super.remove(super.backbutton);//Will be repositioned
		setLayout(new GridBagLayout());
		//Used for formating purposes, basically HTML positioning and
		//padding and such.
		GridBagConstraints l = new GridBagConstraints();
		GridBagConstraints c = new GridBagConstraints();
		//Buttons
		initButts();
		//Customizing 2 buttons
		c.gridwidth=2;
		c.gridheight=2;
		c.fill=GridBagConstraints.HORIZONTAL;
		c.insets=new Insets(5,10,30,15);
		//Positioning then adding buttons
		//Music
		c.gridy=0;
		c.gridx=0;
		add(new PngJButton("Music"),c);
		c.gridx=2;
		add(music,c);
		//SFX
		c.gridy=2;
		c.gridx=0;
		add(new PngJButton("SFX"),c);
		c.gridx=2;
		add(sfx,c);
		//Customizing for invisible buttons to fill space
		l.gridy=10;
		l.gridx=0;
		l.gridwidth=4;
		l.fill=GridBagConstraints.HORIZONTAL;
		//Filling space
		add(new PngJButton(),l);
		l.gridy++;
		add(new PngJButton(),l);
		l.gridy++;
		add(new PngJButton(),l);
		l.gridy++;
		add(new PngJButton(),l);
		l.gridy++;
		//Adding back button
		add(super.backbutton,l);
	}
	
	public void initButts() throws IOException
	{
		music = new PngJButton(ResourceLoader.loadVisual("Visual/Buttons/on.png"));
		music.addActionListener(new ActionListener()
		{
			boolean on = true;
			String s = "Visual/Buttons/";
			public void actionPerformed(ActionEvent e)
			{
				if(on)
				{
					music.setIcon(new ImageIcon(ResourceLoader.loadVisual(s+"off.png")));
					AudioTool.musicOn=false;
					AudioTool.prevSong.stop();
				}
				else
				{
					music.setIcon(new ImageIcon(ResourceLoader.loadVisual(s+"on.png")));
					AudioTool.musicOn=true;
					AudioTool.playSFX("Audio/select.au");
					AudioTool.prevSong.start();
				}
				on=!on;
			}
		});
		sfx = new PngJButton(ResourceLoader.loadVisual("Visual/Buttons/on.png"));
		sfx.addActionListener(new ActionListener()
		{
			boolean on = true;
			String s = "Visual/Buttons/";
			public void actionPerformed(ActionEvent e)
			{
				if(on)
				{
					sfx.setIcon(new ImageIcon(ResourceLoader.loadVisual(s+"off.png")));
					AudioTool.sfxOn=false;
				}
				else
				{
					sfx.setIcon(new ImageIcon(ResourceLoader.loadVisual(s+"on.png")));
					AudioTool.sfxOn=true;
					AudioTool.playSFX("Audio/select.au");
				}
				on=!on;
			}
		});
	}
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);		
		//Background
		g.drawImage(background, 0, 0, this.getSize().width, this.getSize().height, this);
		
		//g.setFont(myFont.deriveFont(20f));
		g.setColor(new Color(255,78,38));
		//Top Center
		g.drawString(""+name, (int)(this.getSize().width*0.5)-name.length()*10, (int)(this.getSize().height*0.1));
		
	}	
}
