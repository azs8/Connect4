import java.awt.*;
import java.util.ArrayList;

import javax.swing.*;

class GUI extends JFrame{
  
  static int width = 498;
  static int height = 490;
  static int pieceRadius = 35;
  static int boardWidth = pieceRadius*7*2;
  static int boardHeight = pieceRadius*6*2;
  JLabel infoLabel = new JLabel("PAENUS");
  GameBoard gb;
  
 
  public GUI(){
	  
	//make dummy gamestate
	  ArrayList<ArrayList<Integer>> boardTestGUI = new ArrayList<ArrayList<Integer>>();
	  
	  for (int i = 0; i < 7; i ++) {
			ArrayList<Integer> rowI = new ArrayList<Integer>();
			for (int j = 0; j < 6; j ++) {
				rowI.add(0);
			}
			boardTestGUI.add(rowI);
		}
	  //for dummy gamestate, add these pieces -> get(column).set(row, player)
	  boardTestGUI.get(0).set(0,1);
	  
	setSize(width, height);
	
    gb = new GameBoard(boardTestGUI); //create gameboard and pass it the initial array (the empty game)
    gb.setSize(boardWidth, boardHeight);
    
    //button stuff
    JPanel buttonsPanel = new JPanel(new GridLayout(1,6,0,0));
    JButton button0 = new JButton(" ");
    JButton button1 = new JButton(" ");
    JButton button2 = new JButton(" ");
    JButton button3 = new JButton(" ");
    JButton button4 = new JButton(" ");
    JButton button5 = new JButton(" ");
    JButton button6 = new JButton(" ");
    buttonsPanel.add(button0);
    buttonsPanel.add(button1);
    buttonsPanel.add(button2);
    buttonsPanel.add(button3);
    buttonsPanel.add(button4);
    buttonsPanel.add(button5);
    buttonsPanel.add(button6);
    
    //layout stuff, add components
    setLayout(new BorderLayout());
    add(infoLabel, BorderLayout.NORTH);
    add(gb, BorderLayout.CENTER);
    add(buttonsPanel, BorderLayout.SOUTH);
    
    //misc
    setTitle("Connect Four");
    setSize(width, height);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setResizable(false);
    setVisible(true);
  }
  
  public static void main(String[] args){
    GUI gui = new GUI();
    
    String[] argsMainGame = {"-h"};
    MainGame.main(argsMainGame);
  }
  
}

/* A subclass of JPanel which displays the gameboard with pieces */
class GameBoard extends JPanel{
	
	ArrayList<ArrayList<Integer>> arrayList;
	
	public GameBoard(ArrayList<ArrayList<Integer>> arrayList){
		this.arrayList = arrayList;
	}
	
	//paint the gameboard
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		this.setBackground(Color.YELLOW);
		
		paintPieces(g, arrayList);
	}
	
	//paint the pieces placed so far in the game, player1 = green, AI = red
	public void paintPieces(Graphics g, ArrayList<ArrayList<Integer>> arrayList){
		super.paintComponents(g);
		
		for (int i= 0; i <= 5; i++){
			for (int j = 0; j <= 6; j++){
				
				if(arrayList.get(j).get(i) == 0){
					g.setColor(Color.WHITE);
					g.fillOval(GUI.pieceRadius*2*j,
							GUI.pieceRadius*2*i,
							GUI.pieceRadius*2,
							GUI.pieceRadius*2);
					
				} else if (arrayList.get(j).get(i) == 1){
					g.setColor(Color.GREEN);
					g.fillOval(GUI.pieceRadius*2*j,
							GUI.pieceRadius*2*i,
							GUI.pieceRadius*2,
							GUI.pieceRadius*2);
					
				} else if (arrayList.get(j).get(i) == 2){
					g.setColor(Color.RED);
					g.fillOval(GUI.pieceRadius*2*j,
							GUI.pieceRadius*2*i,
							GUI.pieceRadius*2,
							GUI.pieceRadius*2);
				}
			}
		}
	}
}