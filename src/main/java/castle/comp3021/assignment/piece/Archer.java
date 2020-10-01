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
        ArrayList<Move> AvailableMove = new ArrayList<>();
        var originalx = source.x();
        var originaly = source.y();
        //right
        var OtherPiece = 0;
        for (int i=originalx+1; i<game.getConfiguration().getSize(); i++){
            if (game.getPiece(i,originaly) != null){
                OtherPiece += 1;
            }
            if (OtherPiece == 0){
                AvailableMove.add(new Move(source, new Place(i, originaly)));
            }else if(OtherPiece == 1){
                continue;
            }else if(OtherPiece == 2){
                if (game.getPiece(i,originaly) != null && !game.getPiece(i,originaly).getPlayer().equals(game.getCurrentPlayer())){
                    AvailableMove.add(new Move(source, new Place(i, originaly)));
                }
                break;
            }else{
                break;
            }
        }
        //left
        OtherPiece = 0;
        for (int i=originalx-1; i>=0; i--){
            if (game.getPiece(i,originaly) != null){
                OtherPiece += 1;
            }
            if (OtherPiece == 0){
                AvailableMove.add(new Move(source, new Place(i, originaly)));
            }else if(OtherPiece == 1){
                continue;
            }else if(OtherPiece == 2){
                if (game.getPiece(i,originaly) != null && !game.getPiece(i,originaly).getPlayer().equals(game.getCurrentPlayer())){
                    AvailableMove.add(new Move(source, new Place(i, originaly)));
                }
                break;
            }else{
                break;
            }
        }
        //up
        OtherPiece = 0;
        for (int i=originaly+1; i<game.getConfiguration().getSize(); i++){
            if (game.getPiece(originalx,i) != null){
                OtherPiece += 1;
            }
            if (OtherPiece == 0){
                AvailableMove.add(new Move(source, new Place(originalx,i)));
            }else if(OtherPiece == 1){
                continue;
            }else if(OtherPiece == 2){
                if (game.getPiece(originalx,i) != null && !game.getPiece(originalx,i).getPlayer().equals(game.getCurrentPlayer())){
                    AvailableMove.add(new Move(source, new Place(originalx,i)));
                }
                break;
            }else{
                break;
            }
        }
        //down
        OtherPiece = 0;
        for (int i=originaly-1; i>=0; i--){
            if (game.getPiece(originalx,i) != null){
                OtherPiece += 1;
            }
            if (OtherPiece == 0){
                AvailableMove.add(new Move(source, new Place(originalx,i)));
            }else if(OtherPiece == 1){
                continue;
            }else if(OtherPiece == 2){
                if (game.getPiece(originalx,i) != null && !game.getPiece(originalx,i).getPlayer().equals(game.getCurrentPlayer())){
                    AvailableMove.add(new Move(source, new Place(originalx,i)));
                }
                break;
            }else{
                break;
            }
        }
        return AvailableMove.toArray(new Move[AvailableMove.size()]);
    }
}
