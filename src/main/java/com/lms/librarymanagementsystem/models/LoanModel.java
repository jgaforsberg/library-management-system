package com.lms.librarymanagementsystem.models;

import java.util.Date;

//  #011B3E blue
//  #F0F0F0 light gray
public class LoanModel {

    private Integer loanid, mediaid, userid;
    private Date loandate, returndate;

    public LoanModel(Integer loanid, Integer mediaid, Integer userid, Date loandate, Date returndate) {
        this.loanid = loanid;
        this.mediaid = mediaid;
        this.userid = userid;
        this.loandate = loandate;
        this.returndate = returndate;
    }


    @SuppressWarnings("unused")
    public Integer getLoanid() {return loanid;}
    @SuppressWarnings("unused")
    public void setLoanid(Integer loanid) {this.loanid = loanid;}
    @SuppressWarnings("unused")
    public Integer getMediaid() {return mediaid;}
    @SuppressWarnings("unused")
    public void setMediaid(Integer mediaid) {this.mediaid = mediaid;}
    @SuppressWarnings("unused")
    public Integer getUserid() {return userid;}
    @SuppressWarnings("unused")
    public void setUserid(Integer userid) {this.userid = userid;}
    @SuppressWarnings("unused")
    public Date getLoandate() {return loandate;}
    @SuppressWarnings("unused")
    public void setLoandate(Date loandate) {this.loandate = loandate;}
    @SuppressWarnings("unused")
    public Date getReturndate() {return returndate;}
    @SuppressWarnings("unused")
    public void setReturndate(Date returndate) {this.returndate = returndate;}
}
