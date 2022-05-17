package com.lms.librarymanagementsystem.models;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.sql.Date;
@SuppressWarnings("unused")
public class LoanObjectModel {
    private String title;
    private Integer mediaid, loanid;
    private Date returndate;

    public LoanObjectModel(Integer mediaid, String title, Integer loanid, Date returndate)   {
        this.mediaid = mediaid;
        this.title = title;
        this.loanid = loanid;
        this.returndate = returndate;
    }
    public Integer getMediaid() {
        return mediaid;
    }
    public String titleProperty() {
        return title;
    }
    public Integer getLoanid() {
        return loanid;
    }
    public Date getReturndate() {return returndate;}
}
