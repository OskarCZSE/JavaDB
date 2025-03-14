package com.example.javadb2;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

public class HelloApplication extends Application {
    String url = "jdbc:mysql://localhost:3306/school";
    String username = "root";
    String password = "";

    GridPane gridPane = new GridPane();
    GridPane gridPane2 = new GridPane();
    GridPane gridPane3 = new GridPane();
    GridPane gridPane4 = new GridPane();
    GridPane gridPane5 = new GridPane();
    GridPane gridPane6 = new GridPane();

    TextField idField = new TextField();
    TextField dbField = new TextField();
    TextField unField = new TextField();
    TextField pswdField = new TextField();
    TextField nameField = new TextField();
    TextField ageField = new TextField();
    TextField gradeField = new TextField();

    @Override
    public void start(Stage stage) {

        Button readBtn = new Button("Odczytaj baze");
        Button addBtn = new Button("Dodaj do bazy");
        Button tblBtn = new Button("Dodaj tabelę");
        Button updateBtn = new Button("Modyfikuj bazę");
        Button deleteBtn = new Button("Usuń Rekord");
        Button ConnectBtn = new Button("Połącz");

        Text db = new Text("Baza:");
        Text un = new Text("Użytkownik:");
        Text ps = new Text("Hasło:");
        Text idtxt = new Text("ID:");
        Text nametxt = new Text("Imię:");
        Text agetxt = new Text("Wiek:");
        Text gradetxt = new Text("Klasa:");

        Button newCollumnBtn = new Button("+");
        Button delCollumnBtn = new Button("-");
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        Connection connection = null;

        EventHandler<ActionEvent> ReadEvent = _ -> {
            int j = 0;
            for(var i : ReadDB(connection)){
                gridPane6.add(new Text(" ID: "),0,j);
                gridPane6.add(new Text(i.get("id")),1,j);
                gridPane6.add(new Text(" Imie: "),2,j);
                gridPane6.add(new Text(i.get("name")),3,j);
                gridPane6.add(new Text(" Wiek: "),4,j);
                gridPane6.add(new Text(i.get("age")),5,j);
                gridPane6.add(new Text(" Klasa: "),6,j);
                gridPane6.add(new Text(i.get("grade")),7,j);
                j++;
            }
        };
        EventHandler<ActionEvent> ConnectEvent = _ -> {
            url = "jdbc:mysql://localhost:3306/" + dbField.getText();
            username = unField.getText();
            password = pswdField.getText();
            SwitchPanels(true);
        };
        EventHandler<ActionEvent> AddEvent = _ -> {
            HashMap<String, String> valMap = getValues();

            InsertDB(valMap.get("name"), Integer.parseInt(valMap.get("age")), valMap.get("grade"));
        };

        EventHandler<ActionEvent> UpdateEvent = _ -> {
            HashMap<String, String> valMap = getValues();

            updateDB(Integer.parseInt(valMap.get("id")), valMap.get("name"), Integer.parseInt(valMap.get("age")), valMap.get("grade"));
        };
        EventHandler<ActionEvent> DeleteEvent = _ -> {
            HashMap<String, String> valMap = getValues();

            deleteDB(Integer.parseInt(valMap.get("id")));
        };
        EventHandler<ActionEvent> CreateColumn = _ -> {
            //txtFields.add(new TextField());
            //gridPane5.add(txtFields.get(colAmount[0]),0, colAmount[0] + 1);
            GridPane qPane = new GridPane();
            qPane.add(new TextField(),0,0);
            qPane.add(new TextField(),1,0);
            qPane.add(new TextField(),2,0);
            qPane.add(new CheckBox(), 3, 0);
            qPane.add(new CheckBox(), 4, 0);
            gridPane5.add(qPane,0, gridPane5.getChildren().size());
        };
        EventHandler<ActionEvent> DeleteColumn = _ -> {
            gridPane5.getChildren().removeLast();
            //txtFields.remove(colAmount[0] - 1);
                for(var b : gridPane5.getChildren()){
                    Parent parentNode = (Parent) b;
                    for(var a : parentNode.getChildrenUnmodifiable()){
                        if(a instanceof TextField) {
                             System.out.println("Something: " + ((TextField) a).getText());
                        }
                    }
                }
        };

        readBtn.setOnAction(ReadEvent);
        addBtn.setOnAction(AddEvent);
        updateBtn.setOnAction(UpdateEvent);
        deleteBtn.setOnAction(DeleteEvent);
        newCollumnBtn.setOnAction(CreateColumn);
        delCollumnBtn.setOnAction(DeleteColumn);
        ConnectBtn.setOnAction(ConnectEvent);

        gridPane.add(gridPane2, 0, 0);
        gridPane.add(gridPane3, 0, 1);
        gridPane.add(gridPane4, 0, 2);
        gridPane.add(gridPane5, 0, 3);
        gridPane.add(gridPane6, 1, 3);

        setupLayout(gridPane2);
        gridPane2.add(db, 0, 0);
        gridPane2.add(un, 0, 1);
        gridPane2.add(ps, 0, 2);
        gridPane2.add(dbField,1 ,0); dbField.setText("school");
        gridPane2.add(unField,1 ,1); unField.setText("root");
        gridPane2.add(pswdField,1 ,2);
        gridPane2.add(ConnectBtn, 2, 1);

        setupLayout(gridPane3);
        gridPane3.add(idtxt, 0,0);
        gridPane3.add(nametxt, 0,1);
        gridPane3.add(agetxt, 0,2);
        gridPane3.add(gradetxt, 0,3);
        gridPane3.add(idField, 1, 0);
        gridPane3.add(nameField, 1, 1);
        gridPane3.add(ageField, 1, 2);
        gridPane3.add(gradeField, 1, 3);

        setupLayout(gridPane4);
        gridPane4.add(addBtn, 0, 0);
        gridPane4.add(updateBtn, 1, 0);
        gridPane4.add(deleteBtn, 2, 0);
        gridPane4.add(newCollumnBtn, 0, 1);
        gridPane4.add(delCollumnBtn, 1, 1);
        gridPane4.add(readBtn, 2, 1);
        gridPane4.add(tblBtn, 3, 1);

        setupLayout(gridPane5);


        SwitchPanels(false);
        Scene scene = new Scene(gridPane, 800, 800);
        stage.setTitle("JavaDB");
        stage.setScene(scene);
        stage.show();

    }

    public void SwitchPanels(boolean val){
        gridPane3.setVisible(val);
        gridPane4.setVisible(val);
        gridPane5.setVisible(val);
        gridPane6.setVisible(val);
    }

    public static void main(String[] args) {
        launch();
    }

    public void setupLayout(GridPane pane){
        pane.setPadding(new Insets(10, 0, 0, 10));
        pane.setVgap(5);
        pane.setHgap(10);
    }

    public ArrayList<HashMap<String, String>> ReadDB(Connection connection){
        ArrayList<HashMap<String,String >> returnList = new ArrayList<>();
        try{
            connection = DriverManager.getConnection(url, username, password);

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM students");

            while (resultSet.next()) {
                HashMap<String, String> returnHash = new HashMap<>();
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                int age = resultSet.getInt("age");
                String grade = resultSet.getString("grade");

                returnHash.put("id", String.valueOf(id));
                returnHash.put("name", name);
                returnHash.put("age", String.valueOf(age));
                returnHash.put("grade", grade);
                returnList.add(returnHash);
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
        return returnList;
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

    public void CreateTable(){

    }
    public HashMap<String, String> getValues(){
        HashMap<String, String> returnHash = new HashMap<>();

        returnHash.put("id", idField.getText());
        returnHash.put("name", nameField.getText());
        returnHash.put("age", ageField.getText());
        returnHash.put("grade", gradeField.getText());

        return returnHash;
    }
}