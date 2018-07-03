package com.sharewith.smartudy.utils;


import android.graphics.Path;

/**
 * Created by Simjae on 2018-07-01.
 */

public class CustomPath {
    public int color;
    Path path;
    public int strokeWidth;

    public CustomPath(int color, Path path, int strokeWidth) {
        this.color = color;
        this.path = path;
        this.strokeWidth = strokeWidth;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public Path getPath() {
        return path;
    }

    public void setPath(Path path) {
        this.path = path;
    }

    public int getStrokeWidth() {
        return strokeWidth;
    }

    public void setStrokeWidth(int strokeWidth) {
        this.strokeWidth = strokeWidth;
    }
}
