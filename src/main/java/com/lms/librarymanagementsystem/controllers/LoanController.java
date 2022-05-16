package com.lms.librarymanagementsystem.controllers;
//  #011B3E blue
//  #F0F0F0 light gray
import com.lms.librarymanagementsystem.Constants;
import com.lms.librarymanagementsystem.DBUtils;
import com.lms.librarymanagementsystem.models.LoanModel;
import com.lms.librarymanagementsystem.models.MediaModel;
import com.lms.librarymanagementsystem.models.ReservationModel;
import com.lms.librarymanagementsystem.models.UserModel;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
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

@SuppressWarnings({"IndexOfReplaceableByContains", "ConstantConditions", "Convert2Diamond", "Convert2Lambda", "CodeBlock2Expr", "RedundantIfStatement", "DuplicatedCode"})
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
    @SuppressWarnings("unused")
    private ObservableList<LoanModel> loanModelObservableList = FXCollections.observableArrayList();
    @SuppressWarnings("unused")
    private ObservableList<ReservationModel> reservationModelObservableList = FXCollections.observableArrayList();

    private UserModel activeUser;
    private LoanModel activeLoan;
    private ReservationModel activeReservation;

    private final String fetchLoan = "";
//  since both loan() and reservation() needs userid for their PreparedStatements, userid is declared as a Class variable
    private Integer userid, loanid, reservationid;

//  accepts a person to initialize the view
/*
        public void initData(UserModel user, LoanModel loan, ReservationModel reservation)    {
        activeUser = user;
        userid = String.valueOf(user.getUserid());
        activeLoan = loan;
        loanid = String.valueOf(loan.getLoanid());
        activeReservation = reservation;
        reservationid = String.valueOf(reservation.getReservationid());

    }
*/
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        search();
        loanButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // TODO retrieve information from search tableview and create loan object/DB record
                extractArticle();
                refreshSearch();
                search();
                if(loanModelObservableList != null) {
                    refreshLoan();
                    loan();
                }
            }
        });
        reserveButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // TODO retrieve information from search tableview and create reservation object/DB record
                refreshSearch();
                search();
                if(reservationModelObservableList != null) {
                    refreshReservation();
                    reservation();
               }else System.out.println("Reservationer ej laddade.");
            }
        });
        finishButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DBUtils.changeSceneLogin(event, Constants.LOGIN, Constants.LOGIN_TITLE, nameLabel.getText());
            }
        });
    }

    private void extractArticle() {

       /* searchTableView.getSelectionModel().setCellSelectionEnabled(true);
        ObservableList selectedCells = searchTableView.getSelectionModel().getSelectedCells();

        selectedCells.addListener(new ListChangeListener() {
            @Override
            public void onChanged(Change c) {
                TablePosition tablePosition = (TablePosition) selectedCells.get(0);
                Object val = tablePosition.getTableColumn().getCellData(tablePosition.getRow());
                System.out.println("Selected Value" + val);

            }
        });  */
//      TODO extract necessary information and pass on to loan object / reservation object
        MediaModel mediaModel = searchTableView.getSelectionModel().getSelectedItem();
        System.out.println(mediaModel.titleProperty().toString());

    }
    public void setUserInformation(String username){
        nameLabel.setText(username);
        setUserId();
    }
    public void setUserId()    {
        String username = nameLabel.getText();
        if(!username.isEmpty()) {
            Connection connection = null;
            PreparedStatement psFetchUserId = null;
            ResultSet resultSet = null;
            try {
                connection = DBUtils.getDBLink();
                psFetchUserId = connection.prepareStatement("SELECT id FROM users WHERE username = ?;");
                psFetchUserId.setString(1, username);
                resultSet = psFetchUserId.executeQuery();
                while (resultSet.next()) {
                    userid = resultSet.getInt("id");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                e.getCause();
            } finally {
                if (psFetchUserId != null) {
                    try {
                        psFetchUserId.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                if (resultSet != null) {
                    try {
                        resultSet.close();
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
        }
        System.out.println(userid);
    }
    public String getUsername(){return nameLabel.getText();}
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
//              populates the observable list
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
                        if (mediaModel.titleProperty().toString().toLowerCase().indexOf(searchKeyWord) > -1)  {
//                      match in book title etc.
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
//                      return false = no match found in database
                    });
                });
//              bind sorted result with table view
                SortedList<MediaModel> sortedData = new SortedList<>(filteredData);
                sortedData.comparatorProperty().bind(searchTableView.comparatorProperty());
//              apply filtered and sorted data to the table view
                searchTableView.setItems(sortedData);
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }finally    {
            if (resultSet != null) {
                try {
                    resultSet.close();
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
    }
    private void reservation() {
        Connection connection = null;
        PreparedStatement psFetchReservations = null;
        ResultSet resultSet = null;
        try{
            connection = DBUtils.getDBLink();
            psFetchReservations = connection.prepareStatement("SELECT reservationid, mediaid, userid, queuenumber, reservationdate FROM reservation WHERE userid = ?;");
            psFetchReservations.setInt(1, userid);
            resultSet = psFetchReservations.executeQuery();
            while (resultSet.next()) {
                Integer reservationid = resultSet.getInt("reservationid");
                Integer mediaid = resultSet.getInt("mediaid");
                Integer userid = resultSet.getInt("userid");
                Integer queuenumber = resultSet.getInt("queuenumber");
                Date reservationdate = resultSet.getDate("reservationdate");
                reservationModelObservableList.add(new ReservationModel(reservationid,
                                                                        mediaid,
                                                                        userid,
                                                                        queuenumber,
                                                                        reservationdate
                                                                        ));
                resResIdColumn.setCellValueFactory((new PropertyValueFactory<>("reservationid")));
                resMediaIdColumn.setCellValueFactory((new PropertyValueFactory<>("mediaid")));
                resUserIdColumn.setCellValueFactory((new PropertyValueFactory<>("userid")));
                resQueueColumn.setCellValueFactory((new PropertyValueFactory<>("queuenumber")));
                resResDateColumn.setCellValueFactory((new PropertyValueFactory<>("reservationdate")));
                reserveTableView.setItems((reservationModelObservableList));
            }

        } catch (SQLException e) {
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
            if (psFetchReservations != null) {
                try {
                    psFetchReservations.close();
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
    private void loan() {
        Connection connection = null;
        PreparedStatement psFetchLoans = null;
        ResultSet resultSet = null;
        try {
            connection = DBUtils.getDBLink();
            psFetchLoans = connection.prepareStatement("SELECT loanid, mediaid, userid, loandate, returndate, returned FROM loan WHERE userid = ?;");
            psFetchLoans.setInt(1, userid);
            resultSet = psFetchLoans.executeQuery();
            while (resultSet.next()) {
                Integer loanid = resultSet.getInt("loanid");
                Integer mediaid = resultSet.getInt("mediaid");
                Integer userid = resultSet.getInt("userid");
                Date loandate = resultSet.getDate("loandate");
                Date returndate = resultSet.getDate("returndate");
                Integer returned = resultSet.getInt("returned");
//              populates the observable list
                loanModelObservableList.add(new LoanModel(  loanid,
                                                            mediaid,
                                                            userid,
                                                            loandate,
                                                            returndate,
                                                            returned
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
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (psFetchLoans != null) {
                try {
                    psFetchLoans.close();
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
}
