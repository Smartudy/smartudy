package com.sharewith.smartudy.dto;

import com.sharewith.smartudy.fragment.WriteFragment;

import java.util.ArrayList;

/**
 * Created by Simjae on 2018-07-23.
 */


public class WriteFragComponent { //빌더 패턴 적용
    String title;
    String content;
    String category;
    String hashtag;
    String subject;
    String money;
    ArrayList<String> images; //파일의 절대 경로(외장메모리/smartudy/~.jpg)
    ArrayList<String> audios;
    ArrayList<String> draws;

    private WriteFragComponent(builder b) {
        this.title = b.title;
        this.content = b.content;
        this.hashtag = b.hashtag;
        this.money = b.money;
        this.subject = b.subject;
        this.images = b.images; //파일의 절대 경로(외장메모리/smartudy/~.jpg)
        this.audios = b.audios;
        this.draws = b.draws;
        this.category = b.category;
    }

    public static class builder{
        String title;
        String content;
        String category;
        String money;
        String hashtag = "";
        String subject;
        ArrayList<String> images= new ArrayList<>(); //파일의 절대 경로(외장메모리/smartudy/~.jpg)
        ArrayList<String> audios= new ArrayList<>();
        ArrayList<String> draws= new ArrayList<>();
        public builder setCategory(String category) {
            this.category = category;
            return this;
        }
        public builder setImages(ArrayList<String> images) {
            this.images = images;return this;
        }
        public builder setAudios(ArrayList<String> audios) {
            this.audios = audios;return this;
        }
        public builder setDraws(ArrayList<String> draws) {
            this.draws = draws;return this;
        }
        public builder() {}
        public builder setTitle(String title) {
            this.title = title;
            return this;
        }
        public builder setContent(String content) {
            this.content = content;
            return this;
        }
        public builder setHashtag(ArrayList<String> hashtag) {
            for(String s : hashtag){
                this.hashtag += "#" + s+" ";
            }
            if(this.hashtag.length() > 0)
                this.hashtag = this.hashtag.substring(0,this.hashtag.length()-1);
            return this;
        }
        public builder setMoney(String money) {
            this.money = money;return this;
        }
        public builder setSubject(ArrayList<String> subject) {
            this.subject = toString(subject);return this;
        }
        public WriteFragComponent build(){
            return new WriteFragComponent(this);
        }
        public String toString(ArrayList<String> list){
            String str = "";
            for(String e : list){
                if(e == null || e.equals("")) continue;
                str += e + ",";
            }
            if(list.size() != 0 ) str = str.substring(0,str.length()-1);
            return str;
        }
    }

    @Override
    public String toString() {
        return "WriteFragComponent{" +
                "title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", category='" + category + '\'' +
                ", hashtag='" + hashtag + '\'' +
                ", subject='" + subject + '\'' +
                ", money='" + money + '\'' +
                ", images=" + images +
                ", audios=" + audios +
                ", draws=" + draws +
                '}';
    }
    public String getTitle() {
        return title;
    }
    public String getContent() {
        return content;
    }
    public String getHashtag() {
        return hashtag;
    }
    public String getSubject() {
        return subject;
    }
    public String getMoney() {
        return money;
    }
    public ArrayList<String> getImages() {
        return images;
    }
    public ArrayList<String> getAudios() {
        return audios;
    }
    public ArrayList<String> getDraws() {
        return draws;
    }
    public String getCategory() {
        return category;
    }
}
