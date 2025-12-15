package org.example.gymmanagement;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class loginController {

    public Button loginBtn;
    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label errorLabel;

    @FXML
    private void initialize() {

        errorLabel.setText("");
    }
    @FXML
    private void login() {
        String user = usernameField.getText();
        String pass = passwordField.getText();

        if (user.equals("admin") && pass.equals("1234")) {
            errorLabel.setText("Login successful!");
            switchToDashboard();


        } else {
            errorLabel.setText("Invalid username or password!");
        }
    }

    private void switchToDashboard() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("dashboard.fxml")
            );

            Stage stage = (Stage) loginBtn.getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("Gym Management Dashboard");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


