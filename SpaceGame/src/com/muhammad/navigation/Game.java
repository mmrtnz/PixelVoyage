/**
 * Muhammad Martinez
 *
 * @version 1.00 2013/4/7
 */

package com.muhammad.navigation;

import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Image;

import javax.sound.sampled.Clip;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.muhammad.utilities.AudioTool;
import com.muhammad.utilities.ResourceLoader;

import java.io.IOException;
import java.io.InputStream;
import java.util.EmptyStackException;
import java.util.Stack;

public class Game extends JFrame implements Runnable 
{
	private static final long serialVersionUID = 1L;
	public static final int WIDTH = 80;
	public static final int HEIGHT = WIDTH*2;
	public static final int SCALE = 3;
	public static final String NAME = "SpaceGame";
	//Background
	public static Image background;
//	public static JLabel bg;
	public static Clip bgMusic;
	//Thread
	public Thread t;
	
	public static Container content;
	public static JPanel cardPanel;
	public static CardLayout cl;
	public static Stack <State> gameStates;
	//Main state
	public static MenuPanel menu;
	//Font
	public static Font gameFont;
	
	
    public Game() throws IOException
    {
    	// Setting up JFrame
    	setMinimumSize(new Dimension(WIDTH*SCALE, HEIGHT*SCALE));
		setMaximumSize(new Dimension(WIDTH*SCALE, HEIGHT*SCALE));
		setPreferredSize(new Dimension(WIDTH*SCALE, HEIGHT*SCALE));
    	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	setResizable(false);
    	setLocationRelativeTo(null);
    	setLayout(new CardLayout());
    	super.setTitle(NAME);
    	//Container & Stack & Main JPanel
    	content = getContentPane();
    	gameStates = new Stack<State>(); //Handles ticks
    	cl = new CardLayout();
    	cardPanel = new JPanel(cl);
    	//Font
		try {
			InputStream is = ResourceLoader.class.getResourceAsStream("/Fonts/PressStart2P.ttf");
			gameFont = Font.createFont(Font.TRUETYPE_FONT, is);
		} catch (FontFormatException e) {
			System.err.println("PngJButton FontFormatException");
		} catch (IOException e) {
			System.err.println("PngJButton could not load/find font");
		}

		//Adding background - menupanel covers this
		background = ResourceLoader.loadVisualGif("/Visual/bg-0.gif");
		// Adding states to stack and container
		menu = new MenuPanel(WIDTH*SCALE, HEIGHT*SCALE);
		Game.addState(menu);
		//Sets frame at or above preferred size
    	pack();
    	setVisible(true);
    	//Starting thread
    	start();   	
    }
    
    public void start()
    {
    	t = new Thread(this);
    	t.start();
    	//Music
    	AudioTool.playSong("Man Overboard - Montrose");
    }
    
    public void run()
    {
    	while(true)
    	{
    		try{
	    		//Updating current state
	        	gameStates.peek().tick();
	        	gameStates.peek().render();
    		}catch(EmptyStackException e)
    		{System.out.println("Bottom of Stack");}
        	//The position here may be responsible for IllegalStateException in GamePanel
        	try{
        		Thread.sleep(100);
        	}catch(InterruptedException e){
        		System.err.println("Main Thread sleep interrupted");
        	}    		
    	}
    }

    public static void addState(State e)
    {
		gameStates.push(e);
		
		cardPanel.add((Component) e, e.toString()); //"Stacking" another JPanel
		content.add(cardPanel, BorderLayout.CENTER); //Adding to container\
    	cl.last(cardPanel);
    }
    
    public static void removeState()
    {
    	try{
    		gameStates.peek().reset();
    		gameStates.remove(gameStates.peek());
        	//Previous state
        	String previous = gameStates.peek().toString();
        	cl.show(cardPanel, previous);
    	}catch(EmptyStackException e) //Returns home
    	{
    		System.err.println("Bottom of stack");
    		Game.addState(menu);
    	}
    }
    
    public static State getTopState()
    {
    	return gameStates.peek();
    }
    
    public static void returnHome()
    {
    	while(gameStates.size()>1)
    		removeState();
    }
  
    public static void main(String[] args) throws IOException
    {
  		new Game();
    }
}