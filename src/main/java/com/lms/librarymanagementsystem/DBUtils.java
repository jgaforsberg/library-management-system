package com.lms.librarymanagementsystem;

import com.lms.librarymanagementsystem.controllers.AccountController;
import com.lms.librarymanagementsystem.controllers.InventoryController;
import com.lms.librarymanagementsystem.controllers.LoanController;
import com.lms.librarymanagementsystem.controllers.LoginController;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import javax.xml.transform.Result;
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
//  Static connection called through getDBLink() to establish DB connection
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
/*
    Method to be called at the end of each DB transaction
    done to prevent e.g., memory leakage and to free DB resources
    connection is closed last

 */
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
//  Login change scene - same format is used throughout every changeSceneXXX()
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
//  method for adding new users to DB
    public static void signUpUser(ActionEvent event, String username, String password, String firstname, String lastname, String usertype, String email) {
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
                psInsert = connection.prepareStatement("INSERT INTO users (username, password, firstname, lastname, usertype, email) VALUES (?, ?, ?, ?, ?, ?)");
                psInsert.setString(1, username);
                psInsert.setString(2, password);
                psInsert.setString(3, firstname);
                psInsert.setString(4, lastname);
                psInsert.setString(5, usertype);
                psInsert.setString(6, email);
                psInsert.executeUpdate();
//                      change scenes to logged in scene
                changeSceneLogin(event, Constants.LOGIN, Constants.LOGIN_TITLE, username);
            }
        } catch (SQLException e) {
            e.printStackTrace();
//          close connection to DB with finally block - executed no matter what happens before
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
                System.out.println(username);
                FXMLLoader loader = new FXMLLoader(DBUtils.class.getResource(fxmlFile));
                root = loader.load();
                AccountController accountController = loader.getController();
                accountController.setUserInformation(username);
                System.out.println(username);
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
// TODO CHECK IF NECESSARY
    private static String date(int d){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, d);
        String date = dateFormat.format(cal.getTime());
        return date;
    }
    public static Integer maxReservations(Integer userid)   {
        Integer maxReservations = null;
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
                        maxReservations = 1;
                        System.out.println(maxReservations);
                        return maxReservations;
                    }
                    case "anställd" -> {
                        System.out.println("anställd");
                        maxReservations = 3;
                        System.out.println(maxReservations);
                        return maxReservations;
                    }
                    case "student" -> {
                        System.out.println("student");
                        maxReservations = 3;
                        System.out.println(maxReservations);
                        return maxReservations;
                    }
                    case "forskare" -> {
                        System.out.println("forskare");
                        maxReservations = 5;
                        System.out.println(maxReservations);
                        return maxReservations;
                    }
                    case "admin" -> {
                        System.out.println("admin");
                        maxReservations = 100;
                        System.out.println(maxReservations);
                        return maxReservations;
                    }
                    default -> {
                        System.out.println("Kan ej läsa användartyp! ");
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setContentText("Fel i användarverifiering. ");
                        alert.show();
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            closeDBLink(connection, psCheckUser, null, null, resultSet);
        }
        return maxReservations;
    }
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
                        maxLoans = 3;
                        return maxLoans;
                    }
                    case "anställd" -> {
                        maxLoans = 5;
                        return maxLoans;
                    }
                    case "student" -> {
                        maxLoans = 5;
                        return maxLoans;
                    }
                    case "forskare" -> {
                        maxLoans = 10;
                        return maxLoans;
                    }
                    case "admin" -> {
                        maxLoans = 100;
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
    public static Integer remainingReservations(Integer maxReservations, Integer userid) {
        Integer remainingReservations = null;
        Connection connection = null;
        PreparedStatement psCheckUser = null;
        ResultSet resultSet = null;
        try {
            connection = getDBLink();
            psCheckUser = connection.prepareStatement("SELECT COUNT(*) FROM reservation WHERE userid = ?;");
            psCheckUser.setInt(1, userid);
            resultSet = psCheckUser.executeQuery();
            while (resultSet.next())    {
                remainingReservations = maxReservations - resultSet.getInt("COUNT(*)");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            closeDBLink(connection, psCheckUser, null, null, resultSet);
        }
        return remainingReservations;
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
            closeDBLink(connection, psCheckFormat, null, null, resultSet);
        }
        return queryFormat;
    }
    private static boolean checkReservation(Integer mediaid, Integer userid) {
        Integer queryUserid = 0;
        Connection connection = null;
        PreparedStatement psCheckReservation = null;
        ResultSet resultSet = null;
        try {
            System.out.println("try block checkReservation()");
            connection = getDBLink();
            psCheckReservation = connection.prepareStatement("SELECT userid FROM reservation WHERE mediaid = ? AND queuenumber = 1;");
            psCheckReservation.setInt(1, mediaid);
            resultSet = psCheckReservation.executeQuery();
            if(!resultSet.isBeforeFirst()) {
                System.out.println("!resultSet.isBeforeFirst()");
                return true;
            }else {
                System.out.println("else resultSet.next()");
                while (resultSet.next()) {
                    System.out.println("while resultSet.next()");
                    queryUserid = resultSet.getInt("userid");
                    System.out.println("while queryUserid = "+queryUserid);
                    updateQueuenumber(mediaid);
                    System.out.println("while queryUserid & userid = "+queryUserid+" "+userid);
                }
            }
        }catch (SQLException e) {
            e.printStackTrace();
            e.getCause();
        }finally {
            closeDBLink(connection, psCheckReservation, null, null, resultSet);
        }
        System.out.println("After finally block");
        System.out.println(queryUserid);
        System.out.println(userid);
        return queryUserid.equals(userid);
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
    public static void addLoan(Integer mediaid, Integer userid) {
        Integer maxLoans = maxLoans(userid);
        Integer remainingLoans = remainingLoans(maxLoans, userid);
        boolean reserved = checkReservation(mediaid, userid);
        System.out.println("addLoan(reserved) = "+reserved);
        if(remainingLoans <= 0) {
            System.out.println("Användaren har inga tillgängliga lånetillfällen! ");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Du har nått din maxgräns för aktiva lån. ");
            alert.show();
        }
        String mediaAvailable = checkAvailable(mediaid);
        if(!mediaAvailable.equalsIgnoreCase("ledig"))   {
            System.out.println("Artikeln är otillgänglig för utlåning! ");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Artikeln är inte tillgänglig för utlåning. ");
            alert.show();
        }
//      TODO boolean reserved blocks all loans where the user does not have queuenumber = 1
        if(!reserved)   {
            System.out.println("En annan användare står före i reservationskön för denna mediaartikel. ");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("En annan användare står före i reservationskön för denna mediaartikel. ");
            alert.show();
        }
        String mediaFormat = checkFormat(mediaid);
        if(remainingLoans > 0 && mediaAvailable.equalsIgnoreCase("ledig") && reserved)   {
            Connection connection = null;
            PreparedStatement  psInsert = null;
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
                    psInsert = connection.prepareStatement("INSERT INTO loan (mediaid, userid,loandate,returndate,returned) VALUES (?, ?, CURDATE(), DATE_ADD(CURDATE(),INTERVAL 14 DAY ) , 0);");
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
                updateQueuenumber(mediaid);
            } catch (SQLException e) {
                e.printStackTrace();
                e.getCause();
            }finally {
                closeDBLink(connection, psInsert, null, null, null);
            }
        }
    }
    public static void extendLoan(Integer loanid)   {
        Connection connection = null;
        PreparedStatement psUpdate = null;
        ResultSet resultSet = null;
        try {
            connection = getDBLink();
            psUpdate = connection.prepareStatement("UPDATE loan SET returndate = returndate+interval 7 day WHERE loanid = ?;");
            psUpdate.setInt(1, loanid);
            psUpdate.executeUpdate();
            System.out.println("Returdatum för lån med lånid: "+loanid+" förlängt 7 dagar! ");
        }catch (SQLException e) {
            e.printStackTrace();
            e.getCause();
        }finally {
            closeDBLink(connection, psUpdate, null, null, resultSet);
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText("Ditt lån med lånid: "+loanid+" är förlängt med 7 dagar. ");
        alert.show();
    }
    private static void setAvailable(Integer mediaid)   {
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
            closeDBLink(connection, psUpdate, null, null, null);
        }
    }
    private static Integer getQueuenumber(Integer mediaid)  {
        Integer queueNumber = null;
        Connection connection = null;
        PreparedStatement psCheckQueue = null;
        ResultSet resultSet = null;
        try {
            connection = getDBLink();
            psCheckQueue = connection.prepareStatement("SELECT COUNT(*) FROM reservation WHERE mediaid = ?;");
            psCheckQueue.setInt(1, mediaid);
            resultSet = psCheckQueue.executeQuery();
            while (resultSet.next())    {
                queueNumber = resultSet.getInt("COUNT(*)");
                System.out.println("getQueuenumber() while loop queueNumber = "+queueNumber);
            }
        }catch (SQLException e) {
            e.printStackTrace();
            e.getCause();
        }finally {
            closeDBLink(connection, psCheckQueue, null, null, resultSet);
        }
        System.out.println("getQueuenumber() after finally block queueNumber = "+queueNumber);
        return queueNumber;
    }
    private static void updateQueuenumber(Integer mediaid) {
        System.out.println("updateQueuenumber(mediaid) ");
        Connection connection = null;
        PreparedStatement psUpdate = null;
        ResultSet resultSet = null;
        try {
            System.out.println("updateQueuenumber try block ");
            connection = getDBLink();
//          Check if any reservations of a media exists
            psUpdate = connection.prepareStatement("SELECT * FROM reservation WHERE mediaid = ?;");
            psUpdate.setInt(1, mediaid);
            resultSet = psUpdate.executeQuery();
                while (resultSet.next())    {
                    System.out.println("updateQueuenumber while loop");
                    psUpdate = connection.prepareStatement("DELETE FROM reservation WHERE queuenumber = 1 AND mediaid = ?;");
                    psUpdate.setInt(1, mediaid);
                    psUpdate.executeUpdate();
                    psUpdate = connection.prepareStatement("UPDATE reservation SET queuenumber = queuenumber - 1 WHERE mediaid = ?;");
                    psUpdate.setInt(1, mediaid);
                    psUpdate.executeUpdate();
                }
        } catch (SQLException e) {
            e.printStackTrace();
            e.getCause();
        }finally {
            closeDBLink(connection, psUpdate, null, null, resultSet);
        }
    }
    public static void returnLoan(Integer loanid, Integer mediaid)   {
        String mediaTitle = "";
        Connection connection = null;
        PreparedStatement psRemove = null;
        ResultSet resultSet = null;
        try {
            connection = getDBLink();
            psRemove = connection.prepareStatement("DELETE FROM loan WHERE loanid = ?;");
            psRemove.setInt(1, loanid);
            psRemove.executeUpdate();
            psRemove = connection.prepareStatement("SELECT title FROM media WHERE mediaid = ?;");
            psRemove.setInt(1, mediaid);
            resultSet = psRemove.executeQuery();
            while (resultSet.next())    {
                mediaTitle = resultSet.getString("title");
            }
            System.out.println("Lån med lånid: "+loanid+ " terminerat! ");
        } catch (SQLException e) {
            e.printStackTrace();
            e.getCause();
        }finally {
            closeDBLink(connection, psRemove, null, null, resultSet);
        }
        setAvailable(mediaid);
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText("Ditt lån av: "+mediaTitle+" är avslutat. ");
        alert.show();
    }
    public static void addReservation(Integer mediaid, Integer userid)  {
        Integer maxReservations = maxReservations(userid);
        Integer remainingReservations = remainingReservations(maxReservations, userid);
        if(remainingReservations <= 0)  {
            System.out.println("Användaren har inga tillgängliga reservationstillfällen! ");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Du har nått din maxgräns för aktiva reservationer. ");
            alert.show();
        }
        String queryAvailable = checkAvailable(mediaid);
        if(!queryAvailable.equalsIgnoreCase("Utlånad"))   {
            System.out.println("Användaren har försökt reservera en ej utlånad mediaartikel! ");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Du kan bara reservera utlånade mediaartiklar. ");
            alert.show();
        }else   {
        System.out.println("addReservation() before calling getQueuenumber() ");
            Integer queueNumber = (getQueuenumber(mediaid)+1);
//          TODO Set check for reservations of available and unreserved media articles
            System.out.println(queueNumber+" returned from getQueuenumber() +1 ");
            Connection connection = null;
            PreparedStatement psInsert = null;
            try {
                connection = getDBLink();
                psInsert = connection.prepareStatement("INSERT INTO reservation(mediaid, userid, queuenumber,reservationdate) VALUES (?,?,?,CURDATE());");
                psInsert.setInt(1, mediaid);
                psInsert.setInt(2, userid);
                psInsert.setInt(3, queueNumber);
                psInsert.executeUpdate();
                System.out.println("Reservation skapad! ");
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setContentText("Reservation skapad. ");
                alert.show();
            }catch (SQLException e) {
                e.printStackTrace();
                e.getCause();
            }finally {
                closeDBLink(connection, psInsert, null, null, null);
            }
        }
    }
    public static void returnReservation(Integer reservationid, String mediaTitle) {
        Connection connection = null;
        PreparedStatement psRemove = null;
        try {
            connection = getDBLink();
            psRemove = connection.prepareStatement("DELETE FROM reservation WHERE reservationid = ?;");
            psRemove.setInt(1, reservationid);
            psRemove.executeUpdate();
            System.out.println("Reservation med reservationid: "+reservationid+" terminerat! ");
        }catch (SQLException e) {
            e.printStackTrace();
            e.getCause();
        }finally {
            closeDBLink(connection, psRemove, null, null, null);
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText("Din reservation av: "+mediaTitle+" är avslutad. ");
        alert.show();
    }
    /*
        TODO Implement reminder function for overdue loans
     */
    private void checkOverdue() {
        Connection connection = null;
        PreparedStatement psCheckOverdue = null;
        ResultSet resultSet = null;
        try {
            connection = getDBLink();
            psCheckOverdue = connection.prepareStatement("SELECT * FROM loan WHERE returndate <= CURDATE() + INTERVAL 1 DAY;");
            resultSet = psCheckOverdue.executeQuery();
            if(!resultSet.isBeforeFirst()) {
                while(resultSet.next()) {
                    Integer userid = resultSet.getInt("userid");
                    psCheckOverdue = connection.prepareStatement("SELECT email FROM users WHERE id = ?;");
                    psCheckOverdue.setInt(1, userid);
                    while (resultSet.next()) {
                        String queryEmail = resultSet.getString("email");
                    /*
                        TODO Send reminder emails to overdue users
                        TODO sendReminder();
                     */
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            e.getCause();
        }finally {
            closeDBLink(connection, psCheckOverdue, null, null, resultSet);
        }
    }
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
            closeDBLink(connection, psInsert, null, null, resultSet);
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText("Media med mediaid: "+mediaid+" är uppdaterad. ");
        alert.show();
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
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText("Media med mediaid: "+mediaid+" är uppdaterad. ");
        alert.show();
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
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText("Media med mediaid: "+mediaid+" är borttagen ur databasen. ");
        alert.show();
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