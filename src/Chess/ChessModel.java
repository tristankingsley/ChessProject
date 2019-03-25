package Chess;


import javax.swing.*;
import java.lang.reflect.Array;
import java.util.*;

public class ChessModel implements IChessModel {

    //a 2-D Array representing the board
    private IChessPiece[][] board;

    //Temp IChessPiece to copy piece taken
    private ArrayList<IChessPiece> takenPieces;

    //player objects representing both players
    private Player player1, player2;

    //an ArrayList that keeps track of every move made
    private ArrayList<String> moveList;

    //integers the saveMove method uses
    private int numMoves = 0;
    private int piecesTaken = 0;

    //booleans for castling
    private boolean whiteRightRook = true;
    private boolean whiteKing = true;
    private boolean whiteLeftRook = true;
    private boolean blackRightRook = true;
    private boolean blackKing = true;
    private boolean blackLeftRook = true;

    public ChessModel() {
        //instantiates ArrayList to store move coordinates
        moveList = new ArrayList<>();

        //instantiates ArrayList for taken pieces
        takenPieces = new ArrayList<>();

        //Fills first element of both ArrayLists with blank
        // string/object so that the very FIRST move/take piece
        // will line up with index ONE.
        takenPieces.add(piecesTaken, null);
        moveList.add(numMoves, "");

        //sets up standard chess board
        board = new IChessPiece[8][8];
        player1 = Player.WHITE;
        player2 = Player.BLACK;


        for (int i = 0; i < 8; i++)
            board[6][i] = new Pawn(Player.WHITE);


        board[7][0] = new Rook(Player.WHITE);
        board[7][1] = new Knight(Player.WHITE);
        board[7][2] = new Bishop(Player.WHITE);
        board[7][3] = new Queen(Player.WHITE);
        board[7][4] = new King(Player.WHITE);
        board[7][5] = new Bishop(Player.WHITE);
        board[7][6] = new Knight(Player.WHITE);
        board[7][7] = new Rook(Player.WHITE);

        for (int i = 0; i < 8; i++) {
            board[1][i] = new Pawn(Player.BLACK);
        }

        board[0][0] = new Rook(Player.BLACK);
        board[0][1] = new Knight(Player.BLACK);
        board[0][2] = new Bishop(Player.BLACK);
        board[0][3] = new Queen(Player.BLACK);
        board[0][4] = new King(Player.BLACK);
        board[0][5] = new Bishop(Player.BLACK);
        board[0][6] = new Knight(Player.BLACK);
        board[0][7] = new Rook(Player.BLACK);

    }


    /*******************************************************************************************************************
     * Returns whether the game is complete.
     *
     * @return {@code true} if complete, {@code false} otherwise.
     ******************************************************************************************************************/
    public boolean isComplete() {
        boolean valid = false;

        if (inCheck(currentPlayer()) && checkmate(currentPlayer())) {
            valid = true;
        }

        return valid;
    }

    /*******************************************************************************************************************
     * Returns whether the piece at location {@code [move.fromRow, move.fromColumn]} is allowed to move to location
     * {@code [move.fromRow, move.fromColumn]}.
     *
     * @param move a {@link Chess.Move} object describing the move to be made.
     * @return {@code true} if the proposed move is valid, {@code false} otherwise.
     * @throws IndexOutOfBoundsException if either {@code [move.fromRow, move.fromColumn]} or {@code [move.toRow,
                                        move.toColumn]} don't represent valid locations on the board.
     ******************************************************************************************************************/
    public boolean isValidMove(Move move) {
        if(move.fromRow < 0 || move.fromRow > 7 || move.toRow < 0 || move.toRow > 7 || move.fromColumn < 0
                || move.fromColumn > 7 || move.toColumn < 0 || move.toColumn > 7)
            throw new IndexOutOfBoundsException();
        else {


            boolean valid = false;

            if (board[move.fromRow][move.fromColumn] != null) {
                if (board[move.fromRow][move.fromColumn].player() == currentPlayer()) {
                    if (board[move.fromRow][move.fromColumn] != null)
                        if ((board[move.fromRow][move.fromColumn].isValidMove(move, board))
                                || canEnPassant(currentPlayer(), move))
                            if (putsPlayerInCheck(move))


                                valid = true;
                } else {
                    JOptionPane.showMessageDialog(null, currentPlayer().toString() + "'S TURN");
                }
            }
            return valid;
        }
    }


    /*******************************************************************************************************************
     * Returns the validity of a move if the move puts a player in check
     *
     * @param move a Move object that represents the attempted move
     * @return {@code false} if player end up in check, {@code true} otherwise.
     ******************************************************************************************************************/
    private boolean putsPlayerInCheck(Move move){
        boolean valid = true;
        saveMove(move.fromRow, move.fromColumn, move.toRow, move.toColumn);
        move(move);

        if(inCheck(currentPlayer()))
            valid = false;

        undoMove();

        return valid;
    }


    /*******************************************************************************************************************
     * Moves the piece from location {@code [move.fromRow, move.fromColumn]} to location {@code [move.fromRow,
     * move.fromColumn]}.
     *
     * @param move a {@link Chess.Move} object describing the move to be made.
     * @throws IndexOutOfBoundsException if either {@code [move.fromRow, move.fromColumn]} or {@code [move.toRow,
     *                                   move.toColumn]} don't represent valid locations on the board.
     ******************************************************************************************************************/
    public void move(Move move) {

        //throws index out of bounds if any coordinates aren't on the board
        if(move.fromRow < 0 || move.fromRow > 7 || move.toRow < 0 || move.toRow > 7 || move.fromColumn < 0
                || move.fromColumn > 7 || move.toColumn < 0 || move.toColumn > 7)
            throw new IndexOutOfBoundsException();
        else{
            setFirstMoveBoolean(move);
            board[move.toRow][move.toColumn] = board[move.fromRow][move.fromColumn];
            board[move.fromRow][move.fromColumn] = null;
        }
    }

    /*******************************************************************************************************************
     * Returns whether a pawn has made it to row 0 or row 7
     * Utilized by pawnTransformation
     *
     * @return {@code true} if pawn is in row 0 or 7, {@code false} otherwise.
     ******************************************************************************************************************/
    public boolean pawnInEndzone(){

        //checks the last move in our ArrayList of moves
        String check = moveList.get(numMoves);
        int pieceRow = Integer.parseInt(Character.toString(check.charAt(2)));
        int pieceCol = Integer.parseInt(Character.toString(check.charAt(3)));


        //checks piece's type
        if((pieceRow == 7 || pieceRow == 0) && board[pieceRow][pieceCol]!= null
                && board[pieceRow][pieceCol].type().equals("Pawn")) {
            return true;
        }

        return false;
    }

    /*******************************************************************************************************************
     * Changes the piece to whatever piece the user specifies
     *
     * @param type a String object representing type of piece to be made
     ******************************************************************************************************************/
    public void pawnTransform(String type){

        //takes the coordinates of the last moved piece from our ArrayList of moves
        String check = moveList.get(numMoves);
        int pieceRow = Integer.parseInt(Character.toString(check.charAt(2)));
        int pieceCol = Integer.parseInt(Character.toString(check.charAt(3)));

        //checks the user's response to which kind of piece they want
        if(type.equals("Queen"))
            setPiece(pieceRow,pieceCol, new Queen(currentPlayer()));
        else if(type.equals("Rook"))
            setPiece(pieceRow,pieceCol, new Rook(currentPlayer()));
        else if(type.equals("Bishop"))
            setPiece(pieceRow,pieceCol, new Bishop(currentPlayer()));
        else if(type.equals("Knight"))
            setPiece(pieceRow,pieceCol, new Knight(currentPlayer()));
    }

    /*******************************************************************************************************************
     * Checks to see if opponent's pieces can target the param player's king
     *
     * @param player a player object to see if their king is in check
     * @return {@code true} if player is in check, else {@code false}
     ******************************************************************************************************************/
    public boolean inCheck(Player player) {
        boolean valid = false;

        int kingRow = 0;
        int kingCol = 0;

        // find the king
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if (board[row][col] != null &&
                        board[row][col].type().equals("King")
                        && board[row][col].player() == currentPlayer()) {
                    kingRow = row;
                    kingCol = col;

                }
            }
        }

        // traverse board for opposing pieces.
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {

                //if the opposite player has a piece that can take their opponent's king
                if (board[r][c] != null && board[r][c].player() != currentPlayer() &&
                        board[r][c].isValidMove(new Move(r, c, kingRow, kingCol), board)) {
                    valid = true;
                }
            }
        }

        return valid;
    }

    /*******************************************************************************************************************
     * Checks to see if there are any moves to get the player out of checkmate
     *
     * @param player a player object to see if their king is in checkmate
     * @return {@code true} if player is in checkmate, else {@code false}
     ******************************************************************************************************************/
    public boolean checkmate(Player player) {

        //coordinate's of player's piece
        for (int row = 0; row < 8; row++)
            for (int col = 0; col < 8; col++)

                //if the piece belongs to the player
                if ((board[row][col] != null && board[row][col].player() == currentPlayer())) {

                    //coordinates of attempted space to move
                    for (int atrow = 0; atrow < 8; atrow++) {
                        for (int atcol = 0; atcol < 8; atcol++) {

                            //if the move is valid
                            if(board[row][col] != null
                                    && board[row][col].isValidMove(new Move(row, col, atrow, atcol),board)){

                                //save and make the move
                                saveMove(row, col, atrow, atcol);
                                move(new Move(row, col, atrow, atcol));

                                // if the player isn't in check, undo move, and return false
                            if (!inCheck(currentPlayer())){
                                undoMove();
                                return false;
                            }

                            //otherwise undo move and keep checking
                            else
                                undoMove();
                        }
                        }
                    }
                }
                    return true;
    }

    public void saveMove(int fromRow, int fromCol, int toRow, int toCol) {

        //Incremented BEFORE saving to ensure FIRST moves saves in element
        //ONE for simplicity.
        numMoves++;

        //Creates blank string to hold integers turned into strings
        String saveSpot = "";

        //Adding each number to the blank string in specific order
        saveSpot += Integer.toString(fromRow);
        saveSpot += Integer.toString(fromCol);
        saveSpot += Integer.toString(toRow);
        saveSpot += Integer.toString(toCol);

        //If statement for adding piece type to string if taking a piece
        if (board[toRow][toCol] != null && board[toRow][toCol].player() != currentPlayer()) {

            //Increment pieces taken for accurate sorting into list
            piecesTaken++;

            //Saves piece to string
            saveSpot += board[toRow][toCol].type();

            //Saves piece to IChessPiece ArrayList
            takenPieces.add(piecesTaken, board[toRow][toCol]);
        }

        //If statement for pawn in endzone
        if (board[fromRow][fromCol].type().equals("Pawn")
            && (toRow == 7 || toRow == 0)){
            saveSpot += "@";
        }

        //Add string to ArrayList of strings
        moveList.add(numMoves, saveSpot);
        System.out.println(moveList);
    }

    public void undoMove(){
        if (numMoves > 0) {

            //Getting string representation of previous move
            //Using num moves to ensure accurate index
            String savedSpot = moveList.get(numMoves);

            //This statement covers for castling(explanation in castling methods)
            if (savedSpot.equals("7777")) {

                //Removes the signal "7777" move
                moveList.remove(numMoves);

                //Decrements to show the removal
                numMoves--;

                //Loads most previous move string
                savedSpot = moveList.get(numMoves);

                //Takes the char from 0-3, turns it into a string, parses it into an int
                int toRow = Integer.parseInt(Character.toString(savedSpot.charAt(0)));
                int toCol = Integer.parseInt(Character.toString(savedSpot.charAt(1)));
                int fromRow = Integer.parseInt(Character.toString(savedSpot.charAt(2)));
                int fromCol = Integer.parseInt(Character.toString(savedSpot.charAt(3)));

                //Creates move object
                Move m = new Move(fromRow, fromCol, toRow, toCol);

                //Checks for first move boolean reset
                resetFirstMoveBoolean(m);

                //Makes move
                move(m);

                //Removes the 1st castle move
                moveList.remove(numMoves);

                //Decrements to show the removal
                numMoves--;

                //Loads most previous move string
                savedSpot = moveList.get(numMoves);

                //Takes the char from 0-3, turns it into a string, parses it into an int
                toRow = Integer.parseInt(Character.toString(savedSpot.charAt(0)));
                toCol = Integer.parseInt(Character.toString(savedSpot.charAt(1)));
                fromRow = Integer.parseInt(Character.toString(savedSpot.charAt(2)));
                fromCol = Integer.parseInt(Character.toString(savedSpot.charAt(3)));

                //Creates move object
                Move m2 = new Move(fromRow, fromCol, toRow, toCol);

                //Checks for first move boolean reset
                resetFirstMoveBoolean(m2);

                //Makes move
                move(m2);

                //Removes the "2nd" castle move
                moveList.remove(numMoves);

                //Decrements to show the removal
                numMoves--;

                //If statement for whiteRightRook boolean
                if (board[fromRow][fromCol] != null &&
                        board[fromRow][fromCol].type().equals("Rook") && (toCol == 7 && toRow == 7)) {
                    whiteRightRook = true;
                }
            }

            //If statement for pawn transformation
            else if (savedSpot.length() == 5 && savedSpot.substring(4).equals("@")){

                //Loads most previous move string
                savedSpot = moveList.get(numMoves);

                //Takes the char from 0-3, turns it into a string, parses it into an int
                int toRow = Integer.parseInt(Character.toString(savedSpot.charAt(0)));
                int toCol = Integer.parseInt(Character.toString(savedSpot.charAt(1)));
                int fromRow = Integer.parseInt(Character.toString(savedSpot.charAt(2)));
                int fromCol = Integer.parseInt(Character.toString(savedSpot.charAt(3)));

                //Creates move object
                Move m = new Move(fromRow, fromCol, toRow, toCol);

                //Reset first move boolean
                resetFirstMoveBoolean(m);

                //Makes move
                move(m);

                //Sets pawn
                setPiece(toRow, toCol, new Pawn(currentPlayer()));

                //Removes the pawn transformation move
                moveList.remove(numMoves);

                //Decrements to show the removal
                numMoves--;
            }

            //Else covers En Passant
            else if (savedSpot.equals("0474")){

                //Removes the signal "0747" move
                moveList.remove(numMoves);

                //Decrements to show the removal
                numMoves--;

                //Loads most previous move string
                savedSpot = moveList.get(numMoves);

                //Takes the char from 0-3, turns it into a string, parses it into an int
                int toRow = Integer.parseInt(Character.toString(savedSpot.charAt(0)));
                int toCol = Integer.parseInt(Character.toString(savedSpot.charAt(1)));
                int fromRow = Integer.parseInt(Character.toString(savedSpot.charAt(2)));
                int fromCol = Integer.parseInt(Character.toString(savedSpot.charAt(3)));

                //Creates move object
                Move m = new Move(fromRow, fromCol, toRow, toCol);

                //Checks for first move boolean reset
                resetFirstMoveBoolean(m);

                //Makes move
                move(m);

                //if statement for pawn placement
                if (toCol > fromCol){
                    setPiece(toRow, toCol - 1, new Pawn(currentPlayer().next()));
                } else {
                    setPiece(toRow, toCol + 1, new Pawn(currentPlayer().next()));
                }
            }

            //Else covers everything else
            else {

                //Takes the char from 0-3, turns it into a string, parses it into an int
                int toRow = Integer.parseInt(Character.toString(savedSpot.charAt(0)));
                int toCol = Integer.parseInt(Character.toString(savedSpot.charAt(1)));
                int fromRow = Integer.parseInt(Character.toString(savedSpot.charAt(2)));
                int fromCol = Integer.parseInt(Character.toString(savedSpot.charAt(3)));

                //Creates move object
                Move m = new Move(fromRow, fromCol, toRow, toCol);

                //resets Boolean if part of castle
                resetFirstMoveBoolean(m);

                //Makes move
                move(m);

                //If statement for setPiece
                if (savedSpot.length() > 4) {
                    setPiece(fromRow, fromCol, takenPieces.get(piecesTaken));
                    piecesTaken--;
                }

                //Removes element at desired index
                moveList.remove(numMoves);

                //Decrements numMoves to reflect the removal 0f the element at that index
                numMoves--;
            }
        }
    }


    public boolean canCastleLeft(Player p){

        //Creates boolean
        boolean valid = false;

        //Checks for being a black player1
        if (p == player2){

            //Checks to make sure we are not in check
            if (!inCheck(p)) {

                //Check if it is black king
                if (board[0][4].type().equals("King") && board[0][4].player() == p && blackKing) {

                    //Checks to see if left-Rook is in spot and is black
                    if (board[0][0].type().equals("Rook") && board[0][0].player() == p && blackLeftRook) {

                        //check if spots between are null
                        if (board[0][1] == null &&
                                board[0][2] == null &&
                                board[0][3] == null) {
                            valid = true;
                        }
                    }
                }
            }
        } else if (p == player1){

            //Checks to make sure we are not in check
            if (!inCheck(p)) {

                //Check if it is white king
                if (board[7][4].type().equals("King") && board[7][4].player() == p && whiteKing) {

                    //Checks to see if left-Rook is in spot and is black
                    if (board[7][0].type().equals("Rook") && board[7][0].player() == p && whiteLeftRook) {

                        //check if spots between are null
                        if (board[7][1] == null &&
                                board[7][2] == null &&
                                board[7][3] == null) {
                            valid = true;
                        }
                    }
                }
            }
        }
        return valid;
    }

    public void castleLeft(Player p){

        //Checks for player
        if (p == player2){
            if (canCastleLeft(p)){

                //Create move objects, move, save
                Move m = new Move(0, 0, 0, 3);
                Move m1 = new Move(0, 4, 0, 2);
                saveMove(0, 4, 0, 3);
                saveMove(0, 3, 0, 2);

                //To show that the previous moves are a castle sequence
                saveMove(7, 7, 7, 7);
                move(m);
                move(m1);
                if (inCheck(p)){
                    undoMove();
                }
            }
        }

        //Checks for player
        else if (p == player1){
            if (canCastleLeft(p)) {

                //Create move objects, move, save
                Move m = new Move(7, 0, 7, 3);
                Move m1 = new Move(7, 4, 7, 2);
                saveMove(7, 0, 7, 3);
                saveMove(7, 4, 7, 2);

                //To show previous moves are a castle sequence
                saveMove(7, 7, 7, 7);
                move(m);
                move(m1);

                //Checks to see if move put us into check
                if (inCheck(p)) {

                    //Reverses moves to allow for another option
                    undoMove();
                }
            }
        }
    }

    public boolean canCastleRight(Player p){

        //Creates boolean
        boolean valid = false;

        //Checks for black player2
        if (p == player2){

            //Checks to make sue we are not in check
            if (!inCheck(p)) {

                //Checks if right-rook is in it's spot
                if (board[0][7].type().equals("Rook") && board[0][7].player() == p && blackRightRook == true) {

                    //Checks location of king
                    if (board[0][4].type().equals("King") && board[0][4].player() == p && blackKing == true) {

                        //check if spots between are null
                        if (board[0][6] == null && board[0][5] == null) {
                            valid = true;
                        }
                    }
                }
            }

            //check if it is white player1
        } else if (p == player1){

            //Checks to make sure we are not in check
            if (!inCheck(p)) {

                //Checks if right-rook is in it's spot
                if (board[7][7].type().equals("Rook") && board[7][7].player() == p && whiteRightRook == true) {

                    //Checks location of king
                    if (board[7][4].type().equals("King") && board[7][4].player() == p && whiteKing == true) {

                        //check if spots between are null
                        if (board[7][6] == null && board[7][5] == null) {
                            valid = true;
                        }
                    }
                }
            }
        }
        return valid;
    }

    public void castleRight(Player p){

        //Checks for player
        if (p == player2){
            if (canCastleRight(p)){

                //Create move objects, move, save
                Move m = new Move(0, 7, 0, 5);
                Move m1 = new Move(0, 4, 0, 6);
                saveMove(0, 7, 0, 5);
                saveMove(0, 4, 0, 6);

                //To show that previous move was a castle sequence
                saveMove(7, 7, 7, 7);
                move(m);
                move(m1);
                if (inCheck(p)){

                    //If in check, moves pieces back to continue turn
                    undoMove();
                }
            }
        }

        //Checks for player
        else if (p == player1){
            if (canCastleRight(p)){

                //Create move objects, move, save
                Move m = new Move(7, 7, 7, 5);
                Move m1 = new Move(7, 4, 7, 6);
                saveMove(7, 7, 7, 5);
                saveMove(7, 4, 7, 6);

                //To show that the previous two moves were a castle sequence
                saveMove(7, 7, 7, 7);
                move(m);
                move(m1);
                if (inCheck(p)){

                    //If in check, moves pieces back to continue turn
                    undoMove();
                }
            }
        }
    }

    public boolean canEnPassant(Player p, Move move) {

        if(numMoves != 0) {
            String lastMove = moveList.get(numMoves);

            int direction = 1; //direction for white

            if (board[move.fromRow][move.fromColumn] != null &&
                    board[move.fromRow][move.fromColumn].player() == Player.BLACK) //direction for black
                direction = -1;

            //Takes the char from 0-3, turns it into a string, parses it into an int
            int pawnFromRow = Integer.parseInt(Character.toString(lastMove.charAt(0)));
            int pawnFromCol = Integer.parseInt(Character.toString(lastMove.charAt(1)));
            int pawnToRow = Integer.parseInt(Character.toString(lastMove.charAt(2)));
            int pawnToCol = Integer.parseInt(Character.toString(lastMove.charAt(3)));

            // if move was two spaces
            if (Math.abs(pawnFromRow - pawnToRow) == 2) {

                // if piece moving is a pawn
                if (board[pawnToRow][pawnToCol] != null && board[pawnToRow][pawnToCol].type().equals("Pawn")
                        && board[pawnToRow][pawnToCol].player() != p) {
                    if (move.fromRow == pawnToRow &&
                            move.toRow + direction == pawnToRow && move.toColumn == pawnToCol) {

                        //Sets piece move to null
                        setPiece(pawnToRow, pawnFromCol, null);
                        return true;
                    }

                }
            }
        }
        return false;
    }

    public void setFirstMoveBoolean(Move m){

        //If statement for whiteRightRook
        if (board[m.fromRow][m.fromColumn] != null
                && board[m.fromRow][m.fromColumn].type().equals("Rook")
                && (m.fromRow == 7 && m.fromColumn == 7)
                && board[m.fromRow][m.fromColumn].player() == currentPlayer()){
            whiteRightRook = false;
        }

        //If statement for whiteKing
        if (board[m.fromRow][m.fromColumn] != null
                && board[m.fromRow][m.fromColumn].type().equals("King")
                && (m.fromRow == 7 && m.fromColumn == 4)
                && board[m.fromRow][m.fromColumn].player() == currentPlayer()){
            whiteKing = false;
        }

        //If statement for whiteLeftRook
        if (board[m.fromRow][m.fromColumn] != null
                && board[m.fromRow][m.fromColumn].type().equals("Rook")
                && (m.fromRow == 7 && m.fromColumn == 0)
                && board[m.fromRow][m.fromColumn].player() == currentPlayer()){
            whiteLeftRook = false;
        }

        //If statement for blackRightRook
        if (board[m.fromRow][m.fromColumn] != null
                && board[m.fromRow][m.fromColumn].type().equals("Rook")
                && (m.fromRow == 0 && m.fromColumn == 7)
                && board[m.fromRow][m.fromColumn].player() == currentPlayer()){
            blackRightRook = false;
        }

        //If statement for blackKing
        if (board[m.fromRow][m.fromColumn] != null
                && board[m.fromRow][m.fromColumn].type().equals("King")
                && (m.fromRow == 0 && m.fromColumn == 4)
                && board[m.fromRow][m.fromColumn].player() == currentPlayer()){
            blackKing = false;
        }

        //If statement for blackLeftRook
        if (board[m.fromRow][m.fromColumn] != null
                && board[m.fromRow][m.fromColumn].type().equals("Rook")
                && (m.fromRow == 0 && m.fromColumn == 0)
                && board[m.fromRow][m.fromColumn].player() == currentPlayer()){
            blackLeftRook = false;
        }

    }

    public void resetFirstMoveBoolean(Move m){

        //If statement for whiteRightRook
        if (board[m.fromRow][m.fromColumn] != null
                && board[m.fromRow][m.fromColumn].type().equals("Rook")
            && (m.toRow == 7 && m.toColumn == 7)
            && board[m.fromRow][m.fromColumn].player() == currentPlayer()){
            whiteRightRook = true;
        }

        //If statement for whiteKing
        if (board[m.fromRow][m.fromColumn] != null
                && board[m.fromRow][m.fromColumn].type().equals("King")
                && (m.toRow == 7 && m.toColumn == 4)
                && board[m.fromRow][m.fromColumn].player() == currentPlayer()){
            whiteKing = true;
        }

        //If statement for whiteLeftRook
        if (board[m.fromRow][m.fromColumn] != null
                && board[m.fromRow][m.fromColumn].type().equals("Rook")
                && (m.toRow == 7 && m.toColumn == 0)
                && board[m.fromRow][m.fromColumn].player() == currentPlayer()){
            whiteLeftRook = true;
        }

        //If statement for blackRightRook
        if (board[m.fromRow][m.fromColumn] != null
                && board[m.fromRow][m.fromColumn].type().equals("Rook")
                && (m.toRow == 0 && m.toColumn == 7)
                && board[m.fromRow][m.fromColumn].player() == currentPlayer()){
            blackRightRook = true;
        }

        //If statement for blackKing
        if (board[m.fromRow][m.fromColumn] != null
                && board[m.fromRow][m.fromColumn].type().equals("King")
                && (m.toRow == 0 && m.toColumn == 4)
                && board[m.fromRow][m.fromColumn].player() == currentPlayer()){
            blackKing = true;
        }

        //If statement for blackLeftRook
        if (board[m.fromRow][m.fromColumn] != null
                && board[m.fromRow][m.fromColumn].type().equals("Rook")
                && (m.toRow == 0 && m.toColumn == 0)
                && board[m.fromRow][m.fromColumn].player() == currentPlayer()){
            blackLeftRook = true;
        }
    }

    public Player currentPlayer() {
        return player1;
    }

    public int numRows() {
        return 8;
    }

    public int numColumns() {
        return 8;
    }

    public IChessPiece pieceAt(int row, int column) {
        return board[row][column];
    }

    public void setNextPlayer() {
       player1 = player1.next();
    }

    public void setPiece(int row, int column, IChessPiece piece) {
        board[row][column] = piece;
    }

    /*******************************************************************************************************************
     *This method has the AI take moves for the black pieces. The order of logic is listed below
     *
     * a. Check to see if you are in check.
     * 		i. If so, get out of check by moving the king, or placing a piece to block the check
     *
     * b. Attempt to put opponent into check (or checkmate).
     * 		i. Attempt to put opponent into check without losing your piece
     *		ii. Perhaps you have won the game.
     *
     *c. Determine if any of your pieces are in danger,
     *		i. Capture the threat if you can.
     *		ii. Attempt to protect that piece.
     *
     *d. Move a piece (pawns first) forward toward opponent king
     *		i. check to see if that piece is in danger of being removed, if so, move a different piece.
     ******************************************************************************************************************/

    public void AI() {
        /*
         * Write a simple AI set of rules in the following order.
         * a. Check to see if you are in check.
         * 		i. If so, get out of check by moving the king or placing a piece to block the check
         *
         * b. Attempt to put opponent into check (or checkmate).
         * 		i. Attempt to put opponent into check without losing your piece
         *		ii. Perhaps you have won the game.
         *
         *c. Determine if any of your pieces are in danger,
         *		i. Move them if you can.
         *		ii. Attempt to protect that piece.
         *
         *d. Move a piece (pawns first) forward toward opponent king
         *		i. check to see if that piece is in danger of being removed, if so, move a different piece.
         */


        //used as coordinates for the white king
        int wKingX = 0;
        int wKingY = 0;

        //used to reset the loops to find alternate moves
        boolean goneYet = false;
        boolean inDanger = false;

        //used to prioritize pieces within the loops
        String[] types = {"Queen", "Bishop", "Rook", "Knight", "Pawn"};


        if (currentPlayer() == Player.BLACK) {

            //locates the white king
            for (int i = 0; i < 8; i++)
                for (int j = 0; j < 8; j++) {
                    if (board[i][j] != null && board[i][j].player() == Player.WHITE && board[i][j].type().equals("King")) {
                        wKingX = i;
                        wKingY = j;
                    }
                }

            //keeps black out of check
            if (inCheck(Player.BLACK)) {

                //black piece coordinates
                for (int i = 0; i < 8; i++) {
                    for (int j = 0; j < 8; j++)

                        //attempted space to move to
                        for (int k = 0; k < 8; k++)
                            for (int l = 0; l < 8; l++) {

                                //if the piece belongs to black, the move is valid and we haven't gone yet
                                if (board[i][j] != null && board[i][j].player() == Player.BLACK
                                        && board[i][j].isValidMove(new Move(i, j, k, l), board) && !goneYet) {

                                    //saves and tries move
                                    saveMove(i, j, k, l);
                                    move(new Move(i, j, k, l));
                                    goneYet = true;

                                    //if black is still in check, undoes move and does loop again
                                    if (inCheck(Player.BLACK)) {
                                        undoMove();
                                        goneYet = false;
                                    }
                                }
                            }
                }
            }

            // tries to put white in check
            if (!goneYet) {

                //Black piece coordinates
                for (int i = 0; i < 8; i++) {
                    for (int j = 0; j < 8; j++)
                        //Attempted space

                        for (int k = 0; k < 8; k++)
                            for (int l = 0; l < 8; l++) {

                                //if the piece belongs to black, the move is valid and AI hasn't gone yet
                                if (board[i][j] != null && board[i][j].player() == Player.BLACK
                                        && board[i][j].isValidMove(new Move(i, j, k, l), board)
                                        && !goneYet) {

                                    // saves and makes the move
                                    saveMove(i, j, k, l);
                                    move(new Move(i, j, k, l));
                                    goneYet = true;


                                    // sets the boolean if we didn't get white in check
                                    if (board[k][l] != null &&
                                            board[k][l].player() == Player.BLACK
                                            && !board[k][l].isValidMove(new Move(k, l, wKingX, wKingY), board)) {
                                        inDanger = true;
                                    }

                                    //checks to see if the move was dangerous
                                    for (int m = 0; m < 8; m++) {
                                        for (int n = 0; n < 8; n++)
                                            if (board[m][n] != null && board[k][l] != null
                                                    && board[m][n].player() == Player.WHITE
                                                    && board[m][n].isValidMove(new Move(m, n, k, l), board)) {
                                                inDanger = true;
                                            }
                                    }
                                }

                                //undoes move to trigger the loop again
                                if (inDanger) {
                                    undoMove();
                                    goneYet = false;
                                    inDanger = false;
                                }
                            }
                }
            }
            //attempts to move pieces out of danger
            if (!goneYet) {

                //prioritizes the pieces by type
                for (int type = 0; type < 5; type++)

                    //white's coordinate
                    for (int i = 0; i < 8; i++) {
                        for (int j = 0; j < 8; j++)

                            //black's threatened coordinate
                            for (int k = 0; k < 8; k++)
                                for (int l = 0; l < 8; l++) {

                                    //checks to see if AI's piece can be targeted by black and if AI has gone
                                    if (board[i][j] != null && board[i][j].player() == Player.WHITE
                                            && board[k][l] != null && board[k][l].player() == Player.BLACK
                                            && board[k][l].type().equals(types[type])
                                            && board[i][j].isValidMove(new Move(i, j, k, l), board)) {

                                        //for piece prioritization
                                        for (int typeTwo = 0; typeTwo < 5; typeTwo++) {

                                            //capture the threat
                                            //AI's piece to move coordinates
                                            for (int m = 0; m < 8; m++)
                                                for (int n = 0; n < 8; n++) {

                                                    //if the piece belongs to AI and if the move is valid
                                                    if (board[m][n] != null && board[m][n].player() == Player.BLACK
                                                            && board[m][n].isValidMove(new Move(m, n, i, j), board)
                                                            && board[m][n].type().equals(types[4 - typeTwo]) && !goneYet) {

                                                        //saves and makes the move
                                                        saveMove(m, n, i, j);
                                                        move(new Move(m, n, i, j));
                                                        goneYet = true;

                                                        //sees if any white pieces can take it
                                                        for (int o = 0; o < 8; o++) {
                                                            for (int p = 0; p < 8; p++)
                                                                if (board[o][p] != null && board[i][j] != null &&
                                                                        board[i][j].player() == Player.BLACK
                                                                        && board[o][p].player() == Player.WHITE
                                                                        && board[o][p].isValidMove(new Move(o, p, i, j), board)) {
                                                                    inDanger = true;
                                                                }
                                                        }

                                                        //checks to see if move put AI in check
                                                        if (inCheck(Player.BLACK))
                                                            inDanger = true;

                                                        //redoes loop if need be
                                                        if (inDanger) {
                                                            undoMove();
                                                            goneYet = false;
                                                            inDanger = false;
                                                        }
                                                    }


                                                    //if AI couldn't capture the threat
                                                     if (!goneYet) {

                                                    //attempted move coordinates
                                                        for (int t = 0; t < 8; t++)
                                                            for (int q = 0; q < 8; q++)

                                                                //if the move is valid and our piece is of the right type
                                                                if (board[m][n] != null && board[m][n].player() == Player.BLACK
                                                                        && board[m][n].isValidMove(new Move(m, n, t, q), board)
                                                                        && board[m][n].type().equals(types[4 - typeTwo]) && !goneYet) {

                                                                    //saves and makes the move
                                                                    saveMove(m, n, t, q);
                                                                    move(new Move(m, n, t, q));
                                                                    goneYet = true;

                                                                    //sees if any white pieces can take it
                                                                    for (int o = 0; o < 8; o++) {
                                                                        for (int p = 0; p < 8; p++)
                                                                            if (board[o][p] != null && board[k][l] != null
                                                                                    && board[o][p].player() == Player.WHITE
                                                                                    && board[o][p].isValidMove(new Move(o, p, k, l), board)) {
                                                                                inDanger = true;
                                                                            }
                                                                    }

                                                                    //checks to see if move put AI in check
                                                                    if (inCheck(Player.BLACK))
                                                                        inDanger = true;

                                                                    //redoes loop if need be
                                                                    if (inDanger) {
                                                                        undoMove();
                                                                        goneYet = false;
                                                                        inDanger = false;
                                                                    }
                                                                }
                                                    }
                                                }
                                        }
                                    }
                                }
                    }
            }

            //moves pieces safely
            if (!goneYet){

                //prioritizes pieces
                for (int type = 0; type < 5; type++)

                    //AI's coordinates
                    for (int i = 0; i < 8; i++) {
                        for (int j = 0; j < 8; j++)

                            //attempted move coordinates
                            for (int k = 0; k < 8; k++)
                                for (int l = 0; l < 8; l++)

                                    //if the piece belongs to AI, is the right type, the move is valid, and
                                    //AI hasn't gone yet
                                    if (board[i][j] != null && board[i][j].player() == Player.BLACK
                                            && board[i][j].type().equals(types[4 - type])
                                            && board[i][j].isValidMove(new Move(i, j, k, l), board)
                                            && !goneYet) {

                                        //saves  and attempts the move
                                        saveMove(i, j, k, l);
                                        move(new Move(i, j, k, l));
                                        goneYet = true;

                                        //checks to see if white can take the moved piece
                                        for (int o = 0; o < 8; o++) {
                                            for (int p = 0; p < 8; p++)
                                                if (board[o][p] != null && board[k][l] != null
                                                        && board[o][p].player() == Player.WHITE
                                                        && board[o][p].isValidMove(new Move(o, p, k, l), board)) {
                                                    inDanger = true;
                                                }
                                        }

                                        //redoes loop if necessary
                                        if (inDanger) {
                                            undoMove();
                                            goneYet = false;
                                            inDanger = false;
                                        }
                                    }
                    }
            }
        }
    }
}
