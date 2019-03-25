package Chess;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class ChessPanel extends JPanel {

    private JButton[][] board;
    protected ChessModel model;
    private JButton undoBtn;
    private JButton castleRight;
    private JButton castleLeft;
    protected JToggleButton AI;
    protected JToggleButton twoPlayer;

    private ImageIcon wRook;
    private ImageIcon wBishop;
    private ImageIcon wQueen;
    private ImageIcon wKing;
    private ImageIcon wPawn;
    private ImageIcon wKnight;

    private ImageIcon bRook;
    private ImageIcon bBishop;
    private ImageIcon bQueen;
    private ImageIcon bKing;
    private ImageIcon bPawn;
    private ImageIcon bKnight;

    private boolean firstTurnFlag;
    private int fromRow;
    private int toRow;
    private int fromCol;
    private int toCol;

    private JLabel selected;
    private JLabel turn;

    private JButton reset;
    // declare other intance variables as needed

    private listener listener;

    public void setwPawn(ImageIcon wPawn) {
        this.wPawn = wPawn;
    }

    public void setbPawn(ImageIcon bPawn) {
        this.bPawn = bPawn;
    }

    public ChessPanel() {

        AI = new JToggleButton("VS AI");
        twoPlayer = new JToggleButton("VS PLAYER");

        //Undo button
        undoBtn = new JButton("Undo Move");
        //Castle buttons
        castleRight = new JButton("Castle Right Side");
        castleLeft = new JButton("Castle Left Side");


        model = new ChessModel();
        board = new JButton[model.numRows()][model.numColumns()];
        listener = new listener();
        createIcons();

        JPanel boardpanel = new JPanel();
        JPanel buttonpanel = new JPanel();
        boardpanel.setLayout(new GridLayout(model.numRows(), model.numColumns(), 1, 1));

        for (int r = 0; r < model.numRows(); r++) {
            for (int c = 0; c < model.numColumns(); c++) {
                if (model.pieceAt(r, c) == null) {
                    board[r][c] = new JButton("", null);
                    board[r][c].addActionListener(listener);
                } else if (model.pieceAt(r, c).player() == Player.WHITE)
                    placeWhitePieces(r, c);
                else if (model.pieceAt(r, c).player() == Player.BLACK)
                    placeBlackPieces(r, c);

                setBackGroundColor(r, c);
                boardpanel.add(board[r][c]);
            }
        }

        ButtonGroup playerOptions = new ButtonGroup();

        playerOptions.add(AI);
        playerOptions.add(twoPlayer);

        selected = new JLabel("Not selected");

        turn = new JLabel(model.currentPlayer().toString() + "'S TURN");

        reset = new JButton("Reset");

        turn.setPreferredSize(new Dimension(100, 20));
        selected.setPreferredSize(new Dimension(100,20));

        buttonpanel.setLayout(new GridBagLayout());
        GridBagConstraints position = new GridBagConstraints();
        position.insets = new Insets(10, 0, 10, 0);

        position.gridy = 0;
        buttonpanel.add(selected, position);

        position.gridy = 1;
        buttonpanel.add(turn,position);

        reset.addActionListener(listener);
        reset.setPreferredSize(new Dimension(150, 40));

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

        buttonpanel.setPreferredSize(new Dimension(150, 700));

        add(boardpanel, BorderLayout.WEST);
        add(buttonpanel, BorderLayout.EAST);
        firstTurnFlag = true;
    }

    private void setBackGroundColor(int r, int c) {
        if ((c % 2 == 1 && r % 2 == 0) || (c % 2 == 0 && r % 2 == 1)) {
            board[r][c].setBackground(Color.LIGHT_GRAY);
        } else if ((c % 2 == 0 && r % 2 == 0) || (c % 2 == 1 && r % 2 == 1)) {
            board[r][c].setBackground(Color.WHITE);
        }
    }

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

    // method that updates the board
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

    // inner class that represents action listener for buttons
    private class listener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            for (int r = 0; r < model.numRows(); r++) {
                for (int c = 0; c < model.numColumns(); c++) {
                    if (board[r][c] == event.getSource()) {
                        if (firstTurnFlag == true) {
                            fromRow = r;
                            fromCol = c;
                            firstTurnFlag = false;
                            if (model.pieceAt(r, c) != null)
                                selected.setText("Selected");
                            else
                                selected.setText("Selected Empty");
                        } else {

                            turn.setText(model.currentPlayer().toString() + "'S TURN");
                            toRow = r;
                            toCol = c;
                            firstTurnFlag = true;
                            Move m = new Move(fromRow, fromCol, toRow, toCol);
                            if ((model.isValidMove(m)) == true) {
                                //Saving before moving so that if there is
                                //a piece in the 'TO' spot, we can save it's
                                //type before it disappears
                                model.saveMove(fromRow, fromCol, toRow, toCol);
                                model.move(m);

                                if (model.pawnInEndzone()) {
                                    String[] options = {"Queen", "Bishop", "Rook", "Knight"};
                                    int type = JOptionPane.showOptionDialog(null,
                                            "What do you want your pawn to transform into?",
                                            "Pawn transformation", JOptionPane.YES_NO_CANCEL_OPTION,
                                            JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

                                    model.pawnTransform(options[type]);
                                }

                                model.setNextPlayer();

                                if (model.inCheck(model.currentPlayer()))
                                    JOptionPane.showMessageDialog(null, model.currentPlayer() + " is in Check");

                                if (AI.isSelected()) {
                                    model.AI();

                                    if (model.pawnInEndzone()) {
                                        model.pawnTransform("Queen");
                                    }

                                        model.setNextPlayer();

                                        if (model.inCheck(model.currentPlayer()))
                                            JOptionPane.showMessageDialog(null, model.currentPlayer() + " is in Check");

                                } else {

                                    turn.setText(model.currentPlayer().toString() + "'S TURN");

                                }
                                selected.setText("Not selected");
                                displayBoard();
                            }
                        }
                    }
                }
            }
            if(reset == event.getSource()){
                model = new ChessModel();
                displayBoard();
            }


            if (undoBtn == event.getSource()) {
                model.undoMove();
                displayBoard();
            }

            if (castleRight == event.getSource()){
                if(model.canCastleRight(model.currentPlayer())) {
                    model.castleRight(model.currentPlayer());
                    displayBoard();
                    model.setNextPlayer();
                    if (AI.isSelected()) {
                        model.AI();
                        displayBoard();
                        model.setNextPlayer();
                    }
                }
            }

            if (castleLeft == event.getSource()){
                if(model.canCastleLeft(model.currentPlayer())) {
                    model.castleLeft(model.currentPlayer());
                    displayBoard();
                    model.setNextPlayer();
                    if (AI.isSelected()) {
                        model.AI();
                        displayBoard();
                        model.setNextPlayer();
                    }
                }
            }

            if (AI.isSelected() && model.isComplete()){

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
