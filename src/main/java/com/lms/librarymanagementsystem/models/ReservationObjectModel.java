package com.lms.librarymanagementsystem.models;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
@SuppressWarnings("unused")
public class ReservationObjectModel{

    private String title;
    private Integer mediaid, reservationid, queuenumber;

    public ReservationObjectModel(Integer mediaid, String title, Integer reservationid, Integer queuenumber)   {
        this.mediaid = mediaid;
        this.title = title;
        this.reservationid = reservationid;
        this.queuenumber = queuenumber;
    }
    public Integer getMediaid() {
        return mediaid;
    }
    public String getTitle() {
        return title;
    }
    public Integer getReservationid(){return reservationid;}
    public Integer getQueuenumber() {
        return queuenumber;
    }
}
