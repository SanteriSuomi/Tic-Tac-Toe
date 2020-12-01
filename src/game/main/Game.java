package game.main;

import java.awt.Color;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Component;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

import javax.swing.Box;
import javax.swing.BoxLayout;

import game.board.Board;
import game.dialogs.NewGameDialog;
import game.labels.RotatableJLabel;
import game.player.Bot;
import game.player.Human;
import game.player.Player;
import game.utils.Utilities;

/**
 * Game class is the "main frame" or main view of the application, and some things such as settings and crucial game constants.
 */
public class Game {
    /**
     * Minimum and maximum board sizes (includes both row and column).
     */
    public static final int MIN_BOARD_SIZE = 3;
    public static final int MAX_BOARD_SIZE = 10;

    /**
     * Minimum and maximum consecutive marks needed for a win.
     */
    public static final int MIN_WINNING_MARKS = 3;
    public static final int MAX_WINNING_MARKS = 5;

    /**
     * Minimum and maximum AI accuracy, in other words the depth of minimax (lower = less accurate, faster; higher = more accurate, slower).
     */
    public static final int MIN_AI_ACCURACY = 2;
    public static final int MAX_AI_ACCURACY = 10;

    /**
     * Path to the logo in local resources.
     */
    @SuppressWarnings("java:S1075")
    private static final String LOCAL_LOGO_ICON_PATH = "/images/logo.png";

    /**
     * Winning marks as used by the current game instance (consecutive marks needed for a win).
     */
    private static int winLength;
    public static int getWinLength() {
        return winLength;
    }
    public static void setWinLength(int length) {
        winLength = length;
    }

    /**
     * Currently used AI accuracy as used by the current game instance
     */
    private static int aiAccuracy;
    public static int getAiAccuracy() {
        return aiAccuracy;
    }
    public static void setAiAccuracy(int value) {
        aiAccuracy = value;
    }

    private JFrame frame;
    private Board board;

    private Player human;
    private JLabel humanScoreLabel;
    private int humanScore;

    private Player bot;
    private JLabel botScoreLabel;
    private int botScore;

    private JLabel tieScoreLabel;
    private int amountOfTies;
    
    private JLabel botMovingText;

    /**
     * When canPlaceMarks = false, new marks cannot be set on the board.
     */
    private boolean canPlaceMarks;
    
    public JFrame getFrame() {
        return frame;
    }

    public Board getBoard() {
        return board;
    }

    public Player getHuman() {
        return human;
    }

    /**
     * Increase the score for the human player and update the UI.
     */
    public void increaseAndUpdateHumanScore() {
        humanScore++;
        humanScoreLabel.setText("Human Score: " + humanScore);
    }

    public Player getBot() {
        return bot;
    }

    /**
     * Increase the score for the bot player and update the UI.
     */
    public void increaseAndUpdateBotScore() {
        botScore++;
        botScoreLabel.setText("Bot Score: " + botScore);
    }

    /**
     * Increase tie amount and update the UI.
     */
    public void increaseAndUpdateTieScore() {
        amountOfTies++;
        tieScoreLabel.setText("Ties: " + amountOfTies);
    }

    public boolean getCanPlaceMarks() {
        return canPlaceMarks;
    }

    public void setCanPlaceMarks(boolean value) {
        canPlaceMarks = value;
    }

    /**
     * Set whether or not the bot processing a move text shows.
     * @param value true or false
     */
    public void setBotMovingText(boolean value) {
        botMovingText.setVisible(value);
    }

    public Game() {
        initializeUI();
    }

    /**
     * All initial UI elements are created here.
     */
    private void initializeUI() {
        frame = new JFrame();
        frame.setIconImage(Utilities.getImage(LOCAL_LOGO_ICON_PATH, 32, 32, this));
        frame.setTitle("Tic-Tac-Toe by Santeri Suomi");
        frame.setSize(new Dimension(750, 750));
        frame.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        createHeaderPanel();
        createSidePanels();
        createFooterPanel();
        frame.setVisible(true);
    }

    /**
     * Create main view header and everything in it.
     */
    private void createHeaderPanel() {
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.X_AXIS));
        headerPanel.setBackground(Color.blue);
        headerPanel.setPreferredSize(new Dimension(500, 85));
        headerPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        JButton endGameButton = new JButton();
        endGameButton.setFocusable(false);
        endGameButton.setVerticalAlignment(SwingConstants.CENTER);
        endGameButton.setText("End Game");
        endGameButton.addActionListener(event -> onEndGameButtonPressed());
        endGameButton.setPreferredSize(new Dimension(140, 70));
        endGameButton.setFont(new Font(null, Font.PLAIN, 25));
        JButton newGameButton = new JButton();
        newGameButton.setFocusable(false);
        newGameButton.setVerticalAlignment(SwingConstants.CENTER);
        newGameButton.setText("New Game");
        newGameButton.addActionListener(event -> onNewGameButtonPressed());
        newGameButton.setPreferredSize(new Dimension(140, 70));
        newGameButton.setFont(new Font(null, Font.PLAIN, 25));
        botMovingText = new JLabel();
        botMovingText.setVerticalAlignment(SwingConstants.CENTER);
        botMovingText.setText("Bot move processing...");
        botMovingText.setPreferredSize(new Dimension(150, 70));
        botMovingText.setFont(new Font(null, Font.BOLD, 25));
        botMovingText.setForeground(Color.white);
        botMovingText.setVisible(false);
        headerPanel.add(endGameButton);
        headerPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        headerPanel.add(newGameButton);
        headerPanel.add(Box.createRigidArea(new Dimension(50, 0)));
        headerPanel.add(botMovingText);
        frame.add(headerPanel, BorderLayout.NORTH);
    }

    /**
     * Create main view side panels (left and right) and everything in them.
     */
    private void createSidePanels() {
        JPanel leftPanel = new JPanel();
        leftPanel.setBackground(Color.green);
        leftPanel.setPreferredSize(new Dimension(110, 500));
        leftPanel.setLayout(new BorderLayout());
        RotatableJLabel playerTitle = new RotatableJLabel("Human", new Font(null, Font.BOLD, 30), -0.75);
        leftPanel.add(playerTitle, BorderLayout.CENTER);
        frame.add(leftPanel, BorderLayout.WEST);
        JPanel rightPanel = new JPanel();
        rightPanel.setBackground(Color.ORANGE);
        rightPanel.setPreferredSize(new Dimension(110, 500));
        rightPanel.setLayout(new BorderLayout());
        RotatableJLabel botTitle = new RotatableJLabel("Bot (AI)", new Font(null, Font.BOLD, 30), 0.75);
        rightPanel.add(botTitle, BorderLayout.CENTER);
        frame.add(rightPanel, BorderLayout.EAST);
    }

    /**
     * Create main view footer panel and everything in it.
     */
    private void createFooterPanel() {
        JPanel footerPanel = new JPanel();
        footerPanel.setBackground(Color.PINK);
        footerPanel.setPreferredSize(new Dimension(500, 85));
        footerPanel.setLayout(new BorderLayout());
        JPanel scorePanel = new JPanel();
        scorePanel.setBorder(new EmptyBorder(15, 0, 0, 0));
        scorePanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        scorePanel.setBackground(Color.PINK);
        scorePanel.setLayout(new BoxLayout(scorePanel, BoxLayout.Y_AXIS));
        JLabel scoreTitle = new JLabel();
        scoreTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        scoreTitle.setText("SCORE");
        scoreTitle.setFont(new Font(null, Font.BOLD, 21));
        tieScoreLabel = new JLabel();
        tieScoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        tieScoreLabel.setText("Ties: 0");
        tieScoreLabel.setFont(new Font(null, Font.PLAIN, 21));
        scorePanel.add(scoreTitle);
        scorePanel.add(tieScoreLabel);
        humanScoreLabel = new JLabel();
        humanScoreLabel.setBorder(new EmptyBorder(0, 60, 0, 0));
        humanScoreLabel.setText("Human Score: 0");
        humanScoreLabel.setFont(new Font(null, Font.PLAIN, 21));
        botScoreLabel = new JLabel();
        botScoreLabel.setBorder(new EmptyBorder(0, 0, 0, 60));
        botScoreLabel.setText("Bot Score: 0");
        botScoreLabel.setFont(new Font(null, Font.PLAIN, 21));
        footerPanel.add(scorePanel, BorderLayout.CENTER);
        footerPanel.add(humanScoreLabel, BorderLayout.WEST);
        footerPanel.add(botScoreLabel, BorderLayout.EAST);
        frame.add(footerPanel, BorderLayout.SOUTH);
    }

    /**
     * Called when the NewGame button is pressed.
     */
    private void onNewGameButtonPressed() {
        frame.setEnabled(false);
        new NewGameDialog(this);
    }

    /**
     * Called when the EndGame button is pressed.
     */
    private void onEndGameButtonPressed() {
        frame.setEnabled(false);
        int result = JOptionPane.showConfirmDialog(frame, "Are you sure you want to exit?");
        if (result == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
        Utilities.bringWindowToFront(frame);
    }

    /**
     * Creates a brand new game state (board, players, etc). Removes game state currently active.
     * @param row Board row size
     * @param column Board column size
     */
    public void createNewGameState(int row, int column) {
        if (board != null) {
            frame.remove(board.getPanel());
        }
        board = new Board(row, column, this);
        human = new Human(this);
        bot = new Bot(this);
        frame.add(board.getPanel(), BorderLayout.CENTER);
        SwingUtilities.updateComponentTreeUI(frame); // Make sure UI components get updated
    }
}