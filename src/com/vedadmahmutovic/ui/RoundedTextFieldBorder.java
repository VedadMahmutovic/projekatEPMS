package com.vedadmahmutovic.ui;

import javax.swing.border.Border;
import java.awt.*;

public class RoundedTextFieldBorder implements Border {
    private final int radius;

    public RoundedTextFieldBorder(int radius) {
        this.radius = radius;
    }

    @Override
    public Insets getBorderInsets(Component c) {

        return new Insets(6, 6, 6, 6);
    }

    @Override
    public boolean isBorderOpaque() {
        return true;
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);


        g2.setColor(c.getForeground()); // Use the foreground color for the border
        g2.drawRoundRect(x, y, width - 1, height - 1, radius, radius); // Border drawing

        g2.dispose();
    }

}
