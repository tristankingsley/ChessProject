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
				if ((Math.abs(move.fromRow - move.toRow)
						== Math.abs(move.fromColumn - move.toColumn) &&
						(move.fromColumn != move.toColumn ||
								move.fromRow != move.toRow))
					&& clearPath(move, board))
					return true;
			}


		return false;
	}

	public boolean clearPath(Move move, IChessPiece[][] board){

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
