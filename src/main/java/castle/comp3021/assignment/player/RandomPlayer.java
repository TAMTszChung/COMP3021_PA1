package castle.comp3021.assignment.player;

import castle.comp3021.assignment.piece.Archer;
import castle.comp3021.assignment.piece.Knight;
import castle.comp3021.assignment.protocol.Game;
import castle.comp3021.assignment.protocol.Color;
import castle.comp3021.assignment.protocol.Move;
import castle.comp3021.assignment.protocol.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

/**
 * A computer player that makes a move randomly.
 */
public class RandomPlayer extends Player {
    public RandomPlayer(String name, Color color) {
        super(name, color);
    }

    public RandomPlayer(String name) {
        this(name, Color.BLUE);
    }

    /**
     * Choose a move from available moves.
     * This method will be called by {@link Game} object to get the move that the player wants to make when it is the
     * player's turn.
     * <p>
     * {@link RandomPlayer} chooses a move from available ones randomly.
     * <p>
     * <strong>Attention: Student should make sure the {@link Move} returned is valid.</strong>
     *
     * @param game           the current game object
     * @param availableMoves available moves for this player to choose from.
     * @return the chosen move
     */
    @Override
    public @NotNull Move nextMove(Game game, Move[] availableMoves) {
        // TODO student implementation
        Move chosenMove = null;
        while(chosenMove == null) {
            Move tempMove = availableMoves[new Random().nextInt(availableMoves.length)];

            if (!checkMoveValidity(game, tempMove)){
                continue;
            }

            chosenMove = tempMove;
        }

        return chosenMove;
    }

    private boolean checkMoveValidity(Game game, Move tempMove){
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
            return false;
        }

        var originPiece = game.getPiece(originalX, originalY);
        var desPiece = game.getPiece(destinationX,destinationY);

        //no piece at origin
        if (originPiece == null){
            return false;
        }
        //check if destination is self
        if (destinationX == originalX && destinationY == originalY){
            return false;
        }
        //check piece belong to player
        if (!originPiece.getPlayer().equals(this)){
            return false;
        }

        //check capturing
        if (desPiece != null){
            if (desPiece.getPlayer().equals(originPiece.getPlayer())){
                //capturing own piece
                return false;
            }else if (game.getNumMoves() < game.getConfiguration().getNumMovesProtection()){
                //capturing enemy within NumMovesProtection
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
                    return false;
                }
            }else if (Math.abs(xShift) == 1 && Math.abs(yShift) == 2){
                var legPosX = originalX;
                var legPosY = (destinationY + originalY)/2;
                if (game.getPiece(legPosX,legPosY) != null){
                    return false;
                }
            }else{
                return false;
            }
        }else if(originPiece instanceof Archer){
            //check archer
            //check moving rule of archer
            if (Math.abs(xShift) > 0 && Math.abs(yShift) > 0){
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
                return false;
            }

            if (numPiecebetween >= 2){
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
        return true;
    }
}
