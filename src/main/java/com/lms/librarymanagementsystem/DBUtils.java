package com.lms.librarymanagementsystem;

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

public class DBUtils {

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

    //  method for adding new users to DB
    public static void signUpUser(ActionEvent event, String username, String password, String firstname, String lastname, String usertype)  {
        Connection connection = null;
//      prepared statements are used to query the database
        PreparedStatement psInsert = null;
        PreparedStatement psCheckUserExists = null;
//      resultset is query result
        ResultSet resultSet = null;

        try {
//              change parameters depending on dbms
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/javafxtest", "root", "1234");
//              question mark is the user input from sign-up form
            psCheckUserExists = connection.prepareStatement("SELECT * FROM users WHERE username = ?");
//              parameter index 1 is the first ? from above prepared statement, a second ? above would entail another method call with parameter index 2
//              in this case, username is checked so second parameter is the username passed by the user through the sign-up form
            psCheckUserExists.setString(1, username);
//              if resultSet is empty, no equal username exists -> sign-up completes
            resultSet = psCheckUserExists.executeQuery();

//              isBeforeFirst() checks if returned username query is empty, if returned true the username is taken
            if(resultSet.isBeforeFirst())   {
                System.out.println("Användare finns redan! ");
                Alert alert = new Alert(Alert.AlertType.ERROR);
//                  for alert, a generic error message is shown for security reasons, i.e., user is not told the name already exists in the database
                alert.setContentText("Felaktigt användarnamn, försök med ett annat. ");
                alert.show();
//                  user doesn't exist, psInsert queries database with new user transaction
            }   else    {
                psInsert = connection.prepareStatement("INSERT INTO users (username, password, firstname, lastname, usertype) VALUES (?, ?, ?, ?, ?)");
                psInsert.setString(1, username);
                psInsert.setString(2, password);
                psInsert.setString(3, firstname);
                psInsert.setString(4, lastname);
                psInsert.setString(5, usertype);
                psInsert.executeUpdate();
//                      change scenes to logged in scene
                changeScene(event,"login.fxml", "D0024E Bibliotekssystem - Inloggad ", username);
            }

        }   catch(SQLException e) {
            e.printStackTrace();
//          close connection to DB with finally block - executed no matter what happens before
//          done to prevent e.g., memory leakage and to free DB resources
//          connection is closed last
        }   finally {
            if(resultSet != null)  {
                try {
                    resultSet.close();
                }   catch(SQLException e)   {
                    e.printStackTrace();
                }
            }
            if(psInsert != null)  {
                try {
                    psInsert.close();
                }   catch(SQLException e)   {
                    e.printStackTrace();
                }
            }
            if(psCheckUserExists != null)  {
                try {
                    psCheckUserExists.close();
                }   catch(SQLException e)   {
                    e.printStackTrace();
                }
            }
            if(connection != null)  {
                try {
                    connection.close();
                }   catch(SQLException e)   {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void logInUser(ActionEvent event, String username, String password)   {
        Connection connection = null;
        PreparedStatement checkLogin = null;
        ResultSet resultSet = null;
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/javafxtest", "root", "1234");
            checkLogin = connection.prepareStatement("SELECT password, firstname FROM users WHERE username = ?");
            checkLogin.setString(1, username);
            resultSet = checkLogin.executeQuery();
//          if username query returns false = no username in database
            if(!resultSet.isBeforeFirst())   {
                System.out.println("Användare finns ej i databasen! ");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Fel användarnamn, försök igen. ");
                alert.show();
            }   else    {
//                  querying if entered password matches username's password in database
                while(resultSet.next()) {
//                      DB table columns are varchar, which is why getString("column name") is used
                    String retrievePassword = resultSet.getString("password");
                    String retrieveName = resultSet.getString(("firstname"));
                    if(retrievePassword.equals(password))   {
                        changeScene(event, "login.fxml", "D0024E Bibliotekssystem - Inloggad ", username);
                    }   else    {
                        System.out.println("Lösenord matchar ej användare! ");
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setContentText("Fel lösenord, försök igen. ");
                        alert.show();
                    }
                }
            }
        }   catch(SQLException e)   {
            e.printStackTrace();
        }   finally {
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

    public static void exitProgram(ActionEvent event)    {
        System.exit(0);
    }
}