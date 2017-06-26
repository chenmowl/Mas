package com.eme.mas.eventbus;

/**
 * Created by dijiaoliang on 16/8/18.
 */
public class SwitchMainFragmentEvent {

    private int flag;

    public SwitchMainFragmentEvent() {
    }

    public SwitchMainFragmentEvent(int flag) {
        this.flag = flag;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }
}
