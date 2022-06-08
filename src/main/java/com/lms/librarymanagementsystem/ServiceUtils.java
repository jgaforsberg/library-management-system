package com.lms.librarymanagementsystem;

import com.lms.librarymanagementsystem.models.LoanModel;
import javafx.scene.control.Alert;

import java.sql.*;
import java.util.Objects;

public class ServiceUtils extends DBUtils{
    //  Checks if there are any overdue loans
    protected static Boolean checkOverdue() {
        Connection connection = null;
        PreparedStatement psCheckOverdue = null;
        ResultSet resultSet = null;
        try {
            connection = DBUtils.getDBLink();
            psCheckOverdue = connection.prepareStatement("SELECT * FROM loan WHERE returndate <= CURDATE();");
            resultSet = psCheckOverdue.executeQuery();
            if(resultSet.isBeforeFirst()) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            e.getCause();
        }finally {
            DBUtils.closeDBLink(connection, psCheckOverdue, resultSet);
        }
        return false;
    }
    //  Extends an active loan with a chosen interval
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
            closeDBLink(connection, psUpdate, resultSet);
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText("Ditt lån med lånid: "+loanid+" är förlängt med 7 dagar. ");
        alert.show();
    }

    //  Checks whether the parameter userid matches the userid of the media reservation where queue number = min value for addLoan()
    private static Integer checkQueue(Integer mediaid, Integer userid) {
        Integer queryUserid = null;
        Integer reserved = null;
        Connection connection = null;
        PreparedStatement psCheckReservation = null;
        ResultSet resultSet = null;
        try {
            connection = getDBLink();
            psCheckReservation = connection.prepareStatement("SELECT userid FROM reservation " +
                    "WHERE queuenumber =(SELECT MIN(queuenumber) FROM reservation) AND mediaid = ?;");
            psCheckReservation.setInt(1, mediaid);
            resultSet = psCheckReservation.executeQuery();
            while (resultSet.next()) {
                queryUserid = resultSet.getInt("userid");
            }
            if(Objects.equals(queryUserid, userid))  {
                reserved = 1;
            }
            if(!Objects.equals(queryUserid, userid)) {
                reserved = 0;
            }
            if(queryUserid == null)    {
                reserved = -1;
            }

        }catch (SQLException e) {
            e.printStackTrace();
            e.getCause();
        }finally {
            closeDBLink(connection, psCheckReservation, resultSet);
        }
        return reserved;
    }
    //  Updates all queue numbers of a certain media article, used when ending reservations
    private static void updateQueuenumber(Integer mediaid) {
        Integer userid = null;
        Connection connection = null;
        PreparedStatement psUpdate = null;
        ResultSet resultSet = null;
        try {
            connection = getDBLink();
            // Update queue numbers of all reservations of media article
            psUpdate = connection.prepareStatement("UPDATE reservation SET queuenumber = queuenumber - 1 WHERE mediaid = ?;");
            psUpdate.setInt(1, mediaid);
            psUpdate.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            e.getCause();
        }finally {
            closeDBLink(connection, psUpdate, resultSet);
        }
    }
    //  Returns loan
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
            // Sets returned item as "Ledig"
            ArticleUtils.setAvailable(mediaid);
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setContentText("Ditt lån av: "+mediaTitle+" är avslutat. ");
            alert.show();
        } catch (SQLException e) {
            e.printStackTrace();
            e.getCause();
        }finally {
            closeDBLink(connection, psRemove, resultSet);
        }
    }
    //  Returns reservation from AccountController and through addLoan()
    public static void returnReservation(Integer reservationid, Integer mediaid, String mediaTitle) {
        Connection connection = null;
        PreparedStatement psRemove = null;
        if(reservationid != null) {
            try {
                connection = getDBLink();
                psRemove = connection.prepareStatement("DELETE FROM reservation WHERE reservationid = ? AND mediaid = ?;");
                psRemove.setInt(1, reservationid);
                psRemove.setInt(2, mediaid);
                psRemove.executeUpdate();
                System.out.println("Reservation med reservationid: " + reservationid + " terminerat! ");
            } catch (SQLException e) {
                e.printStackTrace();
                e.getCause();
            } finally {
                closeDBLink(connection, psRemove);
            }
            updateQueuenumber(mediaid);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Din reservation av: "+mediaTitle+" är avslutad. ");
            alert.show();
        }
    }
    //  Add a loan and tie it to the user id and media id
    public static void addLoan(Integer mediaid, Integer userid) {
        Integer maxLoans = UserUtils.maxLoans(userid);
        Integer remainingLoans = UserUtils.remainingLoans(maxLoans, userid);
        Integer reserved = checkQueue(mediaid, userid);
        if(remainingLoans <= 0) {
            System.out.println("Användaren har inga tillgängliga lånetillfällen! ");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Du har nått din maxgräns för aktiva lån. ");
            alert.show();
        }
        String mediaAvailable = ArticleUtils.checkAvailable(mediaid);
        if(!mediaAvailable.equalsIgnoreCase("ledig"))   {
            System.out.println("Artikeln är otillgänglig för utlåning! ");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Artikeln är inte tillgänglig för utlåning. ");
            alert.show();
        }
        if(reserved == 0)   {
            System.out.println("En annan användare står före i reservationskön för denna mediaartikel. ");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("En annan användare står före i reservationskön för denna mediaartikel. ");
            alert.show();
        }
        String mediaFormat = ArticleUtils.checkFormat(mediaid);
        if(remainingLoans > 0 && mediaAvailable.equalsIgnoreCase("ledig") && (reserved == 1 || reserved == -1))   {
            Connection connection = null;
            PreparedStatement  psInsert = null;
            ResultSet resultSet = null;
            try {
                String mediatitle = "";
                Integer reservationid = null;
                connection = getDBLink();
             /*
                psInsert = connection.prepareStatement("DELETE FROM reservation WHERE queuenumber =(SELECT MIN(queuenumber) FROM reservation) AND mediaid = ? AND userid = ?;");
                psInsert.setInt(1, mediaid);
                psInsert.setInt(2, userid);
                psInsert.executeUpdate();
             */
                psInsert = connection.prepareStatement("SELECT media.title, reservation.reservationid FROM media " +
                        "INNER JOIN reservation ON media.mediaid = reservation.mediaid " +
                        "WHERE reservation.mediaid = ?;");
                psInsert.setInt(1, mediaid);
                resultSet = psInsert.executeQuery();
                if (resultSet.next()) {
                    mediatitle = resultSet.getString("title");
                    reservationid = resultSet.getInt("reservationid");
                }
                if (mediaFormat.equalsIgnoreCase("bok")) {
                    psInsert = connection.prepareStatement("INSERT INTO loan (mediaid, userid,loandate,returndate,returned) VALUES (?, ?, curdate(), date_add(curdate(),interval 28 day ) , 0);");
                    psInsert.setInt(1, mediaid);
                    psInsert.setInt(2, userid);
                    psInsert.executeUpdate();
                    ArticleUtils.setUnavailable(mediaid);
                    System.out.println("Boklån skapat! ");
                    printReceipt(userid, mediaid);
                } else if (mediaFormat.equalsIgnoreCase("film")) {
                    psInsert = connection.prepareStatement("INSERT INTO loan (mediaid, userid,loandate,returndate,returned) VALUES (?, ?, CURDATE(), DATE_ADD(CURDATE(),INTERVAL 7 DAY ) , 0);");
                    psInsert.setInt(1, mediaid);
                    psInsert.setInt(2, userid);
                    psInsert.executeUpdate();
                    ArticleUtils.setUnavailable(mediaid);
                    System.out.println("Filmlån skapat! ");
                    printReceipt(userid, mediaid);
                }else if(mediaFormat.equalsIgnoreCase("kurslitteratur"))  {
                    psInsert = connection.prepareStatement("INSERT INTO loan (mediaid, userid,loandate,returndate,returned) VALUES (?, ?, CURDATE(), DATE_ADD(CURDATE(),INTERVAL 14 DAY ) , 0);");
                    psInsert.setInt(1, mediaid);
                    psInsert.setInt(2, userid);
                    psInsert.executeUpdate();
                    ArticleUtils.setUnavailable(mediaid);
                    System.out.println("Kurslitteraturlån skapat! ");
                    printReceipt(userid, mediaid);
                }else   {
                    System.out.println("Lån kunde ej skapas, mediaformat ej tillgängligt för utlåning! ");
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Lån misslyckades. ");
                    alert.show();
                }
                returnReservation(reservationid, mediaid, mediatitle);
            } catch (SQLException e) {
                e.printStackTrace();
                e.getCause();
            }finally {
                closeDBLink(connection, psInsert, resultSet);
            }
        }
    }
    //  Adds new reservation
    public static void addReservation(Integer mediaid, Integer userid)  {
        Integer maxReservations = UserUtils.maxReservations(userid);
        Integer remainingReservations = UserUtils.remainingReservations(maxReservations, userid);
        Boolean activeReservation = UserUtils.checkReservation(mediaid, userid);
        if(remainingReservations <= 0)  {
            System.out.println("Användaren har inga tillgängliga reservationstillfällen! ");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Du har nått din maxgräns för aktiva reservationer. ");
            alert.show();
        }
        String queryAvailable = ArticleUtils.checkAvailable(mediaid);
        if(!queryAvailable.equalsIgnoreCase("Utlånad") && !queryAvailable.equalsIgnoreCase("Ledig"))   {
            System.out.println("Användaren har försökt reservera en ej utlånad eller ledig mediaartikel! ");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Du kan bara reservera utlånade eller lediga mediaartiklar. ");
            alert.show();
        }
        if(activeReservation)  {
            System.out.println("Användaren har redan reserverat denna artikel! ");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Du har redan en reservation för den här artikeln. ");
            alert.show();
        }
        if((queryAvailable.equalsIgnoreCase("Utlånad") || queryAvailable.equalsIgnoreCase("Ledig")) && remainingReservations >0 && !activeReservation)   {
            Integer queueNumber = ArticleUtils.getQueuenumber(mediaid);
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
                closeDBLink(connection, psInsert);
            }
        }
    }
    //  Prints the receipt of the user's latest loan to an Alert
    private static void printReceipt(Integer userid, Integer mediaid)    {
        LoanModel loanObjectModel;
        Alert receipt = new Alert(Alert.AlertType.INFORMATION);
        Connection connection = null;
        PreparedStatement psFetchLoan = null;
        ResultSet resultSet = null;
        try {
            connection = getDBLink();
            psFetchLoan = connection.prepareStatement("SELECT media.mediaid, media.title, loan.loanid, loan.loandate, loan.returndate FROM media " +
                    "JOIN loan on media.mediaid = loan.mediaid WHERE loan.userid = ? AND loan.mediaid = ? AND loandate = CURDATE() " +
                    "ORDER BY loan.userid DESC LIMIT 1;");
            psFetchLoan.setInt(1, userid);
            psFetchLoan.setInt(2, mediaid);
            resultSet = psFetchLoan.executeQuery();
            while (resultSet.next())    {
                String mediatitle = resultSet.getString("title");
                Integer loanid = resultSet.getInt("loanid");
                Date loandate = resultSet.getDate("loandate");
                Date returndate = resultSet.getDate("returndate");
                loanObjectModel = new LoanModel(null,mediaid,mediatitle,null,loandate,returndate, null);
                receipt.setContentText( "MediaID:\t" +loanObjectModel.getMediaid() +
                        "\nTitel:\t" + loanObjectModel.titleProperty().getValue() +
                        "\nLåndatum:\t" + loanObjectModel.getLoandate() +
                        "\nReturdatum:\t" + loanObjectModel.getReturndate());
                receipt.setTitle("Lånekvitto för lån nummer: "+loanid+" "+loandate);
                receipt.setHeaderText("Du har lånat: ");
                receipt.show();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            e.getCause();
        }finally {
            closeDBLink(connection, psFetchLoan, resultSet);
        }
    }
}
