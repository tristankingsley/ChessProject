/*******************************************************************************************************************
 * This class creates a pawn as a chess piece for our chess program
 *
 * @author Tristan Kingsley, Trevor Spitzley, Kyle Kukla
 * @version Spring 2019
 ******************************************************************************************************************/
package Chess;

public class Pawn extends ChessPiece {

	/*******************************************************************************************************************
	 * Return the player that owns this piece.
	 *
	 * @return the player that owns this piece.
	 ******************************************************************************************************************/
	public Pawn(Player player) {
		super(player);
	}

	/*******************************************************************************************************************
	 * Return the type of this piece ("King", "Queen", "Rook", etc.).  Note:  In this case "type" refers to the game
	 * of chess, not the type of the Java class.
	 *
	 * @return the type of this piece
	 ******************************************************************************************************************/
	public String type() {
		return "Pawn";
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

		if(super.isValidMove(move, board)) {
			int direction = 1; //direction for white

			if (board[move.fromRow][move.fromColumn].player() == Player.BLACK) //direction for black
				direction = -1;

			//Checks for empty spots since pawns can't capture by moving forward
			if (move.fromRow != 0 && move.fromRow != 7 && move.fromColumn == move.toColumn
					&& board[move.fromRow - direction][move.fromColumn] == null) {

				//checks to see if it's the pawns first move
				if (move.fromRow == 1 || move.fromRow == 6) {

					//can move one or 2 spaces in the right direction
					if ((move.fromRow - move.toRow) == 2 * direction && board[move.toRow][move.toColumn] == null)
						return true;
					if((move.fromRow - move.toRow) == direction && board[move.toRow][move.toColumn] == null)
						return true;

				}

				//otherwise pawn must only move forward by one in the right direction
				else if ((move.fromRow - move.toRow) == direction)
					return true;
			}
			else //for diagonal capturing, can't capture at board ends
				if(move.fromRow != 0 && move.fromRow != 7) {

					//towards the left
					if (move.fromColumn != 7) {
						if (board[move.fromRow - direction][move.fromColumn + 1] != null)
							if (move.toRow == move.fromRow - direction && move.fromColumn == move.toColumn - 1)
								return true;}

					//towards the right
					if (move.fromColumn != 0)
						if (board[move.fromRow - direction][move.fromColumn - 1] != null)
							if (move.toRow == move.fromRow - direction && move.fromColumn == move.toColumn + 1)
								return true;
				}


		}
		return false;
	}

}
