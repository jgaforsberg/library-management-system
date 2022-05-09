package com.lms.librarymanagementsystem;
//  #011B3E blue
//  #F0F0F0 light gray
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

public class SearchController implements Initializable {
    @FXML
    public TableView<MediaModel> searchTableView;
    @FXML
    public TableColumn<MediaModel, Integer>   mediaIdColumn;
    @FXML
    public TableColumn<MediaModel, String>    titleColumn, formatColumn, categoryColumn, descriptionColumn,
                                                    publisherColumn, editionColumn, authorColumn, isbnColumn,
                                                    directorColumn, actorColumn, countryColumn, ratingColumn,
                                                    availableColumn;
    @FXML
    public TextField searchTextField;
    @FXML
    public Button loginButton;

    public ObservableList<MediaModel> mediaModelObservableList = FXCollections.observableArrayList();
//  Database values can't be NULL for the SearchController class to function, empty strings like '' must be used
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        DBUtils getDBLink = new DBUtils();
        Connection connection = null;
        PreparedStatement psFetchArticles = null;
        ResultSet resultSet = null;
        try {
            connection = DBUtils.getDBLink();
            psFetchArticles = connection.prepareStatement("SELECT mediaid, title, format, category, description, publisher, edition, author, isbn, director, actor, country, rating, available FROM media;");
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
                mediaModelObservableList.add(new MediaModel(    queryMediaId,
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
        loginButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DBUtils.changeScene(event, "main.fxml", "D0024E Bibliotekssystem - Välkommen! ");
            }
        });
    }
}
