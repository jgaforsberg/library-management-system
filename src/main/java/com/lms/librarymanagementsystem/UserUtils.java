package com.lms.librarymanagementsystem;

import com.lms.librarymanagementsystem.models.LoanModel;
import com.lms.librarymanagementsystem.models.MediaModel;
import com.lms.librarymanagementsystem.models.ReservationModel;
import javafx.scene.control.Alert;

import java.sql.*;
import java.util.ArrayList;

public class UserUtils extends DBUtils{
//  Method of returning users reservations as ArrayList of reservation objects
    public static ArrayList<MediaModel> reservation(Integer userid) {
        ArrayList<MediaModel> reservationModelArrayList = new ArrayList<>();
        Connection connection = null;
        PreparedStatement psFetchReservations = null;
        ResultSet resultSet = null;
        try {
            connection = getDBLink();
            psFetchReservations = connection.prepareStatement("SELECT reservation.reservationid, reservation.mediaid, media.title, reservation.queuenumber, reservation.reservationdate FROM media JOIN reservation on media.mediaid = reservation.mediaid WHERE userid = ?;");
            psFetchReservations.setInt(1, userid);
            resultSet = psFetchReservations.executeQuery();
            while (resultSet.next())    {
                Integer queryReservationid = resultSet.getInt("reservationid");
                Integer queryMediaid = resultSet.getInt("mediaid");
                String queryTitle = resultSet.getString("title");
                Integer queryQueueNumber = resultSet.getInt("queuenumber");
                Date queryReservationdate = resultSet.getDate("reservationdate");
//              The ArrayList<MediaModel> is populated by subclass objects, i.e., ReservationModel objects. Example of downcasting
                reservationModelArrayList.add(new ReservationModel( queryReservationid,
                                                                    queryMediaid,
                                                                    queryTitle,
                                                                    queryQueueNumber,
                                                                    queryReservationdate));
            }
        }catch (SQLException e) {
            System.out.println("Kan ej ladda reservationer till tabellen! ");
            e.printStackTrace();
            e.getCause();
        }finally {
            closeDBLink(connection,psFetchReservations,resultSet);
    }

    return reservationModelArrayList;
    }
//  Method returning users loans as ArrayList of loan objects
    public static ArrayList<MediaModel>  loan(Integer userid)    {
        ArrayList<MediaModel> loanModelArrayList = new ArrayList<>();
        Connection connection = null;
        PreparedStatement psFetchLoans = null;
        ResultSet resultSet = null;
        try{
            connection = getDBLink();
            psFetchLoans = connection.prepareStatement( "SELECT loan.loanid, loan.mediaid, media.title, loan.loandate, loan.returndate FROM media " +
                    "JOIN loan ON media.mediaid = loan.mediaid WHERE userid = ?;");
            psFetchLoans.setInt(1, userid);
            resultSet = psFetchLoans.executeQuery();
            while (resultSet.next())    {
                Integer queryLoanid = resultSet.getInt("loanid");
                Integer queryMediaid = resultSet.getInt("mediaid");
                String queryTitle = resultSet.getString("title");
                java.sql.Date queryLoandate = resultSet.getDate("loandate");
                java.sql.Date queryReturndate = resultSet.getDate("returndate");
//              The ArrayList<MediaModel> is populated by subclass objects, i.e., LoanModel objects. Example of downcasting
                loanModelArrayList.add(new LoanModel(   queryLoanid,
                                                        queryMediaid,
                                                        queryTitle,
                                                        queryLoandate,
                                                        queryReturndate));
            }
        }catch (SQLException e) {
            System.out.println("Kan ej ladda lån till tabellen! ");
            e.printStackTrace();
            e.getCause();
        }finally {
            closeDBLink(connection, psFetchLoans, resultSet);
        }
        return loanModelArrayList;
    }
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
