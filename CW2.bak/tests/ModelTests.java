import draughts.*;

import java.awt.*;
import java.util.*;
import org.junit.*;
import static org.junit.Assert.*;

public class ModelTests {

    private class TestPlayer implements Player {

        @Override
        public Move notify(Set<Move> moves) {
            if (moves.iterator().hasNext()) return moves.iterator().next();
            return null;
        }

    }

    public class TestModel extends DraughtsModel {

        public TestModel(String gameName, Player player, Colour currentPlayer, Set<Piece> pieces) {
            super(gameName, player, currentPlayer, pieces);
        }

        public TestModel(String gameName, Player player) {
            super(gameName, player);
        }

        public boolean removePieceInModel(Point position, Point destination) {
            return removePiece(position, destination);
        }

        public void turnInModel() {
            turn();
        }
        
        public void playInModel(Move move) {
            play(move);
        }
    }

    @Test
    public void testGameNameIsCorrect() throws Exception {
        DraughtsModel model = new DraughtsModel("Test", null);

        assertEquals("The game name should be the same as the one passed in", "Test", model.getGameName());
    }

    @Test
    public void testCurrentPlayerIsRedAtStartOfGame() throws Exception {
        DraughtsModel model = new DraughtsModel("Game", null);

        assertEquals("The red player should be the current player at the beginning of the game.", Colour.Red, model.getCurrentPlayer());
    }

    @Test
    public void testCurrentPlayerUpdatesCorrectly() throws Exception {
        TestModel model = new TestModel("Test", new TestPlayer());

        assertEquals("The current player should be red initially", Colour.Red, model.getCurrentPlayer());

        model.turnInModel();
        assertEquals("The current player should be white after one turn", Colour.White, model.getCurrentPlayer());

        model.turnInModel();
        assertEquals("The current player should be red after two turns", Colour.Red, model.getCurrentPlayer());
    }

    @Test
    public void testCorrectPieceIsReturned() {
        Set<Piece> pieces = new HashSet<Piece>();
        Piece piece = new Piece(Colour.Red, 3, 5);
        pieces.add(piece);

        DraughtsModel model = new DraughtsModel("Test", null, Colour.Red, pieces);

        assertEquals("The correct piece should be returned", piece, model.getPiece(3, 5));
        assertEquals("A null piece should be returned", null, model.getPiece(4, 5));
    }

    @Test
    public void testCorrectPieceRemoved() {
        Set<Piece> pieces = new HashSet<Piece>();
        Piece piece = new Piece(Colour.Red, 4, 6);
        pieces.add(piece);
        
        Point p1 = new Point(3,5);
        Point p2 = new Point(4,6);
        Point p3 = new Point(5,7);

        TestModel model = new TestModel("Test", new TestPlayer(), Colour.Red, pieces);

        assertEquals("A piece has not been jumped", false, model.removePieceInModel(p1, p2));
        assertEquals("A piece has been jumped", true, model.removePieceInModel(p1, p3));
    }

    @Test
    public void testPieceMovedCorrectly() {
        Set<Piece> pieces = new HashSet<Piece>();
        Piece p1 = new Piece(Colour.Red, 3, 5);
        pieces.add(p1);

        TestModel model = new TestModel("Test", new TestPlayer(), Colour.Red, pieces);

        Move move1 = new Move(p1, 4, 6);
        
        //model.play(move1);
        model.playInModel(move1);

        Piece normal_move_p1 = model.getPiece(4, 6);
        Piece non_move_p1 = model.getPiece(3, 5);
        
        assertEquals("Piece has been moved from original position correctly", non_move_p1, null);
        assertEquals("Piece moved single cell correctly", normal_move_p1, p1);

        Move move2 = new Move(p1, 5, 7);

        //model.play(move2);
        model.playInModel(move2);

        Piece jump_move_p1 = model.getPiece(5, 7); //i think
        assertEquals("Piece jump moved correctly", jump_move_p1, p1);
    }

    @Test
    public void testJumpRemovePiece() {
        Set<Piece> pieces = new HashSet<Piece>();
        Piece p1 = new Piece(Colour.Red, 3, 5);
        Piece p2 = new Piece(Colour.Red, 4, 6);
        pieces.add(p1);
        pieces.add(p2);

        TestModel model = new TestModel("Test", new TestPlayer(), Colour.Red, pieces);

        Move move = new Move(p1, 5, 7);

        model.playInModel(move);

        Piece removed_peice = model.getPiece(4, 6);
        assertEquals("Piece removed after jump", removed_peice, null);
    }

    @Test
    public void testPiecesInitilaisedCorrectly() {
        TestModel model = new TestModel("Test", new TestPlayer());

        Point[] white_points = {
            new Point(1, 0),
            new Point(3, 0),
            new Point(5, 0),
            new Point(7, 0),
            new Point(0, 1),
            new Point(2, 1),
            new Point(4, 1),
            new Point(6, 1),
            new Point(1, 2),
            new Point(3, 2),
            new Point(5, 2),
            new Point(7, 2)
        };

        for (Point point: white_points) {
            Piece piece = model.getPiece((int) point.getX(),(int) point.getY());
            assertNotNull(piece);
            assertEquals("Piece is correct colour", piece.getColour(), Colour.White);
        }

        Point[] red_points = {
            new Point(0, 5),
            new Point(2, 5),
            new Point(4, 5),
            new Point(6, 5),
            new Point(1, 6),
            new Point(3, 6),
            new Point(5, 6),
            new Point(7, 6),
            new Point(0, 7),
            new Point(2, 7),
            new Point(4, 7),
            new Point(6, 7)
        };

        for (Point point: red_points) {
            Piece piece = model.getPiece((int) point.getX(), (int) point.getY());
            assertNotNull(piece);
            assertEquals("Piece is correct colour", piece.getColour(), Colour.Red);
        }
    }
}
