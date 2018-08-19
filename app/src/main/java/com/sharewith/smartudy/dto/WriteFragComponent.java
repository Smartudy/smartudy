package com.sharewith.smartudy.dto;

/**
 * Created by Simjae on 2018-07-23.
 */

public class WriteFragComponent {
    int type;//텍스트, 사진, 음성, 그리기 중 어떤 타입인가?
    int order; // 리싸이클러뷰에서 몇번째에 표시될것인가?
    String string; //텍스트 또는 이미지 파일의 절대 경로(외장메모리/smartudy/~.jpg)

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
