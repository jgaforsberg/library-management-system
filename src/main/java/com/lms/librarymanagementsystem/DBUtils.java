package com.lms.librarymanagementsystem;

import com.lms.librarymanagementsystem.controllers.LoginController;
import com.lms.librarymanagementsystem.utils.Constants;
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
    private final static String URL = "jdbc:mysql://localhost:3306/javafxtest";
    private final static String USER = "root";
    private final static String PWD = "1234";
//  for connecting through controller classes for e.g., search function
    public Connection DBLink;

    public Connection getDBConnection() throws SQLException {
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
            }
        }
//      event is the click of button -> source of the click, scene of source, window of scene
//      stage is the window of the GUI, scene is what is displayed in the window. Stages can have multiple scenes
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setTitle(title);
        stage.setScene((new Scene(root, 600, 500)));
        stage.show();
    }

//  logged in change scene search
    public static void changeScene(ActionEvent event, String fxmlFile, String title) {
        Parent root = null;
        try {
            root = FXMLLoader.load(DBUtils.class.getResource(fxmlFile));
        } catch (IOException e) {
            e.getCause();
        }
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle(title);
        stage.setScene((new Scene(root, 600, 1500)));
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
            connection = DriverManager.getConnection(URL,USER,PWD);
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

    //  method logging in user from main screen
//  TODO add parameter usertype to validate actions in next scene
    public static void logInUser(ActionEvent event, String username, String password) {
        Connection connection = null;
        PreparedStatement checkLogin = null;
        ResultSet resultSet = null;
        try {
            connection = DriverManager.getConnection(URL,USER,PWD);
            checkLogin = connection.prepareStatement("SELECT password FROM users WHERE username = ?");
            checkLogin.setString(1, username);
            resultSet = checkLogin.executeQuery();
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
                        changeScene(event, "login.fxml", "D0024E Bibliotekssystem - Inloggad ", username);
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
            if (checkLogin != null) {
                try {
                    checkLogin.close();
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
// simple title search
// TODO implement other parameter search function
    /*public static void search(String articleType) {
        System.out.println("DBUtils.searchArticle()");

        Connection connection = null;
        PreparedStatement psFetchBook = null, psFetchFilm = null, psFetchJournal = null;
        ResultSet resultSet = null;
        try {
            if(articleType.equalsIgnoreCase("book")){
                psFetchBook = connection.prepareStatement("SELECT bookid, title, author, isbn, category, publisher, description, available FROM ?;");
                psFetchBook.setString(1, articleType);
                resultSet = psFetchBook.executeQuery();

                while(resultSet.next()) {
                    Integer queryId = resultSet.getInt("bookid");
                    String queryTitle = resultSet.getString("title");
                    String queryAuthor = resultSet.getString("author");
                    String queryIsbn = resultSet.getString("isbn");
                    String queryCategory = resultSet.getString("category");
                    String queryPublisher = resultSet.getString("publisher");
                    String queryDescription = resultSet.getString("description");
                    Integer queryAvailable = resultSet.getInt("available");
//                  populates the observable list
                    SearchController.bookSearchModelObservableList.add(new BookSearchModel( queryId,
                                                                                            queryTitle,
                                                                                            queryAuthor,
                                                                                            queryIsbn,
                                                                                            queryCategory,
                                                                                            queryPublisher,
                                                                                            queryDescription,
                                                                                            queryAvailable));
//              PropertyValueFactory corresponds to the new BookSearchModel
//              populate the tableview columns
                    SearchController.bookIdColumn.setCellValueFactory((new PropertyValueFactory<>("bookid")));
                    SearchController.titleColumn.setCellValueFactory((new PropertyValueFactory<>("title")));
                    SearchController.authorColumn.setCellValueFactory((new PropertyValueFactory<>("author")));
                    SearchController.isbnColumn.setCellValueFactory((new PropertyValueFactory<>("isbn")));
                    SearchController.categoryColumn.setCellValueFactory((new PropertyValueFactory<>("category")));
                    SearchController.publisherColumn.setCellValueFactory((new PropertyValueFactory<>("publisher")));
                    SearchController.descriptionColumn.setCellValueFactory((new PropertyValueFactory<>("description")));
                    SearchController.availableColumn.setCellValueFactory((new PropertyValueFactory<>("available")));

                    SearchController.searchTableView.setItems(SearchController.bookSearchModelObservableList);

                }
            }else if(articleType.equalsIgnoreCase("film"))    {
                psFetchFilm = connection.prepareStatement("SELECT filmid, title, director, category, description, rating, country, actors, available FROM ?;");
                psFetchFilm.setString(1, articleType);
                resultSet = psFetchFilm.executeQuery();
            }else if (articleType.equalsIgnoreCase("journal"))  {
                psFetchJournal = connection.prepareStatement("SELECT journalid, title, publisher, number, category, available FROM ?;");
                psFetchJournal.setString(1, articleType);
                resultSet = psFetchJournal.executeQuery();
            }else;

            connection = DriverManager.getConnection(Constants.url, Constants.user, Constants.pw);
             = connection.prepareStatement("select * from articles where title like ?;");
            psFetchArticles.setString(1, "%"+searchTerm+"%");
//              if resultSet is empty, no equal database record exists
            resultSet = psFetchArticles.executeQuery();
            if(!resultSet.isBeforeFirst())  {
                System.out.println("Tomt sökfält! ");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Ange ett sökord ");
                alert.show();
            }   else    {
                while(resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String title = resultSet.getString("title");
                    String authorCreator = resultSet.getString("authorcreator");
                    String isbn = resultSet.getString("isbn");
                    String category = resultSet.getString("category");
                    String articleType = resultSet.getString("articletype");
                    String actors = resultSet.getString("actors");

                    System.out.println("Resultat: "+id+", "+title+", "+authorCreator+", "+isbn+", "+category+", "+articleType+", "+actors+", ");
                    searchResult = id+", "+title+", "+authorCreator+", "+isbn+", "+category+", "+articleType+", "+actors+", ";
                }
            }
        SearchController.printSearchResult(searchResult);
        } catch (SQLException e) {
            e.getCause();
        }finally {
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
    }*/
}