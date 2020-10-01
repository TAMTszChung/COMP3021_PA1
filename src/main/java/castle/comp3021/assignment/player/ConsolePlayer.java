package castle.comp3021.assignment.player;

import castle.comp3021.assignment.piece.Archer;
import castle.comp3021.assignment.piece.Knight;
import castle.comp3021.assignment.protocol.*;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/**
 * The player that makes move according to user input from console.
 */
public class ConsolePlayer extends Player {
    public ConsolePlayer(String name, Color color) {
        super(name, color);
    }

    public ConsolePlayer(String name) {
        this(name, Color.GREEN);
    }

    /**
     * Choose a move from available moves.
     * This method will be called by {@link Game} object to get the move that the player wants to make when it is the
     * player's turn.
     * <p>
     * {@link ConsolePlayer} returns a move according to user's input in the console.
     * The console input format should conform the format described in the assignment description.
     * (e.g. {@literal a1->b3} means move the {@link Piece} at {@link Place}(x=0,y=0) to {@link Place}(x=1,y=2))
     * Note that in the {@link Game}.board, the index starts from 0 in both x and y dimension, while in the console
     * display, x dimension index starts from 'a' and y dimension index starts from 1.
     * <p>
     * Hint: be sure to handle invalid input to avoid invalid {@link Move}s.
     * <p>
     * <strong>Attention: Student should make sure the {@link Move} returned is valid.</strong>
     * <p>
     * <strong>Attention: {@link Place} object uses integer as index of x and y-axis, both starting from 0 to
     * facilitate programming.
     * This is VERY different from the coordinate used in console display.</strong>
     *
     * @param game           the current game object
     * @param availableMoves available moves for this player to choose from.
     * @return the chosen move
     */
    @Override
    public @NotNull Move nextMove(Game game, Move[] availableMoves) {
        // TODO student implementation
        Move chosenMove = null;

        while (chosenMove == null){
            System.out.print("["+this.name+"]"+" Make a Move: ");
            Scanner scanner = new Scanner(System.in);
            String userInput = scanner.nextLine();
            if (!userInput.matches("[ \\t]*[a-zA-Z]{1}[\\d]+[ \\t]*->[ \\t]*[a-zA-Z]{1}[\\d]+[ \\t]*$")){
                System.out.println("[Invalid Move]: Incorrect format");
                continue;
            }
            //split the input into 2 string for location and trim white space
            String[] locations = userInput.split("->");
            for (int i=0;i< locations.length;i++){
                locations[i] = locations[i].trim();
            }

            var inputsourceX = (locations[0].substring(0,1).toLowerCase().charAt(0)-'a');
            var inputsourceY = Integer.parseInt(locations[0].substring(1))-1;
            var inputdesX = (locations[1].substring(0,1).toLowerCase().charAt(0)-'a');
            var inputdesY = Integer.parseInt(locations[1].substring(1))-1;

            if (inputsourceX<0
                    ||inputsourceY<0
                    ||inputsourceX>=game.getConfiguration().getSize()
                    ||inputsourceY>=game.getConfiguration().getSize()
                    || inputdesX<0
                    || inputdesY<0
                    || inputdesX>=game.getConfiguration().getSize()
                    || inputdesY>=game.getConfiguration().getSize()){
                System.out.println("[Invalid Move]: place is out of boundary of gameboard");
                continue;
            }
            if (game.getPiece(inputdesX, inputdesY) != null){
                if (game.getPiece(inputdesX, inputdesY).getPlayer()
                        .equals(game.getPiece(inputsourceX, inputsourceY).getPlayer())){
                    System.out.println("[Invalid Move]: piece cannot be captured by another piece belonging to the same player");
                    continue;
                }
            }

            if (game.getPiece(inputsourceX, inputsourceY) == null){
                System.out.println("[Invalid Move]: No piece at s(" + inputsourceX + ", " + inputsourceY + ")");
                continue;
            }else if (game.getPiece(inputsourceX, inputsourceY) instanceof Knight){
                var xShift = inputdesX - inputsourceX;
                var yShift = inputdesY - inputsourceY;
                if (!(xShift==1 && yShift==2)
                        && !(xShift==-1 && yShift==2)
                        && !(xShift==2 && yShift==1)
                        && !(xShift==-2 && yShift==1)
                        && !(xShift==2 && yShift==-1)
                        && !(xShift==-2 && yShift==-1)
                        && !(xShift==1 && yShift==-2)
                        && !(xShift==-1 && yShift==-2)){
                    System.out.println("[Invalid Move]: knight move rule is violated");
                    continue;
                }
            }

            Move tempMove = new Move(inputsourceX,inputsourceY,inputdesX,inputdesY);
            ArrayList<Move> Moves =
                    new ArrayList<Move>(Arrays.asList(availableMoves));
            if (!Moves.contains(tempMove)){
                System.out.println("[Invalid Move]: please make a valid move");
                continue;
            }
            chosenMove = tempMove;
        }

        return chosenMove;
    }
}
