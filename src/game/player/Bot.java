package game.player;

import game.main.Game;
import game.ai.DecisionMaker;
import game.board.BoardPiece;
import game.board.BoardTester;
import game.board.Move;

/**
 * Player object that represents the bot (AI) player.
 */
public class Bot implements Player {
    private Game game;
    private BoardTester tester;
    
    /**
     * Instance of the bot decision maker, the "AI"
     */
    private DecisionMaker ai; 

    /**
     * Create a new bot player instance. Creates a new instance of BoardTester for move testing and a new instance of minimax for decision making
     * @param game Main game instance
     */
    public Bot(Game game) {
        this.game = game;
        tester = new BoardTester(game);
        ai = new DecisionMaker(game, tester);
    }

    /**
     * Handle the bot move, first gets the best move from the decision maker after which it sets it on the main board
     * @param piece This parameter is not currently in use by the bot object!
     */
    @Override
    public int move(BoardPiece piece) {
        Move move = ai.getBestMove(); // Get the best move from the minimax decision maker algorithm
        if (move.isValid()) { // If the move is valid
            BoardPiece movePiece = game.getBoard().setPieceOccupant(move.getRow(), move.getColumn(), Occupant.BOT); // Set the given best move  on the actual game board
            return tester.getMoveResult(movePiece); // Return result
        }
        return -1; // If move wasn't valid, automatically return -1 (no result)
    }
}