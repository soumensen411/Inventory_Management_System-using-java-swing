package main.java.ui;

import main.java.db.DatabaseHandler;
import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class StockManagement extends JDialog {
    private JTextField amountField;
    private JRadioButton addRadio, removeRadio;
    private DatabaseHandler dbHandler;
    private DashboardPanel parent;
    private int productId;

    public StockManagement(DashboardPanel parent, DatabaseHandler dbHandler, int productId) {
        super((Frame) SwingUtilities.getWindowAncestor(parent), "Manage Stock", true);
        this.parent = parent;
        this.dbHandler = dbHandler;
        this.productId = productId;
        setLayout(new GridBagLayout());
        setBackground(new Color(240, 248, 255));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        amountField = new JTextField(10);
        addRadio = new JRadioButton("Add Stock", true);
        removeRadio = new JRadioButton("Remove Stock");
        ButtonGroup group = new ButtonGroup();
        group.add(addRadio);
        group.add(removeRadio);
        JButton saveButton = new JButton("Save");
        saveButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        saveButton.setBackground(new Color(100, 149, 237));
        saveButton.setForeground(Color.WHITE);

        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel("Amount:", SwingConstants.RIGHT), gbc);
        gbc.gridx = 1;
        add(amountField, gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(addRadio, gbc);
        gbc.gridx = 1;
        add(removeRadio, gbc);
        gbc.gridx = 1;
        gbc.gridy = 2;
        add(saveButton, gbc);

        saveButton.addActionListener(e -> saveStock());

        pack();
        setLocationRelativeTo(parent);
    }

    private void saveStock() {
        try {
            String amountText = amountField.getText().trim();
            if (amountText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Amount cannot be empty.");
                return;
            }
            int amount;
            try {
                amount = Integer.parseInt(amountText);
                if (amount <= 0) {
                    JOptionPane.showMessageDialog(this, "Amount must be a positive number.");
                    return;
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Amount must be a valid number.");
                return;
            }
            dbHandler.manageStock(productId, amount, addRadio.isSelected());
            parent.refreshProductTable();
            dispose();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }
}