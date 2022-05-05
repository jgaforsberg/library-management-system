package com.lms.librarymanagementsystem.controllers;


//  #011B3E blue
//  #F0F0F0 light gray


import com.lms.librarymanagementsystem.DBUtils;
import com.lms.librarymanagementsystem.MediaSearchModel;
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

public class SearchController /*implements Initializable */{

    @FXML
    public TableView<MediaSearchModel> searchTableView;
    @FXML
    public TableColumn<MediaSearchModel, Integer>   mediaIdColumn,
                                                    availableColumn;
    @FXML
    public TableColumn<MediaSearchModel, String>    titleColumn,
                                                    formatColumn,
                                                    categoryColumn,
                                                    descriptionColumn,
                                                    publisherColumn,
                                                    authorColumn,
                                                    isbnColumn,
                                                    directorColumn,
                                                    actorColumn,
                                                    countryColumn,
                                                    ratingColumn;
    @FXML
    public TextField searchTextField;
    @FXML
    public Button loginButton;

    public  ObservableList<MediaSearchModel> mediaSearchModelObservableList = FXCollections.observableArrayList();

//  TODO FIX ROOT IS NULL PROBLEM WHEN CHANGING SCENES
    /*@Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Connection connection = null;
        PreparedStatement psFetchArticles = null;
        ResultSet resultSet = null;

        try {
            psFetchArticles = connection.prepareStatement("SELECT mediaid, title, format, category, description, publisher, author, isbn, director, actor, country, rating, available FROM media;");
            resultSet = psFetchArticles.executeQuery();
            while (resultSet.next()) {
                Integer queryMediaId = resultSet.getInt("mediaid");
                String queryTitle = resultSet.getString("title");
                String queryFormat = resultSet.getString("format");
                String queryCategory = resultSet.getString("category");
                String queryDescription = resultSet.getString("description");
                String queryPublisher = resultSet.getString("publisher");
                String queryAuthor = resultSet.getString("author");
                String queryIsbn = resultSet.getString("isbn");
                String queryDirector = resultSet.getString("director");
                String queryActor = resultSet.getString("actor");
                String queryCountry = resultSet.getString("country");
                String queryRating = resultSet.getString("rating");
                Integer queryAvailable = resultSet.getInt("available");
//              populates the observable list
                mediaSearchModelObservableList.add(new MediaSearchModel(
                                                                            queryMediaId,
                                                                            queryTitle,
                                                                            queryFormat,
                                                                            queryCategory,
                                                                            queryDescription,
                                                                            queryPublisher,
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
                authorColumn.setCellValueFactory((new PropertyValueFactory<>("author")));
                isbnColumn.setCellValueFactory((new PropertyValueFactory<>("isbn")));
                directorColumn.setCellValueFactory((new PropertyValueFactory<>("director")));
                actorColumn.setCellValueFactory((new PropertyValueFactory<>("actor")));
                countryColumn.setCellValueFactory((new PropertyValueFactory<>("country")));
                ratingColumn.setCellValueFactory((new PropertyValueFactory<>("rating")));
                availableColumn.setCellValueFactory((new PropertyValueFactory<>("available")));

                searchTableView.setItems(mediaSearchModelObservableList);
//              initialize filtered list for interactive search
                FilteredList<MediaSearchModel> filteredData = new FilteredList<>(mediaSearchModelObservableList, b -> true);
                searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
                    filteredData.setPredicate(mediaSearchModel -> {
//                  if no search value is present, all records, or all current records will be displayed
                    if (newValue.isEmpty() || newValue.isBlank() || newValue == null) {
                        return true;
                    }
                        String searchKeyWord = newValue.toLowerCase();
//                      an index > 0 means a match has been found
//                      to return Integer type, use toString() method
                        if (mediaSearchModel.getTitle().toLowerCase().indexOf(searchKeyWord) > -1)  {
//                      match in book title etc.
                            return true;
                        }else if (mediaSearchModel.getFormat().toLowerCase().indexOf(searchKeyWord) > -1)   {
                            return true;
                        }else if (mediaSearchModel.getCategory().toLowerCase().indexOf(searchKeyWord) > -1) {
                            return true;
                        }else if (mediaSearchModel.getDescription().toLowerCase().indexOf(searchKeyWord) > -1)  {
                            return true;
                        }else if (mediaSearchModel.getPublisher().toLowerCase().indexOf(searchKeyWord) > -1){
                            return true;
                        }else if (mediaSearchModel.getAuthor().toLowerCase().indexOf(searchKeyWord) > -1)   {
                            return true;
                        }else if (mediaSearchModel.getIsbn().toLowerCase().indexOf(searchKeyWord) > -1)     {
                            return true;
                        }else if(mediaSearchModel.getDirector().toLowerCase().indexOf(searchKeyWord) > -1)  {
                            return true;
                        }else if (mediaSearchModel.getActor().toLowerCase().indexOf(searchKeyWord) > -1)    {
                            return true;
                        }else if (mediaSearchModel.getCountry().toLowerCase().indexOf(searchKeyWord) > -1)  {
                            return true;
                        }else if (mediaSearchModel.getRating().toLowerCase().indexOf(searchKeyWord) > -1)   {
                            return true;
                        }else
                            return false;
//                      return false = no match found in database
                    });
                });
//              bind sorted result with table view
                SortedList<MediaSearchModel> sortedData = new SortedList<>(filteredData);
                sortedData.comparatorProperty().bind(searchTableView.comparatorProperty());
//              apply filtered and sorted data to the table view
                searchTableView.setItems(sortedData);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        loginButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DBUtils.changeScene(event, "main.fxml", "D0024E Bibliotekssystem - Välkommen! ");
            }
        });
    }*/
}
