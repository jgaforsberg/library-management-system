package com.lms.librarymanagementsystem.models;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.sql.Date;

public class LoanObjectModel {
    private SimpleStringProperty title;
    private SimpleIntegerProperty mediaid, loanid;
    private Date returndate;

    public LoanObjectModel(Integer mediaid, String title, Integer loanid, Date returndate)   {
        this.mediaid = new SimpleIntegerProperty(mediaid);
        this.title = new SimpleStringProperty(title);
        this.loanid = new SimpleIntegerProperty(loanid);
        this.returndate = returndate;
    }
    public SimpleIntegerProperty mediaidProperty() {
        return mediaid;
    }
    public SimpleStringProperty titleProperty() {
        return title;
    }
    public SimpleIntegerProperty queuenumberProperty() {
        return loanid;
    }
    public Date getReturndate() {return returndate;}
}
