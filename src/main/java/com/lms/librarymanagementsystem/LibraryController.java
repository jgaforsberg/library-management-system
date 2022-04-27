package com.lms.librarymanagementsystem;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class LibraryController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }

    public void login() {
        // TODO Login function
    }
}