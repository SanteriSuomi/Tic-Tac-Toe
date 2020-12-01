package game;

import java.awt.EventQueue;

import game.main.Game;

/**
 * Application entry point. Starts the main Game instance by forcing it to run in the dispatch thread of Swing (invokeLater).
 */
public class App {
    public static void main(String[] args) {
        EventQueue.invokeLater(Game::new);
    }
}