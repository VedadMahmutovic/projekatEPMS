package com.vedadmahmutovic.ui;

import javax.swing.*;
import java.awt.*;

public class ResizableIconLabel extends JLabel {
    private ImageIcon icon;

    public ResizableIconLabel(String imagePath) {
        icon = new ImageIcon(getClass().getResource(imagePath));
        setHorizontalAlignment(JLabel.CENTER);
        setVerticalAlignment(JLabel.CENTER);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (icon != null) {
            Graphics2D g2d = (Graphics2D) g.create();
            Image img = icon.getImage();
            g2d.drawImage(img, 0, 0, getWidth(), getHeight(), this);
            g2d.dispose();
        }
    }
}