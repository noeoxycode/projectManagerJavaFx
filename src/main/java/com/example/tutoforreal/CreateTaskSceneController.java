package com.example.tutoforreal;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

import static com.example.tutoforreal.TaskStatus.*;
import static com.example.tutoforreal.TaskStatus.Done;

public class CreateTaskSceneController implements Initializable {
    @FXML
    private Label updateTaskErrorMessage;

    @FXML
    private TextField updateTaskTitle;

    @FXML
    private TextArea updateTaskDescription;

    @FXML
    private DatePicker updateTaskDeadline;

    @FXML
    private TextField updateTaskDuration;

    @FXML
    private TextField updateTaskAuthor;

    @FXML
    private TextField updateTaskAssignedTo;

    @FXML
    private ComboBox taskStatusCombobox;

    @Override
    public void initialize(URL location, ResourceBundle ressources) {
        ObservableList<TaskStatus> status = FXCollections.observableArrayList(New, In_progress, Testing, Done);
        taskStatusCombobox.setItems(status);
    }

    public void askForTaskCreation(ActionEvent event) throws SQLException {
        TaskQueries taskQueries = new TaskQueries(DatabaseConnection.getInstance());
        String title = null;
        TaskStatus taskStatus = null;
        String description = null;
        LocalDate deadline = null;
        int taskDuration = 0;
        String author = null;
        String asignedTo = null;
        if (updateTaskTitle.getText() != null)
            title = updateTaskTitle.getText();
        if (taskStatusCombobox.getValue() != null)
            taskStatus = (TaskStatus) taskStatusCombobox.getValue();
        if ( updateTaskDescription.getText() != null)
            description =  updateTaskDescription.getText();
        if (updateTaskDeadline.getValue() != null)
            deadline = updateTaskDeadline.getValue();
        if (Integer.parseInt(updateTaskDuration.getText()) != 0)
            taskDuration = Integer.parseInt(updateTaskDuration.getText());
        if (updateTaskAuthor.getText() != null)
            author = updateTaskAuthor.getText();
        if (updateTaskAssignedTo.getText() != null)
            asignedTo = updateTaskAssignedTo.getText();
        int projectId = Data.project.getId();
        Task task = new Task(title, taskStatus, description, projectId, deadline, taskDuration, author, asignedTo);
        System.out.println("task id" + task);
        try {
            taskQueries.createTask(task);
            updateTaskErrorMessage.setText("Tâche crée");
            updateTaskErrorMessage.setTextFill(Color.GREEN);
        }
        catch (SQLException e){
            updateTaskErrorMessage.setText("Veuillez renseigner un ID valide");
            updateTaskErrorMessage.setTextFill(Color.RED);
            System.out.println(e);
        }
    }

    public void cancelWindow(ActionEvent event) {
        ((Stage)(((Button)event.getSource()).getScene().getWindow())).close();
    }
}
