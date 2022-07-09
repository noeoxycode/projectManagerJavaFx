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
    private AnchorPane mainProjectPage;
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


    ObservableList<Project>projectList = FXCollections.observableArrayList();

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
            Connection connectDB = DatabaseConnection.getInstance().getDatabaseLink();
            ProjectQueries projectQueries = new ProjectQueries(DatabaseConnection.getInstance());
            ObservableList<Project>projectList = FXCollections.observableArrayList();
            try {
                ArrayList<Project>queryOutput = projectQueries.getAllProject();
                int size = queryOutput.size();
                for (int cpt = 0; cpt < size; cpt++) {
                    Project p = queryOutput.get(cpt);
                    projectList.add(p);
                }
                projetColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
                projetColumnTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
                projetColumnDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
                projectTableView.setItems(projectList);
            } catch (SQLException e) {
                e.printStackTrace();
            }
    }

    public void createProject(){
        String title = newProjectTitle.getText();
        String description = newProjectDescription.getText();
        if(title != "" && description != ""){
            Project p = new Project(title, description);
            Connection connectDB = DatabaseConnection.getInstance().getDatabaseLink();
            ProjectQueries projectQueries = new ProjectQueries(DatabaseConnection.getInstance());
            try {
                projectQueries.createProject(p);
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

    @FXML
    private void updateProjectWindow(ActionEvent event) throws IOException {
        Data.project = projectTableView.getSelectionModel().getSelectedItem();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("updateProject.fxml"));
        Scene updateScene = new Scene(fxmlLoader.load(), 450, 300);
        Stage updateWindow = new Stage();
        updateWindow.setScene(updateScene);
        updateWindow.show();
    }

    public void deleteProjectWindow() throws IOException {
        Project p = projectTableView.getSelectionModel().getSelectedItem();
        System.out.println(p.getId());
        int id = p.getId();
        deleteProject(id);
    }

    public void deleteProject(int id) {
        ProjectQueries projectQueries = new ProjectQueries(DatabaseConnection.getInstance());
        try {
            projectQueries.deleteProject(id);
        }
        catch (SQLException e){
            System.out.println(e);
        }
    }

    public void createProjectWindow() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("createProject.fxml"));
        Scene updateScene = new Scene(fxmlLoader.load(), 450, 300);
        Stage updateWindow = new Stage();
        updateWindow.setScene(updateScene);
        updateWindow.show();
    }

    public void cancelWindow(ActionEvent event) {
        ((Stage)(((Button)event.getSource()).getScene().getWindow())).close();
    }
}