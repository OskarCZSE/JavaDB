package com.example.javadb2;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

public class HelloApplication extends Application {
    String url = "jdbc:mysql://localhost:3306/school3tp";
    String username = "root";
    String password = "";

    @Override
    public void start(Stage stage) throws IOException {
        GridPane gridPane = new GridPane();
        GridPane gridPane2 = new GridPane();
        Button readBtn = new Button("Odczytaj baze");
        Button addBtn = new Button("Dodaj do bazy");
        Button updateBtn = new Button("Modyfikuj bazę");
        Button deleteBtn = new Button("Usuń Rekord");
        Button newCollumnBtn = new Button("+");
        Button delCollumnBtn = new Button("-");
        final int[] colAmount = {0};
        ArrayList<TextField> txtFields = new ArrayList<TextField>();
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
        EventHandler<ActionEvent> DeleteEvent = _ -> {
            deleteDB(8);
        };
        EventHandler<ActionEvent> CreateColumn = _ -> {
            txtFields.add(new TextField());
            gridPane2.add(txtFields.get(colAmount[0]),0, colAmount[0]);
            colAmount[0] = colAmount[0] + 1;

            for (int i = 0; i < colAmount[0]; i++) {
                System.out.println(txtFields.get(i).getText());
            }
        };
        EventHandler<ActionEvent> DeleteColumn = _ -> {
            System.out.println(gridPane2.getChildren());
            gridPane2.getChildren().remove(colAmount[0] + 1);
            System.out.println(colAmount[0]);
            txtFields.remove(colAmount[0] - 1);
            colAmount[0]--;
        };

        readBtn.setOnAction(ReadEvent);
        addBtn.setOnAction(AddEvent);
        updateBtn.setOnAction(UpdateEvent);
        deleteBtn.setOnAction(DeleteEvent);
        newCollumnBtn.setOnAction(CreateColumn);
        delCollumnBtn.setOnAction(DeleteColumn);

        gridPane.add(readBtn,1,1);
        gridPane.add(addBtn,1,2);
        gridPane.add(updateBtn,1,3);
        gridPane.add(deleteBtn,1,4);
        gridPane.add(gridPane2, 1, 5);
        gridPane2.add(newCollumnBtn,0,99);
        gridPane2.add(delCollumnBtn,1,99);

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
        String sql = "UPDATE students SET " +
                "name = ?," +
                "age = ?," +
                "grade = ?" +
                " WHERE id = ?;";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, name);
            statement.setInt(2, age);
            statement.setString(3, grade);
            statement.setInt(4, id);
            int rowsAffected = statement.executeUpdate();
            System.out.println("Modified: " + rowsAffected);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void deleteDB(int id){

        String sql = "DELETE FROM students WHERE id = ?;";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            int rowsAffected = statement.executeUpdate();
            System.out.println("Removed: " + rowsAffected);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void CreateDB(){

    }
}