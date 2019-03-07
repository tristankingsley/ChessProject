package Chess;

public class Rook extends ChessPiece {

	public Rook(Player player) {
		
		super(player);
		
	}

	public String type() {
		
		return "Rook";
		
	}
	
	// determines if the move is valid for a rook piece
	public boolean isValidMove(Move move, IChessPiece[][] board) {
		
		  if(((Math.abs(move.fromColumn - move.toColumn) == 0
				|| Math.abs(move.fromRow - move.toRow) == 0) &&
				( move.fromColumn != move.toColumn ||
								move.fromRow != move.toRow)
				))
        	return true;
        else
        	return false;
		
	}
	
}
