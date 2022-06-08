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

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SceneUtils extends DBUtils{
    // Initializes the logged in view - same format is used throughout every changeSceneXXX()
    public static void changeSceneLogin(ActionEvent event, String fxmlFile, String title, String username)   {
        Parent root = null;
//      make sure username is passed to login scene
        if(username != null)    {
            try {
//              Passing fxmlfile allows to choose fxmlfile to pass in as parameter
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
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setTitle(title);
        stage.setScene((new Scene(root)));
        stage.show();
    }
    // Initializes the search and loan view for a logged in user
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
    //  Initializes a view that does not require a user to be logged in
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
    // Loads the user account scene and sets user information in that scene through the AccountController object
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
    // Loads the user account scene and sets user information in that scene through the AccountController object
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
    //  Creating a new user and inserting the user into the database. Comments of this method is universal through all changeScene methods
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
//              For alert, a generic error message is shown for security reasons, i.e., user is not told the name already exists in the database
                alert.setContentText("Felaktigt användarnamn, försök med ett annat. ");
                alert.show();
//              User doesn't exist, psInsert queries database with new user transaction
            }else {
                psCheckUserExists = connection.prepareStatement("SELECT * FROM users WHERE email = ?");
                psCheckUserExists.setString(1, email);
                resultSet = psCheckUserExists.executeQuery();
                if (resultSet.isBeforeFirst()){
                    System.out.println("Email används redan! ");
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Felaktig email, försök med en annan. ");
                    alert.show();
                }else {
                    psInsert = connection.prepareStatement("INSERT INTO users (username, password, firstname, lastname, usertype, email) VALUES (?, ?, ?, ?, ?, ?)");
                    psInsert.setString(1, username);
                    psInsert.setString(2, password);
                    psInsert.setString(3, firstname);
                    psInsert.setString(4, lastname);
                    psInsert.setString(5, usertype);
                    psInsert.setString(6, email);
                    psInsert.executeUpdate();
//                  Change scenes to logged in scene
                    changeSceneLogin(event, Constants.LOGIN, Constants.LOGIN_TITLE, username);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
//      Close connection to DB with finally block - executed no matter what happens before
        } finally {
            closeDBLink(connection, psInsert, psCheckUserExists, resultSet);
        }
    }
    // Used to validate the user type of a user trying to access the InventoryController
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
            closeDBLink(connection, psCheckUserType, resultSet);
        }
    }
    //  Logs in the user if credentials exist and match
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
            closeDBLink(connection, psCheckLogin, resultSet);
        }
    }
}
