package ui;

import dao.UserDAO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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

    public LoginGUI() {
        setTitle("Login - Employee Payroll Management System");
        setContentPane(loginPanel); // Glavni panel iz GUI Designera
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300); // Veličina prozora
        setLocationRelativeTo(null); // Centriraj prozor

        //Izgled

        //Background
        loginPanel.setOpaque(false); // Omogućava transparentnost za crtanje pozadine
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

        //Border
        mainLoginPanel.setBorder(new RoundedBorder(20));
        mainLoginPanel.setOpaque(false);

        imePolje.setBorder(new RoundedTextFieldBorder(20)); // Apply the border
        imePolje.setForeground(Color.WHITE);  // Set text color
        imePolje.setCaretColor(Color.WHITE);

        passwordPolje.setBorder(new RoundedTextFieldBorder(20));
        passwordPolje.setOpaque(false);
        passwordPolje.setForeground(Color.WHITE);  // Set text color
        passwordPolje.setCaretColor(Color.WHITE);

        gradientPanel.setLayout(new BorderLayout()); // Preuzmi layout iz loginPanel
        gradientPanel.add(loginPanel); // Dodaj originalni loginPanel u gradientPanel

        setContentPane(gradientPanel); // Postavi gradientPanel kao glavni sadržaj

        // Dodaj funkcionalnost Login dugmeta
        login.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = imePolje.getText();
                String password = new String(passwordPolje.getPassword());
                String role = null;

                // Check which radio button is selected
                if (adminButton.isSelected()) {
                    role = "Admin";
                } else if (managerButton.isSelected()) {
                    role = "Manager";
                } else if (employeeButton.isSelected()) {
                    role = "Employee";
                } else if (superAdminButton.isSelected()) {
                    role = "Super Admin"; // Add Super Admin role
                }

                // If no role is selected, show an error message
                if (role == null) {
                    JOptionPane.showMessageDialog(null, "Please select a role.");
                    return; // Prevent further action if no role is selected
                }

                // Authenticate user
                UserDAO userDAO = new UserDAO();
                if (userDAO.authenticate(username, password, role)) {
                    JOptionPane.showMessageDialog(null, "Login successful!");
                    dispose(); // Close the Login window

                    // Check for the Super Admin role and proceed to MenuGUI only for Super Admin
                    if ("Super Admin".equals(role)) {
                        MenuGUI menuGUI = new MenuGUI();
                        menuGUI.setVisible(true);
                    } /*else {
                        // Open a different GUI for Admin, Manager, or Employee
                        // You can create different classes for each role
                        switch (role) {
                            case "Admin":
                                AdminGUI adminGUI = new AdminGUI();
                                adminGUI.setVisible(true);
                                break;
                            case "Manager":
                                ManagerGUI managerGUI = new ManagerGUI();
                                managerGUI.setVisible(true);
                                break;
                            case "Employee":
                                EmployeeGUI employeeGUI = new EmployeeGUI();
                                employeeGUI.setVisible(true);
                                break;
                        }
                    }*/
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid username, password, or role.");
                }
            }
        });
    }
}
