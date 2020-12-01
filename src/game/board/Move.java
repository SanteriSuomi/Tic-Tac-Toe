package game.board;

/**
 * Represents a move that can be made. e.g a best move given by the minimax decision maker.
 */
public class Move {
    private int row;
    private int column;

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    /**
     * Update the move with a piece (getting its' row and column sizes)
     * @param piece Piece to update this move with
     */
    public void update(Piece piece) {
        this.row = piece.getRow();
        this.column = piece.getColumn();
    }

    /**
     * Is this move a valid move valid? (row and column are both NOT -1)
     * @return True if row and column are not -1
     */
    public boolean isValid() {
        return row != -1 && column != -1;
    }

    /**
     * Constuct a new move instance
     * @param row Initial row of the move
     * @param column Initial column of the move
     */
    public Move(int row, int column) {
        this.row = row;
        this.column = column;
    }
}