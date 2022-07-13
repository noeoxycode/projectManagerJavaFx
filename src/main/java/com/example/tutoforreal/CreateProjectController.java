package com.example.tutoforreal;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class CreateProjectController implements Initializable {
    @FXML
    private TextField newProjectTitle;

    @FXML
    private TextArea newProjectDescription;


    @FXML
    private Label createProjectErrorMessage;

    @Override
    public void initialize(URL location, ResourceBundle ressources) {
    }


    public void createProject(){
        String title = newProjectTitle.getText();
        String description = newProjectDescription.getText();
        if(title != "" && description != ""){
            Project p = new Project(title, description);
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

    public void cancelWindow(ActionEvent event) {
        ((Stage)(((Button)event.getSource()).getScene().getWindow())).close();
    }
}
