package courier.tracking.system;

import courier.tracking.system.AdminLogin.DatabaseHandler;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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


public class Allcompany extends JFrame {

    JTable companyTable;
    JButton addButton, updateButton, deleteButton;

    public Allcompany() {
        setTitle("All Companies");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setPreferredSize(new Dimension(800, 600));
        setLocationRelativeTo(null);

        String[] columns = {"Company ID", "Company Name", "Contact"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);

        companyTable = new JTable(model);
        companyTable.setFont(new Font("Arial", Font.PLAIN, 20)); 
        companyTable.setRowHeight(40); 
        companyTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        JScrollPane scrollPane = new JScrollPane(companyTable);
        scrollPane.setBorder(new EmptyBorder(10, 10, 10, 10)); 
        add(scrollPane, BorderLayout.CENTER);

        JTableHeader header = companyTable.getTableHeader();
        header.setFont(new Font("Arial", Font.BOLD, 30));

        addButton = new JButton("Add");
        updateButton = new JButton("Update");
        deleteButton = new JButton("Delete");
        Font buttonFont = new Font("Arial", Font.PLAIN, 24);
        addButton.setFont(buttonFont);
        updateButton.setFont(buttonFont);
        deleteButton.setFont(buttonFont);
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10)); 
        buttonPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        add(buttonPanel, BorderLayout.SOUTH);

        pack();
        setVisible(true);

        displayCompanies();
        setExtendedState(Allcompany.MAXIMIZED_BOTH);

        addButton.addActionListener(e -> showCompanyForm("Add Company", -1));

        updateButton.addActionListener(e -> {
            int selectedRow = companyTable.getSelectedRow();
            if (selectedRow != -1) {
                int companyId = (int) companyTable.getValueAt(selectedRow, 0);
                showCompanyForm("Update Company", companyId);
            } else {
                JOptionPane.showMessageDialog(this, "Please select a company to update.", "Warning", JOptionPane.WARNING_MESSAGE);
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteCompany();
            }
        });
    }

    private void showCompanyForm(String title, int companyId) {
        java.awt.EventQueue.invokeLater(() -> {
            new CompanyForm(this, title, companyId).setVisible(true);
        });
    }

void displayCompanies() {
    try (Connection connection = DatabaseHandler.getConnection()) {
        String query = "SELECT * FROM company";
        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            DefaultTableModel model = (DefaultTableModel) companyTable.getModel();

            // Clear existing rows from the table model
            model.setRowCount(0);

            while (resultSet.next()) {
                int companyId = resultSet.getInt("companyid");
                String companyName = resultSet.getString("companyname");
                String contact = resultSet.getString("contact");

                Object[] row = {companyId, companyName, contact};
                model.addRow(row);
            }
        }

        
        companyTable.clearSelection();
        companyTable.setFocusable(false);
        
     
        companyTable.setDefaultEditor(Object.class, null);
        
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Error fetching companies: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}


    private void deleteCompany() {
        int selectedRow = companyTable.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a company to delete.", "Delete Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int companyId = (int) companyTable.getValueAt(selectedRow, 0);

        int choice = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this company?", "Confirmation", JOptionPane.YES_NO_OPTION);

        if (choice == JOptionPane.YES_OPTION) {
            try (Connection connection = DatabaseHandler.getConnection()) {
                String deleteQuery = "DELETE FROM company WHERE companyid = ?";
                try (PreparedStatement statement = connection.prepareStatement(deleteQuery)) {
                    statement.setInt(1, companyId);
                    statement.executeUpdate();
                }

                JOptionPane.showMessageDialog(this, "Company deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                displayCompanies(); 
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error deleting company: " + e.getMessage(), "Delete Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static void main(String[] args) {
        new Allcompany();
    }
}
