package ui;

import dao.UserDAO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class LoginGUI extends JFrame {

    private JTextField imePolje;
    private JPasswordField passwordPolje;
    private JButton login;
    private JPanel loginPanel;
    private JRadioButton adminButton;
    private JRadioButton managerButton;
    private JRadioButton employeeButton;
    private JPanel korisniciPanel;
    private JPanel mainLoginPanel;
    private JRadioButton superAdminButton;

    public static String loggedInUser;

    public LoginGUI() {
        setTitle("Login - Employee Payroll Management System");
        setContentPane(loginPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);

        // Background, omogućava transparentnost za crtanje pozadine
        loginPanel.setOpaque(false);
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


        mainLoginPanel.setBorder(new RoundedBorder(20));
        mainLoginPanel.setOpaque(false);

        imePolje.setBorder(new RoundedTextFieldBorder(20));
        imePolje.setForeground(Color.WHITE);
        imePolje.setCaretColor(Color.WHITE);

        passwordPolje.setBorder(new RoundedTextFieldBorder(20));
        passwordPolje.setOpaque(false);
        passwordPolje.setForeground(Color.WHITE);
        passwordPolje.setCaretColor(Color.WHITE);

        gradientPanel.setLayout(new BorderLayout());
        gradientPanel.add(loginPanel);

        setContentPane(gradientPanel);
        // Ovdje se dodaje funkcionalnost za login dugme
        login.addActionListener(this::performLogin);

        InputMap inputMap = loginPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = loginPanel.getActionMap();

        inputMap.put(KeyStroke.getKeyStroke("ENTER"), "loginAction");
        actionMap.put("loginAction", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performLogin(e);
            }
        });
    }

    private void performLogin(ActionEvent e) {
        String username = imePolje.getText();
        String password = new String(passwordPolje.getPassword());
        String role = determineSelectedRole();

        if (role == null) {
            JOptionPane.showMessageDialog(this, "Please select a role.");
            return;
        }

        // Autentifikacija korisnika
        UserDAO userDAO = new UserDAO();
        if (userDAO.authenticate(username, password, role)) {
            loggedInUser = username;
            JOptionPane.showMessageDialog(this, "Login successful!");
            dispose();
            openRoleSpecificMenu(role);
        } else {
            JOptionPane.showMessageDialog(this, "Invalid username, password, or role.");
        }
    }

    private String determineSelectedRole() {
        if (adminButton.isSelected()) {
            return "Admin";
        } else if (managerButton.isSelected()) {
            return "Manager";
        } else if (employeeButton.isSelected()) {
            return "Employee";
        } else if (superAdminButton.isSelected()) {
            return "Super Admin";
        }
        return null;
    }

    private void openRoleSpecificMenu(String role) {
        switch (role) {
            case "Admin":
                new AdminGUI().setVisible(true);
                break;
            case "Manager":
                new ManagerGUI().setVisible(true);
                break;
            case "Employee":
                new EmployeeGUI().setVisible(true);
                break;
            case "Super Admin":
                new MenuGUI().setVisible(true); // MenuGUI je Super admin
                break;
            default:
                throw new IllegalArgumentException("Invalid role: " + role);
        }
    }
}
