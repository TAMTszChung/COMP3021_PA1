package castle.comp3021.assignment;

import castle.comp3021.assignment.mock.MockPiece;
import castle.comp3021.assignment.mock.MockPlayer;
import castle.comp3021.assignment.piece.Archer;
import castle.comp3021.assignment.piece.Knight;
import castle.comp3021.assignment.player.ConsolePlayer;
import castle.comp3021.assignment.player.RandomPlayer;
import castle.comp3021.assignment.protocol.*;
import castle.comp3021.assignment.protocol.exception.InvalidConfigurationError;
import castle.comp3021.assignment.util.Compares;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Put your additional JUnit5 tests for Bonus Task 2 in this class.
 */
public class AdditionalTests {

    @Test
    public void createGame0(){
        var game = Main.createGame(25, 0);
        for (int i = 0; i < game.getConfiguration().getSize(); i++) {
            for (int j = 0; j < game.getConfiguration().getSize(); j++) {
                var piece = game.getPiece(i,j);
                // piece must be initially on the first and last row of gameboard
                if (j == 0 || j == game.getConfiguration().getSize() - 1) {
                    assertNotNull(piece);
                } else {
                    assertNull(piece);
                }
            }
        }
    }

    @Test
    public void createGame1(){
        boolean throwed = false;
        try{
            Main.createGame(8, 0);
        }catch(InvalidConfigurationError e){
            throwed = true;
        }

        Assertions.assertTrue(throwed);
    }

    @Test
    public void createGame2(){
        boolean throwed = false;
        try{
            Main.createGame(2, 0);
        }catch(InvalidConfigurationError e){
            throwed = true;
        }

        Assertions.assertTrue(throwed);
    }

    @Test
    public void createGame3(){
        boolean throwed = false;
        try{
            Main.createGame(29, 0);
        }catch(InvalidConfigurationError e){
            throwed = true;
        }

        Assertions.assertTrue(throwed);
    }

    @Test
    public void createGame4(){
        boolean throwed = false;
        try{
            Main.createGame(9, -5);
        }catch(InvalidConfigurationError e){
            throwed = true;
        }

        Assertions.assertTrue(throwed);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "5",
            "test, 5",
            "5, test",
            "test,test"
    })
    public void testmain0(String teststring){
        String[] args = teststring.split(", ");
        Assertions.assertThrows(IllegalArgumentException.class, () -> Main.main(args));
    }

    @Test
    public void testconfig0(){
        boolean throwed = false;
        try{
            var player1 = new MockPlayer(Color.PURPLE);
            var player2 = new MockPlayer(Color.YELLOW);
            var config = new Configuration(7, new Player[]{player1, player2},5);
            var archer0 = new Archer(player2);
            config.addInitialPiece(archer0,3,3);
        }catch(InvalidConfigurationError e){
            throwed = true;
        }

        Assertions.assertTrue(throwed);
    }

    @Test
    public void testplayer0(){
        var player1 = new MockPlayer(Color.PURPLE);
        var sameplayer = player1.equals(null);

        Assertions.assertFalse(sameplayer);
    }

    @Test
    public void testplayer1(){
        var player1 = new MockPlayer(Color.PURPLE);
        var player2 = new ConsolePlayer("White");
        var sameplayer = player1.equals(player2);

        Assertions.assertFalse(sameplayer);
    }

    @Test
    public void testmove0(){
        var move = new Move(0,0,1,2);
        var samemove = move.equals(null);

        Assertions.assertFalse(samemove);
    }

    @Test
    public void testmove1(){
        var move = new Move(0,0,1,2);
        var player2 = new ConsolePlayer("White");
        var samemove = move.equals(player2);

        Assertions.assertFalse(samemove);
    }

    @Test
    public void testmove2(){
        var move = new Move(0,0,1,2);
        var move2 = new Move(0,0,3,2);
        var samemove = move.equals(move2);

        Assertions.assertFalse(samemove);
    }

    @Test
    public void getwinnertest0() {
        var player1 = new MockPlayer(Color.PURPLE);
        var player2 = new MockPlayer(Color.YELLOW);
        var config = new Configuration(13, new Player[]{player1, player2});

        var knight1 = new Knight(player1);

        config.addInitialPiece(knight1, 0, 0);
        player1.setNextMoves(new Move[]{
                new Move(0, 0, 1, 2),
        });
        var game = new JesonMor(config);
        var winner = game.start();

        assertEquals(player1, winner);
    }

    @Test
    public void getwinnertest1() {
        var player1 = new MockPlayer(Color.PURPLE);
        var player2 = new MockPlayer(Color.YELLOW);
        var config = new Configuration(7, new Player[]{player2, player1},1);

        var knight1 = new Knight(player1);
        var knight2 = new Knight(player2);

        config.addInitialPiece(knight1, 4, 0);
        config.addInitialPiece(knight2, 0, 6);
        player1.setNextMoves(new Move[]{
                new Move(4, 0, 5, 2),
                new Move(5, 2, 3, 3),
                new Move(3, 3, 5, 2)
        });

        player2.setNextMoves(new Move[]{
                new Move(0, 6, 1, 4),
                new Move(1, 4, 0, 6),
                new Move(0, 6, 1, 4)
        });
        var game = new JesonMor(config);
        var beforestartCurrentP = game.getCurrentPlayer();
        var winner = game.start();
        var afterstartCurrentP =game.getCurrentPlayer();

        assertEquals(player2, beforestartCurrentP);
        assertEquals(player1, afterstartCurrentP);
        assertEquals(player1, winner);
    }

    @Test
    public void knightGetAvailableMoves0() {
        var player1 = new MockPlayer(Color.PURPLE);
        var player2 = new MockPlayer(Color.YELLOW);
        var config = new Configuration(13, new Player[]{player1, player2});

        var knight1 = new Knight(player1);
        var knight2 = new Knight(player2);
        var knight3 = new Knight(player2);
        var knight4 = new Knight(player2);
        var knight5 = new Knight(player2);

        config.addInitialPiece(knight1, 3, 3);
        config.addInitialPiece(knight2, 4, 3);
        config.addInitialPiece(knight3, 2, 3);
        config.addInitialPiece(knight4, 3, 2);
        config.addInitialPiece(knight5, 3, 4);

        var game = new JesonMor(config);
        var moves = knight1.getAvailableMoves(game, new Place(3, 3));
        var expectedMoves = new Move[]{};
        Assertions.assertTrue(Compares.areContentsEqual(moves, expectedMoves));
    }

    @Test
    public void knightGetAvailableMoves1() {
        var player1 = new MockPlayer(Color.PURPLE);
        var player2 = new MockPlayer(Color.YELLOW);
        var config = new Configuration(13, new Player[]{player1, player2});

        var knight1 = new Knight(player1);
        var knight2 = new Knight(player2);
        var knight3 = new Knight(player2);
        var knight4 = new Knight(player2);
        var knight5 = new Knight(player2);

        config.addInitialPiece(knight1, 3, 3);
        config.addInitialPiece(knight2, 4, 3);
        config.addInitialPiece(knight3, 2, 3);
        config.addInitialPiece(knight4, 3, 2);
        config.addInitialPiece(knight5, 2, 5);

        var game = new JesonMor(config);
        var moves = knight1.getAvailableMoves(game, new Place(3, 3));
        var expectedMoves = new Move[]{new Move(3,3,4,5)
                ,new Move(3,3,2,5)};
        Assertions.assertTrue(Compares.areContentsEqual(moves, expectedMoves));
    }

    @Test
    public void knightGetAvailableMoves2() {
        var player1 = new MockPlayer(Color.PURPLE);
        var player2 = new MockPlayer(Color.YELLOW);
        var config = new Configuration(13, new Player[]{player1, player2},5);

        var knight1 = new Knight(player1);
        var knight2 = new Knight(player2);
        var knight3 = new Knight(player2);
        var knight4 = new Knight(player2);
        var knight5 = new Knight(player2);

        config.addInitialPiece(knight1, 3, 3);
        config.addInitialPiece(knight2, 4, 3);
        config.addInitialPiece(knight3, 2, 3);
        config.addInitialPiece(knight4, 3, 2);
        config.addInitialPiece(knight5, 2, 5);

        var game = new JesonMor(config);
        var moves = knight1.getAvailableMoves(game, new Place(3, 3));
        var expectedMoves = new Move[]{new Move(3,3,4,5)};
        Assertions.assertTrue(Compares.areContentsEqual(moves, expectedMoves));
    }

    @Test
    public void knightGetAvailableMoves3() {
        var player1 = new MockPlayer(Color.PURPLE);
        var player2 = new MockPlayer(Color.YELLOW);
        var config = new Configuration(13, new Player[]{player1, player2},5);

        var knight1 = new Knight(player1);

        config.addInitialPiece(knight1, 3, 3);

        var game = new JesonMor(config);
        var moves = knight1.getAvailableMoves(game, new Place(4, 3));
        var expectedMoves = new Move[]{};
        Assertions.assertTrue(Compares.areContentsEqual(moves, expectedMoves));
    }

    @Test
    public void knightGetAvailableMoves4() {
        var player1 = new MockPlayer(Color.PURPLE);
        var player2 = new MockPlayer(Color.YELLOW);
        var config = new Configuration(13, new Player[]{player1, player2},5);

        var knight1 = new Knight(player1);


        config.addInitialPiece(knight1, 3, 3);

        var game = new JesonMor(config);
        var moves = knight1.getAvailableMoves(game, new Place(-1, 3));
        var expectedMoves = new Move[]{};
        Assertions.assertTrue(Compares.areContentsEqual(moves, expectedMoves));
    }
    @Test
    public void knightGetAvailableMoves5() {
        var player1 = new MockPlayer(Color.PURPLE);
        var player2 = new MockPlayer(Color.YELLOW);
        var config = new Configuration(13, new Player[]{player1, player2},5);

        var knight1 = new Knight(player1);


        config.addInitialPiece(knight1, 3, 3);

        var game = new JesonMor(config);
        var moves = knight1.getAvailableMoves(game, new Place(3, -1));
        var expectedMoves = new Move[]{};
        Assertions.assertTrue(Compares.areContentsEqual(moves, expectedMoves));
    }

    @Test
    public void knightGetAvailableMoves6() {
        var player1 = new MockPlayer(Color.PURPLE);
        var player2 = new MockPlayer(Color.YELLOW);
        var config = new Configuration(13, new Player[]{player1, player2},5);

        var knight1 = new Knight(player1);


        config.addInitialPiece(knight1, 3, 3);

        var game = new JesonMor(config);
        var moves = knight1.getAvailableMoves(game, new Place(-1, -1));
        var expectedMoves = new Move[]{};
        Assertions.assertTrue(Compares.areContentsEqual(moves, expectedMoves));
    }

    @Test
    public void knightGetAvailableMoves7() {
        var player1 = new MockPlayer(Color.PURPLE);
        var player2 = new MockPlayer(Color.YELLOW);
        var config = new Configuration(13, new Player[]{player1, player2},5);

        var knight1 = new Knight(player1);


        config.addInitialPiece(knight1, 3, 3);

        var game = new JesonMor(config);
        var moves = knight1.getAvailableMoves(game, new Place(21, 3));
        var expectedMoves = new Move[]{};
        Assertions.assertTrue(Compares.areContentsEqual(moves, expectedMoves));
    }

    @Test
    public void knightGetAvailableMoves8() {
        var player1 = new MockPlayer(Color.PURPLE);
        var player2 = new MockPlayer(Color.YELLOW);
        var config = new Configuration(13, new Player[]{player1, player2},5);

        var knight1 = new Knight(player1);


        config.addInitialPiece(knight1, 3, 3);

        var game = new JesonMor(config);
        var moves = knight1.getAvailableMoves(game, new Place(3, 22));
        var expectedMoves = new Move[]{};
        Assertions.assertTrue(Compares.areContentsEqual(moves, expectedMoves));
    }

    @Test
    public void knightGetAvailableMoves9() {
        var player1 = new MockPlayer(Color.PURPLE);
        var player2 = new MockPlayer(Color.YELLOW);
        var config = new Configuration(13, new Player[]{player1, player2},5);

        var knight1 = new Knight(player1);


        config.addInitialPiece(knight1, 3, 3);

        var moves = knight1.getAvailableMoves(null, new Place(3, 22));
        var expectedMoves = new Move[]{};
        Assertions.assertTrue(Compares.areContentsEqual(moves, expectedMoves));
    }

    @Test
    public void knightGetAvailableMoves10() {
        var player1 = new MockPlayer(Color.PURPLE);
        var player2 = new MockPlayer(Color.YELLOW);
        var config = new Configuration(13, new Player[]{player1, player2},5);

        var knight1 = new Knight(player1);


        config.addInitialPiece(knight1, 3, 3);

        var game = new JesonMor(config);
        var moves = knight1.getAvailableMoves(game, null);
        var expectedMoves = new Move[]{};
        Assertions.assertTrue(Compares.areContentsEqual(moves, expectedMoves));
    }

    @Test
    public void knightcheckvalidmethodtest0() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        var player1 = new MockPlayer(Color.PURPLE);
        var player2 = new MockPlayer(Color.YELLOW);
        var config = new Configuration(13, new Player[]{player1, player2},5);
        var knight1 = new Knight(player1);
        config.addInitialPiece(knight1, 0, 0);
        var knight2 = new Knight(player1);
        config.addInitialPiece(knight2, 3, 3);
        var game = new JesonMor(config);

        Method checkvalidmethod = Knight.class.getDeclaredMethod("checkMoveValidity", Game.class, Move.class);
        checkvalidmethod.setAccessible(true);
        var valid1 = (boolean) checkvalidmethod.invoke(knight1, null, new Move(0,0,1,2));
        var valid2 = (boolean) checkvalidmethod.invoke(knight1, game, null);
        //violate move rule
        var valid3 = (boolean) checkvalidmethod.invoke(knight1, game, new Move(0,0,0,1));
        //not self
        var valid4 = (boolean) checkvalidmethod.invoke(knight2, game, new Move(0,0,1,2));
        //same position
        var valid5 = (boolean) checkvalidmethod.invoke(knight1, game, new Move(0,0,0,0));

        //out of bound
        var valid7 = (boolean) checkvalidmethod.invoke(knight1, game, new Move(-1,0,0,0));
        var valid8 = (boolean) checkvalidmethod.invoke(knight1, game, new Move(0,-1,0,0));
        var valid9 = (boolean) checkvalidmethod.invoke(knight1, game, new Move(0,0,-1,0));
        var valid10 = (boolean) checkvalidmethod.invoke(knight1, game, new Move(0,0,0,-1));
        var valid11 = (boolean) checkvalidmethod.invoke(knight1, game, new Move(21,0,0,0));
        var valid12 = (boolean) checkvalidmethod.invoke(knight1, game, new Move(0,21,0,0));
        var valid13 = (boolean) checkvalidmethod.invoke(knight1, game, new Move(0,0,21,0));
        var valid14 = (boolean) checkvalidmethod.invoke(knight1, game, new Move(0,0,0,21));

        Assertions.assertFalse(valid1);
        Assertions.assertFalse(valid2);
        Assertions.assertFalse(valid3);
        Assertions.assertFalse(valid4);
        Assertions.assertFalse(valid5);
        Assertions.assertFalse(valid7);
        Assertions.assertFalse(valid8);
        Assertions.assertFalse(valid9);
        Assertions.assertFalse(valid10);
        Assertions.assertFalse(valid11);
        Assertions.assertFalse(valid12);
        Assertions.assertFalse(valid13);
        Assertions.assertFalse(valid14);
    }

    @Test
    public void archercheckvalidmethodtest0() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        var player1 = new MockPlayer(Color.PURPLE);
        var player2 = new MockPlayer(Color.YELLOW);
        var config = new Configuration(13, new Player[]{player1, player2},5);
        var archer1 = new Archer(player1);
        config.addInitialPiece(archer1, 0, 0);
        var archer2 = new Archer(player1);
        config.addInitialPiece(archer2, 3, 3);
        var game = new JesonMor(config);

        Method checkvalidmethod = Archer.class.getDeclaredMethod("checkMoveValidity", Game.class, Move.class);
        checkvalidmethod.setAccessible(true);
        var valid1 = (boolean) checkvalidmethod.invoke(archer1, null, new Move(0,0,0,2));
        var valid2 = (boolean) checkvalidmethod.invoke(archer1, game, null);
        //violate move rule
        var valid3 = (boolean) checkvalidmethod.invoke(archer1, game, new Move(0,0,1,1));
        //not self
        var valid4 = (boolean) checkvalidmethod.invoke(archer2, game, new Move(0,0,1,2));
        //same position
        var valid5 = (boolean) checkvalidmethod.invoke(archer1, game, new Move(0,0,0,0));
        //no piece
        var valid6 = (boolean) checkvalidmethod.invoke(archer1, game, new Move(1,0,0,0));
        //out of bound
        var valid7 = (boolean) checkvalidmethod.invoke(archer1, game, new Move(-1,0,0,0));
        var valid8 = (boolean) checkvalidmethod.invoke(archer1, game, new Move(0,-1,0,0));
        var valid9 = (boolean) checkvalidmethod.invoke(archer1, game, new Move(0,0,-1,0));
        var valid10 = (boolean) checkvalidmethod.invoke(archer1, game, new Move(0,0,0,-1));
        var valid11 = (boolean) checkvalidmethod.invoke(archer1, game, new Move(21,0,0,0));
        var valid12 = (boolean) checkvalidmethod.invoke(archer1, game, new Move(0,21,0,0));
        var valid13 = (boolean) checkvalidmethod.invoke(archer1, game, new Move(0,0,21,0));
        var valid14 = (boolean) checkvalidmethod.invoke(archer1, game, new Move(0,0,0,21));

        Assertions.assertFalse(valid1);
        Assertions.assertFalse(valid2);
        Assertions.assertFalse(valid3);
        Assertions.assertFalse(valid4);
        Assertions.assertFalse(valid5);
        Assertions.assertFalse(valid6);
        Assertions.assertFalse(valid7);
        Assertions.assertFalse(valid8);
        Assertions.assertFalse(valid9);
        Assertions.assertFalse(valid10);
        Assertions.assertFalse(valid11);
        Assertions.assertFalse(valid12);
        Assertions.assertFalse(valid13);
        Assertions.assertFalse(valid14);
    }

    @Test
    public void archerGetAvailableMoves0() {
        var player1 = new MockPlayer(Color.PURPLE);
        var player2 = new MockPlayer(Color.YELLOW);
        var config = new Configuration(7, new Player[]{player1, player2},5);

        var archer0 = new Archer(player1);
        config.addInitialPiece(archer0,2,1);
        var archer1 = new Archer(player1);
        config.addInitialPiece(archer1,2,3);
        var archer2 = new Archer(player2);
        config.addInitialPiece(archer2,4,3);
        var knight0 = new Knight(player1);
        config.addInitialPiece(knight0,5,3);
        var archer3 = new Archer(player1);
        config.addInitialPiece(archer3,2,4);

        var game = new JesonMor(config);
        var moves = archer1.getAvailableMoves(game, new Place(2, 3));
        var expectedMoves = new Move[]
                {new Move(2,3,1,3),
                        new Move(2,3,0,3),
                        new Move(2,3,3,3),
                        new Move(2,3,2,2)
                };
        Assertions.assertTrue(Compares.areContentsEqual(moves, expectedMoves));
    }

    @Test
    public void archerGetAvailableMoves1() {
        var player1 = new MockPlayer(Color.PURPLE);
        var player2 = new MockPlayer(Color.YELLOW);
        var config = new Configuration(7, new Player[]{player1, player2},0);

        var archer0 = new Archer(player1);
        config.addInitialPiece(archer0,2,1);
        var archer1 = new Archer(player1);
        config.addInitialPiece(archer1,2,3);
        var archer2 = new Archer(player2);
        config.addInitialPiece(archer2,4,3);
        var knight0 = new Knight(player2);
        config.addInitialPiece(knight0,5,3);
        var archer3 = new Archer(player1);
        config.addInitialPiece(archer3,2,4);

        var game = new JesonMor(config);
        var moves = archer1.getAvailableMoves(game, new Place(2, 3));
        var expectedMoves = new Move[]
                {new Move(2,3,1,3),
                        new Move(2,3,0,3),
                        new Move(2,3,3,3),
                        new Move(2,3,2,2),
                        new Move(2,3,5,3)
                };
        Assertions.assertTrue(Compares.areContentsEqual(moves, expectedMoves));
    }

    @Test
    public void archerGetAvailableMoves2() {
        var player1 = new MockPlayer(Color.PURPLE);
        var player2 = new MockPlayer(Color.YELLOW);
        var config = new Configuration(7, new Player[]{player1, player2},0);

        var knight0 = new Knight(player2);
        config.addInitialPiece(knight0,2,0);
        var archer0 = new Archer(player1);
        config.addInitialPiece(archer0,2,1);
        var knight1 = new Knight(player2);
        config.addInitialPiece(knight1,2,2);
        var archer1 = new Archer(player1);
        config.addInitialPiece(archer1,2,3);
        var archer2 = new Archer(player2);
        config.addInitialPiece(archer2,4,3);
        var knight2 = new Knight(player2);
        config.addInitialPiece(knight2,5,3);
        var archer3 = new Archer(player1);
        config.addInitialPiece(archer3,2,4);
        var knight3 = new Knight(player2);
        config.addInitialPiece(knight3,2,5);
        var knight4 = new Knight(player2);
        config.addInitialPiece(knight4,2,6);

        var game = new JesonMor(config);
        var moves = archer1.getAvailableMoves(game, new Place(2, 3));
        var expectedMoves = new Move[]
                {new Move(2,3,1,3),
                        new Move(2,3,0,3),
                        new Move(2,3,3,3),
                        new Move(2,3,2,5),
                        new Move(2,3,5,3)
                };
        Assertions.assertTrue(Compares.areContentsEqual(moves, expectedMoves));
    }

    @Test
    public void archerGetAvailableMoves3() {
        var player1 = new MockPlayer(Color.PURPLE);
        var player2 = new MockPlayer(Color.YELLOW);
        var config = new Configuration(7, new Player[]{player1, player2},5);

        var knight0 = new Knight(player2);
        config.addInitialPiece(knight0,2,0);
        var archer0 = new Archer(player1);
        config.addInitialPiece(archer0,2,1);
        var knight1 = new Knight(player2);
        config.addInitialPiece(knight1,2,2);
        var archer1 = new Archer(player1);
        config.addInitialPiece(archer1,2,3);
        var archer2 = new Archer(player2);
        config.addInitialPiece(archer2,4,3);
        var knight2 = new Knight(player2);
        config.addInitialPiece(knight2,5,3);
        var archer3 = new Archer(player1);
        config.addInitialPiece(archer3,2,4);
        var knight3 = new Knight(player2);
        config.addInitialPiece(knight3,2,5);
        var knight4 = new Knight(player2);
        config.addInitialPiece(knight4,2,6);

        var game = new JesonMor(config);
        var moves = archer1.getAvailableMoves(game, new Place(2, 3));
        var expectedMoves = new Move[]
                {new Move(2,3,1,3),
                        new Move(2,3,0,3),
                        new Move(2,3,3,3)
                };
        Assertions.assertTrue(Compares.areContentsEqual(moves, expectedMoves));
    }

    @Test
    public void archerGetAvailableMoves4() {
        var player1 = new MockPlayer(Color.PURPLE);
        var player2 = new MockPlayer(Color.YELLOW);
        var config = new Configuration(5, new Player[]{player1, player2},0);

        var archer0 = new Archer(player1);
        config.addInitialPiece(archer0,2,3);

        var game = new JesonMor(config);
        var moves = archer0.getAvailableMoves(game, new Place(2, 3));
        var expectedMoves = new Move[]
                {new Move(2,3,2,0),
                        new Move(2,3,2,1),
                        new Move(2,3,2,2),
                        new Move(2,3,2,4),
                        new Move(2,3,0,3),
                        new Move(2,3,1,3),
                        new Move(2,3,3,3),
                        new Move(2,3,4,3)
                };
        Assertions.assertTrue(Compares.areContentsEqual(moves, expectedMoves));
    }

    @Test
    public void archerGetAvailableMoves5() {
        var player1 = new MockPlayer(Color.PURPLE);
        var player2 = new MockPlayer(Color.YELLOW);
        var config = new Configuration(5, new Player[]{player1, player2},0);

        var knight0 = new Knight(player1);
        config.addInitialPiece(knight0,1,0);
        var knight1 = new Knight(player1);
        config.addInitialPiece(knight1,2,0);
        var knight2 = new Knight(player1);
        config.addInitialPiece(knight2,0,3);
        var archer0 = new Archer(player1);
        config.addInitialPiece(archer0,2,3);
        var knight3 = new Knight(player1);
        config.addInitialPiece(knight3,4,3);
        var knight4 = new Knight(player1);
        config.addInitialPiece(knight4,2,4);

        var game = new JesonMor(config);
        var moves = archer0.getAvailableMoves(game, new Place(2, 3));
        var expectedMoves = new Move[]
                {new Move(2,3,2,1),
                        new Move(2,3,2,2),
                        new Move(2,3,1,3),
                        new Move(2,3,3,3)
                };
        Assertions.assertTrue(Compares.areContentsEqual(moves, expectedMoves));
    }

    @Test
    public void archerGetAvailableMoves6() {
        var player1 = new MockPlayer(Color.PURPLE);
        var player2 = new MockPlayer(Color.YELLOW);
        var config = new Configuration(7, new Player[]{player1, player2},5);

        var archer0 = new Archer(player2);
        config.addInitialPiece(archer0,2,1);
        var archer1 = new Archer(player2);
        config.addInitialPiece(archer1,2,2);
        var archer2 = new Archer(player2);
        config.addInitialPiece(archer2,0,3);
        var archer3 = new Archer(player2);
        config.addInitialPiece(archer3,1,3);
        var archer4 = new Archer(player1);
        config.addInitialPiece(archer4,2,3);
        var archer5 = new Archer(player2);
        config.addInitialPiece(archer5,4,3);
        var archer6 = new Archer(player2);
        config.addInitialPiece(archer6,5,3);
        var archer7 = new Archer(player2);
        config.addInitialPiece(archer7,2,4);
        var archer8 = new Archer(player2);
        config.addInitialPiece(archer8,2,5);

        var game = new JesonMor(config);
        var moves = archer4.getAvailableMoves(game, new Place(2, 3));
        var expectedMoves = new Move[]
                {new Move(2,3,3,3)
                };
        Assertions.assertTrue(Compares.areContentsEqual(moves, expectedMoves));
    }

    @Test
    public void archerGetAvailableMoves7() {
        var player1 = new MockPlayer(Color.PURPLE);
        var player2 = new MockPlayer(Color.YELLOW);
        var config = new Configuration(7, new Player[]{player1, player2},0);

        var archer0 = new Archer(player2);
        config.addInitialPiece(archer0,2,1);
        var archer1 = new Archer(player2);
        config.addInitialPiece(archer1,2,2);
        var archer2 = new Archer(player2);
        config.addInitialPiece(archer2,0,3);
        var archer3 = new Archer(player2);
        config.addInitialPiece(archer3,1,3);
        var archer4 = new Archer(player1);
        config.addInitialPiece(archer4,2,3);
        var archer5 = new Archer(player2);
        config.addInitialPiece(archer5,4,3);
        var archer6 = new Archer(player2);
        config.addInitialPiece(archer6,5,3);
        var archer7 = new Archer(player2);
        config.addInitialPiece(archer7,2,4);
        var archer8 = new Archer(player2);
        config.addInitialPiece(archer8,2,5);

        var game = new JesonMor(config);
        var moves = archer4.getAvailableMoves(game, new Place(2, 3));
        var expectedMoves = new Move[]
                {new Move(2,3,3,3),
                        new Move(2,3,0,3),
                        new Move(2,3,5,3),
                        new Move(2,3,2,5),
                        new Move(2,3,2,1)
                };
        Assertions.assertTrue(Compares.areContentsEqual(moves, expectedMoves));
    }

    @Test
    public void archerGetAvailableMoves8() {
        var player1 = new MockPlayer(Color.PURPLE);
        var player2 = new MockPlayer(Color.YELLOW);
        var config = new Configuration(3, new Player[]{player1, player2},0);

        var archer0 = new Archer(player1);
        config.addInitialPiece(archer0,0,2);

        var game = new JesonMor(config);
        var moves = game.getAvailableMoves(player1);
        var expectedMoves = new Move[]
                {new Move(0,2,0,0),
                        new Move(0,2,0,1),
                        new Move(0,2,1,2),
                        new Move(0,2,2,2)
                };
        Assertions.assertTrue(Compares.areContentsEqual(moves, expectedMoves));
    }

    @Test
    public void archerGetAvailableMoves9() {
        var player1 = new MockPlayer(Color.PURPLE);
        var player2 = new MockPlayer(Color.YELLOW);
        var config = new Configuration(3, new Player[]{player1, player2},0);

        var archer0 = new Archer(player2);
        config.addInitialPiece(archer0,0,1);
        var archer1 = new Archer(player1);
        config.addInitialPiece(archer1,0,2);
        var archer2 = new Archer(player2);
        config.addInitialPiece(archer2,1,2);

        var game = new JesonMor(config);
        var moves = game.getAvailableMoves(player1);
        var expectedMoves = new Move[]
                {
                };
        Assertions.assertTrue(Compares.areContentsEqual(moves, expectedMoves));
    }

    @Test
    public void archerGetAvailableMoves10() {
        var player1 = new MockPlayer(Color.PURPLE);
        var player2 = new MockPlayer(Color.YELLOW);
        var config = new Configuration(3, new Player[]{player1, player2},0);

        var archer0 = new Archer(player1);
        config.addInitialPiece(archer0,0,2);
        var archer1 = new Archer(player1);
        config.addInitialPiece(archer0,2,2);
        var game = new JesonMor(config);
        var moves = archer1.getAvailableMoves(game,new Place(0,2));
        var expectedMoves = new Move[]
                {
                };
        Assertions.assertTrue(Compares.areContentsEqual(moves, expectedMoves));
    }

    @Test
    public void archerGetAvailableMoves11() {
        var player1 = new MockPlayer(Color.PURPLE);
        var player2 = new MockPlayer(Color.YELLOW);
        var config = new Configuration(3, new Player[]{player1, player2},0);

        var archer0 = new Archer(player1);
        config.addInitialPiece(archer0,0,2);
        var archer1 = new Archer(player1);
        config.addInitialPiece(archer0,2,2);
        var moves = archer1.getAvailableMoves(null,new Place(0,2));
        var expectedMoves = new Move[]
                {
                };
        Assertions.assertTrue(Compares.areContentsEqual(moves, expectedMoves));
    }

    @Test
    public void archerGetAvailableMoves12() {
        var player1 = new MockPlayer(Color.PURPLE);
        var player2 = new MockPlayer(Color.YELLOW);
        var config = new Configuration(3, new Player[]{player1, player2},0);

        var archer0 = new Archer(player1);
        config.addInitialPiece(archer0,0,2);
        var archer1 = new Archer(player1);
        config.addInitialPiece(archer0,2,2);
        var game = new JesonMor(config);
        var moves = archer1.getAvailableMoves(game,null);
        var expectedMoves = new Move[]
                {
                };
        Assertions.assertTrue(Compares.areContentsEqual(moves, expectedMoves));
    }

    @Test
    public void consolePlayernextmovetest() {
        String data = "aa1-> cc2\r\nc3->c2\r\n"; // correct
        InputStream stdin = System.in;
        try {
            System.setIn(new ByteArrayInputStream(data.getBytes()));
            var player1 = new MockPlayer(Color.PURPLE);
            var player2 = new ConsolePlayer("RandomPlayer");
            var config = new Configuration(3, new Player[]{player1, player2});
            var piece1 = new MockPiece(player1);
            var piece2 = new MockPiece(player2);
            config.addInitialPiece(piece1, 0, 0);
            config.addInitialPiece(piece2, 2, 2);
            var game = new JesonMor(config);
            var move = player2.nextMove(game, game.getAvailableMoves(player2));
            assertEquals(new Move(2, 2, 2, 1), move);
        } finally {
            System.setIn(stdin);
        }
    }

    @Test
    public void consolePlayercheckvalidmethodtest0() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        var player1 = new ConsolePlayer("White");
        var player2 = new ConsolePlayer("Black");
        var config = new Configuration(13, new Player[]{player1, player2},5);
        var knight1 = new Knight(player1);
        config.addInitialPiece(knight1, 0, 0);
        var knight2 = new Knight(player2);
        config.addInitialPiece(knight2, 2, 1);

        var knight3 = new Knight(player1);
        config.addInitialPiece(knight3, 10, 10);
        var knight4 = new Knight(player1);
        config.addInitialPiece(knight4, 9, 8);
        var game = new JesonMor(config);

        var availMoves = new Move[]
                {new Move(0,0,2,1),
                        new Move(10,10,9,8)
                };
        Method checkvalidmethod = ConsolePlayer.class.getDeclaredMethod("checkMoveValidity", Game.class, Move.class, Move[].class);
        checkvalidmethod.setAccessible(true);

        var valid1 = (boolean) checkvalidmethod.invoke(player1, null, new Move(0,0,1,2),availMoves);
        var valid2 = (boolean) checkvalidmethod.invoke(player1, game, null,availMoves);
        //violate move rule
        var valid3 = (boolean) checkvalidmethod.invoke(player1, game, new Move(0,0,0,1),availMoves);
        //not self
        var valid4 = (boolean) checkvalidmethod.invoke(player2, game, new Move(0,0,1,2),availMoves);
        //same position
        var valid5 = (boolean) checkvalidmethod.invoke(player1, game, new Move(0,0,0,0),availMoves);
        //not in array
        var valid6 = (boolean) checkvalidmethod.invoke(player1, game, new Move(0,0,1,2),availMoves);
        //out of bound
        var valid7 = (boolean) checkvalidmethod.invoke(player1, game, new Move(-1,0,0,0),availMoves);
        var valid8 = (boolean) checkvalidmethod.invoke(player1, game, new Move(0,-1,0,0),availMoves);
        var valid9 = (boolean) checkvalidmethod.invoke(player1, game, new Move(0,0,-1,0),availMoves);
        var valid10 = (boolean) checkvalidmethod.invoke(player1, game, new Move(0,0,0,-1),availMoves);
        var valid11 = (boolean) checkvalidmethod.invoke(player1, game, new Move(21,0,0,0),availMoves);
        var valid12 = (boolean) checkvalidmethod.invoke(player1, game, new Move(0,21,0,0),availMoves);
        var valid13 = (boolean) checkvalidmethod.invoke(player1, game, new Move(0,0,21,0),availMoves);
        var valid14 = (boolean) checkvalidmethod.invoke(player1, game, new Move(0,0,0,21),availMoves);
        //no piece at origin
        var valid15 = (boolean) checkvalidmethod.invoke(player1, game, new Move(1,1,0,0),availMoves);
        var valid16 = (boolean) checkvalidmethod.invoke(player1, game, new Move(0,0,2,1),availMoves);
        var valid17 = (boolean) checkvalidmethod.invoke(player1, game, new Move(10,10,9,8),availMoves);
        Assertions.assertFalse(valid1);
        Assertions.assertFalse(valid2);
        Assertions.assertFalse(valid3);
        Assertions.assertFalse(valid4);
        Assertions.assertFalse(valid5);
        Assertions.assertFalse(valid6);
        Assertions.assertFalse(valid7);
        Assertions.assertFalse(valid8);
        Assertions.assertFalse(valid9);
        Assertions.assertFalse(valid10);
        Assertions.assertFalse(valid11);
        Assertions.assertFalse(valid12);
        Assertions.assertFalse(valid13);
        Assertions.assertFalse(valid14);
        Assertions.assertFalse(valid15);
        Assertions.assertFalse(valid16);
        Assertions.assertFalse(valid17);
    }

    @Test
    public void consolePlayercheckvalidmethodtest1() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        var player1 = new ConsolePlayer("White");
        var player2 = new ConsolePlayer("Black");
        var config = new Configuration(13, new Player[]{player1, player2},5);
        var knight1 = new Knight(player1);
        config.addInitialPiece(knight1, 5, 5);
        var knight2 = new Knight(player2);
        config.addInitialPiece(knight2, 5, 6);

        var knight3 = new Knight(player1);
        config.addInitialPiece(knight3, 5, 4);
        var knight4 = new Knight(player1);
        config.addInitialPiece(knight4, 4, 5);
        var knight5 = new Knight(player1);
        config.addInitialPiece(knight5, 6, 5);
        var game = new JesonMor(config);

        var availMoves = new Move[]
                {new Move(0,0,2,1),
                        new Move(10,10,9,8)
                };
        Method checkvalidmethod = ConsolePlayer.class.getDeclaredMethod("checkMoveValidity", Game.class, Move.class, Move[].class);
        checkvalidmethod.setAccessible(true);
        var valid1 = (boolean) checkvalidmethod.invoke(player1, game, new Move(5,5,6,7),availMoves);
        var valid2 = (boolean) checkvalidmethod.invoke(player1, game, new Move(5,5,7,6),availMoves);
        var valid3 = (boolean) checkvalidmethod.invoke(player1, game, new Move(5,5,4,3),availMoves);
        var valid4 = (boolean) checkvalidmethod.invoke(player1, game, new Move(5,5,3,4),availMoves);
        Assertions.assertFalse(valid1);
        Assertions.assertFalse(valid2);
        Assertions.assertFalse(valid3);
        Assertions.assertFalse(valid4);

    }

    @Test
    public void consolePlayercheckvalidmethodtest2() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        var player1 = new ConsolePlayer("White");
        var player2 = new ConsolePlayer("Black");
        var config = new Configuration(13, new Player[]{player1, player2},0);

        var archer1 = new Archer(player1);
        config.addInitialPiece(archer1, 5, 5);

        var archer2 = new Archer(player2);
        config.addInitialPiece(archer2, 5, 6);
        var archer3 = new Archer(player1);
        config.addInitialPiece(archer3, 5, 4);
        var archer4 = new Archer(player1);
        config.addInitialPiece(archer4, 4, 5);
        var archer5 = new Archer(player1);
        config.addInitialPiece(archer5, 6, 5);

        var archer6 = new Archer(player1);
        config.addInitialPiece(archer6, 5, 8);
        var archer7 = new Archer(player1);
        config.addInitialPiece(archer7, 5, 2);
        var archer8 = new Archer(player1);
        config.addInitialPiece(archer8, 2, 5);
        var archer9 = new Archer(player1);
        config.addInitialPiece(archer9, 8, 5);
        var archer10 = new Archer(player1);
        config.addInitialPiece(archer10, 5, 7);
        var game = new JesonMor(config);

        var availMoves = new Move[]
                {new Move(0,0,2,1),
                        new Move(10,10,9,8)
                };

        Method checkvalidmethod = ConsolePlayer.class.getDeclaredMethod("checkMoveValidity", Game.class, Move.class, Move[].class);
        checkvalidmethod.setAccessible(true);

        //violate move rule
        var valid1 = (boolean) checkvalidmethod.invoke(player1, game, new Move(5,5,0,1),availMoves);
        var valid2 = (boolean) checkvalidmethod.invoke(player1, game, new Move(5,5,5,7),availMoves);
        var valid3 = (boolean) checkvalidmethod.invoke(player1, game, new Move(5,5,5,3),availMoves);
        var valid4 = (boolean) checkvalidmethod.invoke(player1, game, new Move(5,5,7,5),availMoves);
        var valid5 = (boolean) checkvalidmethod.invoke(player1, game, new Move(5,5,3,5),availMoves);

        var valid6 = (boolean) checkvalidmethod.invoke(player1, game, new Move(5,5,5,10),availMoves);
        var valid7 = (boolean) checkvalidmethod.invoke(player1, game, new Move(5,5,5,0),availMoves);
        var valid8 = (boolean) checkvalidmethod.invoke(player1, game, new Move(5,5,10,5),availMoves);
        var valid9 = (boolean) checkvalidmethod.invoke(player1, game, new Move(5,5,0,5),availMoves);

        var valid10 = (boolean) checkvalidmethod.invoke(player1, game, new Move(5,5,5,6),availMoves);

        Assertions.assertFalse(valid1);
        Assertions.assertFalse(valid2);
        Assertions.assertFalse(valid3);
        Assertions.assertFalse(valid4);
        Assertions.assertFalse(valid5);
        Assertions.assertFalse(valid6);
        Assertions.assertFalse(valid7);
        Assertions.assertFalse(valid8);
        Assertions.assertFalse(valid9);
        Assertions.assertFalse(valid10);
    }


    @Test
    public void randomPlayercheckvalidmethodtest0() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        var player1 = new RandomPlayer("White");
        var player2 = new RandomPlayer("Black");
        var config = new Configuration(13, new Player[]{player1, player2},5);
        var knight1 = new Knight(player1);
        config.addInitialPiece(knight1, 0, 0);
        var knight2 = new Knight(player2);
        config.addInitialPiece(knight2, 2, 1);

        var knight3 = new Knight(player1);
        config.addInitialPiece(knight3, 10, 10);
        var knight4 = new Knight(player1);
        config.addInitialPiece(knight4, 9, 8);
        var game = new JesonMor(config);

        Method checkvalidmethod = RandomPlayer.class.getDeclaredMethod("checkMoveValidity", Game.class, Move.class);
        checkvalidmethod.setAccessible(true);

        var valid1 = (boolean) checkvalidmethod.invoke(player1, null, new Move(0,0,1,2));
        var valid2 = (boolean) checkvalidmethod.invoke(player1, game, null);
        //violate move rule
        var valid3 = (boolean) checkvalidmethod.invoke(player1, game, new Move(0,0,0,1));
        //not self
        var valid4 = (boolean) checkvalidmethod.invoke(player2, game, new Move(0,0,1,2));
        //same position
        var valid5 = (boolean) checkvalidmethod.invoke(player1, game, new Move(0,0,0,0));
        //out of bound
        var valid7 = (boolean) checkvalidmethod.invoke(player1, game, new Move(-1,0,0,0));
        var valid8 = (boolean) checkvalidmethod.invoke(player1, game, new Move(0,-1,0,0));
        var valid9 = (boolean) checkvalidmethod.invoke(player1, game, new Move(0,0,-1,0));
        var valid10 = (boolean) checkvalidmethod.invoke(player1, game, new Move(0,0,0,-1));
        var valid11 = (boolean) checkvalidmethod.invoke(player1, game, new Move(21,0,0,0));
        var valid12 = (boolean) checkvalidmethod.invoke(player1, game, new Move(0,21,0,0));
        var valid13 = (boolean) checkvalidmethod.invoke(player1, game, new Move(0,0,21,0));
        var valid14 = (boolean) checkvalidmethod.invoke(player1, game, new Move(0,0,0,21));
        //no piece at origin
        var valid15 = (boolean) checkvalidmethod.invoke(player1, game, new Move(1,1,0,0));
        var valid16 = (boolean) checkvalidmethod.invoke(player1, game, new Move(0,0,2,1));
        var valid17 = (boolean) checkvalidmethod.invoke(player1, game, new Move(10,10,9,8));
        Assertions.assertFalse(valid1);
        Assertions.assertFalse(valid2);
        Assertions.assertFalse(valid3);
        Assertions.assertFalse(valid4);
        Assertions.assertFalse(valid5);
        Assertions.assertFalse(valid7);
        Assertions.assertFalse(valid8);
        Assertions.assertFalse(valid9);
        Assertions.assertFalse(valid10);
        Assertions.assertFalse(valid11);
        Assertions.assertFalse(valid12);
        Assertions.assertFalse(valid13);
        Assertions.assertFalse(valid14);
        Assertions.assertFalse(valid15);
        Assertions.assertFalse(valid16);
        Assertions.assertFalse(valid17);
    }

    @Test
    public void randomPlayercheckvalidmethodtest1() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        var player1 = new RandomPlayer("White");
        var player2 = new RandomPlayer("Black");
        var config = new Configuration(13, new Player[]{player1, player2},5);
        var knight1 = new Knight(player1);
        config.addInitialPiece(knight1, 5, 5);
        var knight2 = new Knight(player2);
        config.addInitialPiece(knight2, 5, 6);

        var knight3 = new Knight(player1);
        config.addInitialPiece(knight3, 5, 4);
        var knight4 = new Knight(player1);
        config.addInitialPiece(knight4, 4, 5);
        var knight5 = new Knight(player1);
        config.addInitialPiece(knight5, 6, 5);
        var game = new JesonMor(config);

        Method checkvalidmethod = RandomPlayer.class.getDeclaredMethod("checkMoveValidity", Game.class, Move.class);
        checkvalidmethod.setAccessible(true);

        var valid1 = (boolean) checkvalidmethod.invoke(player1, game, new Move(5,5,6,7));
        var valid2 = (boolean) checkvalidmethod.invoke(player1, game, new Move(5,5,7,6));
        var valid3 = (boolean) checkvalidmethod.invoke(player1, game, new Move(5,5,4,3));
        var valid4 = (boolean) checkvalidmethod.invoke(player1, game, new Move(5,5,3,4));
        Assertions.assertFalse(valid1);
        Assertions.assertFalse(valid2);
        Assertions.assertFalse(valid3);
        Assertions.assertFalse(valid4);

    }

    @Test
    public void randomPlayercheckvalidmethodtest2() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        var player1 = new RandomPlayer("White");
        var player2 = new RandomPlayer("Black");
        var config = new Configuration(13, new Player[]{player1, player2},0);

        var archer1 = new Archer(player1);
        config.addInitialPiece(archer1, 5, 5);

        var archer2 = new Archer(player2);
        config.addInitialPiece(archer2, 5, 6);
        var archer3 = new Archer(player1);
        config.addInitialPiece(archer3, 5, 4);
        var archer4 = new Archer(player1);
        config.addInitialPiece(archer4, 4, 5);
        var archer5 = new Archer(player1);
        config.addInitialPiece(archer5, 6, 5);

        var archer6 = new Archer(player1);
        config.addInitialPiece(archer6, 5, 8);
        var archer7 = new Archer(player1);
        config.addInitialPiece(archer7, 5, 2);
        var archer8 = new Archer(player1);
        config.addInitialPiece(archer8, 2, 5);
        var archer9 = new Archer(player1);
        config.addInitialPiece(archer9, 8, 5);
        var archer10 = new Archer(player1);
        config.addInitialPiece(archer10, 5, 7);
        var game = new JesonMor(config);


        Method checkvalidmethod = RandomPlayer.class.getDeclaredMethod("checkMoveValidity", Game.class, Move.class);
        checkvalidmethod.setAccessible(true);

        //violate move rule
        var valid1 = (boolean) checkvalidmethod.invoke(player1, game, new Move(5,5,0,1));
        var valid2 = (boolean) checkvalidmethod.invoke(player1, game, new Move(5,5,5,7));
        var valid3 = (boolean) checkvalidmethod.invoke(player1, game, new Move(5,5,5,3));
        var valid4 = (boolean) checkvalidmethod.invoke(player1, game, new Move(5,5,7,5));
        var valid5 = (boolean) checkvalidmethod.invoke(player1, game, new Move(5,5,3,5));

        var valid6 = (boolean) checkvalidmethod.invoke(player1, game, new Move(5,5,5,10));
        var valid7 = (boolean) checkvalidmethod.invoke(player1, game, new Move(5,5,5,0));
        var valid8 = (boolean) checkvalidmethod.invoke(player1, game, new Move(5,5,10,5));
        var valid9 = (boolean) checkvalidmethod.invoke(player1, game, new Move(5,5,0,5));
        var valid10 = (boolean) checkvalidmethod.invoke(player1, game, new Move(5,5,5,6));

        Assertions.assertFalse(valid1);
        Assertions.assertFalse(valid2);
        Assertions.assertFalse(valid3);
        Assertions.assertFalse(valid4);
        Assertions.assertFalse(valid5);
        Assertions.assertFalse(valid6);
        Assertions.assertFalse(valid7);
        Assertions.assertFalse(valid8);
        Assertions.assertFalse(valid9);
        Assertions.assertFalse(valid10);
    }

    @Test
    public void jesonmorcheckvalidmethodtest0() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        var player1 = new RandomPlayer("White");
        var player2 = new RandomPlayer("Black");
        var config = new Configuration(13, new Player[]{player1, player2},5);
        var knight1 = new Knight(player1);
        config.addInitialPiece(knight1, 0, 0);
        var knight2 = new Knight(player2);
        config.addInitialPiece(knight2, 2, 1);

        var knight3 = new Knight(player1);
        config.addInitialPiece(knight3, 10, 10);
        var knight4 = new Knight(player1);
        config.addInitialPiece(knight4, 9, 8);
        var game = new JesonMor(config);

        Method checkvalidmethod = JesonMor.class.getDeclaredMethod("checkMoveValidity", Move.class, Player.class);
        checkvalidmethod.setAccessible(true);

        var valid1 = (boolean) checkvalidmethod.invoke(game, null, player1);
        var valid2 = (boolean) checkvalidmethod.invoke(game, new Move(0,0,0,1), null);
        //violate move rule
        var valid3 = (boolean) checkvalidmethod.invoke(game,new Move(0,0,0,1), player1);
        //not correct player
        var valid4 = (boolean) checkvalidmethod.invoke(game, new Move(0,0,1,2), player2);
        //same position
        var valid5 = (boolean) checkvalidmethod.invoke(game, new Move(0,0,0,0), player1);
        //out of bound
        var valid7 = (boolean) checkvalidmethod.invoke(game, new Move(-1,0,0,0), player1);
        var valid8 = (boolean) checkvalidmethod.invoke(game, new Move(0,-1,0,0), player1);
        var valid9 = (boolean) checkvalidmethod.invoke(game, new Move(0,0,-1,0), player1);
        var valid10 = (boolean) checkvalidmethod.invoke(game, new Move(0,0,0,-1), player1);
        var valid11 = (boolean) checkvalidmethod.invoke(game, new Move(21,0,0,0), player1);
        var valid12 = (boolean) checkvalidmethod.invoke(game, new Move(0,21,0,0), player1);
        var valid13 = (boolean) checkvalidmethod.invoke(game, new Move(0,0,21,0), player1);
        var valid14 = (boolean) checkvalidmethod.invoke(game, new Move(0,0,0,21), player1);
        //no piece at origin
        var valid15 = (boolean) checkvalidmethod.invoke(game, new Move(1,1,0,0), player1);
        var valid16 = (boolean) checkvalidmethod.invoke(game, new Move(0,0,2,1), player1);
        var valid17 = (boolean) checkvalidmethod.invoke(game, new Move(10,10,9,8), player1);
        Assertions.assertFalse(valid1);
        Assertions.assertFalse(valid2);
        Assertions.assertFalse(valid3);
        Assertions.assertFalse(valid4);
        Assertions.assertFalse(valid5);
        Assertions.assertFalse(valid7);
        Assertions.assertFalse(valid8);
        Assertions.assertFalse(valid9);
        Assertions.assertFalse(valid10);
        Assertions.assertFalse(valid11);
        Assertions.assertFalse(valid12);
        Assertions.assertFalse(valid13);
        Assertions.assertFalse(valid14);
        Assertions.assertFalse(valid15);
        Assertions.assertFalse(valid16);
        Assertions.assertFalse(valid17);
    }

    @Test
    public void jesonmorPlayercheckvalidmethodtest1() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        var player1 = new RandomPlayer("White");
        var player2 = new RandomPlayer("Black");
        var config = new Configuration(13, new Player[]{player1, player2},5);
        var knight1 = new Knight(player1);
        config.addInitialPiece(knight1, 5, 5);
        var knight2 = new Knight(player2);
        config.addInitialPiece(knight2, 5, 6);

        var knight3 = new Knight(player1);
        config.addInitialPiece(knight3, 5, 4);
        var knight4 = new Knight(player1);
        config.addInitialPiece(knight4, 4, 5);
        var knight5 = new Knight(player1);
        config.addInitialPiece(knight5, 6, 5);
        var game = new JesonMor(config);

        Method checkvalidmethod = JesonMor.class.getDeclaredMethod("checkMoveValidity", Move.class, Player.class);
        checkvalidmethod.setAccessible(true);

        var valid1 = (boolean) checkvalidmethod.invoke(game, new Move(5,5,6,7),player1);
        var valid2 = (boolean) checkvalidmethod.invoke(game, new Move(5,5,7,6),player1);
        var valid3 = (boolean) checkvalidmethod.invoke(game, new Move(5,5,4,3),player1);
        var valid4 = (boolean) checkvalidmethod.invoke(game, new Move(5,5,3,4),player1);
        Assertions.assertFalse(valid1);
        Assertions.assertFalse(valid2);
        Assertions.assertFalse(valid3);
        Assertions.assertFalse(valid4);

    }

    @Test
    public void jesonmorPlayercheckvalidmethodtest2() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        var player1 = new RandomPlayer("White");
        var player2 = new RandomPlayer("Black");
        var config = new Configuration(13, new Player[]{player1, player2},0);

        var archer1 = new Archer(player1);
        config.addInitialPiece(archer1, 5, 5);

        var archer2 = new Archer(player2);
        config.addInitialPiece(archer2, 5, 6);
        var archer3 = new Archer(player1);
        config.addInitialPiece(archer3, 5, 4);
        var archer4 = new Archer(player1);
        config.addInitialPiece(archer4, 4, 5);
        var archer5 = new Archer(player1);
        config.addInitialPiece(archer5, 6, 5);

        var archer6 = new Archer(player1);
        config.addInitialPiece(archer6, 5, 8);
        var archer7 = new Archer(player1);
        config.addInitialPiece(archer7, 5, 2);
        var archer8 = new Archer(player1);
        config.addInitialPiece(archer8, 2, 5);
        var archer9 = new Archer(player1);
        config.addInitialPiece(archer9, 8, 5);
        var archer10 = new Archer(player1);
        config.addInitialPiece(archer10, 5, 7);
        var game = new JesonMor(config);


        Method checkvalidmethod = JesonMor.class.getDeclaredMethod("checkMoveValidity", Move.class, Player.class);
        checkvalidmethod.setAccessible(true);

        //violate move rule
        var valid1 = (boolean) checkvalidmethod.invoke(game, new Move(5,5,0,1),player1);
        var valid2 = (boolean) checkvalidmethod.invoke(game, new Move(5,5,5,7),player1);
        var valid3 = (boolean) checkvalidmethod.invoke(game, new Move(5,5,5,3),player1);
        var valid4 = (boolean) checkvalidmethod.invoke(game, new Move(5,5,7,5),player1);
        var valid5 = (boolean) checkvalidmethod.invoke(game, new Move(5,5,3,5),player1);

        var valid6 = (boolean) checkvalidmethod.invoke(game, new Move(5,5,5,10),player1);
        var valid7 = (boolean) checkvalidmethod.invoke(game, new Move(5,5,5,0),player1);
        var valid8 = (boolean) checkvalidmethod.invoke(game, new Move(5,5,10,5),player1);
        var valid9 = (boolean) checkvalidmethod.invoke(game, new Move(5,5,0,5),player1);
        var valid10 = (boolean) checkvalidmethod.invoke(game, new Move(5,5,5,6),player1);

        Assertions.assertFalse(valid1);
        Assertions.assertFalse(valid2);
        Assertions.assertFalse(valid3);
        Assertions.assertFalse(valid4);
        Assertions.assertFalse(valid5);
        Assertions.assertFalse(valid6);
        Assertions.assertFalse(valid7);
        Assertions.assertFalse(valid8);
        Assertions.assertFalse(valid9);
        Assertions.assertFalse(valid10);
    }

}
