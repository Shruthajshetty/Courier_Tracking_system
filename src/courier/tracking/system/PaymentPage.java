package courier.tracking.system;

import java.sql.*;
import javax.swing.JOptionPane;

public class PaymentPage extends javax.swing.JFrame {
    private int userID;
    private int shipmentID;
    private float weight;
    private String type;
    private String company;
    private int driverID;
    float totalAmount=0;
    private String loggedInemail;
    /**
     * Creates new form PaymentPage
     */
    public PaymentPage(String loggedInemail,int userID, int shipmentID, float weight, String type, String company,int driverID) {
        initComponents();
        this.loggedInemail=loggedInemail;
        this.userID = userID;
        this.shipmentID = shipmentID;
        this.weight = weight;
        this.type = type;
        this.company = company;
        this.driverID=driverID;
        // Calculate total amount and display
         totalAmount = calculateTotalAmount(weight, type, company);
        jTextField1.setText(String.valueOf(totalAmount));
    }
    
    private float calculateTotalAmount(float weight, String type, String company) {
        // Assuming type and company determine the rate, you can define your logic here
        float rate = 0.0f;
        // Adjust the rates based on company and type
        if (company.equals("Delivery Express")) {
            if (type.equals("Documents")) {
                rate = 10.0f; // Rate for documents per kg for Delivery Express
            } else if (type.equals("Fabric/Cloth")) {
                rate = 15.0f; // Rate for fabric/cloth per kg for Delivery Express
            } else if (type.equals("Shoes")) {
                rate = 25.0f; // Rate for shoes per kg for Delivery Express
            } else if (type.equals("Devices/Parts")) {
                rate = 30.0f; // Rate for devices/parts per kg for Delivery Express
            }
        } else if (company.equals("Speedy Couriers")) {
           if (type.equals("Documents")) {
                rate = 11.0f; // Rate for documents per kg for Delivery Express
            } else if (type.equals("Fabric/Cloth")) {
                rate = 14.0f; // Rate for fabric/cloth per kg for Delivery Express
            } else if (type.equals("Shoes")) {
                rate = 21.0f; // Rate for shoes per kg for Delivery Express
            } else if (type.equals("Devices/Parts")) {
                rate = 35.0f; // Rate for devices/parts per kg for Delivery Express
            }
        } else if (company.equals("Express Logistics")) {
          if (type.equals("Documents")) {
                rate = 9.0f; // Rate for documents per kg for Delivery Express
            } else if (type.equals("Fabric/Cloth")) {
                rate = 15.0f; // Rate for fabric/cloth per kg for Delivery Express
            } else if (type.equals("Shoes")) {
                rate = 29.0f; // Rate for shoes per kg for Delivery Express
            } else if (type.equals("Devices/Parts")) {
                rate = 35.0f; // Rate for devices/parts per kg for Delivery Express
            }
        } else if (company.equals("AvinoX")) {
          if (type.equals("Documents")) {
                rate = 10.0f; // Rate for documents per kg for Delivery Express
            } else if (type.equals("Fabric/Cloth")) {
                rate = 15.0f; // Rate for fabric/cloth per kg for Delivery Express
            } else if (type.equals("Shoes")) {
                rate = 25.0f; // Rate for shoes per kg for Delivery Express
            } else if (type.equals("Devices/Parts")) {
                rate = 30.0f; // Rate for devices/parts per kg for Delivery Express
            }
        }else if (company.equals("Swift Deliveries")) {
          if (type.equals("Documents")) {
                rate = 10.0f; // Rate for documents per kg for Delivery Express
            } else if (type.equals("Fabric/Cloth")) {
                rate = 15.0f; // Rate for fabric/cloth per kg for Delivery Express
            } else if (type.equals("Shoes")) {
                rate = 25.0f; // Rate for shoes per kg for Delivery Express
            } else if (type.equals("Devices/Parts")) {
                rate = 30.0f; // Rate for devices/parts per kg for Delivery Express
            }
        }
        else if (company.equals("Quick Ship")) {
          if (type.equals("Documents")) {
                rate = 10.0f; // Rate for documents per kg for Delivery Express
            } else if (type.equals("Fabric/Cloth")) {
                rate = 15.0f; // Rate for fabric/cloth per kg for Delivery Express
            } else if (type.equals("Shoes")) {
                rate = 25.0f; // Rate for shoes per kg for Delivery Express
            } else if (type.equals("Devices/Parts")) {
                rate = 30.0f; // Rate for devices/parts per kg for Delivery Express
            }
        }
        else{
            if (type.equals("Documents")) {
                rate = 15.0f; // Rate for documents per kg for Delivery Express
            } else if (type.equals("Fabric/Cloth")) {
                rate = 20.0f; // Rate for fabric/cloth per kg for Delivery Express
            } else if (type.equals("Shoes")) {
                rate = 30.0f; // Rate for shoes per kg for Delivery Express
            } else if (type.equals("Devices/Parts")) {
                rate = 35.0f; // Rate for devices/parts per kg for Delivery Express
            }
        }
        // Calculate total amount
        return weight * rate;
    }

    private void insertPaymentDetails() {
        
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/courier_db", "root", "SHRU@04");
//            Statement stm = con.createStatement();

            // Insert the payment details into the payment table
            String query = "INSERT INTO payment (TotalAmount, UserID, ShipmentID) VALUES (?, ?, ?)";
            PreparedStatement pstmt = con.prepareStatement(query);
            pstmt.setFloat(1, Float.parseFloat(jTextField1.getText())); // Total Amount
            pstmt.setInt(2, userID); // UserID
            pstmt.setInt(3, shipmentID); // ShipmentID
            pstmt.executeUpdate();
                   
            String deliveryQuery = "INSERT INTO delivery (UserID, ShipmentID, DriverID, Status) VALUES (?, ?, ?, ?)";
        PreparedStatement deliveryPstmt = con.prepareStatement(deliveryQuery);
        deliveryPstmt.setInt(1, userID); // UserID
        deliveryPstmt.setInt(2, shipmentID); // ShipmentID
        deliveryPstmt.setInt(3, driverID); // DriverID
        deliveryPstmt.setString(4, "In Transit"); // DeliveryStatus
        deliveryPstmt.executeUpdate();
        
        
              String locationQuery = "SELECT LocationID FROM location ORDER BY LocationID DESC LIMIT 2";
        Statement locationStmt = con.createStatement();
        ResultSet locationRs = locationStmt.executeQuery(locationQuery);
            int[] locationIDs = new int[2];
        int i = 0;
        while (locationRs.next() && i < 2) {
            locationIDs[i] = locationRs.getInt("LocationID");
            i++;
        }
        
         String updateShipmentQuery = "UPDATE shipment SET OriginAddressID = ?, DestinationID = ? WHERE ShipmentID = ?";
        PreparedStatement updateShipmentPstmt = con.prepareStatement(updateShipmentQuery);
        updateShipmentPstmt.setInt(1, locationIDs[1]);
        updateShipmentPstmt.setInt(2, locationIDs[0]);
        updateShipmentPstmt.setInt(3, shipmentID);
        updateShipmentPstmt.executeUpdate();
        
        
            // Close the statement and connection
            pstmt.close();
            deliveryPstmt.close();
        locationStmt.close();
        updateShipmentPstmt.close();
        con.close();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
   private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(186, 217, 217));

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel1.setText("Payment Details");

        jLabel2.setText("Total Amount:");

        jTextField1.setEditable(false);

        jButton1.setText("Proceed to Payment");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(145, 145, 145)
                        .addComponent(jLabel1))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(65, 65, 65)
                        .addComponent(jLabel2)
                        .addGap(29, 29, 29)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(132, 132, 132)
                        .addComponent(jButton1)))
                .addContainerGap(129, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addComponent(jLabel1)
                .addGap(33, 33, 33)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(35, 35, 35)
                .addComponent(jButton1)
                .addContainerGap(40, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(78, 78, 78)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(123, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(66, 66, 66)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(73, Short.MAX_VALUE))
        );

        pack();
    }
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {                                         
        // Proceed to payment logic goes here
        // For example, you can display a confirmation message
        JOptionPane.showMessageDialog(this, "Payment Successful!");
        // Insert payment details into the payment table
        insertPaymentDetails();
        // Close the payment page after successful payment
        this.dispose();
        
       CustomerShipmentDetails shipmentDetails = new CustomerShipmentDetails(loggedInemail,userID,shipmentID);
    shipmentDetails.setVisible(true);
    }                                        

    // Your existing main method

    // Variables declaration - do not modify                     
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration                   
}
