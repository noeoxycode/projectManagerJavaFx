package com.example.tutoforreal.controller;

import com.example.tutoforreal.*;
import com.itextpdf.text.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.sqlite.SQLiteCommitListener;

import java.io.*;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import com.itextpdf.text.pdf.PdfWriter;

public class Controller implements Initializable {

    @FXML
    private TableView<Project> projectTableView;

    @FXML
    private TableColumn<Project, String> projetColumnTitle;

    @FXML
    private TableColumn<Project, String> projetColumnDescription;

    @FXML
    private Label logMessageProject;

    @Override
    public void initialize(URL location, ResourceBundle ressources) {
        try {
            Controller.this.getAllProjectsController();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            DatabaseConnection.getInstance().getDatabaseLink().addCommitListener(new SQLiteCommitListener() {
                @Override
                public void onCommit() {
                    try {
                        Controller.this.getAllProjectsController();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                @Override
                public void onRollback() {

                }
            });
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getAllProjectsController () throws IOException {
        String logs = "Controller getAllProjectsController : ";
            ProjectQueries projectQueries = new ProjectQueries(DatabaseConnection.getInstance());
            ObservableList<Project>projectList = FXCollections.observableArrayList();
            try {
                ArrayList<Project>queryOutput = projectQueries.getAllProject();
                int size = queryOutput.size();
                for (int cpt = 0; cpt < size; cpt++) {
                    Project p = queryOutput.get(cpt);
                    projectList.add(p);
                }
                projetColumnTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
                projetColumnDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
                projectTableView.setItems(projectList);
                logs = logs + "success";
            } catch (SQLException e) {
                e.printStackTrace();
                logs = logs + "failed " + e;
                LogWriter.writeLogs(logs);

            }
    }

    public void deleteProject() throws IOException {
        String logs = "Controller deleteProject : ";
        if(projectTableView.getSelectionModel().getSelectedItem() != null){
            Project p = projectTableView.getSelectionModel().getSelectedItem();
            System.out.println(p.getId());
            int id = p.getId();
            ProjectQueries projectQueries = new ProjectQueries(DatabaseConnection.getInstance());
            try {
                projectQueries.deleteProject(id);
                logMessageProject.setText("Projet suprimé");
                logMessageProject.setTextFill(Color.GREEN);
                logs = logs + "success";
                LogWriter.writeLogs(logs);
            }
            catch (SQLException e){
                System.out.println(e);
                logs = logs + "failed : " + e;
                LogWriter.writeLogs(logs);
            }
        }
        else {
            logMessageProject.setText("Veuillez séléctionner un projet pour le supprimer");
            logMessageProject.setTextFill(Color.RED);
        }
    }

    @FXML
    private void updateProjectWindow(ActionEvent event) throws IOException {
        if(projectTableView.getSelectionModel().getSelectedItem() != null){
            Data.project = projectTableView.getSelectionModel().getSelectedItem();
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("updateProject.fxml"));
            Scene updateScene = new Scene(fxmlLoader.load(), 450, 300);
            Stage updateWindow = new Stage();
            updateWindow.setScene(updateScene);
            File file = new File("imgLink.txt");
            BufferedReader br = new BufferedReader(new FileReader(file));
            String logoLink = br.readLine();
            updateWindow.getIcons().add(new Image(logoLink));
            updateWindow.show();
        }
        else {
            logMessageProject.setText("Veuillez séléctionner un projet pour le modifier");
            logMessageProject.setTextFill(Color.RED);
        }
    }

    public void createProjectWindow() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("createProject.fxml"));
        Scene updateScene = new Scene(fxmlLoader.load(), 450, 300);
        Stage updateWindow = new Stage();
        updateWindow.setScene(updateScene);
        File file = new File("imgLink.txt");
        BufferedReader br = new BufferedReader(new FileReader(file));
        String logoLink = br.readLine();
        updateWindow.getIcons().add(new Image(logoLink));
        updateWindow.show();
    }

    public void manageUsers() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("usersManager.fxml"));
        Scene updateScene = new Scene(fxmlLoader.load(), 450, 300);
        Stage updateWindow = new Stage();
        updateWindow.setScene(updateScene);
        File file = new File("imgLink.txt");
        BufferedReader br = new BufferedReader(new FileReader(file));
        String logoLink = br.readLine();
        updateWindow.getIcons().add(new Image(logoLink));
        updateWindow.show();
    }

    public void creatingPdf() throws DocumentException, FileNotFoundException, SQLException {
        String logs = "Controller creatingPdf : ";
        Data.project = projectTableView.getSelectionModel().getSelectedItem();
        if(Data.project == null){
            logMessageProject.setText("Veuillez séléctionner un projet pour en faire un pdf");
            logMessageProject.setTextFill(Color.RED);
            return;
        }
        try{
            TaskQueries taskQueries = new TaskQueries(DatabaseConnection.getInstance());
            ArrayList<Task>queryOutput = taskQueries.getAlltask();

            Document document = new Document();
            try{
                PdfWriter.getInstance(document, new FileOutputStream("projectPdf.pdf"));

                document.open();
                Font font = FontFactory.getFont(FontFactory.COURIER, 16, BaseColor.BLACK);

                Paragraph chunk = new Paragraph(Data.project.toString(), font);
                Paragraph tasks = new Paragraph(queryOutput.toString(), font);

                document.add(chunk);
                document.add(tasks);
                try {
                    Runtime.getRuntime().exec("rundll32 url.dll, FileProtocolHandler " + "projectPdf.pdf");
                } catch (IOException e) {
                    e.printStackTrace();
                    logs = logs + "failed " + e;
                    LogWriter.writeLogs(logs);
                }
                document.close();
                logs = logs + "success";
                LogWriter.writeLogs(logs);
            } catch (DocumentException e) {
                e.printStackTrace();
                logs = logs + "failed " + e;
                LogWriter.writeLogs(logs);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                logs = logs + "failed " + e;
                LogWriter.writeLogs(logs);
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
            logs = logs + "failed " + e;
            LogWriter.writeLogs(logs);
        }
    }
}