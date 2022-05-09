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
    public TableView<MediaSearchModel> searchTableView;
    @FXML
    public TableColumn<MediaSearchModel, Integer>   mediaIdColumn;
    @FXML
    public TableColumn<MediaSearchModel, String>    titleColumn, formatColumn, categoryColumn, descriptionColumn,
                                                    publisherColumn, editionColumn, authorColumn, isbnColumn,
                                                    directorColumn, actorColumn, countryColumn, ratingColumn,
                                                    availableColumn;
    @FXML
    public TextField searchTextField;
    @FXML
    public Button loginButton;

    public ObservableList<MediaSearchModel> mediaSearchModelObservableList = FXCollections.observableArrayList();
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
                mediaSearchModelObservableList.add(new MediaSearchModel(    queryMediaId,
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
                        }else if (mediaSearchModel.getEdition().toLowerCase().indexOf(searchKeyWord) > -1){
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
                        }else if (mediaSearchModel.getAvailable().toLowerCase().indexOf(searchKeyWord) > -1)   {
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
    public class MediaSearchModel {
        Integer mediaid;
        String  title, format, category, description,
                publisher, edition, author, isbn,
                director, actor, country, rating,
                available;
        public MediaSearchModel(Integer mediaid,
                                String title, String format, String category, String description,
                                String publisher, String edition, String author, String isbn,
                                String director, String actor, String country, String rating,
                                String available) {
            this.mediaid = mediaid;
            this.title = title;
            this.format = format;
            this.category = category;
            this.description = description;
            this.publisher = publisher;
            this.edition = edition;
            this.author = author;
            this.isbn = isbn;
            this.director = director;
            this.actor = actor;
            this.country = country;
            this.rating = rating;
            this.available = available;
        }
        public Integer getMediaid() {
            return mediaid;
        }
        public String getTitle() {
            return title;
        }
        public String getFormat() {
            return format;
        }
        public String getCategory() {
            return category;
        }
        public String getDescription() {
            return description;
        }
        public String getPublisher() {
            return publisher;
        }
        public String getEdition(){return edition;}
        public String getAuthor() {
            return author;
        }
        public String getIsbn() {
            return isbn;
        }
        public String getDirector() {
            return director;
        }
        public String getActor() {
            return actor;
        }
        public String getCountry() {
            return country;
        }
        public String getRating() {
            return rating;
        }
        public String getAvailable() {
            return available;
        }
        public void setMediaid(Integer mediaid) {
            this.mediaid = mediaid;
        }
        public void setTitle(String title) {
            this.title = title;
        }
        public void setFormat(String format) {
            this.format = format;
        }
        public void setCategory(String category) {
            this.category = category;
        }
        public void setDescription(String description) {
            this.description = description;
        }
        public void setPublisher(String publisher) {
            this.publisher = publisher;
        }
        public void setEdition(String edition){this.edition=edition;}
        public void setAuthor(String author) {
            this.author = author;
        }
        public void setIsbn(String isbn) {
            this.isbn = isbn;
        }
        public void setDirector(String director) {
            this.director = director;
        }
        public void setActor(String actor) {
            this.actor = actor;
        }
        public void setCountry(String country) {
            this.country = country;
        }
        public void setRating(String rating) {
            this.rating = rating;
        }
        public void setAvailable(String available) {
            this.available = available;
        }
    }
}
