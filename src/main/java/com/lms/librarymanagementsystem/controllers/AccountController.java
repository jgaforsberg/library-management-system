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
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class AccountController implements Initializable {
    @FXML
    private Label idLabel, usernameLabel, firstnameLabel, lastnameLabel, usertypeLabel, emailLabel, maxLoanLabel, remainingLoanLabel;
    @FXML
    private TableView<LoanObjectModel> loanTableView;
    @FXML
    private TableColumn<String, Integer> loanMediaIdColumn;
    @FXML
    private TableColumn<String, String> loanTitleColumn;
    @FXML
    private TableColumn<Date, Date> loanLoanDateColumn, loanReturndateColumn;
    @FXML
    private TableColumn<String, Integer> resMediaIdColumn, resResidColumn, resQueueColumn;
    @FXML
    private TableColumn<String, String> resTitleColumn;
    @FXML
    private TableView<ReservationObjectModel> reservationTableView;
    @FXML
    private Button returnLoanButton, endReservationButton, returnButton;
    @FXML
    private ObservableList<LoanObjectModel> loanObservableList = FXCollections.observableArrayList();
    @FXML
    private ObservableList<ReservationObjectModel> reservationObservableList = FXCollections.observableArrayList();

    private UserModel activeUser;
    private LoanObjectModel loanObject;
    private ReservationObjectModel reservationObject;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
       returnLoanButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try{
                    extractLoan();
                }catch (NullPointerException e) {
                    e.getCause();
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Välj ett objekt i listan. ");
                    alert.show();
                }
                if(loanObservableList != null)  {
                    DBUtils.returnLoan(loanObject.getLoanid(), loanObject.getMediaid());
                    refreshLoan();
                    loan();
                }
            }
        });
        endReservationButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            try {
                extractReservation();
            }catch (NullPointerException e) {
                e.getCause();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Välj ett objekt i listan. ");
                alert.show();
            }
                if(reservationObservableList != null)   {
                    DBUtils.returnReservation(reservationObject.getReservationid(), reservationObject.getTitle());
                    refreshReservation();
                    reservation();
                }
            }
        });
        returnButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DBUtils.changeSceneLogin(event, Constants.LOGIN, Constants.LOGIN_TITLE, activeUser.getUsername());
            }
        });
    }
    private void extractLoan() {
        loanObject = loanTableView.getSelectionModel().getSelectedItem();
    }
    private  void extractReservation()  {
        reservationObject = reservationTableView.getSelectionModel().getSelectedItem();
    }
    public void setUserInformation(String username){
        activeUser = new UserModel();
        setUserModelInformation(username);
        idLabel.setText(activeUser.getUserid().toString());
        usernameLabel.setText(activeUser.getUsername());
        firstnameLabel.setText(activeUser.getFirstname());
        lastnameLabel.setText(activeUser.getLastname());
        usertypeLabel.setText(activeUser.getUsertype());
        emailLabel.setText(activeUser.getEmail());
        maxLoanLabel.setText(DBUtils.maxLoans(activeUser.getUserid()).toString());
        remainingLoanLabel.setText(DBUtils.remainingLoans(Integer.valueOf(maxLoanLabel.getText()), activeUser.getUserid()).toString());
        loan();
        reservation();
    }
    private void setUserModelInformation(String username) {
        Connection connection = null;
        PreparedStatement psFetchUser = null;
        ResultSet resultSet = null;
        try {
            connection = DBUtils.getDBLink();
            psFetchUser = connection.prepareStatement("SELECT id, firstname, lastname, usertype, email FROM users WHERE username = ?;");
            psFetchUser.setString(1, username);
            resultSet = psFetchUser.executeQuery();
            while (resultSet.next())    {
                activeUser.setUserid(resultSet.getInt("id"));
                activeUser.setFirstname(resultSet.getString("firstname"));
                activeUser.setLastname(resultSet.getString("lastname"));
                activeUser.setUsertype(resultSet.getString("usertype"));
                activeUser.setEmail(resultSet.getString("email"));
            }
            activeUser.setUsername(username);
        } catch (SQLException e) {
            e.printStackTrace();
            e.getCause();
        }finally {
            DBUtils.closeDBLink(connection, psFetchUser, null, null, resultSet);
        }
    }
    private void refreshLoan() {
        loanObservableList.clear();
    }
    private void refreshReservation(){reservationObservableList.clear();}
    private void loan() {
        Connection connection = null;
        PreparedStatement psFetchLoans = null;
        ResultSet resultSet = null;
        try {
            connection = DBUtils.getDBLink();
            psFetchLoans = connection.prepareStatement("SELECT loan.loandate, loan.loandate FROM loan where userid = ?;");
            psFetchLoans.setInt(1, activeUser.getUserid());
            resultSet = psFetchLoans.executeQuery();
            while (resultSet.next())    {
          //      Integer loanid = resultSet.getInt("loanid");
                psFetchLoans = connection.prepareStatement( "SELECT media.mediaid, media.title, loan.loandate, loan.returndate FROM media " +
                                                                "JOIN loan ON media.mediaid = loan.mediaid WHERE loan.userid = ?;");
                psFetchLoans.setInt(1, activeUser.getUserid());
                resultSet = psFetchLoans.executeQuery();
                while (resultSet.next())    {
                    Integer queryMediaid = resultSet.getInt("mediaid");
                    String queryTitle = resultSet.getString("title");
                    Date queryLoandate = resultSet.getDate("loandate");
                    Date queryReturndate = resultSet.getDate("returndate");
                    loanObservableList.add(new LoanObjectModel( queryMediaid,
                                                                queryTitle,
                                                                queryLoandate,
                                                                queryReturndate
                                                                ));
                    loanMediaIdColumn.setCellValueFactory((new PropertyValueFactory<>("mediaid")));
                    loanTitleColumn.setCellValueFactory((new PropertyValueFactory<>("title")));
                    loanLoanDateColumn.setCellValueFactory((new PropertyValueFactory<>("loandate")));
                    loanReturndateColumn.setCellValueFactory((new PropertyValueFactory<>("returndate")));
                    loanTableView.setItems(loanObservableList);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            e.getCause();
        }finally {
            DBUtils.closeDBLink(connection, psFetchLoans, null, null, resultSet);
        }
    }
    private void reservation()  {
        Connection connection = null;
        PreparedStatement psFetchReservations = null;
        ResultSet resultSet = null;
        try {
            connection = DBUtils.getDBLink();
            psFetchReservations = connection.prepareStatement("SELECT reservation.reservationid, reservation.queuenumber FROM reservation where userid = ?;");
            psFetchReservations.setInt(1, activeUser.getUserid());
            resultSet = psFetchReservations.executeQuery();
            while (resultSet.next())    {
                Integer reservationid = resultSet.getInt("reservationid");
                psFetchReservations = connection.prepareStatement(  "SELECT media.mediaid, media.title, reservation.reservationid, reservation.queuenumber FROM media " +
                                                                        "JOIN reservation ON media.mediaid = reservation.mediaID WHERE reservation.userid = ?;");
                psFetchReservations.setInt(1, activeUser.getUserid());
                resultSet = psFetchReservations.executeQuery();
                while (resultSet.next())    {
                    Integer queryMediaid = resultSet.getInt("mediaid");
                    String queryTitle = resultSet.getString("title");
                    Integer queryReservationid = resultSet.getInt("reservationid");
                    Integer queryQueuenumber = resultSet.getInt("queuenumber");
                    reservationObservableList.add(new ReservationObjectModel(   queryMediaid,
                                                                                queryTitle,
                                                                                queryReservationid,
                                                                                queryQueuenumber
                                                                                ));
                    resMediaIdColumn.setCellValueFactory((new PropertyValueFactory<>("mediaid")));
                    resTitleColumn.setCellValueFactory((new PropertyValueFactory<>("title")));
                    resResidColumn.setCellValueFactory((new PropertyValueFactory<>("reservationid")));
                    resQueueColumn.setCellValueFactory((new PropertyValueFactory<>("queuenumber")));
                    reservationTableView.setItems(reservationObservableList);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            e.getCause();
        }finally {
            DBUtils.closeDBLink(connection, psFetchReservations, null, null, resultSet);
        }
    }
}
