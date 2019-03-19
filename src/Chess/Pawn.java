package Chess;

public class Pawn extends ChessPiece {

	public Pawn(Player player) {
		super(player);
	}

	public String type() {
		return "Pawn";
	}

	// determines if the move is valid for a pawn piece
	public boolean isValidMove(Move move, IChessPiece[][] board) {

		if(super.isValidMove(move, board)) {
			int direction = 1; //direction for white

			if (board[move.fromRow][move.fromColumn].player() == Player.BLACK) //direction for black
				direction = -1;

			//Checks for empty spots since pawns can't capture by moving forward
			if (move.toRow != 0 && move.toRow != 7 && move.fromColumn == move.toColumn
					&& board[move.toRow - direction][move.toColumn] == null) {
				//checks to see if it's the pawns first move
				if (move.fromRow == 1 || move.fromRow == 6) {
					//can move one or 2 spaces in the right direction
					if (((move.fromRow - move.toRow) == 2 * direction ||
							((move.fromRow - move.toRow)) == direction) && board[move.toRow][move.toColumn] == null)
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
        // More code is needed
		return false;
	}

}
