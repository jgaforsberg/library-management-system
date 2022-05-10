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
    public Integer getLoanid() {return loanid;}
    public void setLoanid(Integer loanid) {this.loanid = loanid;}
    public Integer getMediaid() {return mediaid;}
    public void setMediaid(Integer mediaid) {this.mediaid = mediaid;}
    public Integer getUserid() {return userid;}
    public void setUserid(Integer userid) {this.userid = userid;}
    public Date getLoandate() {return loandate;}
    public void setLoandate(Date loandate) {this.loandate = loandate;}
    public Date getReturndate() {return returndate;}
    public void setReturndate(Date returndate) {this.returndate = returndate;}
}
