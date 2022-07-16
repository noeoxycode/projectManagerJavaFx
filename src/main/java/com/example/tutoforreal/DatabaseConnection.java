package com.example.tutoforreal;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import org.sqlite.SQLiteConnection;

public class DatabaseConnection {
    private static DatabaseConnection INSTANCE;
    private  SQLiteConnection databaseLink;

    private DatabaseConnection() throws IOException {
        File file = new File("dbLink.txt");
        BufferedReader br = new BufferedReader(new FileReader(file));
        String dbLink = br.readLine();

        String url = "jdbc:sqlite:" + dbLink;
        try {
            databaseLink = (SQLiteConnection)DriverManager.getConnection(url);
            System.out.println("Connected to the database");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static synchronized DatabaseConnection getInstance() throws IOException {
        if(INSTANCE==null) INSTANCE = new DatabaseConnection();
        return INSTANCE;
    }

    public SQLiteConnection getDatabaseLink() {
        return databaseLink;
    }
}
