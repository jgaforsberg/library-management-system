package com.lms.librarymanagementsystem.controllers;
//  #011B3E blue
//  #F0F0F0 light gray
import com.lms.librarymanagementsystem.Constants;
import com.lms.librarymanagementsystem.DBUtils;
import com.lms.librarymanagementsystem.models.MediaModel;
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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

// INSERT statement
// INSERT INTO media(title, format, category, description, publisher, edition, author, isbn, director, actor, country, rating, available) VALUES('title', 'format', 'category', 'description', 'publisher', 'edition', 'author', 'isbn', 'director', 'actor', 'country', 'rating', 'available');
// UPDATE statement
// UPDATE media SET mediaid = mediaid, title = 'title', format = 'format', category = 'category', description = 'description', publisher = 'publisher', edition = 'edition', author = 'author', isbn = 'isbn', director = 'director', actor = 'actor', country = 'country', rating = 'rating', available = 'available' WHERE mediaid = mediaid;
// DELETE statement
// DELETE FROM media WHERE mediaid = mediaid;
public class InventoryController implements Initializable {
    @FXML
    private TextField   mediaIdTextField, titleTextField, categoryTextField, descriptionTextField,
                        editionTextField, publisherTextField, authorTextField, isbnTextField,
                        directorTextField, actorTextField, countryTextField, ratingTextField;
    @FXML
    private ChoiceBox<String> formatChoiceBox, availableChoiceBox;
    @FXML
    private Button  addMediaButton, updateMediaButton, removeMediaButton, finishButton;
    @FXML
    private Label   messageLabel;
    @FXML
    private TableView<MediaModel> searchTableView;
    @FXML
    private TableColumn<MediaModel, Integer> mediaIdColumn;
    @FXML
    private TableColumn<MediaModel, String>  titleColumn, formatColumn, categoryColumn, descriptionColumn,
                                            publisherColumn, editionColumn, authorColumn, isbnColumn,
                                            directorColumn, actorColumn, countryColumn, ratingColumn,
                                            availableColumn;
    @FXML
    private TextField searchTextField;
    @FXML
    private Label nameLabel;

    private final ObservableList<String> format = FXCollections.observableArrayList("Bok", "Film", "Journal");
    private final ObservableList<String> available = FXCollections.observableArrayList("Referens", "Ledig");
    private final ObservableList<MediaModel> mediaModelObservableList = FXCollections.observableArrayList();

    private Integer userid;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        formatChoiceBox.getItems().addAll(format);
        availableChoiceBox.getItems().addAll(available);
        search();

        addMediaButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DBUtils.addMedia(   titleTextField.getText(), formatChoiceBox.getValue(), categoryTextField.getText(), descriptionTextField.getText(),
                                    publisherTextField.getText(),editionTextField.getText(), authorTextField.getText(), isbnTextField.getText(),
                                    directorTextField.getText(), actorTextField.getText(), countryTextField.getText(), ratingTextField.getText(),
                                    availableChoiceBox.getValue());
                setActionInformation("Media tillagd ");
                refreshSearch();
                search();
            }
        });
        updateMediaButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DBUtils.updateMedia(Integer.valueOf(mediaIdTextField.getText()), titleTextField.getText(), formatChoiceBox.getValue(), categoryTextField.getText(), descriptionTextField.getText(),
                                                    publisherTextField.getText(),editionTextField.getText(), authorTextField.getText(), isbnTextField.getText(),
                                                    directorTextField.getText(), actorTextField.getText(), countryTextField.getText(), ratingTextField.getText(),
                                                    availableChoiceBox.getValue());
                setActionInformation("Media uppdaterad ");
                refreshSearch();
                search();
            }
        });
        removeMediaButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DBUtils.removeMedia(Integer.valueOf(mediaIdTextField.getText()));
                setActionInformation("Media borttagen ");
                refreshSearch();
                search();
            }
        });
        finishButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DBUtils.changeSceneLogin(event, Constants.LOGIN, Constants.LOGIN_TITLE, nameLabel.getText());
            }
        });
    }
    private void refreshSearch() {mediaModelObservableList.clear();}
    private void setActionInformation(String actionInformation){ messageLabel.setText(actionInformation);}
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

    public void setUserInformation(String username) {
        //this.userid = DBUtils.getUserId(username);
        nameLabel.setText(username);
    }
}
