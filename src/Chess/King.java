package Chess;

public class King extends ChessPiece {

	public King(Player player) {
		super(player);
	}

	public String type() {
		return "King";
	}
	
	public boolean isValidMove(Move move, IChessPiece[][] board) {
        // More code is needed
		if(super.isValidMove(move, board)) {
			if ((Math.abs(move.toColumn - move.fromColumn)
					+ Math.abs(move.toRow - move.fromRow) == 1) ||
					(Math.abs(move.toColumn - move.fromColumn) == 1 &&
							Math.abs(move.toRow - move.fromRow) == 1))
				return true;
		}

		return false;
	}
}
