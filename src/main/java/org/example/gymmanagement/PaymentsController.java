package org.example.gymmanagement;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import util.DBConnection;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class PaymentsController implements Initializable {

    @FXML private TableView<PaymentRecord> paymentsTable;
    @FXML private TableColumn<PaymentRecord, Integer> memberIdCol;
    @FXML private TableColumn<PaymentRecord, String> memberNameCol;
    @FXML private TableColumn<PaymentRecord, Double> amountCol;
    @FXML private TableColumn<PaymentRecord, String> dateCol;
    @FXML private TableColumn<PaymentRecord, String> statusCol;

    @FXML private Label totalIncomeLabel;
    @FXML private Button refreshBtn, backBtn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupTableColumns();
        loadPaymentsData();
        updateTotalIncome();
    }

    private void setupTableColumns() {
        memberIdCol.setCellValueFactory(data -> data.getValue().memberIdProperty().asObject());
        memberNameCol.setCellValueFactory(data -> data.getValue().memberNameProperty());
        amountCol.setCellValueFactory(data -> data.getValue().amountProperty().asObject());
        dateCol.setCellValueFactory(data -> data.getValue().dateProperty());
        statusCol.setCellValueFactory(data -> data.getValue().statusProperty());
    }

    @FXML
    private void refreshData() {
        loadPaymentsData();
        updateTotalIncome();
        System.out.println("🔄 Payments refreshed");
    }

    private void loadPaymentsData() {
        ObservableList<PaymentRecord> payments = FXCollections.observableArrayList();

        String query = """
            SELECT p.id, p.member_id, m.name as member_name, p.amount, p.paid_date, 
                   CASE WHEN p.amount > 500 THEN 'Premium' ELSE 'Regular' END as status
            FROM payments p 
            JOIN members m ON p.member_id = m.id 
            ORDER BY p.paid_date DESC
            """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(query);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                payments.add(new PaymentRecord(
                        rs.getInt("member_id"),
                        rs.getString("member_name"),
                        rs.getDouble("amount"),
                        rs.getString("paid_date"),
                        rs.getString("status")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        paymentsTable.setItems(payments);
        System.out.println("📊 Loaded " + payments.size() + " payment records");
    }

    private void updateTotalIncome() {
        String query = "SELECT SUM(amount) as total FROM payments";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(query);
             ResultSet rs = pst.executeQuery()) {

            if (rs.next()) {
                double total = rs.getDouble("total");
                totalIncomeLabel.setText(String.format("Total Income: $%.2f", total));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void goBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Dashboard.fxml"));
            Stage stage = (Stage) backBtn.getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("Gym Management Dashboard");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Payment Record Model Class
    public static class PaymentRecord {
        private final javafx.beans.property.SimpleIntegerProperty memberId;
        private final javafx.beans.property.SimpleStringProperty memberName;
        private final javafx.beans.property.SimpleDoubleProperty amount;
        private final javafx.beans.property.SimpleStringProperty date;
        private final javafx.beans.property.SimpleStringProperty status;

        public PaymentRecord(int memberId, String memberName, double amount, String date, String status) {
            this.memberId = new javafx.beans.property.SimpleIntegerProperty(memberId);
            this.memberName = new javafx.beans.property.SimpleStringProperty(memberName);
            this.amount = new javafx.beans.property.SimpleDoubleProperty(amount);
            this.date = new javafx.beans.property.SimpleStringProperty(date);
            this.status = new javafx.beans.property.SimpleStringProperty(status);
        }

        // Getters for properties
        public javafx.beans.property.SimpleIntegerProperty memberIdProperty() { return memberId; }
        public javafx.beans.property.SimpleStringProperty memberNameProperty() { return memberName; }
        public javafx.beans.property.SimpleDoubleProperty amountProperty() { return amount; }
        public javafx.beans.property.SimpleStringProperty dateProperty() { return date; }
        public javafx.beans.property.SimpleStringProperty statusProperty() { return status; }
    }
}
