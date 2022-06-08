package com.lms.librarymanagementsystem;

import javafx.scene.control.Alert;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserUtils extends DBUtils{
    //  Fetches the email of a user with overdue loans
    protected static String getOverdueUser()  {
        String overdueUser = "";
        Connection connection = null;
        PreparedStatement psFetchUser = null;
        ResultSet resultSet = null;
        try {
            connection = DBUtils.getDBLink();
            psFetchUser = connection.prepareStatement("SELECT users.email FROM loan JOIN users ON users.id = loan.userid WHERE returndate <= CURDATE();");
            resultSet = psFetchUser.executeQuery();
            while (resultSet.next()) {
                overdueUser = resultSet.getString("email");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            e.getCause();
        }finally {
            DBUtils.closeDBLink(connection, psFetchUser, resultSet);
        }
        return overdueUser;
    }
    // Defines the max allowed reservations of a certain user type
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
                        maxReservations = 1;
                        return maxReservations;
                    }
                    case "anställd" -> {
                        maxReservations = 3;
                        return maxReservations;
                    }
                    case "student" -> {
                        maxReservations = 3;
                        return maxReservations;
                    }
                    case "forskare" -> {
                        maxReservations = 5;
                        return maxReservations;
                    }
                    case "admin" -> {
                        maxReservations = 100;
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
    // Defines the max allowed loans of a certain user type
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
            closeDBLink(connection, psCheckUser, resultSet);
        }
        System.out.println(maxLoans);
        return maxLoans;
    }
    // Returns the remaining reservations a user has by counting rows in the database.reservation
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
            closeDBLink(connection, psCheckUser, resultSet);
        }
        return remainingReservations;
    }
    // Returns the remaining loans a user has by counting rows in the database.loan
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
            closeDBLink(connection, psCheckUser, resultSet);
        }
        return remainingLoans;
    }

    // Checks whether a user has an active reservation of the media for addReservation
    protected static boolean checkReservation(Integer mediaid, Integer userid)    {
        Integer queryUserid = -1;
        Connection connection = null;
        PreparedStatement psCheckReservation = null;
        ResultSet resultSet = null;
        try{
            connection = getDBLink();
            psCheckReservation = connection.prepareStatement("SELECT userid FROM reservation WHERE mediaid = ?;");
            psCheckReservation.setInt(1, mediaid);
            resultSet = psCheckReservation.executeQuery();
            if(resultSet.next())    {
                queryUserid = resultSet.getInt("userid");
            }

        }catch (SQLException e) {
            e.printStackTrace();
            e.getCause();
        }finally {
            closeDBLink(connection, psCheckReservation, resultSet);
        }
        return queryUserid.equals(userid);
    }
}
