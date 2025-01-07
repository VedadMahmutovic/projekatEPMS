package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;

public class MenuGUI extends JFrame {
    private JPanel glavniPanel;
    private JPanel platePanel;
    private JPanel izvjestajPanel;
    private JPanel zaposleniciPanel;
    private JPanel zadnjiPanel;
    private JPanel backgroundPanel;

    private JLabel zaposlenikLabel;
    private JLabel plataLabel;
    private JLabel izvjestajLabel;
    private JLabel settingsLabel;
    private JButton odjavaButton;
    private JButton dodajZaposlenikaButton;
    private JPanel dodajZaposlenikaPanel;
    private JButton urediZaposlenikaButton;
    private JButton pregledZaposlenikaButton;
    private JButton obrisiZaposlenikaButton;
    private JTabbedPane tabbedPanel;
    private JLabel rukeLabel;

    public MenuGUI() {
        setTitle("Menu - Employee Payroll Management System");

        // Gradient background setup
        JPanel gradientPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                Color topColor = Color.decode("#1C2518");
                Color bottomColor = Color.decode("#477C64");

                GradientPaint gradient = new GradientPaint(
                        0, getHeight(), bottomColor,
                        0, 0, topColor
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        gradientPanel.setLayout(new BorderLayout());
        gradientPanel.add(glavniPanel, BorderLayout.CENTER);



        zaposleniciPanel.setBorder(new RoundedBorder(20));
        zaposleniciPanel.setOpaque(false);
        platePanel.setBorder(new RoundedBorder(20));
        platePanel.setOpaque(false);
        izvjestajPanel.setBorder(new RoundedBorder(20));
        izvjestajPanel.setOpaque(false);
        zadnjiPanel.setBorder(new RoundedBorder(20));
        zadnjiPanel.setOpaque(false);

        // Button Colors
        Color defaultColor = Color.decode("#1C3A28"); // Default button color
        Color hoverColor = defaultColor.brighter(); // Slightly lighter on hover
        Color clickColor = hoverColor.brighter(); // Even lighter on click


        // MouseListener for Hover and Click Effects
        odjavaButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                odjavaButton.setBackground(hoverColor); // Hover effect
                odjavaButton.repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                odjavaButton.setBackground(defaultColor); // Reset to default
                odjavaButton.repaint();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                odjavaButton.setBackground(clickColor); // Click effect
                odjavaButton.repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                odjavaButton.setBackground(hoverColor); // Return to hover color
                odjavaButton.repaint();
            }
        });

        // Custom painting to apply rounded corners
        odjavaButton.setUI(new javax.swing.plaf.basic.BasicButtonUI() {
            @Override
            public void paint(Graphics g, JComponent c) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Paint rounded background
                g2.setColor(c.getBackground());
                g2.fillRoundRect(0, 0, c.getWidth(), c.getHeight(), 20, 20);

                // Paint text
                g2.setColor(c.getForeground());
                FontMetrics fm = g2.getFontMetrics();
                String text = ((JButton) c).getText();
                int textWidth = fm.stringWidth(text);
                int textHeight = fm.getAscent();
                int x = (c.getWidth() - textWidth) / 2;
                int y = (c.getHeight() + textHeight) / 2 - 2;
                g2.drawString(text, x, y);

                // Paint rounded border
                g2.setColor(c.getForeground());
                g2.drawRoundRect(0, 0, c.getWidth() - 1, c.getHeight() - 1, 20, 20);

                g2.dispose();
            }
        });

        // Add Button to GridBagLayout
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; // Adjust based on your original layout
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Replace JLabel placeholders with ResizableIconLabel
        replaceWithResizableIcon(zaposlenikLabel, "/ikone/IkonaZaposlenik.png");
        replaceWithResizableIcon(plataLabel, "/ikone/IkonaCash.png");
        replaceWithResizableIcon(izvjestajLabel, "/ikone/IkonaIzvjestaj.png");
        replaceWithResizableIcon(settingsLabel, "/ikone/IkonaCog.png");


        // Scale the icon for the dodajZaposlenikaButton
        dodajZaposlenikaButton.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                scaleIconWithButton(dodajZaposlenikaButton, "/ikone/plus.png"); // Provide the correct path for your icon
                scaleIconWithButton(urediZaposlenikaButton, "/ikone/plus.png");
                scaleIconWithButton(pregledZaposlenikaButton, "/ikone/plus.png");
                scaleIconWithButton(obrisiZaposlenikaButton, "/ikone/plus.png");
            }
        });

        setContentPane(gradientPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1920, 1080);
        setLocationRelativeTo(null);
    }

    /**
     * Replace JLabel with a ResizableIconLabel dynamically.
     */
    private void replaceWithResizableIcon(JLabel label, String imagePath) {
        if (label.getParent() != null) {
            Container parent = label.getParent();
            int index = findComponentIndex(parent, label);
            if (index != -1) {
                parent.remove(index);
                ResizableIconLabel iconLabel = new ResizableIconLabel(imagePath);
                iconLabel.setPreferredSize(label.getPreferredSize());
                parent.add(iconLabel, index);
                parent.revalidate();
                parent.repaint();
            }
        }
    }

    /**
     * Find the index of a component in its parent container.
     */
    private int findComponentIndex(Container parent, Component comp) {
        for (int i = 0; i < parent.getComponentCount(); i++) {
            if (parent.getComponent(i) == comp) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Scale the icon with the button size.
     */
    private void scaleIconWithButton(JButton button, String iconPath) {
        // Use getClass().getResource() to correctly access the resource
        URL iconURL = getClass().getResource(iconPath);

        if (iconURL != null) {
            ImageIcon icon = new ImageIcon(iconURL);
            Image img = icon.getImage();  // Get the original image
            Image scaledImg = img.getScaledInstance(button.getWidth(), button.getHeight(), Image.SCALE_SMOOTH); // Scale the image
            button.setIcon(new ImageIcon(scaledImg)); // Set the scaled image as the button's icon
        } else {
            System.err.println("Icon not found at: " + iconPath);
        }
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new MenuGUI();
            frame.setVisible(true);
        });
    }
}
