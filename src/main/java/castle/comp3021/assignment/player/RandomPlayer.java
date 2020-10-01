package castle.comp3021.assignment.player;

import castle.comp3021.assignment.piece.Knight;
import castle.comp3021.assignment.protocol.Game;
import castle.comp3021.assignment.protocol.Color;
import castle.comp3021.assignment.protocol.Move;
import castle.comp3021.assignment.protocol.Player;
import org.jetbrains.annotations.NotNull;

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
            var randomInt = (int) (Math.random() * (availableMoves.length));
            Move tempchosenMove = availableMoves[randomInt];

            var inputsourceX = tempchosenMove.getSource().x();
            var inputsourceY = tempchosenMove.getSource().y();
            var inputdesX = tempchosenMove.getDestination().x();
            var inputdesY = tempchosenMove.getDestination().y();

            if (inputsourceX < 0
                    || inputsourceY < 0
                    || inputsourceX >= game.getConfiguration().getSize()
                    || inputsourceY >= game.getConfiguration().getSize()
                    || inputdesX < 0
                    || inputdesY < 0
                    || inputdesX >= game.getConfiguration().getSize()
                    || inputdesY >= game.getConfiguration().getSize()) {
                System.out.println("[Invalid Move]: place is out of boundary of gameboard");
                continue;
            }
            if (game.getPiece(inputdesX, inputdesY) != null) {
                if (game.getPiece(inputdesX, inputdesY).getPlayer()
                        .equals(game.getPiece(inputsourceX, inputsourceY).getPlayer())) {
                    System.out.println("[Invalid Move]: piece cannot be captured by another piece belonging to the same player");
                    continue;
                }
            }

            if (game.getPiece(inputsourceX, inputsourceY) == null) {
                System.out.println("[Invalid Move]: No piece at s(" + inputsourceX + ", " + inputsourceY + ")");
                continue;
            } else if (game.getPiece(inputsourceX, inputsourceY) instanceof Knight) {
                var xShift = inputdesX - inputsourceX;
                var yShift = inputdesY - inputsourceY;
                if (!(xShift == 1 && yShift == 2)
                        && !(xShift == -1 && yShift == 2)
                        && !(xShift == 2 && yShift == 1)
                        && !(xShift == -2 && yShift == 1)
                        && !(xShift == 2 && yShift == -1)
                        && !(xShift == -2 && yShift == -1)
                        && !(xShift == 1 && yShift == -2)
                        && !(xShift == -1 && yShift == -2)) {
                    System.out.println("[Invalid Move]: knight move rule is violated");
                    continue;
                }
            }

            chosenMove = tempchosenMove;
        }

        return chosenMove;
    }
}
