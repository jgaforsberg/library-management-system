package com.lms.librarymanagementsystem.models;
//  #011B3E blue
//  #F0F0F0 light gray
import javafx.beans.property.SimpleIntegerProperty;
import java.util.Date;
@SuppressWarnings("ALL")
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
    public SimpleIntegerProperty reservationidProperty() {return reservationid;}
    public SimpleIntegerProperty mediaidProperty() {return mediaid;}
    public SimpleIntegerProperty useridProperty() {return userid;}
    public SimpleIntegerProperty queuenumberProperty() {return queuenumber;}
    public Date getReservationdate() {return reservationdate;}
    public void setReservationid(SimpleIntegerProperty reservationid) {this.reservationid = reservationid;}
    public void setMediaid(SimpleIntegerProperty mediaid) {this.mediaid = mediaid;}
    public void setUserid(SimpleIntegerProperty userid) {this.userid = userid;}
    public void setQueuenumber(SimpleIntegerProperty queuenumber) {this.queuenumber = queuenumber;}
    public void setReservationdate(Date reservationdate) {this.reservationdate = reservationdate;}
}
