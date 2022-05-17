package com.lms.librarymanagementsystem.models;

import javafx.beans.property.SimpleIntegerProperty;

import java.util.Date;

//  #011B3E blue
//  #F0F0F0 light gray
@SuppressWarnings("unused")
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
    public SimpleIntegerProperty loanidProperty() {return loanid;}
    public SimpleIntegerProperty mediaidProperty() {return mediaid;}
    public SimpleIntegerProperty useridProperty() {return userid;}
    public SimpleIntegerProperty returnedProperty() {return returned;}
    public Date getLoandate() {return loandate;}
    public Date getReturndate() {return returndate;}
    public void setLoanid(SimpleIntegerProperty loanid) {this.loanid = loanid;}
    public void setMediaid(SimpleIntegerProperty mediaid) {this.mediaid = mediaid;}
    public void setUserid(SimpleIntegerProperty userid) {this.userid = userid;}
    public void setLoandate(Date loandate) {this.loandate = loandate;}
    public void setReturndate(Date returndate) {this.returndate = returndate;}
}
