package com.lms.librarymanagementsystem.models;
//  #011B3E blue
//  #F0F0F0 light gray
@SuppressWarnings("ALL")
public class UserModel {
    private Integer userid;
    private String username, password, firstname, lastname, usertype, email;
    public UserModel(Integer userid, String username, String password, String firstname, String lastname, String usertype, String email) {
        this.userid = userid;
        this.username = username;
        this.password = password;
        this.firstname = firstname;
        this.lastname = lastname;
        this.usertype = usertype;
        this.email = email;
    }
    public UserModel(String username)   {this.username = username;}
    public UserModel(Integer userid, String username, String firstname, String lastname, String usertype) {
    }
    public UserModel()  {
    }
    public Integer getUserid() {return userid;}
    public String getUsername() {return username;}
    public String getPassword() {return password;}
    public String getFirstname() {return firstname;}
    public String getLastname() {return lastname;}
    public String getUsertype() {return usertype;}
    public String getEmail()    {return email;}
    public void setUserid(Integer userid) {this.userid = userid;}
    public void setUsername(String username) {this.username = username;}
    public void setPassword(String password) {this.password = password;}
    public void setFirstname(String firstname) {this.firstname = firstname;}
    public void setLastname(String lastname) {this.lastname = lastname;}
    public void setUsertype(String usertype) {this.usertype = usertype;}
    public void setEmail(String email)  {this.email = email;}
}