/*******************************************************************************************************************
 * This class creates a bishop as a chess piece for our chess program
 *
 * @author Tristan Kingsley, Trevor Spitzley, Kyle Kukla
 * @version Spring 2019
 ******************************************************************************************************************/
package Chess;

public class Bishop extends ChessPiece {

	/*******************************************************************************************************************
	 * Return the player that owns this piece.
	 *
	 * @return the player that owns this piece.
	 ******************************************************************************************************************/
	public Bishop(Player player) {
		super(player);
	}

	/*******************************************************************************************************************
	 * Return the type of this piece ("King", "Queen", "Rook", etc.).  Note:  In this case "type" refers to the game
	 * of chess, not the type of the Java class.
	 *
	 * @return the type of this piece
	 ******************************************************************************************************************/
	public String type() {
		return "Bishop";
	}

	/*******************************************************************************************************************
	 * Returns whether the piece at location {@code [move.fromRow, move.fromColumn]} is allowed to move to location
	 * {@code [move.fromRow, move.fromColumn]}.
	 *
	 * @param move  a {@link Chess.Move} object describing the move to be made.
	 * @param board the {@link Chess.IChessModel} in which this piece resides.
	 * @return {@code true} if the proposed move is valid, {@code false} otherwise.
	 * @throws IndexOutOfBoundsException if either {@code [move.fromRow, move.fromColumn]} or {@code [move.toRow,
	 *                                   move.toColumn]} don't represent valid locations on the board.
	 * @throws IllegalArgumentException if {@code this} object isn't the piece at location
	 * 									{@code [move.fromRow, move.fromColumn]}.
	 ******************************************************************************************************************/
	public boolean isValidMove(Move move, IChessPiece[][] board) {

		//only can move diagonally
		if(super.isValidMove(move, board)) {
				if ((Math.abs(move.fromRow - move.toRow) == Math.abs(move.fromColumn - move.toColumn) &&
						(move.fromColumn != move.toColumn || move.fromRow != move.toRow))
					&& clearPath(move, board))
					return true;
			}


		return false;
	}

	/*******************************************************************************************************************
	 * Checks to see if there are pieces in the way of the piece's attempted move
	 *
	 * @return {code.true} if there aren't pieces in the way, else {code.false}
	 ******************************************************************************************************************/
	private boolean clearPath(Move move, IChessPiece[][] board){

		//initialized to 1 if the move happens to be up and right
		int addX = 1;
		int addY = 1;


		//changes if the move isn't up and right
		if(move.fromColumn > move.toColumn)
			addX = -1;

		if(move.fromRow > move.toRow)
			addY = -1;

		//sets the coordinates of the first cell to be tested
		int initRow = move.fromRow + addY;
		int initCol = move.fromColumn + addX;

		//runs until the number of places moved is reached
		for(int i = 0; i < Math.abs(move.toRow - move.fromRow) - 1; i++) {
            if (board[initRow][initCol] != null)
                return false;
            else {

            	//incremented values based on above
                initRow += addY;
                initCol += addX;
            }
        }

		return true;

	}
}
