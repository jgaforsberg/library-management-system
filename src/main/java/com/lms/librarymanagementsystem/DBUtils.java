package com.lms.librarymanagementsystem;

import com.lms.librarymanagementsystem.controllers.AccountController;
import com.lms.librarymanagementsystem.controllers.InventoryController;
import com.lms.librarymanagementsystem.controllers.LoanController;
import com.lms.librarymanagementsystem.controllers.LoginController;
import com.lms.librarymanagementsystem.models.LoanModel;
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
import java.util.Objects;

//  #011B3E blue
//  #F0F0F0 light gray

/*
    The purpose of using prepared statements is increased protection against sql injection, it's easier to set parameters,
    and to improve application performance due to queries being precompiled. All statements in this program are prepared
*/
@SuppressWarnings("ALL")
public class DBUtils {
    //  Static connection called through getDBLink() to establish DB connection
    public static Connection DBLink;
    // Returns the database link for all database connections
    public static Connection getDBLink() throws SQLException {
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            DBLink = DriverManager.getConnection(Constants.URL,Constants.USER,Constants.PWD);
        }catch(ClassNotFoundException e)    {
            e.printStackTrace();
        }
        return DBLink;
    }
    /*
            Methods to be called at the end of each DB transaction
            Done to prevent e.g., memory leakage and to free DB resources
            Connection is closed last
            Implements method overloading depending on the type of DB connection to close
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
    public static void closeDBLink(Connection connection, PreparedStatement ps1, PreparedStatement ps2, ResultSet resultSet)    {
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
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
                e.getCause();
            }
        }
    }
    public static void closeDBLink(Connection connection, PreparedStatement ps1, ResultSet resultSet)   {
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
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
                e.getCause();
            }
        }
    }
    public static void closeDBLink(Connection connection, PreparedStatement ps1)    {
        if (ps1 != null) {
            try {
                ps1.close();
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

    public static Date date(int d){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, d);
        return Date.valueOf(dateFormat.format(cal.getTime()));
    }



}