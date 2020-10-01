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
        ArrayList<Move> AvailableMove = new ArrayList<>();
        AvailableMove.add(new Move(source, new Place(sourceX+1, sourceY+2)));
        AvailableMove.add(new Move(source, new Place(sourceX+2, sourceY+1)));
        AvailableMove.add(new Move(source, new Place(sourceX+2, sourceY-1)));
        AvailableMove.add(new Move(source, new Place(sourceX+1, sourceY-2)));
        AvailableMove.add(new Move(source, new Place(sourceX-1, sourceY-2)));
        AvailableMove.add(new Move(source, new Place(sourceX-2, sourceY-1)));
        AvailableMove.add(new Move(source, new Place(sourceX-2, sourceY+1)));
        AvailableMove.add(new Move(source, new Place(sourceX-1, sourceY+2)));

        for (int i=AvailableMove.size()-1; i>=0;i--){
            var originalx = AvailableMove.get(i).getSource().x();
            var originaly = AvailableMove.get(i).getSource().y();
            var destinationx = AvailableMove.get(i).getDestination().x();
            var destinationy = AvailableMove.get(i).getDestination().y();

            if (destinationx<0|| destinationy<0
                    || destinationx>=game.getConfiguration().getSize() || destinationy>=game.getConfiguration().getSize()){
                AvailableMove.remove(i);
                continue;
            }
            if (game.getPiece(destinationx,destinationy) != null
                    && game.getPiece(destinationx,destinationy).getPlayer().equals(game.getCurrentPlayer())){
                AvailableMove.remove(i);
                continue;
            }
            var Xshift = AvailableMove.get(i).getDestination().x() - AvailableMove.get(i).getSource().x();
            var Yshift = AvailableMove.get(i).getDestination().y() - AvailableMove.get(i).getSource().y();
            if (Math.abs(Xshift) == 2){
                if (Xshift < 0){
                    if (game.getPiece(originalx-1,originaly) != null)
                        AvailableMove.remove(i);
                }else{
                    if (game.getPiece(originalx+1,originaly) != null)
                        AvailableMove.remove(i);
                }
            }else{
                if (Yshift < 0){
                    if (game.getPiece(originalx,originaly-1) != null)
                        AvailableMove.remove(i);
                }else{
                    if (game.getPiece(originalx,originaly+1) != null)
                        AvailableMove.remove(i);
                }
            }
        }

        return AvailableMove.toArray(new Move[AvailableMove.size()]);
    }
}
