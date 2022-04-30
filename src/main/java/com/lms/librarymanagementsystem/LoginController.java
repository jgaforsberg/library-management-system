package com.lms.librarymanagementsystem;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {


    @FXML
    private Button logoutButton, searchButton, activeLoansButton, inventoryButton, accountLoans;
    @FXML
    private Label welcomeLabel, nameLabel;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        logoutButton.setOnAction(new EventHandler<ActionEvent>() {
            //          changes scenes through DBUtils at press of logoutButton
            @Override
            public void handle(ActionEvent event) {

                DBUtils.changeScene(event,"main.fxml", "Log in!", null);

            }
        });   {

        }


    }

    //  display of username on welcome screen
    public void setUserInformation(String username)    {
        nameLabel.setText(username);
    }

}
