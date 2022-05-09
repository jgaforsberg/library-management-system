package com.lms.librarymanagementsystem;

import com.lms.librarymanagementsystem.utils.Constants;
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

public class LoginController implements Initializable {


    @FXML
    private Button logoutButton, searchButton, activeLoansButton, inventoryButton, accountLoans;
    @FXML
    private Label welcomeLabel, nameLabel;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        logoutButton.setOnAction(new EventHandler<ActionEvent>()    {
//          changes scenes through DBUtils at press of logoutButton
            @Override
            public void handle(ActionEvent event) {
                DBUtils.changeScene(event, "main.fxml", " Bibliotekssystem - Välkommen! ");
            }
        });
        searchButton.setOnAction(new EventHandler<ActionEvent>()    {
            @Override
            public void handle(ActionEvent event) {
                DBUtils.changeScene(event, "search.fxml", "D0024E Bibliotekssystem - Sök ");
            }
        });
        activeLoansButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Ej implementerad funktion! ");
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("Kommer snart. ");
                alert.show();
                //DBUtils.changeScene(event, "account.fxml", "D0024E Bibliotekssystem - Konto", null);
            }
        });
        inventoryButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Ej implementerad funktion! ");
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("Kommer snart. ");
                alert.show();
            }
        });
        accountLoans.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Ej implementerad funktion! ");
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("Kommer snart. ");
                alert.show();
            }
        });

    }

//  display of username on welcome screen
    public void setUserInformation(String username)    {
        nameLabel.setText(username);
    }

}
