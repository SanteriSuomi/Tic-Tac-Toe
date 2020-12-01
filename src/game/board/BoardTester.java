package game.board;

import game.board.copy.BoardStateCopy;
import game.main.Game;
import game.player.Occupant;

/**
 * BoardTester contains different methods for testing the board for move results, 
 * e.g if there is a winner at a specific piece or row/column, etc. Also has methods used by the minimax algorithm.
 */
public class BoardTester {
    private Game game;

    /**
     * Create a new instance of BoardTester
     * @param game Main game instance
     */
    public BoardTester(Game game) {
        this.game = game;
    }

    /**
     * Get the move result for a particular piece
     * @param piece Piece to check
     * @return Move result for this piece (0 = tie, 1 = win, -1 = no win and no tie)
     */
    public int getMoveResult(Piece piece) {
        if (game.getBoard().isBoardFull()) return 0; // If the board has no available pieces, return a tie automatically
        if (hasWinner(game.getBoard().getPieces(), piece.getOccupant(), piece.getRow(), piece.getColumn())) {
            return 1;
        }
        return -1;
    }

    /**
     * Get the move result for a piece at the specified index
     * @param occupant Occupant to get the move result for
     * @param row Row index
     * @param column Column index
     * @return Move result (0 = tie, 1 = win, -1 = no win and no tie)
     */
    public int getMoveResult(Occupant occupant, int row, int column) {
        if (game.getBoard().isBoardFull()) return 0; // If the board has no available pieces, return a tie automatically
        if (hasWinner(game.getBoard().getPieces(), occupant, row, column)) {
            return 1;
        }
        return -1;
    }

    /**
     * Check if there is a winner, tie or no winner on the current state of the board (entire board) and return the result (used by the minimax algorithm)
     * @param state Copy of the state of the board
     * @param depth Depth of current minimax caller
     * @return 0 for tie0, 100 for bot win (maximizer), -100 for human win (minimizer) and -1000 for terminal state (no winners, no ties)
     * Negate the depth from this return value, because we want to attempt to discourage the algorithm from using moves at a large depth
     */
    public int getMoveResultForMinimax(BoardStateCopy state, int depth) {
        int openSpots = 0; // Keep track of the open spots
        for (Move move : state.getMoves()) {
            Piece piece = state.getPiece(move.getRow(), move.getColumn());
            if (piece.getOccupant() == Occupant.NONE) {
                openSpots++; 
            } else {
                if (piece.getOccupant() == Occupant.BOT 
                    && hasWinner(state.getPieces(), Occupant.BOT, move.getRow(), move.getColumn())) {
                    return 100 - depth;
                } else if (hasWinner(state.getPieces(), Occupant.HUMAN, move.getRow(), move.getColumn())) {
                    return -100 + depth;
                }
            }
        }
        if (openSpots == 0) { // No open spots and no winner, must be a tie
            return 0;
        } 
        return -1000; // Terminal state (no winners, no ties)
    }

    /**
     * Fast heuristic for estimating a moves' worth, used when depth is too high. Checks every row, column and both diagonals
     * @param state Copy of the state of the board
     * @param depth Depth of current minimax caller
     * @return 100 if bot has filled any row, column or diagonal, -100 if human has filled any row, column or diagonal, 0 if neither of these are true
     * Negate depth from this return value, because we want to attempt to discourage the algorithm from using moves from large depth
     */
    public int getHeuristicResultForMinimax(BoardStateCopy state, int depth) {
        Occupant row = isAnyRowFilledWithOnlyOne(state.getPieces());
        Occupant column = isAnyColumnFilledWithOnlyOne(state.getPieces());
        Occupant diagonal = isAnyDiagonalFilledWithOnlyOne(state.getPieces());
        if (row == Occupant.BOT || column == Occupant.BOT || diagonal == Occupant.BOT) {
            return 100 - depth;
        } else if (row == Occupant.HUMAN || column == Occupant.HUMAN || diagonal == Occupant.HUMAN) {
            return -100 + depth;
        }
        return 0;
    }

    /**
     * Has only one occupant filled ANY row on the board?
     * @param pieces Pieces (board) to check
     * @return Occupant who has filled only one, if none then Occupant.NONE
     */
    private Occupant isAnyRowFilledWithOnlyOne(Piece[][] pieces) {
        int c = 0;
        for (int r = 0; r < pieces.length; r++) {
            boolean hasOnlyOne = true;
            Occupant occupant = pieces[r][c].getOccupant();
            for (c = 1; c < pieces[0].length; c++) {
                Piece piece = pieces[r][c];
                if (occupant == Occupant.NONE && piece.getOccupant() != Occupant.NONE) {
                    occupant = piece.getOccupant();
                } else if (occupant != Occupant.NONE && piece.getOccupant() != occupant) {
                    hasOnlyOne = false;
                    break;
                }
            }
            if (hasOnlyOne) return occupant;
        }
        return Occupant.NONE;
    }

    /**
     * Has only one occupant filled ANY column on the pieces?
     * @param pieces Pieces (board) to check
     * @return Occupant who has filled only one, if none then Occupant.NONE
     */
    private Occupant isAnyColumnFilledWithOnlyOne(Piece[][] pieces) {
        int r = 0;
        for (int c = 0; c < pieces[0].length; c++) {
            boolean hasOnlyOne = true;
            Occupant occupant = pieces[r][c].getOccupant();
            for (r = 1; r < pieces.length; r++) {
                Piece currPiece = pieces[r][c];
                if (occupant == Occupant.NONE && currPiece.getOccupant() != Occupant.NONE) {
                    occupant = currPiece.getOccupant();
                } else if (occupant != Occupant.NONE && currPiece.getOccupant() != occupant) {
                    hasOnlyOne = false;
                    break;
                }
            }
            if (hasOnlyOne) return occupant;
        }
        return Occupant.NONE;
    }

    /**
     * Has only one occupant filled ANY diagonal on the pieces?
     * @param pieces Pieces (board) to check
     * @return Occupant who has filled only one, if none then Occupant.NONE
     */
    @SuppressWarnings("java:S3776") // Suppress method complexity warning
    private Occupant isAnyDiagonalFilledWithOnlyOne(Piece[][] pieces) {
        int row = 1;
        int column = 1;
        boolean hasOnlyOne = true;
        Occupant occupant = pieces[row - 1][column - 1].getOccupant();
        // Top left to bottom right check
        while (row < pieces.length && column < pieces[0].length) {
            Piece currPiece = pieces[row][column];
            if (occupant == Occupant.NONE && currPiece.getOccupant() != Occupant.NONE) {
                occupant = currPiece.getOccupant();
            } else if (occupant != Occupant.NONE && currPiece.getOccupant() != occupant) {
                hasOnlyOne = false;
                break;
            }
            row++;
            column++;
        }
        if (hasOnlyOne) return occupant; // Return premately if any was filled already
        row = pieces.length - 2;
        column = pieces[0].length - 2;
        hasOnlyOne = true;
        occupant = pieces[row - 1][column - 1].getOccupant();
        // Top right to bottom left check
        while (row >= 0 && column < pieces[0].length) {
            Piece currPiece = pieces[row][column];
            if (occupant == Occupant.NONE && currPiece.getOccupant() != Occupant.NONE) {
                occupant = currPiece.getOccupant();
            } else if (occupant != Occupant.NONE && currPiece.getOccupant() != occupant) {
                hasOnlyOne = false;
                break;
            }
            row--;
            column++;
        }
        if (hasOnlyOne) return occupant;  
        return Occupant.NONE;
    }

    /**
     * Check all possible winning combinations at specified index, for the given occupant
     * @param pieces Pieces (board) to check
     * @param occupant Occupant to check for (return value will correspond to this)
     * @param row Row index
     * @param column Column index
     * @return True if has winner on the specified index on horizontals/verticals/diagonals, else false
     */
    private boolean hasWinner(Piece[][] pieces, Occupant occupant, int row, int column) {
        return checkHorizontal(pieces, occupant, row, column, 0)
            || checkVertical(pieces, occupant, row, column, 0)
            || checkTopLeftToBottomRightDiagonal(pieces, occupant, row, column, 0)
            || checkTopRightToBottomLeftDiagonal(pieces, occupant, row, column, 0);
    }

    /**
     * Check at the given index horizontally for possible winning combinations, for the given occupant
     * @param pieces Pieces (board) to check
     * @param occupant Occupant to check for (return value will correspond to this)
     * @param row Row index
     * @param column Column index
     * @param amount Amount of consecutive marks currently, always call with 0!
     * @return True if had winner else false
     */
    private boolean checkHorizontal(Piece[][] pieces, Occupant occupant, int row, int column, int amount) {
        int origColumn = column;
        // From piece to right
        while (row >= 0 && row < pieces.length && column >= 0 && column < pieces[0].length) {
            if (pieces[row][column++].getOccupant() == occupant) {
                amount++;
            } else {
                break;
            }
        }
        // From piece to left
        column = origColumn - 1;
        while (row >= 0 && row < pieces.length && column >= 0 && column < pieces[0].length) {
            if (pieces[row][column--].getOccupant() == occupant) {
                amount++;
            } else {
                break;
            }
        }
        return amount >= Game.getWinLength();
    }

    /**
     * Check at the given index vertically for possible winning combinations, for the given occupant
     * @param pieces Pieces (board) to check
     * @param occupant Occupant to check for (return value will correspond to this)
     * @param row Row index
     * @param column Column index
     * @param amount Amount of consecutive marks currently, always call with 0!
     * @return True if had winner else false
     */
    private boolean checkVertical(Piece[][] pieces, Occupant occupant, int row, int column, int amount) {
        int origRow = row;
        // From piece to down
        while (row >= 0 && row < pieces.length && column >= 0 && column < pieces[0].length) {
            if (pieces[row++][column].getOccupant() == occupant) {
                amount++;
            } else {
                break;
            }
        }
        // From piece to up
        row = origRow - 1;
        while (row >= 0 && row < pieces.length && column >= 0 && column < pieces[0].length) {
            if (pieces[row--][column].getOccupant() == occupant) {
                amount++;
            } else {
                break;
            }
        }
        return amount >= Game.getWinLength();
    }

    /**
     * Check at the given index diagonally (top left to bottom right) for possible winning combinations, for the given occupant
     * @param pieces Pieces (board) to check
     * @param occupant Occupant to check for (return value will correspond to this)
     * @param row Row index
     * @param column Column index
     * @param amount Amount of consecutive marks currently, always call with 0!
     * @return True if had winner else false
     */
    private boolean checkTopLeftToBottomRightDiagonal(Piece[][] pieces, Occupant occupant, int row, int column, int amount) {
        int origRow = row;
        int origColumn = column;
        // From piece to top left
        while (row >= 0 && row < pieces.length && column >= 0 && column < pieces[0].length) {
            if (pieces[row--][column--].getOccupant() == occupant) {
                amount++;
            } else {
                break;
            }
        }
        // From piece to bottom right
        row = origRow + 1;
        column = origColumn + 1;
        while (row >= 0 && row < pieces.length && column >= 0 && column < pieces[0].length) {
            if (pieces[row++][column++].getOccupant() == occupant) {
                amount++;
            } else {
                break;
            }
        }
        return amount >= Game.getWinLength();
    }

    /**
     * Check at the given index diagonally (top right to bottom left) for possible winning combinations, for the given occupant
     * @param pieces Pieces (board) to check
     * @param occupant Occupant to check for (return value will correspond to this)
     * @param row Row index
     * @param column Column index
     * @param amount Amount of consecutive marks currently, always call with 0!
     * @return True if had winner else false
     */
    private boolean checkTopRightToBottomLeftDiagonal(Piece[][] pieces, Occupant occupant, int row, int column, int amount) {
        int origRow = row;
        int origColumn = column;
        // From piece to top right
        while (row >= 0 && row < pieces.length && column >= 0 && column < pieces[0].length) {
            if (pieces[row--][column++].getOccupant() == occupant) {
                amount++;
            } else {
                break;
            }
        }
        // From piece to bottom left
        row = origRow + 1;
        column = origColumn - 1;
        while (row >= 0 && row < pieces.length && column >= 0 && column < pieces[0].length) {
            if (pieces[row++][column--].getOccupant() == occupant) {
                amount++;
            } else {
                break;
            }
        }
        return amount >= Game.getWinLength();
    }
}