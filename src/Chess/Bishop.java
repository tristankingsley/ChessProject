package Chess;

public class Bishop extends ChessPiece {

	public Bishop(Player player) {
		super(player);
	}

	public String type() {
		return "Bishop";
	}
	
	public boolean isValidMove(Move move, IChessPiece[][] board) {

		if(super.isValidMove(move, board)) {
			if (Math.abs(move.fromRow - move.toRow)
					== Math.abs(move.fromColumn - move.toColumn) &&
					(move.fromColumn != move.toColumn ||
							move.fromRow != move.toRow))
				return true;
		}

		return false;
	}
}
