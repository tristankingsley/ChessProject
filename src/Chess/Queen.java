package Chess;

public class Queen extends ChessPiece {

	public Queen(Player player) {
		super(player);

	}

	public String type() {
		return "Queen";
		
	}

	/*******************************************************************************************************************
	 * Returns whether the piece at location {@code [move.fromRow, move.fromColumn]} is allowed to move to location
	 * {@code [move.fromRow, move.fromColumn]}.
	 *
	 * @param move  a {@link Chess.Move} object describing the move to be made.
	 * @param board the {@link Chess.IChessModel} in which this piece resides.
	 * @return {@code true} if the proposed move is valid, {@code false} otherwise.
	 * @throws IndexOutOfBoundsException if either {@code [move.fromRow, move.fromColumn]} or {@code [move.toRow,
	 *                                   move.toColumn]} don't represent valid locations on the board.
	 * @throws IllegalArgumentException if {@code this} object isn't the piece at location
	 * 									{@code [move.fromRow, move.fromColumn]}.
	 ******************************************************************************************************************/
	public boolean isValidMove(Move move, IChessPiece[][] board) {
		if(super.isValidMove(move, board)) {
			Bishop move1 = new Bishop(board[move.fromRow][move.fromColumn].player());
			Rook move2 = new Rook(board[move.fromRow][move.fromColumn].player());
			return (move2.isValidMove(move, board) || move1.isValidMove(move, board));
		}
		else
			return false;
		}
}
