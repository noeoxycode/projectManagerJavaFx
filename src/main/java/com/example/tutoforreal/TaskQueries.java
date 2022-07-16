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
import java.sql.*;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.Scanner;

/**
 * Cette classe met à disposition les méthodes liées à la table task
 */
public class TaskQueries {
    private final DatabaseConnection connection;
    public TaskQueries(DatabaseConnection connection) {
        this.connection = connection;
    }

    public Task getTaskById(int id, int idProject) throws SQLException {
        Scanner scan = new Scanner(System.in);
        String sql = "SELECT * FROM task WHERE id == ? AND projectId == ?";
        PreparedStatement pstmt = this.connection.getDatabaseLink().prepareStatement(sql);
        pstmt.setInt(1, id);
        pstmt.setInt(2, idProject);
        ResultSet result = pstmt.executeQuery();
        String title = result.getString("title");
        TaskStatus taskStatus = TaskStatus.valueOf(result.getString("taskStatus"));
        String description = result.getString("description");
        int projectId = result.getInt("projectId");
        LocalDate deadline = result.getDate("deadline").toLocalDate();
        int taskDuration = result.getInt("taskDuration");
        String author = result.getString("author");
        String assignedTo = result.getString("assignedTo");
        return new Task(title, taskStatus, description, projectId, deadline, taskDuration, author, assignedTo);
    }

    public ArrayList<Task> getAlltask() throws SQLException {
        int idProject = Data.project.getId();
        ArrayList<Task> taskList = new ArrayList<Task>();
        String sql = "SELECT * FROM task WHERE projectId == ?";
        PreparedStatement pstmt = this.connection.getDatabaseLink().prepareStatement(sql);
        pstmt.setInt(1, idProject);
        ResultSet result = pstmt.executeQuery();
        while (result.next()){
            int taskId = result.getInt("id");
            String title = result.getString("title");
            TaskStatus taskStatus = TaskStatus.valueOf(result.getString("taskStatus"));
            String description = result.getString("description");
            LocalDate publishedDate = result.getDate("publishedDate").toLocalDate();
            int projectId = result.getInt("projectId");
            LocalDate deadline = result.getDate("deadline").toLocalDate();
            int taskDuration = result.getInt("taskDuration");
            String author = result.getString("author");
            String assignedTo = result.getString("assignedTo");
            Task t = new Task(taskId, title, taskStatus, description, publishedDate, projectId, deadline, taskDuration, author, assignedTo);
            taskList.add(t);
        }
        return taskList;
    }

    public ArrayList<String> getAllUsers() throws SQLException {
        ArrayList<String> users = new ArrayList<String>();
        String sql = "SELECT name FROM user";
        PreparedStatement pstmt = this.connection.getDatabaseLink().prepareStatement(sql);
        ResultSet result = pstmt.executeQuery();
        while (result.next())
            users.add(result.getString("name"));
        return users;
    }

    public ArrayList<TaskStatus> getAllTaskStatus() throws SQLException {
        ArrayList<TaskStatus> status = new ArrayList<TaskStatus>();
        String sql = "SELECT label FROM taskStatus";
        PreparedStatement pstmt = this.connection.getDatabaseLink().prepareStatement(sql);
        ResultSet result = pstmt.executeQuery();
        while (result.next())
            status.add(TaskStatus.valueOf(result.getString("label")));
        return status;
    }

    public void createTask(Task newTask) throws SQLException {
        String sql = "insert into task values (null, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement pstmt = this.connection.getDatabaseLink().prepareStatement(sql);
        pstmt.setString(1, newTask.getTitle());
        pstmt.setString(2, newTask.getTaskStatus().toString());
        pstmt.setString(3, newTask.getDescription());
        Date publishedDate = Date.from(newTask.getPublishedDate().atStartOfDay(ZoneId.systemDefault()).toInstant());
        long newPublishedDate = publishedDate.getTime();
        pstmt.setLong(4, newPublishedDate);
        pstmt.setInt(5, newTask.getProjectId());
        Date deadline = Date.from(newTask.getDeadLine().atStartOfDay(ZoneId.systemDefault()).toInstant());
        long newDeadline = publishedDate.getTime();
        pstmt.setLong(6, newDeadline);
        pstmt.setInt(7, newTask.getTaskDuration());
        pstmt.setString(8, newTask.getAuthor());
        pstmt.setString(9, newTask.getAssignedTo());
        pstmt.executeUpdate();
    }

    public void updateTask(Task task) throws SQLException {
        String sql = "update task set title = ?, taskStatus = ?, description = ?, publishedDate = ?, projectId = ?, deadline = ?, taskDuration = ?, author = ?, assignedTo = ? where id == ? AND projectId == ?";
        PreparedStatement pstmt = this.connection.getDatabaseLink().prepareStatement(sql);
        pstmt.setString(1, task.getTitle());
        pstmt.setString(2, task.getTaskStatus().toString());
        pstmt.setString(3, task.getDescription());
        Date publishedDate = Date.from(task.getPublishedDate().atStartOfDay(ZoneId.systemDefault()).toInstant());
        long newPublishedDate = publishedDate.getTime();
        pstmt.setLong(4, newPublishedDate);
        pstmt.setInt(5, task.getProjectId());
        Date deadline = Date.from(task.getDeadLine().atStartOfDay(ZoneId.systemDefault()).toInstant());
        long newDeadline = deadline.getTime();
        pstmt.setLong(6, newDeadline);
        pstmt.setInt(7, task.getTaskDuration());
        pstmt.setString(8, task.getAuthor());
        pstmt.setString(9, task.getAssignedTo());
        pstmt.setInt(10, task.getId());
        pstmt.setInt(11, task.getProjectId());
        pstmt.executeUpdate();
    }

    public void updateTasktitle(String newTitle) throws SQLException {
        String sql = "update task set title = ? where id == ? AND projectId == ?";
        PreparedStatement pstmt = this.connection.getDatabaseLink().prepareStatement(sql);
        pstmt.setString(1, newTitle);
        pstmt.setInt(2, Data.task.getId());
        pstmt.setInt(3, Data.task.getProjectId());
        pstmt.executeUpdate();
    }

    public void updateTaskStatus(int id, String taskStatus, int idProject) throws SQLException {
        String sql = "update task set taskStatus = ? where id == ? AND projectId == ?";
        PreparedStatement pstmt = this.connection.getDatabaseLink().prepareStatement(sql);
        pstmt.setString(1, taskStatus);
        pstmt.setInt(2, id);
        pstmt.setInt(3, idProject);
        pstmt.executeUpdate();
    }

    public void updateTaskDescription(String description) throws SQLException {
        String sql = "update task set description = ? where id == ? AND projectId == ?";
        PreparedStatement pstmt = this.connection.getDatabaseLink().prepareStatement(sql);
        pstmt.setString(1, description);
        pstmt.setInt(2, Data.task.getId());
        pstmt.setInt(3, Data.task.getProjectId());
        pstmt.executeUpdate();
    }

    public void updateTaskProjectId(int id, int projectId, int idProject) throws SQLException {
        String sql = "update task set projectId = ? where id == ? AND projectId == ?";
        PreparedStatement pstmt = this.connection.getDatabaseLink().prepareStatement(sql);
        pstmt.setInt(1, projectId);
        pstmt.setInt(2, id);
        pstmt.setInt(3, idProject);
        pstmt.executeUpdate();
    }

    public void updateTaskDeadline(int id, String deadline, int idProject) throws SQLException {
        String sql = "update task set deadline = ? where id == ? AND projectId == ?";
        PreparedStatement pstmt = this.connection.getDatabaseLink().prepareStatement(sql);
        pstmt.setString(1, deadline);
        pstmt.setInt(2, id);
        pstmt.setInt(3, idProject);
        pstmt.executeUpdate();
    }

    public void updateTaskDuration(int id, int duration, int idProject) throws SQLException {
        String sql = "update task set taskDuration = ? where id == ? AND projectId == ?";
        PreparedStatement pstmt = this.connection.getDatabaseLink().prepareStatement(sql);
        pstmt.setInt(1, duration);
        pstmt.setInt(2, id);
        pstmt.setInt(3, idProject);
        pstmt.executeUpdate();
    }

    public void updateTaskAuthor(int id, String author, int idProject) throws SQLException {
        String sql = "update task set author = ? where id == ? AND projectId == ?";
        PreparedStatement pstmt = this.connection.getDatabaseLink().prepareStatement(sql);
        pstmt.setString(1, author);
        pstmt.setInt(2, id);
        pstmt.setInt(3, idProject);
        pstmt.executeUpdate();
    }

    public void updateTaskAssignedTo(int id, String assignedTo, int idProject) throws SQLException {
        String sql = "update task set assignedTo = ? where id == ? AND projectId == ?";
        PreparedStatement pstmt = this.connection.getDatabaseLink().prepareStatement(sql);
        pstmt.setString(1, assignedTo);
        pstmt.setInt(2, id);
        pstmt.setInt(3, idProject);
        pstmt.executeUpdate();
    }


    public void deleteTask() throws SQLException {
        int id = Data.task.getId();
        String sql = "delete from task where id == ?";
        PreparedStatement pstmt = this.connection.getDatabaseLink().prepareStatement(sql);
        pstmt.setInt(1, id);
        pstmt.executeUpdate();
    }
}