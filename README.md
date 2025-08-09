# 📦 Inventory Management System

A Java-based desktop application for managing inventory, featuring a modern GUI built with Java Swing and a MySQL backend.  
The system allows users to authenticate, manage products and suppliers, track stock levels, and generate low-stock reports.

---

## 📋 Table of Contents

- [✨ Features](#-features)
- [🛠 Technologies](#-technologies)
- [📂 Project Structure](#-project-structure)
- [🔧 Prerequisites](#-prerequisites)
- [📸 Screenshots](#-screenshots)
- [🔮 Future Improvements](#-future-improvements)
---

## ✨ Features

- **Secure Login** – Authenticate users with username & password.  
- **Product Management** – Add, edit, delete, and view products (ID, name, quantity, price, category).  
- **Supplier Management** – Manage supplier details (name, contact, associated product).  
- **Stock Tracking** – Add or remove stock with history logging.  
- **Low-Stock Reports** – Generate **CSV** reports for products below a specified threshold.  
- **Modern UI** – Styled with **FlatLaf** for a clean, professional look, including custom buttons and rounded panels.

---

## 🛠 Technologies

| Technology | Version / Notes |
|------------|-----------------|
| Java       | JDK 17 or higher |
| Java Swing | GUI framework for desktop interface |
| MySQL      | 8.0+ |
| FlatLaf    | 3.2.5 – modern Look & Feel |
| JDBC       | MySQL Connector |

---

## 📂 Project Structure
```text
inventory-management-system/
└── screenshots/
│    ├── login.png
│    ├── dashboard.png
│    └── product-dialog.png
├── src/
│   └── main/
│       └── java/
│           ├── Main.java
│           ├── db/
│           │   └── DatabaseHandler.java
│           └── ui/
│               ├── LoginPanel.java
│               ├── DashboardPanel.java
│               ├── ProductManagement.java
│               ├── StockManagement.java
│               └── SupplierManagement.java
├── lib/
│   ├── mysql-connector-java-8.0.33.jar
│   └── flatlaf-3.2.5.jar
├── config.properties
├── LICENSE
└── README.md
```
---
##  🚀 config.properties 
1. db.url=jdbc:mysql://localhost:3306/inventory_db?useSSL=false
2. db.user=your_username
3. db.password=your_password
---

## 🔧 Prerequisites

1. **Java Development Kit (JDK)** – 17 or higher  
2. **MySQL** – 8.0+ with a database created (e.g., `inventory_db`)  
3. **MySQL JDBC Driver** – `mysql-connector-java`  
4. **FlatLaf** – 3.2.5  
5. **IDE (optional)** – IntelliJ IDEA, Eclipse, VS Code, etc.

---
## 📸 Screenshots

| Login | Products Dialog | Suppliers Dialog |
|-------|-----------|----------------|
| ![Login](inventory-management-system/screenshots/login.png) | ![Dashboard](inventory-management-system/screenshots/products.png) | ![Product Dialog](inventory-management-system/screenshots/suppliers.png) |

---
## 🔮 Future Improvements

Here’s a roadmap of enhancements we’d love to tackle (PRs welcome!):

| Area | Planned Features |
|------|------------------|
| **User Management** | • User registration & e-mail verification  <br>• Role-based access control (Admin, Viewer, Manager)  <br>• Self-service profile updates |
| **Security** | • BCrypt password hashing  <br>• Configurable session timeouts  <br>• Optional two-factor authentication (2FA) |
| **Reporting** | • Interactive charts for stock trends & sales analytics (JFreeChart)  <br>• Export reports to PDF & Excel  <br>• Scheduled e-mail reports |
| **Stock Features** | • Batch stock updates via CSV/Excel upload  <br>• Real-time low-stock alerts (desktop notifications)  <br>• Basic demand forecasting algorithm |
| **UI / UX** | • Toggle-able dark / light themes  <br>• Responsive layout scaling for 4K & Hi-DPI displays  <br>• WCAG accessibility improvements |
| **Data Handling** | • Full import / export wizard (CSV, Excel, JSON)  <br>• Cloud sync with Google Drive / OneDrive  <br>• Automated daily backups |
| **Integrations** | • RESTful API for e-commerce platforms (Shopify, WooCommerce)  <br>• Accounting software connectors (QuickBooks, Xero)  <br>• xAI API for smart reorder suggestions |
| **Performance** | • Indexed queries & prepared statements  <br>• Pagination on large product / history tables  <br>• Background caching layer (optional Redis) |
---

