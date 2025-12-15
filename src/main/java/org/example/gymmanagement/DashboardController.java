package org.example.gymmanagement;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.awt.event.MouseEvent;

public class DashboardController {

    @FXML
    private Label logout;

    @FXML
    private void handleLogout() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("hello-view.fxml")
            );

            Stage stage = (Stage) logout.getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("Gym Management Login");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

