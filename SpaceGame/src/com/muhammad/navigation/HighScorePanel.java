package com.muhammad.navigation;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import javax.swing.text.PlainDocument;

import com.muhammad.utilities.AudioTool;
import com.muhammad.utilities.PngJButton;
import com.muhammad.utilities.ResourceLoader;

public class HighScorePanel extends InfoPanel implements State
{
	private static final long serialVersionUID = 1L;
	private int userScore;
	private Image scorebox;
	private static ArrayList<String>highscores;
	private PngJButton done;
	private JTextField jtxtfld;
	private static String path;
	
	public HighScorePanel(int userscore) throws IOException
	{
		super("HIGHSCORES");
		this.userScore = userscore;
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		highscores = new ArrayList<String>();
		jtxtfld = initTxtField();
		done = initDoneButt();
		path = getClass().getClassLoader().getResource(".").getPath();
		
		//Reading and Writing
		try
		{
			readScores();
			writeScores(highscores);//Writes with added new score
			highscores = mergeSortScores(highscores);
		}
		catch(Exception e)//Creates new file.
		{
			writeScores(highscores);
			highscores = mergeSortScores(highscores);
		}
		
		//Box
		scorebox=ResourceLoader.loadVisual("Visual/scorebox.png");
		//Components
		if(userscore != 0)
		{
			//Adding score
			fillSpace(4);
			PngJButton score = new PngJButton(Integer.toString(userscore));
			score.setAlignmentX(CENTER_ALIGNMENT);
			add(score);
			//Adding text field
			add(jtxtfld);
			//Adding done button
			add(done);
			//Adding home button	
			fillSpace(14);
			add(super.backbutton);
		}
		else
		{
			//Adding home button	
			fillSpace(22);
			add(super.backbutton);
		}
	}
	
	/* Adds transparent buttons to fill space */
	public void fillSpace(int i)
	{
		for(int k=0; k<i;k++)
			add(new PngJButton("\n"));
	}
	
	/* Creates a transparent text field adhering to the theme. */
	public JTextField initTxtField()
	{
		Dimension d = new Dimension((int)(GamePanel.WIDTH*0.5),(int)(GamePanel.HEIGHT*0.05));
		Color c = new Color(255,140,0); //Game Orange
		Border border = BorderFactory.createLineBorder(c);
		
		JTextField jtf = new JTextField(5);
		
		jtf.setAlignmentX(CENTER_ALIGNMENT);
		//Color & Transparency
		jtf.setBackground(new Color(0,0,0,100));
		jtf.setFont(Game.gameFont.deriveFont(14f));
		jtf.setForeground(c);
		jtf.setBorder(border);
		//Size
		jtf.setMaximumSize(d);
		jtf.setMinimumSize(d);
		jtf.setPreferredSize(d);
		//Text
		jtf.setText(" NAME");
		jtf.setDocument(new JTextFieldLimit(8));
		((AbstractDocument) jtf.getDocument()).setDocumentFilter(new UppercaseDocumentFilter());
		
		return jtf;
	}
	
	/* Initializing Done Button. */
	public PngJButton initDoneButt() throws IOException
	{
		PngJButton temp;

		temp = new PngJButton(ResourceLoader.loadVisual("Visual/Buttons/doneA.png"));
		temp.setPressedIcon(new ImageIcon(ResourceLoader.loadVisual("Visual/Buttons/doneB.png")));
		temp.setAlignmentX(CENTER_ALIGNMENT);
		temp.addActionListener(new ActionListenerHSP(userScore, jtxtfld, highscores));
		
		return temp;
	}
	
	/* A BufferedWriter builds the bridge between you and the file. The Writer 
	 * variable is only used to show where the stream is, in this case it is to
	 * a file. \\ means that you want to include \ and not some command like \n.
	 * The objects are pretty much the same for BufferedReader. It is crucial to
	 * close readers & writers in order to avoid memory leaks and bugs.
	 */

	public static void readScores() throws FileNotFoundException,IOException
	{
		BufferedReader read = new BufferedReader(new FileReader(path+"PVtest.txt"));

		String line;
		while((line = read.readLine()) != null)
			highscores.add(line);
		read.close();
	}
	
	public static void writeScores(ArrayList<String>highscores)
	{			
		BufferedWriter write;
		try {
			write = new BufferedWriter(new FileWriter(path+"PVtest.txt"));
			//Top 5
			if(highscores.size()<5)
				for(String s:highscores)
				{
					write.write(s);
					write.newLine();		
				}
			else
				for(int s=0; s<5; s++)
				{
					write.write(highscores.get(s));
					write.newLine();	
				}
			write.close();
		} 
		catch (IOException e1) 
		{e1.printStackTrace();}
	}
	
	//MergeSort - nlogn
	public static ArrayList<String> mergeSortScores(ArrayList<String> scores)
	{
		//If empty of 1, considered already sorted.
		if(scores.size() <= 1)
			return scores;
		//Initalizing variables
		ArrayList<String>left=new ArrayList<String>();
		ArrayList<String>right=new ArrayList<String>();
		//Splitting scores
		Iterator<String> itr = scores.iterator();
		int mid=scores.size()/2;
		while(mid-- > 0)
			left.add(itr.next());
		while(itr.hasNext())
			right.add(itr.next());
		//Recurses until unable to split in half.
		left = mergeSortScores(left);
		right = mergeSortScores(right);
		//Merge left and right.
		return merge(left,right);
	}
	//Merge
	public static ArrayList<String> merge(ArrayList<String>left, ArrayList<String>right)
	{
		ArrayList<String>result=new ArrayList<String>();
		//Best way to understand is to roll pairs of die and do this yourself.
		while(left.size()>0 || right.size()>0)
			//Able to compare. Doesn't necessarily mean that both are equal.
			if(left.size()>0 && right.size()>0)
				if(getScoreValue(left.get(0)) > getScoreValue(right.get(0)))
					result.add(left.remove(0));
				else
					result.add(right.remove(0));
			else if(left.size()>0) //Else adds what's leftover to end of result
				result.add(left.remove(0));
			else if(right.size()>0)
				result.add(right.remove(0));
				
		return result;
	}
	public static int getScoreValue(String s)
	{
		if(s.contains("%"))
		{
			String[]ss = s.split("%-%");
			return Integer.parseInt(ss[1]);
		}
		return -1;
	}
	
	public void displayScores(Graphics g, ArrayList<String>scores)
	{
		int cnt=0;
		int centerX = (int)((this.getWidth()/2.0)-(scorebox.getWidth(this)/2.0));
		//Y depends if there is a score
		if(userScore != 0)
			g.drawImage(scorebox, centerX, (int)(this.getHeight()*0.4), this);
		else
			g.drawImage(scorebox, centerX, (int)(this.getHeight()*0.2), this);
		//Slight shifted center x.
		int x = (int)(centerX+(scorebox.getWidth(this)/4.0));
		int y;
		if(userScore != 0)
			y = (int)(this.getHeight()*0.6);
		else
			y = (int)(this.getHeight()*0.4);
		//Format
		g.setFont(Game.gameFont.deriveFont(14f));
		g.setColor(new Color(255,140,0));
		for(String s:scores)
			if(cnt++<5)//Top 5 only
			{
				String[]ss = s.split("%-%");
				//Centering based on name length
				int k = x-(7*(ss[0].length()-3));
				
				g.drawString(ss[0]+" "+ss[1],k,y);
				y+=(int)(this.getHeight()*0.06);
			}
	}
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		
		displayScores(g,highscores);
	}	
	
	public void tick() {}
	public void render()
	{this.repaint();}
	public void reset() {}	
	
	/**
	 * Limits characters of JTextField.
	 * 
	 * By npinti:
	 * http://stackoverflow.com/questions/10136794/limiting-the-number-of-characters-in-a-jtextfield
	 */
	class JTextFieldLimit extends PlainDocument {
		private static final long serialVersionUID = 1L;
		private int limit;
		  JTextFieldLimit(int limit) {
		    super();
		    this.limit = limit;
		  }

		  JTextFieldLimit(int limit, boolean upper) {
		    super();
		    this.limit = limit;
		  }

		  public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {
		    if (str == null)
		      return;

		    if ((getLength() + str.length()) <= limit) {
		      super.insertString(offset, str, attr);
		    }
		  }
		}
	
	/**
	 * Makes characters of JTextField uppercase.
	 * 
	 * By Wayan Saryada:
	 * http://www.kodejava.org/how-do-i-format-jtextfields-text-to-uppercase/
	 */
	class UppercaseDocumentFilter extends DocumentFilter {
        //
        // Override insertString method of DocumentFilter to make the text format
        // to uppercase.
        //
        public void insertString(DocumentFilter.FilterBypass fb, int offset,
                                 String text, AttributeSet attr)
                throws BadLocationException {
 
            fb.insertString(offset, text.toUpperCase(), attr);
        }
 
        //
        // Override replace method of DocumentFilter to make the text format
        // to uppercase.
        //
        public void replace(DocumentFilter.FilterBypass fb, int offset, int length,
                            String text, AttributeSet attrs)
                throws BadLocationException {
 
            fb.replace(offset, length, text.toUpperCase(), attrs);
        }
    }

	class ActionListenerHSP implements ActionListener
	{
		ArrayList<String>list;
		private JTextField name;
		private int userscore;
		private boolean clicked;
		
		public ActionListenerHSP(int userscore, JTextField name, ArrayList<String>list) 
		{
			this.name=name;
			this.userscore=userscore;
			this.list=list;
			clicked=false;
		}

		public void actionPerformed(ActionEvent e) 
		{
			//Avoids posting multiple times.
			if(!clicked)
			{
				AudioTool.playSFX("Audio/select.au");
				//Adds new scores to list.
				list.add(name.getText()+"%-%"+userscore);
				//Sorts list.
				list=HighScorePanel.mergeSortScores(list);
				//Updates highscore list and file
				HighScorePanel.writeScores(list);
				HighScorePanel.highscores=list;
				
				clicked=true;			
			}
		}
		
	}
}
