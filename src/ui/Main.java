package ui;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            LoginGUI loginGUI = new LoginGUI(); // Instanciraj Login GUI
            loginGUI.setVisible(true); // Prikaz GUI prozora
        });
    }
}
