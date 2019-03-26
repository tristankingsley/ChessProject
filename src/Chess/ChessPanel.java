package Chess;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/*******************************************************************************************************************
 * This class uses buttons, toggles, labels, and panels in order to create
 * a working user-interface for the game of Chess
 ******************************************************************************************************************/
public class ChessPanel extends JPanel {

    //Button array for the board
    private JButton[][] board;

    //ChessModel object giving us access to methods in ChessModel
    protected ChessModel model;

    //JButton for undoing moves
    private JButton undoBtn;

    //JButton for castling on the right
    private JButton castleRight;

    //JButton for castling on the left
    private JButton castleLeft;

    //JToggleButton for turning on the AI
    protected JToggleButton AI;

    //JToggleButton for turning on two player mode
    protected JToggleButton twoPlayer;

    //Image icons for white pieces
    private ImageIcon wRook;
    private ImageIcon wBishop;
    private ImageIcon wQueen;
    private ImageIcon wKing;
    private ImageIcon wPawn;
    private ImageIcon wKnight;

    //Image icons for black pieces
    private ImageIcon bRook;
    private ImageIcon bBishop;
    private ImageIcon bQueen;
    private ImageIcon bKing;
    private ImageIcon bPawn;
    private ImageIcon bKnight;

    //Boolean for the first part of a move
    private boolean firstTurnFlag;

    //Integer values representing the coordiantes of a move
    private int fromRow;
    private int toRow;
    private int fromCol;
    private int toCol;

    //JLabels for piece selected and for player turn
    private JLabel selected;
    private JLabel turn;

    //JButton for reseting game back to beginning
    private JButton reset;

    //Listener object for ActionPerformed
    private listener listener;

/*******************************************************************************************************************
 * This constructor instantiates buttons, toggles, labels, and panels in order to create
 * a working user-interface for the game of Chess
 ******************************************************************************************************************/
 public ChessPanel() {

        //Instantiates and sets text of JToggleButtons
        AI = new JToggleButton("VS AI");
        twoPlayer = new JToggleButton("VS PLAYER");

        //Undo button
        undoBtn = new JButton("Undo Move");

        //Reset button
        reset = new JButton("Reset");

        //Castle buttons
        castleRight = new JButton("Castle Right Side");
        castleLeft = new JButton("Castle Left Side");

        //Instantiates model object
        model = new ChessModel();

        //Instantiates button array and listener
        board = new JButton[model.numRows()][model.numColumns()];
        listener = new listener();

        //Creates image icons for each respective piece
        createIcons();

        //Instantiates JPanel for board and buttons
        JPanel boardpanel = new JPanel();
        JPanel buttonpanel = new JPanel();
        boardpanel.setLayout(new GridLayout(model.numRows(), model.numColumns(), 1, 1));

        for (int r = 0; r < model.numRows(); r++) {
            for (int c = 0; c < model.numColumns(); c++) {
                if (model.pieceAt(r, c) == null) {
                    //Sets text/icon of null pieces to null
                    board[r][c] = new JButton("", null);
                    board[r][c].addActionListener(listener);
                } else if (model.pieceAt(r, c).player() == Player.WHITE)
                    //Sets image icons to respective white pieces
                    placeWhitePieces(r, c);
                else if (model.pieceAt(r, c).player() == Player.BLACK)
                    //Sets image icons to respective black pieces
                    placeBlackPieces(r, c);

                //Sets background color of pieces, adds button array to panel
                setBackGroundColor(r, c);
                boardpanel.add(board[r][c]);
            }
        }

        //Creates new button group, adds both AI and twoPlayer toggles
        ButtonGroup playerOptions = new ButtonGroup();
        playerOptions.add(AI);
        playerOptions.add(twoPlayer);

        //Instantiates and sets text of JLabels
        selected = new JLabel("Not selected");
        turn = new JLabel(model.currentPlayer().toString() + "'S TURN");

        //Sets size of JLabels
        turn.setPreferredSize(new Dimension(100, 20));
        selected.setPreferredSize(new Dimension(100,20));

        //Sets new Grid layout with custom insets
        buttonpanel.setLayout(new GridBagLayout());
        GridBagConstraints position = new GridBagConstraints();
        position.insets = new Insets(10, 0, 10, 0);

        //Adds selected JLabel to grid
        position.gridy = 0;
        buttonpanel.add(selected, position);

        //Adds turn JLabel to grid
        position.gridy = 1;
        buttonpanel.add(turn,position);

        //Adds action listener to reset button, sets custom size
        reset.addActionListener(listener);
        reset.setPreferredSize(new Dimension(150, 40));

        //Sets location of reset button
        position.gridy = 2;
        buttonpanel.add(reset, position);
        boardpanel.setPreferredSize(new Dimension(600, 600));

        //Adds actionlistener, sets size, adds to BorderLayout
        position.gridy = 3;
        undoBtn.addActionListener(listener);
        undoBtn.setPreferredSize(new Dimension(150, 40));
        buttonpanel.add(undoBtn, position);

        position.gridy = 4;
        castleLeft.addActionListener(listener);
        castleLeft.setPreferredSize(new Dimension(150, 40));
        buttonpanel.add(castleLeft, position);

        position.gridy = 5;
        castleRight.addActionListener(listener);
        castleRight.setPreferredSize(new Dimension(150, 40));
        buttonpanel.add(castleRight, position);

        position.gridy = 6;
        buttonpanel.add(AI, position);
        AI.setSelected(true);

        position.insets = new Insets(0, 0, 0, 0);
        position.gridy = 7;
        buttonpanel.add(twoPlayer, position);

        //Sets size of button panel
        buttonpanel.setPreferredSize(new Dimension(150, 700));

        //Adds both panel and sets firstTurnFlag to true for the beginning of the game
        add(boardpanel, BorderLayout.WEST);
        add(buttonpanel, BorderLayout.EAST);
        firstTurnFlag = true;
    }

    /*******************************************************************************************************************
     * This method sets the background color to white or black of each respective JButton in the button array
     * @param r Being the respective row of the JButton
     * @param c Being the respective column of the JButton
     ******************************************************************************************************************/
    private void setBackGroundColor(int r, int c) {
        if ((c % 2 == 1 && r % 2 == 0) || (c % 2 == 0 && r % 2 == 1)) {
            board[r][c].setBackground(Color.LIGHT_GRAY);
        } else if ((c % 2 == 0 && r % 2 == 0) || (c % 2 == 1 && r % 2 == 1)) {
            board[r][c].setBackground(Color.WHITE);
        }
    }

    /*******************************************************************************************************************
     * This method adds an action listener and sets and ImageIcon for each respective white piece
     * @param r Being the respective row of the piece
     * @param c Being the respective column of the piece
     ******************************************************************************************************************/
    private void placeWhitePieces(int r, int c) {
        if (model.pieceAt(r, c).type().equals("Pawn")) {
            board[r][c] = new JButton(null, wPawn);
            board[r][c].addActionListener(listener);
        }
        if (model.pieceAt(r, c).type().equals("Rook")) {
            board[r][c] = new JButton(null, wRook);
            board[r][c].addActionListener(listener);
        }
        if (model.pieceAt(r, c).type().equals("Knight")) {
            board[r][c] = new JButton(null, wKnight);
            board[r][c].addActionListener(listener);
        }
        if (model.pieceAt(r, c).type().equals("Bishop")) {
            board[r][c] = new JButton(null, wBishop);
            board[r][c].addActionListener(listener);
        }
        if (model.pieceAt(r, c).type().equals("Queen")) {
            board[r][c] = new JButton(null, wQueen);
            board[r][c].addActionListener(listener);
        }
        if (model.pieceAt(r, c).type().equals("King")) {
            board[r][c] = new JButton(null, wKing);
            board[r][c].addActionListener(listener);
        }
    }

    /*******************************************************************************************************************
     * This method adds an action listener and sets and ImageIcon for each respective black piece
     * @param r Being the respective row of the piece
     * @param c Being the respective column of the piece
     ******************************************************************************************************************/
    private void placeBlackPieces(int r, int c){
        if (model.pieceAt(r, c).type().equals("Pawn")){
            board[r][c] = new JButton(null, bPawn);
            board[r][c].addActionListener(listener);
        }
        if (model.pieceAt(r, c).type().equals("Rook")){
            board[r][c] = new JButton(null, bRook);
            board[r][c].addActionListener(listener);
        }
        if (model.pieceAt(r, c).type().equals("Knight")){
            board[r][c] = new JButton(null, bKnight);
            board[r][c].addActionListener(listener);
        }
        if (model.pieceAt(r, c).type().equals("Bishop")){
            board[r][c] = new JButton(null, bBishop);
            board[r][c].addActionListener(listener);
        }
        if (model.pieceAt(r, c).type().equals("Queen")){
            board[r][c] = new JButton(null, bQueen);
            board[r][c].addActionListener(listener);
        }
        if (model.pieceAt(r, c).type().equals("King")){
            board[r][c] = new JButton(null, bKing);
            board[r][c].addActionListener(listener);
        }
    }

    /*******************************************************************************************************************
     * This method loads PNG image files from the project folder and assigns them to respective chess pieces
     ******************************************************************************************************************/
    private void createIcons() {
        // Sets the Image for white player pieces
        wRook = new ImageIcon("./src/Chess/wRook.png");
        wBishop = new ImageIcon("./src/Chess/wBishop.png");
        wQueen = new ImageIcon("./src/Chess/wQueen.png");
        wKing = new ImageIcon("./src/Chess/wKing.png");
        wPawn = new ImageIcon("./src/Chess/wPawn.png");
        wKnight = new ImageIcon("./src/Chess/wKnight.png");

        bRook = new ImageIcon("./src/Chess/bRook.png");
        bBishop = new ImageIcon("./src/Chess/bBishop.png");
        bQueen = new ImageIcon("./src/Chess/bQueen.png");
        bKing = new ImageIcon("./src/Chess/bKing.png");
        bPawn = new ImageIcon("./src/Chess/bPawn.png");
        bKnight = new ImageIcon("./src/Chess/bKnight.png");
    }

    /*******************************************************************************************************************
     * This method sets each icon for each respective piece, after determing what
     * each piece is using a double nested loop
     ******************************************************************************************************************/
    private void displayBoard() {

        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++)
                if (model.pieceAt(r, c) == null) {
                    board[r][c].setIcon(null);
                }
                else if (model.pieceAt(r, c).player() == Player.WHITE) {
                    if (model.pieceAt(r, c).type().equals("Pawn"))
                        board[r][c].setIcon(wPawn);

                    if (model.pieceAt(r, c).type().equals("Rook"))
                        board[r][c].setIcon(wRook);

                    if (model.pieceAt(r, c).type().equals("Knight"))
                        board[r][c].setIcon(wKnight);

                    if (model.pieceAt(r, c).type().equals("Bishop"))
                        board[r][c].setIcon(wBishop);

                    if (model.pieceAt(r, c).type().equals("Queen"))
                        board[r][c].setIcon(wQueen);

                    if (model.pieceAt(r, c).type().equals("King"))
                        board[r][c].setIcon(wKing);
                } else if (model.pieceAt(r, c).player() == Player.BLACK){
                    if (model.pieceAt(r, c).type().equals("Pawn"))
                        board[r][c].setIcon(bPawn);

                    if (model.pieceAt(r, c).type().equals("Rook"))
                        board[r][c].setIcon(bRook);

                    if (model.pieceAt(r, c).type().equals("Knight"))
                        board[r][c].setIcon(bKnight);

                    if (model.pieceAt(r, c).type().equals("Bishop"))
                        board[r][c].setIcon(bBishop);

                    if (model.pieceAt(r, c).type().equals("Queen"))
                        board[r][c].setIcon(bQueen);

                    if (model.pieceAt(r, c).type().equals("King"))
                        board[r][c].setIcon(bKing);
                }
        }
        repaint();
    }

    /*******************************************************************************************************************
     * This method contains many if statement and loops to determine things like if you
     * picked a valid move, if the you've reached the other endzone, if the AI is on, and also
     * runs certain methods from the model class if the respective buttons are clicked
     ******************************************************************************************************************/    private class listener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            for (int r = 0; r < model.numRows(); r++) {
                for (int c = 0; c < model.numColumns(); c++) {
                    if (board[r][c] == event.getSource()) {
                        if (firstTurnFlag == true) {
                            fromRow = r;
                            fromCol = c;
                            firstTurnFlag = false;
                            //If you clicked on a valid piece, text set to selected
                            if (model.pieceAt(r, c) != null)
                                selected.setText("Selected");
                            else
                                //If you clicked on an invalid piece, text set to Selected empty
                                selected.setText("Selected Empty");
                        } else {

                            //Sets text to display whose turn it is
                            turn.setText(model.currentPlayer().toString() + "'S TURN");
                            toRow = r;
                            toCol = c;
                            firstTurnFlag = true;
                            //Creates move object
                            Move m = new Move(fromRow, fromCol, toRow, toCol);
                            //Checks validity of move object
                            if ((model.isValidMove(m)) == true) {
                                //Saving before moving so that if there is
                                //a piece in the 'TO' spot, we can save it's
                                //type before it disappears
                                model.saveMove(fromRow, fromCol, toRow, toCol);
                                //Makes move
                                model.move(m);

                                //Checks to see if pawn made it in endzone
                                if (model.pawnInEndzone()) {
                                    //Array of transformation options
                                    String[] options = {"Queen", "Bishop", "Rook", "Knight"};
                                    //Opens JOptionPane with four buttons for transformation options
                                    int type = JOptionPane.showOptionDialog(null,
                                            "What do you want your pawn to transform into?",
                                            "Pawn transformation", JOptionPane.YES_NO_CANCEL_OPTION,
                                            JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
                                    //Runs transformation method
                                    model.pawnTransform(options[type]);
                                }
                                //Sets next player if move is completed
                                model.setNextPlayer();

                                //If current player in check, display message
                                if (model.inCheck(model.currentPlayer()))
                                    JOptionPane.showMessageDialog(null, model.currentPlayer() + " is in Check");

                                //If Ai is on, run the AI after he user makes a valid move
                                if (AI.isSelected()) {
                                    model.AI();

                                    //Auto sets pawns to Queen if endzone is reached
                                    if (model.pawnInEndzone()) {
                                        model.pawnTransform("Queen");
                                    }

                                    //Sets next player after AI move
                                    model.setNextPlayer();

                                    //If next player is in check, display message
                                    if (model.inCheck(model.currentPlayer()))
                                            JOptionPane.showMessageDialog(null, model.currentPlayer() + " is in Check");

                                } else {

                                    //Sets text to display current player's turn
                                    turn.setText(model.currentPlayer().toString() + "'S TURN");

                                }
                                //If not a valid move, then sets text to Not Selected
                                selected.setText("Not selected");
                                //Updates the viewable board
                                displayBoard();
                            }
                        }
                    }
                }
            }
            //If statement for clicking reset button
            if(reset == event.getSource()){
                //Runs constructor for model and displays new board
                model = new ChessModel();
                displayBoard();
            }

            //If statement for clicking undo button
            if (undoBtn == event.getSource()) {
                //Sets next player, undoes move, updates board
                model.setNextPlayer();
                model.undoMove();
                displayBoard();
            }

            //If statement for Castling right
            if (castleRight == event.getSource()){
                //Double checks boolean method for castling
                if(model.canCastleRight(model.currentPlayer())) {
                    //Performs castle method, updates board, sets next player
                    model.castleRight(model.currentPlayer());
                    displayBoard();
                    model.setNextPlayer();
                    //If statement for checking is AI is toggled on
                    if (AI.isSelected()) {
                        //Runs AI after castling, updates board, sets next player
                        model.AI();
                        displayBoard();
                        model.setNextPlayer();
                    }
                }
            }

            //If statement for Castling Left
            if (castleLeft == event.getSource()){
                //Double checks boolean method for castling
                if(model.canCastleLeft(model.currentPlayer())) {
                    //Performs castle method, updates board, sets next player
                    model.castleLeft(model.currentPlayer());
                    displayBoard();
                    model.setNextPlayer();
                    //Checks if AI is turned on
                    if (AI.isSelected()) {
                        //If so, run AI, updates board, then sets next player
                        model.AI();
                        displayBoard();
                        model.setNextPlayer();
                    }
                }
            }

            //If statement for AI toggle ON and checkmate
            if (AI.isSelected() && model.isComplete()){

                //Sets next player to see if THAT player has put the enemy in check
                model.setNextPlayer();
                //Shows message saying who has won
                JOptionPane.showMessageDialog(null,
                        "Game Over!" + "\n" + model.currentPlayer()
                                + "has won!");
                //Shows messages to click reset button if wanting to play again
                JOptionPane.showMessageDialog(null,
                        "Please click the 'OKAY; button if you" +
                                "\n would like to play again!");
                //Resets board anyway :)
                model = new ChessModel();
                displayBoard();
            }
        }
    }
}
