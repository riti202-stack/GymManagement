package org.example.gymmanagement;


import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;

public class workoutController implements Initializable {

    // Top section
    @FXML
    private ComboBox<String> memberComboBox;

    @FXML
    private Label memberInfoLabel;

    // Workout table
    @FXML
    private TableView<Workout> workoutTable;

    @FXML
    private TableColumn<Workout, String> exerciseCol;

    @FXML
    private TableColumn<Workout, Integer> setsCol;

    @FXML
    private TableColumn<Workout, Integer> repsCol;

    @FXML
    private TableColumn<Workout, String> dayCol;

    // Diet table (simple String rows)
    @FXML
    private TableView<String> dietTable;

    @FXML
    private TableColumn<String, String> mealCol;

    @FXML
    private TableColumn<String, String> foodCol;

    @FXML
    private TableColumn<String, String> caloriesCol;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


        memberComboBox.getItems().addAll("John", "Alex", "Rahim");
        memberComboBox.getSelectionModel().selectFirst();

        memberInfoLabel.setText("Age: 25 | Weight: 70kg");


        exerciseCol.setCellValueFactory(data -> data.getValue().exerciseProperty());
        setsCol.setCellValueFactory(data -> data.getValue().setsProperty().asObject());
        repsCol.setCellValueFactory(data -> data.getValue().repsProperty().asObject());
        dayCol.setCellValueFactory(data -> data.getValue().dayProperty());


    }
}
