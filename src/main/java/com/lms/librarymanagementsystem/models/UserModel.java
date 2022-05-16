package com.lms.librarymanagementsystem.models;
//  #011B3E blue
//  #F0F0F0 light gray
public class UserModel {
    private Integer userid;
    private String username, password, firstname, lastname, usertype;
    @SuppressWarnings("unused")
    public UserModel(Integer userid, String username, String password, String firstname, String lastname, String usertype) {
        this.userid = userid;
        this.username = username;
        this.password = password;
        this.firstname = firstname;
        this.lastname = lastname;
        this.usertype = usertype;
    }
    @SuppressWarnings("unused")
    public UserModel(String username)   {this.username = username;}

    public UserModel(Integer userid, String username, String firstname, String lastname, String usertype) {

    }
    @SuppressWarnings("unused")
    public Integer getUserid() {return userid;}
    @SuppressWarnings("unused")
    public String getUsername() {return username;}
    @SuppressWarnings("unused")
    public String getPassword() {return password;}
    @SuppressWarnings("unused")
    public String getFirstname() {return firstname;}
    @SuppressWarnings("unused")
    public String getLastname() {return lastname;}
    @SuppressWarnings("unused")
    public String getUsertype() {return usertype;}
    @SuppressWarnings("unused")
    public void setUserid(Integer userid) {this.userid = userid;}
    @SuppressWarnings("unused")
    public void setUsername(String username) {this.username = username;}
    @SuppressWarnings("unused")
    public void setPassword(String password) {this.password = password;}
    @SuppressWarnings("unused")
    public void setFirstname(String firstname) {this.firstname = firstname;}
    @SuppressWarnings("unused")
    public void setLastname(String lastname) {this.lastname = lastname;}
    @SuppressWarnings("unused")
    public void setUsertype(String usertype) {this.usertype = usertype;}
}