package com.vedadmahmutovic.ui;

import com.vedadmahmutovic.dao.UserDAO;
import com.vedadmahmutovic.dao.PayrollDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.util.List;

public class EmployeeGUI extends JFrame {
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
    private JButton pregledIsplataButton;
    private JPanel izvjestajPanel;
    private JLabel izvjestajLabel;
    private JButton mjesecniIzvjestajPlataButton;
    private JButton odjavaButton;

    private UserDAO userDAO = new UserDAO();
    private PayrollDAO payrollDAO = new PayrollDAO();

    public EmployeeGUI() {
        setTitle("Employee Menu - Employee Payroll Management System");


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

        pregledZaposlenikaButton.addActionListener(e -> viewEmployeeInfo());
        pregledIsplataButton.addActionListener(e -> viewPayrollDetails());
        mjesecniIzvjestajPlataButton.addActionListener(e -> viewMonthlyReport());
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
                    "Da li ste sigurni da želite da se odjavite?",
                    "Potvrda odjave",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirmed == JOptionPane.YES_OPTION) {
                // Zatvara sve otvorene prozore
                for (Window window : Window.getWindows()) {
                    if (window instanceof JFrame) {
                        window.dispose();
                    }
                }

                // Prikaz LoginGUI prozora
                SwingUtilities.invokeLater(() -> {
                    JFrame loginFrame = new LoginGUI();
                    loginFrame.setVisible(true);
                });
            }
        });
    }


    private void viewEmployeeInfo() {
        try {
            String loggedInUser = LoginGUI.loggedInUser;

            if (loggedInUser == null || loggedInUser.isEmpty()) {
                JOptionPane.showMessageDialog(
                        this,
                        "No user is currently logged in.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            String[] employeeDetails = userDAO.getEmployeeDetailsByUsername(loggedInUser);

            if (employeeDetails == null) {
                JOptionPane.showMessageDialog(
                        this,
                        "No details found for the logged-in user.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            StringBuilder details = new StringBuilder("Your Details:\n");
            details.append("Username: ").append(employeeDetails[0]).append("\n");
            details.append("Role: ").append(employeeDetails[1]).append("\n");
            details.append("Full Name: ").append(employeeDetails[2]).append("\n");
            details.append("Email: ").append(employeeDetails[3]).append("\n");
            details.append("Department: ").append(employeeDetails[4]).append("\n");
            details.append("Job Title: ").append(employeeDetails[5]).append("\n");
            details.append("Basic Salary: ").append(employeeDetails[6]).append("\n");
            details.append("Date Hired: ").append(employeeDetails[7]).append("\n");

            JOptionPane.showMessageDialog(
                    this,
                    details.toString(),
                    "Employee Info",
                    JOptionPane.INFORMATION_MESSAGE
            );

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "An error occurred: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            ex.printStackTrace();
        }
    }



    private void viewPayrollDetails() {
        try {
            String loggedInUser = LoginGUI.loggedInUser;

            if (loggedInUser == null || loggedInUser.isEmpty()) {
                JOptionPane.showMessageDialog(
                        this,
                        "No user is currently logged in.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            List<String[]> payrollDetails = payrollDAO.getPayrollForLoggedInUser(loggedInUser);

            if (payrollDetails.isEmpty()) {
                JOptionPane.showMessageDialog(
                        this,
                        "No payroll details found for the logged-in user.",
                        "Info",
                        JOptionPane.INFORMATION_MESSAGE
                );
                return;
            }

            String[] columns = {"Payroll ID", "Salary", "Deductions", "Tax", "Bonus", "Pay Date"};
            DefaultTableModel model = new DefaultTableModel(columns, 0);

            for (String[] row : payrollDetails) {
                model.addRow(row);
            }

            JTable payrollTable = new JTable(model);
            JScrollPane scrollPane = new JScrollPane(payrollTable);


            payrollTable.setBackground(new Color(39, 83, 56));
            payrollTable.setForeground(new Color(173, 212, 190));
            payrollTable.setGridColor(new Color(173, 212, 190));
            payrollTable.setSelectionBackground(new Color(25, 58, 39));
            payrollTable.setSelectionForeground(new Color(173, 212, 190));

            JFrame payrollFrame = new JFrame("Your Payroll Details");
            payrollFrame.setSize(800, 400);
            payrollFrame.add(scrollPane);
            payrollFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            payrollFrame.setLocationRelativeTo(null);
            payrollFrame.setVisible(true);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "An error occurred: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            ex.printStackTrace();
        }
    }



    private void viewMonthlyReport() {
        try {
            String loggedInUser = LoginGUI.loggedInUser;

            if (loggedInUser == null || loggedInUser.isEmpty()) {
                JOptionPane.showMessageDialog(
                        this,
                        "No user is currently logged in.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            // Dohvata mjesečni izvještaj za prijavljenog korisnika
            List<String[]> monthlyReport = payrollDAO.getMonthlyReportForLoggedInUser(loggedInUser);

            if (monthlyReport.isEmpty()) {
                JOptionPane.showMessageDialog(
                        this,
                        "No monthly report found for the logged-in user.",
                        "Info",
                        JOptionPane.INFORMATION_MESSAGE
                );
                return;
            }

            // Prikaz izvještaja u tabeli
            String[] columns = {"Month", "Total Salary", "Total Bonus", "Total Deductions", "Total Tax", "Net Income"};
            DefaultTableModel model = new DefaultTableModel(columns, 0);

            for (String[] row : monthlyReport) {
                model.addRow(row);
            }

            JTable reportTable = new JTable(model);
            JScrollPane scrollPane = new JScrollPane(reportTable);


            reportTable.setBackground(new Color(39, 83, 56));
            reportTable.setForeground(new Color(173, 212, 190));
            reportTable.setGridColor(new Color(173, 212, 190));
            reportTable.setSelectionBackground(new Color(25, 58, 39));
            reportTable.setSelectionForeground(new Color(173, 212, 190));

            JFrame reportFrame = new JFrame("Your Monthly Report");
            reportFrame.setSize(800, 400);
            reportFrame.add(scrollPane);
            reportFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            reportFrame.setLocationRelativeTo(null);
            reportFrame.setVisible(true);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "An error occurred: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            ex.printStackTrace();
        }
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

    private void replaceIcons() {
        zaposlenikLabel.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent evt) {
                resizeIcon(zaposlenikLabel, "/ikone/IkonaZaposlenik.png");
            }
        });

        plataLabel.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent evt) {
                resizeIcon(plataLabel, "/ikone/IkonaCash.png");
            }
        });

        izvjestajLabel.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent evt) {
                resizeIcon(izvjestajLabel, "/ikone/IkonaIzvjestaj.png");
            }
        });

        settingsLabel.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent evt) {
                resizeIcon(settingsLabel, "/ikone/IkonaCog.png");
            }
        });
    }

    private void resizeIcon(JLabel label, String imagePath) {
        ImageIcon icon = new ImageIcon(getClass().getResource(imagePath));
        if (icon != null && label.getWidth() > 0 && label.getHeight() > 0) {
            Image img = icon.getImage();
            Image scaledImg = img.getScaledInstance(label.getWidth(), label.getHeight(), Image.SCALE_SMOOTH);
            label.setIcon(new ImageIcon(scaledImg));
        }
    }


    private void scaleIconsOnResize() {
        pregledZaposlenikaButton.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                scaleIconWithButton(pregledZaposlenikaButton, "/ikone/plus.png");
                scaleIconWithButton(pregledIsplataButton, "/ikone/plus.png");
                scaleIconWithButton(mjesecniIzvjestajPlataButton, "/ikone/plus.png");
                scaleIconWithButton(promjeniSifruButton, "/ikone/plus.png");
            }
        });
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
        SwingUtilities.invokeLater(() -> new EmployeeGUI().setVisible(true));
    }
}
