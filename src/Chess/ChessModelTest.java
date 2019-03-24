package Chess;

import org.junit.Assert;
import org.junit.Test;

public class ChessModelTest {

    @Test
    public void testWhitePawn() {
        ChessModel game = new ChessModel();
        Move m = new Move(6,2,5,2);
        Assert.assertTrue(game.isValidMove(m));
    }

    @Test
    public void testWhiteKnight() {
        ChessModel game = new ChessModel();
       Move m = new Move(7,1,5,2);
       Assert.assertTrue(game.isValidMove(m));
    }

    @Test
    public void testWhiteRook() {
        ChessModel game = new ChessModel();
        Move m = new Move(6,0,4,0);
        game.move(m);
        Move m2 = new Move(7,0,5,0);
        Assert.assertTrue(game.isValidMove(m2));
    }

    @Test
    public void testWhiteBishop() {
        ChessModel game = new ChessModel();
        Move m = new Move(6,3,5,3);
        game.move(m);
        Move m2 = new Move(7,2,3,6);
        Assert.assertTrue(game.isValidMove(m2));
    }

    @Test
    public void testWhiteQueen() {
        ChessModel game = new ChessModel();
        Move m = new Move(6,2,5,2);
        game.move(m);
        Move m2 = new Move(7,3,4,0);
        Assert.assertTrue(game.isValidMove(m2));
    }

    @Test
    public void testWhiteKing() {
        ChessModel game = new ChessModel();
        Move m = new Move(6,3,5,3);
        game.move(m);
        Move m2 = new Move(7,4,6,3);
        Assert.assertTrue(game.isValidMove(m2));
    }

    @Test
    public void testCastling() {
        ChessModel game = new ChessModel();
        Move m = new Move(6,4,3,4);
        Move m2 = new Move(7,6,5,7);
        Move m3 = new Move(7,5,5,3);
        game.move(m);
        game.move(m2);
        game.move(m3);
        Assert.assertTrue(game.canCastleRight(Player.WHITE));
        game.castleRight(Player.WHITE);
    }

    @Test
    public void testBlackPawn() {
        ChessPanel game = new ChessPanel();
        game.twoPlayer.setSelected(true);
        game.model.move(new Move(6,4,5,4));
        game.model.setNextPlayer();
        Move m2 = new Move(1,4,2,4);
        Assert.assertTrue(game.model.isValidMove(m2));
    }

    @Test
    public void testBlackKnight() {
        ChessPanel game = new ChessPanel();
        game.twoPlayer.setSelected(true);
        game.model.move(new Move(6,0,4,0));
        game.model.setNextPlayer();
        Move m2 = new Move(0,1,2,0);
        Assert.assertTrue(game.model.isValidMove(m2));
    }

    @Test
    public void testBlackBishop() {
        ChessPanel game = new ChessPanel();
        game.twoPlayer.setSelected(true);

        //white pawn move
        game.model.move(new Move(6,0,4,0));
        game.model.setNextPlayer();

        // black pawn move
        game.model.move(new Move(1,3,3,3));
        game.model.setNextPlayer();

        // white pawn move
        game.model.move(new Move (4,0,3,0));
        game.model.setNextPlayer();

        // black bishop move
        Move m = new Move(0,2,3,5);
        Assert.assertTrue(game.model.isValidMove(m));
    }

    @Test
    public void testBlackRook() {
        ChessPanel game = new ChessPanel();
        game.twoPlayer.setSelected(true);
        game.model.move(new Move(6,0,4,0));
        game.model.setNextPlayer();

        game.model.move(new Move(1,0,3,0));
        game.model.setNextPlayer();

        game.model.move(new Move(4,0,3,0));
        game.model.setNextPlayer();

        Move m = new Move(0,0,2,0);
        Assert.assertTrue(game.model.isValidMove(m));
    }

    @Test
    public void testBlackQueen() {
        ChessPanel game = new ChessPanel();
        //white move
        game.model.move(new Move(6,0,4,0));
        game.model.setNextPlayer();

        //black move
        game.model.move(new Move(1,2,2,2));
        game.model.setNextPlayer();

        //white move
        game.model.move(new Move(4,0,3,0));
        game.model.setNextPlayer();

        Move m = new Move(0,3,3,0);
        Assert.assertTrue(game.model.isValidMove(m));
    }

    @Test
    public void testBlackKing() {
        ChessPanel game = new ChessPanel();

        //white move
        game.model.move(new Move(6,0,4,0));
        game.model.setNextPlayer();

        //black move
        game.model.move(new Move(1,3,2,3));
        game.model.setNextPlayer();

        //white move
        game.model.move(new Move(4,0,3,0));
        game.model.setNextPlayer();

        Move m = new Move(0,4,1,3);
        Assert.assertTrue(game.model.isValidMove(m));
    }
}
