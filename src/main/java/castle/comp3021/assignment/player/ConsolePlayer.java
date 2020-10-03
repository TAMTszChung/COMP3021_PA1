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
        Scanner scanner = new Scanner(System.in);

        while (chosenMove == null){
            System.out.print("["+this.name+"]"+" Make a Move: ");
            String userInput = scanner.nextLine();
            if (!userInput.matches("^\\s*[a-zA-Z]{1}[\\d]+\\s*->\\s*[a-zA-Z]{1}[\\d]+\\s*$")){
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
            if (!checkMoveValidity(game, tempMove, availableMoves)){
                continue;
            }

            chosenMove = tempMove;
        }

        return chosenMove;
    }

    private boolean checkMoveValidity(Game game, Move tempMove, Move[] availableMoves){
        var originalX = tempMove.getSource().x();
        var originalY = tempMove.getSource().y();
        var destinationX = tempMove.getDestination().x();
        var destinationY = tempMove.getDestination().y();
        var gameSize = game.getConfiguration().getSize();
        //validate the move
        //out of boundary
        if (originalX<0
                ||originalY<0
                ||originalX>=gameSize
                ||originalY>=gameSize
                || destinationX<0
                || destinationY<0
                || destinationX>=gameSize
                || destinationY>=gameSize){
            System.out.println("[Invalid Move]: place is out of boundary of gameboard");
            return false;
        }

        var originPiece = game.getPiece(originalX, originalY);
        var desPiece = game.getPiece(destinationX,destinationY);

        //no piece at origin
        if (originPiece == null){
            System.out.println("[Invalid Move]: No piece at s(" + originalX + ", " + originalY + ")");
            return false;
        }
        //check if destination is self
        if (destinationX == originalX && destinationY == originalY){
            System.out.println("[Invalid Move]: piece cannot be captured by another piece belonging to the same player");
            return false;
        }
        //check piece belong to player
        if (!originPiece.getPlayer().equals(this)){
            System.out.println("[Invalid Move]: Piece does not belong to player");
            return false;
        }

        //check capturing
        if (desPiece != null){
            if (desPiece.getPlayer().equals(originPiece.getPlayer())){
                //capturing own piece
                System.out.println("[Invalid Move]: piece cannot be captured by another piece belonging to the same player");
                return false;
            }else if (game.getNumMoves() < game.getConfiguration().getNumMovesProtection()){
                //capturing enemy within NumMovesProtection
                System.out.println("[Invalid Move]: Cannot capture within NumMovesProtection");
                return false;
            }
        }

        var xShift = destinationX - originalX;
        var yShift = destinationY - originalY;
        if (originPiece instanceof Knight){
            //check moving rule of Knight
            if (Math.abs(xShift) == 2 && Math.abs(yShift) == 1){
                var legPosX = (destinationX + originalX)/2;
                var legPosY = originalY;
                if (game.getPiece(legPosX,legPosY) != null){
                    System.out.println("[Invalid Move]: knight is blocked by another piece");
                    return false;
                }
            }else if (Math.abs(xShift) == 1 && Math.abs(yShift) == 2){
                var legPosX = originalX;
                var legPosY = (destinationY + originalY)/2;
                if (game.getPiece(legPosX,legPosY) != null){
                    System.out.println("[Invalid Move]: knight is blocked by another piece");
                    return false;
                }
            }else{
                System.out.println("[Invalid Move]: knight move rule is violated");
                return false;
            }
        }else if(originPiece instanceof Archer){
            //check archer
            //check moving rule of archer
            if (Math.abs(xShift) > 0 && Math.abs(yShift) > 0){
                System.out.println("[Invalid Move]: Archer move rule is violated");
                return false;
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
                return false;
            }

            if (numPiecebetween >= 2){
                System.out.println("[Invalid Move]: Archer move rule is violated");
                return false;
            }else if (numPiecebetween == 1) {
                if (desPiece == null) {
                    return false;
                } else if (desPiece.getPlayer().equals(originPiece.getPlayer())) {
                    return false;
                }
            }else{
                if (desPiece != null){
                    return false;
                }
            }
        }

        ArrayList<Move> availMoves = new ArrayList<>(Arrays.asList(availableMoves));
        if (!availMoves.contains(tempMove)){
            System.out.println("[Invalid Move]: please make a valid move");
            return false;
        }

        return true;
    }
}
