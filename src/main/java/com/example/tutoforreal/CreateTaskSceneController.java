package com.example.tutoforreal;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

import static com.example.tutoforreal.TaskStatus.*;
import static com.example.tutoforreal.TaskStatus.Done;

public class CreateTaskSceneController implements Initializable {
    @FXML
    private Label LogMessage;

    @FXML
    private TextField updateTaskTitle;

    @FXML
    private TextArea updateTaskDescription;

    @FXML
    private DatePicker updateTaskDeadline;

    @FXML
    private TextField updateTaskDuration;

    @FXML
    private ComboBox updateTaskAuthor;

    @FXML
    private ComboBox updateTaskAssignedTo;

    @FXML
    private ComboBox<TaskStatus> taskStatusCombobox;

    @Override
    public void initialize(URL location, ResourceBundle ressources) {
        ArrayList<String> users = null;
        ArrayList<TaskStatus> status = null;
        TaskQueries taskQueries = null;
        try {
            taskQueries = new TaskQueries(DatabaseConnection.getInstance());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            users = taskQueries.getAllUsers();
            status = taskQueries.getAllTaskStatus();
            taskStatusCombobox.setItems(FXCollections.observableList(status));
            updateTaskAuthor.setItems(FXCollections.observableList(users));
            updateTaskAssignedTo.setItems(FXCollections.observableList(users));

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void askForTaskCreation(ActionEvent event) throws IOException {
        String logs = "CreateTaskSceneController askForTaskCreation : ";
        TaskQueries taskQueries = new TaskQueries(DatabaseConnection.getInstance());
        String title = null;
        TaskStatus taskStatus = null;
        String description = null;
        LocalDate deadline = null;
        int taskDuration = 0;
        String author = null;
        String asignedTo = null;
        if (!updateTaskTitle.getText().isBlank())
            title = updateTaskTitle.getText();
        else {
            LogMessage.setText("Veuillez renseigner le titre de la tâche.");
            LogMessage.setTextFill(Color.RED);
            return;
        }
        if (taskStatusCombobox.getValue() != null)
            taskStatus = taskStatusCombobox.getValue();
        else {
            LogMessage.setText("Veuillez renseigner le status de la tâche.");
            LogMessage.setTextFill(Color.RED);
            return;
        }
        if (!updateTaskDescription.getText().isBlank())
            description =  updateTaskDescription.getText();
        else{
            LogMessage.setText("Veuillez renseigner la description de la tâche.");
            LogMessage.setTextFill(Color.RED);
            return;
        }
        if (updateTaskDeadline.getValue() != null){
            deadline = updateTaskDeadline.getValue();
        }
        else {
            LogMessage.setText("Veuillez renseigner une deadline valide.");
            LogMessage.setTextFill(Color.RED);
            return;
        }
        if (!updateTaskDuration.getText().isBlank())
        {
            try {
                int tmp = Integer.parseInt(updateTaskDuration.getText());
                taskDuration = tmp;
            }
            catch(NumberFormatException e){
                LogMessage.setText("Veuillez rentrer une durée valide.");
                LogMessage.setTextFill(Color.RED);
                return;
            }
        }
        else {
            LogMessage.setText("Veuillez renseignerla durée de la tâche.");
            LogMessage.setTextFill(Color.RED);
            return;
        }
        if (updateTaskAuthor.getValue() != null)
            author = updateTaskAuthor.getValue().toString();
        else{
            LogMessage.setText("Veuillez renseigner l'auteur de la tâche.");
            LogMessage.setTextFill(Color.RED);
            return;
        }
        if (updateTaskAssignedTo.getValue() != null)
            asignedTo = updateTaskAssignedTo.getValue().toString();
        else{
            LogMessage.setText("Veuillez renseigner la personne assignée à la tâche.");
            LogMessage.setTextFill(Color.RED);
            return;
        }
        int projectId = Data.project.getId();


        Task task = new Task(title, taskStatus, description, projectId, deadline, taskDuration, author, asignedTo);
        System.out.println("task id" + task);
        try {
            taskQueries.createTask(task);
            LogMessage.setText("Tâche crée");
            LogMessage.setTextFill(Color.GREEN);
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
}
