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
        String user = usernameField.getText();
        String pass = passwordField.getText();

        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(
                     "SELECT * FROM users WHERE username = ? AND password = ?"
             )) {

            pst.setString(1, user);
            pst.setString(2, pass);

            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                String role = rs.getString("role");

                if (role.equals("manager")) {
                    switchToDashboard(); // manager sees dashboard
                } else {
                    switchToUserPanel(rs.getInt("id")); // user sees only their panel
                }

            } else {
                errorLabel.setText("Invalid username or password!");
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("UserPanel.fxml"));
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("User Panel");

            UserPanelController controller = loader.getController();
            controller.setUserId(userId); // send member id to panel

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
