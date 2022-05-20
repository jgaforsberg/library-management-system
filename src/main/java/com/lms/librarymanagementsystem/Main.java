package com.lms.librarymanagementsystem;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
//  #011B3E blue
//  #F0F0F0 light gray
public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(Constants.MAIN));
        Scene scene = new Scene(fxmlLoader.load(), 600, 500);
        stage.setTitle("D0024E Bibliotekssystem - Välkommen! ");
        stage.setScene(scene);
        stage.show();
       try {
            MailUtils.sendMail();
        }catch (Exception e)    {
            e.printStackTrace();
            e.getCause();
        }
        stage.setOnCloseRequest(e -> Platform.exit());
    }
    public static void main(String[] args) {
        launch();
    }
}