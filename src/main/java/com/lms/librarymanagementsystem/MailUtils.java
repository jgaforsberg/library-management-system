package com.lms.librarymanagementsystem;

import javafx.scene.control.Alert;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class MailUtils {
//  If conditions are true, reminder emails are sent to accounts with overdue loans
    public static void sendMail() {
        System.out.println("Kollar överskridna lån! ");
        Boolean overdue = DBUtils.checkOverdue();
        if(overdue) {
            System.out.println("Förbereder påminnelseutskick! ");
            String recepient = DBUtils.getOverdueUser();
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
        }
        else System.out.println("Inga överskridna lån finns! ");
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

}
