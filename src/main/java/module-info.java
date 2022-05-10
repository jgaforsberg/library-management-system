module com.lms.librarymanagementsystem {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.java;

    opens com.lms.librarymanagementsystem to javafx.fxml;
    exports com.lms.librarymanagementsystem;
  /*  exports com.lms.librarymanagementsystem.controllers;
    opens com.lms.librarymanagementsystem.controllers to javafx.fxml; */
    exports com.lms.librarymanagementsystem.utils;
    opens com.lms.librarymanagementsystem.utils to javafx.fxml;
    exports com.lms.librarymanagementsystem.controllers;
    opens com.lms.librarymanagementsystem.controllers to javafx.fxml;
    exports com.lms.librarymanagementsystem.models;
    opens com.lms.librarymanagementsystem.models to javafx.fxml;
}