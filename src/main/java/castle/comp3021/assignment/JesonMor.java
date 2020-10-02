package castle.comp3021.assignment;

import castle.comp3021.assignment.piece.Archer;
import castle.comp3021.assignment.piece.Knight;
import castle.comp3021.assignment.protocol.*;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * This class extends {@link Game}, implementing the game logic of JesonMor game.
 * Student needs to implement methods in this class to make the game work.
 * Hint: make good use of methods predefined in {@link Game} to get various information to facilitate your work.
 * <p>
 * Several sample tests are provided to test your implementation of each method in the test directory.
 * Please make make sure all tests pass before submitting the assignment.
 */
public class JesonMor extends Game {
    public JesonMor(Configuration configuration) {
        super(configuration);
    }

    /**
     * Start the game
     * Players will take turns according to the order in {@link Configuration#getPlayers()} to make a move until
     * a player wins.
     * <p>
     * In the implementation, student should implement the loop letting two players take turns to move pieces.
     * The order of the players should be consistent to the order in {@link Configuration#getPlayers()}.
     * {@link Player#nextMove(Game, Move[])} should be used to retrieve the player's choice of his next move.
     * After each move, {@link Game#refreshOutput()} should be called to refresh the gameboard printed in the console.
     * <p>
     * When a winner appears, set the local variable {@code winner} so that this method can return the winner.
     *
     * @return the winner
     */
    @Override
    public Player start() {
        // reset all things
        Player winner = null;
        this.numMoves = 0;
        this.board = configuration.getInitialBoard();
        this.currentPlayer = null;
        this.refreshOutput();
        while (true) {
            // TODO student implementation starts here
            this.currentPlayer = configuration.getPlayers()[this.numMoves%2];
            Move nextmove = this.currentPlayer.nextMove(this, getAvailableMoves(currentPlayer));
            movePiece(nextmove);
            Piece currentPiece = this.getPiece(nextmove.getDestination());
            updateScore(currentPlayer, currentPiece,nextmove);
            this.numMoves += 1;
            this.refreshOutput();
            winner = getWinner(currentPlayer,currentPiece,nextmove);
            // student implementation ends here
            if (winner != null) {
                System.out.println();
                System.out.println("Congratulations! ");
                System.out.printf("Winner: %s%s%s\n", winner.getColor(), winner.getName(), Color.DEFAULT);
                return winner;
            }
        }
    }

    /**
     * Get the winner of the game. If there is no winner yet, return null;
     * This method will be called every time after a player makes a move and after
     * {@link JesonMor#updateScore(Player, Piece, Move)} is called, in order to
     * check whether any {@link Player} wins.
     * If this method returns a player (the winner), then the game will exit with the winner.
     * If this method returns null, next player will be asked to make a move.
     *
     * @param lastPlayer the last player who makes a move
     * @param lastMove   the last move made by lastPlayer
     * @param lastPiece  the last piece that is moved by the player
     * @return the winner if it exists, otherwise return null
     */
    @Override
    public Player getWinner(Player lastPlayer, Piece lastPiece, Move lastMove) {
        // TODO student implementation
        var numenemy = 0;
        for (int i=0; i<this.board.length;i++){
            for (int j=0; j<this.board[i].length; j++){
                if (this.board[i][j] == null){
                    continue;
                }else{
                    if (!this.board[i][j].getPlayer().equals(lastPlayer)){
                        numenemy += 1;
                    }
                }
            }
        }
        if (numenemy == 0){
            return lastPlayer;
        }

        Player nextplayer = configuration.getPlayers()[this.numMoves%2];
        if (getAvailableMoves(nextplayer).length == 0){
            if (lastPlayer.getScore() > nextplayer.getScore()){
                return nextplayer;
            }else if (lastPlayer.getScore() < nextplayer.getScore()){
                return lastPlayer;
            }else{
                return lastPlayer;
            }
        }

        //numMoves less than protection, cannot win
        if (this.numMoves <= this.configuration.getNumMovesProtection()){
            return null;
        }
        //win by: leaving central place
        if (lastPiece instanceof Knight && lastMove.getSource().equals(this.configuration.getCentralPlace())){
            return lastPlayer;
        }
        return null;
    }

    /**
     * Update the score of a player according to the {@link Piece} and corresponding move made by him just now.
     * This method will be called every time after a player makes a move, in order to update the corresponding score
     * of this player.
     * <p>
     * The score of a player is the cumulative score of each move he makes.
     * The score of each move is calculated with the Manhattan distance between the source and destination {@link Place}.
     * <p>
     * Student can use {@link Player#getScore()} to get the current score of a player before updating.
     * {@link Player#setScore(int)} can be used to update the score of a player.
     * <p>
     * <strong>Attention: Student should make NO assumption that the {@link Move} is valid.</strong>
     *
     * @param player the player who just makes a move
     * @param piece  the piece that is just moved
     * @param move   the move that is just made
     */
    public void updateScore(Player player, Piece piece, Move move) {
        // TODO student implementation
        var originalx = move.getSource().x();
        var originaly = move.getSource().y();
        var destinationx = move.getDestination().x();
        var destinationy = move.getDestination().y();

        var movescore = Math.abs((originalx-destinationx)) + Math.abs((originaly-destinationy));
        var newscore = player.getScore() + movescore;
        player.setScore(newscore);
    }


    /**
     * Make a move.
     * This method performs moving a {@link Piece} from source to destination {@link Place} according {@link Move} object.
     * Note that after the move, there will be no {@link Piece} in source {@link Place}.
     * <p>
     * Positions of all {@link Piece}s on the gameboard are stored in {@link JesonMor#board} field as a 2-dimension array of
     * {@link Piece} objects.
     * The x and y coordinate of a {@link Place} on the gameboard are used as index in {@link JesonMor#board}.
     * E.g. {@code board[place.x()][place.y()]}.
     * If one {@link Place} does not have a piece on it, it will be null in {@code board[place.x()][place.y()]}.
     * Student may modify elements in {@link JesonMor#board} to implement moving a {@link Piece}.
     * The {@link Move} object can be considered valid on present gameboard.
     *
     * @param move the move to make
     */
    public void movePiece(@NotNull Move move) {
        // TODO student implementation
        var originalx = move.getSource().x();
        var originaly = move.getSource().y();
        var destinationx = move.getDestination().x();
        var destinationy = move.getDestination().y();

        board[destinationx][destinationy] = board[originalx][originaly];
        board[originalx][originaly] = null;
    }

    /**
     * Get all available moves of one player.
     * This method is called when it is the {@link Player}'s turn to make a move.
     * It will iterate all {@link Piece}s belonging to the {@link Player} on board and obtain available moves of
     * each of the {@link Piece}s through method {@link Piece#getAvailableMoves(Game, Place)} of each {@link Piece}.
     * <p>
     * <strong>Attention: Student should make sure all {@link Move}s returned are valid.</strong>
     *
     * @param player the player whose available moves to get
     * @return an array of available moves
     */
    public @NotNull Move[] getAvailableMoves(Player player) {
        // TODO student implementation
        ArrayList<Move> availableMove = new ArrayList<>();
        for (int i=0; i<this.board.length;i++){
            for (int j=0; j<this.board[i].length; j++){
                if (this.board[i][j]!=null && this.board[i][j].getPlayer().equals(player)){
                    ArrayList<Move> tempMoves =
                            new ArrayList<>(Arrays.asList(this.board[i][j].getAvailableMoves(this, new Place(i,j))));
                    availableMove.addAll(tempMoves);
                }
            }
        }

        for (int x=availableMove.size()-1; x>=0; x--){
            var originalX = availableMove.get(x).getSource().x();
            var originalY = availableMove.get(x).getSource().y();
            var destinationX = availableMove.get(x).getDestination().x();
            var destinationY = availableMove.get(x).getDestination().y();

            //validate the move
            //out of boundary
            if (originalX<0
                    ||originalY<0
                    ||originalX>=this.getConfiguration().getSize()
                    ||originalY>=this.getConfiguration().getSize()
                    || destinationX<0
                    || destinationY<0
                    || destinationX>=this.getConfiguration().getSize()
                    || destinationY>=this.getConfiguration().getSize()){
                availableMove.remove(x);
                continue;
            }

            //check any piece at original location
            if (this.getPiece(originalX, originalY) == null){
                availableMove.remove(x);
                continue;
            }
            //check piece belong to player
            if (!this.getPiece(originalX, originalY).getPlayer().equals(player)){
                availableMove.remove(x);
                continue;
            }

            //check capturing own piece
            if (this.getPiece(destinationX, destinationY) != null){
                if (this.getPiece(destinationX, destinationY).getPlayer()
                        .equals(this.getPiece(originalX, originalY).getPlayer())){
                    availableMove.remove(x);
                    continue;
                }
            }

            var xShift = destinationX - originalX;
            var yShift = destinationY - originalY;
            if (this.getPiece(originalX, originalY) instanceof Knight){
                //check moving rule of Knight
                if (Math.abs(xShift) == 2 && Math.abs(yShift) == 1){
                    var legPosX = (destinationX + originalX)/2;
                    if (this.getPiece(legPosX,originalY) != null){
                        availableMove.remove(x);
                        continue;
                    }
                }else if (Math.abs(xShift) == 1 && Math.abs(yShift) == 2){
                    var legPosY = (destinationY + originalY)/2;
                    if (this.getPiece(originalX,legPosY) != null){
                        availableMove.remove(x);
                        continue;
                    }
                }else{
                    availableMove.remove(x);
                    continue;
                }
            }else if(this.getPiece(originalX, originalY) instanceof Archer){
                //check archer
                //check moving rule of archer
                if (Math.abs(xShift) > 0 && Math.abs(yShift) > 0){
                    availableMove.remove(x);
                    continue;
                }
                var numPiecebetween = 0;
                if (xShift<0 && yShift==0){
                    for (int i=originalX-1; i>destinationX; i--){
                        if (this.getPiece(i,originalY) != null){
                            numPiecebetween += 1;
                        }
                    }
                }else if(xShift>0 && yShift==0){
                    for (int i=originalX+1; i<destinationX; i++){
                        if (this.getPiece(i,originalY) != null){
                            numPiecebetween += 1;
                        }
                    }
                }else if (xShift==0 && yShift<0){
                    for (int i=originalY-1; i>destinationY; i--){
                        if (this.getPiece(originalX,i) != null){
                            numPiecebetween += 1;
                        }
                    }
                }else if (xShift==0 && yShift>0){
                    for (int i=originalY+1; i<destinationY; i++){
                        if (this.getPiece(originalX,i) != null){
                            numPiecebetween += 1;
                        }
                    }
                }else{
                    availableMove.remove(x);
                    continue;
                }

                if (numPiecebetween >= 2){
                    availableMove.remove(x);
                    continue;
                }else if (numPiecebetween == 1){
                    if (this.getPiece(destinationX,destinationY) == null){
                        availableMove.remove(x);
                        continue;
                    }else if(this.getPiece(destinationX,destinationY).getPlayer()
                            .equals(this.getPiece(originalX, originalY).getPlayer())){
                        availableMove.remove(x);
                        continue;
                    }
                }
            }
        }

        return availableMove.toArray(new Move[availableMove.size()]);
    }
}
