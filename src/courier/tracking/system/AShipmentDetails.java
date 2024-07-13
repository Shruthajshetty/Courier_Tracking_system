/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package courier.tracking.system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class AShipmentDetails extends JFrame {

    private JLabel shipmentIdLabel;
    private JTextField shipmentIdField;
    private JButton searchButton;
    private JTextArea resultArea;
    private JPanel resultPanel;

    public AShipmentDetails() {
        initComponents();
        setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

    private void initComponents() {
        setTitle("Shipment Details");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout());
        resultPanel = new JPanel(new GridLayout(0, 1));

        shipmentIdLabel = new JLabel("Enter Shipment ID:");
        shipmentIdField = new JTextField(20);
        searchButton = new JButton("Search");
        resultArea = new JTextArea(20, 50);
        resultArea.setEditable(false);
        Font labelFont = new Font("Arial", Font.BOLD, 30);
        shipmentIdLabel.setFont(labelFont);
        shipmentIdField.setFont(labelFont);
//        searchButton.setSize(140, 120);
        JPanel inputPanel = new JPanel();
         searchButton.setFont(labelFont);
        inputPanel.add(shipmentIdLabel);
        inputPanel.add(shipmentIdField);
        inputPanel.add(searchButton);
        
        mainPanel.add(inputPanel, BorderLayout.NORTH);
        mainPanel.add(new JScrollPane(resultArea), BorderLayout.CENTER);
        mainPanel.add(new JScrollPane(resultPanel), BorderLayout.CENTER); // Added resultPanel to mainPanel

        add(mainPanel);

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String shipmentId = shipmentIdField.getText().trim();
                if (!shipmentId.isEmpty()) {
                    try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/courier_db", "root", "SHRU@04")) {
                        String query = "SELECT s.*, c.CompanyName, del.Status ,del.driverID "
                                + "FROM shipment s "
                                + "JOIN delivery del ON s.ShipmentID = del.ShipmentID "
                                + "JOIN company c ON s.CompanyID = c.CompanyID "
                                + "WHERE s.ShipmentID = ?";

                        try (PreparedStatement statement = connection.prepareStatement(query)) {
                            statement.setString(1, shipmentId);
                            try (ResultSet resultSet = statement.executeQuery()) {
                                if (resultSet.next()) {
                                    String shipmentType = resultSet.getString("Type");
                                    double weight = resultSet.getDouble("Weight");
                                    int userId = resultSet.getInt("UserID");
                                    int originAddressId = resultSet.getInt("OriginAddressID");
                                    int destinationId = resultSet.getInt("DestinationID");
                                    String deliveryStatus = resultSet.getString("Status");
                                    Date shipmentDate = resultSet.getDate("ShippingDate");
                                   // Date expectedDate = resultSet.getDate("ExpectedDate");
                                    String employeeName = getEmployeeName(connection, resultSet.getInt("UserID"));
                                    String driverName = getDriverName(connection, resultSet.getInt("DriverID"));

                                    resultPanel.removeAll(); // Clear previous content

                                    JLabel shipmentTypeLabel = new JLabel("Shipment Type: " + shipmentType);
                                    JLabel weightLabel = new JLabel("Weight: " + String.format("%.2f", weight));
                                    JLabel employeeNameLabel = new JLabel("Customer Name: " + employeeName);
                                    JLabel driverNameLabel = new JLabel("Driver Name: " + driverName);
                                    JLabel companyNameLabel = new JLabel("Company Name: " + resultSet.getString("CompanyName"));
                                    JLabel deliveryStatusLabel = new JLabel("Delivery Status: " + deliveryStatus);
                                    JLabel shipmentDateLabel = new JLabel("Shipment Date: " + shipmentDate);
                                  //  JLabel expectedDateLabel = new JLabel("Expected Date: " + expectedDate);

                                    Font labelFont = new Font("Arial", Font.BOLD, 20);
                                    shipmentTypeLabel.setFont(labelFont);
                                    
                                    weightLabel.setFont(labelFont);
                                    employeeNameLabel.setFont(labelFont);
                                    driverNameLabel.setFont(labelFont);
                                    companyNameLabel.setFont(labelFont);
                                    deliveryStatusLabel.setFont(labelFont);
                                    shipmentDateLabel.setFont(labelFont);
                                  //  expectedDateLabel.setFont(labelFont);

                                    resultPanel.add(shipmentTypeLabel);
                                    resultPanel.add(weightLabel);
                                    resultPanel.add(employeeNameLabel);
                                    resultPanel.add(driverNameLabel);
                                    resultPanel.add(companyNameLabel);
                                    resultPanel.add(deliveryStatusLabel);
                                    resultPanel.add(shipmentDateLabel);
                                   // resultPanel.add(expectedDateLabel);

                                    resultPanel.revalidate();
                                    resultPanel.repaint();
                                } else {
                                    resultPanel.removeAll();
                                    JOptionPane.showMessageDialog(AShipmentDetails.this, "Shipment ID not found.", "Warning", JOptionPane.WARNING_MESSAGE);// Force repaint
                                    }
                            }
                        }
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(AShipmentDetails.this, "Error executing query: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(AShipmentDetails.this, "Please enter shipment ID", "Warning", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
    }

    private String getEmployeeName(Connection connection, int userId) throws SQLException {
        String query = "SELECT Fname, Lname FROM user WHERE UserID = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("Fname") + " " + resultSet.getString("Lname");
                }
            }
        }
        return "Unknown";
    }

    private String getDriverName(Connection connection, int driverId) throws SQLException {
        String query = "SELECT Fname, Lname FROM driver WHERE DriverID = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, driverId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("Fname") + " " + resultSet.getString("Lname");
                }
            }
        }
        return "Unknown";
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new AShipmentDetails().setVisible(true);
            }
        });
    }
}

