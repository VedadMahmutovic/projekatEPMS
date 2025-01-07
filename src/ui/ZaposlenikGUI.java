package ui;

import dao.EmployeeDAO;
import model.Employee;

import javax.swing.*;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.plaf.basic.BasicTextFieldUI;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.RenderingHints;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ZaposlenikGUI extends JFrame {
    private JTabbedPane glavniTabbedPanel;
    private JPanel glavniPanel;
    // Dodavanje Zaposlenika
    private JTextField idField;
    private JTextField nameField;
    private JTextField surnameField;
    private JTextField emailField;
    private JTextField dobField;
    private JTextField contactField;
    private JTextField adressField;
    private JTextField houseNumberField;
    private JTextField postalField;
    private JTextField departmantField;
    private JTextField dateHiredField;
    private JTextField salaryField;
    private JTextField jobTitleField;
    private JTextField statusField;
    JPanel backgroundPanel;
    private JPanel dodajZaposlenikaPanel;
    private JButton deleteButton;
    private JButton clearButton;
    private JButton addButton;  // Define the add button here
    private JPanel pregledZaposlenikaPanel;
    private JTable pregledZaposlenikaTabela;
    private JPanel urediZapBackPanel;
    private JPanel urediZapPanel;
    JScrollPane pregledZapPanel;

    // Konstruktor GUI
    public ZaposlenikGUI() {
        setupUI();
        addListeners();
        loadEmployeeTable();
    }

    // Method for styling buttons
    private void setupRoundedButton(JButton button) {
        button.setUI(new BasicButtonUI() {
            @Override
            public void paint(Graphics g, JComponent c) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Draw rounded rectangle background
                g2.setColor(c.getBackground());
                g2.fillRoundRect(0, 0, c.getWidth(), c.getHeight(), 20, 20);

                // Draw button text
                g2.setColor(c.getForeground());
                FontMetrics fm = g2.getFontMetrics();
                String text = ((JButton) c).getText();
                int textWidth = fm.stringWidth(text);
                int textHeight = fm.getAscent();
                int x = (c.getWidth() - textWidth) / 2;
                int y = (c.getHeight() + textHeight) / 2 - 2;
                g2.drawString(text, x, y);

                // Draw rounded rectangle border
                g2.setColor(c.getForeground());
                g2.drawRoundRect(0, 0, c.getWidth() - 1, c.getHeight() - 1, 20, 20);

                g2.dispose();
            }
        });

        // Remove default button visuals
        button.setOpaque(false);
        button.setContentAreaFilled(false); // Prevent default background rendering
        button.setBorderPainted(false); // Prevent default border rendering

        // Set colors
        button.setBackground(Color.decode("#4D7B66"));
        button.setForeground(Color.decode("#DDF5E5"));

        // Hover effects
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(button.getBackground().brighter());
                button.repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(Color.decode("#4D7B66"));
                button.repaint();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                button.setBackground(button.getBackground().darker());
                button.repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                button.setBackground(button.getBackground().brighter());
                button.repaint();
            }
        });
    }

    // Method for styling text fields
    private void setupRoundedTextField(JTextField textField) {
        textField.setBorder(new RoundedBorder(10)); // Apply rounded border

        textField.setOpaque(false); // Allow custom background rendering

        // Apply custom UI to handle painting cleanly
        textField.setUI(new BasicTextFieldUI() {
            @Override
            protected void paintSafely(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Draw rounded background
                g2.setColor(textField.getBackground());
                g2.fillRoundRect(0, 0, textField.getWidth(), textField.getHeight(), 10, 10);

                // Draw border
                g2.setColor(Color.LIGHT_GRAY);
                g2.drawRoundRect(0, 0, textField.getWidth() - 1, textField.getHeight() - 1, 10, 10);

                g2.dispose();

                // Let Swing handle text rendering
                super.paintSafely(g);
            }
        });

        // Prevent default border artifacts
        textField.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
    }

    private void setupUI() {
        dodajZaposlenikaPanel.setBorder(new RoundedBorder(20));
        dodajZaposlenikaPanel.setOpaque(false);

        // Apply rounded borders to buttons and text fields
        setupRoundedButton(deleteButton);
        setupRoundedButton(clearButton);
        setupRoundedButton(addButton);
        setupRoundedTextField(idField);
        setupRoundedTextField(nameField);
        setupRoundedTextField(surnameField);
        setupRoundedTextField(emailField);
        setupRoundedTextField(dobField);
        setupRoundedTextField(contactField);
        setupRoundedTextField(adressField);
        setupRoundedTextField(houseNumberField);
        setupRoundedTextField(postalField);
        setupRoundedTextField(departmantField);
        setupRoundedTextField(dateHiredField);
        setupRoundedTextField(salaryField);
        setupRoundedTextField(jobTitleField);
        setupRoundedTextField(statusField);
    }

    private void addListeners() {
        addButton.addActionListener(e -> addEmployee());
        clearButton.addActionListener(e -> clearFields());
    }

    private void loadEmployeeTable() {
        try {
            EmployeeDAO dao = new EmployeeDAO();
            List<Employee> employees = dao.getAllEmployees();
            String[] columnNames = {"ID", "First Name", "Last Name", "Email", "DOB", "Contact", "Department", "Date Hired", "Salary", "Job Title", "Status"};
            DefaultTableModel model = new DefaultTableModel(columnNames, 0);
            for (Employee emp : employees) {
                model.addRow(new Object[]{
                        emp.getId(),
                        emp.getFirstName(),
                        emp.getLastName(),
                        emp.getEmail(),
                        emp.getDateOfBirth(),
                        emp.getContact(),
                        emp.getDepartment(),
                        emp.getDateHired(),
                        emp.getBasicSalary(),
                        emp.getJobTitle(),
                        emp.getStatus()
                });
            }
            pregledZaposlenikaTabela.setModel(model);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading employee data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void addEmployee() {
        try {
            String firstName = nameField.getText();
            String lastName = surnameField.getText();
            String dateOfBirth = dobField.getText().trim();
            String email = emailField.getText();
            String contact = contactField.getText();
            String address = adressField.getText();
            String aptHouseNo = houseNumberField.getText();
            String postCode = postalField.getText();
            String department = departmantField.getText();
            String dateHired = dateHiredField.getText().trim();
            String salaryText = salaryField.getText();
            String jobTitle = jobTitleField.getText();
            String status = statusField.getText();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            LocalDate dob = LocalDate.parse(dateOfBirth, formatter);
            LocalDate hiredDate = LocalDate.parse(dateHired, formatter);
            double basicSalary = Double.parseDouble(salaryText);
            Employee newEmployee = new Employee();
            newEmployee.setFirstName(firstName);
            newEmployee.setLastName(lastName);
            newEmployee.setDateOfBirth(dob);
            newEmployee.setEmail(email);
            newEmployee.setContact(contact);
            newEmployee.setAddress(address);
            newEmployee.setAptHouseNo(aptHouseNo);
            newEmployee.setPostCode(postCode);
            newEmployee.setDepartment(department);
            newEmployee.setDateHired(hiredDate);
            newEmployee.setBasicSalary(basicSalary);
            newEmployee.setJobTitle(jobTitle);
            newEmployee.setStatus(status);
            EmployeeDAO dao = new EmployeeDAO();
            dao.addEmployee(newEmployee);
            JOptionPane.showMessageDialog(this, "Employee added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            loadEmployeeTable();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error adding employee: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void clearFields() {
        idField.setText("");
        nameField.setText("");
        surnameField.setText("");
        dobField.setText("");
        emailField.setText("");
        contactField.setText("");
        adressField.setText("");
        houseNumberField.setText("");
        postalField.setText("");
        departmantField.setText("");
        dateHiredField.setText("");
        salaryField.setText("");
        jobTitleField.setText("");
        statusField.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ZaposlenikGUI gui = new ZaposlenikGUI();
            gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            gui.setSize(1200, 800);
            gui.setVisible(true);
        });
    }
}
