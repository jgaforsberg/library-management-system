package com.lms.librarymanagementsystem.models;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class ReservationObjectModel{

    private SimpleStringProperty title;
    private SimpleIntegerProperty mediaid, queuenumber;

    public ReservationObjectModel(Integer mediaid, String title, Integer queuenumber)   {
        this.mediaid = new SimpleIntegerProperty(mediaid);
        this.title = new SimpleStringProperty(title);
        this.queuenumber = new SimpleIntegerProperty(queuenumber);
    }
    public SimpleIntegerProperty mediaidProperty() {
        return mediaid;
    }
    public SimpleStringProperty titleProperty() {
        return title;
    }
    public SimpleIntegerProperty queuenumberProperty() {
        return queuenumber;
    }
}
