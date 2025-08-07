package main.java.db;

import java.sql.*;
import javax.swing.JOptionPane;
import java.util.Vector;
import java.io.FileInputStream;
import java.util.Properties;

public class DatabaseHandler {
    private Connection conn;

    public DatabaseHandler() {
        try {
            Properties props = new Properties();
            props.load(new FileInputStream("config.properties"));
            String url = props.getProperty("db.url");
            String user = props.getProperty("db.user");
            String password = props.getProperty("db.password");

            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(url, user, password);
            initializeDatabase();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Database Connection Error: " + e.getMessage());
        }
    }

    private void initializeDatabase() throws SQLException {
        Statement stmt = conn.createStatement();
        stmt.execute("CREATE TABLE IF NOT EXISTS users (username VARCHAR(50), password VARCHAR(50))");
        stmt.execute("CREATE TABLE IF NOT EXISTS products (id INT AUTO_INCREMENT PRIMARY KEY, name VARCHAR(100), quantity INT, price DOUBLE, category VARCHAR(50))");
        stmt.execute("CREATE TABLE IF NOT EXISTS suppliers (id INT AUTO_INCREMENT PRIMARY KEY, name VARCHAR(100), contact VARCHAR(100), product_id INT)");
        stmt.execute("CREATE TABLE IF NOT EXISTS stock_history (id INT AUTO_INCREMENT PRIMARY KEY, product_id INT, change_amount INT, timestamp VARCHAR(50))");

        ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM users WHERE username = 'admin'");
        if (rs.next() && rs.getInt(1) == 0) {
            stmt.execute("INSERT INTO users (username, password) VALUES ('admin', 'admin123')");
        }
    }

    public void close() {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error closing database connection: " + e.getMessage());
        }
    }

    public boolean validateLogin(String username, String password) {
        try {
            PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM users WHERE username = ? AND password = ?");
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
            return false;
        }
    }

    public Vector<Vector<Object>> getProducts() {
        Vector<Vector<Object>> data = new Vector<>();
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM products");
            if (!rs.isBeforeFirst()) {
                JOptionPane.showMessageDialog(null, "No products found in the database.");
            }
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getInt("id"));
                row.add(rs.getString("name"));
                row.add(rs.getInt("quantity"));
                row.add(rs.getDouble("price"));
                row.add(rs.getString("category"));
                data.add(row);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
        return data;
    }

    public Vector<Vector<Object>> getSuppliers() {
        Vector<Vector<Object>> data = new Vector<>();
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT s.id, s.name, s.contact, p.name AS product_name FROM suppliers s LEFT JOIN products p ON s.product_id = p.id");
            if (!rs.isBeforeFirst()) {
                JOptionPane.showMessageDialog(null, "No suppliers found in the database.");
            }
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getInt("id"));
                row.add(rs.getString("name"));
                row.add(rs.getString("contact"));
                row.add(rs.getString("product_name"));
                data.add(row);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
        return data;
    }

    public void addProduct(String name, int quantity, double price, String category) throws SQLException {
        PreparedStatement pstmt = conn.prepareStatement("INSERT INTO products (name, quantity, price, category) VALUES (?, ?, ?, ?)");
        pstmt.setString(1, name);
        pstmt.setInt(2, quantity);
        pstmt.setDouble(3, price);
        pstmt.setString(4, category);
        pstmt.executeUpdate();
    }

    public void updateProduct(int id, String name, int quantity, double price, String category) throws SQLException {
        PreparedStatement pstmt = conn.prepareStatement("UPDATE products SET name = ?, quantity = ?, price = ?, category = ? WHERE id = ?");
        pstmt.setString(1, name);
        pstmt.setInt(2, quantity);
        pstmt.setDouble(3, price);
        pstmt.setString(4, category);
        pstmt.setInt(5, id);
        pstmt.executeUpdate();
    }

    public void deleteProduct(int id) throws SQLException {
        PreparedStatement pstmt = conn.prepareStatement("DELETE FROM products WHERE id = ?");
        pstmt.setInt(1, id);
        pstmt.executeUpdate();
    }

    public void addSupplier(String name, String contact, int productId) throws SQLException {
        PreparedStatement pstmt = conn.prepareStatement("INSERT INTO suppliers (name, contact, product_id) VALUES (?, ?, ?)");
        pstmt.setString(1, name);
        pstmt.setString(2, contact);
        pstmt.setInt(3, productId);
        pstmt.executeUpdate();
    }

    public void updateSupplier(int id, String name, String contact, int productId) throws SQLException {
        PreparedStatement pstmt = conn.prepareStatement("UPDATE suppliers SET name = ?, contact = ?, product_id = ? WHERE id = ?");
        pstmt.setString(1, name);
        pstmt.setString(2, contact);
        pstmt.setInt(3, productId);
        pstmt.setInt(4, id);
        pstmt.executeUpdate();
    }

    public void deleteSupplier(int id) throws SQLException {
        PreparedStatement pstmt = conn.prepareStatement("DELETE FROM suppliers WHERE id = ?");
        pstmt.setInt(1, id);
        pstmt.executeUpdate();
    }

    public void manageStock(int productId, int amount, boolean isAdd) throws SQLException {
        int currentQuantity = 0;
        PreparedStatement pstmt = conn.prepareStatement("SELECT quantity FROM products WHERE id = ?");
        pstmt.setInt(1, productId);
        ResultSet rs = pstmt.executeQuery();
        if (rs.next()) {
            currentQuantity = rs.getInt("quantity");
        }

        int newQuantity = isAdd ? currentQuantity + amount : currentQuantity - amount;
        if (newQuantity < 0) {
            throw new SQLException("Cannot reduce stock below 0!");
        }

        pstmt = conn.prepareStatement("UPDATE products SET quantity = ? WHERE id = ?");
        pstmt.setInt(1, newQuantity);
        pstmt.setInt(2, productId);
        pstmt.executeUpdate();

        pstmt = conn.prepareStatement("INSERT INTO stock_history (product_id, change_amount, timestamp) VALUES (?, ?, ?)");
        pstmt.setInt(1, productId);
        pstmt.setInt(2, isAdd ? amount : -amount);
        pstmt.setString(3, new java.util.Date().toString());
        pstmt.executeUpdate();
    }

    public Vector<Vector<Object>> getLowStockProducts(int threshold) {
        Vector<Vector<Object>> data = new Vector<>();
        try {
            PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM products WHERE quantity < ?");
            pstmt.setInt(1, threshold);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getInt("id"));
                row.add(rs.getString("name"));
                row.add(rs.getInt("quantity"));
                row.add(rs.getDouble("price"));
                row.add(rs.getString("category"));
                data.add(row);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
        return data;
    }

    public Vector<String> getProductNames() {
        Vector<String> names = new Vector<>();
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT id, name FROM products");
            while (rs.next()) {
                names.add(rs.getInt("id") + ": " + rs.getString("name"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
        return names;
    }
}