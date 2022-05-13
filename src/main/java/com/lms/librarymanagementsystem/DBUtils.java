package com.lms.librarymanagementsystem;

import com.lms.librarymanagementsystem.controllers.LoginController;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;

import java.sql.*;

//  #011B3E blue
//  #F0F0F0 light gray

// purpose of using prepared statements is increased protection against sql injection, easier to set parameters,
// and to improve application performance due to being precompiled
public class DBUtils {
    private static final String URL = "jdbc:mysql://localhost:3306/javafxtest";
    private static final String USER = "root";
    private static final String PWD = "1234";
    //  for connecting through controller classes for e.g., search function
    public static Connection DBLink;

    public static Connection getDBLink() throws SQLException {
        try{
        Class.forName("com.mysql.cj.jdbc.Driver");
            DBLink = DriverManager.getConnection(URL,USER,PWD);
        }catch(ClassNotFoundException e)    {
            e.printStackTrace();
        }
        return DBLink;
    }
    //  login change scene
    public static void changeScene(ActionEvent event, String fxmlFile, String title, String username)   {
        Parent root = null;
//      make sure username is passed to login scene
        if(username != null)    {
            try {
//              passing fxmlfile allows to choose fxmlfile to pass in as parameter
                FXMLLoader loader = new FXMLLoader(DBUtils.class.getResource(fxmlFile));
                root = loader.load();
                LoginController loginController = loader.getController();
                loginController.setUserInformation(username);
            }   catch(IOException e) {
                e.printStackTrace();
            }
        }   else    {
            try {
                root = FXMLLoader.load(DBUtils.class.getResource(fxmlFile));
            }   catch(IOException e)    {
                e.printStackTrace();
                e.getCause();
            }
        }
//      event is the click of button -> source of the click, scene of source, window of scene
//      stage is the window of the GUI, scene is what is displayed in the window. Stages can have multiple scenes
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setTitle(title);
        stage.setScene((new Scene(root)));
        stage.show();
    }
//  logged in change scene search
    public static void changeScene(ActionEvent event, String fxmlFile, String title) {
        Parent root = null;
        try {
            root = FXMLLoader.load(DBUtils.class.getResource(fxmlFile));
        } catch (IOException e) {
            e.printStackTrace();
            e.getCause();
        }
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle(title);
        stage.setScene((new Scene(root)));
        stage.show();
    }
//  method for adding new users to DB
    public static void signUpUser(ActionEvent event, String username, String password, String firstname, String lastname, String usertype) {
        Connection connection = null;
//      prepared statements are used to query the database
        PreparedStatement psInsert = null;
        PreparedStatement psCheckUserExists = null;
//      resultset is query result
        ResultSet resultSet = null;
        try {
//              change parameters depending on dbms
            connection = getDBLink();
//              question mark is the user input from sign-up form
            psCheckUserExists = connection.prepareStatement("SELECT * FROM users WHERE username = ?");
//              parameter index 1 is the first ? from above prepared statement, a second ? above would entail another method call with parameter index 2
//              in this case, username is checked so second parameter is the username passed by the user through the sign-up form
            psCheckUserExists.setString(1, username);
//              if resultSet is empty, no equal username exists -> sign-up completes
            resultSet = psCheckUserExists.executeQuery();
//              isBeforeFirst() checks if returned username query is empty, if returned true the username is taken
            if (resultSet.isBeforeFirst()) {
                System.out.println("Användare finns redan! ");
                Alert alert = new Alert(Alert.AlertType.ERROR);
//                  for alert, a generic error message is shown for security reasons, i.e., user is not told the name already exists in the database
                alert.setContentText("Felaktigt användarnamn, försök med ett annat. ");
                alert.show();
//                  user doesn't exist, psInsert queries database with new user transaction
            } else {
                psInsert = connection.prepareStatement("INSERT INTO users (username, password, firstname, lastname, usertype) VALUES (?, ?, ?, ?, ?)");
                psInsert.setString(1, username);
                psInsert.setString(2, password);
                psInsert.setString(3, firstname);
                psInsert.setString(4, lastname);
                psInsert.setString(5, usertype);
                psInsert.executeUpdate();
//                      change scenes to logged in scene
                changeScene(event, "login.fxml", "D0024E Bibliotekssystem - Inloggad ", username);
            }
        } catch (SQLException e) {
            e.printStackTrace();
//          close connection to DB with finally block - executed no matter what happens before
//          done to prevent e.g., memory leakage and to free DB resources
//          connection is closed last
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (psInsert != null) {
                try {
                    psInsert.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (psCheckUserExists != null) {
                try {
                    psCheckUserExists.close();
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
    public static void addMedia(String title, String format, String category, String description,
                                String publisher, String edition, String author, String isbn,
                                String director, String actor, String country, String rating,
                                String available)   {
        Connection connection = null;
        PreparedStatement psInsert = null;
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
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (psInsert != null) {
                try {
                    psInsert.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                    e.getCause();
                }
            }
        }
    }
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
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (psUpdate != null) {
                try {
                    psUpdate.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (psCheckMediaExists != null) {
                try {
                    psCheckMediaExists.close();
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
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (psRemove != null) {
                try {
                    psRemove.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (psCheckMediaExists != null) {
                try {
                    psCheckMediaExists.close();
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
//  method logging in user from main screen
//  TODO add parameter usertype to validate actions in next scene
    public static void logInUser(ActionEvent event, String username, String password) {
        Connection connection = null;
        PreparedStatement psCheckLogin = null;
        ResultSet resultSet = null;
        try {
            connection = getDBLink();
            psCheckLogin = connection.prepareStatement("SELECT password FROM users WHERE username = ?;");
            psCheckLogin.setString(1, username);
            resultSet = psCheckLogin.executeQuery();
//          if username query returns false = no username in database
            if (!resultSet.isBeforeFirst()) {
                System.out.println("Användare finns ej i databasen! ");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Ogiltigt användarnamn, försök igen. ");
                alert.show();
            } else {
//                  querying if entered password matches username's password in database
                while (resultSet.next()) {
//                      DB table columns are varchar, which is why getString("column name") is used
                    String retrievePassword = resultSet.getString("password");
                    if (retrievePassword.equals(password)) {
                        changeScene(event, Constants.LOGIN, Constants.LOGIN_TITLE, username);
                    } else {
                        System.out.println("Lösenord matchar ej användare! ");
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setContentText("Ogiltigt lösenord, försök igen. ");
                        alert.show();
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (psCheckLogin != null) {
                try {
                    psCheckLogin.close();
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
    // TODO delegate search function to DBUtils
    public static void initSearch()  {
        Connection connection = null;
        PreparedStatement psFetchArticles = null;
        ResultSet resultSet = null;
        try {
            connection = getDBLink();
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
    }
}