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
			int direction = 1;

			if (board[move.fromRow][move.fromColumn].player() == Player.BLACK)
				direction = -1;

			if (move.fromColumn == move.toColumn && board[move.toRow][move.toColumn] == null) {
				if (move.fromRow == 1 || move.fromRow == 6) {
					if ((move.fromRow - move.toRow) == 2 * direction ||
							((move.fromRow - move.toRow)) == direction)
						return true;
				} else if ((move.fromRow - move.toRow) == direction)
					return true;
			}
//			else
//				if(board[move.fromRow - direction][move.fromColumn + 1] != null && move.toRow == move.fromRow -1 &&
//						move
//							&& move.fromColumn != move.toColumn)
//					return true;
//				else if(board[move.fromRow - direction][move.fromColumn - 1] != null
//						&& move.fromColumn != move.toColumn)
//							return true;


		}
        // More code is needed
		return false;
	}
}
