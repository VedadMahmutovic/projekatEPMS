package ui;

import javax.swing.border.Border;
import java.awt.*;

public class RoundedTextFieldBorder implements Border {
    private final int radius;

    public RoundedTextFieldBorder(int radius) {
        this.radius = radius;
    }

    @Override
    public Insets getBorderInsets(Component c) {
        // Providing some padding between the text field content and the border
        return new Insets(6, 6, 6, 6);  // Adjust this for proper padding
    }

    @Override
    public boolean isBorderOpaque() {
        return true; // Let Swing handle the background naturally
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw the rounded border
        g2.setColor(c.getForeground()); // Use the foreground color for the border
        g2.drawRoundRect(x, y, width - 1, height - 1, radius, radius); // Border drawing

        g2.dispose();
    }

}
