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

@SuppressWarnings({"Convert2Lambda", "Convert2Diamond"})
public class LoginController implements Initializable {
    @FXML
    private Button logoutButton, searchButton, inventoryButton, accountButton;
    @FXML @SuppressWarnings("unused")
    private Label welcomeLabel, nameLabel;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        logoutButton.setOnAction(new EventHandler<ActionEvent>()    {
//          changes scenes through DBUtils at press of logoutButton
            @Override
            public void handle(ActionEvent event) {
                DBUtils.changeScene(event, Constants.MAIN, Constants.MAIN_TITLE);
            }
        });
        searchButton.setOnAction(new EventHandler<ActionEvent>()    {
            @Override
            public void handle(ActionEvent event) {
                //information();
                DBUtils.changeScene(event, Constants.LOAN, Constants.LOAN_TITLE, nameLabel.getText());
            }
        });
        inventoryButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //information();
                DBUtils.changeScene(event, Constants.INVENTORY, Constants.INVENTORY_TITLE, nameLabel.getText());
            }
        });
        accountButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //information();
                DBUtils.changeScene(event, Constants.ACCOUNT, Constants.ACCOUNT_TITLE, nameLabel.getText());
            }
        });

    }
//  display of username on welcome screen
    public void setUserInformation(String username)    {
        nameLabel.setText(username);
    }
    public void information()   {
        System.out.println("Ej implementerad funktion! ");
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("Kommer snart. ");
        alert.show();
    }
}
