package com.lms.librarymanagementsystem.controllers;
//  #011B3E blue
//  #F0F0F0 light gray
import com.lms.librarymanagementsystem.*;
import com.lms.librarymanagementsystem.models.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.ResourceBundle;

@SuppressWarnings("ALL")
public class LoanController implements Initializable {
    @FXML
    private TableView<MediaModel> searchTableView, loanTableView, reserveTableView;
    @FXML
    private TableColumn<MediaModel, Integer>         mediaIdColumn;
    @FXML
    private TableColumn<MediaModel, String>         titleColumn, formatColumn, categoryColumn, descriptionColumn,
                                                    publisherColumn, editionColumn, authorColumn, isbnColumn,
                                                    directorColumn, actorColumn, countryColumn, ratingColumn,
                                                    availableColumn;
    @FXML
    private TableColumn<LoanModel, Integer> loanLoanIdColumn, loanMediaIdColumn;
    @FXML
    private TableColumn<LoanModel, Date> loanLoanDateColumn, loanReturnDateColumn;
    @FXML
    private TableColumn<LoanModel, String> loanTitleColumn;
    @FXML
    private TableColumn<ReservationModel, Integer> resResIdColumn, resMediaIdColumn, resQueueColumn;
    @FXML
    private TableColumn<ReservationModel, Date> resResDateColumn;
    @FXML
    private TableColumn<ReservationModel, String> resTitleColumn;
    @FXML
    private TextField searchTextField;
    @FXML
    private Button loanButton, reserveButton, finishButton;
    @FXML
    private Label nameLabel;

    private ArrayList<MediaModel>   loanModelArrayList = new ArrayList<>(),
                                    reservationModelArrayList = new ArrayList<>(),
                                    mediaModelArrayList = new ArrayList<>();
    private ObservableList<MediaModel>  mediaModelObservableList = FXCollections.observableArrayList(),
                                        loanModelObservableList = FXCollections.observableArrayList(),
                                        reservationModelObservableList = FXCollections.observableArrayList();

    private UserModel activeUser;
    private MediaModel mediaModel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        search();
        loanButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                extractArticle();
                if(loanModelObservableList != null) {
                    ServiceUtils.addLoan(mediaModel.getMediaid(), activeUser.getUserid());
                    refreshLoan();
                    loan();
                    refreshSearch();
                    search();
                }else System.out.println("Lån ej laddade! ");
            }
        });
        reserveButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                extractArticle();
                if(reservationModelObservableList != null) {
                    ServiceUtils.addReservation(mediaModel.getMediaid(), activeUser.getUserid());
                    refreshReservation();
                    reservation();
                    refreshSearch();
                    search();
               }else System.out.println("Reservationer ej laddade!");
            }
        });
        finishButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                SceneUtils.changeSceneLogin(event, Constants.LOGIN, Constants.LOGIN_TITLE, activeUser.getUsername());
            }
        });
    }
    private void extractArticle() {
        mediaModel = searchTableView.getSelectionModel().getSelectedItem();
    }
    public void setUserInformation(String username){
        activeUser = new UserModel();
        setUserModelInformation(username);
        nameLabel.setText(activeUser.getUsername());
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
        loanModelObservableList.clear();
    }
    private void refreshSearch()    {
        mediaModelObservableList.clear();
    }
    private void refreshReservation(){reservationModelObservableList.clear();}

    private void search()  {
        mediaModelArrayList = ArticleUtils.search();
        mediaModelObservableList.addAll(mediaModelArrayList);
        mediaIdColumn.setCellValueFactory((new PropertyValueFactory<>("mediaid")));
        titleColumn.setCellValueFactory((new PropertyValueFactory<>("title")));
        formatColumn.setCellValueFactory((new PropertyValueFactory<>("format")));
        categoryColumn.setCellValueFactory((new PropertyValueFactory<>("category")));
        descriptionColumn.setCellValueFactory((new PropertyValueFactory<>("description")));
        publisherColumn.setCellValueFactory((new PropertyValueFactory<>("publisher")));
        editionColumn.setCellValueFactory((new PropertyValueFactory<>("edition")));
        authorColumn.setCellValueFactory((new PropertyValueFactory<>("author")));
        isbnColumn.setCellValueFactory((new PropertyValueFactory<>("isbn")));
        directorColumn.setCellValueFactory((new PropertyValueFactory<>("director")));
        actorColumn.setCellValueFactory((new PropertyValueFactory<>("actor")));
        countryColumn.setCellValueFactory((new PropertyValueFactory<>("country")));
        ratingColumn.setCellValueFactory((new PropertyValueFactory<>("rating")));
        availableColumn.setCellValueFactory((new PropertyValueFactory<>("available")));

        searchTableView.setItems(mediaModelObservableList);
        FilteredList<MediaModel> filteredData = new FilteredList<>(mediaModelObservableList, b -> true);
        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
        filteredData.setPredicate(mediaModel -> {
            if (newValue.isEmpty() || newValue.isBlank() || newValue == null) {return true;}
            String searchKeyWord = newValue.toLowerCase();
            if (mediaModel.titleProperty().toString().toLowerCase().indexOf(searchKeyWord) > -1)  {
                return true;
            }else if (mediaModel.formatProperty().toString().toLowerCase().indexOf(searchKeyWord) > -1)   {
                return true;
            }else if (mediaModel.categoryProperty().toString().toLowerCase().indexOf(searchKeyWord) > -1) {
                return true;
            }else if (mediaModel.descriptionProperty().toString().toLowerCase().indexOf(searchKeyWord) > -1)  {
                return true;
            }else if (mediaModel.publisherProperty().toString().toLowerCase().indexOf(searchKeyWord) > -1){
                return true;
            }else if (mediaModel.editionProperty().toString().toLowerCase().indexOf(searchKeyWord) > -1){
                return true;
            }else if (mediaModel.authorProperty().toString().toLowerCase().indexOf(searchKeyWord) > -1)   {
                return true;
            }else if (mediaModel.isbnProperty().toString().toLowerCase().indexOf(searchKeyWord) > -1)     {
                return true;
            }else if(mediaModel.directorProperty().toString().toLowerCase().indexOf(searchKeyWord) > -1)  {
                return true;
            }else if (mediaModel.actorProperty().toString().toLowerCase().indexOf(searchKeyWord) > -1)    {
                return true;
            }else if (mediaModel.countryProperty().toString().toLowerCase().indexOf(searchKeyWord) > -1)  {
                return true;
            }else if (mediaModel.ratingProperty().toString().toLowerCase().indexOf(searchKeyWord) > -1)   {
                return true;
            }else if (mediaModel.availableProperty().toString().toLowerCase().indexOf(searchKeyWord) > -1)   {
                return true;
            }else
                return false;
            });
        });
        SortedList<MediaModel> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(searchTableView.comparatorProperty());
        searchTableView.setItems(sortedData);
    }
    private void reservation() {
        reservationModelArrayList = UserUtils.reservation(activeUser.getUserid());
        reservationModelObservableList.addAll(reservationModelArrayList);
        resResIdColumn.setCellValueFactory((new PropertyValueFactory<>("reservationid")));
        resMediaIdColumn.setCellValueFactory((new PropertyValueFactory<>("mediaid")));
        resTitleColumn.setCellValueFactory((new PropertyValueFactory<>("title")));
        resQueueColumn.setCellValueFactory((new PropertyValueFactory<>("queuenumber")));
        resResDateColumn.setCellValueFactory((new PropertyValueFactory<>("reservationdate")));
        reserveTableView.setItems(reservationModelObservableList);

    }
    private void loan() {
        loanModelArrayList = UserUtils.loan(activeUser.getUserid());
        loanModelObservableList.addAll(loanModelArrayList);
        loanLoanIdColumn.setCellValueFactory((new PropertyValueFactory<>("loanid")));
        loanMediaIdColumn.setCellValueFactory((new PropertyValueFactory<>("mediaid")));
        loanTitleColumn.setCellValueFactory((new PropertyValueFactory<>("title")));
        loanLoanDateColumn.setCellValueFactory((new PropertyValueFactory<>("loandate")));
        loanReturnDateColumn.setCellValueFactory((new PropertyValueFactory<>("returndate")));
        loanTableView.setItems((loanModelObservableList));
    }
}
