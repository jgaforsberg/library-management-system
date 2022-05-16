package com.lms.librarymanagementsystem;

import com.lms.librarymanagementsystem.controllers.AccountController;
import com.lms.librarymanagementsystem.controllers.InventoryController;
import com.lms.librarymanagementsystem.controllers.LoanController;
import com.lms.librarymanagementsystem.controllers.LoginController;
import com.lms.librarymanagementsystem.models.UserModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;

import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

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
    public static void closeDBLink(Connection connection, PreparedStatement ps1, PreparedStatement ps2, PreparedStatement ps3, ResultSet resultSet) {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (ps1 != null) {
            try {
                ps1.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (ps2!= null) {
            try {
                ps2.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (ps3 != null) {
            try {
                ps3.close();
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
    // TODO changeScene methods for each unique scene change that utilizes username
    // TODO username can be used as unique identifier since its value is unique

    //  login change scene
    public static void changeSceneLogin(ActionEvent event, String fxmlFile, String title, String username)   {
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
                e.getCause();
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
    // loan scene change
    public static void changeSceneLoan(ActionEvent event, String fxmlfile, String title, String username)   {
        Parent root = null;
        if(username != null)    {
            try{
                FXMLLoader loader = new FXMLLoader(DBUtils.class.getResource(fxmlfile));
                root = loader.load();
                LoanController loanController = loader.getController();
                loanController.setUserInformation(username);
                System.out.println("Sök- och lånevy initierad! ");
            }catch (IOException e)  {
                e.printStackTrace();
                e.getCause();
            }
        }else{
            try {
                root = FXMLLoader.load(DBUtils.class.getResource(fxmlfile));
                System.out.println("Kunde ej initiera lånevy! ");
            }catch (IOException e)  {
                e.printStackTrace();
                e.getCause();
            }
        }
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setTitle(title);
        stage.setScene((new Scene(root)));
        stage.show();
    }
//  logged in change scene search
    public static void changeSceneLogout(ActionEvent event, String fxmlFile, String title) {
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
 /*   public static Integer getUserId(String username)    {
        Integer userid = null;
        if(!username.isEmpty()) {
            Connection connection = null;
            PreparedStatement psFetchUserId = null;
            ResultSet resultSet = null;
            try {
                connection = DBUtils.getDBLink();
                psFetchUserId = connection.prepareStatement("SELECT id FROM users WHERE username = ?;");
                psFetchUserId.setString(1, username);
                resultSet = psFetchUserId.executeQuery();
                while (resultSet.next()) {
                    userid = resultSet.getInt("id");
                }
                return userid;
            } catch (SQLException e) {
                e.printStackTrace();
                e.getCause();
            } finally {
                if (psFetchUserId != null) {
                    try {
                        psFetchUserId.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                if (resultSet != null) {
                    try {
                        resultSet.close();
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
        }else return null;
        System.out.println(userid);
        return userid;
    }  */
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
                changeSceneLogin(event, Constants.LOGIN, Constants.LOGIN_TITLE, username);
            }
        } catch (SQLException e) {
            e.printStackTrace();
//          close connection to DB with finally block - executed no matter what happens before
//          done to prevent e.g., memory leakage and to free DB resources
//          connection is closed last
        } finally {
            closeDBLink(connection, psInsert, psCheckUserExists, null, resultSet);
        }
    }
    public static void validateUser(ActionEvent event, String username) {
        String authorizedLibrarian = "bibliotekarie";
        String authorizedAdmin = "admin";
        Connection connection = null;
        PreparedStatement psCheckUserType = null;
        ResultSet resultSet = null;
        try{
            connection = getDBLink();
            psCheckUserType = connection.prepareStatement("SELECT usertype FROM users WHERE username = ?;");
            psCheckUserType.setString(1, username);
            resultSet = psCheckUserType.executeQuery();
            while (resultSet.next())    {
                String actualUsertype = resultSet.getString("usertype");
                if(actualUsertype.equalsIgnoreCase(authorizedLibrarian) || actualUsertype.equalsIgnoreCase(authorizedAdmin)) {
                    changeSceneInventory(event, Constants.INVENTORY, Constants.INVENTORY_TITLE, username);
                    System.out.println("Användare validerad! ");
                }else {
                    System.out.println();
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Du har inte behörighet för denna funktion. ");
                    alert.show();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            e.getCause();
        }finally {
            closeDBLink(connection, psCheckUserType, null, null, resultSet);
        }
    }
    public static void changeSceneInventory(ActionEvent event, String fxmlFile, String title, String username)   {
        Parent root = null;
        if(username != null)    {
            try{
                FXMLLoader loader = new FXMLLoader(DBUtils.class.getResource(fxmlFile));
                root = loader.load();
                InventoryController inventoryController = loader.getController();
                inventoryController.setUserInformation(username);
                System.out.println("Artikelhanteringsvy initierad! ");
            }catch (IOException e)  {
                e.printStackTrace();
                e.getCause();
            }
        }else{
            try {
                root = FXMLLoader.load(DBUtils.class.getResource(fxmlFile));
                System.out.println("Kunde ej initiera artikelhanteringsvy! ");
            }catch (IOException e)  {
                e.printStackTrace();
                e.getCause();
            }
        }
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setTitle(title);
        stage.setScene((new Scene(root)));
        stage.show();
    }
    public static void changeSceneAccount(ActionEvent event, String fxmlFile, String title, String username)    {
        Parent root = null;
        if(username != null)    {
            try{
                FXMLLoader loader = new FXMLLoader(DBUtils.class.getResource(fxmlFile));
                root = loader.load();
                AccountController accountController = loader.getController();
                accountController.setUserInformation(username);
                System.out.println("Kontovy initierad! ");
            }catch (IOException e)  {
                e.printStackTrace();
                e.getCause();
            }
        }else{
            try{
                root = FXMLLoader.load(DBUtils.class.getResource(fxmlFile));
                System.out.println("Kunde ej initiera kontovy! ");
            }catch (IOException e)  {
                e.printStackTrace();
                e.getCause();
            }
        }
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setTitle(title);
        stage.setScene((new Scene(root)));
        stage.show();
    }
    //  method logging in user from main screen
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
                        changeSceneLogin(event, Constants.LOGIN, Constants.LOGIN_TITLE, username);
                        System.out.println("Inloggad vy initierad! ");
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
            closeDBLink(connection, psCheckLogin, null, null, resultSet);
        }
    }
    private static String date(int d){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, d);
        String date = dateFormat.format(cal.getTime());
        return date;
    }
    // TODO Use this for AccountController too
    public static Integer maxLoans(Integer userid)  {
        Integer maxLoans = null;
        Connection connection = null;
        PreparedStatement  psCheckUser = null;
        ResultSet resultSet = null;
        try {
            connection = getDBLink();
            psCheckUser = connection.prepareStatement("SELECT usertype FROM users WHERE id = ?;");
            psCheckUser.setInt(1, userid);
            resultSet = psCheckUser.executeQuery();
            while (resultSet.next()) {
                String queryUsertype = resultSet.getString("usertype");
                switch (queryUsertype.toLowerCase()) {
                    case "allmän" -> {
                        System.out.println("allmän");
                        maxLoans = 3;
                        System.out.println(maxLoans);
                        return maxLoans;
                    }
                    case "anställd" -> {
                        System.out.println("anställd");
                        maxLoans = 5;
                        System.out.println(maxLoans);
                        return maxLoans;
                    }
                    case "student" -> {
                        System.out.println("student");
                        maxLoans = 5;
                        System.out.println(maxLoans);
                        return maxLoans;
                    }
                    case "forskare" -> {
                        System.out.println("forskare");
                        maxLoans = 10;
                        System.out.println(maxLoans);
                        return maxLoans;
                    }
                    case "admin" -> {
                        System.out.println("admin");
                        maxLoans = 100;
                        System.out.println(maxLoans);
                        return maxLoans;
                    }
                    default -> {
                        System.out.println("Kan ej läsa användartyp! ");
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setContentText("Fel i användarverifiering. ");
                        alert.show();
                    }
                }
            }
        }catch (SQLException e) {
            e.printStackTrace();
            e.getCause();
        }finally {
            closeDBLink(connection, psCheckUser, null, null, resultSet);
        }
        System.out.println(maxLoans);
        return maxLoans;
    }
    public static Integer remainingLoans(Integer maxLoans, Integer userid) {
        Integer remainingLoans = null;
        Connection connection = null;
        PreparedStatement  psCheckUser = null;
        ResultSet resultSet = null;
        try{
            connection = getDBLink();
            psCheckUser = connection.prepareStatement("SELECT COUNT(*)FROM loan WHERE userid = ?;");
            psCheckUser.setInt(1, userid);
            resultSet = psCheckUser.executeQuery();
            while (resultSet.next())    {
                remainingLoans = maxLoans - resultSet.getInt("COUNT(*)");
                System.out.println();
                System.out.println(maxLoans);
                System.out.println(remainingLoans);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            e.getCause();
        }finally {
            closeDBLink(connection, psCheckUser, null, null, resultSet);
        }
        return remainingLoans;
    }
    private static String checkAvailable(Integer mediaid) {
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
            closeDBLink(connection, psCheckAvailable, null, null, resultSet);
        }
        return queryAvailable;
    }
    private static String checkFormat(Integer mediaid)   {
        String queryFormat = "";
        Connection connection = null;
        PreparedStatement psCheckFormat = null;
        ResultSet resultSet = null;
        try {
            connection = getDBLink();
            psCheckFormat = connection.prepareStatement("SELECT * FROM media WHERE mediaid = ?;");
            psCheckFormat.setInt(1, mediaid);
            resultSet = psCheckFormat.executeQuery();
            while (resultSet.next())    {
                queryFormat = resultSet.getString("format");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            e.getCause();
        }finally {
            closeDBLink(connection, psCheckFormat, null, null, resultSet);
        }
        return queryFormat;
    }
    private static void setUnavailable(Integer mediaid) {
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
            closeDBLink(connection, psUpdate, null, null, resultSet);
        }
    }
    private static void setAvailable(Integer mediaid)   {
        Connection connection = null;
        PreparedStatement psUpdate = null;
        ResultSet resultSet = null;
        try {
            connection = getDBLink();
            psUpdate = connection.prepareStatement("UPDATE media SET available = 'Ledig' WHERE mediaid = ?");
            psUpdate.setInt(1, mediaid);
            psUpdate.executeUpdate();
            System.out.println("Mediatillgänglighet för mediaartikel "+mediaid+" satt till status ledig! ");
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            closeDBLink(connection, psUpdate, null, null, resultSet);
        }
    }
    public static void addLoan(Integer mediaid, Integer userid) {
        Integer maxLoans = maxLoans(userid);
        Integer remainingLoans = remainingLoans(maxLoans, userid);
        if(remainingLoans <= 0) {
            System.out.println("Användaren har inga tillgängliga lånetillfällen! ");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Du har nått din maxgräns för aktiva lån. ");
            alert.show();
        }
        String mediaAvailable = checkAvailable(mediaid);
        if(mediaAvailable.equalsIgnoreCase("referens") || mediaAvailable.equalsIgnoreCase("utlånad"))   {
            System.out.println("Artikeln är otillgänglig för utlåning! ");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Artikeln är inte tillgänglig för utlåning. ");
            alert.show();
        }
        String mediaFormat = checkFormat(mediaid);
        if(remainingLoans > 0 && (!mediaAvailable.equalsIgnoreCase("referens") && !mediaAvailable.equalsIgnoreCase("utlånad")))   {
            Connection connection = null;
            PreparedStatement  psInsert = null;
            ResultSet resultSet = null;
            try{
                connection = getDBLink();
                if (mediaFormat.equalsIgnoreCase("bok")) {
                    psInsert = connection.prepareStatement("INSERT INTO loan (mediaid, userid,loandate,returndate,returned) VALUES (?, ?, curdate(), date_add(curdate(),interval 28 day ) , 0);");
                    psInsert.setInt(1, mediaid);
                    psInsert.setInt(2, userid);
                    psInsert.executeUpdate();
                    setUnavailable(mediaid);
                    System.out.println("Lån skapat! ");
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setContentText("Lån skapat. ");
                    alert.show();
                }else if(mediaFormat.equalsIgnoreCase("film"))  {
                    psInsert = connection.prepareStatement("INSERT INTO loan (mediaid, userid,loandate,returndate,returned) VALUES (?, ?, curdate(), date_add(curdate(),interval 14 day ) , 0);");
                    psInsert.setInt(1, mediaid);
                    psInsert.setInt(2, userid);
                    psInsert.executeUpdate();
                    setUnavailable(mediaid);
                    System.out.println("Lån skapat! ");
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setContentText("Lån skapat. ");
                    alert.show();
                }else   {
                    System.out.println("Lån kunde ej skapas, mediaformat ej tillgängligt för utlåning! ");
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Lån misslyckades. ");
                    alert.show();
                }
            } catch (SQLException e) {
                e.printStackTrace();
                e.getCause();
            }finally {
                closeDBLink(connection, psInsert, null, null, resultSet);
            }
        }
    }


    public static void addMedia(String title, String format, String category, String description,
                                String publisher, String edition, String author, String isbn,
                                String director, String actor, String country, String rating,
                                String available)   {
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
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            closeDBLink(connection, psInsert, null, null, resultSet);
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
            closeDBLink(connection, psUpdate, psCheckMediaExists, null, resultSet);
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
            closeDBLink(connection, psRemove, psCheckMediaExists, null, resultSet);
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
            closeDBLink(connection, psFetchArticles, null, null, resultSet);
        }
    }
}