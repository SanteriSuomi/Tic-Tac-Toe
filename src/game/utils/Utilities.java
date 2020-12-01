package game.utils;

import java.awt.Window;
import java.awt.Point;
import java.awt.Image;

import javax.swing.ImageIcon;

/**
 * A static, non-instantiable class for small utility functions.
 */
public final class Utilities {
    /**
     * Prevent instantiating by throwing exceptions if somehow tried.
     */
    private Utilities() {
        throw new IllegalStateException("Utility class, do not instantiate");
    }

    /**
     * Attempt to position a window element to the middle of a another window element.
     * @param win1 Window to center
     * @param win2 Window to center to
     */
    public static void centerWindowTo(Window win1, Window win2) {
        win1.setLocation(new Point((int)(win2.getX() + (win2.getWidth() / 3.75)), (int)(win2.getY() + (win2.getHeight() / 2.5))));
    }

    /**
     * Attempt to bring a window element to the front of other window elements. Also enables the window.
     * @param win Window to bring to the front
     */
    public static void bringWindowToFront(Window win) {
        win.toFront();
        win.repaint();
        win.setEnabled(true);
    }

    /**
     * Get a image object from local resources rescaled to width and height.
     * @param path Path of the mark image file
     * @param width Width of the image to be returned
     * @param height Height of the image to be returned
     * @param obj Object context, so that the resource can be found locally
     */
    public static Image getImage(String path, int width, int height, Object obj) {
        ImageIcon imgIcon = new ImageIcon(obj.getClass().getResource(path));
        return imgIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
    }
}