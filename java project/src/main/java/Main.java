package main.java;

import main.java.db.DatabaseHandler;
import main.java.ui.DashboardPanel;
import main.java.ui.LoginPanel;
import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel("com.formdev.flatlaf.FlatLightLaf");
            } catch (Exception e) {
                e.printStackTrace();
            }

            JFrame frame = new JFrame("Inventory Management System");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1000, 700);
            frame.setLocationRelativeTo(null);

            CardLayout cardLayout = new CardLayout();
            JPanel mainPanel = new JPanel(cardLayout);

            DatabaseHandler dbHandler = new DatabaseHandler();
            LoginPanel loginPanel = new LoginPanel(cardLayout, mainPanel, dbHandler);
            DashboardPanel dashboardPanel = new DashboardPanel(cardLayout, mainPanel, dbHandler);

            mainPanel.add(loginPanel, "Login");
            mainPanel.add(dashboardPanel, "Dashboard");

            frame.add(mainPanel);
            cardLayout.show(mainPanel, "Login");
            frame.setVisible(true);
        });
        
    }
}