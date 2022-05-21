package com.example.tutoforreal;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import javax.xml.transform.Result;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML
    private TableView<Project> projectTableView;


    @FXML
    private Label pageLabel;


    @Override
    public void initialize(URL location, ResourceBundle ressources) {
        TableColumn id = new TableColumn("ID");
        TableColumn title = new TableColumn("TITLE");
        TableColumn description = new TableColumn("DESCRIPTION");
        projectTableView.getColumns().addAll(id, title, description);
        ObservableList<Project>projectList = FXCollections.observableArrayList();
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getDbConnection();
        String connectQuerry = "SELECT * FROM project";

        try {
            Statement statement = connectDB.createStatement();
            ResultSet queryOutput = statement.executeQuery(connectQuerry);
            while (queryOutput.next()) {
                int idP = queryOutput.getInt("id");
                String titleP = queryOutput.getString("title");
                String descriptionP = queryOutput.getString("description");
                Project newProject = new Project(idP, titleP, descriptionP);
                projectList.add(newProject);
                pageLabel.setText("Liste de vos projets : ");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        id.setCellValueFactory(new PropertyValueFactory<Project, String>("id"));
        title.setCellValueFactory(new PropertyValueFactory<Project, String>("title"));
        description.setCellValueFactory(new PropertyValueFactory<Project, String>("description"));
        projectTableView.setItems(projectList);
    }
}