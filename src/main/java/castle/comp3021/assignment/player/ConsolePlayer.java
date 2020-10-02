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
            //create the move
            String[] locations = userInput.split("->");
            for (int i=0;i< locations.length;i++){
                locations[i] = locations[i].trim();
            }

            var inputoriginalX = (locations[0].substring(0,1).toLowerCase().charAt(0)-'a');
            var inputoriginalY = Integer.parseInt(locations[0].substring(1))-1;
            var inputdestinationX = (locations[1].substring(0,1).toLowerCase().charAt(0)-'a');
            var inputdestinationY = Integer.parseInt(locations[1].substring(1))-1;

            Move tempMove = new Move(inputoriginalX,inputoriginalY,inputdestinationX,inputdestinationY);

            var originalX = tempMove.getSource().x();
            var originalY = tempMove.getSource().y();
            var destinationX = tempMove.getDestination().x();
            var destinationY = tempMove.getDestination().y();

            //validate the move
            //out of boundary
            if (originalX<0
                    ||originalY<0
                    ||originalX>=game.getConfiguration().getSize()
                    ||originalY>=game.getConfiguration().getSize()
                    || destinationX<0
                    || destinationY<0
                    || destinationX>=game.getConfiguration().getSize()
                    || destinationY>=game.getConfiguration().getSize()){
                System.out.println("[Invalid Move]: place is out of boundary of gameboard");
                continue;
            }

            //check any piece at original location
            if (game.getPiece(originalX, originalY) == null){
                System.out.println("[Invalid Move]: No piece at s(" + originalX + ", " + originalY + ")");
                continue;
            }

            //check piece belong to player
            if (!game.getPiece(originalX, originalY).getPlayer().equals(this)){
                System.out.println("[Invalid Move]: Piece do not belong to player");
                continue;
            }

            //check capturing own piece
            if (game.getPiece(destinationX, destinationY) != null){
                if (game.getPiece(destinationX, destinationY).getPlayer()
                        .equals(game.getPiece(originalX, originalY).getPlayer())){
                    System.out.println("[Invalid Move]: piece cannot be captured by another piece belonging to the same player");
                    continue;
                }
            }

            var xShift = destinationX - originalX;
            var yShift = destinationY - originalY;
            if (game.getPiece(originalX, originalY) instanceof Knight){
                //check moving rule of Knight
                if (Math.abs(xShift) == 2 && Math.abs(yShift) == 1){
                    var legPosX = (destinationX + originalX)/2;
                    var legPosY = originalY;
                    if (game.getPiece(legPosX,legPosY) != null){
                        System.out.println("[Invalid Move]: knight is blocked by another piece");
                        continue;
                    }
                }else if (Math.abs(xShift) == 1 && Math.abs(yShift) == 2){
                    var legPosX = originalX;
                    var legPosY = (destinationY + originalY)/2;
                    if (game.getPiece(legPosX,legPosY) != null){
                        System.out.println("[Invalid Move]: knight is blocked by another piece");
                        continue;
                    }
                }else{
                    System.out.println("[Invalid Move]: knight move rule is violated");
                    continue;
                }
            }else if(game.getPiece(originalX, originalY) instanceof Archer){
                //check archer
                //check moving rule of archer
                if (Math.abs(xShift) > 0 && Math.abs(yShift) > 0){
                    System.out.println("[Invalid Move]: Archer move rule is violated");
                    continue;
                }
                var numPiecebetween = 0;
                if (xShift<0 && yShift==0){
                    for (int i=originalX-1; i>destinationX; i--){
                        if (game.getPiece(i,originalY) != null){
                            numPiecebetween += 1;
                        }
                    }
                }else if(xShift>0 && yShift==0){
                    for (int i=originalX+1; i<destinationX; i++){
                        if (game.getPiece(i,originalY) != null){
                            numPiecebetween += 1;
                        }
                    }
                }else if (xShift==0 && yShift<0){
                    for (int i=originalY-1; i>destinationY; i--){
                        if (game.getPiece(originalX,i) != null){
                            numPiecebetween += 1;
                        }
                    }
                }else if (xShift==0 && yShift>0){
                    for (int i=originalY+1; i<destinationY; i++){
                        if (game.getPiece(originalX,i) != null){
                            numPiecebetween += 1;
                        }
                    }
                }else{
                    System.out.println("[Invalid Move]: Archer move rule is violated");
                    continue;
                }

                if (numPiecebetween >= 2){
                    System.out.println("[Invalid Move]: Archer move rule is violated");
                    continue;
                }else if (numPiecebetween == 1
                        && game.getPiece(destinationX,destinationY).getPlayer()
                        .equals(game.getPiece(originalX, originalY).getPlayer())){
                    System.out.println("[Invalid Move]: Archer move rule is violated");
                    continue;
                }
            }

            ArrayList<Move> availMoves =
                    new ArrayList<Move>(Arrays.asList(availableMoves));
            if (!availMoves.contains(tempMove)){
                System.out.println("[Invalid Move]: please make a valid move");
                continue;
            }
            chosenMove = tempMove;
        }

        return chosenMove;
    }
}
