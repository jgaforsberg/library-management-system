package com.lms.librarymanagementsystem.controllers;

import com.lms.librarymanagementsystem.Constants;
import com.lms.librarymanagementsystem.DBUtils;
import javafx.application.Platform;
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
@SuppressWarnings("ALL")
public class MainController implements Initializable {
    @FXML
    private Button loginButton;
    @FXML
    private Button signupButton;
    @FXML
    private Button searchButton;
    @FXML
    private Button exitButton;
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
        searchButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DBUtils.changeSceneLogout(event, Constants.SEARCH, Constants.SEARCH_TITLE);
            }
        });
        signupButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DBUtils.changeSceneLogin(event, Constants.SIGN_UP, Constants.SIGN_UP_TITLE,null);
            }
        });
        exitButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                exitProgram();
            }
        });
    }
    public void exitProgram() {
        Platform.exit();
        System.exit(0);
    }
}