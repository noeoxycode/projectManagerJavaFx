package com.example.tutoforreal;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.sqlite.SQLiteCommitListener;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
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
    private Label projectToUpdateTitle;

    @Override
    public void initialize(URL location, ResourceBundle ressources) {
        /*pageTitle.setText(Data.project.getTitle());*/
        if(Data.project.getTitle() != null)
            projectToUpdateTitle.setText(Data.project.getTitle());
        if(Data.project.getTitle() != null)
            updateProjectTitle.setText(Data.project.getTitle());
        if(Data.project.getDescription() != null)
            updateProjectDescription.setText(Data.project.getDescription());
    }

    public void askForUpdate(ActionEvent event) throws SQLException, IOException {
        String logs = "UpdateProjectSceneController : ";
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
            if(Data.project.getTitle() == title && Data.project.getDescription() == description){
                updateProjectErrorMessage.setText("Rien ?? modifier");
                updateProjectErrorMessage.setTextFill(Color.YELLOW);
                System.out.println("on rentre dans le prog");
            }
            /*else {
                updateProjectErrorMessage.setText("Projet modifi??");
                updateProjectErrorMessage.setTextFill(Color.GREEN);
            }*/
            logs = logs + "success";
            LogWriter.writeLogs(logs);
        }
        catch (SQLException e){
            System.out.println(e);
            logs = logs + "failed : " + e;
            LogWriter.writeLogs(logs);
        }
    }

    public void cancelWindow(ActionEvent event) {
        ((Stage)(((Button)event.getSource()).getScene().getWindow())).close();
    }

    @FXML
    private void openProjectDetails () throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("projectDetail.fxml"));
        Scene updateScene = new Scene(fxmlLoader.load(), 812, 400);
        Stage updateWindow = new Stage();
        updateWindow.setScene(updateScene);
        File file = new File("imgLink.txt");
        BufferedReader br = new BufferedReader(new FileReader(file));
        String logoLink = br.readLine();
        updateWindow.getIcons().add(new Image(logoLink));
        updateWindow.show();
    }
}
