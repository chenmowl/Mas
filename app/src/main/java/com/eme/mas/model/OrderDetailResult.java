package com.eme.mas.model;

import com.eme.mas.model.entity.MyOrderBo;

/**
 * 订单详情接口实体类
 * Created by zulei on 16/8/15.
 */
public class OrderDetailResult extends Result {

    private MyOrderBo data;

    public MyOrderBo getData() {
        return data;
    }

    public void setData(MyOrderBo data) {
        this.data = data;
    }
}
