package org.example.gymmanagement;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import util.DBConnection;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {

    @FXML private Label logout;
    @FXML private Label paymentsLabel;
    @FXML private Label totalMembersLabel;      // ← NEW
    @FXML private Label monthlyIncomeLabel;     // ← NEW

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadDashboardStats();  // Load initial stats
        startAutoRefresh();    // Auto-update every 30 seconds
    }

    private void loadDashboardStats() {
        // Total Members
        updateTotalMembers();

        // Monthly Income (current month)
        updateMonthlyIncome();
    }

    private void updateTotalMembers() {
        String query = "SELECT COUNT(*) as total FROM members";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(query);
             ResultSet rs = pst.executeQuery()) {

            if (rs.next()) {
                int total = rs.getInt("total");
                totalMembersLabel.setText(String.valueOf(total));
                System.out.println("👥 Total Members: " + total);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateMonthlyIncome() {
        // Current month income
        YearMonth currentMonth = YearMonth.now();
        String monthStart = currentMonth.atDay(1).format(DateTimeFormatter.ISO_LOCAL_DATE);
        String monthEnd = currentMonth.atEndOfMonth().format(DateTimeFormatter.ISO_LOCAL_DATE);

        String query = """
            SELECT COALESCE(SUM(amount), 0) as monthly_total 
            FROM payments 
            WHERE paid_date BETWEEN ? AND ?
            """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(query)) {

            pst.setString(1, monthStart);
            pst.setString(2, monthEnd);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                double monthlyTotal = rs.getDouble("monthly_total");
                monthlyIncomeLabel.setText(String.format("$%,.0f", monthlyTotal));
                System.out.println("💰 Monthly Income: $" + monthlyTotal);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void startAutoRefresh() {
        AnimationTimer timer = new AnimationTimer() {
            private long lastUpdate = 0;

            @Override
            public void handle(long now) {
                if (now - lastUpdate > 30_000_000_000L) { // 30 seconds
                    loadDashboardStats();
                    lastUpdate = now;
                }
            }
        };
        timer.start();
    }

    @FXML private void showPayments() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Payments.fxml"));
            Stage stage = (Stage) paymentsLabel.getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("Payments Management");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML private void handleLogout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("hello-view.fxml"));
            Stage stage = (Stage) logout.getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("Gym Management Login");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
