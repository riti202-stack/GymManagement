package org.example.gymmanagement;



import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class userController {

    private int userId;

    @FXML
    private TableView<Payment> paymentsTable;
    @FXML
    private TableColumn<Payment, Integer> paymentIdCol;
    @FXML
    private TableColumn<Payment, Double> paymentAmountCol;
    @FXML
    private TableColumn<Payment, String> paymentDateCol;

    @FXML
    private TableView<Attendance> attendanceTable;
    @FXML
    private TableColumn<Attendance, String> attendanceDateCol;
    @FXML
    private TableColumn<Attendance, String> attendanceStatusCol;

    @FXML
    private TableView<Workout> workoutTable;
    @FXML
    private TableColumn<Workout, String> workoutExerciseCol;
    @FXML
    private TableColumn<Workout, Integer> workoutRepsCol;
    @FXML
    private TableColumn<Workout, String> workoutDateCol;

    @FXML
    private TableView<Diet> dietTable;
    @FXML
    private TableColumn<Diet, String> dietMealCol;
    @FXML
    private TableColumn<Diet, Integer> dietCaloriesCol;
    @FXML
    private TableColumn<Diet, String> dietDateCol;

    @FXML
    private TableView<Weight> weightTable;
    @FXML
    private TableColumn<Weight, Double> weightCol;
    @FXML
    private TableColumn<Weight, String> weightDateCol;

    // Method to be called after login
    public void setUserId(int userId) {
        this.userId = userId;
        loadPayments();
        loadAttendance();
        loadWorkout();
        loadDiet();
        loadWeight();
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:sqlite:test.db");
    }

    private void loadPayments() {
        ObservableList<Payment> list = FXCollections.observableArrayList();
        String query = "SELECT * FROM payments WHERE user_id = ?";
        try (Connection con = getConnection(); PreparedStatement pst = con.prepareStatement(query)) {
            pst.setInt(1, userId);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                list.add(new Payment(rs.getInt("id"), rs.getDouble("amount"), rs.getString("date")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        paymentsTable.setItems(list);
    }

    private void loadAttendance() {
        ObservableList<Attendance> list = FXCollections.observableArrayList();
        String query = "SELECT * FROM attendance WHERE user_id = ?";
        try (Connection con = getConnection(); PreparedStatement pst = con.prepareStatement(query)) {
            pst.setInt(1, userId);
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
        String query = "SELECT * FROM workout WHERE user_id = ?";
        try (Connection con = getConnection(); PreparedStatement pst = con.prepareStatement(query)) {
            pst.setInt(1, userId);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                list.add(new Workout(rs.getString("exercise"), rs.getInt("reps"), rs.getString("date")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        workoutTable.setItems(list);
    }

    private void loadDiet() {
        ObservableList<Diet> list = FXCollections.observableArrayList();
        String query = "SELECT * FROM diet WHERE user_id = ?";
        try (Connection con = getConnection(); PreparedStatement pst = con.prepareStatement(query)) {
            pst.setInt(1, userId);
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
        String query = "SELECT * FROM weight WHERE user_id = ?";
        try (Connection con = getConnection(); PreparedStatement pst = con.prepareStatement(query)) {
            pst.setInt(1, userId);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                list.add(new Weight(rs.getDouble("weight"), rs.getString("date")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        weightTable.setItems(list);
    }

    // Model classes
    public static class Payment {
        public int id; public double amount; public String date;
        public Payment(int id, double amount, String date) { this.id=id; this.amount=amount; this.date=date; }
        // Getters & setters
        public int getId(){return id;} public double getAmount(){return amount;} public String getDate(){return date;}
    }
    public static class Attendance { public String date, status; public Attendance(String d,String s){date=d;status=s;}
        public String getDate(){return date;} public String getStatus(){return status;}
    }
    public static class Workout { public String exercise, date; public int reps; public Workout(String e,int r,String d){exercise=e;reps=r;date=d;}
        public String getExercise(){return exercise;} public int getReps(){return reps;} public String getDate(){return date;}
    }
    public static class Diet { public String meal, date; public int calories; public Diet(String m,int c,String d){meal=m;calories=c;date=d;}
        public String getMeal(){return meal;} public int getCalories(){return calories;} public String getDate(){return date;}
    }
    public static class Weight { public double weight; public String date; public Weight(double w,String d){weight=w;date=d;}
        public double getWeight(){return weight;} public String getDate(){return date;}
    }
}

