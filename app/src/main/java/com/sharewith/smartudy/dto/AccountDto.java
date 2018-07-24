package com.sharewith.smartudy.dto;

/**
 * Created by cheba on 2018-07-23.
 */

public class AccountDto {
    private String a_tel_number;
    private String a_nick;
    private String a_password;
    private String a_major;
    private int a_grade;

    public AccountDto(String a_tel_number, String a_nick, String a_password, String a_major, int a_grade) {
        this.a_tel_number = a_tel_number;
        this.a_nick = a_nick;
        this.a_password = a_password;
        this.a_major = a_major;
        this.a_grade = a_grade;
    }

    public String getA_tel_number() {
        return a_tel_number;
    }
    public void setA_tel_number(String a_tel_number) {
        this.a_tel_number = a_tel_number;
    }
    public String getA_nick() {
        return a_nick;
    }
    public void setA_nick(String a_nick) {
        this.a_nick = a_nick;
    }
    public String getA_password() {
        return a_password;
    }
    public void setA_password(String a_password) {
        this.a_password = a_password;
    }
    public String getA_major() {
        return a_major;
    }
    public void setA_major(String a_major) {
        this.a_major = a_major;
    }
    public int getA_grade() {
        return a_grade;
    }
    public void setA_grade(int a_grade) {
        this.a_grade = a_grade;
    }
}
