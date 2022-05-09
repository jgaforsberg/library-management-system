package com.lms.librarymanagementsystem;

import javafx.fxml.Initializable;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

//  TODO class for adding, editing, and deleting articles from DB
public class InventoryController implements Initializable {

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
    }
}
