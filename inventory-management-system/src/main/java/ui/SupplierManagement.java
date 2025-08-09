package main.java.ui;

import main.java.db.DatabaseHandler;
import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.Vector;

public class SupplierManagement extends JDialog {
    private JTextField nameField, contactField;
    private JComboBox<String> productCombo;
    private DatabaseHandler dbHandler;
    private DashboardPanel parent;
    private Vector<Object> supplier;

    public SupplierManagement(DashboardPanel parent, DatabaseHandler dbHandler, Vector<Object> supplier) {
        super((Frame) SwingUtilities.getWindowAncestor(parent), supplier == null ? "Add Supplier" : "Edit Supplier", true);
        this.parent = parent;
        this.dbHandler = dbHandler;
        this.supplier = supplier;
        setLayout(new GridBagLayout());
        setBackground(new Color(240, 248, 255));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        nameField = new JTextField(20);
        contactField = new JTextField(20);
        productCombo = new JComboBox<>(dbHandler.getProductNames());
        JButton saveButton = new JButton("Save");
        saveButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        saveButton.setBackground(new Color(100, 149, 237));
        saveButton.setForeground(Color.WHITE);

        if (supplier != null) {
            nameField.setText(supplier.get(1).toString());
            contactField.setText(supplier.get(2).toString());
            productCombo.setSelectedItem(supplier.get(3).toString());
        }

        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel("Name:", SwingConstants.RIGHT), gbc);
        gbc.gridx = 1;
        add(nameField, gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(new JLabel("Contact:", SwingConstants.RIGHT), gbc);
        gbc.gridx = 1;
        add(contactField, gbc);
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(new JLabel("Product:", SwingConstants.RIGHT), gbc);
        gbc.gridx = 1;
        add(productCombo, gbc);
        gbc.gridx = 1;
        gbc.gridy = 3;
        add(saveButton, gbc);

        saveButton.addActionListener(e -> saveSupplier());

        pack();
        setLocationRelativeTo(parent);
    }

    private void saveSupplier() {
        try {
            String name = nameField.getText().trim();
            String contact = contactField.getText().trim();
            String product = (String) productCombo.getSelectedItem();
            int productId = 0;
            if (product != null && product.contains(":")) {
                productId = Integer.parseInt(product.split(":")[0].trim());
            } else {
                JOptionPane.showMessageDialog(this, "Please select a valid product.");
                return;
            }

            if (name.isEmpty() || contact.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Name and contact cannot be empty.");
                return;
            }

            if (supplier == null) {
                dbHandler.addSupplier(name, contact, productId);
            } else {
                dbHandler.updateSupplier((Integer) supplier.get(0), name, contact, productId);
            }
            parent.refreshSupplierTable();
            dispose();
        } catch (SQLException | NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }
}