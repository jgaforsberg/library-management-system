package com.lms.librarymanagementsystem.models;
//  #011B3E blue
//  #F0F0F0 light gray
import javafx.beans.property.SimpleIntegerProperty;

import java.util.Date;

public class ReservationModel {

    private SimpleIntegerProperty reservationid, mediaid, userid, queuenumber;
    private Date reservationdate;

    public ReservationModel(Integer reservationid, Integer mediaid, Integer userid, Integer queuenumber, Date reservationdate) {
        this.reservationid = new SimpleIntegerProperty(reservationid);
        this.mediaid = new SimpleIntegerProperty(mediaid);
        this.userid = new SimpleIntegerProperty(userid);
        this.queuenumber = new SimpleIntegerProperty(queuenumber);
        this.reservationdate = reservationdate;
    }
    @SuppressWarnings("unused")
    public SimpleIntegerProperty reservationidProperty() {return reservationid;}
    @SuppressWarnings("unused")
    public SimpleIntegerProperty mediaidProperty() {return mediaid;}
    @SuppressWarnings("unused")
    public SimpleIntegerProperty useridProperty() {return userid;}
    @SuppressWarnings("unused")
    public SimpleIntegerProperty queuenumberProperty() {return queuenumber;}
    @SuppressWarnings("unused")
    public Date getReservationdate() {return reservationdate;}
    @SuppressWarnings("unused")
    public void setReservationid(SimpleIntegerProperty reservationid) {this.reservationid = reservationid;}
    @SuppressWarnings("unused")
    public void setMediaid(SimpleIntegerProperty mediaid) {this.mediaid = mediaid;}
    @SuppressWarnings("unused")
    public void setUserid(SimpleIntegerProperty userid) {this.userid = userid;}
    @SuppressWarnings("unused")
    public void setQueuenumber(SimpleIntegerProperty queuenumber) {this.queuenumber = queuenumber;}
    @SuppressWarnings("unused")
    public void setReservationdate(Date reservationdate) {this.reservationdate = reservationdate;}
}
