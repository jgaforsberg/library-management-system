package com.lms.librarymanagementsystem.models;

import javafx.beans.property.SimpleIntegerProperty;

import java.util.Date;

//  #011B3E blue
//  #F0F0F0 light gray
public class LoanModel {

    private SimpleIntegerProperty loanid, mediaid, userid, returned;
    private Date loandate, returndate;

    public LoanModel(Integer loanid, Integer mediaid, Integer userid, Date loandate, Date returndate, Integer returned) {
        this.loanid = new SimpleIntegerProperty(loanid);
        this.mediaid = new SimpleIntegerProperty(mediaid);
        this.userid = new SimpleIntegerProperty(userid);
        this.loandate = loandate;
        this.returndate = returndate;
        this.returned = new SimpleIntegerProperty(returned);
    }
    @SuppressWarnings("unused")
    public SimpleIntegerProperty loanidProperty() {return loanid;}
    @SuppressWarnings("unused")
    public SimpleIntegerProperty mediaidProperty() {return mediaid;}
    @SuppressWarnings("unused")
    public SimpleIntegerProperty useridProperty() {return userid;}
    @SuppressWarnings("unused")
    public SimpleIntegerProperty returnedProperty() {return returned;}
    @SuppressWarnings("unused")
    public void setLoanid(SimpleIntegerProperty loanid) {this.loanid = loanid;}
    @SuppressWarnings("unused")
    public void setMediaid(SimpleIntegerProperty mediaid) {this.mediaid = mediaid;}
    @SuppressWarnings("unused")
    public void setUserid(SimpleIntegerProperty userid) {this.userid = userid;}
    @SuppressWarnings("unused")
    public Date getLoandate() {return loandate;}
    @SuppressWarnings("unused")
    public void setLoandate(Date loandate) {this.loandate = loandate;}
    @SuppressWarnings("unused")
    public Date getReturndate() {return returndate;}
    @SuppressWarnings("unused")
    public void setReturndate(Date returndate) {this.returndate = returndate;}
}
