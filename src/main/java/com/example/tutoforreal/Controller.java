package com.example.tutoforreal;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import javax.xml.transform.Result;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML
    private TableView<Project> projectTableView;

    @FXML
    private Label pageLabel;

    @FXML
    private TextField newProjectTitle;

    @FXML
    private TextArea newProjectDescription;

    @FXML
    private TableColumn<Project, String> projetColumnId;

    @FXML
    private TableColumn<Project, String> projetColumnTitle;

    @FXML
    private TableColumn<Project, String> projetColumnDescription;

    ObservableList<Project>projectList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle ressources) {
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getDbConnection();
        ProjectQueries projectQueries = new ProjectQueries(connectNow);
        getAllProjectsController(projectQueries);
    }

    public void getAllProjectsController (ProjectQueries projectQueries){
            ObservableList<Project>projectList = FXCollections.observableArrayList();
            pageLabel.setText("Liste de vos projets : ");
            try {
                ArrayList<Project>queryOutput = projectQueries.getAllProject();
                int size = queryOutput.size();
                for (int cpt = 0; cpt < size; cpt++) {
                    Project p = queryOutput.get(cpt);
                    projectList.add(p);

                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            projetColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
            projetColumnTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
            projetColumnDescription.setCellValueFactory(new PropertyValueFactory<>("description"));

            projectTableView.setItems(projectList);
    }

    public void createProject(){
        String title = newProjectTitle.getText();
        String description = newProjectDescription.getText();
        Project p = new Project(title, description);
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getDbConnection();
        ProjectQueries projectQueries = new ProjectQueries(connectNow);
        try {
            projectQueries.createProject(p);
            projectTableView.getColumns().removeAll();
            getAllProjectsController(projectQueries);
        }
        catch (SQLException e){
            System.out.println(e);
        }
    }
}