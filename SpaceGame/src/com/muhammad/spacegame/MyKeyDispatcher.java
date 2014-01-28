package com.muhammad.spacegame;

import java.awt.KeyEventDispatcher;
import java.awt.event.KeyEvent;
import javax.swing.BoxLayout;
import com.muhammad.navigation.Game;
import com.muhammad.navigation.GamePanel;
import com.muhammad.navigation.PausePanel;

public class MyKeyDispatcher implements KeyEventDispatcher
{
	PausePanel pauseP = new PausePanel(Game.WIDTH*Game.SCALE, Game.HEIGHT*Game.SCALE);
	boolean paused = false;
	
	public boolean dispatchKeyEvent(KeyEvent e)
	{
		if(e.getID() == KeyEvent.KEY_PRESSED)
			switch(e.getKeyCode())
			{
			case KeyEvent.VK_UP:
				GamePanel.up = true;
				break;
			case KeyEvent.VK_DOWN:
				GamePanel.down = true;
				break;
			case KeyEvent.VK_LEFT:
				GamePanel.left = true;
				break;
			case KeyEvent.VK_RIGHT:
				GamePanel.right = true;
				break;	
			case KeyEvent.VK_SPACE:
				GamePanel.shooting = true;
				break;
			//Pause
			case KeyEvent.VK_P:
				if(!paused)
				{
					pauseP.setLayout(new BoxLayout(pauseP, BoxLayout.Y_AXIS));
					Game.addState(pauseP);
				}
				else
					Game.removeState();
				paused = !paused;
				break;
			}
		else if(e.getID() == KeyEvent.KEY_RELEASED)
			switch(e.getKeyCode())
			{
				case KeyEvent.VK_UP:
					GamePanel.up = false;
					break;
				case KeyEvent.VK_DOWN:
					GamePanel.down = false;
					break;
				case KeyEvent.VK_LEFT:
					GamePanel.left = false;
					break;
				case KeyEvent.VK_RIGHT:
					GamePanel.right = false;
					break;	
				case KeyEvent.VK_SPACE:
					GamePanel.shooting = false;
					break;
			}
		return false;
	}
}
