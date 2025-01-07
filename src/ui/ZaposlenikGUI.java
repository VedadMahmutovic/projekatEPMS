package ui;

import javax.swing.*;

public class ZaposlenikGUI {
    private JTabbedPane tabbedPane1;
    private JPanel panel1;

    public static void main(String[] args) {
        JFrame frame = new JFrame("ZaposlenikGUI");
        frame.setContentPane(new ZaposlenikGUI().panel1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
