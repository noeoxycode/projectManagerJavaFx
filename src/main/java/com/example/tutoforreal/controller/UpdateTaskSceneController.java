package com.example.tutoforreal.controller;

import com.example.tutoforreal.*;
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

public class UpdateTaskSceneController implements Initializable {
    @FXML
    private Label updateTaskErrorMessage;

    @FXML
    private TextField updateTaskTitle;

    @FXML
    private TextArea updateTaskDescription;

    @FXML
    private DatePicker updateTaskPublishedDate;

    @FXML
    private DatePicker updateTaskDeadline;

    @FXML
    private TextField updateTaskDuration;

    @FXML
    private ComboBox updateTaskAuthor;

    @FXML
    private ComboBox updateTaskAssignedTo;

    @FXML
    private Label taskToUpdateTitle;

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
        if(Data.task.getTitle() != null)
        {
            taskToUpdateTitle.setText(Data.task.getTitle());
            updateTaskTitle.setText(Data.task.getTitle());
        }
        if(Data.task.getTaskStatus() != null){
            int i = status.indexOf(Data.task.getTaskStatus());
            taskStatusCombobox.getSelectionModel().select(i);

        }
        if(Data.task.getDescription() != null)
            updateTaskDescription.setText(Data.task.getDescription());
        if(Data.task.getPublishedDate() != null)
            updateTaskPublishedDate.setValue(Data.task.getPublishedDate());
        if(Data.task.getDeadLine() != null)
            updateTaskDeadline.setValue(Data.task.getDeadLine());
        if(Data.task.getTaskDuration() > 0)
            updateTaskDuration.setText(String.valueOf(Data.task.getTaskDuration()));
        if(Data.task.getAuthor() != null)
        {
            int i = users.indexOf(Data.task.getAuthor());
            updateTaskAuthor.getSelectionModel().select(i);
        }
        if(Data.task.getAssignedTo() != null)
        {
            int i = users.indexOf(Data.task.getAssignedTo());
            updateTaskAssignedTo.getSelectionModel().select(i);
        }
    }

    public void askForUpdate(ActionEvent event) throws IOException {
        String logs = "UpdateTaskSceneController : askForUpdate : ";
        int cptModif = 0;
        TaskQueries taskQueries = new TaskQueries(DatabaseConnection.getInstance());
        String title = "";
        if(!updateTaskTitle.getText().isBlank())
            title = updateTaskTitle.getText();
        TaskStatus taskStatus = taskStatusCombobox.getValue();
        String description = "";
        if(!updateTaskDescription.getText().isBlank())
            description = updateTaskDescription.getText();
        LocalDate publishedDate;
        if(updateTaskPublishedDate.getValue() != null)
             publishedDate = updateTaskPublishedDate.getValue();
        else {
            updateTaskErrorMessage.setText("Veuillez renseigner une date de publication valide.");
            updateTaskErrorMessage.setTextFill(Color.RED);
            return;
        }
        int projectId = Data.task.getProjectId();
        LocalDate deadline;
        if(updateTaskDeadline.getValue() != null)
            deadline = updateTaskDeadline.getValue();
        else {
            updateTaskErrorMessage.setText("Veuillez renseigner une deadline valide.");
            updateTaskErrorMessage.setTextFill(Color.RED);
            return;
        }
        int taskDuration = 0;
        if (!updateTaskDuration.getText().isBlank())
        {
            try {
                int tmp = Integer.parseInt(updateTaskDuration.getText());
                taskDuration = tmp;
            }
            catch(NumberFormatException e){
                updateTaskErrorMessage.setText("Veuillez rentrer une durée valide.");
                updateTaskErrorMessage.setTextFill(Color.RED);
                return;
            }
        }
        String author = updateTaskAuthor.getValue().toString();
        String asignedTo = updateTaskAssignedTo.getValue().toString();
        Task task = Data.task;
        if (Data.task.getTitle() != title & title != ""){
            task.setTitle(title);
            taskToUpdateTitle.setText(title);
        }
        if (Data.task.getTaskStatus() != taskStatus & taskStatus != null){
            task.setTaskStatus(taskStatus);
            cptModif++;
        }
        if (Data.task.getDescription() != description & description != ""){
            task.setDescription(description);
            cptModif++;
        }
        if (Data.task.getPublishedDate() != publishedDate & publishedDate != null){
            task.setPublishedDate(publishedDate);
            cptModif++;
        }
        if (Data.task.getDeadLine() != deadline & deadline != null){
            task.setDeadline(deadline);
            cptModif++;
        }
        if (Data.task.getTaskDuration() != taskDuration & taskDuration != 0){
            task.setTaskDuration(taskDuration);
            cptModif++;
        }
        if (Data.task.getAuthor() != author & author != ""){
            task.setAuthor(author);
            cptModif++;
        }
        if (Data.task.getAssignedTo() != asignedTo & asignedTo != ""){
            task.setAsignedTo(asignedTo);
            cptModif++;
        }

        try {
            taskQueries.updateTask(task);
            updateTaskErrorMessage.setText("Champs de la tâche modifiés");
            updateTaskErrorMessage.setTextFill(Color.GREEN);
            logs = logs + "success";
            LogWriter.writeLogs(logs);
        }
        catch (SQLException e){
            updateTaskErrorMessage.setText("Veuillez renseigner un ID valide");
            updateTaskErrorMessage.setTextFill(Color.RED);
            System.out.println(e);
            logs = logs + "failed : " + e;
            LogWriter.writeLogs(logs);
        }
    }

    public void cancelWindow(ActionEvent event) {
        ((Stage)(((Button)event.getSource()).getScene().getWindow())).close();
    }
}
