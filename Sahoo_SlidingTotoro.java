/*Ankur Sahoo
 * Sliding Totoro: This program is a sliding puzzle that lets the user
 * move around "totoro" to have the matrix in numerical order*/

import java.awt.*;

import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;


public class Sahoo_SlidingTotoro extends JFrame implements KeyListener{

	private BufferedImage image;
	private PicPanel[][] allPanels;
	private int totRow;		// location of Totoro
	private int totCol;
	private int moves;

	public Sahoo_SlidingTotoro(){

		setSize(375,375);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(null);
		setTitle("Sliding Tortoro");
		getContentPane().setBackground(Color.black);

		//enables the program to detect key presses
		this.addKeyListener(this);

		allPanels = new PicPanel[4][4];
		setLayout(new GridLayout(4,4,2,2));
		setBackground(Color.black);
		moves=0;

		try {
			image = ImageIO.read(new File("totoro.jpg"));


		} catch (IOException ioe) {
			JOptionPane.showMessageDialog(null, "Could not read in the pic");
			System.exit(0);
		}	

		//numbers to add to the matrix
		ArrayList<Integer>nums=new ArrayList<Integer>();

		for(int i = 1; i<=16; i++)
		{
			nums.add(i);
		}

		int i=0;

		//puts numbers in the array in a random order
		//until the number 16 is found, which is
		//reserved for the totoro
		for(int row = 0; row<allPanels.length; row++)
		{
			for(int col = 0; col<allPanels[0].length; col++)
			{
				i=(int)(Math.random()*nums.size());
				allPanels[row][col]=new PicPanel(row,col);

				//if the chosen number is not 16, add the number to the matrix
				if(nums.get(i)!=16)
				{
					allPanels[row][col].setNumber(nums.remove(i));
					add(allPanels[row][col]);
				}
				//if the number is 16, don't add it to the matrix
				//exits the loop
				else
				{
					totRow=row;
					totCol=col;
					nums.remove(i);
					add(allPanels[row][col]);
					col=allPanels[0].length;
					row=allPanels.length;
				}
			}
		}

		int colStart= totCol+1;

		//puts numbers into the matrix randomly after the totoro spot
		for(int row = totRow; row<allPanels.length; row++)
		{
			for(int col = colStart; col<allPanels[0].length; col++)
			{
				i=(int)(Math.random()*nums.size());
				allPanels[row][col]=new PicPanel(row,col);
				allPanels[row][col].setNumber(nums.remove(i));
				add(allPanels[row][col]);
			}
			colStart=0;
		}

		setVisible(true);
	}

	//if a key is pressed, this method is called, and this methods
	//performs some actions that will correspond to the key pressed
	public void keyPressed(KeyEvent arg0) {

		int keyVal = arg0.getKeyCode();	

		//if the user wants to go right
		if(keyVal == KeyEvent.VK_RIGHT){

			int nextCol = totCol+1;
			if(nextCol<allPanels[0].length)
			{
				moveTo(totRow,nextCol);
			}	
		}

		//if the user wants to go left
		if(keyVal == KeyEvent.VK_LEFT){

			int nextCol = totCol-1;
			if(nextCol>=0)
			{
				moveTo(totRow,nextCol);
			}	

		}

		//if the user wants to go up
		if(keyVal == KeyEvent.VK_UP){

			int nextRow = totRow-1;

			if(nextRow>=0)
			{
				moveTo(nextRow,totCol);
			}	
		}

		//if the user wants to go down
		if(keyVal == KeyEvent.VK_DOWN){

			int nextRow = totRow+1;

			if(nextRow<allPanels.length)
			{
				moveTo(nextRow,totCol);
			}	

		}
	}

	//helper method that swaps totoro with the number next to it
	//that is determined by the direction
	private void moveTo(int nextRow, int nextCol)
	{
		int temp = allPanels[nextRow][nextCol].number;
		allPanels[nextRow][nextCol].removeNumber();
		allPanels[totRow][totCol].setNumber(temp);
		moves+=1;
		totRow=nextRow;
		totCol=nextCol;
		
		if(checkWin()==true)
		{
			JOptionPane.showMessageDialog (null, "You Win!\nYou made " + moves + " moves!", "Game Over", JOptionPane.INFORMATION_MESSAGE);
			this.removeKeyListener(this);
		}
	}

	//checks if the numbers are in ascending order
	private boolean checkWin()
	{
		if(totRow!=allPanels.length-1)
		{
			return false;
		}

		if(totCol!=allPanels[0].length-1)
		{
			return false;
		}

		int num = 1;
		//checks if the numbers are in ascending order for every row except the last
		for(int row = 0; row<allPanels.length-1; row++)
		{
			for(int col = 0; col<allPanels[row].length; col++)
			{
				if(allPanels[row][col].number!=num)
					return false;

				num++;
			}
		}
		
		//checks the last row 
		for(int row = allPanels.length-1; row<allPanels.length; row++)
		{
			for(int col = 0; col<allPanels[row].length-1; col++)
			{
				if(allPanels[row][col].number!=num)
					return false;

				num++;
			}
		}

		return true;
	}

	public void keyReleased(KeyEvent arg0) {


	}
	
	public void keyTyped(KeyEvent arg0) {

	}

	class PicPanel extends JPanel{


		private int width = 76;
		private int height = 80;	//dimensions of the Panel 

		private int number=-1;		// -1 when Totoro is at that position.
		private JLabel text;

		public PicPanel(int r, int c){

			setBackground(Color.white);
			setLayout(null);

		}		

		//changes the panel to have the given number
		public void setNumber(int num){	

			number = num;
			text = new JLabel(""+number,SwingConstants.CENTER);
			text.setFont(new Font("Calibri",Font.PLAIN,55));
			text.setBounds(0,35,70,50);
			this.add(text);

			repaint();
		}

		//replaces the number with Totoro
		public void removeNumber(){
			this.remove(text);
			number = -1;
			repaint();
		}

		public Dimension getPreferredSize() {
			return new Dimension(width,height);
		}

		//this will draw the image or the number
		// called by repaint and when the panel is initially drawn
		public void paintComponent(Graphics g){
			super.paintComponent(g);

			if(number == -1)
				g.drawImage(image,8,0,this);
		}

	}



	public static void main(String[] args){
		new Sahoo_SlidingTotoro();
	}



}
