package main.java.ui;

import main.java.db.DatabaseHandler;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.sql.SQLException;
import java.util.Vector;

public class DashboardPanel extends JPanel {
    private JTable productTable;
    private JTable supplierTable;
    private DatabaseHandler dbHandler;
    private Vector<Vector<Object>> productData;
    private Vector<String> productColumns;
    private Vector<Vector<Object>> supplierData;
    private Vector<String> supplierColumns;

    public DashboardPanel(CardLayout cardLayout, JPanel mainPanel, DatabaseHandler dbHandler) {
        this.dbHandler = dbHandler;
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(250, 250, 252));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Modern color scheme
        Color primaryColor = new Color(48, 104, 222);
        Color secondaryColor = new Color(250, 250, 252);
        Color accentColor = new Color(255, 107, 107);
        Color textColor = new Color(60, 60, 60);
        Color pureWhite = new Color(255 , 255 ,255);  

        productColumns = new Vector<>();
        productColumns.add("ID");
        productColumns.add("Name");
        productColumns.add("Quantity");
        productColumns.add("Price");
        productColumns.add("Category");
        productData = dbHandler.getProducts();

        supplierColumns = new Vector<>();
        supplierColumns.add("ID");
        supplierColumns.add("Name");
        supplierColumns.add("Contact");
        supplierColumns.add("Product");
        supplierData = dbHandler.getSuppliers();

        // Modern tabbed pane
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tabbedPane.setBackground(secondaryColor);
        tabbedPane.setForeground(textColor);
        tabbedPane.setBorder(BorderFactory.createEmptyBorder());

        // Product panel with modern styling
        JPanel productPanel = new JPanel(new BorderLayout());
        productPanel.setBackground(secondaryColor);
        productPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        
        productTable = new JTable(productData, productColumns);
        styleTable(productTable);
        JScrollPane productScrollPane = new JScrollPane(productTable);
        productScrollPane.setBorder(BorderFactory.createEmptyBorder());
        productScrollPane.getViewport().setBackground(Color.WHITE);
        productPanel.add(productScrollPane, BorderLayout.CENTER);
        productPanel.add(createProductControls(primaryColor, accentColor, textColor), BorderLayout.SOUTH);
        tabbedPane.addTab("Products", new ImageIcon(), productPanel);

        // Supplier panel with modern styling
        JPanel supplierPanel = new JPanel(new BorderLayout());
        supplierPanel.setBackground(secondaryColor);
        supplierPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        
        supplierTable = new JTable(supplierData, supplierColumns);
        styleTable(supplierTable);
        JScrollPane supplierScrollPane = new JScrollPane(supplierTable);
        supplierScrollPane.setBorder(BorderFactory.createEmptyBorder());
        supplierScrollPane.getViewport().setBackground(Color.WHITE);
        supplierPanel.add(supplierScrollPane, BorderLayout.CENTER);
        supplierPanel.add(createSupplierControls(primaryColor, accentColor, textColor), BorderLayout.SOUTH);
        tabbedPane.addTab("Suppliers", new ImageIcon(), supplierPanel);

        add(tabbedPane, BorderLayout.CENTER);

        // Modern header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(secondaryColor);
        headerPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(230, 230, 230)));
        
        JLabel header = new JLabel("Dashboard", SwingConstants.LEFT);
        header.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 22));
        header.setForeground(primaryColor);
        header.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        
        JLabel subtitle = new JLabel("Manage your inventory and suppliers", SwingConstants.LEFT);
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitle.setForeground(new Color(120, 120, 120));
        
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setBackground(secondaryColor);
        titlePanel.add(header);
        titlePanel.add(subtitle);
        
        headerPanel.add(titlePanel, BorderLayout.WEST);
        add(headerPanel, BorderLayout.NORTH);
    }

    private void styleTable(JTable table) {
        table.setFillsViewportHeight(true);
        table.setGridColor(new Color(240, 240, 240));
        table.setSelectionBackground(new Color(220, 235, 255));
        table.setSelectionForeground(Color.BLACK);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setRowHeight(30);
        table.setShowGrid(true);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setBorder(BorderFactory.createEmptyBorder());
        
        // Header styling
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 13));
        header.setBackground(new Color(245, 245, 245));
        header.setForeground(new Color(80, 80, 80));
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(230, 230, 230)));
        header.setPreferredSize(new Dimension(header.getWidth(), 35));
    }

    private JPanel createProductControls(Color primaryColor, Color accentColor, Color textColor) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        panel.setBackground(new Color(245, 245, 247));
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(220, 220, 220)),
            BorderFactory.createEmptyBorder(5, 0, 5, 0)
        ));
        
        JButton addButton = createStyledButton("Add Product", primaryColor,Color.white);
        JButton editButton = createStyledButton("Edit Product", primaryColor, Color.white);
        JButton deleteButton = createStyledButton("Delete Product", accentColor, Color.WHITE);
        JButton stockButton = createStyledButton("Manage Stock", primaryColor, Color.white);
        JButton reportButton = createStyledButton("Low Stock Report", primaryColor, Color.white);

        panel.add(addButton);
        panel.add(editButton);
        panel.add(deleteButton);
        panel.add(stockButton);
        panel.add(reportButton);

        addButton.addActionListener(e -> new ProductManagement(this, dbHandler, null).setVisible(true));
        editButton.addActionListener(e -> {
            int selectedRow = productTable.getSelectedRow();
            if (selectedRow != -1) {
                new ProductManagement(this, dbHandler, productData.get(selectedRow)).setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Select a product to edit.");
            }
        });
        deleteButton.addActionListener(e -> {
            int selectedRow = productTable.getSelectedRow();
            if (selectedRow != -1) {
                try {
                    dbHandler.deleteProduct((Integer) productData.get(selectedRow).get(0));
                    refreshProductTable();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
                }
            } else {
                JOptionPane.showMessageDialog(this, "Select a product to delete.");
            }
        });
        stockButton.addActionListener(e -> {
            int selectedRow = productTable.getSelectedRow();
            if (selectedRow != -1) {
                new StockManagement(this, dbHandler, (Integer) productData.get(selectedRow).get(0)).setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Select a product to manage stock.");
            }
        });
        reportButton.addActionListener(e -> generateLowStockReport());

        return panel;
    }

    private JPanel createSupplierControls(Color primaryColor, Color accentColor, Color textColor) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        panel.setBackground(new Color(245, 245, 247));
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(220, 220, 220)),
            BorderFactory.createEmptyBorder(5, 0, 5, 0)
        ));
        
        JButton addButton = createStyledButton("Add Supplier", primaryColor, Color.white);
        JButton editButton = createStyledButton("Edit Supplier", primaryColor, Color.WHITE);
        JButton deleteButton = createStyledButton("Delete Supplier", accentColor, Color.WHITE);

        panel.add(addButton);
        panel.add(editButton);
        panel.add(deleteButton);

        addButton.addActionListener(e -> new SupplierManagement(this, dbHandler, null).setVisible(true));
        editButton.addActionListener(e -> {
            int selectedRow = supplierTable.getSelectedRow();
            if (selectedRow != -1) {
                new SupplierManagement(this, dbHandler, supplierData.get(selectedRow)).setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Select a supplier to edit.");
            }
        });
        deleteButton.addActionListener(e -> {
            int selectedRow = supplierTable.getSelectedRow();
            if (selectedRow != -1) {
                try {
                    dbHandler.deleteSupplier((Integer) supplierData.get(selectedRow).get(0));
                    refreshSupplierTable();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
                }
            } else {
                JOptionPane.showMessageDialog(this, "Select a supplier to delete.");
            }
        });

        return panel;
    }

    private JButton createStyledButton(String text, Color bgColor, Color fgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 14));
        button.setBackground(bgColor);
        button.setForeground(fgColor);
        button.setFocusPainted(false);
        
        // More pronounced border and shadow effect
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(bgColor.getRed(), bgColor.getGreen(), bgColor.getBlue(), 70), 1),
                BorderFactory.createLineBorder(new Color(bgColor.getRed(), bgColor.getGreen(), bgColor.getBlue(), 150), 1)
            ),
            BorderFactory.createEmptyBorder(10, 25, 10, 25)
        ));
        
        // Enhanced hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            private final Color originalBg = bgColor;
            private final Color hoverBg = new Color(
                Math.max(bgColor.getRed() - 25, 0),
                Math.max(bgColor.getGreen() - 25, 0),
                Math.max(bgColor.getBlue() - 25, 0)
            );
            
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(hoverBg);
                button.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(hoverBg.getRed(), hoverBg.getGreen(), hoverBg.getBlue(), 70), 1),
                        BorderFactory.createLineBorder(new Color(hoverBg.getRed(), hoverBg.getGreen(), hoverBg.getBlue(), 150), 1)
                    ),
                    BorderFactory.createEmptyBorder(10, 25, 10, 25)
                ));
            }
            
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(originalBg);
                button.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(originalBg.getRed(), originalBg.getGreen(), originalBg.getBlue(), 70), 1),
                        BorderFactory.createLineBorder(new Color(originalBg.getRed(), originalBg.getGreen(), originalBg.getBlue(), 150), 1)
                    ),
                    BorderFactory.createEmptyBorder(10, 25, 10, 25)
                ));
            }
        });
        
        return button;
    }

    public void refreshProductTable() {
        productData = dbHandler.getProducts();
        productTable.setModel(new DefaultTableModel(productData, productColumns));
        styleTable(productTable);
    }

    public void refreshSupplierTable() {
        supplierData = dbHandler.getSuppliers();
        supplierTable.setModel(new DefaultTableModel(supplierData, supplierColumns));
        styleTable(supplierTable);
    }

    private void generateLowStockReport() {
        String thresholdInput = JOptionPane.showInputDialog(this, "Enter low stock threshold:", "10");
        try {
            int threshold = Integer.parseInt(thresholdInput);
            if (threshold < 0) {
                JOptionPane.showMessageDialog(this, "Threshold cannot be negative.");
                return;
            }
            Vector<Vector<Object>> lowStockData = dbHandler.getLowStockProducts(threshold);
            if (lowStockData.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No products below threshold.");
                return;
            }
            StringBuilder csv = new StringBuilder("ID,Name,Quantity,Price,Category\n");
            for (Vector<Object> row : lowStockData) {
                csv.append(String.format("%d,%s,%d,%.2f,%s\n", row.get(0), row.get(1), row.get(2), row.get(3), row.get(4)));
            }
            java.nio.file.Files.writeString(java.nio.file.Paths.get("low_stock_report.csv"), csv.toString());
            JOptionPane.showMessageDialog(this, "Report generated as low_stock_report.csv");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number for threshold.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error generating report: " + e.getMessage());
        }
    }
}