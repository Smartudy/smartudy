package com.sharewith.smartudy.directory;

/**
 * Created by Simjae on 2018-08-03.
 */

public class HttpResultVO { //서버로부터 넘어온 json을 매핑하기 위한 Value Object
    boolean success;
    String error;

    public HttpResultVO(boolean success, String error) {
        this.success = success;
        this.error = error;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
