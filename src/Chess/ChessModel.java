package Chess;


import javax.swing.*;
import java.util.*;

public class ChessModel implements IChessModel {	 
    private IChessPiece[][] board;
    //Temp IChessPiece to copy piece taken
    private ArrayList<IChessPiece> takenPieces;
	private Player player;
	//Do we need this here for the Player.BLACK?
	private Player player2;
	private ArrayList<String> moveList;
	private int numMoves = 0;
	private int piecesTaken = 0;


	// declare other instance variables as needed

	public ChessModel() {
	    //Creates ArrayList to store move coordinates
        moveList = new ArrayList<>();

        //Creates List for taken pieces
		takenPieces = new ArrayList<>();

		//Fills first element of both ArrayLists with blank
		// string/object so that the very FIRST move/take piece
		// will line up with index ONE.
		takenPieces.add(piecesTaken, null);
		moveList.add(numMoves, "");

		board = new IChessPiece[8][8];
		player = Player.WHITE;


		for(int i = 0; i < 8; i++)
			board[6][i] = new Pawn(Player.WHITE);


        board[7][0] = new Rook(Player.WHITE);
        board[7][1] = new Knight(Player.WHITE);
        board[7][2] = new Bishop(Player.WHITE);
        board[7][3] = new Queen(Player.WHITE);
        board[7][4] = new King(Player.WHITE);
        board[7][5] = new Bishop(Player.WHITE);
        board[7][6] = new Knight (Player.WHITE);
        board[7][7] = new Rook(Player.WHITE);

		//Do we need this here I'm guessing?
		player2 = Player.BLACK;

		for(int i = 0; i < 8; i++) {
			board[1][i] = new Pawn(Player.BLACK);
		}

		board[0][0] = new Rook(Player.BLACK);
		board[0][1] = new Knight(Player.BLACK);
		board[0][2] = new Bishop(Player.BLACK);
		board[0][3] = new Queen(Player.BLACK);
		board[0][4] = new King(Player.BLACK);
		board[0][5] = new Bishop(Player.BLACK);
		board[0][6] = new Knight (Player.BLACK);
		board[0][7] = new Rook(Player.BLACK);

	}

	public boolean isComplete() {
		boolean valid = false;

		return valid;
	}

	public boolean isValidMove(Move move) {
		boolean valid = false;

		if (board[move.fromRow][move.fromColumn].player() == currentPlayer()) {
			if (board[move.fromRow][move.fromColumn] != null)
				if (board[move.fromRow][move.fromColumn].isValidMove(move, board))
					return true;
		} else {
			JOptionPane.showMessageDialog(null, currentPlayer().toString()+ "'S TURN");
		}

		return valid;
	}

	public void move(Move move) {
		board[move.toRow][move.toColumn] =  board[move.fromRow][move.fromColumn];
		board[move.fromRow][move.fromColumn] = null;
	}

	public boolean inCheck(Player p) {
		boolean valid = false;
		return valid;
	}

	public void saveMove(int fromRow, int fromCol, int toRow, int toCol){
		//Increment counter for locating moves in ArrayList.
		//Incremented BEFORE saving to ensure FIRST moves saves in element
		//ONE for simplicity.
		numMoves++;

		//Creates blank string to hold integers turned into strings
		String saveSpot = "";

		//Adding each number to the blank string in specific order
		saveSpot += Integer.toString(fromRow);
		saveSpot += Integer.toString(fromCol);
		saveSpot += Integer.toString(toRow);
		saveSpot += Integer.toString(toCol);

		//If statement for adding piece type to string if taking a piece
		if (board[toRow][toCol] != null) {
			//Increment pieces taken for accurate sorting into list
			piecesTaken++;
			//Saves piece to string
			saveSpot += board[toRow][toCol].type();
			//Saves piece to IChessPiece ArrayList
			takenPieces.add(piecesTaken, board[toRow][toCol]);
		}

		//Add string to ArrayList of strings
		moveList.add(numMoves, saveSpot);
	}

	public void undoMove(){
		if (numMoves > 0) {
			//Getting string representation of previous move
			//Using num moves to ensure accurate index
			String savedSpot = moveList.get(numMoves);

			//Takes the char from 0-3, turns it into a string, parses it into an int
			int toRow = Integer.parseInt(Character.toString(savedSpot.charAt(0)));
			int toCol = Integer.parseInt(Character.toString(savedSpot.charAt(1)));
			int fromRow = Integer.parseInt(Character.toString(savedSpot.charAt(2)));
			int fromCol = Integer.parseInt(Character.toString(savedSpot.charAt(3)));

			//Creates move object
			Move m = new Move(fromRow, fromCol, toRow, toCol);

			//Makes move
			move(m);

			//If statement for setPiece
			if (savedSpot.length() > 4) {
                setPiece(fromRow, fromCol, takenPieces.get(piecesTaken));
				piecesTaken--;
			}

			//Removes element at desired index
			moveList.remove(numMoves);

			//Decrements numMoves to reflect the removal 0f the element at that index
			numMoves--;
		}
    }


	public Player currentPlayer() {
		return player;
	}

	public int numRows() {
		return 8;
	}

	public int numColumns() {
		return 8;
	}

	public IChessPiece pieceAt(int row, int column) {		
		return board[row][column];
	}

	public void setNextPlayer() {
		player = player.next();
	}

	public void setPiece(int row, int column, IChessPiece piece) {
		board[row][column] = piece;
	}

	public void AI() {
		/*
		 * Write a simple AI set of rules in the following order. 
		 * a. Check to see if you are in check.
		 * 		i. If so, get out of check by moving the king or placing a piece to block the check 
		 * 
		 * b. Attempt to put opponent into check (or checkmate). 
		 * 		i. Attempt to put opponent into check without losing your piece
		 *		ii. Perhaps you have won the game. 
		 *
		 *c. Determine if any of your pieces are in danger, 
		 *		i. Move them if you can. 
		 *		ii. Attempt to protect that piece. 
		 *
		 *d. Move a piece (pawns first) forward toward opponent king 
		 *		i. check to see if that piece is in danger of being removed, if so, move a different piece.
		 */

		}
}
