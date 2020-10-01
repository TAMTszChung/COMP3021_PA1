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
        //right
        var otherPiece = 0;
        for (int i=originalx+1; i<game.getConfiguration().getSize(); i++){
            if (game.getPiece(i,originaly) != null){
                otherPiece += 1;
            }
            if (otherPiece == 0){
                availableMove.add(new Move(source, new Place(i, originaly)));
            }else if(otherPiece == 1){
                continue;
            }else if(otherPiece == 2){
                if (game.getPiece(i,originaly) != null && !game.getPiece(i,originaly).getPlayer().equals(game.getCurrentPlayer())){
                    availableMove.add(new Move(source, new Place(i, originaly)));
                }
                break;
            }else{
                break;
            }
        }
        //left
        otherPiece = 0;
        for (int i=originalx-1; i>=0; i--){
            if (game.getPiece(i,originaly) != null){
                otherPiece += 1;
            }
            if (otherPiece == 0){
                availableMove.add(new Move(source, new Place(i, originaly)));
            }else if(otherPiece == 1){
                continue;
            }else if(otherPiece == 2){
                if (game.getPiece(i,originaly) != null && !game.getPiece(i,originaly).getPlayer().equals(game.getCurrentPlayer())){
                    availableMove.add(new Move(source, new Place(i, originaly)));
                }
                break;
            }else{
                break;
            }
        }
        //up
        otherPiece = 0;
        for (int i=originaly+1; i<game.getConfiguration().getSize(); i++){
            if (game.getPiece(originalx,i) != null){
                otherPiece += 1;
            }
            if (otherPiece == 0){
                availableMove.add(new Move(source, new Place(originalx,i)));
            }else if(otherPiece == 1){
                continue;
            }else if(otherPiece == 2){
                if (game.getPiece(originalx,i) != null && !game.getPiece(originalx,i).getPlayer().equals(game.getCurrentPlayer())){
                    availableMove.add(new Move(source, new Place(originalx,i)));
                }
                break;
            }else{
                break;
            }
        }
        //down
        otherPiece = 0;
        for (int i=originaly-1; i>=0; i--){
            if (game.getPiece(originalx,i) != null){
                otherPiece += 1;
            }
            if (otherPiece == 0){
                availableMove.add(new Move(source, new Place(originalx,i)));
            }else if(otherPiece == 1){
                continue;
            }else if(otherPiece == 2){
                if (game.getPiece(originalx,i) != null && !game.getPiece(originalx,i).getPlayer().equals(game.getCurrentPlayer())){
                    availableMove.add(new Move(source, new Place(originalx,i)));
                }
                break;
            }else{
                break;
            }
        }
        return availableMove.toArray(new Move[availableMove.size()]);
    }
}
