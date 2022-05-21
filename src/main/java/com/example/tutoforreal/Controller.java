package com.example.tutoforreal;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import javax.xml.transform.Result;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML
    private ListView<String> projectListView;

    @FXML
    private Label pageLabel;


    @Override
    public void initialize(URL location, ResourceBundle ressources) {
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getDbConnection();
        String connectQuerry = "SELECT * FROM task";

        try {
            Statement statement = connectDB.createStatement();
            ResultSet queryOutput = statement.executeQuery(connectQuerry);
            while (queryOutput.next()) {
                String name = queryOutput.getString("title");
                String deadline = queryOutput.getString("deadline");
                String total = name + " " + deadline;
                pageLabel.setText("Liste de vos projets : ");
                projectListView.getItems().add(total);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}