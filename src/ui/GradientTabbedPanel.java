package ui;

import javax.swing.*;
import java.awt.*;

public class GradientTabbedPanel extends JTabbedPane {
    private Color topColor = Color.decode("#1C2518");
    private Color bottomColor = Color.decode("#477C64");

    public GradientTabbedPanel() {
        setOpaque(false); // Ensure transparency
        setUI(new javax.swing.plaf.basic.BasicTabbedPaneUI() {
            @Override
            protected void paintContentBorder(Graphics g, int tabPlacement, int selectedIndex) {
                // Prevent default content border painting
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        GradientPaint gradient = new GradientPaint(
                0, 0, topColor,
                0, getHeight(), bottomColor
        );
        g2d.setPaint(gradient);
        g2d.fillRect(0, 0, getWidth(), getHeight());

        g2d.dispose();
        super.paintComponent(g); // Paint the tabs and their contents
    }
}
