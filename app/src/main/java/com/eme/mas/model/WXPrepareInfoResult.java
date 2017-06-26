package com.eme.mas.model;

import com.eme.mas.model.entity.WXPrepareBo;

/**
 * wx pre
 * Created by zulei on 16/8/15.
 */
public class WXPrepareInfoResult extends Result {

    private WXPrepareBo data;

    public WXPrepareBo getData() {
        return data;
    }

    public void setData(WXPrepareBo data) {
        this.data = data;
    }
}
