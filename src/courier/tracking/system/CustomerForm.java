package courier.tracking.system;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;
import static courier.tracking.system.AdminLogin.DatabaseHandler;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;


public class CustomerForm extends JFrame {

    JLabel nameLabel, emailLabel, phoneLabel, passwordLabel;
    JTextField nameField, emailField, phoneField, passwordField;
    JButton saveButton, cancelButton;
    int customerId;
    DisplayAllCustomers parent;

    public CustomerForm(DisplayAllCustomers parent, String title, int customerId) {
        super(title);
        this.parent = parent;
        this.customerId = customerId;
        
        nameLabel = new JLabel("First Name:");
        emailLabel = new JLabel("Email:");
        phoneLabel = new JLabel("Phone Number:");
        passwordLabel = new JLabel("Password:");

        nameField = new JTextField(20);
        emailField = new JTextField(20);
        phoneField = new JTextField(20);
        passwordField = new JPasswordField(20);

        saveButton = new JButton("Save");
        cancelButton = new JButton("Cancel");

        JPanel panel = new JPanel(new GridLayout(5, 2, 5, 5));
        panel.add(nameLabel);
        panel.add(nameField);
        panel.add(emailLabel);
        panel.add(emailField);
        panel.add(phoneLabel);
        panel.add(phoneField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(saveButton);
        panel.add(cancelButton);
        add(panel);
        
        if (customerId != -1) {
            populateFields();
        }

        saveButton.addActionListener(e -> saveCustomer());
        cancelButton.addActionListener(e -> dispose());

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(300, 250);
        setLocationRelativeTo(parent);
        setVisible(true);
    }

    private void populateFields() {
        try (Connection connection = DatabaseHandler.getConnection()) {
            String query = "SELECT * FROM user WHERE UserID = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, customerId);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        nameField.setText(resultSet.getString("Fname"));
                        emailField.setText(resultSet.getString("Email"));
                        phoneField.setText(resultSet.getString("PhoneNumber"));
                        passwordField.setText(resultSet.getString("Password"));
                    }
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error fetching customer details: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void saveCustomer() {
        String name = nameField.getText();
        String email = emailField.getText();
        String phone = phoneField.getText();
        String password = passwordField.getText();

        if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection connection = DatabaseHandler.getConnection()) {
            String query;
            if (customerId == -1) {
                query = "INSERT INTO user (Fname, Email, PhoneNumber, Password) VALUES (?, ?, ?, ?)";
            } else {
                query = "UPDATE user SET Fname = ?, Email = ?, PhoneNumber = ?, Password = ? WHERE UserID = ?";
            }
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, name);
                statement.setString(2, email);
                statement.setString(3, phone);
                statement.setString(4, password);
                if (customerId != -1) {
                    statement.setInt(5, customerId);
                }
                statement.executeUpdate();
            }
            
            JOptionPane.showMessageDialog(this, "User saved successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
            parent.displayCustomers(); // Refresh the parent table
            dispose();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error saving user: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        new CustomerForm(null, "Add User", -1);
    }
}
