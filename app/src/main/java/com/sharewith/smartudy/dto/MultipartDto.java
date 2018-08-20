package com.sharewith.smartudy.dto;

import java.util.ArrayList;

/**
 * Created by Simjae on 2018-08-13.
 */

public class MultipartDto {
    ArrayList<String> images;
    ArrayList<String> audios;
    ArrayList<String> draws;

    public MultipartDto(ArrayList<String> images, ArrayList<String> audios, ArrayList<String> draws) {
        this.images = images;
        this.audios = audios;
        this.draws = draws;
    }

    public ArrayList<String> getImages() {
        return images;
    }

    public void setImages(ArrayList<String> images) {
        this.images = images;
    }

    public ArrayList<String> getAudios() {
        return audios;
    }

    public void setAudios(ArrayList<String> audios) {
        this.audios = audios;
    }

    public ArrayList<String> getDraws() {
        return draws;
    }

    public void setDraws(ArrayList<String> draws) {
        this.draws = draws;
    }
}
