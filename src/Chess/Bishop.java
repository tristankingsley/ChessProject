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
			if (clearPath(move, board)) {
				if (Math.abs(move.fromRow - move.toRow)
						== Math.abs(move.fromColumn - move.toColumn) &&
						(move.fromColumn != move.toColumn ||
								move.fromRow != move.toRow))
					return true;
			}
		}

		return false;
	}

	public boolean clearPath(Move move, IChessPiece[][] board){
		//int to represent spots in between piece before the selected spot
		int numHops = ( (Math.abs(move.toColumn - move.fromColumn) ) - 1);

		//If statement for moving upward/leftward
		if (move.toColumn < move.fromColumn &&
				move.toRow < move.fromRow){
			//If statement using numHops to look at spots
			if (board[move.toRow + numHops][move.toColumn + numHops] == null){
				return true;
			}
		}

		return false;
	}
}
