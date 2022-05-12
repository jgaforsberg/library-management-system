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

//  TODO class for adding, editing, and deleting articles from DB
//  TODO implement same interactive search from search controller ???
// INSERT statement
// INSERT INTO media(title, format, category, description, publisher, edition, author, isbn, director, actor, country, rating, available) VALUES('title', 'format', 'category', 'description', 'publisher', 'edition', 'author', 'isbn', 'director', 'actor', 'country', 'rating', 'available');
// UPDATE statement
// UPDATE media SET mediaid = mediaid, title = 'title', format = 'format', category = 'category', description = 'description', publisher = 'publisher', edition = 'edition', author = 'author', isbn = 'isbn', director = 'director', actor = 'actor', country = 'country', rating = 'rating', available = 'available' WHERE mediaid = mediaid;
// DELETE statement
// DELETE FROM media WHERE mediaid = mediaid;
public class InventoryController implements Initializable {
    @FXML
    private TextField   mediaIdTextField, titleTextField, formatTextField, categoryTextField, descriptionTextField,
                        editionTextField, publisherTextField, authorTextField, isbnTextField,
                        directorTextField, actorTextField, countryTextField, ratingTextField,
                        availableTextField;
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

    private final ObservableList<String> format = FXCollections.observableArrayList("bok", "film", "journal");
    private final ObservableList<String> available = FXCollections.observableArrayList("referens", "ledig");
    private final ObservableList<MediaModel> mediaModelObservableList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        formatChoiceBox.getItems().addAll(format);
        availableChoiceBox.getItems().addAll(available);

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
                        if (mediaModel.getTitle().toLowerCase().indexOf(searchKeyWord) > -1) {
//                      match in book title etc.
                            return true;
                        } else if (mediaModel.getFormat().toLowerCase().indexOf(searchKeyWord) > -1) {
                            return true;
                        } else if (mediaModel.getCategory().toLowerCase().indexOf(searchKeyWord) > -1) {
                            return true;
                        } else if (mediaModel.getDescription().toLowerCase().indexOf(searchKeyWord) > -1) {
                            return true;
                        } else if (mediaModel.getPublisher().toLowerCase().indexOf(searchKeyWord) > -1) {
                            return true;
                        } else if (mediaModel.getEdition().toLowerCase().indexOf(searchKeyWord) > -1) {
                            return true;
                        } else if (mediaModel.getAuthor().toLowerCase().indexOf(searchKeyWord) > -1) {
                            return true;
                        } else if (mediaModel.getIsbn().toLowerCase().indexOf(searchKeyWord) > -1) {
                            return true;
                        } else if (mediaModel.getDirector().toLowerCase().indexOf(searchKeyWord) > -1) {
                            return true;
                        } else if (mediaModel.getActor().toLowerCase().indexOf(searchKeyWord) > -1) {
                            return true;
                        } else if (mediaModel.getCountry().toLowerCase().indexOf(searchKeyWord) > -1) {
                            return true;
                        } else if (mediaModel.getRating().toLowerCase().indexOf(searchKeyWord) > -1) {
                            return true;
                        } else if (mediaModel.getAvailable().toLowerCase().indexOf(searchKeyWord) > -1) {
                            return true;
                        } else
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

        addMediaButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DBUtils.addMedia(   titleTextField.getText(), formatTextField.getText(), categoryTextField.getText(), descriptionTextField.getText(),
                                    publisherTextField.getText(),editionTextField.getText(), authorTextField.getText(), isbnTextField.getText(),
                                    directorTextField.getText(), actorTextField.getText(), countryTextField.getText(), ratingTextField.getText(),
                                    availableTextField.getText());
                messageLabel.setText("Media tillagd ");

                updateSearch();
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
                updateSearch();
            }
        });
        removeMediaButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DBUtils.removeMedia(Integer.valueOf(mediaIdTextField.getText()));
                setActionInformation("Media borttagen ");
                updateSearch();
            }
        });
//      finish button triggers changes to CRUD the database
        finishButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DBUtils.changeScene(event, Constants.LOGIN, Constants.LOGIN_TITLE);
            }
        });
    }
    public void setActionInformation(String actionInformation){ messageLabel.setText(actionInformation);}
    public void updateSearch()  {
    // TODO reinit search


    }
}
