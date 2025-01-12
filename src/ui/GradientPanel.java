package ui;

import javax.swing.*;
import java.awt.*;


public class GradientPanel extends JPanel {
    private final Color topColor;
    private final Color bottomColor;

    public GradientPanel(Color topColor, Color bottomColor) {
        this.topColor = topColor;
        this.bottomColor = bottomColor;
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        GradientPaint gradient = new GradientPaint(
                0, getHeight(), bottomColor,
                0, 0, topColor
        );
        g2d.setPaint(gradient);
        g2d.fillRect(0, 0, getWidth(), getHeight());
    }
}
