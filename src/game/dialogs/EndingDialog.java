package game.dialogs;

import java.awt.Component;
import java.awt.Font;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import game.main.Game;
import game.player.Occupant;
import game.utils.Utilities;

/**
 * A popup UI dialog that is created when one of the players is confirmed as a winner or a tie happens.
 */
public class EndingDialog {
    private JFrame parentFrame;
    private Game game;
    private Occupant result;
    private JDialog resultDialog;

    /**
     * Create a new EndingDialog popup.
     * @param game Main game instance
     * @param result Result of the win, NONE = tie
     */
    public EndingDialog(Game game, Occupant result) {
        this.game = game;
        this.result = result;
        initializeUI();
    }

    /**
     * All initial UI elements are created here.
     */
    private void initializeUI() {
        this.parentFrame = game.getFrame();
        parentFrame.setEnabled(false); // Disable main game UI during the ending dialog.
        createResultPopup();
    }

    /**
     * Create the result (ending dialog) UI.
     */
    private void createResultPopup() {
        resultDialog = new JDialog(parentFrame);
        resultDialog.setLayout(new BoxLayout(resultDialog.getContentPane(), BoxLayout.Y_AXIS));
        resultDialog.setAlwaysOnTop(true);
        Utilities.centerWindowTo(resultDialog, parentFrame);
        resultDialog.add(createResultLabel());
        resultDialog.add(createButtonDescription());
        resultDialog.add(createConfirmationButtons());
        resultDialog.pack();
        resultDialog.setVisible(true);
    }

    /**
     * Create a text label with the result of the game
     * @return Initialized label object
     */
    private JLabel createResultLabel() {
        JLabel resultLabel = new JLabel("Result");
        resultLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        resultLabel.setBorder(new EmptyBorder(8, 8, 8, 8));
        setResultLabelText(resultLabel);
        resultLabel.setFont(new Font(null, Font.BOLD, 30));
        return resultLabel;
    }

    /**
     * Set the result label text according to the Occupant result
     * @param resultLabel Label object
     */
    private void setResultLabelText(JLabel resultLabel) {
        if (result == Occupant.NONE) { // If the result was a tie
            resultLabel.setText("Tie! No winners!");
        } else { 
            resultLabel.setText(result.toString() + " has won this round!");
        }
    }

    /**
     * Text label for the result description
     * @return Label object
     */
    private JLabel createButtonDescription() {
        JLabel buttonDescription = new JLabel("Result");
        buttonDescription.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonDescription.setBorder(new EmptyBorder(5, 5, 5, 5));
        buttonDescription.setText("Play again?");
        buttonDescription.setFont(new Font(null, Font.PLAIN, 20));
        return buttonDescription;
    }

    /**
     * Create dialog confirmation and cancel buttons
     * @return Label object
     */
    private JPanel createConfirmationButtons() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        JButton confirmButton = new JButton("Confirm");
        confirmButton.addActionListener(event -> onConfirmButtonPressed());
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(event -> onCancelButtonPressed());
        buttonPanel.add(confirmButton);
        buttonPanel.add(cancelButton);
        return buttonPanel;
    }

    /**
     * called when result confirm button is pressed.
     */
    private void onConfirmButtonPressed() {
        disposeDialogAndCreateNewGame();
    }

    /**
     * Called when the cancel button is pressed.
     */
    private void onCancelButtonPressed() {
        disposeDialogAndCreateNewGame(); // Reset board,
        game.setCanPlaceMarks(false); // But don't start a new game.
    }

    /**
     * Dispose this dialog instance and create a new game state using existing settings (essentially erasing the current one).
     */
    private void disposeDialogAndCreateNewGame() {
        resultDialog.dispose();
        Utilities.bringWindowToFront(parentFrame);
        game.createNewGameState(game.getBoard().getRowLength(), game.getBoard().getColumnLength());
    }
}