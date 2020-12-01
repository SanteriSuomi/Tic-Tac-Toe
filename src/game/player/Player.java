package game.player;

import game.board.BoardPiece;

/**
 * Represents a player of the game that can make a move.
 */
public interface Player {
    public int move(BoardPiece piece);
}