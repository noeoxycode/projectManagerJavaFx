package com.example.tutoforreal;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ProjectQueries {
    private final DatabaseConnection connection;
    public ProjectQueries(DatabaseConnection connection) {
        this.connection = connection;
    }

    public Project getProjectById(int id) throws SQLException {
        String sql = "SELECT * FROM PROJECT WHERE id == ?";
        String title;
        String description;
        PreparedStatement pstmt = this.connection.getDatabaseLink().prepareStatement(sql);
        pstmt.setInt(1, id);
        ResultSet result = pstmt.executeQuery();
        result.next();
        title = result.getString("title");
        description = result.getString("description");
        return new Project(title, description);
    }

    public ArrayList<Project> getAllProject() throws SQLException {
        ArrayList<Project> projectList = new ArrayList<Project>();
        String sql = "SELECT * FROM PROJECT";
        String title;
        String description;
        PreparedStatement pstmt = this.connection.getDatabaseLink().prepareStatement(sql);
        ResultSet result = pstmt.executeQuery();
        while (result.next()){
            Project p = new Project(result.getInt("id"), result.getString("title"), result.getString("description"));
            projectList.add(p);
        }
        return projectList;
    }

    public ArrayList<User> getAllUsers() throws SQLException {
        ArrayList<User> userList = new ArrayList<User>();
        String sql = "SELECT * FROM user";
        PreparedStatement pstmt = this.connection.getDatabaseLink().prepareStatement(sql);
        ResultSet result = pstmt.executeQuery();
        while (result.next()){
            String name =result.getString("name");
            int id = result.getInt("id");
            User newUser = new User(id, name);
            userList.add(newUser);
        }
        return userList;
    }

    public void addUser(String user) throws SQLException {
        String sql = "insert into user values (null, ?)";
        PreparedStatement pstmt = this.connection.getDatabaseLink().prepareStatement(sql);
        pstmt.setString(1, user);
        pstmt.executeUpdate();
    }

    public void deleteUser(int id) throws SQLException {
        String sql = "delete from user where id == ?";
        PreparedStatement pstmt = this.connection.getDatabaseLink().prepareStatement(sql);
        pstmt.setInt(1, id);
        pstmt.executeUpdate();
    }

    public void createProject(Project p) throws SQLException {
        String sql = "insert into project values (null, ?, ?)";
        PreparedStatement pstmt = this.connection.getDatabaseLink().prepareStatement(sql);
        pstmt.setString(1, p.getTitle());
        pstmt.setString(2, p.getDescription());
        pstmt.executeUpdate();
    }

    void updateProjecttitle(int id, String newTitle) throws SQLException {
        String sql = "update project set title = ? where id == ?";
        PreparedStatement pstmt = this.connection.getDatabaseLink().prepareStatement(sql);
        pstmt.setString(1, newTitle);
        pstmt.setInt(2, id);
        pstmt.executeUpdate();
    }

    void updateProjectDescription(int id, String newDescription) throws SQLException {
        String sql = "update project set description = ? where id == ?";
        PreparedStatement pstmt = this.connection.getDatabaseLink().prepareStatement(sql);
        pstmt.setString(1, newDescription);
        pstmt.setInt(2, id);
        pstmt.executeUpdate();
    }
    public void deleteProject(int id) throws SQLException {
        String sql = "delete from project where id == ?";
        PreparedStatement pstmt = this.connection.getDatabaseLink().prepareStatement(sql);
        pstmt.setInt(1, id);
        pstmt.executeUpdate();
    }

}
