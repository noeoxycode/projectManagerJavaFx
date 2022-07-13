package com.example.tutoforreal;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.sqlite.SQLiteCommitListener;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    private TableView<Project> projectTableView;

    @FXML
    private TableColumn<Project, String> projetColumnTitle;

    @FXML
    private TableColumn<Project, String> projetColumnDescription;

    @FXML
    private Label logMessageProject;

    @Override
    public void initialize(URL location, ResourceBundle ressources) {
        DatabaseConnection.getInstance().getDatabaseLink().addCommitListener(new SQLiteCommitListener() {
            @Override
            public void onCommit() {
                Controller.this.getAllProjectsController();
            }
            @Override
            public void onRollback() {

            }
        });
    }

    public void getAllProjectsController (){
            ProjectQueries projectQueries = new ProjectQueries(DatabaseConnection.getInstance());
            ObservableList<Project>projectList = FXCollections.observableArrayList();
            try {
                ArrayList<Project>queryOutput = projectQueries.getAllProject();
                int size = queryOutput.size();
                for (int cpt = 0; cpt < size; cpt++) {
                    Project p = queryOutput.get(cpt);
                    projectList.add(p);
                }
                projetColumnTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
                projetColumnDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
                projectTableView.setItems(projectList);
            } catch (SQLException e) {
                e.printStackTrace();
            }
    }

    public void deleteProject() {
        if(projectTableView.getSelectionModel().getSelectedItem() != null){
            Project p = projectTableView.getSelectionModel().getSelectedItem();
            System.out.println(p.getId());
            int id = p.getId();
            ProjectQueries projectQueries = new ProjectQueries(DatabaseConnection.getInstance());
            try {
                projectQueries.deleteProject(id);
                logMessageProject.setText("Projet suprimé");
                logMessageProject.setTextFill(Color.GREEN);
            }
            catch (SQLException e){
                System.out.println(e);
            }
        }
        else {
            logMessageProject.setText("Veuillez séléctionner un projet pour le supprimer");
            logMessageProject.setTextFill(Color.RED);
        }
    }

    @FXML
    private void updateProjectWindow(ActionEvent event) throws IOException {
        if(projectTableView.getSelectionModel().getSelectedItem() != null){
            Data.project = projectTableView.getSelectionModel().getSelectedItem();
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("updateProject.fxml"));
            Scene updateScene = new Scene(fxmlLoader.load(), 450, 300);
            Stage updateWindow = new Stage();
            updateWindow.setScene(updateScene);
            updateWindow.show();
        }
        else {
            logMessageProject.setText("Veuillez séléctionner un projet pour le modifier");
            logMessageProject.setTextFill(Color.RED);
        }
    }

    public void createProjectWindow() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("createProject.fxml"));
        Scene updateScene = new Scene(fxmlLoader.load(), 450, 300);
        Stage updateWindow = new Stage();
        updateWindow.setScene(updateScene);
        updateWindow.show();
    }
}