package com.sharewith.smartudy.dto;

import com.sharewith.smartudy.utils.TimeUtils;

/**
 * Created by Simjae on 2018-08-21.
 */

public class Answer {
    private int id;
    private String grp;
    private String title;
    private String content;
    private String hashtag;
    private String image;
    private String money;
    private String time;
    private String nickname;
    private String questioncount;
    private String pickrate;
    private String level;

    @Override
    public String toString() {
        return "Answer{" +
                "id=" + id +
                ", grp='" + grp + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", hashtag='" + hashtag + '\'' +
                ", image='" + image + '\'' +
                ", money='" + money + '\'' +
                ", time='" + time + '\'' +
                ", nickname='" + nickname + '\'' +
                ", questioncount='" + questioncount + '\'' +
                ", pickrate='" + pickrate + '\'' +
                ", level='" + level + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getTime() {
        return TimeUtils.getPastTime(time);
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getQuestioncount() {
        return questioncount;
    }

    public void setQuestioncount(String questioncount) {
        this.questioncount = questioncount;
    }

    public String getPickrate() {
        return pickrate;
    }

    public void setPickrate(String pickrate) {
        this.pickrate = pickrate;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }
}
