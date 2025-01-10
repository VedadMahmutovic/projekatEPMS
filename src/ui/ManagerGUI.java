package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;

public class ManagerGUI extends JFrame {
    private JPanel glavniPanel;
    private JPanel backgroundPanel;
    private JPanel zaposleniciPanel;
    private JLabel zaposlenikLabel;
    private JButton pregledZaposlenikaButton;
    private JPanel zadnjiPanel;
    private JLabel settingsLabel;
    private JButton promjeniSifruButton;
    private JPanel platePanel;
    private JLabel plataLabel;
    private JButton generisiPLButton;
    private JButton obracunPLButton;
    private JButton pregledIsplataButton;
    private JPanel izvjestajPanel;
    private JLabel izvjestajLabel;
    private JButton mjesecniIzvjestajPlataButton;
    private JButton izvjestajZaposlenikaButton;
    private JButton odjavaButton;

    public ManagerGUI() {
        setTitle("Manager Menu - Employee Payroll Management System");

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

        setupPanelBorders();
        setupButtonEffects();
        setupLogoutButton();
        replaceIcons();
        scaleIconsOnResize();

        setContentPane(gradientPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1920, 1080);
        setLocationRelativeTo(null);

        // Add action listeners for retained functionalities
        pregledZaposlenikaButton.addActionListener(e -> openPregledZaposlenikaPanel());
        generisiPLButton.addActionListener(e -> openGenPlatnuListuPanel());
        obracunPLButton.addActionListener(e -> openObracunPlatePanel());
        pregledIsplataButton.addActionListener(e -> openPregledIsplataPanel());
        mjesecniIzvjestajPlataButton.addActionListener(e -> openMjesecniIzvjestajBackPanel());
        izvjestajZaposlenikaButton.addActionListener(e -> openIzvjestajZapBackPanel());
        promjeniSifruButton.addActionListener(e -> openChangePasswordPanel());
    }

    private void setupPanelBorders() {
        zaposleniciPanel.setBorder(new RoundedBorder(20));
        zaposleniciPanel.setOpaque(false);
        platePanel.setBorder(new RoundedBorder(20));
        platePanel.setOpaque(false);
        izvjestajPanel.setBorder(new RoundedBorder(20));
        izvjestajPanel.setOpaque(false);
        zadnjiPanel.setBorder(new RoundedBorder(20));
        zadnjiPanel.setOpaque(false);
    }

    private void setupButtonEffects() {
        Color defaultColor = Color.decode("#1C3A28");
        Color hoverColor = defaultColor.brighter();
        Color clickColor = hoverColor.brighter();

        odjavaButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                odjavaButton.setBackground(hoverColor);
                odjavaButton.repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                odjavaButton.setBackground(defaultColor);
                odjavaButton.repaint();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                odjavaButton.setBackground(clickColor);
                odjavaButton.repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                odjavaButton.setBackground(hoverColor);
                odjavaButton.repaint();
            }
        });

        odjavaButton.setUI(new javax.swing.plaf.basic.BasicButtonUI() {
            @Override
            public void paint(Graphics g, JComponent c) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2.setColor(c.getBackground());
                g2.fillRoundRect(0, 0, c.getWidth(), c.getHeight(), 20, 20);

                g2.setColor(c.getForeground());
                FontMetrics fm = g2.getFontMetrics();
                String text = ((JButton) c).getText();
                int textWidth = fm.stringWidth(text);
                int textHeight = fm.getAscent();
                int x = (c.getWidth() - textWidth) / 2;
                int y = (c.getHeight() + textHeight) / 2 - 2;
                g2.drawString(text, x, y);

                g2.drawRoundRect(0, 0, c.getWidth() - 1, c.getHeight() - 1, 20, 20);
                g2.dispose();
            }
        });
    }

    private void setupLogoutButton() {
        odjavaButton.addActionListener(e -> {
            int confirmed = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to log out?",
                    "Logout Confirmation",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirmed == JOptionPane.YES_OPTION) {
                SwingUtilities.invokeLater(() -> {
                    dispose();
                    JFrame loginFrame = new LoginGUI();
                    loginFrame.setVisible(true);
                });
            }
        });
    }

    private void replaceIcons() {
        replaceWithResizableIcon(zaposlenikLabel, "/ikone/IkonaZaposlenik.png");
        replaceWithResizableIcon(plataLabel, "/ikone/IkonaCash.png");
        replaceWithResizableIcon(izvjestajLabel, "/ikone/IkonaIzvjestaj.png");
        replaceWithResizableIcon(settingsLabel, "/ikone/IkonaCog.png");
    }

    private void scaleIconsOnResize() {
        pregledZaposlenikaButton.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                scaleIconWithButton(pregledZaposlenikaButton, "/ikone/plus.png");
                scaleIconWithButton(generisiPLButton, "/ikone/plus.png");
                scaleIconWithButton(obracunPLButton, "/ikone/plus.png");
                scaleIconWithButton(pregledIsplataButton, "/ikone/plus.png");
                scaleIconWithButton(mjesecniIzvjestajPlataButton, "/ikone/plus.png");
                scaleIconWithButton(izvjestajZaposlenikaButton, "/ikone/plus.png");
                scaleIconWithButton(promjeniSifruButton, "/ikone/plus.png");
            }
        });
    }

    // Functionality implementations
    private void openPregledZaposlenikaPanel() {
        SwingUtilities.invokeLater(() -> {
            JFrame pregledFrame = new JFrame("Employee Overview");
            pregledFrame.setContentPane(new ZaposlenikGUI().pregledZapPanel);
            pregledFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            pregledFrame.pack();
            pregledFrame.setLocationRelativeTo(null);
            pregledFrame.setVisible(true);
        });
    }

    private void openGenPlatnuListuPanel() {
        SwingUtilities.invokeLater(() -> {
            JFrame platnaListaFrame = new JFrame("Generate Payroll");
            platnaListaFrame.setContentPane(new PlataGUI().genPlatListuBackPanel);
            platnaListaFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            platnaListaFrame.pack();
            platnaListaFrame.setLocationRelativeTo(null);
            platnaListaFrame.setVisible(true);
        });
    }

    private void openObracunPlatePanel() {
        SwingUtilities.invokeLater(() -> {
            JFrame obracunFrame = new JFrame("Payroll Calculation");
            obracunFrame.setContentPane(new PlataGUI().obracunPlateBackPanel);
            obracunFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            obracunFrame.pack();
            obracunFrame.setLocationRelativeTo(null);
            obracunFrame.setVisible(true);
        });
    }

    private void openPregledIsplataPanel() {
        SwingUtilities.invokeLater(() -> {
            JFrame pregledFrame = new JFrame("Salary Overview");
            pregledFrame.setContentPane(new PlataGUI().pregledIsplataBackPanel);
            pregledFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            pregledFrame.pack();
            pregledFrame.setLocationRelativeTo(null);
            pregledFrame.setVisible(true);
        });
    }

    private void openMjesecniIzvjestajBackPanel() {
        SwingUtilities.invokeLater(() -> {
            JFrame reportFrame = new JFrame("Monthly Report");
            reportFrame.setContentPane(new IzvjestajGUI().mjesecniIzvjestajBackPanel);
            reportFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            reportFrame.pack();
            reportFrame.setLocationRelativeTo(null);
            reportFrame.setVisible(true);
        });
    }

    private void openIzvjestajZapBackPanel() {
        SwingUtilities.invokeLater(() -> {
            JFrame reportFrame = new JFrame("Employee Reports");
            reportFrame.setContentPane(new IzvjestajGUI().izvjestajZapBackPanel);
            reportFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            reportFrame.pack();
            reportFrame.setLocationRelativeTo(null);
            reportFrame.setVisible(true);
        });
    }

    private void openChangePasswordPanel() {
        SwingUtilities.invokeLater(() -> {
            JFrame passwordFrame = new JFrame("Change Password");
            passwordFrame.setContentPane(new IzvjestajGUI().passwordChangeBackPanel);
            passwordFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            passwordFrame.pack();
            passwordFrame.setLocationRelativeTo(null);
            passwordFrame.setVisible(true);
        });
    }

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

    private int findComponentIndex(Container parent, Component comp) {
        for (int i = 0; i < parent.getComponentCount(); i++) {
            if (parent.getComponent(i) == comp) {
                return i;
            }
        }
        return -1;
    }

    private void scaleIconWithButton(JButton button, String iconPath) {
        URL iconURL = getClass().getResource(iconPath);
        if (iconURL != null) {
            ImageIcon icon = new ImageIcon(iconURL);
            Image img = icon.getImage();
            Image scaledImg = img.getScaledInstance(button.getWidth(), button.getHeight(), Image.SCALE_SMOOTH);
            button.setIcon(new ImageIcon(scaledImg));
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ManagerGUI().setVisible(true));
    }
}
