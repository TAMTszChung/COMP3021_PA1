package castle.comp3021.assignment.piece;

import castle.comp3021.assignment.protocol.Game;
import castle.comp3021.assignment.protocol.Move;
import castle.comp3021.assignment.protocol.Piece;
import castle.comp3021.assignment.protocol.Place;
import castle.comp3021.assignment.protocol.Player;

import java.util.ArrayList;

/**
 * Knight piece that moves similar to knight in chess.
 * Rules of move of Knight can be found in wikipedia (https://en.wikipedia.org/wiki/Knight_(chess)).
 *
 * @see <a href='https://en.wikipedia.org/wiki/Knight_(chess)'>Wikipedia</a>
 */
public class Knight extends Piece {
    public Knight(Player player) {
        super(player);
    }

    @Override
    public char getLabel() {
        return 'K';
    }

    /**
     * Returns an array of moves that are valid given the current place of the piece.
     * Given the {@link Game} object and the {@link Place} that current knight piece locates, this method should
     * return ALL VALID {@link Move}s according to the current {@link Place} of this knight piece.
     * All the returned {@link Move} should have source equal to the source parameter.
     * <p>
     * Hint: you should consider corner cases when the {@link Move} is not valid on the gameboard.
     * Several tests are provided and your implementation should pass them.
     * <p>
     * <strong>Attention: Student should make sure all {@link Move}s returned are valid.</strong>
     *
     * @param game   the game object
     * @param source the current place of the piece
     * @return an array of available moves
     */
    @Override
    public Move[] getAvailableMoves(Game game, Place source) {
        // TODO student implementation
        var sourceX = source.x();
        var sourceY = source.y();
        ArrayList<Move> availableMove = new ArrayList<>();
        availableMove.add(new Move(source, new Place(sourceX+1, sourceY+2)));
        availableMove.add(new Move(source, new Place(sourceX+2, sourceY+1)));
        availableMove.add(new Move(source, new Place(sourceX+2, sourceY-1)));
        availableMove.add(new Move(source, new Place(sourceX+1, sourceY-2)));
        availableMove.add(new Move(source, new Place(sourceX-1, sourceY-2)));
        availableMove.add(new Move(source, new Place(sourceX-2, sourceY-1)));
        availableMove.add(new Move(source, new Place(sourceX-2, sourceY+1)));
        availableMove.add(new Move(source, new Place(sourceX-1, sourceY+2)));

        //validation
        for (int i=availableMove.size()-1; i>=0;i--){
            if (!checkMoveValidity(game, availableMove.get(i))){
                availableMove.remove(i);
            }
        }

        return availableMove.toArray(new Move[availableMove.size()]);
    }

    private boolean checkMoveValidity(Game game, Move move){
        var originalX = move.getSource().x();
        var originalY = move.getSource().y();
        var destinationX = move.getDestination().x();
        var destinationY = move.getDestination().y();

        //out of bound
        if (originalX<0
                ||originalY<0
                ||originalX>=game.getConfiguration().getSize()
                ||originalY>=game.getConfiguration().getSize()
                || destinationX<0
                || destinationY<0
                || destinationX>=game.getConfiguration().getSize()
                || destinationY>=game.getConfiguration().getSize()){
            return false;
        }

        //no piece at origin
        if (game.getPiece(originalX,originalY) == null){
            return false;
        }


        //check capturing
        if (game.getPiece(destinationX,destinationY) != null){
            if (game.getPiece(destinationX,destinationY).getPlayer().equals(game.getPiece(originalX,originalY).getPlayer())){
                //capturing own piece
                return false;
            }else if (!game.getPiece(destinationX,destinationY).getPlayer().equals(game.getPiece(originalX,originalY).getPlayer())
                    && game.getNumMoves() < game.getConfiguration().getNumMovesProtection()){
                //capturing enemy within NumMovesProtection
                return false;
            }
        }

        var xShift = destinationX - originalX;
        var yShift = destinationY - originalY;
        //blocking condition
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
            //violate Knight move rule
            return false;
        }
        return true;
    }
}
