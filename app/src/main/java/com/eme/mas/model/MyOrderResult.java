package com.eme.mas.model;

import com.eme.mas.model.entity.MyOrderBo;

import java.util.List;

/**
 * 我的订单实体类
 * Created by zulei on 16/8/15.
 */
public class MyOrderResult extends Result {

    private List<MyOrderBo> data;

    public List<MyOrderBo> getData() {
        return data;
    }

    public void setData(List<MyOrderBo> data) {
        this.data = data;
    }
}
