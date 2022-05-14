package com.lms.librarymanagementsystem.controllers;
//  #011B3E blue
//  #F0F0F0 light gray
// TODO add functionality for handling loans and reservations

import com.lms.librarymanagementsystem.Constants;
import com.lms.librarymanagementsystem.DBUtils;
import com.lms.librarymanagementsystem.models.LoanModel;
import com.lms.librarymanagementsystem.models.ReservationModel;
import com.lms.librarymanagementsystem.models.UserModel;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AccountController implements Initializable {

    private UserModel accountUser;

    @FXML
    private Label idLabel, usernameLabel, firstnameLabel, lastnameLabel, accounttypeLabel, maxLoanLabel, maxReservationLabel;
    @FXML
    private ListView<LoanModel> loanListView;
    @FXML
    private ListView<ReservationModel> reservationListView;
    @FXML
    private Button returnLoanButton, endReservationButton, returnButton;
    private Integer userid;

    public void initData(UserModel userModel) {
        accountUser = userModel;
        idLabel.setText(accountUser.getUserid().toString());
        usernameLabel.setText(accountUser.getUsername());
        firstnameLabel.setText(accountUser.getFirstname());
        lastnameLabel.setText(accountUser.getLastname());
        accounttypeLabel.setText(accountUser.getUsertype());
        maxLoanLabel.setText(typeCheck(accountUser.getUserid()).toString());
        maxReservationLabel.setText("3");
    }
    public Integer typeCheck(int userid)    {
        Connection connection = null;
        PreparedStatement psFetchLoans = null;
        ResultSet resultSet = null;
        Integer allowedLoans = Integer.valueOf(accounttypeLabel.getText());
        try{
            connection = DBUtils.getDBLink();
            psFetchLoans = connection.prepareStatement("SELECT * FROM loan WHERE userid = ?;");
            psFetchLoans.setInt(1, userid);
            resultSet = psFetchLoans.executeQuery();
            if (!resultSet.isBeforeFirst()) {
                if(accounttypeLabel.getText().equalsIgnoreCase("")) return 5;
            }   else {
                while (resultSet.next()) {
                    Integer retrieveLoans = resultSet.getInt("loanid");
                    if (retrieveLoans == 5) {
                        return 0;
                    }
                }
            }
        }catch (SQLException e) {
            e.printStackTrace();
            e.getCause();
        }

    return 0;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        returnLoanButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

            }
        });
        endReservationButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

            }
        });
        returnButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DBUtils.changeSceneLogin(event, Constants.LOGIN, Constants.LOGIN_TITLE, usernameLabel.getText());
            }
        });
    }
    public void setUserInformation(String username){
        //this.userid = DBUtils.getUserId(username);
        usernameLabel.setText(username);
    }
}
