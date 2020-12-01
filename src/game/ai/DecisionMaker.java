package game.ai;

import game.main.Game;
import game.board.copy.BoardPieceCopy;
import game.board.copy.BoardStateCopy;
import game.board.Move;
import game.board.BoardTester;
import game.player.Occupant;

/**
 * Decision maker is the "AI" used by the bot player. Uses the minimax algorithm (with alpha-beta pruning optimization)
 * I used mainly used the sources below to create this variation of minimax, they explain it way better than I ever could!
 * https://www.youtube.com/watch?v=trKjYdBASyQ
 * https://medium.com/swlh/optimizing-decision-making-with-the-minimax-ai-algorithm-69cce500c6d6
 * https://www.geeksforgeeks.org/minimax-algorithm-in-game-theory-set-3-tic-tac-toe-ai-finding-optimal-move/?ref=lbp
 * https://www.geeksforgeeks.org/minimax-algorithm-in-game-theory-set-4-alpha-beta-pruning/
 */
public class DecisionMaker {
    private Game game;
    private BoardTester tester;

    /**
     * Create a new "AI" decision maker instance
     * @param game Main game instance
     * @param tester Board tester to test move resuls with
     */
    public DecisionMaker(Game game, BoardTester tester) {
        this.game = game;
        this.tester = tester;
    }

    /**
     * (Attempt to) Get the best possible move for the bot (AI)
     * In reality, this is not the "best" move except on smaller boards, because of the processing power needed
     * https://stackoverflow.com/questions/2080050/how-do-we-determine-the-time-and-space-complexity-of-minmax
     * @return Optimal or sub-optimal move
     */
    public Move getBestMove() {
        BoardStateCopy state = new BoardStateCopy(game.getBoard().getPieces()); // Get a copy of the current state of the board
        Move bestMove = new Move(-1, -1); // Initialize the BestMove with invalid state at the start
        int bestScore = Integer.MIN_VALUE; // Score is the smallest possible at the beginning, gets overridden by the first move
        for (Move move : state.getMoves()) { // For all possible moves...
            BoardPieceCopy piece = state.getPiece(move.getRow(), move.getColumn()); // Get the piece of this move
            piece.setOccupant(Occupant.BOT); // Set the occupant to the bot
            int score = minimax(state, 0, false, Integer.MIN_VALUE, Integer.MAX_VALUE); // Get the result for performing this move (creating a game tree)
            piece.setOccupant(Occupant.NONE); // Reset This move, as we already got it's score
            if (score > bestScore) { // Update score and move if this move was better than the current one.
                bestScore = score;
                bestMove.update(piece);
            }
        }
        return bestMove;
    }

    /**
     * The meat and bone of the decision maker. In short it is called with current board state and it plays the game 
     * forward as long as there is no tie, win or lose, and then returns the score of the move
     * @param state Copy of the state of the game
     * @param depth Current depth of this iteration of minimax
     * @param isMaximizing Is the current call the maximizer? (bot)
     * @param alpha Alpha value (maximizer)
     * @param beta Beta value (minimizer)
     * @return Score of performing the move (in getBestMove)
     */
    private int minimax(BoardStateCopy state, int depth, boolean isMaximizing, int alpha, int beta) {
        if (depth >= Game.getAiAccuracy()) { // If we're too deep in the current game tree branch, just return the heuristic for this move
            return tester.getHeuristicResultForMinimax(state, depth);
        }
        int result = tester.getMoveResultForMinimax(state, depth); // Check if there are any results in the current branch of the game tree
        if (result != -1000) { // Other than -1000 means terminal, as in a win, lose or a tie
            return result; 
        }
        if (isMaximizing) { // Bot turn
            return getMaximizerScore(state, depth, alpha, beta);
        } else { // Player turn
            return getMinimizerScore(state, depth, alpha, beta);
        }
    }

    /**
     * Returns the score for a maximizer (bot)
     * @param state Copy of the board state
     * @param depth Depth of the current game tree
     * @param alpha Alpha value
     * @param beta Beta value
     * @return Maximizer score
     */
    private int getMaximizerScore(BoardStateCopy state, int depth, int alpha, int beta) {
        int bestScore = Integer.MIN_VALUE;
        for (Move move : state.getMoves()) {
            BoardPieceCopy piece = state.getPiece(move.getRow(), move.getColumn());
            if (piece.getOccupant() == Occupant.NONE) {
                piece.setOccupant(Occupant.BOT);
                bestScore = Math.max(bestScore, minimax(state, depth + 1, false, alpha, beta));
                piece.setOccupant(Occupant.NONE);
                alpha = Math.max(alpha, bestScore);
                if (beta <= alpha) { 
                    break;
                }
            }
        }
        return bestScore;
    }

    /**
     * Returns the score for a minimizer (player)
     * @param state Copy of the board state
     * @param depth Depth of the current game tree
     * @param alpha Alpha value
     * @param beta Beta value
     * @return Minimizer score
     */
    private int getMinimizerScore(BoardStateCopy state, int depth, int alpha, int beta) {
        int bestScore = Integer.MAX_VALUE;
        for (Move move : state.getMoves()) {
            BoardPieceCopy piece = state.getPiece(move.getRow(), move.getColumn());
            if (piece.getOccupant() == Occupant.NONE) {
                piece.setOccupant(Occupant.HUMAN);
                bestScore = Math.min(bestScore, minimax(state, depth + 1, true, alpha, beta));
                piece.setOccupant(Occupant.NONE);
                beta = Math.min(beta, bestScore);
                if (beta <= alpha) {
                    break;
                }
            }
        }
        return bestScore;
    }
}