package me.lyz.eventbus;

/**
 * Created by LYZ on 2018/2/12 0012.
 */

public class FirstEvent {
    private String msg;

    public FirstEvent(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
