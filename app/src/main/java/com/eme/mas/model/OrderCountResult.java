package com.eme.mas.model;

import com.eme.mas.model.entity.OrderCountBo;

/**
 * 订单详情接口实体类
 * Created by zulei on 16/8/15.
 */
public class OrderCountResult extends Result {

    private OrderCountBo data;

    public OrderCountBo getData() {
        return data;
    }

    public void setData(OrderCountBo data) {
        this.data = data;
    }
}
