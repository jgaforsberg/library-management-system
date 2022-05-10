package com.lms.librarymanagementsystem.controllers;
//  #011B3E blue
//  #F0F0F0 light gray
import com.lms.librarymanagementsystem.DBUtils;
import com.lms.librarymanagementsystem.models.MediaModel;
import com.lms.librarymanagementsystem.models.UserModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.w3c.dom.Text;

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

/*
public class InventoryController implements Initializable {
    @FXML
    private TextField   mediaidTextField, titleTextField, formatTextField, categoryTextField, descriptionTextField,
                        editionTextField, publisherTextField, authorTextField, isbnTextField,
                        directorTextField, actorTextField, countryTextField, ratingTextField,
                        availableTextField;
    @FXML
    private Button  addMediaButton, updateMediaButton, removeMediaButton, finishButton;
    @FXML
    private Label   messageLabel;
    @FXML
    public TableView<MediaModel> mediaTableView;
    @FXML
    public TableColumn<MediaModel, Integer> mediaIdColumn;
    @FXML
    public TableColumn<MediaModel, String>  titleColumn, formatColumn, categoryColumn, descriptionColumn,
                                            publisherColumn, editionColumn, authorColumn, isbnColumn,
                                            directorColumn, actorColumn, countryColumn, ratingColumn,
                                            availableColumn;

    public ObservableList<MediaModel> mediaModelObservableList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        DBUtils getDBLink = new DBUtils();
        Connection connection = null;
        try {
            connection = DBUtils.getDBLink();
            PreparedStatement psFetchArticles = connection.prepareStatement("SELECT mediaid, title, format, category, description, publisher, edition, author, isbn, director, actor, country, rating, available FROM media;");
            ResultSet resultSet = psFetchArticles.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            e.getCause();
        }

        public static void addMedia(ActionEvent event, Integer mediaid, String title, String format, String category, String description, String publisher, String edition, String author, String isbn, String director, String actor, String country, String rating, String available) {
            Connection connection = null;
            PreparedStatement psAddMedia = null;
            ResultSet resultSet = null;
            try {
                connection = getDBLink();
                psAddMedia = connection.prepareStatement("INSERT INTO media(title, format, category, description, publisher, edition, author, isbn, director, actor, country, rating, available) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?);");

                resultSet = psAddMedia.executeQuery();
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

              /*  mediaModelObservableList.add(new MediaModel(                queryMediaId,
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
                        queryAvailable)); */
               /* }
            }catch(SQLException e){
                e.printStackTrace();
                e.getCause();
            }finally {
                try {
                    psAddMedia.close();
                }catch (SQLException e){
                    e.printStackTrace();
                    e.getCause();
                }
            }
        }
        public static void updateMedia(ActionEvent event, Integer mediaid, String title, String format, String category, String description, String publisher, String edition, String author, String isbn, String director, String actor, String country, String rating, String available) {
            Connection connection = null;
            PreparedStatement psAddMedia = null;
            ResultSet resultSet = null;
            try {
                connection = getDBLink();


            }catch (SQLException e) {
                e.printStackTrace();
                e.getCause();
            }
        }
        public static void removeMedia(ActionEvent event, Integer mediaid, String title, String format, String category, String description, String publisher, String edition, String author, String isbn, String director, String actor, String country, String rating, String available) {
            Connection connection = null;
            PreparedStatement psAddMedia = null;
            ResultSet resultSet = null;
            try {
                connection = getDBLink();


            }catch (SQLException e) {
                e.printStackTrace();
                e.getCause();
            }
        }
        addMediaButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DBUtils.addMedia( event, Integer.valueOf(mediaidTextField.getText()), titleTextField.getText(), formatTextField.getText(), categoryTextField.getText(), descriptionTextField.getText(),
                                editionTextField.getText(), publisherTextField.getText(), authorTextField.getText(), isbnTextField.getText(),
                                directorTextField.getText(), actorTextField.getText(), countryTextField.getText(), ratingTextField.getText(),
                                availableTextField.getText());
                setActionInformation("Media tillagd ");
            }
        });
        updateMediaButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                setActionInformation("Media uppdaterad ");
            }
        });
        removeMediaButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                setActionInformation("Media borttagen ");
            }
        });
//      finish button triggers changes to CRUD the database
        finishButton.setOnAction(new EventHandler<ActionEvent>() {
            UserModel user = new UserModel("");
            String username = user.getUsername();
            @Override
            public void handle(ActionEvent event) {
                DBUtils.changeScene(event, "login.fxml", "D0024E Bibliotekssystem - Inloggad", username);
            }
        });


    }
    public void setActionInformation(String actionInformation){ messageLabel.setText(actionInformation);}
}
*/