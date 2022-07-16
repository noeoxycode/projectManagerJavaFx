package com.example.tutoforreal.controller;

import com.example.tutoforreal.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.sqlite.SQLiteCommitListener;

import java.io.*;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ProjectDetailController implements Initializable {

    @FXML
    private TableView<Task> taskTableView;

    @FXML
    private TableColumn<Task, String> taskColumnTitle;

    @FXML
    private TableColumn<Task, String> taskColumnStatus;

    @FXML
    private TableColumn<Task, String> taskColumnAssigned;

    @FXML
    private Label logMessageTask;

    @Override
    public void initialize(URL location, ResourceBundle ressources) {
        try {
            ProjectDetailController.this.getAllTasksController();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            DatabaseConnection.getInstance().getDatabaseLink().addCommitListener(new SQLiteCommitListener() {
                @Override
                public void onCommit() {
                    try {
                        ProjectDetailController.this.getAllTasksController();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                @Override
                public void onRollback() {

                }
            });
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getAllTasksController () throws IOException {
        String logs = "ProjectDetailController : getAllTasksController : ";
        TaskQueries taskQueries = new TaskQueries(DatabaseConnection.getInstance());
        ObservableList<Task>taskList = FXCollections.observableArrayList();
        try {
            ArrayList<Task>queryOutput = taskQueries.getAlltask();
            int size = queryOutput.size();
            for (int cpt = 0; cpt < size; cpt++) {
                Task p = queryOutput.get(cpt);
                taskList.add(p);
            }
            taskColumnTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
            taskColumnStatus.setCellValueFactory(new PropertyValueFactory<>("taskStatus"));
            taskColumnAssigned.setCellValueFactory(new PropertyValueFactory<>("assignedTo"));
            taskTableView.setItems(taskList);
            logs = logs + "success";
            LogWriter.writeLogs(logs);
        } catch (SQLException e) {
            e.printStackTrace();
            logs = logs + "failed : " + e;
            LogWriter.writeLogs(logs);
        }
    }

    public void deleteTask() throws IOException {
        String logs = "ProjectDetailController : deleteTask : ";
        if(taskTableView.getSelectionModel().getSelectedItem() != null){
            Data.task = taskTableView.getSelectionModel().getSelectedItem();
            TaskQueries taskQueries = new TaskQueries(DatabaseConnection.getInstance());
            try {
                taskQueries.deleteTask();
                logMessageTask.setText("Tâche suprimée");
                logMessageTask.setTextFill(Color.GREEN);
                logs = logs + "success";
                LogWriter.writeLogs(logs);
            }
            catch (SQLException e){
                System.out.println(e);
                logs = logs + "failed : " + e;
            }
        }
        else {
            logMessageTask.setText("Veuillez séléctionner une tâche pour la modifier");
            logMessageTask.setTextFill(Color.RED);
        }
    }

    @FXML
    private void updateTaskWindow(ActionEvent event) throws IOException {
        if(taskTableView.getSelectionModel().getSelectedItem() != null){
            Data.task = taskTableView.getSelectionModel().getSelectedItem();
            System.out.println(Data.task);
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("updateTask.fxml"));
            Scene updateScene = new Scene(fxmlLoader.load(), 450, 550);
            Stage updateWindow = new Stage();
            updateWindow.setScene(updateScene);
            File file = new File("imgLink.txt");
            BufferedReader br = new BufferedReader(new FileReader(file));
            String logoLink = br.readLine();
            updateWindow.getIcons().add(new Image(logoLink));
            updateWindow.show();
        }
        else {
            logMessageTask.setText("Veuillez séléctionner une tâche pour la modifier");
            logMessageTask.setTextFill(Color.RED);
        }

    }

    public void createTaskWindow() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("createTask.fxml"));
        Scene updateScene = new Scene(fxmlLoader.load(), 450, 500);
        Stage updateWindow = new Stage();
        updateWindow.setScene(updateScene);
        File file = new File("imgLink.txt");
        BufferedReader br = new BufferedReader(new FileReader(file));
        String logoLink = br.readLine();
        updateWindow.getIcons().add(new Image(logoLink));
        updateWindow.show();
    }

    public void cancelWindow(ActionEvent event) {
        ((Stage)(((Button)event.getSource()).getScene().getWindow())).close();
    }
}