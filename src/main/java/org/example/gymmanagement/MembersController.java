package org.example.gymmanagement;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import util.DBConnection;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class MembersController implements Initializable {

    @FXML private TableView<MemberRecord> membersTable;
    @FXML private TableColumn<MemberRecord, Integer> memberIdCol;
    @FXML private TableColumn<MemberRecord, String> nameCol, emailCol, firstLoginCol, latestPaymentCol, latestWeightCol, joinDateCol;
    @FXML private TableColumn<MemberRecord, Double> totalPaidCol;

    @FXML private Label totalMembersLabel;
    @FXML private Button refreshBtn, backBtn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (memberIdCol != null) {
            setupTableColumns();
        } else {
            System.err.println("❌ TableColumn fx:ids missing in Members.fxml");
        }
        loadMembersData();
        updateTotalMembersCount();
    }

    private void setupTableColumns() {
        memberIdCol.setCellValueFactory(new PropertyValueFactory<>("memberId"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        firstLoginCol.setCellValueFactory(new PropertyValueFactory<>("firstLogin"));
        latestPaymentCol.setCellValueFactory(new PropertyValueFactory<>("latestPayment"));
        totalPaidCol.setCellValueFactory(new PropertyValueFactory<>("totalPaid"));
        latestWeightCol.setCellValueFactory(new PropertyValueFactory<>("latestWeight"));
        joinDateCol.setCellValueFactory(new PropertyValueFactory<>("joinDate"));
    }

    @FXML
    private void refreshData() {
        loadMembersData();
        updateTotalMembersCount();
    }

    private void loadMembersData() {
        ObservableList<MemberRecord> members = FXCollections.observableArrayList();

        String query = """
            SELECT m.id, m.name, m.email, m.join_date,
                   COALESCE(MAX(p.paid_date), 'No payments') as latest_payment,
                   COALESCE(SUM(p.amount), 0) as total_paid,
                   COALESCE(MAX(w.weight), 0) as latest_weight_val,
                   COALESCE(m.join_date, 'N/A') as first_login
            FROM members m 
            LEFT JOIN payments p ON m.id = p.member_id
            LEFT JOIN weight w ON m.id = w.member_id
            GROUP BY m.id, m.name, m.email, m.join_date
            ORDER BY m.id
            """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(query);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                String latestWeight = rs.getDouble("latest_weight_val") > 0 ?
                        rs.getDouble("latest_weight_val") + " kg" : "No weight";

                members.add(new MemberRecord(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email") != null ? rs.getString("email") : "",
                        rs.getString("first_login"),
                        rs.getString("latest_payment"),
                        rs.getDouble("total_paid"),
                        latestWeight,
                        rs.getString("join_date")
                ));
            }
        } catch (SQLException e) {
            System.err.println("❌ Members load failed: " + e.getMessage());
        }

        membersTable.setItems(members);
        System.out.println("📊 Loaded " + members.size() + " members");
    }

    private void updateTotalMembersCount() {
        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement("SELECT COUNT(*) FROM members");
             ResultSet rs = pst.executeQuery()) {
            if (rs.next()) {
                totalMembersLabel.setText("Total Members: " + rs.getInt(1));
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

    // ✅ COMPLETE MemberRecord class
    public static class MemberRecord {
        private final SimpleIntegerProperty memberId;
        private final SimpleStringProperty name;
        private final SimpleStringProperty email;
        private final SimpleStringProperty firstLogin;
        private final SimpleStringProperty latestPayment;
        private final SimpleDoubleProperty totalPaid;
        private final SimpleStringProperty latestWeight;
        private final SimpleStringProperty joinDate;

        public MemberRecord(int memberId, String name, String email, String firstLogin,
                            String latestPayment, double totalPaid, String latestWeight, String joinDate) {
            this.memberId = new SimpleIntegerProperty(memberId);
            this.name = new SimpleStringProperty(name);
            this.email = new SimpleStringProperty(email);
            this.firstLogin = new SimpleStringProperty(firstLogin);
            this.latestPayment = new SimpleStringProperty(latestPayment);
            this.totalPaid = new SimpleDoubleProperty(totalPaid);
            this.latestWeight = new SimpleStringProperty(latestWeight);
            this.joinDate = new SimpleStringProperty(joinDate);
        }

        // ✅ Property getters - EXACT names for PropertyValueFactory
        public SimpleIntegerProperty memberIdProperty() { return memberId; }
        public SimpleStringProperty nameProperty() { return name; }
        public SimpleStringProperty emailProperty() { return email; }
        public SimpleStringProperty firstLoginProperty() { return firstLogin; }
        public SimpleStringProperty latestPaymentProperty() { return latestPayment; }
        public SimpleDoubleProperty totalPaidProperty() { return totalPaid; }
        public SimpleStringProperty latestWeightProperty() { return latestWeight; }
        public SimpleStringProperty joinDateProperty() { return joinDate; }
    }
}
