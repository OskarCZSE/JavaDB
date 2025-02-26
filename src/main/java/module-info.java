module com.example.javadb {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.javadb to javafx.fxml;
    exports com.example.javadb;
}