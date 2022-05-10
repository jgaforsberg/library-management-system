package com.lms.librarymanagementsystem.models;
//  #011B3E blue
//  #F0F0F0 light gray
import java.util.Date;

public class ReservationModel {

    private Integer reservationid, mediaid, userid, queuenumber;
    private Date reservationdate;

    public ReservationModel(Integer reservationid, Integer mediaid, Integer userid, Integer queuenumber, Date reservationdate) {
        this.reservationid = reservationid;
        this.mediaid = mediaid;
        this.userid = userid;
        this.queuenumber = queuenumber;
        this.reservationdate = reservationdate;
    }
    public Integer getReservationid() {return reservationid;}
    public Integer getMediaid() {return mediaid;}
    public Integer getUserid() {return userid;}
    public Integer getQueuenumber() {return queuenumber;}
    public Date getReservationdate() {return reservationdate;}
    public void setReservationid(Integer reservationid) {this.reservationid = reservationid;}
    public void setMediaid(Integer mediaid) {this.mediaid = mediaid;}
    public void setUserid(Integer userid) {this.userid = userid;}
    public void setQueuenumber(Integer queuenumber) {this.queuenumber = queuenumber;}
    public void setReservationdate(Date reservationdate) {this.reservationdate = reservationdate;}
}
