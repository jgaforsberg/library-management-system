package com.lms.librarymanagementsystem.models;

import java.sql.Date;
@SuppressWarnings("unused")
public class LoanObjectModel {
    private String title;
    private Integer mediaid, loanid;
    private Date loandate, returndate;

    public LoanObjectModel(Integer mediaid, String title, Integer loanid, Date returndate)   {
        this.mediaid = mediaid;
        this.title = title;
        this.loanid = loanid;
        this.returndate = returndate;
    }
    public LoanObjectModel(Integer mediaid, String title, Date loandate, Date returndate)   {
        this.mediaid = mediaid;
        this.title = title;
        this.loandate = loandate;
        this.returndate = returndate;
    }
    public LoanObjectModel()    {
    }
    public Integer getMediaid() {
        return mediaid;
    }
    public String getTitle() {
        return title;
    }
    public Integer getLoanid() {
        return loanid;
    }
    public Date getLoandate()   {return loandate;}
    public Date getReturndate() {return returndate;}
    public void setTitle(String title) {
        this.title = title;
    }
    public void setMediaid(Integer mediaid) {
        this.mediaid = mediaid;
    }
    public void setLoanid(Integer loanid) {
        this.loanid = loanid;
    }
    public void setLoandate(Date loandate) {
        this.loandate = loandate;
    }
    public void setReturndate(Date returndate) {
        this.returndate = returndate;
    }
}

