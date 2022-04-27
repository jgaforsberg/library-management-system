module com.lms.librarymanagementsystem {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;

    opens com.lms.librarymanagementsystem to javafx.fxml;
    exports com.lms.librarymanagementsystem;
}