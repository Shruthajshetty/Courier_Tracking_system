package courier.tracking.system;

import courier.tracking.system.AdminLogin.DatabaseHandler;
import java.awt.Color;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.ButtonGroup;
import javax.swing.JOptionPane;

public class ReviewPage extends javax.swing.JFrame {

    private final String loggedInUsername;
    private int shipmentId;
    private int userID;
    private final ButtonGroup ratingButtonGroup; // ButtonGroup to group radio buttons

    public ReviewPage(int userID, String loggedInUsername, int shipmentId) {
        initComponents();
        this.loggedInUsername = loggedInUsername;
        this.shipmentId = shipmentId;
        this.userID = userID;
        
        // Set background color
        getContentPane().setBackground(new Color(255, 255, 255)); // White background
        
        // Group radio buttons
        ratingButtonGroup = new ButtonGroup();
        ratingButtonGroup.add(rating1RadioButton);
        ratingButtonGroup.add(rating2RadioButton);
        ratingButtonGroup.add(rating3RadioButton);
        ratingButtonGroup.add(rating4RadioButton);
        ratingButtonGroup.add(rating5RadioButton);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

       
        jLabel1 = new javax.swing.JLabel();
        rating1RadioButton = new javax.swing.JRadioButton();
        rating2RadioButton = new javax.swing.JRadioButton();
        rating3RadioButton = new javax.swing.JRadioButton();
        rating4RadioButton = new javax.swing.JRadioButton();
        rating5RadioButton = new javax.swing.JRadioButton();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        commentTextArea = new javax.swing.JTextArea();
        submitButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

      
        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel1.setText("Select Rating:");

        rating1RadioButton.setText("1");
        rating1RadioButton.setOpaque(false);

        rating2RadioButton.setText("2");
        rating2RadioButton.setOpaque(false);

        rating3RadioButton.setText("3");
        rating3RadioButton.setOpaque(false);

        rating4RadioButton.setText("4");
        rating4RadioButton.setOpaque(false);

        rating5RadioButton.setText("5");
        rating5RadioButton.setOpaque(false);

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel2.setText("Comments:");

        commentTextArea.setColumns(20);
        commentTextArea.setRows(5);
        jScrollPane2.setViewportView(commentTextArea);

        submitButton.setText("Submit");
        submitButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                submitButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(rating1RadioButton)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(rating2RadioButton)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(rating3RadioButton)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(rating4RadioButton)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(rating5RadioButton))
                            .addComponent(jLabel2)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 370, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(submitButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(rating1RadioButton)
                    .addComponent(rating2RadioButton)
                    .addComponent(rating3RadioButton)
                    .addComponent(rating4RadioButton)
                    .addComponent(rating5RadioButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(submitButton)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>                        

    private void submitButtonActionPerformed(java.awt.event.ActionEvent evt) {                                              
        int rating = 0;
        if (rating1RadioButton.isSelected()) {
            rating = 1;
        } else if (rating2RadioButton.isSelected()) {
            rating = 2;
        } else if (rating3RadioButton.isSelected()) {
            rating = 3;
        } else if (rating4RadioButton.isSelected()) {
            rating = 4;
        } else if (rating5RadioButton.isSelected()) {
            rating = 5;
        } else {
            JOptionPane.showMessageDialog(this, "Please select a rating.");
            return;
        }
        
        String comment = commentTextArea.getText().trim();
        if (comment.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please provide a comment.");
            return;
        }
        
        int driverID = getDriverId(shipmentId); // Replace with the actual driver ID
        
        try (Connection connection = DatabaseHandler.getConnection()) {
            String query = "INSERT INTO review (Rating, Comment, ReviewDate, UserID, ShipmentID, DriverID) VALUES (?, ?, NOW(), ?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, rating);
                statement.setString(2, comment);
                statement.setInt(3,userID);
                statement.setInt(4, shipmentId);
                statement.setInt(5, driverID);
                
                int rowsAffected = statement.executeUpdate();
                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(this, "Review submitted successfully.");
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to submit review.");
                    dispose();
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error occurred while submitting review: " + e.getMessage());
        }
    }                                             
    
    private int getDriverId(int shipmentId) {
        int driverId = -1; // Default value in case of an error

        try (Connection connection = DatabaseHandler.getConnection()) {
            String query = "SELECT d.DriverID FROM driver d, delivery dl, shipment s WHERE d.DriverID = dl.DriverID AND dl.ShipmentID = s.ShipmentID AND s.ShipmentID = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                // Set the shipment ID parameter
                statement.setInt(1, shipmentId);

                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    driverId = resultSet.getInt("DriverID");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle the error appropriately
        }

        return driverId;
    }

    // Variables declaration - do not modify                     
    private javax.swing.JTextArea commentTextArea;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JRadioButton rating1RadioButton;
    private javax.swing.JRadioButton rating2RadioButton;
    private javax.swing.JRadioButton rating3RadioButton;
    private javax.swing.JRadioButton rating4RadioButton;
    private javax.swing.JRadioButton rating5RadioButton;
    private javax.swing.JButton submitButton;
    // End of variables declaration                   
}
