import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import javax.swing.*;

class GUI extends JFrame implements ActionListener{
  
  static int width = 498;
  static int height = 490;
  static int pieceRadius = 35;
  static int boardWidth = pieceRadius*7*2;
  static int boardHeight = pieceRadius*6*2;
  JLabel infoLabel = new JLabel("PAENUS");
  
  //instance of GameBoard which extends JPanel
  GameBoard gb;
  
  //button declarations so that actionPerformed can access them
  JButton button0;
  JButton button1;
  JButton button2;
  JButton button3;
  JButton button4;
  JButton button5;
  JButton button6;
  
 
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
    button0 = new JButton(" ");
    button1 = new JButton(" ");
    button2 = new JButton(" ");
    button3 = new JButton(" ");
    button4 = new JButton(" ");
    button5 = new JButton(" ");
    button6 = new JButton(" ");
    buttonsPanel.add(button0);
    buttonsPanel.add(button1);
    buttonsPanel.add(button2);
    buttonsPanel.add(button3);
    buttonsPanel.add(button4);
    buttonsPanel.add(button5);
    buttonsPanel.add(button6);
    button0.addActionListener(this);
    button1.addActionListener(this);
    button2.addActionListener(this);
    button3.addActionListener(this);
    button4.addActionListener(this);
    button5.addActionListener(this);
    button6.addActionListener(this);
    
    //layout stuff, add components
    setLayout(new BorderLayout());
    add(infoLabel, BorderLayout.NORTH);
    add(gb, BorderLayout.CENTER);
    add(buttonsPanel, BorderLayout.SOUTH);
    
    //misc JFrame stuff
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
  
  public void actionPerformed(ActionEvent e){
	  Object src = e.getSource();
	  if (src == button0){
		  //make human move in column 0
	  } else if (src == button1){
		  //make human move in column 1
	  } else if (src == button2){
		  //make human move in column 2
	  } else if (src == button3){
		  //make human move in column 3
	  } else if (src == button4){
		  //make human move in column 4
	  } else if (src == button5){
		  //make human move in column 5
	  } else if (src == button6){
		  //make human move in column 6
	  }
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