package main.java.ui;

import main.java.db.DatabaseHandler;
import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.Vector;

public class ProductManagement extends JDialog {
    private JTextField nameField, quantityField, priceField, categoryField;
    private DatabaseHandler dbHandler;
    private DashboardPanel parent;
    private Vector<Object> product;

    public ProductManagement(DashboardPanel parent, DatabaseHandler dbHandler, Vector<Object> product) {
        super((Frame) SwingUtilities.getWindowAncestor(parent), product == null ? "Add Product" : "Edit Product", true);
        this.parent = parent;
        this.dbHandler = dbHandler;
        this.product = product;
        setLayout(new GridBagLayout());
        setBackground(new Color(240, 248, 255));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        nameField = new JTextField(20);
        quantityField = new JTextField(20);
        priceField = new JTextField(20);
        categoryField = new JTextField(20);
        JButton saveButton = new JButton("Save");
        saveButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        saveButton.setBackground(new Color(100, 149, 237));
        saveButton.setForeground(Color.WHITE);

        if (product != null) {
            nameField.setText(product.get(1).toString());
            quantityField.setText(product.get(2).toString());
            priceField.setText(product.get(3).toString());
            categoryField.setText(product.get(4).toString());
        }

        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel("Name:", SwingConstants.RIGHT), gbc);
        gbc.gridx = 1;
        add(nameField, gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(new JLabel("Quantity:", SwingConstants.RIGHT), gbc);
        gbc.gridx = 1;
        add(quantityField, gbc);
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(new JLabel("Price:", SwingConstants.RIGHT), gbc);
        gbc.gridx = 1;
        add(priceField, gbc);
        gbc.gridx = 0;
        gbc.gridy = 3;
        add(new JLabel("Category:", SwingConstants.RIGHT), gbc);
        gbc.gridx = 1;
        add(categoryField, gbc);
        gbc.gridx = 1;
        gbc.gridy = 4;
        add(saveButton, gbc);

        saveButton.addActionListener(e -> saveProduct());

        pack();
        setLocationRelativeTo(parent);
    }

    private void saveProduct() {
        try {
            String name = nameField.getText().trim();
            String quantityText = quantityField.getText().trim();
            String priceText = priceField.getText().trim();
            String category = categoryField.getText().trim();

            if (name.isEmpty() || quantityText.isEmpty() || priceText.isEmpty() || category.isEmpty()) {
                JOptionPane.showMessageDialog(this, "All fields must be filled.");
                return;
            }

            int quantity;
            double price;
            try {
                quantity = Integer.parseInt(quantityText);
                price = Double.parseDouble(priceText);
                if (quantity < 0 || price < 0) {
                    JOptionPane.showMessageDialog(this, "Quantity and price cannot be negative.");
                    return;
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Quantity and price must be valid numbers.");
                return;
            }

            if (product == null) {
                dbHandler.addProduct(name, quantity, price, category);
            } else {
                dbHandler.updateProduct((Integer) product.get(0), name, quantity, price, category);
            }
            parent.refreshProductTable();
            dispose();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }
}