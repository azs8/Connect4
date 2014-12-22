import java.io.*;
import java.util.*;
import java.lang.Math;

class Tuple {
	public final int first;
	public final int last;

	public Tuple(int a, int b){
		this.first = a;
		this.last = b;
	}

	public boolean isEqual(Tuple b){
		if (this.first == b.first && this.last == b.last) return true;
		return false;
	}

	public void printTuple(){
		System.out.println("(" + this.first + ", " + this.last + ")");
		return;
	}
}

class GameState {
	public ArrayList<ArrayList<Integer>> board;
	public ArrayList<Tuple> player1WinConditions = new ArrayList<Tuple>();
	public ArrayList<Tuple> player2WinConditions = new ArrayList<Tuple>();
	public int p1offense = 1;
	public int p1defense = 1;
	public int p2offense = 1;
	public int p2defense = 1;
	public boolean player1Won = false;
	public boolean player2Won = false;

	//you don't need to update the other things, because the only time this is called is
	//when the game is first initialized in the main method with all 0s
	public GameState (ArrayList<ArrayList<Integer>> game, int p1o, int p1d, int p2o, int p2d) {
		ArrayList<ArrayList<Integer>> newBoard = new ArrayList<ArrayList<Integer>>();
		for (int i = 0; i < 7; i ++) {
			ArrayList<Integer> rowI = new ArrayList<Integer>();
			for (int j = 0; j < 6; j ++) {
				rowI.add(game.get(i).get(j));
			}
			newBoard.add(rowI);
		}
		board = newBoard;
		p1offense = p1o;
		p1defense = p1d;
		p2offense = p2o;
		p2defense = p2d;
	}

	//this is the create method called for all other occurrences
	//you don't need to check or carry over the booleans, because if they were true
	//then this would not be being created
	public GameState (GameState oldGame) {
		this.player1Won = oldGame.player1Won;
		this.player2Won = oldGame.player2Won;
		ArrayList<ArrayList<Integer>> newBoard = new ArrayList<ArrayList<Integer>>();
		for (int i = 0; i < 7; i ++) {
			ArrayList<Integer> rowI = new ArrayList<Integer>();
			for (int j = 0; j < 6; j ++) {
				rowI.add(oldGame.board.get(i).get(j));
			}
			newBoard.add(rowI);
		}
		board = newBoard;
		for (Tuple p1WinCondition : oldGame.player1WinConditions) {
			this.player1WinConditions.add(p1WinCondition);
		}
		for (Tuple p2WinCondition : oldGame.player2WinConditions) {
			this.player2WinConditions.add(p2WinCondition);
		}
		this.p1offense = oldGame.p1offense;
		this.p1defense = oldGame.p1defense;
		this.p2offense = oldGame.p2offense;
		this.p2defense = oldGame.p2defense;
	}

	//returns 1000 if that player won, -1000 if they lost
	//if neither, calculates a "score" for that player in this scenario
	public int value(int player) {
		if (player == 1) {
			if (player1Won) return 1000;
			if (player2Won) return -1000;
			return (p1offense * player1WinConditions.size() - p1defense * player2WinConditions.size());
		}
		//it must be player 2 then
		if (player1Won) return -1000;
		if (player2Won) return 1000;
		return (p2offense * player2WinConditions.size() - p2defense * player1WinConditions.size());
	}

	public void printBoard () {
		System.out.println("THE GAME BOARD:");
		for (int i = 0; i < 6; i ++){
			String layer = "";
			for (int j = 0; j < 7; j ++){
				layer += "| " + board.get(j).get(i) + " ";
			}
			layer += "|";
			System.out.println(layer);
		}
		System.out.println("-----------------------------");
		// System.out.println("Player1 Win Conditions:");
		// for (Tuple p1WinCondition : player1WinConditions) {
		// 	p1WinCondition.printTuple();
		// }
		// System.out.println("Player2 Win Conditions:");
		// for (Tuple p2WinCondition : player2WinConditions) {
		// 	p2WinCondition.printTuple();
		// }
	}

	//returns true if a piece can be inserted into that column
	public boolean validMove (int column) {
		if (column < 0 || column > 6) return false;
		if (board.get(column).get(0) != 0) return false;
		return true;
	}

	//returns the row where the piece will fall if inserted into that column
	public int topOfColumn (int column) {
		if (!validMove(column)) {
			System.out.println("Invalid Move");
			printBoard();
			System.out.println("Attempted to move: " + column);
		}
		int i = 5;
		while (i >= 0){
			if (board.get(column).get(i) == 0) {
				return i;
			}
			i -= 1;
		}
		return -1;
	}

	//this method adds the piece to the board, sets the playerWon variables,
	//adds the new win conditions if necessary, removes obsolete win conditions
	public void addPiece (int player, int column) {
		//actually adds the piece to the game board
		int row = topOfColumn(column);
		board.get(column).set(row, player);
		Tuple move = new Tuple(column, row);

		//checks if either player has won the game
		if (player == 1) {
			for (Tuple p1WinCondition : player1WinConditions) {
				if (move.isEqual(p1WinCondition)) {
					player1Won = true;
				}
			}
		}
		if (player == 2) {
			for (Tuple p2WinCondition : player2WinConditions) {
				if (move.isEqual(p2WinCondition)) {
					player2Won = true;
				}
			}
		}

		//finds any new win conditions, adds them to the storage variable, removes duplicates
		ArrayList<Tuple> newWinSpaces = findNewWinSpaces(move);
		if (player == 1) {
			for (Tuple newWinMove : newWinSpaces) {
				player1WinConditions.add(newWinMove);
			}
			HashSet<Tuple> tempHashSet = new HashSet<Tuple>();
			tempHashSet.addAll(player1WinConditions);
			player1WinConditions.clear();
			player1WinConditions.addAll(tempHashSet);
		}
		if (player == 2) {
			for (Tuple newWinMove : newWinSpaces) {
				player2WinConditions.add(newWinMove);
			}
			HashSet<Tuple> tempHashSet = new HashSet<Tuple>();
			tempHashSet.addAll(player2WinConditions);
			player2WinConditions.clear();
			player2WinConditions.addAll(tempHashSet);
		}

		//checks if any of the opponents win conditions were removed by its move
		if (player == 1) {
			ArrayList<Tuple> toRemove = new ArrayList<Tuple>();
			for (Tuple p2WinCondition : player2WinConditions) {
				if (move.isEqual(p2WinCondition)) {
					toRemove.add(p2WinCondition);
				}
			}
			for (Tuple removeThisWinCond : toRemove) {
				player2WinConditions.remove(removeThisWinCond);
			}
		}
		if (player == 2) {
			ArrayList<Tuple> toRemove = new ArrayList<Tuple>();
			for (Tuple p1WinCondition : player1WinConditions) {
				if (move.isEqual(p1WinCondition)) {
					toRemove.add(p1WinCondition);
				}
			}
			for (Tuple removeThisWinCond : toRemove) {
				player1WinConditions.remove(removeThisWinCond);
			}
		}
	}

	public ArrayList<Tuple> findNewWinSpaces (Tuple move) {
		int player = board.get(move.first).get(move.last);
		int column = move.first;
		int row = move.last;
		ArrayList<Tuple> potentialNewWinConditions = new ArrayList<Tuple>();

		//horizontal
		ArrayList<Integer> sevenRow = new ArrayList<Integer>();
		for (int i = column - 3; i < column + 4; i ++) {
			int a = 0;
			if (i >= 0 && i < 7) {
				if (board.get(i).get(row) == player) a = 1;
			}
			sevenRow.add(a);
		}
		ArrayList<Integer> possibleHorizontal = findNewWinSpacesHelper(sevenRow);
		for (int e : possibleHorizontal) {
			int col = column + (e - 3);
			if (col >= 0 && col < 7) {
				potentialNewWinConditions.add(new Tuple(col, row));
			}
		}

		//vertical
		sevenRow.clear();
		for (int i = row - 3; i < row + 4; i ++) {
			int a = 0;
			if (i >= 0 && i < 6) {
				if (board.get(column).get(i) == player) a = 1;
			}
			sevenRow.add(a);
		}
		ArrayList<Integer> possibleVertical = findNewWinSpacesHelper(sevenRow);
		for (int e : possibleVertical) {
			int elementRow = row + (e - 3);
			if (elementRow >= 0 && elementRow < 6) {
				potentialNewWinConditions.add(new Tuple(column, elementRow));
			}
		}

		//1st diagonal
		sevenRow.clear();
		for (int i = -3; i < 4; i ++) {
			int newCol = column + i;
			int newRow = row + i;
			int a = 0;
			if (0 <= newCol && newCol < 7 && 0 <= newRow && newRow < 6) {
				if (board.get(newCol).get(newRow) == player) a = 1;
			}
			sevenRow.add(a);
		}
		ArrayList<Integer> possibleDiagonal = findNewWinSpacesHelper(sevenRow);
		for (int e : possibleDiagonal) {
			int elementRow = row + (e - 3);
			int elementCol = column + (e - 3);
			if (elementCol >= 0 && elementCol < 7 && elementRow >= 0 && elementRow < 6) {
				potentialNewWinConditions.add(new Tuple(elementCol, elementRow));
			}
		}

		//2nd diagonal
		sevenRow.clear();
		for (int i = -3; i < 4; i ++) {
			int newCol = column + i;
			int newRow = row - i;
			int a = 0;
			if (0 <= newCol && newCol < 7 && 0 <= newRow && newRow < 6) {
				if (board.get(newCol).get(newRow) == player) a = 1;
			}
			sevenRow.add(a);
		}
		ArrayList<Integer> possibleDiagonal2 = findNewWinSpacesHelper(sevenRow);
		for (int e : possibleDiagonal2) {
			int elementRow = row - (e - 3);
			int elementCol = column + (e - 3);
			if (elementCol >= 0 && elementCol < 7 && elementRow >= 0 && elementRow < 6) {
				potentialNewWinConditions.add(new Tuple(elementCol, elementRow));
			}
		}

		ArrayList<Tuple> newWinSpaces = new ArrayList<Tuple>();
		for (Tuple potentialNewWinCond : potentialNewWinConditions) {
			if (board.get(potentialNewWinCond.first).get(potentialNewWinCond.last) == 0) {
				newWinSpaces.add(potentialNewWinCond);
			}
		}
		return newWinSpaces;
	}

	public ArrayList<Integer> findNewWinSpacesHelper(ArrayList<Integer> sevenRow){
		ArrayList<Integer> possibleWinConditions = new ArrayList<Integer>();
		int firstHalf = sevenRow.get(0) + sevenRow.get(1) * 2 + sevenRow.get(2) * 4;
		int secondHalf = sevenRow.get(4) + sevenRow.get(5) * 2 + sevenRow.get(6) * 4;
		switch (firstHalf) {
			case 0: if (secondHalf == 3) {
						possibleWinConditions.add(6);
						possibleWinConditions.add(2);
					}
					if (secondHalf == 5) possibleWinConditions.add(5);
					if (secondHalf == 6) possibleWinConditions.add(4);
					break;
			case 1: if (secondHalf == 3) {
						possibleWinConditions.add(6);
						possibleWinConditions.add(2);
					}
					if (secondHalf == 5) possibleWinConditions.add(5);
					if (secondHalf == 6) possibleWinConditions.add(4);
					break;
			case 2: if (secondHalf % 2 == 1) possibleWinConditions.add(2);
					if (secondHalf == 3) possibleWinConditions.add(6);
					if (secondHalf == 5) possibleWinConditions.add(5);
					if (secondHalf == 6) possibleWinConditions.add(4);
					break;
			case 3: possibleWinConditions.add(2);
					if (secondHalf == 3) possibleWinConditions.add(6);
					if (secondHalf == 5) possibleWinConditions.add(5);
					if (secondHalf == 6) possibleWinConditions.add(4);
					break;
			case 4: if (secondHalf % 2 == 1) possibleWinConditions.add(1);
					if (secondHalf == 2 || secondHalf == 6) possibleWinConditions.add(4);
					if (secondHalf == 1 || secondHalf == 5) possibleWinConditions.add(5);
					break;
			case 5: possibleWinConditions.add(1);
					if (secondHalf == 2 || secondHalf == 6) possibleWinConditions.add(4);
					if (secondHalf == 1 || secondHalf == 5) possibleWinConditions.add(5);
					break;
			case 6: possibleWinConditions.add(0);
					possibleWinConditions.add(4);
					break;
			default: break;
		}
		return possibleWinConditions;
	}
}

class Tree {
	public int playerWhoStartedTree;
	public int branchNumber = -1;
	public ArrayList<Tree> childNodes = new ArrayList<Tree>();
	public GameState thisBoard;

	public Tree (GameState game, int playerStarting) {
		thisBoard = new GameState(game);
		playerWhoStartedTree = playerStarting;
	}

	public void makeChildNodes (int playerPlacingPiece, boolean firstChild) {
		for (int i = 0; i < 7; i ++) {
			if (thisBoard.validMove(i)) {
				GameState gameI = new GameState(thisBoard);
				gameI.addPiece(playerPlacingPiece, i);
				Tree childI = new Tree (gameI, playerWhoStartedTree);
				if (firstChild) {
					childI.branchNumber = i;
				}
				else {
					childI.branchNumber = this.branchNumber;
				}
				childNodes.add(childI);
				//System.out.println("Branch: " + childI.branchNumber + ", Value: " + childI.thisBoard.value(playerWhoStartedTree));
			}
		}
	}

	public int minimaxSearch(Tree root, int depth, Tuple a, int b, int playerToPlayNext, boolean maximizing, boolean origin) {
		if (depth == 0) return root.thisBoard.value(playerWhoStartedTree);
		if (maximizing) {
			root.makeChildNodes (playerToPlayNext, origin);
			for (Tree childTree : root.childNodes) {
				int childTreeResult = 0;
				if (childTree.thisBoard.value(playerWhoStartedTree) == 1000) {
					childTreeResult = 1000;
					//System.out.println("it sees a way it can win");
				}
				else {
					childTreeResult = minimaxSearch(childTree, depth - 1, a, b, playerToPlayNext % 2 + 1, false, false);
				}
				if (childTreeResult > a.first) {
					a = new Tuple(childTreeResult, childTree.branchNumber);
				}
				if (b <= a.first) break; 
			}
			if (origin) {
				//System.out.print("Best move is: ");
				//a.printTuple();
				return a.last;
			}
			return a.first;
		}
		else {
			root.makeChildNodes (playerToPlayNext, origin);
			for (Tree childTree : root.childNodes) {
				int childTreeResult = 0;
				//System.out.println(childTree.thisBoard.value(playerWhoStartedTree));
				if (childTree.thisBoard.value(playerWhoStartedTree) == -1000) {
					childTreeResult = -1000;
				//	System.out.println("it sees a way it can lose");
				}
				else {
					childTreeResult = minimaxSearch(childTree, depth - 1, a, b, playerToPlayNext % 2 + 1, true, false);
				}
				b = Math.min(b, childTreeResult);
				if (b <= a.first) break;
			}
			return b;
		}
	}
}

class MainGame {
	public static GameState board;
	public static int numberOfMoves = 0;
	public static boolean human = false;
	public static int p1offense = 1;
	public static int p1defense = 1;
	public static int p2offense = 1;
	public static int p2defense = 1;
	public static int p1depth = 6;
	public static int p2depth = 6;
	public static boolean useDefault1 = true;
	public static boolean useDefault2 = true;

	public static void main(String args[]){
		//initialize the game with all 0s
		ArrayList<ArrayList<Integer>> initialBoard = new ArrayList<ArrayList<Integer>>();
		for (int i = 0; i < 7; i ++){
			ArrayList<Integer> row = new ArrayList<Integer>();
			for (int j = 0; j < 6; j ++){
				row.add(0);
			}
			initialBoard.add(row);
		}

		//setting up the optional argument variables
		for (String s : args){
			if (s.equals("-h")) human = true;
			if (s.equals("-p1offense")) p1offense = 2;
			if (s.equals("-p1defense")) p1defense = 2;
			if (s.equals("-p2offense")) p2offense = 2;
			if (s.equals("-p2defense")) p2defense = 2;
			if (s.equals("-p1depth2")) p1depth = 2;
			if (s.equals("-p1depth4")) p1depth = 4;
			if (s.equals("-p1depth8")) p1depth = 8;
			if (s.equals("-p2depth2")) p2depth = 2;
			if (s.equals("-p2depth4")) p2depth = 4;
			if (s.equals("-p2depth8")) p2depth = 8;
			if (s.equals("-p1NoDefaultMove")) useDefault1 = false;
			if (s.equals("-p2NoDefaultMove")) useDefault2 = false;
		}
		board = new GameState(initialBoard, p1offense, p1defense, p2offense, p2defense);
		board.printBoard();

		//TESTING THE MINIMAX ALGORITHM
		//-----------------------------
		//-----------------------------
		//-----------------------------
		//-----------------------------
		//-----------------------------
		// GameState testData = new GameState (board);
		// testData.addPiece(2, 0);
		// testData.addPiece(2, 0);
		// testData.addPiece(2, 1);
		// testData.addPiece(2, 2);

		// testData.addPiece(1, 1);
		// testData.addPiece(1, 1);
		// testData.addPiece(1, 2);
		// testData.addPiece(2, 2);

		// testData.addPiece(1, 3);
		// testData.addPiece(1, 3);
		// testData.addPiece(1, 3);
		// testData.addPiece(1, 4);

		// testData.addPiece(2, 3);
		// testData.addPiece(2, 4);
		// testData.addPiece(1, 4);
		// testData.addPiece(1, 4);
		// testData.addPiece(2, 4);

		// Tree testTree = new Tree(testData, 2);
		// testData.printBoard();

		// testData.addPiece(2,0);
		// testData.addPiece(1,0);
		// System.out.println(testData.value(2));

		// for (int i = 0; i < 7; i ++ ){
		// 	ArrayList<Integer> values = new ArrayList<Integer>();
		// 	GameState gameI = new GameState(testData);
		// 	gameI.addPiece(2, i);
		// 	for (int j = 0; j < 7; j ++ ) {
		// 		//System.out.println(gameI.validMove(j) + ", player1 can play: " + j);
		// 		if (gameI.validMove(j)) {
		// 			//System.out.println(gameI.validMove(j));
		// 			//gameI.printBoard();
		// 			//System.out.println("Attempting to play: (" + i + ", " + j + ")");
		// 			GameState gameJ = new GameState(gameI);
		// 			gameJ.addPiece(1, j);
		// 			values.add(gameJ.value(2));
		// 		}
		// 	}
		// 	System.out.println(values);
		// }
		// Tuple defaultAlpha = new Tuple(-10000, -1);
		// int bestMove = testTree.minimaxSearch(testTree, 2, defaultAlpha, 10000, 2, true, true);
		// System.out.println(bestMove);

		//take all of the turns
		while (!board.player1Won && !board.player2Won) {
			if (numberOfMoves > 41) break;
			player1Move();
			numberOfMoves ++;
			if (!board.player1Won) {
				player2Move();
				numberOfMoves ++;
			}
			board.printBoard();
		}

		//display the winner
		if (board.player1Won) {
			System.out.println("Player1 Wins");
			return;
		}
		if (board.player2Won) {
			System.out.println("Player2 Wins");
			return;
		}
		System.out.println("No moves left: Tie Game");
		return;
	}

	public static void player1Move () {
		if (human) {
			System.out.print("User Move: ");
			Scanner sc = new Scanner(System.in);
		    int column = sc.nextInt();
	    	board.addPiece(1, column);
		}
		else {
		    if (numberOfMoves == 0 && useDefault1) board.addPiece(1,3);
		    else {
			    Tree treeOfMoves = new Tree(board, 1);
				Tuple defaultAlpha = new Tuple(-10000, -1);
				int bestMove = treeOfMoves.minimaxSearch(treeOfMoves, p1depth, defaultAlpha, 10000, 1, true, true);
				if (board.validMove(bestMove)) {
					board.addPiece(1, bestMove);
				}
				else {
					for (int i = 0; i < 7; i ++) {
						if (board.validMove(i)) {
							board.addPiece(1, i);
							break;
						}
					}
				}
			}
		}
	}

	public static void player2Move () {
		if (numberOfMoves == 1 && useDefault2) board.addPiece(2,2);
		else {
			Tree treeOfMoves = new Tree(board, 2);
			Tuple defaultAlpha = new Tuple(-10000, -1);
			int bestMove = treeOfMoves.minimaxSearch(treeOfMoves, p2depth, defaultAlpha, 10000, 2, true, true);
			if (board.validMove(bestMove)) {
					board.addPiece(2, bestMove);
			}
			else {
				for (int i = 0; i < 7; i ++) {
					if (board.validMove(i)) {
						board.addPiece(2, i);
						break;
					}
				}
			}
		}
	}
}




