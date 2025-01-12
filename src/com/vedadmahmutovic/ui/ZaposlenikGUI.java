package com.vedadmahmutovic.ui;

import com.vedadmahmutovic.dao.EmployeeDAO;
import com.vedadmahmutovic.model.Employee;

import javax.swing.*;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.plaf.basic.BasicTextFieldUI;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.RenderingHints;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ZaposlenikGUI extends JFrame {
    private JTabbedPane glavniTabbedPanel;
    private JPanel glavniPanel;
    // Dodavanje Zaposlenika
    private JTextField idField, nameField, surnameField, emailField, dobField, contactField, adressField, houseNumberField, postalField, departmantField, dateHiredField, salaryField, jobTitleField, statusField;

    JPanel backgroundPanel;
    private JPanel dodajZaposlenikaPanel;
    private JButton deleteButton;
    private JButton clearButton;
    private JButton addButton;
    private JPanel pregledZaposlenikaPanel;
    private JTable pregledZaposlenikaTabela;
    JPanel urediZapBackPanel;
    private JPanel urediZapPanel;
    JScrollPane pregledZapPanel;

    private JTextField pretraziPolje, uzIdZaUreditiPolje, uzImePolje, uzPrezime, uzEmailPolje, uzDobPolje, uzKontaktPolje, uzAdresaPolje, uzHNPolje, uzPostalPolje,  uzOdjelPolje, uzDatumPolje, uzPlataPolje, uzNRMPolje, uzStatusPolje;

    private JButton uzUpdateButton;
    private JButton uzClearButton;
    private JButton pretraziButton;

    public ZaposlenikGUI() {
        setupUI();
        addListeners();
        loadEmployeeTable();
        scaleIconsOnResize();
    }

    private void setupRoundedButton(JButton button) {
        button.setUI(new BasicButtonUI() {
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

                g2.setColor(c.getForeground());
                g2.drawRoundRect(0, 0, c.getWidth() - 1, c.getHeight() - 1, 20, 20);

                g2.dispose();
            }
        });

        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);

        button.setBackground(Color.decode("#4D7B66"));
        button.setForeground(Color.decode("#DDF5E5"));

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


    private void setupRoundedTextField(JTextField textField) {
        textField.setBorder(new RoundedBorder(10));

        textField.setOpaque(false);

        textField.setUI(new BasicTextFieldUI() {
            @Override
            protected void paintSafely(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2.setColor(textField.getBackground());
                g2.fillRoundRect(0, 0, textField.getWidth(), textField.getHeight(), 10, 10);

                g2.setColor(Color.LIGHT_GRAY);
                g2.drawRoundRect(0, 0, textField.getWidth() - 1, textField.getHeight() - 1, 10, 10);

                g2.dispose();

                super.paintSafely(g);
            }
        });

        textField.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
    }

    private void setupUI() {
        dodajZaposlenikaPanel.setBorder(new RoundedBorder(20));
        dodajZaposlenikaPanel.setOpaque(false);

        urediZapPanel.setBorder(new RoundedBorder(20));
        urediZapPanel.setOpaque(false);

        setupRoundedButton(deleteButton);
        setupRoundedButton(clearButton);
        setupRoundedButton(addButton);
        setupRoundedButton(uzUpdateButton); // Added
        setupRoundedButton(uzClearButton); // Added
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
        setupRoundedTextField(pretraziPolje);
        setupRoundedTextField(uzIdZaUreditiPolje);
        setupRoundedTextField(uzImePolje);
        setupRoundedTextField(uzPrezime);
        setupRoundedTextField(uzEmailPolje);
        setupRoundedTextField(uzDobPolje);
        setupRoundedTextField(uzKontaktPolje);
        setupRoundedTextField(uzAdresaPolje);
        setupRoundedTextField(uzHNPolje);
        setupRoundedTextField(uzPostalPolje);
        setupRoundedTextField(uzOdjelPolje);
        setupRoundedTextField(uzDatumPolje);
        setupRoundedTextField(uzPlataPolje);
        setupRoundedTextField(uzNRMPolje);
        setupRoundedTextField(uzStatusPolje);
    }

    private void addListeners() {
        addButton.addActionListener(e -> addEmployee());
        clearButton.addActionListener(e -> clearFields());
        uzUpdateButton.addActionListener(e -> updateEmployee());
        uzClearButton.addActionListener(e -> clearUzFields());

        pretraziPolje.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
                    searchEmployees(pretraziPolje.getText());
                }
            }
        });



        pretraziButton.addActionListener(e -> searchEmployees(pretraziPolje.getText()));

        deleteButton.addActionListener(e -> {
            String idText = idField.getText().trim();  // Get ID from input field
            if (idText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter an employee ID to delete.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                int employeeId = Integer.parseInt(idText);
                EmployeeDAO dao = new EmployeeDAO();
                Employee employee = dao.getEmployeeById(employeeId);

                if (employee == null) {
                    JOptionPane.showMessageDialog(this, "Employee not found with ID: " + employeeId, "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int confirmation = JOptionPane.showConfirmDialog(this,
                        "Are you sure you would like to delete employee ID " + employeeId +
                                ": " + employee.getFirstName() + " " + employee.getLastName() + "?",
                        "Delete Employee", JOptionPane.YES_NO_OPTION);

                if (confirmation == JOptionPane.YES_OPTION) {
                    dao.deleteEmployee(employeeId);  // Delete the employee
                    JOptionPane.showMessageDialog(this, "Employee deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    loadEmployeeTable();  // Refresh the employee table
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid ID format. Please enter a valid numeric ID.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error deleting employee: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });
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

    private void searchEmployees(String query) {
        try {
            EmployeeDAO dao = new EmployeeDAO();
            List<Employee> employees = dao.searchEmployees(query);

            if (employees.isEmpty()) {

                JOptionPane.showMessageDialog(this, "No employees found matching the search term.", "Search Result", JOptionPane.INFORMATION_MESSAGE);
            } else {
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

                JTable resultTable = new JTable(model);
                resultTable.setFillsViewportHeight(true);

                resultTable.setBackground(new Color(39, 83, 56));  // Set the background color
                resultTable.setForeground(new Color(173, 212, 190));  // Set the text color
                resultTable.setGridColor(new Color(173, 212, 190));  // Set the grid color
                resultTable.setSelectionBackground(new Color(25, 58, 39));  // Set the selection background color
                resultTable.setSelectionForeground(new Color(173, 212, 190));  // Set the selection text color

                JScrollPane scrollPane = new JScrollPane(resultTable);

                JDialog dialog = new JDialog(this, "Search Results", true);
                dialog.setSize(800, 400);
                dialog.setLocationRelativeTo(this);  // Center the dialog
                dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                dialog.add(scrollPane);  // Add the scroll pane containing the table

                dialog.setVisible(true);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error searching employees: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void updateEmployee() {
        try {

            String employeeId = uzIdZaUreditiPolje.getText().trim();
            if (employeeId.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter an employee ID to update.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            EmployeeDAO dao = new EmployeeDAO();
            Employee employee = dao.getEmployeeById(Integer.parseInt(employeeId));
            if (employee == null) {
                JOptionPane.showMessageDialog(this, "Employee not found.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String firstName = uzImePolje.getText().trim();
            if (!firstName.isEmpty()) {
                employee.setFirstName(firstName);
            }
            String lastName = uzPrezime.getText().trim();
            if (!lastName.isEmpty()) {
                employee.setLastName(lastName);
            }
            String email = uzEmailPolje.getText().trim();
            if (!email.isEmpty()) {
                employee.setEmail(email);
            }
            String dob = uzDobPolje.getText().trim();
            if (!dob.isEmpty()) {
                employee.setDateOfBirth(LocalDate.parse(dob, DateTimeFormatter.ofPattern("dd-MM-yyyy")));
            }
            String contact = uzKontaktPolje.getText().trim();
            if (!contact.isEmpty()) {
                employee.setContact(contact);
            }
            String address = uzAdresaPolje.getText().trim();
            if (!address.isEmpty()) {
                employee.setAddress(address);
            }
            String aptHouseNo = uzHNPolje.getText().trim();
            if (!aptHouseNo.isEmpty()) {
                employee.setAptHouseNo(aptHouseNo);
            }
            String postCode = uzPostalPolje.getText().trim();
            if (!postCode.isEmpty()) {
                employee.setPostCode(postCode);
            }
            String department = uzOdjelPolje.getText().trim();
            if (!department.isEmpty()) {
                employee.setDepartment(department);
            }
            String hiredDate = uzDatumPolje.getText().trim();
            if (!hiredDate.isEmpty()) {
                employee.setDateHired(LocalDate.parse(hiredDate, DateTimeFormatter.ofPattern("dd-MM-yyyy")));
            }
            String salaryText = uzPlataPolje.getText().trim();
            if (!salaryText.isEmpty()) {
                employee.setBasicSalary(Double.parseDouble(salaryText));
            }
            String jobTitle = uzNRMPolje.getText().trim();
            if (!jobTitle.isEmpty()) {
                employee.setJobTitle(jobTitle);
            }
            String status = uzStatusPolje.getText().trim();
            if (!status.isEmpty()) {
                employee.setStatus(status);
            }

            int option = JOptionPane.showConfirmDialog(this, "Are you sure you want to update the employee's details?",
                    "Confirm Update", JOptionPane.YES_NO_OPTION);
            if (option == JOptionPane.YES_OPTION) {
                dao.updateEmployee(employee);
                JOptionPane.showMessageDialog(this, "Employee updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadEmployeeTable();
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error updating employee: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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

    private void clearUzFields() {
        uzIdZaUreditiPolje.setText("");
        uzImePolje.setText("");
        uzPrezime.setText("");
        uzDobPolje.setText("");
        uzEmailPolje.setText("");
        uzKontaktPolje.setText("");
        uzAdresaPolje.setText("");
        uzHNPolje.setText("");
        uzPostalPolje.setText("");
        uzOdjelPolje.setText("");
        uzDatumPolje.setText("");
        uzPlataPolje.setText("");
        uzNRMPolje.setText("");
        uzStatusPolje.setText("");
    }

    private void scaleIconWithButton(JButton button, String iconPath) {
        URL iconURL = getClass().getResource(iconPath);
        if (iconURL != null) {
            ImageIcon originalIcon = new ImageIcon(iconURL);
            button.addComponentListener(new java.awt.event.ComponentAdapter() {
                @Override
                public void componentResized(java.awt.event.ComponentEvent e) {
                    int width = button.getWidth();
                    int height = button.getHeight();
                    if (width > 0 && height > 0) {
                        Image scaledImage = originalIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
                        button.setIcon(new ImageIcon(scaledImage));
                    }
                }
            });

            int initialWidth = button.getWidth();
            int initialHeight = button.getHeight();
            if (initialWidth > 0 && initialHeight > 0) {
                Image initialScaledImage = originalIcon.getImage().getScaledInstance(initialWidth, initialHeight, Image.SCALE_SMOOTH);
                button.setIcon(new ImageIcon(initialScaledImage));
            }
        } else {
            System.err.println("Icon not found at path: " + iconPath);
        }
    }


    private void scaleIconsOnResize() {
        pretraziButton.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                scaleIconWithButton(pretraziButton, "/ikone/SearchIkona.png");

            }
        });
    }

}
