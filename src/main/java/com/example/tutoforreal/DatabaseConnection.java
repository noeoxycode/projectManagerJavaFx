package com.example.tutoforreal;

import java.sql.Connection;
import java.sql.DriverManager;
import org.sqlite.SQLiteConnection;

public class DatabaseConnection {
    private static DatabaseConnection INSTANCE;
    private  SQLiteConnection databaseLink;

    private DatabaseConnection() {
        String url = "jdbc:sqlite:C:\\Users\\noe\\IdeaProjects\\projectManagerJavaFx\\src\\main\\java\\com\\example\\tutoforreal\\mydatabase.db";
        try {
            databaseLink = (SQLiteConnection)DriverManager.getConnection(url);
            System.out.println("Connected to the database");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static synchronized DatabaseConnection getInstance(){
        if(INSTANCE==null) INSTANCE = new DatabaseConnection();
        return INSTANCE;
    }

    public SQLiteConnection getDatabaseLink() {
        return databaseLink;
    }
}
