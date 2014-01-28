package com.muhammad.navigation;
/**Muhammad Martinez */

//Now can move diaganol
//import java.awt.*;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.KeyboardFocusManager;
import java.io.IOException;
import javax.sound.sampled.Clip;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

import com.muhammad.spacegame.*;
import com.muhammad.utilities.AudioTool;
import com.muhammad.utilities.ResourceLoader;
import com.muhammad.utilities.Timer;

public class GamePanel extends JPanel implements State
{
	private static final long serialVersionUID = 1L;
	//Images
	Image background;
	Image heartFULL, heartEMPTY, heartHALF;
	//Audio
	Clip bossMusic, shopMusic;
	//Statics
	public static Spaceship spaceship; //Static so it can be accessed by KeyListener
	public static boolean up, down, left, right, shooting;
	public static int WIDTH, HEIGHT, stageCounter;
	public static Stage stage;
	//Input
	MyKeyDispatcher myKD;
	KeyboardFocusManager manager;
	//Misc.
	public String spaceshipHit;
	private Timer deadTimer;
	public static Timer flyInTimer;

	//Overloaded
	public GamePanel(int width, int height) throws IOException
	{init(width,height);}
	
	public void init(int width, int height) throws IOException
	{
		WIDTH = width;
		HEIGHT = height;
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		//Used for spaceship movement
		up = false;
		down = false;
		left= false;
		right = false;
		shooting = false;
		//Ship
		spaceship = new Spaceship();
		//Background
		background = Game.background;
		//Heart Images
		heartFULL = ResourceLoader.loadVisual("Visual/Gameplay/heartFull.png");
		heartEMPTY = ResourceLoader.loadVisual("Visual/Gameplay/heartEmpty.png");
		heartHALF = ResourceLoader.loadVisual("Visual/Gameplay/heartHalf.png");
		manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
		myKD = new MyKeyDispatcher();
		manager.addKeyEventDispatcher(myKD);
		//Level
		stageCounter = 4;
		setUpLevel();
		//Timer
		deadTimer=null;
		flyInTimer=null;
	}
	
	public void tick() 
	{
		//Updating user input
		if(up){spaceship.moveUP();}
		else if(down){spaceship.moveDOWN();}
		if(left){spaceship.moveLEFT();}
		else if(right){spaceship.moveRIGHT();}	
		else {spaceship.resetImage();}
		if(shooting || spaceship.shootConstant)
		{try {spaceship.shoot();} catch (IOException e) {e.printStackTrace();}}
		//Updates current level
		if(!stage.isClear())
			stage.update();
		else//Next Level
			try 
			{nextLevel();}
			catch (IOException e)
			{System.err.println("Could not generate next stage, IOException");}
		
		//Dead
		if(spaceship.isDead())
			try{
				if(deadTimer.done())
					Game.addState(new HighScorePanel(spaceship.getScore()));
			}catch(NullPointerException e)
			{deadTimer = new Timer(2000);} 
			catch (IOException e) 
			{e.printStackTrace();}
	}
	
	public static void setUpLevel() throws IOException
	{
		//Switching to game music
		if(stage instanceof BossStage)
			AudioTool.playSong("Man Overboard - Montrose");
		//Next Level
		stageCounter++;
		stage = new LevelStage(spaceship, stageCounter);
	}
	
	public static void setUpBoss() throws IOException
	{
		stage = new BossStage(spaceship, stageCounter);
		AudioTool.playSong("Tides of Man - Chemical Fires");
	}
	
	public static void setUpShop() throws IOException
	{
		Game.addState(new ShopPanel(0));//Only 1 shop for now
		AudioTool.playSong("The Few That Remain");
	}
	
	public static void nextLevel() throws IOException
	{
		if(stage instanceof LevelStage)
			if(stageCounter%5 != 0)
				setUpLevel();
			else
				setUpBoss();
		else if(stage instanceof BossStage)
				setUpShop();				
	}
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		//Background
		g.drawImage(background, 0, 0, this.getSize().width, this.getSize().height, this);
		//Modifying font
		g.setFont(Game.gameFont.deriveFont(10f));
		g.setColor(Color.yellow);
		//Painting stage elements
		if(!stage.isClear())
			paintStage(g);
		//Spaceship
		if(!spaceship.isDead() /*&& (flyInTimer == null || flyInTimer.done())*/)
			g.drawImage(spaceship.getImage(), spaceship.getX(), spaceship.getY(), spaceship.getImage().getWidth(this), spaceship.getImage().getHeight(this), this);
		paintStatus(g);
		paintScores(g);
		g.setColor(Color.yellow);
		//Game over
		if(spaceship.isDead())
		{
			g.drawString("GAME OVER",WIDTH/4,HEIGHT/2);
			manager.removeKeyEventDispatcher(myKD);
			//Game.addState(new HighScorePanel(spaceship.getScore()));
		}
		//Stage cleared, all enemies dead
		if(stage.isClear())
			g.drawString("STAGE CLEAR",(int)(WIDTH*0.25),(int)(HEIGHT*0.5));
	}
	//Paints objects in the stage, enemies & lasers
	public void paintStage(Graphics g)
	{
		if(stage instanceof BossStage)
		{
			//Planet
			Laser p = stage.getPlanet();
			g.drawImage(p.getImage(), p.getX(), p.getY(), p.getImage().getWidth(this), p.getImage().getHeight(this), this);
			//Boss
			Boss b = stage.getBoss();
			g.drawImage(b.getImage(), b.getX(), b.getY(), b.getImage().getWidth(this), b.getImage().getHeight(this), this);
		}
		//Enemies
		for(Enemy e : stage.getEnemies())
			if(!e.isDead())
				g.drawImage(e.getImage(), e.getX(), e.getY(), this);
		//Power ups
		for(PowerUp p : stage.getPowerUps())
			g.drawImage(p.getImage(), p.getX(), p.getY(), this);
		//Spaceship lasers
		for(Laser k : stage.getShipLasers())
			if(!k.isHiding())
				g.drawImage(spaceship.getLaserImage(), k.getX(), k.getY(), spaceship.getLaserImage().getWidth(this), spaceship.getLaserImage().getHeight(this), this);
		//Enemy lasers
		for(Laser k : stage.getEnemyLasers())
			if(!k.isHiding())
				g.drawImage(k.getImage(), k.getX(), k.getY(), k.getImage().getWidth(this), k.getImage().getHeight(this), this);
	}
	
	/* Paints health, energy, score, stage #, and boss health (if applicable) */
	public void paintStatus(Graphics g)
	{
		int k = 0; //Heart counter for positioning purposes
		int x = 0;
		int y = (int)(GamePanel.HEIGHT*0.03);
		//Stage & Score
		g.setColor(Color.yellow);
		g.setFont(Game.gameFont.deriveFont(10f));
		g.drawString("STAGE: "+stageCounter,x+3,y);
		g.drawString("SCORE: "+spaceship.getScore(),(int)WIDTH/2,y);
		//Hearts/health: integers represent halves
		for(double heart : spaceship.heartArray)
		{
			x = k*heartFULL.getWidth(this);
			Image img=heartEMPTY;//Default
			if(heart == 2)
				img = heartFULL;				
			if(heart == 1)
				img = heartHALF;
			if(heart == 0)
				img = heartEMPTY;
			g.drawImage(img, x, y+2,heartFULL.getWidth(this), heartFULL.getHeight(this), this);
			k++;		
		}
		//Energy Bar
		x = 0;
		y = y+heartFULL.getHeight(this)+3;
		k = 0;
		for(int j = 0; j < (int)spaceship.getEnergy(); j++)
		{
			k = x+((j)*spaceship.getLaserImage().getWidth(this));
			g.drawImage(spaceship.getLaserImage(),k,y, this);
		}
		g.drawImage(spaceship.getEBar(), x, y, this);
		//Boss Health
		if(stage instanceof BossStage)
			paintBossHealth(g);
	}	
	
	/* When enemies are destroyed via laser, a string representing their value is revealed. */
	public void paintScores(Graphics g)
	{
		g.setFont(Game.gameFont.deriveFont(14f));
		if(stage.getEnemyValues() != null)
			for(ScoreString ss : stage.getEnemyValues())
				if(ss.getY()>0)
				{	
					g.setColor(ss.getColor());
					g.drawString(""+ss.getValue(), ss.getX(), ss.getY());
				}
		g.setFont(Game.gameFont.deriveFont(10f));
	}	
	
	//Boss health bar
	public void paintBossHealth(Graphics g)
	{
		Boss b = stage.getBoss();
		int x = (int)(WIDTH*0.05);
		int y = (int)(HEIGHT*0.85);
		for(int k = 0; k < (int)b.getHealth(); k++)
		{
			int kx = x+((k)*b.getHealthSegment().getWidth(this));
			g.drawImage(b.getHealthSegment(),kx,y, b.getHealthSegment().getWidth(this), b.getHealthSegment().getHeight(this),this);
		}
		g.drawImage(b.getHealthBar(), x, y, b.getHealthBar().getWidth(this), b.getHealthBar().getHeight(this), this);
	}
	
	public void render()
	{this.repaint();}
	
	public void reset()
	{}
	
	public String toString()
	{return "StagePanel";}
}

//Credits:
// Muhammad Martinez
// Kevin Chorath
// http://www.zekkocho.com/java-games/82-java-move-an-object-using-the-arrow-keys-keylistener
// FamilyGuy applet
// Convertfiles.com
// sfxr
