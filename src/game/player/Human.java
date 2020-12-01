package game.player;

import game.main.Game;
import game.board.BoardPiece;
import game.board.BoardTester;

/**
 * Player object that represents the human player.
 */
public class Human implements Player {
    private BoardTester tester;

    /**
     * Create a new human player instance
     * @param game Human doesn't need access to the game instance
     */
    public Human(Game game) {
        tester = new BoardTester(game);
    }

    /**
    * Handle a human move on human's turn. As opposed to the bot (AI), the human just needs to set the piece directly and nothing else
    * @param piece Piece to be set
    */
    @Override
    public int move(BoardPiece piece) {
        piece.setOccupant(Occupant.HUMAN); // Set the piece mark.
        return tester.getMoveResult(piece); // Return the result of the move to using a board tester
    }
}