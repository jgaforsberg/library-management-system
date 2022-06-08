package com.lms.librarymanagementsystem.models;

import javafx.beans.property.SimpleIntegerProperty;

import java.util.Date;

//  #011B3E blue
//  #F0F0F0 light gray
@SuppressWarnings("ALL")
public class LoanModel extends MediaModel{
    private Integer loanid, mediaid, userid, returned;
    private Date loandate, returndate;

    public LoanModel()  {}
    public LoanModel(Integer loanid, Integer mediaid, String title, Integer userid, Date loandate, Date returndate, Integer returned) {
        super(mediaid, title);
        this.loanid = loanid;
        this.mediaid = mediaid;
        this.userid = userid;
        this.loandate = loandate;
        this.returndate = returndate;
        this.returned = returned;
    }
    public LoanModel(Integer loanid, Integer mediaid, String title, Date loandate, Date returndate)  {
        super(mediaid, title);
        this.loanid = loanid;
        this.loandate = loandate;
        this.returndate = returndate;
    }
    public Integer getLoanid() {return loanid;}
    public Integer getUserid() {return userid;}
    public Date getLoandate() {return loandate;}
    public Date getReturndate() {return returndate;}
    public Integer returnedProperty() {return returned;}
    public void setLoanid(Integer loanid) {this.loanid = loanid;}
    public void setMediaid(Integer mediaid) {this.mediaid = mediaid;}
    public void setUserid(Integer userid) {this.userid = userid;}
    public void setLoandate(Date loandate) {this.loandate = loandate;}
    public void setReturndate(Date returndate) {this.returndate = returndate;}
    public void setReturned(Integer returned) {
        this.returned = returned;
    }
}
