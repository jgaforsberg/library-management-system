package com.lms.librarymanagementsystem.controllers;

import com.lms.librarymanagementsystem.Constants;
import com.lms.librarymanagementsystem.DBUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

//  #011B3E blue
//  #F0F0F0 light gray

@SuppressWarnings("ALL")
public class LoginController implements Initializable {
    @FXML @SuppressWarnings("unused")
    private Button logoutButton, searchButton, inventoryButton, accountButton;
    @FXML @SuppressWarnings("unused")
    private Label welcomeLabel, nameLabel;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        searchButton.setOnAction(new EventHandler<ActionEvent>()    {
            @Override
            public void handle(ActionEvent event) {
                DBUtils.changeSceneLoan(event, Constants.LOAN, Constants.LOAN_TITLE, nameLabel.getText());
            }
        });
        accountButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DBUtils.changeSceneAccount(event, Constants.ACCOUNT, Constants.ACCOUNT_TITLE, nameLabel.getText());
            }
        });
        inventoryButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DBUtils.validateUser(event, nameLabel.getText());
            }
        });
        logoutButton.setOnAction(new EventHandler<ActionEvent>()    {
            @Override
            public void handle(ActionEvent event) {
                DBUtils.changeSceneLogout(event, Constants.MAIN, Constants.MAIN_TITLE);
            }
        });
    }
//  Display of username on welcome screen
    public void setUserInformation(String username)    {
        nameLabel.setText(username);
    }
//  DUMMY METHOD FOR NOT YET IMPLEMENTED SCENES
    public void information()   {
        System.out.println("Ej implementerad funktion! ");
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("Kommer snart. ");
        alert.show();
    }
}
