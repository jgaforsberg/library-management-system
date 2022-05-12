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
    @SuppressWarnings("unused")
    public Integer getReservationid() {return reservationid;}
    @SuppressWarnings("unused")
    public Integer getMediaid() {return mediaid;}
    @SuppressWarnings("unused")
    public Integer getUserid() {return userid;}
    @SuppressWarnings("unused")
    public Integer getQueuenumber() {return queuenumber;}
    @SuppressWarnings("unused")
    public Date getReservationdate() {return reservationdate;}
    @SuppressWarnings("unused")
    public void setReservationid(Integer reservationid) {this.reservationid = reservationid;}
    @SuppressWarnings("unused")
    public void setMediaid(Integer mediaid) {this.mediaid = mediaid;}
    @SuppressWarnings("unused")
    public void setUserid(Integer userid) {this.userid = userid;}
    @SuppressWarnings("unused")
    public void setQueuenumber(Integer queuenumber) {this.queuenumber = queuenumber;}
    @SuppressWarnings("unused")
    public void setReservationdate(Date reservationdate) {this.reservationdate = reservationdate;}
}
