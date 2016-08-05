package com.felhr.serialportexample;

import java.io.Serializable;

public class MyNotification implements Serializable{

    private String title;
    private String msg;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
