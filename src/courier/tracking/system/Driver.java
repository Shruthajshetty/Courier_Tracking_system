/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package courier.tracking.system;
import courier.tracking.system.AdminLogin.DatabaseHandler;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;


public class Driver extends JFrame {

    JTable driverTable;
    JButton addButton, updateButton, deleteButton;

    public Driver() {
        setLayout(new BorderLayout());

        String[] columns = {"Driver ID", "First Name", "Last Name", "Contact No", "Company Name"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
         setPreferredSize(new Dimension(800, 600));
         setLocationRelativeTo(null);
        driverTable = new JTable(model);
        driverTable.setFont(new Font("Arial", Font.PLAIN, 20)); // Set font size for table
        driverTable.setRowHeight(40); // Set row height
        JScrollPane scrollPane = new JScrollPane(driverTable);
        add(scrollPane, BorderLayout.CENTER);
 scrollPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        addButton = new JButton("Add");
        updateButton = new JButton("Update");
        deleteButton = new JButton("Delete");
 JTableHeader header = driverTable.getTableHeader();
        header.setFont(new Font("Arial", Font.BOLD, 30)); 
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10)); // Center align and add spacing between buttons
        buttonPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        Font buttonFont = new Font("Arial", Font.PLAIN, 24);
        addButton.setFont(buttonFont);
        updateButton.setFont(buttonFont);
        deleteButton.setFont(buttonFont);// Add padding
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        add(buttonPanel, BorderLayout.SOUTH);

        setTitle("All Drivers");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
driverTable.setDefaultEditor(Object.class, null);
        displayDrivers();
        setExtendedState(Driver.MAXIMIZED_BOTH);

        addButton.addActionListener(e -> addDriver());
        updateButton.addActionListener(e -> updateDriver());
        deleteButton.addActionListener(e -> deleteDriver());
    }

    private void displayDrivers() {
        try (Connection connection = DatabaseHandler.getConnection()) {
            String query = "SELECT d.DriverID, d.Fname, d.Lname, d.ContactNo, c.CompanyName " +
                           "FROM driver d JOIN company c ON d.CompanyID = c.CompanyID";
            try (PreparedStatement statement = connection.prepareStatement(query);
                 ResultSet resultSet = statement.executeQuery()) {

                DefaultTableModel model = (DefaultTableModel) driverTable.getModel();
                model.setRowCount(0);
                while (resultSet.next()) {
                    int driverID = resultSet.getInt("DriverID");
                    String fName = resultSet.getString("Fname");
                    String lName = resultSet.getString("Lname");
                    String contactNo = resultSet.getString("ContactNo");
                    String companyName = resultSet.getString("CompanyName");

                    Object[] row = {driverID, fName, lName, contactNo, companyName};
                    model.addRow(row);
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error fetching drivers: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

private void addDriver() {
    JTextField fNameField = new JTextField();
    JTextField lNameField = new JTextField();
    JTextField contactNoField = new JTextField();

    // Create a dropdown for selecting company
    JComboBox<String> companyComboBox = new JComboBox<>();
    try (Connection connection = DatabaseHandler.getConnection()) {
        String query = "SELECT CompanyID, CompanyName FROM company";
        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                int companyId = resultSet.getInt("CompanyID");
                String companyName = resultSet.getString("CompanyName");
                companyComboBox.addItem(companyId + " - " + companyName);
            }
        }
    } catch (SQLException ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error fetching companies: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    JPanel panel = new JPanel(new GridLayout(0, 1));
    panel.add(new JLabel("First Name:"));
    panel.add(fNameField);
    panel.add(new JLabel("Last Name:"));
    panel.add(lNameField);
    panel.add(new JLabel("Contact No:"));
    panel.add(contactNoField);
    panel.add(new JLabel("Company:"));
    panel.add(companyComboBox);

    int result = JOptionPane.showConfirmDialog(null, panel, "Add Driver",
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
    if (result == JOptionPane.OK_OPTION) {
        String fName = fNameField.getText();
        String lName = lNameField.getText();
        String contactNo = contactNoField.getText();
        String selectedCompany = (String) companyComboBox.getSelectedItem();
        int companyId = Integer.parseInt(selectedCompany.split(" - ")[0]);

        if (fName.isEmpty() || lName.isEmpty() || contactNo.isEmpty()) {
            JOptionPane.showMessageDialog(null, "All fields are required!", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            try (Connection connection = DatabaseHandler.getConnection()) {
                String insertQuery = "INSERT INTO driver (Fname, Lname, ContactNo, CompanyID) VALUES (?, ?, ?, ?)";
                try (PreparedStatement statement = connection.prepareStatement(insertQuery)) {
                    statement.setString(1, fName);
                    statement.setString(2, lName);
                    statement.setString(3, contactNo);
                    statement.setInt(4, companyId);
                    statement.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Driver added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    displayDrivers(); // Refresh the table
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Error adding driver: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}

private void updateDriver() {
    int selectedRow = driverTable.getSelectedRow();
    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(this, "Please select a driver to update.", "Warning", JOptionPane.WARNING_MESSAGE);
        return;
    }

    int driverID = (int) driverTable.getValueAt(selectedRow, 0);
    String fName = (String) driverTable.getValueAt(selectedRow, 1);
    String lName = (String) driverTable.getValueAt(selectedRow, 2);
    String contactNo = (String) driverTable.getValueAt(selectedRow, 3);
    String companyName = (String) driverTable.getValueAt(selectedRow, 4);

    JTextField fNameField = new JTextField(fName);
    JTextField lNameField = new JTextField(lName);
    JTextField contactNoField = new JTextField(contactNo);

    JComboBox<String> companyComboBox = new JComboBox<>();
    try (Connection connection = DatabaseHandler.getConnection()) {
        String query = "SELECT CompanyID, CompanyName FROM company";
        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                int companyId = resultSet.getInt("CompanyID");
                String company = companyId + " - " + resultSet.getString("CompanyName");
                companyComboBox.addItem(company);
                if (companyName.equals(company)) {
                    companyComboBox.setSelectedItem(company);
                }
            }
        }
    } catch (SQLException ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error fetching companies: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    JPanel panel = new JPanel(new GridLayout(0, 1));
    panel.add(new JLabel("First Name:"));
    panel.add(fNameField);
    panel.add(new JLabel("Last Name:"));
    panel.add(lNameField);
    panel.add(new JLabel("Contact No:"));
    panel.add(contactNoField);
    panel.add(new JLabel("Company:"));
    panel.add(companyComboBox);

    int result = JOptionPane.showConfirmDialog(null, panel, "Update Driver",
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
    if (result == JOptionPane.OK_OPTION) {
        String updatedFName = fNameField.getText();
        String updatedLName = lNameField.getText();
        String updatedContactNo = contactNoField.getText();
        String selectedCompany = (String) companyComboBox.getSelectedItem();
        int updatedCompanyId = Integer.parseInt(selectedCompany.split(" - ")[0]);

        if (updatedFName.isEmpty() || updatedLName.isEmpty() || updatedContactNo.isEmpty()) {
            JOptionPane.showMessageDialog(null, "All fields are required!", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            try (Connection connection = DatabaseHandler.getConnection()) {
                String updateQuery = "UPDATE driver SET Fname=?, Lname=?, ContactNo=?, CompanyID=? WHERE DriverID=?";
                try (PreparedStatement statement = connection.prepareStatement(updateQuery)) {
                    statement.setString(1, updatedFName);
                    statement.setString(2, updatedLName);
                    statement.setString(3, updatedContactNo);
                    statement.setInt(4, updatedCompanyId);
                    statement.setInt(5, driverID);
                    statement.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Driver updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    displayDrivers(); // Refresh the table
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Error updating driver: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}


    private void deleteDriver() {
        int selectedRow = driverTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a driver to delete.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int driverID = (int) driverTable.getValueAt(selectedRow, 0);

        int choice = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this driver?", "Confirmation", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            try (Connection connection = DatabaseHandler.getConnection()) {
                String deleteQuery = "DELETE FROM driver WHERE DriverID=?";
                try (PreparedStatement statement = connection.prepareStatement(deleteQuery)) {
                    statement.setInt(1, driverID);
                    statement.executeUpdate();
                    JOptionPane.showMessageDialog(null, "Driver deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    displayDrivers(); // Refresh the table
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Error deleting driver: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static void main(String[] args) {
        new Driver();
    }
}