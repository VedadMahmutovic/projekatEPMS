package ui;

import dao.UserDAO;
import dao.PayrollDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class IzvjestajGUI {
    private JPanel glavniIzvjestajPanel;
    private JTabbedPane glavniTabbedPanel;
    JPanel mjesecniIzvjestajBackPanel;
    private JPanel mjesecniIzvjestajMainPanel;
    private JTextField godinaField;
    private JButton generisiButton;
    private JComboBox<String> mjesecComboBox;
    private JTextField idZaposlenika;
    private JButton generisiIzvjestajButton;
    private JButton generisiIzvjestajSvihZapButton;
    JPanel izvjestajZapBackPanel;
    private JPanel izvjestajZapMainPanel;
    JPanel podesavanjePlataBackPanel;
    private JPanel podesavanjePlataMainPanel;
    private JTextField idZapPodesavanjaPlateField;
    private JTextField novaPlataField;
    private JButton promjeniPlatuButton;
    JPanel superAdminPassChngeBackPanel;
    private JPanel superAdminPassChngeMainPanel;
    JPanel passwordChangeBackPanel;
    private JPanel passwordChangeMainPanel;
    private JTextField superAdminIdKorisnikaField;
    private JTextField superAdminPassChangeField;
    private JButton superAdminPromjeniSifruButton;
    private JPasswordField staraSifraPasswordField;
    private JButton promjeniSifruButton;
    private JPasswordField novaSifraPasswordField;

    public IzvjestajGUI() {
        PayrollDAO payrollDAO = new PayrollDAO();
        UserDAO userDAO = new UserDAO();



        // Apply rounded borders to panels
        mjesecniIzvjestajMainPanel.setBorder(new RoundedBorder(20)); // Rounded border for month report panel
        mjesecniIzvjestajMainPanel.setOpaque(false);
        izvjestajZapMainPanel.setBorder(new RoundedBorder(20)); // Rounded border for employee report panel
        izvjestajZapMainPanel.setOpaque(false);
        podesavanjePlataMainPanel.setBorder(new RoundedBorder(20)); // Rounded border for employee report panel
        podesavanjePlataMainPanel.setOpaque(false);
        superAdminPassChngeMainPanel.setBorder(new RoundedBorder(20)); // Rounded border for employee report panel
        superAdminPassChngeMainPanel.setOpaque(false);
        passwordChangeMainPanel.setBorder(new RoundedBorder(20)); // Rounded border for employee report panel
        passwordChangeMainPanel.setOpaque(false);

        // Apply rounded borders to text fields
        setupRoundedTextField(godinaField);
        setupRoundedTextField(idZaposlenika);

        setupRoundedTextField(superAdminIdKorisnikaField);
        setupRoundedTextField(superAdminPassChangeField);
        setupRoundedPasswordField(staraSifraPasswordField);
        setupRoundedPasswordField(novaSifraPasswordField);

        setupRoundedTextField(idZapPodesavanjaPlateField);
        setupRoundedTextField(novaPlataField);

        generisiButton.addActionListener(e -> generateMonthlyReport(payrollDAO));
        generisiIzvjestajButton.addActionListener(e -> generateEmployeeReport(payrollDAO));
        generisiIzvjestajSvihZapButton.addActionListener(e -> generateFullEmployeeReport(payrollDAO));
        promjeniPlatuButton.addActionListener(e -> adjustSalary(payrollDAO));

        superAdminPromjeniSifruButton.addActionListener(e -> changePasswordAsSuperAdmin(userDAO));
        promjeniSifruButton.addActionListener(e -> changePasswordAsUser(userDAO));
    }

    private void setupRoundedTextField(JTextField textField) {
        textField.setBorder(new RoundedBorder(10));  // Adjust the value (e.g. 10) for the border radius
        textField.setOpaque(false);

        textField.setUI(new javax.swing.plaf.basic.BasicTextFieldUI() {
            @Override
            protected void paintSafely(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2.setColor(textField.getBackground());
                g2.fillRoundRect(0, 0, textField.getWidth(), textField.getHeight(), 10, 10);  // Same radius

                g2.setColor(Color.LIGHT_GRAY);
                g2.drawRoundRect(0, 0, textField.getWidth() - 1, textField.getHeight() - 1, 10, 10);

                g2.dispose();

                super.paintSafely(g);
            }
        });

        textField.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));  // Padding inside text field
    }

    private void setupRoundedPasswordField(JPasswordField passwordField) {
        passwordField.setBorder(new RoundedBorder(10));  // Adjust the value (e.g. 10) for the border radius
        passwordField.setOpaque(false);  // To make background transparent

        passwordField.setUI(new javax.swing.plaf.basic.BasicPasswordFieldUI() {
            @Override
            protected void paintSafely(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2.setColor(passwordField.getBackground());
                g2.fillRoundRect(0, 0, passwordField.getWidth(), passwordField.getHeight(), 10, 10);  // Same radius

                g2.setColor(Color.LIGHT_GRAY);
                g2.drawRoundRect(0, 0, passwordField.getWidth() - 1, passwordField.getHeight() - 1, 10, 10);

                g2.dispose();

                super.paintSafely(g);
            }
        });

        passwordField.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));  // Padding inside password field
    }

    private void generateMonthlyReport(PayrollDAO payrollDAO) {
        try {
            String godina = godinaField.getText(); // Dobijamo godinu
            String mjesec = (String) mjesecComboBox.getSelectedItem(); // Dobijamo odabrani mjesec

            // Ekstrakcija numeričkog dijela mjeseca
            String numerickiMjesec = mjesec.split("\\.")[0];
            numerickiMjesec = String.format("%02d", Integer.parseInt(numerickiMjesec)); // Dodajemo vodeću nulu

            // Provjera unosa
            if (godina.isEmpty() || numerickiMjesec.isEmpty()) {
                JOptionPane.showMessageDialog(
                        null,
                        "Molimo unesite validan mjesec i godinu.",
                        "Greška",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            // Poziv generisanja izvještaja
            List<String[]> report = payrollDAO.generateMonthlyReport(godina, numerickiMjesec);

            if (report.isEmpty()) {
                JOptionPane.showMessageDialog(
                        null,
                        "Nema podataka za zadati mjesec i godinu.",
                        "Mjesečni Izvještaj Plata",
                        JOptionPane.INFORMATION_MESSAGE
                );
                return;
            }

            // Prikaz izvještaja
            String[] columns = {"Employee ID", "Name", "Total Salary", "Month"};
            DefaultTableModel model = new DefaultTableModel(columns, 0);

            for (String[] row : report) {
                model.addRow(row);
            }

            JTable reportTable = new JTable(model);
            JScrollPane scrollPane = new JScrollPane(reportTable);
            reportTable.setFillsViewportHeight(true);

            reportTable.setBackground(new Color(39, 83, 56));
            reportTable.setForeground(new Color(173, 212, 190));
            reportTable.setGridColor(new Color(173, 212, 190));
            reportTable.setSelectionBackground(new Color(25, 58, 39));
            reportTable.setSelectionForeground(new Color(173, 212, 190));

            JFrame popupFrame = new JFrame("Mjesečni Izvještaj Plata");
            popupFrame.setSize(800, 600);
            popupFrame.add(scrollPane, BorderLayout.CENTER);
            popupFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            popupFrame.setVisible(true);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    null,
                    "Greška: " + ex.getMessage(),
                    "Greška",
                    JOptionPane.ERROR_MESSAGE
            );
            ex.printStackTrace();
        }
    }

    private void generateEmployeeReport(PayrollDAO payrollDAO) {
        try {
            String input = idZaposlenika.getText(); // Get entered employee IDs
            if (input.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Unesite ID zaposlenika.", "Greška", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Parse multiple IDs
            String[] idStrings = input.split(",");
            int[] employeeIds = new int[idStrings.length];
            for (int i = 0; i < idStrings.length; i++) {
                employeeIds[i] = Integer.parseInt(idStrings[i].trim());
            }

            // Fetch data for the given IDs
            List<String[]> report = payrollDAO.getEmployeeReportByIds(employeeIds);

            if (report.isEmpty()) {
                JOptionPane.showMessageDialog(
                        null,
                        "Nema podataka za zadate zaposlenike.",
                        "Izvještaj Zaposlenika",
                        JOptionPane.INFORMATION_MESSAGE
                );
                return;
            }

            // Display the report in a table
            displayTable(report, "Izvještaj Zaposlenika");

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "ID zaposlenika mora biti broj.", "Greška", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Greška: " + ex.getMessage(), "Greška", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void generateFullEmployeeReport(PayrollDAO payrollDAO) {
        try {
            // Fetch data for all employees
            List<String[]> report = payrollDAO.getFullEmployeeReport();

            if (report.isEmpty()) {
                JOptionPane.showMessageDialog(
                        null,
                        "Nema podataka u bazi.",
                        "Izvještaj Zaposlenika",
                        JOptionPane.INFORMATION_MESSAGE
                );
                return;
            }

            // Display the report in a table
            displayTable(report, "Izvještaj Zaposlenika - Svi Zaposlenici");

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Greška: " + ex.getMessage(), "Greška", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void displayTable(List<String[]> report, String title) {
        String[] columns = {
                "Employee ID", "Name", "Email", "Contact", "Department", "Job Title", "Basic Salary",
                "Payroll ID", "Salary", "Bonus", "Deductions", "Tax", "Pay Date", "Net Salary"
        };

        DefaultTableModel model = new DefaultTableModel(columns, 0);
        for (String[] row : report) {
            model.addRow(row);
        }

        JTable reportTable = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(reportTable);
        reportTable.setFillsViewportHeight(true);

        reportTable.setBackground(new Color(39, 83, 56));
        reportTable.setForeground(new Color(173, 212, 190));
        reportTable.setGridColor(new Color(173, 212, 190));
        reportTable.setSelectionBackground(new Color(25, 58, 39));
        reportTable.setSelectionForeground(new Color(173, 212, 190));

        JFrame popupFrame = new JFrame(title);
        popupFrame.setSize(1000, 600);
        popupFrame.add(scrollPane, BorderLayout.CENTER);
        popupFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        popupFrame.setVisible(true);
    }

    private void adjustSalary(PayrollDAO payrollDAO) {
        try {
            String employeeIdText = idZapPodesavanjaPlateField.getText(); // Get entered Employee ID
            String newSalaryText = novaPlataField.getText(); // Get entered new salary

            // Validate inputs
            if (employeeIdText.isEmpty() || newSalaryText.isEmpty()) {
                JOptionPane.showMessageDialog(
                        null,
                        "Sva polja su obavezna.",
                        "Greška",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            int employeeId = Integer.parseInt(employeeIdText);
            double newSalary = Double.parseDouble(newSalaryText);

            if (newSalary <= 0) {
                JOptionPane.showMessageDialog(
                        null,
                        "Plata mora biti veća od 0.",
                        "Greška",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            // Fetch employee name
            String employeeName = payrollDAO.getEmployeeName(employeeId);
            if (employeeName == null) {
                JOptionPane.showMessageDialog(
                        null,
                        "Zaposlenik s ID-jem " + employeeId + " nije pronađen.",
                        "Greška",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            // Display confirmation popup
            int confirm = JOptionPane.showConfirmDialog(
                    null,
                    "Jeste li sigurni da želite promijeniti platu za:\n" +
                            "ID: " + employeeId + "\n" +
                            "Ime: " + employeeName + "\n" +
                            "Nova plata: " + newSalary,
                    "Potvrda promjene plate",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm != JOptionPane.YES_OPTION) {
                // Cancel update if the user chooses "No"
                JOptionPane.showMessageDialog(
                        null,
                        "Ažuriranje plate je otkazano.",
                        "Obavijest",
                        JOptionPane.INFORMATION_MESSAGE
                );
                return;
            }

            // Update salary in the database
            boolean success = payrollDAO.adjustSalary(employeeId, newSalary);

            if (success) {
                JOptionPane.showMessageDialog(
                        null,
                        "Plata uspješno ažurirana za zaposlenika:\n" +
                                "ID: " + employeeId + "\n" +
                                "Ime: " + employeeName,
                        "Uspjeh",
                        JOptionPane.INFORMATION_MESSAGE
                );
            } else {
                JOptionPane.showMessageDialog(
                        null,
                        "Greška prilikom ažuriranja plate za zaposlenika ID " + employeeId,
                        "Greška",
                        JOptionPane.ERROR_MESSAGE
                );
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(
                    null,
                    "Unesite ispravan ID i platu.",
                    "Greška",
                    JOptionPane.ERROR_MESSAGE
            );
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    null,
                    "Greška: " + ex.getMessage(),
                    "Greška",
                    JOptionPane.ERROR_MESSAGE
            );
            ex.printStackTrace();
        }
    }

    private void changePasswordAsSuperAdmin(UserDAO userDAO) {
        try {
            String userIdText = superAdminIdKorisnikaField.getText().trim();
            String newPassword = superAdminPassChangeField.getText().trim();

            // Validate inputs
            if (userIdText.isEmpty() || newPassword.isEmpty()) {
                JOptionPane.showMessageDialog(
                        null,
                        "ID korisnika i nova šifra su obavezni.",
                        "Greška",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            int userId = Integer.parseInt(userIdText);

            // Fetch user's name from database
            String userName = userDAO.getUserNameById(userId); // Implement this in UserDAO

            if (userName == null) {
                JOptionPane.showMessageDialog(
                        null,
                        "Korisnik s ID-jem " + userId + " nije pronađen.",
                        "Greška",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            // Show confirmation popup
            int confirm = JOptionPane.showConfirmDialog(
                    null,
                    "Jeste li sigurni da želite promijeniti šifru za korisnika:\n" +
                            "ID: " + userId + "\n" +
                            "Ime: " + userName,
                    "Potvrda promjene šifre",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm != JOptionPane.YES_OPTION) {
                JOptionPane.showMessageDialog(
                        null,
                        "Promjena šifre otkazana.",
                        "Obavijest",
                        JOptionPane.INFORMATION_MESSAGE
                );
                return;
            }

            // Change password
            boolean success = userDAO.changePasswordById(userId, newPassword); // Implement this in UserDAO

            if (success) {
                JOptionPane.showMessageDialog(
                        null,
                        "Šifra uspješno promijenjena za korisnika:\n" +
                                "ID: " + userId + "\n" +
                                "Ime: " + userName,
                        "Uspjeh",
                        JOptionPane.INFORMATION_MESSAGE
                );
                superAdminIdKorisnikaField.setText("");
                superAdminPassChangeField.setText("");
            } else {
                JOptionPane.showMessageDialog(
                        null,
                        "Greška prilikom promjene šifre.",
                        "Greška",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(
                    null,
                    "ID korisnika mora biti broj.",
                    "Greška",
                    JOptionPane.ERROR_MESSAGE
            );
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    null,
                    "Greška: " + ex.getMessage(),
                    "Greška",
                    JOptionPane.ERROR_MESSAGE
            );
            ex.printStackTrace();
        }
    }

    private void changePasswordAsUser(UserDAO userDAO) {
        try {
            // Get currently logged-in user
            String loggedInUser = LoginGUI.loggedInUser;

            if (loggedInUser == null || loggedInUser.isEmpty()) {
                JOptionPane.showMessageDialog(
                        null,
                        "No user is currently logged in.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            // Retrieve input from password fields
            String currentPassword = new String(staraSifraPasswordField.getPassword());
            String newPassword = new String(novaSifraPasswordField.getPassword());

            // Validate inputs
            if (currentPassword.isEmpty() || newPassword.isEmpty()) {
                JOptionPane.showMessageDialog(
                        null,
                        "Both the current and new password fields are required.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            // Confirm the password change action
            int confirm = JOptionPane.showConfirmDialog(
                    null,
                    "Are you sure you want to change your password?",
                    "Confirm Password Change",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm != JOptionPane.YES_OPTION) {
                JOptionPane.showMessageDialog(
                        null,
                        "Password change canceled.",
                        "Info",
                        JOptionPane.INFORMATION_MESSAGE
                );
                return;
            }

            // Attempt password change
            boolean success = userDAO.changePassword(loggedInUser, currentPassword, newPassword);

            if (success) {
                JOptionPane.showMessageDialog(
                        null,
                        "Password changed successfully.",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE
                );
                staraSifraPasswordField.setText("");
                novaSifraPasswordField.setText("");
            } else {
                JOptionPane.showMessageDialog(
                        null,
                        "Password change failed. Check your current password.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    null,
                    "An error occurred: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            ex.printStackTrace();
        }
    }



    public static void main(String[] args) {
        JFrame frame = new JFrame("IzvjestajGUI");
        frame.setContentPane(new IzvjestajGUI().glavniIzvjestajPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
