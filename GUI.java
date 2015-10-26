import java.awt.*;
import javax.swing.*;

class GUI extends JFrame{
  
  int width = 498;
  int height = 490;
  static int pieceRadius = 35;
  static int boardWidth = pieceRadius*7*2;
  static int boardHeight = pieceRadius*6*2;
  JLabel infoLabel = new JLabel("PAENUS");
  GameBoard gb;
  
  	//this is a dummy gamestate array for testing the drawing of the board
  int[][] array = new int[][]{
			{0,0,0,1,0,0,0},
			{0,0,0,1,0,0,0},
			{0,0,0,1,0,0,0},
			{0,0,0,1,0,0,0},
			{0,0,0,1,0,0,0},
			{0,0,2,1,2,0,0}
	};
 
  public GUI(){
	  
	setSize(width, height);
	
    gb = new GameBoard(array); //create gameboard and pass it the initial array (the empty game)
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
    
    //layout stuff
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
  }
  
}

/* A subclass of JPanel which displays the gameboard with pieces */
class GameBoard extends JPanel{
	
	int[][] array;
	
	public GameBoard(int[][] array){
		this.array = array;
	}
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		this.setBackground(Color.WHITE);
		
		//draw game board
		g.setColor(Color.BLACK);
		g.drawLine(70, 0, 70, GUI.boardHeight);
		g.drawLine(140, 0, 140, GUI.boardHeight);
		g.drawLine(210, 0, 210, GUI.boardHeight);
		g.drawLine(280, 0, 280, GUI.boardHeight);
		g.drawLine(350, 0, 350, GUI.boardHeight);
		g.drawLine(420, 0, 420, GUI.boardHeight);
		
		paintPieces(g, array);
	}
	
	//paint the pieces placed so far in the game, player1 = green, AI = red
	public void paintPieces(Graphics g, int[][] array){
		super.paintComponents(g);
		
		for (int i= 0; i <= 5; i++){
			for (int j = 0; j <=6; j++){
				
				if (array[i][j] == 1){
					g.setColor(Color.GREEN);
					g.fillOval(GUI.pieceRadius*2*j,
							GUI.pieceRadius*2*i,
							GUI.pieceRadius*2,
							GUI.pieceRadius*2);
					
				} else if (array[i][j] == 2){
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