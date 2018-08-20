package com.sharewith.smartudy.dto;


import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by cheba on 2018-07-09.
 */

public class Question {
    private String title;
    private String content;
    private String hashtag;
    private String image1;
    private String money;
    private String time;

    public Question() {
    }
    public Question(String title, String content, String hashtag, String image1, String money, String time) {
        this.title = title;
        this.content = content;
        this.hashtag = hashtag;
        this.image1 = image1;
        this.money = money;
        this.time = time;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public String getHashtag() {
        return hashtag;
    }
    public void setHashtag(String hashtag) {
        this.hashtag = hashtag;
    }
    public String getMoney() {
        return money;
    }
    public void setMoney(String money) {
        this.money = money;
    }
    public String getTime() {
        return time;
    }
    public void setTime(String time) {
        this.time = time;
    }
    public String getimage1() {
        return image1;
    }
    public void setimage1(String image1) {
        this.image1 = image1;
    }

    public String getFormattedTime(){
        String resulttime = null;
        SimpleDateFormat format = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
        try {
            long lasttime = format.parse(time).getTime();
            long curtime = format.parse(format.format(new Date())).getTime();
            int minute = (int)((curtime-lasttime) / 60000);
            int hour = minute/60;
            int day = hour/24;
            minute = minute % 60;
            if(hour == 0){
                resulttime = "방금 전";
            }
            else if(day == 0){
                if(minute != 0)
                    resulttime = hour +"h "+minute+"m";
                else
                    resulttime = hour + "h ";
            }
            else {
                resulttime = day + "day";
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        Log.d("Question",resulttime+"에 등록된 게시글을 가져옴");
        return resulttime;
    }
    @Override
    public String toString() {
        return "Question{" +
                "title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", hashtag='" + hashtag + '\'' +
                ", image1='" + image1 + '\'' +
                ", money='" + money + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}
