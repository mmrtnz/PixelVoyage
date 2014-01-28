package com.muhammad.navigation;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.ImageIcon;

import com.muhammad.utilities.AudioTool;
import com.muhammad.utilities.PngJButton;
import com.muhammad.utilities.ResourceLoader;


//Possible error when exporting to jar
public class MenuPanel extends JPanel implements State 
{
	private static final long serialVersionUID = 1L;
	private PngJButton startB, highscoresB, optionsB, helpB;
	private Image [] buttpics;
	private int width, height;
	public static Image bg,bgA,bgB,bgC,bgD;
	
	//Overloaded
	public MenuPanel(int width, int height) throws IOException
	{	
		this.width = width;
		this.height = height;
		//this.add(Game.bg);
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		//Button images
		buttpics = new Image[10];
		for(int k=0; k<buttpics.length; k++)
			buttpics[k] = ResourceLoader.loadVisual("Visual/Buttons/"+k+".png");
		//The way these images are added may cause trouble when creating jar,
		//use the ResourceLoader if this occurs.
		initPanels();
		initButts();
		//Background
		bg = Game.background;
		bgA = ResourceLoader.loadVisual("Visual/bg-A.png");
		bgB = ResourceLoader.loadVisualGif("/Visual/bg-B.gif");//new ImageIcon("res/Visual/bg-B.gif").getImage();
		bgC = ResourceLoader.loadVisualGif("/Visual/bg-C.gif");
		bgD = ResourceLoader.loadVisual("Visual/bg-D.png");
		//Filling space
		for(int k=0; k<5;k++)
		{
			PngJButton n = new PngJButton();
			n.setAlignmentX(CENTER_ALIGNMENT);
			add(n);
		}
		//Adding buttons
		add(startB);
		add(highscoresB);
		add(optionsB);
		add(helpB);
	}
	/**
	 * Initializes JButton with appropriate image, then adds customized 
	 * Action Listener. Hard-coded counter, adds after used.
	 */
	public void initButts() 
	{
		int count = 0;
		
		startB = new PngJButton(buttpics[count++]);
		startB.setPressedIcon(new ImageIcon(buttpics[count++]));
		startB.setAlignmentX(CENTER_ALIGNMENT);
		startB.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e) 
			{ 
				playSFX();
				try //Creates a new game
				{Game.addState(new GamePanel(width, height));} 
				catch (IOException e1) 
				{e1.printStackTrace();}
			}
		});	
		
		highscoresB = new PngJButton(buttpics[count++]);
		highscoresB.setPressedIcon(new ImageIcon(buttpics[count++]));
		highscoresB.setAlignmentX(CENTER_ALIGNMENT);
		highscoresB.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e) 
			{ 
				playSFX(); 
				try 
				{Game.addState(new HighScorePanel(0));}
				catch (IOException e1) 
				{e1.printStackTrace();}
			}
		});	
		
		optionsB = new PngJButton(buttpics[count++]);
		optionsB.setPressedIcon(new ImageIcon(buttpics[count++]));
		optionsB.setAlignmentX(CENTER_ALIGNMENT);
		optionsB.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e) 
			{ 	
				playSFX(); 
				try {Game.addState(new OptionsPanel());} 
				catch (IOException e1) 
				{e1.printStackTrace();}
			}
		});	
		
		helpB = new PngJButton(buttpics[count++]);
		helpB.setPressedIcon(new ImageIcon(buttpics[count++]));
		helpB.setAlignmentX(CENTER_ALIGNMENT);
		helpB.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e) 
			{ 	
				playSFX(); 
				try {Game.addState(new HelpPanel());} 
				catch (IOException e1) 
				{e1.printStackTrace();}
			}
		});	
	}
	
	/**
	 * The same JPanels will be used throughout the program. They will be
	 * added and removed from the stack and card layout as needed and will
	 * be reseted instead of deleted/recreated.
	 */
	public void initPanels() throws IOException
	{
	}
	
	//Audio
	public void playSFX()
	{
		AudioTool.playSFX("Audio/select.au");
	}
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		//Background
		g.drawImage(bg, 0, 0, this.getSize().width, this.getSize().height, this);
		g.drawImage(bgA, 0, 0, this.getSize().width, this.getSize().height, this);
		g.drawImage(bgB, 0, 2, this.getSize().width, this.getSize().height, this);
		g.drawImage(bgC, 0, 0, this.getSize().width, this.getSize().height, this);
		g.drawImage(bgD, 0, 0, this.getSize().width, this.getSize().height, this);
	}
	public void tick(){}
	public void render(){}
	public void reset(){}
	public String toString()
	{return "Menu";}
}
