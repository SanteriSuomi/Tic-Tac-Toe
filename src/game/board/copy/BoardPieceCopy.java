package game.board.copy;

import game.board.BoardPiece;
import game.board.Piece;
import game.player.Occupant;

/**
 * Represents a copy of current specified board piece, used by the minimax algorithm to prevent modifying the "real" pieces, or the current game state.
 */
public class BoardPieceCopy implements Piece {
    private Occupant occupant;
    private int row;
    private int column;

    public Occupant getOccupant() {
        return occupant;
    }

    public void setOccupant(Occupant value) {
        this.occupant = value;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    /**
     * Create a new fake board piece using a "real" piece
     * @param piece Original piece from which values get copied from
     */
    public BoardPieceCopy(BoardPiece piece) {
        this.occupant = piece.getOccupant();
        this.row = piece.getRow();
        this.column = piece.getColumn();
    }
}