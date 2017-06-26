package com.eme.mas.model;

import com.eme.mas.model.entity.BrotherLocationBo;

/**
 * 个人信息实体类
 * Created by zulei on 16/8/4.
 */
public class BrotherLocationResult extends Result {

    private BrotherLocationBo data;

    public BrotherLocationBo getData() {
        return data;
    }

    public void setData(BrotherLocationBo data) {
        this.data = data;
    }
}
