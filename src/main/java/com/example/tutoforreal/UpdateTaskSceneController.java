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
    private TextField updateTaskAuthor;

    @FXML
    private TextField updateTaskAssignedTo;

    @FXML
    private Label taskToUpdateTitle;

    @FXML
    private ComboBox taskStatusCombobox;

    @Override
    public void initialize(URL location, ResourceBundle ressources) {
        /*pageTitle.setText(Data.task.getTitle());*/
        if(Data.task.getTitle() != null)
        {
            taskToUpdateTitle.setText(Data.task.getTitle());
            updateTaskTitle.setText(Data.task.getTitle());
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
            updateTaskAuthor.setText(Data.task.getAuthor());
        if(Data.task.getAssignedTo() != null)
            updateTaskAssignedTo.setText(Data.task.getAssignedTo());
        ObservableList<TaskStatus> status = FXCollections.observableArrayList(New, In_progress, Testing, Done);
        taskStatusCombobox.setItems(status);
    }

    public void askForUpdate(ActionEvent event) throws SQLException {
        System.out.println("project id" + Data.project.getId());
        System.out.println("data task id" + Data.task.getId());
        TaskQueries taskQueries = new TaskQueries(DatabaseConnection.getInstance());
        String title = updateTaskTitle.getText();
        TaskStatus taskStatus = (TaskStatus) taskStatusCombobox.getValue();
        String description = updateTaskDescription.getText();
        LocalDate publishedDate = updateTaskPublishedDate.getValue();
        int projectId = Data.task.getProjectId();
        LocalDate deadline = updateTaskDeadline.getValue();
        int taskDuration = Integer.parseInt(updateTaskDuration.getText());
        String author = updateTaskAuthor.getText();
        String asignedTo = updateTaskAssignedTo.getText();
        Task task = Data.task;
        if (Data.task.getTitle() != title & title != ""){
            task.setTitle(title);
            taskToUpdateTitle.setText(title);
        }
        if (Data.task.getTaskStatus() != taskStatus & taskStatus != null)
            task.setTaskStatus(taskStatus);
        if (Data.task.getDescription() != description & description != "")
            task.setDescription(description);
        if (Data.task.getPublishedDate() != publishedDate & publishedDate != null)
            task.setPublishedDate(publishedDate);
        if (Data.task.getDeadLine() != deadline & deadline != null)
            task.setDeadline(deadline);
        if (Data.task.getTaskDuration() != taskDuration & description != null)
            task.setTaskDuration(taskDuration);
        if (Data.task.getAuthor() != author & author != "")
            task.setAuthor(author);
        if (Data.task.getAssignedTo() != asignedTo & asignedTo != "")
            task.setAsignedTo(asignedTo);
        System.out.println("task id" + task);
        try {
            taskQueries.updateTask(task);
            updateTaskErrorMessage.setText("Projet modifié");
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
