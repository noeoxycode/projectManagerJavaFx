package com.example.tutoforreal;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("project.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 872, 400);
        //stage.setTitle("SQLITE connection");
        stage.setScene(scene);
        File file = new File("imgLink.txt");
        BufferedReader br = new BufferedReader(new FileReader(file));
        String logoLink = br.readLine();
        stage.getIcons().add(new Image(logoLink));
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}