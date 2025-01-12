package ui;

import dao.PayrollDAO;
import model.Payroll;

import javax.swing.*;
import javax.swing.plaf.basic.BasicTextFieldUI;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class PlataGUI {
    private JPanel glavniPanel;
    private JTabbedPane glavniTabbedPanel;
    JPanel genPlatListuBackPanel;
    private JPanel genPlatListuMainPanel;
    private JTextField idField;
    private JTextField datumField;
    private JButton generisiButton;
    private JTextArea statusTextArea;
    private JPanel obracunPlateGlavniPanel;
    JPanel obracunPlateBackPanel;
    private JTextField obracunIDField;
    private JButton obracunajButton;
    private JTextArea obracunajStatusTextArea;
    JPanel pregledIsplataBackPanel;
    private JButton pregledIsplataButton;
    private JTextField pregledIsplataField;
    private JTextField pregledIsplataOdDatumField;
    private JTextField pregledIsplataDoDatumField;
    private JPanel pregledIsplataMainPanel;
    private JTextField bonusField;
    private JTextField dedukcijaField;

    public PlataGUI() {
        PayrollDAO payrollDAO = new PayrollDAO();

        setupUI();

        generisiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generisiPlatnuListu(payrollDAO);
            }
        });

        obracunajButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                calculateNetSalary(payrollDAO);
            }
        });

        pregledIsplataButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewPaymentHistory(payrollDAO);
            }
        });
    }

    private void setupUI() {

        genPlatListuMainPanel.setBorder(new RoundedBorder(20));
        genPlatListuMainPanel.setOpaque(false);
        obracunPlateGlavniPanel.setBorder(new RoundedBorder(20));
        obracunPlateGlavniPanel.setOpaque(false);
        pregledIsplataMainPanel.setBorder(new RoundedBorder(20));
        pregledIsplataMainPanel.setOpaque(false);

        setupRoundedTextField(idField);
        setupRoundedTextField(datumField);
        setupRoundedTextField(obracunIDField);
        setupRoundedTextField(pregledIsplataField);
        setupRoundedTextField(pregledIsplataOdDatumField);
        setupRoundedTextField(pregledIsplataDoDatumField);
        setupRoundedTextField(bonusField);
        setupRoundedTextField(dedukcijaField);
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

    private void generisiPlatnuListu(PayrollDAO payrollDAO) {
        try {
            String employeeIds = idField.getText().trim();
            String payDate = datumField.getText().trim();
            double bonus = bonusField.getText().isEmpty() ? 0.0 : Double.parseDouble(bonusField.getText().trim());
            double deduction = dedukcijaField.getText().isEmpty() ? 0.0 : Double.parseDouble(dedukcijaField.getText().trim());

            if (payDate.isEmpty()) {
                statusTextArea.setText("❌ Unesite datum isplate.");
                return;
            }

            int response = JOptionPane.showConfirmDialog(
                    null,
                    "Da li ste sigurni da želite da generišete platnu listu za ID zaposlenih: " + employeeIds + "?",
                    "Potvrda",
                    JOptionPane.YES_NO_OPTION
            );

            if (response == JOptionPane.YES_OPTION) {
                boolean payrollGenerated = payrollDAO.generatePayroll(employeeIds, payDate, bonus, deduction);

                if (payrollGenerated) {
                    statusTextArea.setText("✅ Platna lista uspešno generisana za zaposlene ID: " + employeeIds);
                } else {
                    statusTextArea.setText("❌ Neuspešno generisanje platne liste za neke ili sve ID-ove: " + employeeIds);
                }
            } else {
                statusTextArea.setText("❌ Akcija otkazana.");
            }
        } catch (NumberFormatException ex) {
            statusTextArea.setText("❌ ID zaposlenika, bonus i odbitak moraju biti ispravno uneti brojevi.");
        } catch (Exception ex) {
            statusTextArea.setText("❌ Greška: " + ex.getMessage());
            ex.printStackTrace();
        }
    }




    private void calculateNetSalary(PayrollDAO payrollDAO) {
        try {
            int employeeId = Integer.parseInt(obracunIDField.getText());


            String employeeName = payrollDAO.getEmployeeName(employeeId);

            if (employeeName == null) {
                obracunajStatusTextArea.setText("❌ Zaposlenik s unesenim ID-jem ne postoji.");
                return;
            }

            List<Payroll> payrolls = payrollDAO.getPaymentHistory(employeeId);

            if (!payrolls.isEmpty()) {
                double totalNetSalary = 0.0;

                for (Payroll payroll : payrolls) {
                    double netSalary = payroll.getSalary() + payroll.getBonus() - payroll.getDeductions() - payroll.getTax();
                    totalNetSalary += netSalary;
                }

                obracunajStatusTextArea.setText(
                        "✅ Neto plata za zaposlenika: " + employeeName + " (ID: " + employeeId + "): " + totalNetSalary

                );
            } else {
                obracunajStatusTextArea.setText("❌ Platni zapisi nisu pronađeni za zaposlenika: " + employeeName + " (ID: " + employeeId + ")");
            }
        } catch (NumberFormatException ex) {
            obracunajStatusTextArea.setText("❌ ID zaposlenika mora biti broj.");
        } catch (Exception ex) {
            obracunajStatusTextArea.setText("❌ Greška: " + ex.getMessage());
            ex.printStackTrace();
        }
    }


    private void viewPaymentHistory(PayrollDAO payrollDAO) {
        try {
            String startDate = pregledIsplataOdDatumField.getText();
            String endDate = pregledIsplataDoDatumField.getText();
            String employeeIdText = pregledIsplataField.getText();

            if (startDate.isEmpty() || endDate.isEmpty()) {
                statusTextArea.setText("❌ Molimo unesite datumski raspon.");
                return;
            }

            List<Payroll> payments;

            if (employeeIdText.isEmpty()) {
                payments = payrollDAO.getPaymentWithEmployeeId(startDate, endDate, null);
            } else {
                try {
                    int employeeId = Integer.parseInt(employeeIdText);
                    payments = payrollDAO.getPaymentWithEmployeeId(startDate, endDate, employeeId);
                } catch (NumberFormatException e) {
                    statusTextArea.setText("❌ ID zaposlenika mora biti broj.");
                    return;
                }
            }

            if (payments.isEmpty()) {
                statusTextArea.setText("❌ Nisu pronađeni platni zapisi u zadatom opsegu.");
                return;
            }

            JFrame popupFrame = new JFrame("Pregled Isplata");
            popupFrame.setSize(800, 600);
            popupFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

            String[] columns = {"Payroll ID", "Employee ID", "Salary", "Bonus", "Deductions", "Tax", "Pay Date"};
            DefaultTableModel model = new DefaultTableModel(columns, 0);
            for (Payroll payroll : payments) {
                model.addRow(new Object[]{
                        payroll.getId(),
                        payroll.getEmployeeId(),
                        payroll.getSalary(),
                        payroll.getBonus(),
                        payroll.getDeductions(),
                        payroll.getTax(),
                        payroll.getPayDate()
                });
            }

            JTable paymentHistoryTable = new JTable(model);
            JScrollPane scrollPane = new JScrollPane(paymentHistoryTable);
            paymentHistoryTable.setFillsViewportHeight(true);

            paymentHistoryTable.setBackground(new Color(39, 83, 56));
            paymentHistoryTable.setForeground(new Color(173, 212, 190));
            paymentHistoryTable.setGridColor(new Color(173, 212, 190));
            paymentHistoryTable.setSelectionBackground(new Color(25, 58, 39));
            paymentHistoryTable.setSelectionForeground(new Color(173, 212, 190));

            popupFrame.add(scrollPane, BorderLayout.CENTER);
            popupFrame.setVisible(true);

        } catch (Exception ex) {
            statusTextArea.setText("❌ Greška: " + ex.getMessage());
            ex.printStackTrace();
        }
    }


    public static void main(String[] args) {
        JFrame frame = new JFrame("Plata GUI");
        frame.setContentPane(new PlataGUI().glavniPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
