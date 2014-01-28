package com.muhammad.navigation;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

import com.muhammad.spacegame.Spaceship;
import com.muhammad.utilities.AudioTool;
import com.muhammad.utilities.PngJButton;
import com.muhammad.utilities.ResourceLoader;
import com.muhammad.utilities.Timer;

public class ShopPanel extends JPanel implements State 
{
	/**
	 * @param prices - The cost for each item. Determined by individual 
	 * 			equations in which X is an reverse equation of the upgrade. 
	 * 			For example: Power inceases by 1/5 and the price for power 
	 * 			is 400+200X where X equals ss.getPow()*5. This enables us
	 * 			to see how many upgrades have been used already.
	 * @param timers - Determine how long a animation or text appears on 
	 * 			screen before reverted back to the standby image or 
	 * 			removing the text box, respectively.
	 * @param status,text - Frequently changed by the action listener 
	 * 			through merchantStatus() and updateText() respectively. 
	 * @param merchant - An image determined by status.      
	 */
	
	private static final long serialVersionUID = 1L;
	private final int WIDTH, HEIGHT;
	public static int powPrice, defPrice, ammoPrice, laserPrice, specPrice;;
	private static String status, text;
	public static Timer animTimer, textTimer;
	private static Image merchant;
	public static char selectedButt;
	private Image[]buttpics;
	private Image iiDeselect, iiSelect;
	private Image bg0, bg1, textBox, scoreBox;
	private PngJButton cont,pow,def,ammo,laz,spec;
	private String parent;
	
	public ShopPanel(int type) throws IOException
	{
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		WIDTH = Game.WIDTH*Game.SCALE;
		HEIGHT = Game.HEIGHT*Game.SCALE;
		//Images
		parent = "Visual/Shop/World-"+type+"/";
		bg0 =  ResourceLoader.loadVisualGif("/"+parent+"bg.gif");
		bg1 = ResourceLoader.loadVisual(parent+"overlay.png");
		textBox = ResourceLoader.loadVisual(parent+"textbox-0.png");
		scoreBox = ResourceLoader.loadVisual(parent+"textbox-1.png");
		iiSelect = ResourceLoader.loadVisual("Visual/Buttons/Shop/game.png");
		iiDeselect = ResourceLoader.loadVisual("Visual/Buttons/Shop/game-2.png");
		merchant = ResourceLoader.loadVisualGif("/"+parent+"Merchant/welcome.gif");
		//An array of images for each button when selected and deselected
		buttpics = new Image[10];
		for(int k=0; k<buttpics.length; k++)
			buttpics[k] = ResourceLoader.loadVisual("Visual/Buttons/Shop/"+k+".png");
		//Prices - vary based on ship stats
		updatePrices();
		//Initializing buttons
		initButts();
		//Adding components
		add(buttonPanel());
		add(cont);
		//Timer
		animTimer = new Timer(5000);
		textTimer = new Timer(5000);
		status = "welcome";
		//Misc.
		text = "Welcome\ncarbon-based\ncustomer!";
		selectedButt = 'M';		
	}
	
	/**
	 * Initializes JButton with appropriate image, then adds customized 
	 * Action Listener. Hard-coded counter, adds after used.
	 */
	public void initButts() 
	{
		int count = 0;
		pow = new PngJButton(buttpics[count++]);
		pow.setPressedIcon(new ImageIcon(buttpics[count++]));
		pow.addActionListener(new shopActionListener(
				"POWER:\n\nDeal more damage"));
		def = new PngJButton(buttpics[count++]);
		def.setPressedIcon(new ImageIcon(buttpics[count++]));
		def.addActionListener(new shopActionListener(
				"DEFENSE:\n\nLess boo-boo's"));
		ammo = new PngJButton(buttpics[count++]);
		ammo.setPressedIcon(new ImageIcon(buttpics[count++]));
		ammo.addActionListener(new shopActionListener(
				"AMMO:\nMore lasers\nper charge."));
		laz = new PngJButton(buttpics[count++]);
		laz.setPressedIcon(new ImageIcon(buttpics[count++]));
		laz.addActionListener(new shopActionListener(
				"LASER:\nAdd a new gun!\nWOOT WOOT!"));
		spec = new PngJButton(buttpics[count++]);
		spec.setPressedIcon(new ImageIcon(buttpics[count++]));
		spec.addActionListener(new shopActionListener(
				"SPECIAL:\nNothing here for\nnow. Sorry dude."));
		cont = new PngJButton(iiSelect);
		cont.setPressedIcon(new ImageIcon(iiDeselect));
		cont.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				AudioTool.playSFX("Audio/select.au");
				try 
				{GamePanel.setUpLevel();} 
				catch (IOException e1) 
				{e1.printStackTrace();}
				Game.removeState();
			}
		});
	}
	
	/* Contains the shop button on the upper right hand corner. */
	public JPanel buttonPanel() throws IOException 
	{
		JPanel buttonPanel = new JPanel();
		//PERFECT HEIGHT
		buttonPanel.setPreferredSize(new Dimension((int)(WIDTH*0.7), (int)(HEIGHT*0.7)));
		buttonPanel.setMinimumSize(new Dimension((int)(WIDTH*0.7), (int)(HEIGHT*0.7)));
		buttonPanel.setMaximumSize(new Dimension((int)(WIDTH*0.7), (int)(HEIGHT*0.7)));
		//Transparency
		buttonPanel.setBackground(new Color(0,0,0,0));
		//Layout
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
		buttonPanel.setAlignmentX(RIGHT_ALIGNMENT);
		//Filling space then adding buttons
		for(int k=0; k<2; k++)
			buttonPanel.add(new PngJButton());
		buttonPanel.add(pow);
		buttonPanel.add(def);
		buttonPanel.add(ammo);
		buttonPanel.add(laz);
		buttonPanel.add(spec);
		
		return buttonPanel;
	}
	
	public static void updatePrices()
	{
		Spaceship ss = GamePanel.spaceship;
		powPrice = (int) (400+(200*(ss.getPow()*5)));
		defPrice = (int) (400+(200*(ss.getDef())));
		ammoPrice = ((5-((int)((5*ss.getEPS())/2.0)))+1)*300;
		laserPrice = 0;//(int) (600+(500*ss.getTotalLasers()));
		specPrice = 0;//1000;
	}
	
	/* Changes merchant's animation. */
	public static void updateMerchant()
	{
		String parent = "Visual/Shop/World-0/Merchant/";
		merchant = ResourceLoader.loadVisualGif("/"+parent+status+".gif");;
	}
	
	/* The following two are accessed through action listeners */
	public static void merchantStatus(String s)
	{status=s;}
	public static void updateText(String txt)
	{text = txt;}

	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		//Backgrounds
		g.drawImage(bg0, 0, 0, WIDTH, HEIGHT, this);
		g.drawImage(bg1, 0, 0, WIDTH, HEIGHT, this);
		//Merchant
		g.drawImage(merchant, (int)(WIDTH*0.45), (int)(HEIGHT*0.33)-2, this);
		//Textbox
		if(!textTimer.done())
			write(g);
		//Score
		g.drawImage(scoreBox, (int)(WIDTH*0.55), (int)(HEIGHT*0.02), this);
		g.setFont(Game.gameFont.deriveFont(8f));
		g.setColor(new Color(220,143,53));
		g.drawString("SCORE: "+GamePanel.spaceship.getScore(),(int)(WIDTH*0.57), (int)(HEIGHT*0.05));
	}
	
	/* Splits the text string into substrings based on "\n". Then displays
	 * each substring in descending order. If a button is selected, adds 
	 * the price and another text to the text box.
	 */
	public void write(Graphics g)
	{
		g.drawImage(textBox, (int)(WIDTH*0.03), (int)(HEIGHT*0.73), this);
		//Text box is 171x98
		int fontSize = 10;
		int x = (int)(WIDTH*0.05);
		int y = (int)(HEIGHT*0.73)+(fontSize*2);
		int k = 0;
		g.setFont(Game.gameFont.deriveFont(10f));
		g.setColor(new Color(220,143,53));
		//Writing text
		String[]textAL=text.split("\n");
		for(String s : textAL)
			g.drawString(s,x,(int)(y+(k++*fontSize*1.3)));
		//Additional info
		if(status.equalsIgnoreCase("select"))
		{
			g.setFont(Game.gameFont.deriveFont(8f));
			String[]textAL2= new String("\nPress again to \npurchase.").split("\n");
			for(String s : textAL2)
				g.drawString(s,x,(int)(y+(k++*fontSize*1.3)));
			int price = -1;
			switch(selectedButt)
			{
			case 'P':
				price = powPrice;
				break;
			case 'D':
				price = defPrice;
				break;		
			case 'A':
				price = ammoPrice;
				break;
			case 'L':
				price = laserPrice;
				break;
			case 'S':
				price = specPrice;
				break;
			}
			g.drawString("Cost: "+price,x*8,y-2);
		}

	}
	
	public void tick() 
	{
		if(animTimer.done() && !status.equalsIgnoreCase("standby"))
			ShopPanel.merchantStatus("standby");
		updateMerchant();
		updatePrices();
	}

	public void render() 
	{this.repaint();}

	public void reset() {}
}

/*****************************************************************************/

//Action Listener
class shopActionListener implements ActionListener
{
	/**
	 * @param selected - Whether a button has already been pressed once.
	 * @param text - What will be displayed on the text box in the JPanel.
	 * @selectedTimer - Buttons must be tapped twice to purchase. The time
	 * 			between these taps is determined through the selected timer. 
	 */
	private boolean selected;
	private char type;
	private int price, lvl;
	private String text;
	private Timer selectedTimer;
	
	public shopActionListener(String s)
	{
		text = s;
		type = s.charAt(0);
		lvl = 0;
		selected=false;
		updatePrice();
	}
	
	/* if(not selected)
	 * 		if(not already maxed out)
	 * 			now selected
	 * 		else
	 * 			maxed out
	 * else
	 * 		if(enough money)
	 * 			buy
	 * 		else
	 * 			deny
	 */
	public void actionPerformed(ActionEvent e) 
	{
		updatePrice();
		if(!selected || selectedTimer.done()) 
		{
			if(lvl<4)//Not maxed out
			{
				//Now selected
				AudioTool.playSFX("Audio/select-shop.au");
				ShopPanel.merchantStatus("select");
				ShopPanel.updateText(text);
				ShopPanel.animTimer = new Timer(7000);
				ShopPanel.textTimer = new Timer(7000);
				selectedTimer = new Timer(2000);
				ShopPanel.selectedButt = type;
			}
			else
			{
				//Maxed out
				AudioTool.playSFX("Audio/deny.au");
				ShopPanel.merchantStatus("deny");
				ShopPanel.updateText("\n\nMaxed Out");
				ShopPanel.animTimer = new Timer(5000);
				ShopPanel.textTimer = new Timer(5000);
			}
		}
		else
		{
			if(GamePanel.spaceship.getScore()>=price)
			{
				//TEMPORARY
				if(price==0)
				{
					AudioTool.playSFX("Audio/deny.au");
					ShopPanel.merchantStatus("deny");
					ShopPanel.updateText("\nUnavailable...");					
				}
				else
				{
					//Purchase
					AudioTool.playSFX("Audio/upgrade.au");
					ShopPanel.merchantStatus("purchase");
					ShopPanel.updateText("Thanks!");
					GamePanel.spaceship.setScore(GamePanel.spaceship.getScore()-price);
					GamePanel.spaceship.upgrade(type);
					lvl++;
				}
			}
			else
			{
				//Deny
				AudioTool.playSFX("Audio/deny.au");
				ShopPanel.merchantStatus("deny");
				ShopPanel.updateText("\nInsufficient\nfunds...");
			}
			ShopPanel.animTimer = new Timer(2000);
			ShopPanel.textTimer = new Timer(2000);
		}
		selected=!selected;
	}
	
	public void updatePrice()
	{
		switch(type)
		{
		case 'P':
			price = ShopPanel.powPrice;
			break;
		case 'D':
			price = ShopPanel.defPrice;
			break;		
		case 'A':
			price = ShopPanel.ammoPrice;
			break;
		case 'L':
			price = ShopPanel.laserPrice;
			break;
		case 'S':
			price = 0;//ShopPanel.specPrice;
			break;
		}
	}

}
