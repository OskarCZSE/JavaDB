package com.example.javadb;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

public class HelloApplication extends Application {
    String url = "jdbc:mysql://localhost:3306/school3tp";
    String username = "root";
    String password = "";

    @Override
    public void start(Stage stage) throws IOException {
        GridPane gridPane = new GridPane();
        Button readBtn = new Button("Odczytaj baze");
        Button addBtn = new Button("Dodaj do bazy");
        Button updateBtn = new Button("Modyfikuj bazę");
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        Connection connection = null;

        EventHandler<ActionEvent> ReadEvent = _ -> {
            ReadDB(connection);
        };
        EventHandler<ActionEvent> AddEvent = _ -> {
            InsertDB("Oskar", 5, "3TP");
        };

        EventHandler<ActionEvent> UpdateEvent = _ -> {
            updateDB(2, "Oskar", 3, "2TP");
        };

        readBtn.setOnAction(ReadEvent);
        addBtn.setOnAction(AddEvent);
        updateBtn.setOnAction(UpdateEvent);

        gridPane.add(readBtn,1,1);
        gridPane.add(addBtn,1,2);
        gridPane.add(updateBtn,1,3);

        Scene scene = new Scene(gridPane, 400, 400);
        stage.setTitle("JavaDB");
        stage.setScene(scene);
        stage.show();



    }

    public static void main(String[] args) {
        launch();
    }
    public void ReadDB(Connection connection){

        try{
            connection = DriverManager.getConnection(url, username, password);

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM students");

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                int age = resultSet.getInt("age");
                String grade = resultSet.getString("grade");

                System.out.println("ID: " + id);
                System.out.println("Name: " + name);
                System.out.println("Surname: " + age);
                System.out.println("Position: " + grade);
                System.out.println("---------");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            if(connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public void InsertDB(String name, int age, String grade) {
            String sql = "INSERT INTO students (name, age, grade) VALUES (?, ?, ?)";

            try (Connection connection = DriverManager.getConnection(url, username, password);
                 PreparedStatement statement = connection.prepareStatement(sql)) {

                statement.setString(1, name);
                statement.setInt(2, age);
                statement.setString(3, grade);

                int rowsAffected = statement.executeUpdate();

                System.out.println("Added: " + rowsAffected);
            } catch (SQLException e) {
                e.printStackTrace();
            }
    }
    public void updateDB(int id, String name, int age, String grade){
        String sql = "UPDATE students SET" +
                "name = ?," +
                "age = ?" +
                "grade = ?" +
                "WHERE id = ?";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, name);
            statement.setInt(2, age);
            statement.setString(3, grade);
            statement.setInt(4, id);

            System.out.println("Modified");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
