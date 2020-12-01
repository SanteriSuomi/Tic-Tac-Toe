package game.board;

import java.awt.Color;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.SwingWorker;

import game.dialogs.EndingDialog;
import game.main.Game;
import game.player.Occupant;
import game.utils.Utilities;

/**
 * Represents a "real" piece on the board (as opposed to the BoardPieceCopy). Player and bot moves are also started from here, when the player presses a button.
 */
public class BoardPiece implements Piece {
    /**
     * X mark image file location in local resources
     */
    @SuppressWarnings("java:S1075")
    public static final String LOCAL_X_MARK_PATH = "/images/x.png";
    /**
     * O mark image location in local resources
     */
    @SuppressWarnings("java:S1075")
    public static final String LOCAL_O_MARK_PATH = "/images/o.png";

    private Game game;
    private JButton button;
    private Occupant occupant;
    private int row;
    private int column;

    public JButton getButton() {
        return button;
    }

    /**
     * Get the current occupant of this piece
     */
    public Occupant getOccupant() {
        return occupant;
    }

    /**
     * Set the occupant on this piece and update the image
     * @param occupant Occupant to update with
     */
    public void setOccupant(Occupant occupant) {
        this.occupant = occupant;
        if (occupant == Occupant.HUMAN) {
            setMark(LOCAL_X_MARK_PATH);
        } else if (occupant == Occupant.BOT) {
            setMark(LOCAL_O_MARK_PATH);
        }
    }

    /**
     * Set the mark of this piece.
     * @param imgPath Path of the image for the mark
     */
    private void setMark(String imgPath) {
        button.setIcon(new ImageIcon(Utilities.getImage(imgPath, button.getWidth(), button.getHeight(), this)));
    }

    public int getRow() {
        return row;
    }   

    public int getColumn() {
        return column;
    }

    /**
     * Create a new piece for the board
     * @param row Row index of the piece
     * @param column Column index of the piece
     * @param game Main game instance
     */
    public BoardPiece(int row, int column, Game game) {
        this.game = game;
        this.row = row;
        this.column = column;
        occupant = Occupant.NONE; // Piece has no occupant in the beginning
        initializeUI();
    }

    /**
     * Create the UI for this piece
     */
    private void initializeUI() {
        button = new JButton();
        button.setBackground(Color.white);
        button.setFocusable(false);
        button.addActionListener(event -> onPieceButtonPressed());
    }

    /**
     * When game is being played, and the human player clicks this piece's button, this gets called
     */
    private void onPieceButtonPressed() {
        performPlayerMoves();
    }

    /**
     * Move processes are started during this method (human move first, then bot (ai) move)
     */
    private void performPlayerMoves() {
        if (!game.getCanPlaceMarks() || occupant != Occupant.NONE) return; // Game is not being played or piece is already occupied
        game.getBoard().updateNumberOfPiecesPlaced(); // Update the amount of pieces placed on the board
        int humanResult = game.getHuman().move(this); // Player move first (only thing it does it activates this current piece)
        if (humanResult == 1) { // If human move resulted in a win
            game.increaseAndUpdateHumanScore();
            new EndingDialog(game, Occupant.HUMAN);
            return;
        }
        botMove(humanResult); // Begin bot move
    }

    /**
     * Start bot move processing task
     * @param humanResult Result of the already made human move
     */
    private void botMove(int humanResult) {
        game.setCanPlaceMarks(false); // Disable making new moves during bot move
        game.setBotMovingText(true); // Enable bot move processing alert text
        new SwingWorker<Integer, Object>() { // Start a new SwingWorker to process bot move on another thread
            /**
             * Process move in a background thread to avoid blocking UI thread
             */
            @Override
            public Integer doInBackground() {
                return game.getBot().move(null);
            }

            /**
             * Called when the background thread is done
             */
            @Override
            protected void done() {
                int botResult = -1;
                try {
                    botResult = get(); // Get the move result from the background thread
                } catch (Exception e) {
                    // Ignore
                }
                finishMoves(humanResult, botResult);
            }

            /**
             * Finish the moves
             * @param humanResult Human result
             * @param botResult Bot result
             */
            private void finishMoves(int humanResult, int botResult) {
                game.setBotMovingText(false); // Disable bot move processing alert text
                if (botResult == 1) { // If bot wins
                    game.increaseAndUpdateBotScore();
                    new EndingDialog(game, Occupant.BOT);
                }
                if (humanResult == 0 || botResult == 0) { // Tie
                    game.increaseAndUpdateTieScore();
                    new EndingDialog(game, Occupant.NONE);
                }
                game.setCanPlaceMarks(true);
            }
        }.execute();
    }
}