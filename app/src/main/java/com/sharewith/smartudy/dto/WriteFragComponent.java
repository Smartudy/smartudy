package com.sharewith.smartudy.dto;

/**
 * Created by Simjae on 2018-07-23.
 */

public class WriteFragComponent {
    int type;
    int order;
    String string;

    public WriteFragComponent(int type, int order, String string) {
        this.type = type;
        this.order = order;
        this.string = string;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }
}
