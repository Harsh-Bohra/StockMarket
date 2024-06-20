import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class Sign_In extends JPanel {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/trademill";
    private static final String USER = "root";
    private static final String PASS = "12345678";

    private JTextField usernameField;
    private JPasswordField passwordField;

    private MainApp mainApp;

    public Sign_In(MainApp mainApp) {
        this.mainApp = mainApp;
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel usernameLabel = new JLabel("Enter username:");
        add(usernameLabel, gbc);
        gbc.gridy++;
        usernameField = new JTextField(20);
        add(usernameField, gbc);
        gbc.gridy++;

        JLabel passwordLabel = new JLabel("Enter password:");
        add(passwordLabel, gbc);
        gbc.gridy++;
        passwordField = new JPasswordField(20);
        add(passwordField, gbc);
        gbc.gridy++;

        JButton signInButton = new JButton("Sign In");
        signInButton.addActionListener(e -> signIn());
        add(signInButton, gbc);
        gbc.gridy++;

        JButton clearButton = new JButton("Clear Fields");
        clearButton.addActionListener(e -> clearFields());
        add(clearButton, gbc);
        gbc.gridy++;
    }

    private void signIn() {
        String username = usernameField.getText();
        String password = String.valueOf(passwordField.getPassword());

        if (validateSignIn(username, password)) {
            clearFields();
            JOptionPane.showMessageDialog(this, "Successful sign-in"); 
            mainApp.showPortfolioPanel(); 
        } else {
            clearFields();
            JOptionPane.showMessageDialog(this, "Sign-in unsuccessful. Please try again.");
        }
    }

    private boolean validateSignIn(String username, String password) {
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS)) {
            String query = "SELECT password FROM users WHERE username = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, username);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        String storedPassword = resultSet.getString("password");
                        if (password.equals(storedPassword)) {
                            // Update login_status to TRUE for the logged-in user
                            updateLoginStatus(connection, username, true);
                            return true;
                        }
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return false;
    }

    private void updateLoginStatus(Connection connection, String username, boolean status) throws SQLException {
        String query = "UPDATE users SET login_status = ? WHERE username = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setBoolean(1, status);
            statement.setString(2, username);
            statement.executeUpdate();
        }
    }

    private void clearFields() {
        usernameField.setText("");
        passwordField.setText("");
    }
}

