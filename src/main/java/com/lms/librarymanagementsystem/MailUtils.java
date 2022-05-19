package com.lms.librarymanagementsystem;

import javafx.scene.control.Alert;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

public class MailUtils {
//  If conditions are true, reminder emails are sent to accounts with overdue loans
    public static void sendMail() {
        System.out.println("Kollar överskridna lån! ");
        Boolean overdue = checkOverdue();
        if(overdue) {
            System.out.println("Förbereder påminnelseutskick! ");
            String recepient = getOverdueUser();
            Properties properties = new Properties();

            properties.put("mail.smtp.auth", "true");
            properties.put("mail.smtp.starttls.enable", "true");
            properties.put("mail.smtp.host", "smtp.gmail.com");
            properties.put("mail.smtp.port", "587");

            String myAccountEmail = "FKSYG.G16@gmail.com";
            String password = "ABCE1235";

            Session session = Session.getInstance(properties, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(myAccountEmail, password);
                }
            });

            Message message = prepareMessage(session, myAccountEmail, recepient);

            try {
                Transport.send(message);
            } catch (MessagingException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Påminnelsemail kunde ej skickas. ");
                alert.show();
                e.printStackTrace();
                e.getCause();
            }
            System.out.println("Påminnelsemail skickade! ");
        }else System.out.println("Inga överskridna lån finns! ");
    }
//  Formats the message to be sent
    private static Message prepareMessage(Session session, String myAccountEmail, String recepient) {
        Message message = new MimeMessage(session);
        try {
            message.setFrom(new InternetAddress(myAccountEmail));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(recepient));
            message.setSubject("Överskridna lån");
            message.setText("Hej,\nDu har överskridna lån.");
            return message;
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return null;
    }
    //  Fetches the email of a user with overdue loans
    private static String getOverdueUser()  {
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
            DBUtils.closeDBLink(connection, psFetchUser, null, null, resultSet);
        }
        return overdueUser;
    }
    //  Checks if there are any overdue loans
    public static Boolean checkOverdue() {
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
            DBUtils.closeDBLink(connection, psCheckOverdue, null, null, resultSet);
        }
        return false;
    }
}
