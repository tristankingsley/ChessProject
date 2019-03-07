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

		if(move.fromRow == 1 || move.fromRow == 6){
			if(Math.abs(move.fromRow - move.toRow) == 2 ||
					Math.abs(move.fromRow - move.toRow) == 1)
				return true;
		}
		else
			if(Math.abs(move.fromRow - move.toRow) == 1)
				return true;
        // More code is needed
		return false;
	}
}
