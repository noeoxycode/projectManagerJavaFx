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
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.sqlite.SQLiteCommitListener;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;

public class UsersManager implements Initializable {
    @FXML
    private Label LogMessage;

    @FXML
    private TextField newUserName;

    @FXML
    private TableView<User> tableUsers;

    @FXML
    private TableColumn<User, String> tableName;

    @Override
    public void initialize(URL location, ResourceBundle ressources) {
        try {
            UsersManager.this.getAllUsers();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            DatabaseConnection.getInstance().getDatabaseLink().addCommitListener(new SQLiteCommitListener() {
                @Override
                public void onCommit() {
                    try {
                        UsersManager.this.getAllUsers();
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

    public void getAllUsers () throws IOException {
        String logs = "Usermanager getAllUsers : ";
        ProjectQueries projectQueries = new ProjectQueries(DatabaseConnection.getInstance());
        ObservableList<User> usersList = FXCollections.observableArrayList();
        try {
            ArrayList<User> queryOutput = projectQueries.getAllUsers();
            int size = queryOutput.size();
            for (int cpt = 0; cpt < size; cpt++) {
                User user = queryOutput.get(cpt);
                usersList.add(user);
            }
            tableName.setCellValueFactory(new PropertyValueFactory<>("name"));
            tableUsers.setItems(usersList);
            logs = logs + "success";
            LogWriter.writeLogs(logs);
        } catch (SQLException e) {
            e.printStackTrace();
            logs = logs + "failed : " + e;
            LogWriter.writeLogs(logs);
        }
    }

    public void addUser() throws SQLException, IOException {
        String logs = "UserManager addUser : ";
        String p = newUserName.getText();
        ProjectQueries projectQueries = new ProjectQueries(DatabaseConnection.getInstance());
        ArrayList<User> tmp = projectQueries.getAllUsers();
        for(int i = 0; i < tmp.size(); i++){
            if(Objects.equals(tmp.get(i).getName(), p))
            {
                LogMessage.setText("Utilisateur d??j?? existant.");
                LogMessage.setTextFill(Color.RED);
                return;
            }
        }
        try {
            LogMessage.setText("Utilisateur ajout??");
            LogMessage.setTextFill(Color.GREEN);
            projectQueries.addUser(p);
            logs = logs + "success";
            LogWriter.writeLogs(logs);
        } catch (SQLException e) {
            e.printStackTrace();
            logs = logs + "failed : " + e;
            LogWriter.writeLogs(logs);
        }

    }

    public void deleteUser() throws SQLException, IOException {
        String logs = "UserManager deleteUser : ";
        User user = tableUsers.getSelectionModel().getSelectedItem();
        ProjectQueries projectQueries = new ProjectQueries(DatabaseConnection.getInstance());
        try {
            projectQueries.deleteUser(user.getId());
            LogMessage.setText("Utilisateur supprim??");
            LogMessage.setTextFill(Color.BLACK);
            logs = logs + "success";
            LogWriter.writeLogs(logs);
        } catch (SQLException e) {
            e.printStackTrace();
            logs = logs + "failed : " + e;
            LogWriter.writeLogs(logs);
        }

    }


    public void cancelWindow(ActionEvent event) {
        ((Stage)(((Button)event.getSource()).getScene().getWindow())).close();
    }
}