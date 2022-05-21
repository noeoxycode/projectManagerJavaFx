package com.example.tutoforreal;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnection {
    private Connection databaseLink;

    public Connection getDbConnection(){
        String url = "jdbc:sqlite:C:\\Users\\noe\\IdeaProjects\\tutoForReal\\src\\main\\java\\com\\example\\tutoforreal\\mydatabase.db";

        try {
            databaseLink = DriverManager.getConnection(url);
            System.out.println("Connected to the database");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return databaseLink;
    }

    public Connection getDatabaseLink() {
        return databaseLink;
    }
}
