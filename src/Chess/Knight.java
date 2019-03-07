package Chess;

public class Knight extends ChessPiece {

	public Knight(Player player) {
		super(player);
	}

	public String type() {
		return "Knight";
	}

	public boolean isValidMove(Move move, IChessPiece[][] board){

        // More code is needed
		if((Math.abs(move.fromColumn - move.toColumn) == 2
				&& Math.abs(move.fromRow - move.toRow) == 1)||
				(Math.abs(move.fromColumn - move.toColumn) == 1
						&& Math.abs(move.fromRow - move.toRow) == 2))
			return true;

		else
			return false;
		
	}

}
