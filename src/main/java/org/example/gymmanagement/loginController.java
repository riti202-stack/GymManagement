package org.example.gymmanagement;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class loginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label errorLabel;

    @FXML
    private void login() {
        String user = usernameField.getText().trim();
        String pass = passwordField.getText().trim();

        if (user.isEmpty() || pass.isEmpty()) {
            errorLabel.setText("Username and password cannot be empty!");
            return;
        }

        try (Connection con = DBConnection.getConnection()) {

            // 1️⃣ Check if user exists
            String checkSql = "SELECT * FROM users WHERE username = ?";
            PreparedStatement checkStmt = con.prepareStatement(checkSql);
            checkStmt.setString(1, user);
            ResultSet rs = checkStmt.executeQuery();

            int userId;

            if (rs.next()) {
                // User exists → verify password
                String dbPass = rs.getString("password");
                String role = rs.getString("role");
                userId = rs.getInt("id");

                if (!dbPass.equals(pass)) {
                    errorLabel.setText("Invalid password!");
                    return;
                }

                // Login successful
                if (role.equals("manager")) {
                    switchToDashboard();
                } else {
                    switchToUserPanel(userId);
                }

            } else {
                // User does NOT exist → insert new user as 'user'
                String insertUserSql = "INSERT INTO users(username,password,role) VALUES(?,?,?)";
                PreparedStatement insertStmt = con.prepareStatement(insertUserSql, PreparedStatement.RETURN_GENERATED_KEYS);
                insertStmt.setString(1, user);
                insertStmt.setString(2, pass);
                insertStmt.setString(3, "user"); // default role
                insertStmt.executeUpdate();

                // Get the generated user ID
                ResultSet generatedKeys = insertStmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    userId = generatedKeys.getInt(1);
                } else {
                    errorLabel.setText("Error creating user!");
                    return;
                }

                // Create default member profile for new user
                String insertMemberSql = "INSERT INTO members(user_id,name,email,phone,join_date) VALUES(?,?,?,?,?)";
                PreparedStatement memberStmt = con.prepareStatement(insertMemberSql);
                memberStmt.setInt(1, userId);
                memberStmt.setString(2, user); // default name = username
                memberStmt.setString(3, ""); // email blank
                memberStmt.setString(4, ""); // phone blank
                memberStmt.setString(5, java.time.LocalDate.now().toString()); // join date = today
                memberStmt.executeUpdate();

                System.out.println("🆕 New user created and logged in: " + user);
                switchToUserPanel(userId);
            }

        } catch (Exception e) {
            e.printStackTrace();
            errorLabel.setText("Database error!");
        }
    }

    private void switchToDashboard() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Dashboard.fxml"));
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("Gym Management Dashboard");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void switchToUserPanel(int userId) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("userPanel.fxml"));
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("User Panel");

            userController controller = loader.getController();
            controller.setUserId(userId); // send member id to panel

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
