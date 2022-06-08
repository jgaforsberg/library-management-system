package com.lms.librarymanagementsystem.controllers;
//  #011B3E blue
//  #F0F0F0 light gray
import com.lms.librarymanagementsystem.Constants;
import com.lms.librarymanagementsystem.DBUtils;
import com.lms.librarymanagementsystem.SceneUtils;
import com.lms.librarymanagementsystem.ServiceUtils;
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
import java.util.ResourceBundle;

@SuppressWarnings("ALL")
public class LoanController implements Initializable {
    @FXML
    private TableView<MediaModel> searchTableView;
    @FXML
    private TableView<LoanModel> loanTableView;
    @FXML
    private TableView<ReservationModel> reserveTableView;
    @FXML
    private TableColumn<MediaModel, Integer>         mediaIdColumn;
    @FXML
    private TableColumn<MediaModel, String>         titleColumn, formatColumn, categoryColumn, descriptionColumn,
                                                    publisherColumn, editionColumn, authorColumn, isbnColumn,
                                                    directorColumn, actorColumn, countryColumn, ratingColumn,
                                                    availableColumn;
    @FXML
    private TableColumn<LoanModel, Integer> loanLoanIdColumn, loanMediaIdColumn, loanUserIdColumn, loanReturnedColumn;
    @FXML
    private TableColumn<LoanModel, Date> loanLoanDateColumn, loanReturnDateColumn;
    @FXML
    private TableColumn<ReservationModel, Integer> resResIdColumn, resMediaIdColumn, resUserIdColumn, resQueueColumn;
    @FXML
    private TableColumn<ReservationModel, Date> resResDateColumn;
    @FXML
    private TextField searchTextField;
    @FXML
    private Button loanButton, reserveButton, finishButton;
    @FXML
    private Label nameLabel;

    private ObservableList<MediaModel> mediaModelObservableList = FXCollections.observableArrayList();
    private ObservableList<LoanModel> loanModelObservableList = FXCollections.observableArrayList();
    private ObservableList<ReservationModel> reservationModelObservableList = FXCollections.observableArrayList();

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
        Connection connection = null;
        PreparedStatement psFetchArticles = null;
        ResultSet resultSet = null;
        try {
            connection = DBUtils.getDBLink();
            psFetchArticles = connection.prepareStatement(  "SELECT mediaid, title, format, category, description, " +
                                                                "publisher, edition, author, isbn, " +
                                                                "director, actor, country, rating, available FROM media;");
            resultSet = psFetchArticles.executeQuery();
            while (resultSet.next()) {
                Integer queryMediaId = resultSet.getInt("mediaid");
                String queryTitle = resultSet.getString("title");
                String queryFormat = resultSet.getString("format");
                String queryCategory = resultSet.getString("category");
                String queryDescription = resultSet.getString("description");
                String queryPublisher = resultSet.getString("publisher");
                String queryEdition = resultSet.getString("edition");
                String queryAuthor = resultSet.getString("author");
                String queryIsbn = resultSet.getString("isbn");
                String queryDirector = resultSet.getString("director");
                String queryActor = resultSet.getString("actor");
                String queryCountry = resultSet.getString("country");
                String queryRating = resultSet.getString("rating");
                String queryAvailable = resultSet.getString("available");
                mediaModelObservableList.add(new MediaModel(queryMediaId,
                                                            queryTitle,
                                                            queryFormat,
                                                            queryCategory,
                                                            queryDescription,
                                                            queryPublisher,
                                                            queryEdition,
                                                            queryAuthor,
                                                            queryIsbn,
                                                            queryDirector,
                                                            queryActor,
                                                            queryCountry,
                                                            queryRating,
                                                            queryAvailable));
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
        }catch (SQLException e) {
            e.printStackTrace();
        }finally    {
            DBUtils.closeDBLink(connection, psFetchArticles, null, null, resultSet);
        }
    }
    private void reservation() {
        Connection connection = null;
        PreparedStatement psFetchReservations = null;
        ResultSet resultSet = null;
        try{
            connection = DBUtils.getDBLink();
            psFetchReservations = connection.prepareStatement("SELECT reservation.reservationid, reservation.mediaid, media.title, reservation.queuenumber, reservation.reservationdate FROM media JOIN reservation on media.mediaid = reservation.mediaid WHERE userid = ?;");
            psFetchReservations.setInt(1, activeUser.getUserid());
            resultSet = psFetchReservations.executeQuery();
            while (resultSet.next()) {
                Integer queryReservationid = resultSet.getInt("reservationid");
                Integer queryMediaid = resultSet.getInt("mediaid");
                String queryTitle = resultSet.getString("title");
                Integer queryQueueNumber = resultSet.getInt("queuenumber");
                Date queryReservationdate = resultSet.getDate("reservationdate");
                reservationModelObservableList.add(new ReservationModel(queryReservationid,
                                                                        queryMediaid,
                                                                        queryTitle,
                                                                        null,
                                                                        queryQueueNumber,
                                                                        queryReservationdate
                                                                        ));
                resResIdColumn.setCellValueFactory((new PropertyValueFactory<>("reservationid")));
                resMediaIdColumn.setCellValueFactory((new PropertyValueFactory<>("mediaid")));
                resUserIdColumn.setCellValueFactory((new PropertyValueFactory<>("userid")));
                resQueueColumn.setCellValueFactory((new PropertyValueFactory<>("queuenumber")));
                resResDateColumn.setCellValueFactory((new PropertyValueFactory<>("reservationdate")));
                reserveTableView.setItems(reservationModelObservableList);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            e.getCause();
        }finally {
            DBUtils.closeDBLink(connection, psFetchReservations, null, null, resultSet);
        }
    }
    private void loan() {
        Connection connection = null;
        PreparedStatement psFetchLoans = null;
        ResultSet resultSet = null;
        try {
            connection = DBUtils.getDBLink();
            psFetchLoans = connection.prepareStatement("SELECT loan.loanid, loan.mediaid, loan.userid, media.title, loan.loandate, loan.returndate FROM media JOIN loan ON media.mediaid = loan.mediaid WHERE userid = ?;");
            psFetchLoans.setInt(1, activeUser.getUserid());
            resultSet = psFetchLoans.executeQuery();
            while (resultSet.next()) {
                Integer queryLoanid = resultSet.getInt("loanid");
                Integer queryMediaid = resultSet.getInt("mediaid");
                String queryTitle = resultSet.getString("title");
                Integer queryUserid = resultSet.getInt("userid");
                Date queryLoandate = resultSet.getDate("loandate");
                Date queryReturndate = resultSet.getDate("returndate");
                loanModelObservableList.add(new LoanModel(  queryLoanid,
                                                            queryMediaid,
                                                            queryTitle,
                                                            queryUserid,
                                                            queryLoandate,
                                                            queryReturndate,
                                                            null
                                                            ));
                loanLoanIdColumn.setCellValueFactory((new PropertyValueFactory<>("loanid")));
                loanMediaIdColumn.setCellValueFactory((new PropertyValueFactory<>("mediaid")));
                loanUserIdColumn.setCellValueFactory((new PropertyValueFactory<>("userid")));
                loanLoanDateColumn.setCellValueFactory((new PropertyValueFactory<>("loandate")));
                loanReturnDateColumn.setCellValueFactory((new PropertyValueFactory<>("returndate")));
                loanReturnedColumn.setCellValueFactory((new PropertyValueFactory<>("returned")));
                loanTableView.setItems((loanModelObservableList));
            }
        } catch (SQLException el) {
            el.printStackTrace();
            el.getCause();
        } finally {
            DBUtils.closeDBLink(connection, psFetchLoans, null, null, resultSet);
        }
    }
}
