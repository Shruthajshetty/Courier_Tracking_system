/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package courier.tracking.system;
import java.awt.Font;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
/**
 *
 * @author User
 */
public class CustAllShips extends javax.swing.JFrame {
    String email;
    /**
     * Creates new form CustAllShips
     */
    public CustAllShips(String email) {
        initComponents();
        this.email=email;
        setExtendedState(SignUp.MAXIMIZED_BOTH);
         JTableHeader header =  jTable1.getTableHeader();
        header.setFont(new Font("Arial", Font.BOLD, 30));
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(null);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(null);

        jLabel1.setBackground(new java.awt.Color(0, 0, 102));
        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jLabel1.setText("Your Shipments");
        jPanel1.add(jLabel1);
        jLabel1.setBounds(640, 30, 350, 80);

        jTable1.setBackground(new java.awt.Color(255, 204, 204));
        jTable1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Shipment ID", "Type", "Weight", "Company", "Price"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.setRowHeight(40);
        jTable1.setRowMargin(1);
        jTable1.setSelectionBackground(new java.awt.Color(0, 0, 51));
        jScrollPane1.setViewportView(jTable1);

        jPanel1.add(jScrollPane1);
        jScrollPane1.setBounds(300, 120, 970, 430);

        jButton1.setBackground(new java.awt.Color(255, 204, 204));
        jButton1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jButton1.setText(">");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton1);
        jButton1.setBounds(200, 90, 32, 32);

        getContentPane().add(jPanel1);
        jPanel1.setBounds(-60, -10, 1960, 1190);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        new Cwelcome(email).setVisible(true);
        dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

   
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(CustAllShips.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(CustAllShips.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(CustAllShips.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(CustAllShips.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                System.out.println("yes");
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables

   public void display(String loggedInUsername) {
         try {
            // Connect to the database
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/courier_db", "root", "SHRU@04");

            // Prepare SQL statement to retrieve shipment details
        String sql = "SELECT s.ShipmentID, s.Type, s.Weight, c.CompanyName, p.TotalAmount " +
             "FROM shipment s " +
             "INNER JOIN company c ON s.CompanyID = c.CompanyID " +
             "INNER JOIN payment p ON s.ShipmentID = p.ShipmentID " +
             "INNER JOIN user u ON s.UserID = u.UserID " +
             "INNER JOIN delivery d ON s.ShipmentID = d.ShipmentID " +  // Added space after the join condition
             "WHERE u.Email = ? " +
             "AND d.Status IN ('delivered', 'in transit') " +
             "ORDER BY s.ShippingDate DESC";   // Order by shipment date descending
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, loggedInUsername);

            // Execute the query
            ResultSet resultSet = statement.executeQuery();

            // Populate the jTable1 with the retrieved shipment details
            DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
            model.setRowCount(0); // Clear existing table data
            while (resultSet.next()) {
                Object[] rowData = {
                    resultSet.getInt("ShipmentID"),
                    resultSet.getString("Type"),
                    resultSet.getDouble("Weight"),
                    resultSet.getString("CompanyName"),
                    resultSet.getDouble("TotalAmount")
                };
                model.addRow(rowData);
            }

            // Close the connections
            resultSet.close();
            statement.close();
            conn.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}