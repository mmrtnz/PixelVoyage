package com.muhammad.navigation;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.ImageIcon;
import com.muhammad.utilities.PngJButton;
import com.muhammad.utilities.ResourceLoader;

public class HelpPanel extends InfoPanel implements State 
{
	private static final long serialVersionUID = 1L;
	public static int cnt;
	private PngJButton left, right;
	private String parent, path;

	public HelpPanel() throws IOException 
	{
		super("HELP");
		super.remove(super.backbutton);//Will be repositioned
		setLayout(new GridLayout(0,3));
		cnt=0;
		initButts();
		parent = "Visual/Help/";
		path = parent+cnt+".png";
		super.overlay = ResourceLoader.loadVisual(path);
		//Filling space
		for(int k=0; k<24;k++)
			add(new PngJButton());
		//Adding buttons
		add(left);
		add(super.backbutton);
		add(right);
	}
	
	public void initButts() throws IOException
	{
		left = new PngJButton(ResourceLoader.loadVisual("Visual/Help/L0.png"));
		left.setPressedIcon(new ImageIcon(ResourceLoader.loadVisual("Visual/Help/L1.png")));
		left.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
					HelpPanel.cnt--;
			}
		});
		right = new PngJButton(ResourceLoader.loadVisual("Visual/Help/R0.png"));
		right.setPressedIcon(new ImageIcon(ResourceLoader.loadVisual("Visual/Help/R1.png")));
		right.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
					HelpPanel.cnt++;
			}
		});
	}	
	
	public void tick(){}
}