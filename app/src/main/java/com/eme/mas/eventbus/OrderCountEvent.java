package com.eme.mas.eventbus;

/**
 * 商品详情的EventBus事件类
 *
 * Created by dijiaoliang on 16/8/18.
 */
public class OrderCountEvent {

    private String tag;
    private String count;

    public OrderCountEvent() {
    }
    public OrderCountEvent(String tag,String count) {
        this.tag = tag;
        this.count = count;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }


    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
