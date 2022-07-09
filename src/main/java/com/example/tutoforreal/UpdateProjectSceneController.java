package com.example.tutoforreal;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.sqlite.SQLiteCommitListener;

import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class UpdateProjectSceneController implements Initializable {
    @FXML
    private Label updateProjectErrorMessage;

    @FXML
    private TextField updateProjectTitle;

    @FXML
    private TextArea updateProjectDescription;

    @FXML
    private Label pageTitle;

    @FXML
    private Label projectToUpdateTitle;

    @Override
    public void initialize(URL location, ResourceBundle ressources) {
        /*pageTitle.setText(Data.project.getTitle());*/
        System.out.println("coucou");
        if(Data.project.getTitle() != null)
            projectToUpdateTitle.setText(Data.project.getTitle());
        if(Data.project.getTitle() != null)
            updateProjectTitle.setText(Data.project.getTitle());
        if(Data.project.getDescription() != null)
            updateProjectDescription.setText(Data.project.getDescription());
    }

    public void askForUpdate(ActionEvent event) throws SQLException {
        Connection connectDB = DatabaseConnection.getInstance().getDatabaseLink();
        ProjectQueries projectQueries = new ProjectQueries(DatabaseConnection.getInstance());
        String title = updateProjectTitle.getText();
        String description = updateProjectDescription.getText();
        try {
            int projectId = Data.project.getId();
            if (Data.project.getDescription() != description & description != "")
                projectQueries.updateProjectDescription(projectId, description);
            if (Data.project.getTitle() != title & title != ""){
                projectQueries.updateProjecttitle(projectId, title);
                projectToUpdateTitle.setText(title);
            }

            updateProjectErrorMessage.setText("Projet modifié");
            updateProjectErrorMessage.setTextFill(Color.GREEN);

        }
        catch (SQLException e){
            updateProjectErrorMessage.setText("Veuillez renseigner un ID valide");
            updateProjectErrorMessage.setTextFill(Color.RED);
            System.out.println(e);
        }
    }

    public void cancelWindow(ActionEvent event) {
        ((Stage)(((Button)event.getSource()).getScene().getWindow())).close();
    }

}
