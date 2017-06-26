package com.eme.mas.eventbus;

/**
 * 商品详情的EventBus事件类
 *
 * Created by dijiaoliang on 16/8/18.
 */
public class LoadingEvent {

    private String tag;

    public LoadingEvent() {
    }

    public LoadingEvent(String tag) {
        this.tag = tag;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
