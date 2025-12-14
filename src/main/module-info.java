module org.example.gymmanagement {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.gymmanagement to javafx.fxml;
    exports org.example.gymmanagement;
}