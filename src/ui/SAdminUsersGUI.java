package ui;

import dao.UserDAO;
import model.User;

import javax.swing.*;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.plaf.basic.BasicTextFieldUI;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.net.URL;
import java.util.List;

public class SAdminUsersGUI {
    private JPanel urediZapPanel;
    private JTextField pretraziPolje;
    private JTextField userIdField;
    private JTextField userImeField;
    private JTextField userPassField;
    private JComboBox<String> ulogaComboBox;
    private JButton dodajButton;
    private JButton obrisiButton;
    private JButton pretraziButton;
    JPanel sAdminUsersBackPanel;
    private JButton izmjeniButton;

    private UserDAO userDAO = new UserDAO();

    public SAdminUsersGUI() {

        scaleIconsOnResize();


        setupRoundedPanel(urediZapPanel);
        setupRoundedTextField(pretraziPolje);
        setupRoundedTextField(userIdField);
        setupRoundedTextField(userImeField);
        setupRoundedTextField(userPassField);

        setupRoundedButton(dodajButton);
        setupRoundedButton(obrisiButton);
        setupRoundedButton(izmjeniButton);

        pretraziButton.addActionListener(e -> izvrsiPretragu());
        pretraziPolje.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    izvrsiPretragu();
                }
            }
        });

        dodajButton.addActionListener(e -> dodajNovogKorisnika());
        izmjeniButton.addActionListener(e -> izmjeniKorisnika());
        obrisiButton.addActionListener(e -> obrisiKorisnika());
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


        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                button.setBackground(button.getBackground().brighter());
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                button.setBackground(Color.decode("#4D7B66"));
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

    private void setupRoundedPanel(JPanel panel) {
        panel.setBorder(new RoundedBorder(20));
        panel.setOpaque(false);
    }

    private void izvrsiPretragu() {
        String searchQuery = pretraziPolje.getText().trim();

        List<Object[]> results;

        if (searchQuery.isEmpty()) {

            results = userDAO.getAllUsersAsList();
        } else {

            results = userDAO.searchUsersByNameOrRole(searchQuery);
        }

        if (results.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Nema rezultata za uneseni kriterij pretrage.", "Rezultati pretrage", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Prikaz rezultata u JTable
        DefaultTableModel model = new DefaultTableModel(new String[]{"ID", "Ime", "Uloga"}, 0);
        for (Object[] row : results) {
            model.addRow(row);
        }

        JTable resultTable = new JTable(model);
        resultTable.setFillsViewportHeight(true);

        resultTable.setBackground(new Color(39, 83, 56));
        resultTable.setForeground(new Color(173, 212, 190));
        resultTable.setGridColor(new Color(173, 212, 190));
        resultTable.setSelectionBackground(new Color(25, 58, 39));
        resultTable.setSelectionForeground(new Color(173, 212, 190));

        JDialog dialog = new JDialog();
        dialog.setTitle("Rezultati pretrage");
        dialog.setSize(600, 400);
        dialog.setLocationRelativeTo(null);
        dialog.add(resultTable);
        dialog.setVisible(true);
    }


    private void dodajNovogKorisnika() {
        String ime = userImeField.getText().trim();
        String password = userPassField.getText().trim();
        String role = (String) ulogaComboBox.getSelectedItem();

        // Validacija unosa
        if (ime.isEmpty() || password.isEmpty() || role == null) {
            JOptionPane.showMessageDialog(null, "Molimo popunite sva polja.", "Greška", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Poziv metode iz UserDAO
        boolean success = userDAO.addUserAndMapToEmployee(ime, password, role);

        // Povratne informacije korisniku
        if (success) {
            JOptionPane.showMessageDialog(null, "Korisnik uspešno dodat i povezan sa zaposlenikom.", "Uspjeh", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "Došlo je do greške pri dodavanju korisnika.", "Greška", JOptionPane.ERROR_MESSAGE);
        }
    }



    private void izmjeniKorisnika() {
        try {

            String idText = userIdField.getText().trim();
            if (idText.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Molimo unesite ID korisnika.", "Greška", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int userId = Integer.parseInt(idText);

            String ime = userImeField.getText().trim();
            String password = userPassField.getText().trim();
            String role = (String) ulogaComboBox.getSelectedItem();

            if (ime.isEmpty() && password.isEmpty() && role == null) {
                JOptionPane.showMessageDialog(null, "Molimo unesite bar jedno polje za izmjenu.", "Greška", JOptionPane.ERROR_MESSAGE);
                return;
            }

            boolean success = false;

            if (!ime.isEmpty()) {
                success |= userDAO.updateUserIme(userId, ime);
            }
            if (!password.isEmpty()) {
                success |= userDAO.changePasswordById(userId, password);
            }
            if (role != null) {
                success |= userDAO.updateUserRole(userId, role);
            }

            if (success) {
                JOptionPane.showMessageDialog(null, "Korisnik uspješno izmijenjen.", "Uspjeh", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Došlo je do greške pri izmjeni korisnika. Provjerite ID ili unesene podatke.", "Greška", JOptionPane.ERROR_MESSAGE);
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Molimo unesite važeći ID korisnika.", "Greška", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Neočekivana greška: " + e.getMessage(), "Greška", JOptionPane.ERROR_MESSAGE);
        }
    }


    private void obrisiKorisnika() {
        try {

            String idText = userIdField.getText().trim();
            if (idText.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Molimo unesite ID korisnika.", "Greška", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int userId = Integer.parseInt(idText);

            User user = userDAO.getUserById(userId);
            if (user != null) {

                int confirm = JOptionPane.showConfirmDialog(
                        null,
                        String.format("Da li ste sigurni da želite obrisati korisnika '%s' sa ulogom '%s'?", user.getIme(), user.getRole()),
                        "Potvrda brisanja",
                        JOptionPane.YES_NO_OPTION
                );

                if (confirm == JOptionPane.YES_OPTION) {
                    boolean success = userDAO.deleteUser(userId);
                    if (success) {
                        JOptionPane.showMessageDialog(null, "Korisnik uspješno obrisan.", "Uspjeh", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null, "Došlo je do greške pri brisanju korisnika.", "Greška", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(null, "Korisnik sa ovim ID-em ne postoji.", "Greška", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Molimo unesite važeći ID korisnika.", "Greška", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Neočekivana greška: " + e.getMessage(), "Greška", JOptionPane.ERROR_MESSAGE);
        }
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


    public static void main(String[] args) {
        JFrame frame = new JFrame("SAdminUsersGUI");
        frame.setContentPane(new SAdminUsersGUI().sAdminUsersBackPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
