package game.board;

import game.player.Occupant;

/**
 * Represents a piece on a board that can supply a row, a column and the current occupant of the piece.
 */
public interface Piece {
    public int getRow();
    public int getColumn();
    public Occupant getOccupant();
}