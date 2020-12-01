package game.dialogs;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

import game.main.Game;

/**
 * A popup UI dialog that is resposible for getting necessary settings from the user and then creating a new game.
 */
public class NewGameDialog {
    private JFrame frame;
    private Game game;

    private JSlider rowSizeSlider;
    private JSlider columnSizeSlider;
    private JSlider winningMarksSlider;
    private JSlider aiAccuracySlider;

    /**
     * Create a new NewGameDialog popup.
     * @param game Main game instance
     */
    public NewGameDialog(Game game) {
        this.game = game;
        initializeUI(game);
    }

    /**
     * All initial UI elements are created here.
     * @param game Main game instance
     */
    private void initializeUI(Game game) {
        frame = new JFrame();
        frame.setTitle("New Game");
        frame.setSize(new Dimension(265, 440));
        frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
        frame.setLocationRelativeTo(game.getFrame());
        frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        frame.setResizable(false);
        createSizeSliders();
        createWinningMarksSlider();
        createAiAccuracySlider();
        createConfirmationButtonsPanel();
        frame.setVisible(true);
    }

    /**
     * Create the sliders necessary for settings the board size.
     */
    private void createSizeSliders() {
        JLabel boardSizeLabelRow = new JLabel();
        boardSizeLabelRow.setAlignmentX(Component.CENTER_ALIGNMENT);
        boardSizeLabelRow.setBorder(new EmptyBorder(3, 3, 3, 3));
        boardSizeLabelRow.setFont(new Font(null, Font.BOLD, 15));
        boardSizeLabelRow.setText("Rows");
        frame.add(boardSizeLabelRow);
        rowSizeSlider = new JSlider(SwingConstants.HORIZONTAL, Game.MIN_BOARD_SIZE, Game.MAX_BOARD_SIZE, Game.MIN_BOARD_SIZE);
        rowSizeSlider.addChangeListener(event -> onSizeSliderValueChanged());
        rowSizeSlider.setBorder(new EmptyBorder(5, 5, 5, 5));  
        rowSizeSlider.setMajorTickSpacing(1);  
        rowSizeSlider.setPaintLabels(true);  
        frame.add(rowSizeSlider);
        JLabel boardSizeLabelColumn = new JLabel();
        boardSizeLabelColumn.setAlignmentX(Component.CENTER_ALIGNMENT);
        boardSizeLabelColumn.setBorder(new EmptyBorder(3, 3, 3, 3));
        boardSizeLabelColumn.setFont(new Font(null, Font.BOLD, 15));
        boardSizeLabelColumn.setText("Columns");
        frame.add(boardSizeLabelColumn);
        columnSizeSlider = new JSlider(SwingConstants.HORIZONTAL, Game.MIN_BOARD_SIZE, Game.MAX_BOARD_SIZE, Game.MIN_BOARD_SIZE);
        columnSizeSlider.addChangeListener(event -> onSizeSliderValueChanged());
        columnSizeSlider.setBorder(new EmptyBorder(5, 5, 5, 5));  
        columnSizeSlider.setMajorTickSpacing(1);  
        columnSizeSlider.setPaintLabels(true);  
        frame.add(columnSizeSlider);
    }

    /**
     * Create a slider for the winning marks (win length) slider, used for setting the consecutive marks to win.
     */
    private void createWinningMarksSlider() {
        JLabel winningMarksLabel = new JLabel();
        winningMarksLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        winningMarksLabel.setBorder(new EmptyBorder(3, 3, 3, 3));
        winningMarksLabel.setFont(new Font(null, Font.BOLD, 15));
        winningMarksLabel.setText("Winning Marks");
        frame.add(winningMarksLabel);
        winningMarksSlider = new JSlider(SwingConstants.HORIZONTAL, Game.MIN_BOARD_SIZE, Game.MIN_BOARD_SIZE, Game.MIN_BOARD_SIZE);
        winningMarksSlider.setBorder(new EmptyBorder(5, 5, 5, 5));  
        winningMarksSlider.setMajorTickSpacing(1);  
        winningMarksSlider.setPaintLabels(true);  
        frame.add(winningMarksSlider);
    }

    /**
     * Create a slider for setting the AI accuracy (depth of minimax).
     */
    private void createAiAccuracySlider() {
        JLabel aiAccuracyLabel = new JLabel();
        aiAccuracyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        aiAccuracyLabel.setBorder(new EmptyBorder(3, 3, 3, 3));
        aiAccuracyLabel.setFont(new Font(null, Font.BOLD, 15));
        aiAccuracyLabel.setText("AI Accuracy");
        frame.add(aiAccuracyLabel);
        aiAccuracySlider = new JSlider(SwingConstants.HORIZONTAL, Game.MIN_AI_ACCURACY, Game.MAX_AI_ACCURACY, Game.MIN_AI_ACCURACY);
        aiAccuracySlider.setBorder(new EmptyBorder(5, 5, 5, 5));  
        aiAccuracySlider.setMajorTickSpacing(1);  
        aiAccuracySlider.setPaintLabels(true);  
        frame.add(aiAccuracySlider);
        JTextArea aiAccuracyWarning = new JTextArea();
        aiAccuracyWarning.setEditable(false);
        aiAccuracyWarning.setLineWrap(true);
        aiAccuracyWarning.setWrapStyleWord(true);
        aiAccuracyWarning.setAlignmentX(Component.CENTER_ALIGNMENT);
        aiAccuracyWarning.setBorder(new EmptyBorder(4, 4, 4, 4));
        aiAccuracyWarning.setForeground(Color.RED);
        aiAccuracyWarning.setFont(new Font(null, Font.BOLD, 12));
        aiAccuracyWarning.setText("WARNING! With big a board size and a high accuracy, it might take a long time for the enemy bot to finish processing its' move." 
                                + " Prefer small size with high accuracy and vice-versa.");
        frame.add(aiAccuracyWarning);
    }

    /**
     * Create buttons for canceling or confirming current settings.
     */
    private void createConfirmationButtonsPanel() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBorder(new EmptyBorder(3, 3, 3, 3));
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        JButton confirmButton = new JButton("Confirm");
        confirmButton.addActionListener(event -> onConfirmButtonPressed());
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(event -> onCancelButtonPressed());
        buttonPanel.add(confirmButton);
        buttonPanel.add(cancelButton);
        frame.add(buttonPanel);
    }

    /**
     * Called when either of the size sliders had their values changed.
     */
    private void onSizeSliderValueChanged() {
        updateWinningMarksSlider();
    }

    /**
     * Keeps winning marks slider maximum value dynamically updated depending on the values in the row and column size sliders. 
     */
    private void updateWinningMarksSlider() {
        int val = Math.max(rowSizeSlider.getValue(), columnSizeSlider.getValue());
        if (val > Game.MAX_WINNING_MARKS) {
            val = Game.MAX_WINNING_MARKS;
        }
        winningMarksSlider.setMaximum(val);
    }

    /**
     * Called when the confirm button is pressed.
     */
    private void onConfirmButtonPressed() {
        createNewGame();
        closeNewGameDialog();
    }

    /**
     * Create and activate a new game.
     */
    private void createNewGame() {
        game.setCanPlaceMarks(true); // Enable mark placing for the player
        Game.setWinLength(winningMarksSlider.getValue()); // Set winning marks length
        Game.setAiAccuracy(aiAccuracySlider.getValue()); // Set ai accuracy
        game.createNewGameState(rowSizeSlider.getValue(), columnSizeSlider.getValue()); // Create a new game from current row and column slider values
    }

    /**
     * Called when the cancel button is pressed.
     */
    private void onCancelButtonPressed() {
        closeNewGameDialog();
    }

    /**
     * Dispose this dialog and re-activate the main game UI.
     */
    private void closeNewGameDialog() {
        game.getFrame().setEnabled(true);
        frame.dispose();
    }
}