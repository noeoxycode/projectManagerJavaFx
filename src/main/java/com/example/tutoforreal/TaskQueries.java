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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
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
        LocalDate deadLine = result.getDate("deadLine").toLocalDate();
        int taskDuration = result.getInt("taskDuration");
        String author = result.getString("author");
        String assignedTo = result.getString("assignedTo");
        return new Task(title, taskStatus, description, projectId, deadLine, taskDuration, author, assignedTo);
    }

    public ArrayList<Task> getAlltask() throws SQLException {
        int idProject = Data.project.getId();
        ArrayList<Task> taskList = new ArrayList<Task>();
        String sql = "SELECT * FROM task WHERE projectId == ?";
        PreparedStatement pstmt = this.connection.getDatabaseLink().prepareStatement(sql);
        pstmt.setInt(1, idProject);
        ResultSet result = pstmt.executeQuery();
        while (result.next()){
            String title = result.getString("title");
            TaskStatus taskStatus = TaskStatus.valueOf(result.getString("taskStatus"));
            String description = result.getString("description");
            int projectId = result.getInt("projectId");
            LocalDate deadLine = result.getDate("deadLine").toLocalDate();
            int taskDuration = result.getInt("taskDuration");
            String author = result.getString("author");
            String assignedTo = result.getString("assignedTo");
            Task t = new Task(title, taskStatus, description, projectId, deadLine, taskDuration, author, assignedTo);
            taskList.add(t);
        }
        return taskList;
    }

    public void createTask(Task newTask) throws SQLException {
        String sql = "insert into task values (null, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement pstmt = this.connection.getDatabaseLink().prepareStatement(sql);
        pstmt.setString(1, newTask.getTitle());
        pstmt.setString(2, newTask.getTaskStatus().toString());
        pstmt.setString(3, newTask.getDescription());
        pstmt.setString(4, newTask.getPublishedDate().toString());
        pstmt.setInt(5, newTask.getProjectId());
        pstmt.setString(6, newTask.getDeadLine().toString());
        pstmt.setInt(7, newTask.getTaskDuration());
        pstmt.setString(8, newTask.getAuthor());
        pstmt.setString(9, newTask.getAssignedTo());
        pstmt.executeUpdate();
    }

    public void updateTasktitle(int id, String newTitle, int idProject) throws SQLException {
        String sql = "update task set title = ? where id == ? AND projectId == ?";
        PreparedStatement pstmt = this.connection.getDatabaseLink().prepareStatement(sql);
        pstmt.setString(1, newTitle);
        pstmt.setInt(2, id);
        pstmt.setInt(3, idProject);
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

    public void updateTaskdescription(int id, String description, int idProject) throws SQLException {
        String sql = "update task set description = ? where id == ? AND projectId == ?";
        PreparedStatement pstmt = this.connection.getDatabaseLink().prepareStatement(sql);
        pstmt.setString(1, description);
        pstmt.setInt(2, id);
        pstmt.setInt(3, idProject);
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


    public void deleteTask(int idProject) throws SQLException {
        int id = Data.project.getId();
        String sql = "delete from task where id == ? AND projectId == ?";
        PreparedStatement pstmt = this.connection.getDatabaseLink().prepareStatement(sql);
        pstmt.setInt(1, id);
        pstmt.setInt(2, idProject);
        pstmt.executeUpdate();
    }
}