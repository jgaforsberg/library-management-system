package com.lms.librarymanagementsystem.controllers;
//  #011B3E blue
//  #F0F0F0 light gray
// TODO add functionality for handling loans and reservations

import com.lms.librarymanagementsystem.Constants;
import com.lms.librarymanagementsystem.DBUtils;
import com.lms.librarymanagementsystem.models.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AccountController implements Initializable {

    private UserModel accountUser;

    @FXML
    private Label idLabel, usernameLabel, firstnameLabel, lastnameLabel, usertypeLabel, maxLoanLabel, maxReservationLabel;
    @FXML
    private TableView<LoanObjectModel> loanTableView;
    @FXML
    private TableColumn<String, Integer> loanMediaIdColumn, loanLoanIdColumn;
    @FXML
    private TableColumn<String, String> loanTitleColumn;
    @FXML
    private TableColumn<String, Integer> resMediaIdColumn, resQueueColumn;
    @FXML
    private TableColumn<String, String> resTitleColumn;
    @FXML
    private TableView<ReservationObjectModel> reservationTableView;
    @FXML
    private Button returnLoanButton, endReservationButton, returnButton;
    @FXML
    private ObservableList<String> loanObservableList = FXCollections.observableArrayList();
    @FXML
    private ObservableList<String> reservationObservableList = FXCollections.observableArrayList();

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
        usernameLabel.setText(username);
        getUserInformation();
        getUserLoans();
        getUserReservations();
    }
    // set number of loans
    private void getUserInformation()    {
        System.out.println("getUserInformation()");
        //  Integer userid;
        String username = usernameLabel.getText();
        String usertype, firstname, lastname;
        Integer userid, maxloan, maxreservation;
        Connection connection = null;
        PreparedStatement psFetchUserInfo = null;
        ResultSet resultSet = null;
        try{
            System.out.println("try block");
            connection = DBUtils.getDBLink();
            psFetchUserInfo = connection.prepareStatement("SELECT * FROM users WHERE username = ?;");
            System.out.println(username);
            psFetchUserInfo.setString(1, username);
            resultSet = psFetchUserInfo.executeQuery();
            while (resultSet.next()) {
                userid = resultSet.getInt("id");
                firstname = resultSet.getString("firstname");
                lastname = resultSet.getString("lastname");
                usertype = resultSet.getString("usertype");

                idLabel.setText(userid.toString());
                System.out.println(userid);
                firstnameLabel.setText(firstname);
                System.out.println(firstname);
                lastnameLabel.setText(lastname);
                System.out.println(lastname);
                usertypeLabel.setText(usertype);
                switch (usertype.toLowerCase()) {
                    case "plebej" -> {
                        System.out.println("plebej");
                        maxloan = 3;
                        maxreservation = 1;
                        maxLoanLabel.setText(maxloan.toString());
                        maxReservationLabel.setText(maxreservation.toString());
                    }
                    case "anställd" -> {
                        System.out.println("anställd");
                        maxloan = 5;
                        maxreservation = 3;
                        maxLoanLabel.setText(maxloan.toString());
                        maxReservationLabel.setText(maxreservation.toString());
                    }
                    case "student" -> {
                        System.out.println("student");
                        maxloan = 5;
                        maxreservation = 3;
                        maxLoanLabel.setText(maxloan.toString());
                        maxReservationLabel.setText(maxreservation.toString());
                    }
                    case "forskare" -> {
                        System.out.println("forskare");
                        maxloan = 10;
                        maxreservation = 5;
                        maxLoanLabel.setText(maxloan.toString());
                        maxReservationLabel.setText(maxreservation.toString());
                    }
                    case "admin" -> {
                        System.out.println("admin");
                        maxloan = 100;
                        maxreservation = 100;
                        maxLoanLabel.setText(maxloan.toString());
                        maxReservationLabel.setText(maxreservation.toString());
                    }
                    default -> {
                        System.out.println("Kan ej läsa användartyp! ");
                    }
                }
            }
        }catch (SQLException e) {
            e.printStackTrace();
            e.getCause();
        }finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (psFetchUserInfo != null) {
                try {
                    psFetchUserInfo.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if(connection != null)  {
                try {
                    connection.close();
                }catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    private void getUserLoans() {
        Connection connection = null;
        PreparedStatement psFetchLoans = null;
        ResultSet resultSet = null;
        Integer loanid;
        try {
            connection = DBUtils.getDBLink();
            psFetchLoans = connection.prepareStatement("SELECT loan.loanid, loan.returndate FROM loan where userid = ?;");
            psFetchLoans.setInt(1, Integer.parseInt(idLabel.getText()));
            resultSet = psFetchLoans.executeQuery();
            while (resultSet.next())    {
                loanid = resultSet.getInt("loanid");
                psFetchLoans = connection.prepareStatement( "SELECT media.mediaid, media.title, loan.loanid, loan.loandate  FROM media JOIN loan ON media.mediaid = loan.mediaid WHERE loan.loanid = ?;");
                psFetchLoans.setInt(1, loanid);
                resultSet = psFetchLoans.executeQuery();
                while (resultSet.next())    {
                    Integer querymedia = resultSet.getInt("mediaid");
                    String querytitle = resultSet.getString("title");
                    Integer queryloanid = resultSet.getInt("loanid");
                    //loanObservableList.add(querymedia,querytitle, queryloanid);
                    //loanListView.getItems().addAll(loanObservableList);
                    System.out.println(querymedia);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            e.getCause();
        }finally {
            if (resultSet != null)  {
                try {
                    resultSet.close();
                }catch (SQLException e) {
                    e.printStackTrace();
                    e.getCause();
                }
            }
            if (psFetchLoans != null)  {
                try {
                    psFetchLoans.close();
                }catch (SQLException e) {
                    e.printStackTrace();
                    e.getCause();
                }
            }
            if (connection != null)  {
                try {
                    connection.close();
                }catch (SQLException e) {
                    e.printStackTrace();
                    e.getCause();
                }
            }
        }
    }
    private void getUserReservations()  {
        Connection connection = null;
        PreparedStatement psFetchLoans = null;
        ResultSet resultSet = null;
        Integer loanid;
    }
}
