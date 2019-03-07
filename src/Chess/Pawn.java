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

		if(super.isValidMove(move, board) && board[move.toRow][move.toColumn] == null) {
			if (move.fromRow == 1 || move.fromRow == 6) {
				if ((move.fromRow - move.toRow) == 2 ||
						((move.fromRow - move.toRow)) == 1)
					return true;
			} else if((move.fromRow - move.toRow) == 1)
				return true;
		}
        // More code is needed
		return false;
	}
}
