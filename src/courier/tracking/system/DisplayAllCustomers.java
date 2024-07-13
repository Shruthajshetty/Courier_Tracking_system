package courier.tracking.system;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import static courier.tracking.system.AdminLogin.DatabaseHandler;
import javax.swing.table.JTableHeader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;


public class DisplayAllCustomers extends JFrame {

    JTable customerTable;
    JButton addButton, updateButton, deleteButton;

    public DisplayAllCustomers() {
        setLayout(new BorderLayout());

        String[] columns = {"User ID", "First Name", "Email", "Phone Number"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);

        customerTable = new JTable(model);
        customerTable.setFont(new Font("Arial", Font.PLAIN, 18)); // Set font size for table
        customerTable.setRowHeight(30); // Set row height
        JScrollPane scrollPane = new JScrollPane(customerTable);
        add(scrollPane, BorderLayout.CENTER);

        addButton = new JButton("Add");
        updateButton = new JButton("Update");
        deleteButton = new JButton("Delete");

        addButton.setFont(new Font("Arial", Font.PLAIN, 18)); // Set font size for buttons
        updateButton.setFont(new Font("Arial", Font.PLAIN, 18));
        deleteButton.setFont(new Font("Arial", Font.PLAIN, 18));
JTableHeader header = customerTable.getTableHeader();
        header.setFont(new Font("Arial", Font.BOLD, 30));
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        add(buttonPanel, BorderLayout.SOUTH);

        setTitle("All Customers");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
 customerTable.setDefaultEditor(Object.class, null);
        displayCustomers();
        
        addButton.addActionListener(e -> showCustomerForm("Add User", -1));

        updateButton.addActionListener(e -> {
            int selectedRow = customerTable.getSelectedRow();
            if (selectedRow != -1) {
                int userId = (int) customerTable.getValueAt(selectedRow, 0);
                showCustomerForm("Update User", userId);
            } else {
                JOptionPane.showMessageDialog(this, "Please select a user to update.", "Warning", JOptionPane.WARNING_MESSAGE);
            }
        });

        deleteButton.addActionListener(e -> deleteCustomer());
    }

    void displayCustomers() {
        try (Connection connection = DatabaseHandler.getConnection()) {
            String query = "SELECT * FROM user";
            try (PreparedStatement statement = connection.prepareStatement(query);
                 ResultSet resultSet = statement.executeQuery()) {

                DefaultTableModel model = (DefaultTableModel) customerTable.getModel();

                // Clear existing rows from the table model
                model.setRowCount(0);

                while (resultSet.next()) {
                    int userId = resultSet.getInt("UserID");
                    String fname = resultSet.getString("Fname");
                    String email = resultSet.getString("Email");
                    String phoneNumber = resultSet.getString("PhoneNumber");

                    Object[] row = {userId, fname, email, phoneNumber};
                    model.addRow(row);
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error fetching users: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showCustomerForm(String title, int userId) {
        java.awt.EventQueue.invokeLater(() -> {
            new CustomerForm(this, title, userId).setVisible(true);
        });
    }

    private void deleteCustomer() {
        int selectedRow = customerTable.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a user to delete.", "Delete Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int userId = (int) customerTable.getValueAt(selectedRow, 0);

        int choice = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this user?", "Confirmation", JOptionPane.YES_NO_OPTION);

        if (choice == JOptionPane.YES_OPTION) {
            try (Connection connection = DatabaseHandler.getConnection()) {
                String deleteQuery ="DELETE FROM user WHERE UserID = ?";
                try (PreparedStatement statement = connection.prepareStatement(deleteQuery)) {
                    statement.setInt(1, userId);
                    statement.executeUpdate();
                }

                JOptionPane.showMessageDialog(this, "User deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                displayCustomers(); // Refresh the table after deletion
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error deleting user: " + e.getMessage(), "Delete Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static void main(String[] args) {
        new DisplayAllCustomers();
    }
}
