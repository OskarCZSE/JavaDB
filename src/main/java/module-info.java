module com.example.javadb2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.j;

    opens com.example.javadb2 to javafx.fxml;
    exports com.example.javadb2;
}