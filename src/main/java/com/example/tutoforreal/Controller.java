package com.example.tutoforreal;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML
    private TableView<Project> projectTableView;

    @FXML
    private Label createProjectErrorMessage;

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

    @FXML
    private TextField idProjectToDelete;

    @FXML
    private Label deleteProjectErrorMessage;

    @FXML
    private Label updateProjectErrorMessage;

    @FXML
    private TextField updateProjectId;

    @FXML
    private TextField updateProjectTitle;

    @FXML
    private TextArea updateProjectDescription;


    ObservableList<Project>projectList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle ressources) {
    }

    public void getAllProjectsController (){
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getDbConnection();
        ProjectQueries projectQueries = new ProjectQueries(connectNow);
        ObservableList<Project>projectList = FXCollections.observableArrayList();
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
        if(title != "" && description != ""){
            Project p = new Project(title, description);
            DatabaseConnection connectNow = new DatabaseConnection();
            Connection connectDB = connectNow.getDbConnection();
            ProjectQueries projectQueries = new ProjectQueries(connectNow);
            try {
                projectQueries.createProject(p);
                projectTableView.getColumns().removeAll();
                getAllProjectsController();
                createProjectErrorMessage.setText("Projet crée");
                createProjectErrorMessage.setTextFill(Color.GREEN);
            }
            catch (SQLException e){
                System.out.println(e);
            }
        }
        else {
            createProjectErrorMessage.setText("Veuillez renseigner tous les champs");
            createProjectErrorMessage.setTextFill(Color.RED);

        }

    }

    public void deleteProject() {
        String tmp = idProjectToDelete.getText();

        if(tmp != ""){
            int id = Integer.parseInt(tmp);
            DatabaseConnection connectNow = new DatabaseConnection();
            Connection connectDB = connectNow.getDbConnection();
            ProjectQueries projectQueries = new ProjectQueries(connectNow);
            try {
                projectQueries.deleteProject(id);
                projectTableView.getColumns().removeAll();
                getAllProjectsController();
                deleteProjectErrorMessage.setText("Projet supprimé");
                deleteProjectErrorMessage.setTextFill(Color.GREEN);
            }
            catch (SQLException e){
                System.out.println(e);
            }
        }
        else {
            deleteProjectErrorMessage.setText("Veuillez renseigner un ID valide :");
            deleteProjectErrorMessage.setTextFill(Color.RED);

        }
    }

    public void updateProjectWindow() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("updateProject.fxml"));
        Scene updateScene = new Scene(fxmlLoader.load(), 448, 316);
        Stage updateWindow = new Stage();
        updateWindow.setScene(updateScene);
        updateWindow.show();
    }
    public void startUpdate(int idP) throws IOException {

    }

    public void cancelUpate() {

    }

    public void askForUpdate() throws SQLException {
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getDbConnection();
        ProjectQueries projectQueries = new ProjectQueries(connectNow);
        String title = updateProjectTitle.getText();
        String description = updateProjectDescription.getText();
        int id = Integer.parseInt(updateProjectId.getText());
        Project p = null;
        try {
            p =projectQueries.getProjectById(id);
            if (p != null){
                if (p.getTitle() != title & title != "")
                    projectQueries.updateProjecttitle(id, title);
                if (p.getDescription() != description & description != "")
                    projectQueries.updateProjectDescription(id, description);
                updateProjectErrorMessage.setText("Projet modifié");
                updateProjectErrorMessage.setTextFill(Color.GREEN);
            }
        }
        catch (SQLException e){
            updateProjectErrorMessage.setText("Veuillez renseigner un ID valide");
            updateProjectErrorMessage.setTextFill(Color.RED);
            System.out.println(e);
        }
    }
}