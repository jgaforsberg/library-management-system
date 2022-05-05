package com.lms.librarymanagementsystem.controllers;

import com.lms.librarymanagementsystem.utils.Constants;
import com.lms.librarymanagementsystem.DBUtils;
import com.lms.librarymanagementsystem.Main;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

//  #011B3E blue
//  #F0F0F0 light gray

public class MainController implements Initializable {

    @FXML
    private Button loginButton, signupButton, searchButton, exitButton;
    @FXML
    private TextField usernameTextField;
    @FXML
    private PasswordField passwordPasswordField;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        loginButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DBUtils.logInUser(event, usernameTextField.getText(), passwordPasswordField.getText());
            }
        });
        signupButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DBUtils.changeScene(event, "sign-up.fxml", "D0024E Bibliotekssystem - Kontoregistrering ",null);
            }
        });
        exitButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                exitProgram();
            }
        });
        searchButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DBUtils.changeScene(event, "search.fxml", "D0024E Bibliotekssystem - Sök artiklar ");
            }
        });
    }

    public void exitProgram() {
        System.exit(0);
    }




}