package game.board.copy;

import java.util.ArrayList;
import java.util.List;

import game.board.BoardPiece;
import game.board.Move;
import game.player.Occupant;

/**
 * Represents a copy of current board state, used by the minimax algorithm to prevent modifying the "real" board, or the current game state.
 */
public class BoardStateCopy {
    private BoardPieceCopy[][] pieces;

    /**
     * All the available (unfilled) pieces (moves) on the board, created in the constructor
     */
    private List<Move> freeMoves;

    public BoardPieceCopy[][] getPieces() {
        return pieces;
    }

    /**
     * Get a piece on the specified row and column
     * @param row Row index of the piece
     * @param column Column index of the piece
     * @return Piece at the specified row and column
     */
    public BoardPieceCopy getPiece(int row, int column) {
        return pieces[row][column];
    }
    
    /**
     * Get all the free moves stored in this board state copy.
     * @return Currently available moves to make
     */
    public List<Move> getMoves() {
        return freeMoves;
    }

    /**
     * Create a copy from a board state
     * @param original Board from which the state will be copied from
     */
    public BoardStateCopy(BoardPiece[][] original) {
        freeMoves = new ArrayList<>();
        initialize(original);
    }

    /**
     * Initialize the copy with the original board
     * @param original Original board state data
     */
    private void initialize(BoardPiece[][] original) {
        int rows = original.length;
        int columns = original[0].length;
        pieces = new BoardPieceCopy[rows][columns];
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < columns; c++) {
                BoardPiece piece = original[r][c];
                if (piece.getOccupant() == Occupant.NONE) {
                    freeMoves.add(new Move(r, c)); // Piece wasn't occupied, add it to free moves
                }
                pieces[r][c] = new BoardPieceCopy(piece);
            }
        }
    }
}