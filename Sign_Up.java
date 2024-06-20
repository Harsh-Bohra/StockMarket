import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.logging.*;

public class Sign_Up extends JPanel {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/trademill";
    private static final String USER = "root";
    private static final String PASS = "12345678";
    private static final Logger logger = Logger.getLogger(Sign_Up.class.getName());
    private final MainApp mainApp;

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JFrame mainFrame;

    public Sign_Up(JFrame mainFrame, MainApp mainApp) {
        this.mainFrame = mainFrame;
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

        JLabel confirmPasswordLabel = new JLabel("Confirm password:");
        add(confirmPasswordLabel, gbc);
        gbc.gridy++;
        confirmPasswordField = new JPasswordField(20);
        add(confirmPasswordField, gbc);
        gbc.gridy++;

        JButton signUpButton = new JButton("Sign Up");
        signUpButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = String.valueOf(passwordField.getPassword());
            String confirmPassword = String.valueOf(confirmPasswordField.getPassword());

            if (validateSignUp(username, password, confirmPassword)) {
                clearFields();
                mainApp.showMainPanel();
                mainFrame.revalidate();
                mainFrame.repaint();
            } else {
                clearFields();
                JOptionPane.showMessageDialog(this, "Sign-up unsuccessful. Please try again.");
            }
        });
        add(signUpButton, gbc);
        gbc.gridy++;

        JButton clearButton = new JButton("Clear Fields");
        clearButton.addActionListener(e -> clearFields());
        add(clearButton, gbc);
        gbc.gridy++;
    }

    private boolean validateSignUp(String username, String password, String confirmPassword) {
        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, "Passwords do not match. Please try again.");
            confirmPasswordField.setText("");
            return false;
        }

        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS)) {
            if (connection != null && !connection.isClosed()) {
                System.out.println("Connected to the database successfully!");
                int signUpResult = signUp(connection, username, password);
                if (signUpResult == 1) {
                    JOptionPane.showMessageDialog(this, "Sign up successful! User added to the database.");
                    clearFields();
                    mainApp.showMainPanel();
                    return true;
                } else {
                    JOptionPane.showMessageDialog(this, "Username already exists. Please choose a different username.");
                    return false;
                }
            } else {
                System.out.println("Failed to connect to the database!");
                JOptionPane.showMessageDialog(this, "Failed to connect to the database. Please try again later.");
                return false;
            }
        } catch (SQLException exception) {
            logger.log(Level.SEVERE, "Error during sign up", exception);
            handleSqlException(exception);
            return false;
        }
    }

    private static int signUp(Connection connection, String username, String password) {
        try {
            int nextUserId = getNextUserId(connection);

            String insertQuery = "INSERT INTO users (user_id, username, password) VALUES (?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
                preparedStatement.setInt(1, nextUserId);
                preparedStatement.setString(2, username);
                preparedStatement.setString(3, password);
                preparedStatement.executeUpdate();
                return 1;
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error during sign up", e);
            return 0;
        }
    }

    private static int getNextUserId(Connection connection) {
        try {
            String getMaxUserIdQuery = "SELECT MAX(user_id) FROM users";
            try (PreparedStatement preparedStatement = connection.prepareStatement(getMaxUserIdQuery);
                 ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1) + 1;
                } else {
                    return 1;
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error getting next user ID", e);
            throw new RuntimeException("Error getting next user ID", e);
        }
    }

    private void handleSqlException(SQLException exception) {
        JOptionPane.showMessageDialog(this, "Error during sign up: " + exception.getMessage());
        clearFields();
    }

    private void clearFields() {
        usernameField.setText("");
        passwordField.setText("");
        confirmPasswordField.setText("");
    }
}
