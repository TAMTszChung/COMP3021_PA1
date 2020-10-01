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
        ArrayList<Move> AvailableMove = new ArrayList<>();
        //checking horse leg condition to here
        if (game.getPiece(source.x(),source.y()+1) == null){
            AvailableMove.add(new Move(source, new Place(source.x()+1, source.y()+2)));
            AvailableMove.add(new Move(source, new Place(source.x()-1, source.y()+2)));
        }
        if (game.getPiece(source.x(),source.y()-1) == null){
            AvailableMove.add(new Move(source, new Place(source.x()+1, source.y()-2)));
            AvailableMove.add(new Move(source, new Place(source.x()-1, source.y()-2)));
        }
        if (game.getPiece(source.x()+1,source.y()) == null){
            AvailableMove.add(new Move(source, new Place(source.x()+2, source.y()+1)));
            AvailableMove.add(new Move(source, new Place(source.x()+2, source.y()-1)));
        }
        if (game.getPiece(source.x()-1,source.y()+1) == null){
            AvailableMove.add(new Move(source, new Place(source.x()-2, source.y()+1)));
            AvailableMove.add(new Move(source, new Place(source.x()-2, source.y()-1)));
        }

        for (int i=AvailableMove.size()-1; i>=0;i--){
            var destinationx = AvailableMove.get(i).getDestination().x();
            var destinationy = AvailableMove.get(i).getDestination().y();

            if (destinationx<0|| destinationy<0
                    || destinationx>=game.getConfiguration().getSize() || destinationy>=game.getConfiguration().getSize()){
                AvailableMove.remove(i);
                continue;
            }
            if (game.getPiece(destinationx,destinationy) != null && game.getPiece(destinationx,destinationy).getPlayer().equals(game.getCurrentPlayer())){
                AvailableMove.remove(i);
                continue;
            }
        }

        return AvailableMove.toArray(new Move[AvailableMove.size()]);
    }
}
