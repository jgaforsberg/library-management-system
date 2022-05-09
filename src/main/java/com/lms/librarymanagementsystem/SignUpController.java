package com.lms.librarymanagementsystem;

import com.lms.librarymanagementsystem.utils.Constants;
import com.lms.librarymanagementsystem.DBUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;

//  #011B3E blue
//  #F0F0F0 light gray

public class SignUpController implements Initializable {

    @FXML
    private Button signupButton, loginButton;
    @FXML
    private TextField usernameTextField, firstnameTextField, lastnameTextField;
    @FXML
    private PasswordField passwordPasswordField;
    @FXML
    private ChoiceBox<String> usertypeChoiceBox;

    private final String [] usertypes = {"admin", "bibliotekarie", "forskare", "student", "plebej"};
    ObservableList<String> userTypes = FXCollections.observableArrayList("admin", "bibliotekarie", "forskare", "student", "plebej");

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        usertypeChoiceBox.getItems().addAll(userTypes);

        signupButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String boxChoice = usertypeChoiceBox.getValue();

                if(!usernameTextField.getText().trim().isEmpty() && !passwordPasswordField.getText().trim().isEmpty())  {
                    DBUtils.signUpUser(event, usernameTextField.getText(), passwordPasswordField.getText(), firstnameTextField.getText(), lastnameTextField.getText(), boxChoice);
                }else   {
                    System.out.println("All information krävs! ");
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Fyll i all information för att gå vidare. ");
                    alert.show();
                }
            }
        });

        loginButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DBUtils.changeScene(event, "main.fxml", "D0024E Bibliotekssystem - Välkommen! ", null);
            }
        });

    }

 /*   public void getUsertypes(ActionEvent event) {

        if(usertypeChoiceBox.getValue().isBlank())  {

        }else   {

        }
        String usertypes = usertypeChoiceBox.getValue();

    }   */
}