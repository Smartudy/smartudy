package com.sharewith.smartudy.dto;

/**
 * Created by Simjae on 2018-08-13.
 */

public class WriteRecyclerDto {
    private int type;
    private String str;

    public WriteRecyclerDto(int type, String str) {
        this.type = type;
        this.str = str;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }
}
