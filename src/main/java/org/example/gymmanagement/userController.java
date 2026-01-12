package org.example.gymmanagement;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class userController {

    private int userId;
    private int memberId;

    // ---------------- TableViews ----------------
    @FXML private TableView<Payment> paymentsTable;
    @FXML private TableColumn<Payment, Double> paymentAmountCol;
    @FXML private TableColumn<Payment, String> paymentDateCol;

    @FXML private TableView<Attendance> attendanceTable;
    @FXML private TableColumn<Attendance, String> attendanceDateCol;
    @FXML private TableColumn<Attendance, String> attendanceStatusCol;

    @FXML private TableView<Workout> workoutTable;
    @FXML private TableColumn<Workout, String> workoutExerciseCol;
    @FXML private TableColumn<Workout, Integer> workoutRepsCol;
    @FXML private TableColumn<Workout, String> workoutDateCol;

    @FXML private TableView<Diet> dietTable;
    @FXML private TableColumn<Diet, String> dietMealCol;
    @FXML private TableColumn<Diet, Integer> dietCaloriesCol;
    @FXML private TableColumn<Diet, String> dietDateCol;

    @FXML private TableView<Weight> weightTable;
    @FXML private TableColumn<Weight, Double> weightCol;
    @FXML private TableColumn<Weight, String> weightDateCol;

    // ---------------- Initialize ----------------
    @FXML
    private void initialize() {
        setupTableColumns();
    }

    private void clearAllTables() {
        paymentsTable.setItems(FXCollections.observableArrayList());
        attendanceTable.setItems(FXCollections.observableArrayList());
        workoutTable.setItems(FXCollections.observableArrayList());
        dietTable.setItems(FXCollections.observableArrayList());
        weightTable.setItems(FXCollections.observableArrayList());
    }


    private void setupTableColumns() {
        // Payments
        paymentAmountCol.setCellValueFactory(data -> new javafx.beans.property.SimpleDoubleProperty(data.getValue().getAmount()).asObject());
        paymentDateCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getDate()));

        // Attendance
        attendanceDateCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getDate()));
        attendanceStatusCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getStatus()));

        // Workout
        workoutExerciseCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getExercise()));
        workoutRepsCol.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getReps()).asObject());
        workoutDateCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getDate()));

        // Diet
        dietMealCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getMeal()));
        dietCaloriesCol.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getCalories()).asObject());
        dietDateCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getDate()));

        // Weight
        weightCol.setCellValueFactory(data -> new javafx.beans.property.SimpleDoubleProperty(data.getValue().getWeight()).asObject());
        weightDateCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getDate()));
    }

    // ---------------- Called after login/from other controllers ----------------
    public void setUserId(int userId) {
        System.out.println("🔄 setUserId called with: " + userId);
        this.userId = userId;
        this.memberId = getMemberId(userId);

        System.out.println("👤 Member ID resolved: " + memberId);

        if (memberId == -1) {
            System.err.println("❌ Member not found for userId: " + userId);
            clearAllTables();
            return;
        }

        loadAllData();
        System.out.println("✅ All tables loaded for member: " + memberId);
    }


    // ---------------- Load ALL data for this specific member ----------------
    private void loadAllData() {
        loadPayments();
        loadAttendance();
        loadWorkout();
        loadDiet();
        loadWeight();
    }

    // ---------------- Helper to get member_id ----------------
    private int getMemberId(int userId) {
        String query = "SELECT id FROM members WHERE user_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {
            pst.setInt(1, userId);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    // ---------------- Load data methods (ALL FIXED with proper WHERE clauses) ----------------
    private void loadPayments() {
        ObservableList<Payment> list = FXCollections.observableArrayList();
        String query = "SELECT amount, paid_date FROM payments WHERE member_id = ? ORDER BY paid_date DESC";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {
            pst.setInt(1, memberId);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                list.add(new Payment(rs.getDouble("amount"), rs.getString("paid_date")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        paymentsTable.setItems(list);
    }

    private void loadAttendance() {
        ObservableList<Attendance> list = FXCollections.observableArrayList();
        String query = "SELECT date, status FROM attendance WHERE member_id = ? ORDER BY date DESC";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {
            pst.setInt(1, memberId);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                list.add(new Attendance(rs.getString("date"), rs.getString("status")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        attendanceTable.setItems(list);
    }

    private void loadWorkout() {
        ObservableList<Workout> list = FXCollections.observableArrayList();
        String query = "SELECT exercise, reps, day FROM workout WHERE member_id = ? ORDER BY day DESC";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {
            pst.setInt(1, memberId);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                list.add(new Workout(rs.getString("exercise"), rs.getInt("reps"), rs.getString("day")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        workoutTable.setItems(list);
    }

    private void loadDiet() {
        ObservableList<Diet> list = FXCollections.observableArrayList();
        String query = "SELECT meal, calories, date FROM diet WHERE member_id = ? ORDER BY date DESC";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {
            pst.setInt(1, memberId);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                list.add(new Diet(rs.getString("meal"), rs.getInt("calories"), rs.getString("date")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        dietTable.setItems(list);
    }

    private void loadWeight() {
        ObservableList<Weight> list = FXCollections.observableArrayList();
        String query = "SELECT weight, record_date FROM weight WHERE member_id = ? ORDER BY record_date DESC";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {
            pst.setInt(1, memberId);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                list.add(new Weight(rs.getDouble("weight"), rs.getString("record_date")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        weightTable.setItems(list);
    }

    // ---------------- Clear tables with message ----------------
    private void clearAllTables(String message) {
        paymentsTable.setItems(FXCollections.emptyObservableList());
        attendanceTable.setItems(FXCollections.emptyObservableList());
        workoutTable.setItems(FXCollections.emptyObservableList());
        dietTable.setItems(FXCollections.emptyObservableList());
        weightTable.setItems(FXCollections.emptyObservableList());
    }

    // ---------------- Navigation ----------------
    @FXML
    private void goToExercise(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Exercise.fxml"));
            Scene scene = new Scene(loader.load());

            ExerciseController controller = loader.getController();
            controller.setMemberId(this.memberId);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Exercise");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML private Button logoutBtn; // Add field

    @FXML
    private void handleLogout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("hello-view.fxml"));
            Stage stage = (Stage) logoutBtn.getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("Gym Management Login");
            System.out.println("👋 User logged out");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // ---------------- Model Classes (UNCHANGED) ----------------
    public static class Payment {
        private final double amount;
        private final String date;
        public Payment(double amount, String date) { this.amount = amount; this.date = date; }
        public double getAmount() { return amount; }
        public String getDate() { return date; }
    }

    public static class Attendance {
        private final String date, status;
        public Attendance(String date, String status) { this.date = date; this.status = status; }
        public String getDate() { return date; }
        public String getStatus() { return status; }
    }

    public static class Workout {
        private final String exercise, date;
        private final int reps;
        public Workout(String exercise, int reps, String date) {
            this.exercise = exercise; this.reps = reps; this.date = date;
        }
        public String getExercise() { return exercise; }
        public int getReps() { return reps; }
        public String getDate() { return date; }
    }

    public static class Diet {
        private final String meal, date;
        private final int calories;
        public Diet(String meal, int calories, String date) {
            this.meal = meal; this.calories = calories; this.date = date;
        }
        public String getMeal() { return meal; }
        public int getCalories() { return calories; }
        public String getDate() { return date; }
    }

    public static class Weight {
        private final double weight;
        private final String date;
        public Weight(double weight, String date) { this.weight = weight; this.date = date; }
        public double getWeight() { return weight; }
        public String getDate() { return date; }
    }
}
