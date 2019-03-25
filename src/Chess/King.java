package Chess;

public class King extends ChessPiece {

	public King(Player player) {
		super(player);
	}

	public String type() {
		return "King";
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
        // More code is needed
		if(super.isValidMove(move, board)) {
			if ((Math.abs(move.toColumn - move.fromColumn)
					+ Math.abs(move.toRow - move.fromRow) == 1) ||
					(Math.abs(move.toColumn - move.fromColumn) == 1 &&
							Math.abs(move.toRow - move.fromRow) == 1)) {
				return true;
			}
		}

		return false;
	}

	public boolean inCheck() {

		return true;
	}
}
