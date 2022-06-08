package com.lms.librarymanagementsystem.models;
//  #011B3E blue
//  #F0F0F0 light gray
import javafx.beans.property.SimpleIntegerProperty;
import java.util.Date;
@SuppressWarnings("ALL")
public class ReservationModel extends MediaModel{
    private Integer reservationid, mediaid, userid, queuenumber;
    private Date reservationdate;

    public ReservationModel()   {}
    public ReservationModel(Integer reservationid, Integer mediaid, String title,Integer userid, Integer queuenumber, Date reservationdate) {
        super(mediaid, title);
        this.reservationid = reservationid;
        this.userid = userid;
        this.queuenumber = queuenumber;
        this.reservationdate = reservationdate;
    }

    public ReservationModel(Integer reservationid, Integer mediaid, String title, Integer queuenumber, Date reservationdate) {
        super(mediaid, title);
        this.reservationid = reservationid;
        this.queuenumber = queuenumber;
        this.reservationdate = reservationdate;
    }

    public Integer getReservationid() {return reservationid;}
    public Integer getUserid() {return userid;}
    public Integer getQueuenumber() {return queuenumber;}
    public Date getReservationdate() {return reservationdate;}
    public void setReservationid(Integer reservationid) {this.reservationid = reservationid;}
    public void setUserid(Integer userid) {this.userid = userid;}
    public void setQueuenumber(Integer queuenumber) {this.queuenumber = queuenumber;}
    public void setReservationdate(Date reservationdate) {this.reservationdate = reservationdate;}
}
