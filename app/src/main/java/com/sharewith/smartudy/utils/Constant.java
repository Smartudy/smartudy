package com.sharewith.smartudy.utils;

/**
 * Created by Simjae on 2018-07-30.
         */

public class Constant {
    public static final String SERVER_IP = "192.168.43.200";
    public static final String SERVER_PORT = "8181";
    public static final String ContextPath = "/smartudy";
    public static final String URL = "http://"+SERVER_IP+":"+SERVER_PORT+ContextPath; // http://localhost:8181/smartudy
    public static final String RegisterURL = URL+"/member/join";
    public static final String LoginURL = URL+"/member/login";
    public static final String PostQuestionURL = URL+"/board/post/question";
    public static final String PostAnswerURL = URL+"/board/post/answer";
    public static final String ListPageURL = URL+"/board/listpage";
    public static final String GetQuestionCountURL = URL+"/board/questioncount";
    public static final String GetQuestionURL = URL+"/board/question";
    public static final String GetAnswerURL = URL+"/board/answer";

    public static final String REGISTER_OK = "회원가입이 정상 처리 되었습니다.";
    public static final String NICK_DUPLICATED = "닉네임이 중복되었습니다";
    public static final String LOGIN_FAIL = "연락처와 비밀번호가 일치 하지 않습니다";
    public static final String NO_MEMBER = "가입 되지 않은 회원입니다";
}
