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

public class TaskDetailController implements Initializable {

    @FXML
    private TableView<Task> taskTableView;

    @FXML
    private Label createTaskErrorMessage;

    @FXML
    private TextField newTaskTitle;

    @FXML
    private TextArea newTaskDescription;

    @FXML
    private TableColumn<Task, String> projetColumnTitle;

    @FXML
    private TableColumn<Task, String> projetColumnDescription;

    @FXML
    private TextField idTaskToDelete;

    @FXML
    private Label logMessageTask;

    @Override
    public void initialize(URL location, ResourceBundle ressources) {
        DatabaseConnection.getInstance().getDatabaseLink().addCommitListener(new SQLiteCommitListener() {
            @Override
            public void onCommit() {
                TaskDetailController.this.getAllTasksController();
            }
            @Override
            public void onRollback() {

            }
        });
    }

    public void getAllTasksController (){
        Connection connectDB = DatabaseConnection.getInstance().getDatabaseLink();
        TaskQueries taskQueries = new TaskQueries(DatabaseConnection.getInstance());
        ObservableList<Task>taskList = FXCollections.observableArrayList();
        try {
            ArrayList<Task>queryOutput = taskQueries.getAllTask();
            int size = queryOutput.size();
            for (int cpt = 0; cpt < size; cpt++) {
                Task p = queryOutput.get(cpt);
                taskList.add(p);
            }
            projetColumnTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
            projetColumnDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
            taskTableView.setItems(taskList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createTask(){
        String title = newTaskTitle.getText();
        String description = newTaskDescription.getText();
        if(title != "" && description != ""){
            Task p = new Task(title, description);
            Connection connectDB = DatabaseConnection.getInstance().getDatabaseLink();
            TaskQueries taskQueries = new TaskQueries(DatabaseConnection.getInstance());
            try {
                taskQueries.createTask(p);
                createTaskErrorMessage.setText("Projet crée");
                createTaskErrorMessage.setTextFill(Color.GREEN);
            }
            catch (SQLException e){
                System.out.println(e);
            }
        }
        else {
            createTaskErrorMessage.setText("Veuillez renseigner tous les champs");
            createTaskErrorMessage.setTextFill(Color.RED);

        }
    }

    public void deleteTask() {
        if(taskTableView.getSelectionModel().getSelectedItem() != null){
            Task p = taskTableView.getSelectionModel().getSelectedItem();
            System.out.println(p.getId());
            int id = p.getId();
            TaskQueries taskQueries = new TaskQueries(DatabaseConnection.getInstance());
            try {
                taskQueries.deleteTask(id);
                logMessageTask.setText("Projet suprimé");
                logMessageTask.setTextFill(Color.GREEN);
            }
            catch (SQLException e){
                System.out.println(e);
            }
        }
        else {
            logMessageTask.setText("Veuillez séléctionner un projet pour le supprimer");
            logMessageTask.setTextFill(Color.RED);
        }
    }

    @FXML
    private void updateTaskWindow(ActionEvent event) throws IOException {
        if(taskTableView.getSelectionModel().getSelectedItem() != null){
            Data.task = taskTableView.getSelectionModel().getSelectedItem();
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("updateTask.fxml"));
            Scene updateScene = new Scene(fxmlLoader.load(), 450, 300);
            Stage updateWindow = new Stage();
            updateWindow.setScene(updateScene);
            updateWindow.show();
        }
        else {
            logMessageTask.setText("Veuillez séléctionner un projet pour le modifier");
            logMessageTask.setTextFill(Color.RED);
        }

    }

    public void createTaskWindow() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("createTask.fxml"));
        Scene updateScene = new Scene(fxmlLoader.load(), 450, 300);
        Stage updateWindow = new Stage();
        updateWindow.setScene(updateScene);
        updateWindow.show();
    }

    public void cancelWindow(ActionEvent event) {
        ((Stage)(((Button)event.getSource()).getScene().getWindow())).close();
    }
}