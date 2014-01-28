package com.muhammad.utilities;

import java.awt.Color;
import java.awt.Image;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;

import com.muhammad.navigation.Game;

/** 
 * A JButton that supports transparency. 
 * 
 * @constructor Default - A blank tranparent png image of equal in 
 * 					dimension to the standard button: 95x40.
 * @constructor Overloaded(String) - A transparent box that displays
 * 					text in the font theme and color. "\n" creates an
 * 					empty box that is smaller than the default.
 * @constructor Overloaded(ImageIcon) - A button that uses the Image 
 * 					given. What is not covered by the image is 
 * 					transparent, making it more aesthetically pleasing.  
 * 
 * The first two constructors are used to fill space; they vary in 
 * size.
 */ 
public class PngJButton extends JButton
{
	private static final long serialVersionUID = 1L;
	
	public PngJButton() throws IOException
	{
		//An empty image with dimensions of normal buttons, 95x40
		super(new ImageIcon(ResourceLoader.loadVisual("Visual/Buttons/blank.png")));
		
		setBorder(null);
        setBorderPainted(false);
        setContentAreaFilled(false);
        setOpaque(false);
	}

	public PngJButton(String text)
	{
		super(text);
		//Font
		setFont(Game.gameFont.deriveFont(18f));
		
		setForeground(new Color(255,140,0));
		setBorder(null);
        setBorderPainted(false);
        setContentAreaFilled(false);
        setOpaque(false);
	}
	
	public PngJButton(Image image)
	{
		super(new ImageIcon(image));
		
		setBorder(null);
        setBorderPainted(false);
        setContentAreaFilled(false);
        setOpaque(false);		
	}
}
