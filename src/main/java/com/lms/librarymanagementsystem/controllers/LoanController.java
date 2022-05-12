package com.lms.librarymanagementsystem.controllers;
//  #011B3E blue
//  #F0F0F0 light gray
import com.lms.librarymanagementsystem.DBUtils;
import com.lms.librarymanagementsystem.models.LoanModel;
import com.lms.librarymanagementsystem.models.MediaModel;
import com.lms.librarymanagementsystem.models.ReservationModel;
import com.lms.librarymanagementsystem.models.UserModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

@SuppressWarnings({"IndexOfReplaceableByContains", "ConstantConditions", "Convert2Diamond", "Convert2Lambda", "CodeBlock2Expr", "RedundantIfStatement", "DuplicatedCode"})
public class LoanController implements Initializable {
    @FXML
    public TableView<MediaModel> searchTableView;
    @FXML
    public TableView<LoanModel> loanTableView;
    @FXML
    public TableView<ReservationModel> reserveTableView;
    @FXML
    public TableColumn<MediaModel, Integer>         mediaIdColumn;
    @FXML
    public TableColumn<MediaModel, String>          titleColumn, formatColumn, categoryColumn, descriptionColumn,
                                                    publisherColumn, editionColumn, authorColumn, isbnColumn,
                                                    directorColumn, actorColumn, countryColumn, ratingColumn,
                                                    availableColumn;
    @FXML
    public TableColumn<LoanModel, Integer> loanLoanIdColumn, loanMediaIdColumn, loanUserIdColumn;
    @FXML
    public TableColumn<LoanModel, Date> loanLoanDateColumn, loanReturnDateColumn;
    @FXML
    public TableColumn<ReservationModel, Integer> resResIdColumn, resMediaIdColumn, resUserIdColumn, resQueueColumn;
    @FXML
    public TableColumn<ReservationModel, Date> resResDateColumn;
    @FXML
    public TextField searchTextField;
    @FXML
    public Button loanButton, reserveButton, finishButton;

    public ObservableList<MediaModel> mediaModelObservableList = FXCollections.observableArrayList();
    @SuppressWarnings("unused")
    public ObservableList<LoanModel> loanModelObservableList = FXCollections.observableArrayList();
    @SuppressWarnings("unused")
    public ObservableList<ReservationModel> reservationModelObservableList = FXCollections.observableArrayList();

    private UserModel activeUser;
    private LoanModel activeLoan;
    private ReservationModel activeReservation;

    private final String fetchLoan = "";
    private String userid, loanid, reservationid;

//  accepts a person to initialize the view
    public void initData(UserModel user, LoanModel loan, ReservationModel reservation)    {
        activeUser = user;
        userid = String.valueOf(user.getUserid());
        activeLoan = loan;
        loanid = String.valueOf(loan.getLoanid());
        activeReservation = reservation;
        reservationid = String.valueOf(reservation.getReservationid());

    }
    Connection connection = null;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


        PreparedStatement psFetchArticles = null, psFetchLoans = null, psFetchReservations = null;
        ResultSet resultSetSearch = null, resultSetLoan = null, resultSetReservation = null;
        try {
            connection = DBUtils.getDBLink();
            psFetchArticles = connection.prepareStatement("SELECT mediaid, title, format, category, description, publisher, edition, author, isbn, director, actor, country, rating, available FROM media;");
            resultSetSearch = psFetchArticles.executeQuery();
            while (resultSetSearch.next()) {
                Integer queryMediaId = resultSetSearch.getInt("mediaid");
                String queryTitle = resultSetSearch.getString("title");
                String queryFormat = resultSetSearch.getString("format");
                String queryCategory = resultSetSearch.getString("category");
                String queryDescription = resultSetSearch.getString("description");
                String queryPublisher = resultSetSearch.getString("publisher");
                String queryEdition = resultSetSearch.getString("edition");
                String queryAuthor = resultSetSearch.getString("author");
                String queryIsbn = resultSetSearch.getString("isbn");
                String queryDirector = resultSetSearch.getString("director");
                String queryActor = resultSetSearch.getString("actor");
                String queryCountry = resultSetSearch.getString("country");
                String queryRating = resultSetSearch.getString("rating");
                String queryAvailable = resultSetSearch.getString("available");
//              populates the observable list
                mediaModelObservableList.add(new MediaModel(                queryMediaId,
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
//              PropertyValueFactory corresponds to the new BookSearchModel
//              populate the tableview columns
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
//              initialize filtered list for interactive search
                FilteredList<MediaModel> filteredData = new FilteredList<>(mediaModelObservableList, b -> true);
                searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
                    filteredData.setPredicate(mediaModel -> {
//                  if no search value is present, all records, or all current records will be displayed
                        if (newValue.isEmpty() || newValue.isBlank() || newValue == null) {
                            return true;
                        }
                        String searchKeyWord = newValue.toLowerCase();
//                      an index > 0 means a match has been found
//                      to return Integer type, use toString() method
                        if (mediaModel.getTitle().toLowerCase().indexOf(searchKeyWord) > -1)  {
//                      match in book title etc.
                            return true;
                        }else if (mediaModel.getFormat().toLowerCase().indexOf(searchKeyWord) > -1)   {
                            return true;
                        }else if (mediaModel.getCategory().toLowerCase().indexOf(searchKeyWord) > -1) {
                            return true;
                        }else if (mediaModel.getDescription().toLowerCase().indexOf(searchKeyWord) > -1)  {
                            return true;
                        }else if (mediaModel.getPublisher().toLowerCase().indexOf(searchKeyWord) > -1){
                            return true;
                        }else if (mediaModel.getEdition().toLowerCase().indexOf(searchKeyWord) > -1){
                            return true;
                        }else if (mediaModel.getAuthor().toLowerCase().indexOf(searchKeyWord) > -1)   {
                            return true;
                        }else if (mediaModel.getIsbn().toLowerCase().indexOf(searchKeyWord) > -1)     {
                            return true;
                        }else if(mediaModel.getDirector().toLowerCase().indexOf(searchKeyWord) > -1)  {
                            return true;
                        }else if (mediaModel.getActor().toLowerCase().indexOf(searchKeyWord) > -1)    {
                            return true;
                        }else if (mediaModel.getCountry().toLowerCase().indexOf(searchKeyWord) > -1)  {
                            return true;
                        }else if (mediaModel.getRating().toLowerCase().indexOf(searchKeyWord) > -1)   {
                            return true;
                        }else if (mediaModel.getAvailable().toLowerCase().indexOf(searchKeyWord) > -1)   {
                            return true;
                        }else
                            return false;
//                      return false = no match found in database
                    });
                });
//              bind sorted result with table view
                SortedList<MediaModel> sortedData = new SortedList<>(filteredData);
                sortedData.comparatorProperty().bind(searchTableView.comparatorProperty());
//              apply filtered and sorted data to the table view
                searchTableView.setItems(sortedData);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        try {
            connection = DBUtils.getDBLink();
            psFetchLoans = connection.prepareStatement("SELECT loanid, mediaid, userid, loandate, returndate FROM loan WHERE userid = ?;");
            psFetchLoans.setInt(1, Integer.parseInt(userid));
            resultSetLoan = psFetchLoans.executeQuery();
            while (resultSetLoan.next()) {
                Integer queryLoanID = resultSetLoan.getInt("loanid");
                Integer queryMediaId = resultSetLoan.getInt("mediaid");
                Integer queryUserId = resultSetLoan.getInt("userid");
                Date queryLoanDate = resultSetLoan.getDate("loandate");
                Date queryReturnDate = resultSetLoan.getDate("returndate");
//              populates the observable list
                loanModelObservableList.add(new LoanModel(      queryLoanID,
                                                                queryMediaId,
                                                                queryUserId,
                                                                queryLoanDate,
                                                                queryReturnDate
                                                                ));
                loanLoanIdColumn.setCellValueFactory((new PropertyValueFactory<>("loanid")));
                loanMediaIdColumn.setCellValueFactory((new PropertyValueFactory<>("mediaid")));
                loanUserIdColumn.setCellValueFactory((new PropertyValueFactory<>("userid")));
                loanLoanDateColumn.setCellValueFactory((new PropertyValueFactory<>("loandate")));
                loanReturnDateColumn.setCellValueFactory((new PropertyValueFactory<>("returndate")));

                loanTableView.setItems((loanModelObservableList));

                FilteredList<LoanModel> filteredData = new FilteredList<>(loanModelObservableList, b -> true);
//

            }
        }catch (SQLException el) {
            el.printStackTrace();
            el.getCause();
        }
        }finally    {
            if (resultSetSearch != null) {
                try {
                    resultSetSearch.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (psFetchArticles != null) {
                try {
                    psFetchArticles.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        loanButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // TODO retrieve information from search tableview and create loan object/DB record


            }
        });
        reserveButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // TODO retrieve information from search tableview and create reservation object/DB record
            }
        });
        finishButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DBUtils.changeScene(event, "login.fxml", "D0024E Bibliotekssystem - Inloggad ");
            }
        });
    }
    public void buildData() {

    }
}
