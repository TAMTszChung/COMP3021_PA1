package castle.comp3021.assignment.piece;

import castle.comp3021.assignment.protocol.Game;
import castle.comp3021.assignment.protocol.Move;
import castle.comp3021.assignment.protocol.Piece;
import castle.comp3021.assignment.protocol.Place;
import castle.comp3021.assignment.protocol.Player;

import java.util.ArrayList;

/**
 * Archer piece that moves similar to cannon in chinese chess.
 * Rules of move of Archer can be found in wikipedia (https://en.wikipedia.org/wiki/Xiangqi#Cannon).
 * <p>
 * <strong>Attention: If you want to implement Archer as the bonus task, you should remove "{@code throw new
 * UnsupportedOperationException();}" in the constructor of this class.</strong>
 *
 * @see <a href='https://en.wikipedia.org/wiki/Xiangqi#Cannon'>Wikipedia</a>
 */
public class Archer extends Piece {
    public Archer(Player player) {
        super(player);
    }

    @Override
    public char getLabel() {
        return 'A';
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
        ArrayList<Move> availableMove = new ArrayList<>();
        var originalx = source.x();
        var originaly = source.y();
        var gameSize = game.getConfiguration().getSize();
        //add x axis
        for (int x=0; x<gameSize; x++){
            availableMove.add(new Move(source, new Place(x, originaly)));
        }
        //add y axis
        for (int y=0; y<gameSize; y++){
            availableMove.add(new Move(source, new Place(originalx, y)));
        }

        for (int x=availableMove.size()-1; x>=0;x--){
            if (!checkMoveValidity(game, availableMove.get(x))){
                availableMove.remove(x);
            }
        }

        return availableMove.toArray(new Move[availableMove.size()]);
    }

    private boolean checkMoveValidity(Game game, Move move){
        var originalX = move.getSource().x();
        var originalY = move.getSource().y();
        var destinationX = move.getDestination().x();
        var destinationY = move.getDestination().y();

        var gameSize = game.getConfiguration().getSize();

        var xShift = destinationX - originalX;
        var yShift = destinationY - originalY;
        //out of bound
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
        if (game.getPiece(originalX,originalY) == null){
            return false;
        }

        //check if destination is self
        if (destinationX == originalX && destinationY == originalY){
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
            if (game.getPiece(destinationX, destinationY) == null) {
                return false;
            } else if (desPiece.getPlayer().equals(originPiece.getPlayer())) {
                return false;
            }
        }else{
            if (desPiece != null){
                return false;
            }
        }

        return true;
    }
}
