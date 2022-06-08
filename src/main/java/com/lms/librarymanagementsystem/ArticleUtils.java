package com.lms.librarymanagementsystem;

import javafx.scene.control.Alert;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ArticleUtils extends DBUtils{
    //  Add new media to database
    public static void addMedia(String title, String format, String category, String description,
                                String publisher, String edition, String author, String isbn,
                                String director, String actor, String country, String rating,
                                String available)   {
        Integer mediaid = null;
        Connection connection = null;
        PreparedStatement psInsert = null;
//      may be unnecessary variable
        PreparedStatement psCheckMediaDuplicate = null;
        ResultSet resultSet = null;
        try{
            connection = getDBLink();
           /* psCheckMediaDuplicate = connection.prepareStatement("SELECT * FROM media WHERE columnname = ?");
            psCheckMediaDuplicate.setString(1, columname);
            resultSet = psCheckMediaDuplicate.executeQuery(); */
            psInsert = connection.prepareStatement( "INSERT INTO media(title,format,category,description," +
                    "publisher,edition,author,isbn," +
                    "director,actor,country,rating,available)" +
                    "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)");
            psInsert.setString(1, title);
            psInsert.setString(2, format);
            psInsert.setString(3, category);
            psInsert.setString(4, description);
            psInsert.setString(5, publisher);
            psInsert.setString(6, edition);
            psInsert.setString(7, author);
            psInsert.setString(8, isbn);
            psInsert.setString(9, director);
            psInsert.setString(10, actor);
            psInsert.setString(11, country);
            psInsert.setString(12, rating);
            psInsert.setString(13, available);
            psInsert.executeUpdate();
            System.out.println("Mediaartikel tillagd! ");
            psInsert = connection.prepareStatement("SELECT mediaid FROM media ORDER BY mediaid DESC LIMIT 1;");
            resultSet = psInsert.executeQuery();
            while (resultSet.next())    {
                mediaid = resultSet.getInt("mediaid");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            closeDBLink(connection, psInsert, resultSet);
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText("Media med mediaid: "+mediaid+" är uppdaterad. ");
        alert.show();
    }
    //  Updates existing media in database
    public static void updateMedia(Integer mediaid, String title, String format, String category, String description,
                                   String publisher, String edition, String author, String isbn,
                                   String director, String actor, String country, String rating,
                                   String available)    {
        Connection connection = null;
        PreparedStatement psUpdate = null;
        PreparedStatement psCheckMediaExists = null;
        ResultSet resultSet = null;
        try {
            connection = getDBLink();
            psCheckMediaExists = connection.prepareStatement("SELECT mediaid FROM media WHERE mediaid = ?;");
            psCheckMediaExists.setInt(1, mediaid);
            resultSet = psCheckMediaExists.executeQuery();
            if(!resultSet.isBeforeFirst()) {
                System.out.println("Artikel med angett artikelnummer finns ej i databasen! ");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Kontrollera att artikelnumret är rätt och försök igen. ");
                alert.show();
            }else {
                psUpdate = connection.prepareStatement( "UPDATE media SET title = ?,format = ?,category = ?,description = ?," +
                        "publisher = ?,edition = ?,author = ?,isbn = ?," +
                        "director = ?,actor = ?,country = ?,rating = ?,available = ?" +
                        "WHERE mediaid = ?;");
                psUpdate.setString(1, title);
                psUpdate.setString(2, format);
                psUpdate.setString(3, category);
                psUpdate.setString(4, description);
                psUpdate.setString(5, publisher);
                psUpdate.setString(6, edition);
                psUpdate.setString(7, author);
                psUpdate.setString(8, isbn);
                psUpdate.setString(9, director);
                psUpdate.setString(10, actor);
                psUpdate.setString(11, country);
                psUpdate.setString(12, rating);
                psUpdate.setString(13, available);
                psUpdate.setInt(14, mediaid);
                psUpdate.executeUpdate();
                System.out.println("Mediaartikel uppdaterad! ");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            e.getCause();
        }finally {
            closeDBLink(connection, psUpdate, psCheckMediaExists, resultSet);
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText("Media med mediaid: "+mediaid+" är uppdaterad. ");
        alert.show();
    }
    //  Delete media from database
    public static void removeMedia(Integer mediaid)    {
        Connection connection = null;
        PreparedStatement psRemove = null;
        PreparedStatement psCheckMediaExists = null;
        ResultSet resultSet = null;
        try {
            connection = getDBLink();
            psCheckMediaExists = connection.prepareStatement("SELECT mediaid FROM media WHERE mediaid = ?;");
            psCheckMediaExists.setInt(1, mediaid);
            resultSet = psCheckMediaExists.executeQuery();
            if(!resultSet.isBeforeFirst()) {
                System.out.println("Artikel med angett artikelnummer finns ej i databasen! ");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Kontrollera att artikelnumret är rätt och försök igen. ");
                alert.show();
            }else {
                psRemove = connection.prepareStatement("DELETE FROM media WHERE mediaid = ?;");
                psRemove.setInt(1, mediaid);
                psRemove.executeUpdate();
                System.out.println("Mediaartikel borttagen! ");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            e.getCause();
        }finally {
            closeDBLink(connection, psRemove, psCheckMediaExists, resultSet);
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText("Media med mediaid: "+mediaid+" är borttagen ur databasen. ");
        alert.show();
    }
    // Sets a returned media to available "Ledig" status when a loan is returned
    protected static void setAvailable(Integer mediaid)   {
        Connection connection = null;
        PreparedStatement psUpdate = null;
        try{
            connection = getDBLink();
            psUpdate = connection.prepareStatement("UPDATE media SET available = 'Ledig' WHERE mediaid = ?");
            psUpdate.setInt(1, mediaid);
            psUpdate.executeUpdate();
            System.out.println("Media med mediaid: "+mediaid+" satt till status 'Ledig'! ");
        }catch (SQLException e) {
            e.printStackTrace();
            e.getCause();
        }finally {
            closeDBLink(connection, psUpdate);
        }
    }
    //  Sets a media article as unavailable ("Utlånad") when a new loan is created
    protected static void setUnavailable(Integer mediaid) {
        Connection connection = null;
        PreparedStatement psUpdate = null;
        ResultSet resultSet = null;
        try {
            connection = getDBLink();
            psUpdate = connection.prepareStatement("UPDATE media SET available = 'Utlånad' WHERE mediaid = ?");
            psUpdate.setInt(1, mediaid);
            psUpdate.executeUpdate();
            System.out.println("Mediatillgänglighet för mediaartikel "+mediaid+" satt till status utlånad! ");
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            closeDBLink(connection, psUpdate, resultSet);
        }
    }
    //  Returns the queuenumber for a reserved media article, used for updating queuenumber and adding loans
    protected static Integer getQueuenumber(Integer mediaid)  {
        Integer queueNumber = null;
        Connection connection = null;
        PreparedStatement psCheckQueue = null;
        ResultSet resultSet = null;
        try {
            connection = getDBLink();
            psCheckQueue = connection.prepareStatement("SELECT COUNT(*) AS rowcount FROM reservation WHERE mediaid = ?;");
            psCheckQueue.setInt(1, mediaid);
            resultSet = psCheckQueue.executeQuery();
            if (resultSet.next())    {
                queueNumber = resultSet.getInt("rowcount")+1;
            }
        }catch (SQLException e) {
            e.printStackTrace();
            e.getCause();
        }finally {
            closeDBLink(connection, psCheckQueue, resultSet);
        }
        return queueNumber;
    }
    // Returns the value of a media's "available" column in the database
    protected static String checkAvailable(Integer mediaid) {
        String queryAvailable = "";
        Connection connection = null;
        PreparedStatement psCheckAvailable = null;
        ResultSet resultSet = null;
        try {
            connection = getDBLink();
            psCheckAvailable = connection.prepareStatement("SELECT * FROM media WHERE mediaid = ?;");
            psCheckAvailable.setInt(1, mediaid);
            resultSet = psCheckAvailable.executeQuery();
            while (resultSet.next())    {
                queryAvailable = resultSet.getString("available");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            e.getCause();
        }finally {
            closeDBLink(connection, psCheckAvailable, resultSet);
        }
        return queryAvailable;
    }
    // Checks and returns the format of an entered media article
    protected static String checkFormat(Integer mediaid)   {
        String queryFormat = "";
        Connection connection = null;
        PreparedStatement psCheckFormat = null;
        ResultSet resultSet = null;
        try {
            connection = getDBLink();
            psCheckFormat = connection.prepareStatement("SELECT format FROM media WHERE mediaid = ?;");
            psCheckFormat.setInt(1, mediaid);
            resultSet = psCheckFormat.executeQuery();
            while (resultSet.next())    {
                queryFormat = resultSet.getString("format");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            e.getCause();
        }finally {
            closeDBLink(connection, psCheckFormat, resultSet);
        }
        return queryFormat;
    }
}
