package com.sharewith.smartudy.dto;

/**
 * Created by cheba on 2018-07-23.
 */

public class AccountDto {
    private String Nick;
    private String Password;
    private String Phone;
    private String Major;
    private String Grade;

    public AccountDto() {
    }

    public AccountDto(String Nick, String Password, String Phone, String Major, String Grade) {
        this.Nick = Nick;
        this.Password = Password;
        this.Phone = Phone;
        this.Major = Major;
        this.Grade = Grade;
    }

    public String getPhone() {
        return Phone;
    }
    public void setPhone(String Phone) {
        this.Phone = Phone;
    }
    public String getNick() {
        return Nick;
    }
    public void setNick(String Nick) {
        this.Nick = Nick;
    }
    public String getPassword() {
        return Password;
    }
    public void setPassword(String Password) {
        this.Password = Password;
    }
    public String getMajor() {
        return Major;
    }
    public void setajor(String Major) {
        this.Major = Major;
    }
    public String getGrade() {
        return Grade;
    }
    public void setGrade(String Grade) {
        this.Grade = Grade;
    }
}
