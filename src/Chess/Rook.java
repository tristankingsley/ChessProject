package Chess;

public class Rook extends ChessPiece {

	public Rook(Player player) { super(player); }

	public String type() { return "Rook"; }
	
	// determines if the move is valid for a rook piece
	public boolean isValidMove(Move move, IChessPiece[][] board) {

		if(super.isValidMove(move, board)) {
			if (((Math.abs(move.fromColumn - move.toColumn) == 0
					|| Math.abs(move.fromRow - move.toRow) == 0) &&
					(move.fromColumn != move.toColumn ||
							move.fromRow != move.toRow)
			) && clearPath(move, board))
				return true;
		}

		return false;
		
	}

	public boolean clearPath(Move move, IChessPiece[][] board){
		//numHops calculates # of hops required. Regardless of direction.
		int numHops =  Math.abs(move.fromRow - move.toRow) +
				Math.abs(move.fromColumn - move.toColumn) - 1;

		//Set to 1, 0 for default upward movement.
		int addX = 0;
		int addY = -1;


		//changes if the move is downward
		if(move.fromRow < move.toRow) {
			addY = 1;
		}

		//changes if movement is rightward
		if(move.fromColumn < move.toColumn){
			addX = 1;
			addY = 0;
		}
		//covers leftward movement
		if (move.fromColumn > move.toColumn) {
			addX = -1;
			addY = 0;
		}

		//sets the coordinates of the first cell to be tested
		int initRow = move.fromRow + addY;
		int initCol = move.fromColumn + addX;

		//runs until the number of places moved is reached
		for(int i = 0; i < numHops; i++) {
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
