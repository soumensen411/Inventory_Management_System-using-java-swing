package main.java.ui;

import main.java.db.DatabaseHandler;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class LoginPanel extends JPanel {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private DatabaseHandler dbHandler;
    private JButton loginButton;

    public LoginPanel(CardLayout cardLayout, JPanel mainPanel, DatabaseHandler dbHandler) {
        this.dbHandler = dbHandler;
        setLayout(new BorderLayout());
        setBackground(new Color(245, 247, 250)); // Light gray-blue background

        // Main content panel with shadow effect
        JPanel mainContentPanel = new JPanel(new BorderLayout(40, 0)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(getBackground());
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2d.dispose();
            }
        };
        mainContentPanel.setBackground(Color.WHITE);
        mainContentPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        mainContentPanel.setPreferredSize(new Dimension(800, 500));

        // Left panel for form
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title
        JLabel titleLabel = new JLabel("Welcome Back");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(new Color(44, 62, 80));
        titleLabel.setForeground(new Color(50, 0, 30));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        formPanel.add(titleLabel, gbc);

        // Subtitle
        JLabel subtitleLabel = new JLabel("Sign in to your account");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(127, 140, 141));
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 15, 30, 15);
        formPanel.add(subtitleLabel, gbc);

        // Username field
        JLabel usernameLabel = new JLabel("Username");
        usernameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        usernameLabel.setForeground(new Color(44, 62, 80));
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(0, 15, 5, 15);
        formPanel.add(usernameLabel, gbc);

        usernameField = new JTextField(20);
        styleTextField(usernameField);
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 15, 15, 15);
        formPanel.add(usernameField, gbc);

        // Password field
        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        passwordLabel.setForeground(new Color(44, 62, 80));
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(0, 15, 5, 15);
        formPanel.add(passwordLabel, gbc);

        passwordField = new JPasswordField(20);
        styleTextField(passwordField);
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 15, 20, 15);
        formPanel.add(passwordField, gbc);

        // Login button
        loginButton = new JButton("Sign In") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                super.paintComponent(g);
            }
        };
        styleLoginButton(loginButton);
        gbc.gridy = 6;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(loginButton, gbc);

        // Right panel for image
        JPanel imagePanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(new Color(230, 240, 250));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2d.dispose();
            }
        };
        imagePanel.setOpaque(false);
        imagePanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        JLabel imageLabel = new JLabel();
        try {
            ImageIcon imageIcon = new ImageIcon("logo.png");
            Image image = imageIcon.getImage().getScaledInstance(300, 300, Image.SCALE_SMOOTH);
            imageLabel.setIcon(new ImageIcon(image));
        } catch (Exception e) {
            imageLabel.setText("Inventory System");
            imageLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
            imageLabel.setForeground(new Color(44, 62, 80));
        }
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        imageLabel.setVerticalAlignment(JLabel.CENTER);
        imagePanel.add(imageLabel, BorderLayout.CENTER);

        // Add panels to main layout
        mainContentPanel.add(formPanel, BorderLayout.WEST);
        mainContentPanel.add(imagePanel, BorderLayout.CENTER);
        
        // Center the main content panel
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(new Color(245, 247, 250));
        centerPanel.add(mainContentPanel);
        add(centerPanel, BorderLayout.CENTER);

        // Login action
        loginButton.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();
            
            if (username.isEmpty() || password.isEmpty()) {
                showError("Username and password cannot be empty.");
                return;
            }
            
            if (dbHandler.validateLogin(username, password)) {
                cardLayout.show(mainPanel, "Dashboard");
            } else {
                showError("Invalid credentials!");
            }
        });
    }

    private void styleTextField(JTextField field) {
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        // field.setBorder(BorderFactory.createCompoundBorder(
            // BorderFactory.createLineBorder(new Color(220, 220, 220),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ;
        field.setBackground(Color.WHITE);
        field.setForeground(new Color(44, 62, 80));
    }

    private void styleLoginButton(JButton button) {
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(new Color(52, 152, 219));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(12, 40, 12, 40));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(41, 128, 185));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(52, 152, 219));
            }
        });
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", 
            JOptionPane.ERROR_MESSAGE, UIManager.getIcon("OptionPane.errorIcon"));
    }
}