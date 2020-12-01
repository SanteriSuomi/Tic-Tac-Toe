package game.labels;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.geom.Rectangle2D;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JLabel;

/**
 * Represents a JLabel object that can be rotated.
 */
public class RotatableJLabel extends JLabel {
    private static final long serialVersionUID = 1L;
    private double rotation;

    /**
     * New rotatable JLabel, with text, font and rotation applied
     * @param text String of text to be displayed on this JLabel
     * @param font Font of the text
     * @param rotation Rotation
     */
    public RotatableJLabel(String text, Font font, double rotation) {
        super(text);
        this.rotation = rotation;
        setFont(font);
        FontMetrics fm = new FontMetrics(font) {
            private static final long serialVersionUID = 1L;
        };
        Rectangle2D bounds = fm.getStringBounds(text, null);
        setBounds(0, 0, (int)bounds.getWidth(), (int)bounds.getHeight());
    }

    @Override
    /**
     * Display the component using an inherited paint method, rotation itself also handled here
     */
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.rotate(rotation, getX() + (double)getWidth() / 2, getY() + (double)getHeight() / 2);
        super.paintComponent(g);
    }
}