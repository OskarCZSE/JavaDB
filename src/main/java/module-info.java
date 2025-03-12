module com.example.javadb2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens com.example.javadb2 to javafx.fxml;
    exports com.example.javadb2;
}