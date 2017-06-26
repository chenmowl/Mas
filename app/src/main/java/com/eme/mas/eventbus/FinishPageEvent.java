package com.eme.mas.eventbus;

/**
 * Created by dijiaoliang on 16/8/18.
 */
public class FinishPageEvent {

    private String tag;

    public FinishPageEvent() {
    }

    public FinishPageEvent(String tag) {
        this.tag = tag;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
