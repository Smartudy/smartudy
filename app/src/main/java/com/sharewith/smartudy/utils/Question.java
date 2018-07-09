package com.sharewith.smartudy.utils;

import android.media.Image;

import java.util.Date;

/**
 * Created by cheba on 2018-07-09.
 */

public class Question {
    private String q_title;
    private String q_content;
    private String[] q_hashtag_array;
    private int q_price;
    private Date q_due_time;
    private Image q_image;

    public String getQ_title() {
        return q_title;
    }

    public void setQ_title(String q_title) {
        this.q_title = q_title;
    }

    public String getQ_content() {
        return q_content;
    }

    public void setQ_content(String q_content) {
        this.q_content = q_content;
    }

    public String[] getQ_hashtag_array() {
        return q_hashtag_array;
    }

    public void setQ_hashtag_array(String[] q_hashtag_array) {
        this.q_hashtag_array = q_hashtag_array;
    }

    public int getQ_price() {
        return q_price;
    }

    public void setQ_price(int q_price) {
        this.q_price = q_price;
    }

    public Date getQ_due_time() {
        return q_due_time;
    }

    public void setQ_due_time(Date q_due_time) {
        this.q_due_time = q_due_time;
    }

    public Image getQ_image() {
        return q_image;
    }

    public void setQ_image(Image q_image) {
        this.q_image = q_image;
    }
}
