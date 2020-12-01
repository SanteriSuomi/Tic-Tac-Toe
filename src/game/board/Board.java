package game.board;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import game.main.Game;
import game.player.Occupant;

/**
 * Represents the "real" game board and its' UI.
 */
public class Board {
    private JPanel panel;
    /**
     * Matrix of all pieces on the board
     */
    private BoardPiece[][] pieces; 

    /**
     * Max amount of pieces that can be placed (max = row * column)
     */
    private int maxPiecesPlaced;

    /**
     * Amount of pieces currently placed on the board.
     */
    private int piecesPlaced;

    public JPanel getPanel() {
        return panel;
    }

    public BoardPiece[][] getPieces() {
        return pieces;
    }

    public int getRowLength() {
        return pieces.length;
    }

    public int getColumnLength() {
        return pieces[0].length;
    }

    /**
     * Is the board full of pieces?
     * @return True or false
     */
    public boolean isBoardFull() {
        return piecesPlaced >= maxPiecesPlaced;
    }

    /**
     * Update the current number of pieces placed on the board
     */
    public void updateNumberOfPiecesPlaced() {
        piecesPlaced += 2; // Update by 2 to account for both human and bot moves, as they happen consecutively
    }

    /**
     * Set piece occupant at specified index
     * @param row Row index of the piece
     * @param column Column index of the piece
     * @param occupant Occupant to set this piece to
     * @return The piece object of whose occupant was just set
     */
    public BoardPiece setPieceOccupant(int row, int column, Occupant occupant) {
        pieces[row][column].setOccupant(occupant);
        return pieces[row][column];
    }

    /**
     * Create a new board instance with specified sizes, row and column
     * @param row Row size of the board
     * @param column Column size of the board
     * @param game Main game instance
     */
    public Board(int row, int column, Game game) {
        panel = new JPanel();
        initializeUI(row, column, game);
    }

    /**
     * Create the board UI
     * @param row Row size of the board
     * @param column Column size of the board
     * @param game Main game instance
     */
    private void initializeUI(int row, int column, Game game) {
        panel.setPreferredSize(new Dimension(500, 500));
        panel.setBackground(Color.black);
        panel.setBorder(new EmptyBorder(4, 4, 4, 4));
        createPiecesGrid(row, column);
        setPieces(row, column, game);
    }

    /**
     * Create the board piece grid UI
     * @param row Number of rows
     * @param column Number of columns
     */
    private void createPiecesGrid(int row, int column) {
        GridLayout gl = new GridLayout(row, column);
        gl.setHgap(4); 
        gl.setVgap(4);
        panel.setLayout(gl);
    }

    /**
     * Instantiate the piece instances on the created grid, and store them in the pieces matrix
     * @param row Row size of the board
     * @param column Column size of the board
     * @param game Game instance
     */
    private void setPieces(int row, int column, Game game) {
        maxPiecesPlaced = row * column; 
        pieces = new BoardPiece[row][column];
        for (int r = 0; r < row; r++) {
            for (int c = 0; c < column; c++) {
                BoardPiece p = new BoardPiece(r, c, game);
                pieces[r][c] = p;
                panel.add(p.getButton()); // Add to the board UI panel
            }
        }
    }
}