package org.example.gymmanagement;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
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

    // ---------------- Initialize TableColumns ----------------
    @FXML
    private void initialize() {
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

    // ---------------- Called after login ----------------
    public void setUserId(int userId) {
        this.userId = userId;
        this.memberId = getMemberId(userId);

        if (memberId == -1) {
            System.out.println("Member not found for this user.");
            return;
        }

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
            if (rs.next()) return rs.getInt("id");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    // ---------------- Load data methods ----------------
    private void loadPayments() {
        ObservableList<Payment> list = FXCollections.observableArrayList();
        String query = "SELECT * FROM payments WHERE member_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {
            pst.setInt(1, memberId);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                list.add(new Payment(rs.getDouble("amount"), rs.getString("paid_date")));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        paymentsTable.setItems(list);
    }

    private void loadAttendance() {
        ObservableList<Attendance> list = FXCollections.observableArrayList();
        String query = "SELECT * FROM attendance WHERE member_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {
            pst.setInt(1, memberId);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                list.add(new Attendance(rs.getString("date"), rs.getString("status")));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        attendanceTable.setItems(list);
    }

    private void loadWorkout() {
        ObservableList<Workout> list = FXCollections.observableArrayList();
        String query = "SELECT * FROM workout WHERE member_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {
            pst.setInt(1, memberId);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                list.add(new Workout(rs.getString("exercise"), rs.getInt("reps"), rs.getString("day")));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        workoutTable.setItems(list);
    }

    private void loadDiet() {
        ObservableList<Diet> list = FXCollections.observableArrayList();
        String query = "SELECT * FROM diet WHERE member_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {
            pst.setInt(1, memberId);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                list.add(new Diet(rs.getString("meal"), rs.getInt("calories"), rs.getString("date")));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        dietTable.setItems(list);
    }

    private void loadWeight() {
        ObservableList<Weight> list = FXCollections.observableArrayList();
        String query = "SELECT * FROM weight WHERE member_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {
            pst.setInt(1, memberId);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                list.add(new Weight(rs.getDouble("weight"), rs.getString("record_date")));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        weightTable.setItems(list);
    }

    // ---------------- Model Classes ----------------
    public static class Payment {
        private final double amount;
        private final String date;
        public Payment(double amount, String date){ this.amount = amount; this.date = date; }
        public double getAmount(){ return amount; }
        public String getDate(){ return date; }
    }

    public static class Attendance {
        private final String date, status;
        public Attendance(String date,String status){ this.date=date; this.status=status; }
        public String getDate(){ return date; }
        public String getStatus(){ return status; }
    }

    public static class Workout {
        private final String exercise, date;
        private final int reps;
        public Workout(String exercise,int reps,String date){ this.exercise=exercise; this.reps=reps; this.date=date; }
        public String getExercise(){ return exercise; }
        public int getReps(){ return reps; }
        public String getDate(){ return date; }
    }

    public static class Diet {
        private final String meal, date;
        private final int calories;
        public Diet(String meal,int calories,String date){ this.meal=meal; this.calories=calories; this.date=date; }
        public String getMeal(){ return meal; }
        public int getCalories(){ return calories; }
        public String getDate(){ return date; }
    }

    public static class Weight {
        private final double weight;
        private final String date;
        public Weight(double weight,String date){ this.weight=weight; this.date=date; }
        public double getWeight(){ return weight; }
        public String getDate(){ return date; }
    }
}
