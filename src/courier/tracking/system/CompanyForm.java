package courier.tracking.system;
import courier.tracking.system.AdminLogin;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CompanyForm extends JFrame {

    private final Allcompany parent;
    private final String title;
    private final int companyId;

    private JTextField nameField, contactField;
    private JButton saveButton;

    public CompanyForm(Allcompany parent, String title, int companyId) {
        this.parent = parent;
        this.title = title;
        this.companyId = companyId;

        initComponents();
        populateFields();
        setupSaveButtonAction();

        setTitle(title);
        setSize(400, 200);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
        setVisible(true);
    }

    private void initComponents() {
        setLayout(new GridLayout(3, 2, 10, 10));

        add(new JLabel("Company Name:"));
        nameField = new JTextField();
        add(nameField);

        add(new JLabel("Contact:"));
        contactField = new JTextField();
        add(contactField);

        add(new JPanel()); // Empty panel for spacing

        saveButton = new JButton("Save");
        add(saveButton);
    }

    private void populateFields() {
        if (title.equals("Update Company")) {
            try (Connection connection = AdminLogin.DatabaseHandler.getConnection()) {
                String query = "SELECT * FROM company WHERE companyid = ?";
                try (PreparedStatement statement = connection.prepareStatement(query)) {
                    statement.setInt(1, companyId);
                    var resultSet = statement.executeQuery();

                    if (resultSet.next()) {
                        nameField.setText(resultSet.getString("companyname"));
                        contactField.setText(resultSet.getString("contact"));
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void setupSaveButtonAction() {
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveCompany();
            }
        });
    }

    private void saveCompany() {
        String companyName = nameField.getText();
        String contact = contactField.getText();
        if (companyName.isEmpty() || contact.isEmpty() ) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try (Connection connection = AdminLogin.DatabaseHandler.getConnection()) {
            String query;

            if (title.equals("Add Company")) {
                query = "INSERT INTO company (companyname, contact) VALUES (?, ?)";
            } else {
                query = "UPDATE company SET companyname = ?, contact = ? WHERE companyid = ?";
            }

            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, companyName);
                statement.setString(2, contact);

                if (title.equals("Update Company")) {
                    statement.setInt(3, companyId);
                }

                statement.executeUpdate();
            }

            JOptionPane.showMessageDialog(this, title + " successful.", "Success", JOptionPane.INFORMATION_MESSAGE);
            dispose();
           parent.displayCompanies(); // Refresh the table after adding/updating
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        new CompanyForm(null, "Add Company", -1);
    }
}
