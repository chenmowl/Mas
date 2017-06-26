package com.eme.mas.model;

import com.eme.mas.model.entity.OrderCouponBo;

import java.util.List;

/**
 * 提交订单的优惠券列表bean
 *
 * Created by dijiaoliang on 16/11/16.
 */

public class OrderCouponResult extends Result {

    private List<OrderCouponBo> data;

    public List<OrderCouponBo> getData() {
        return data;
    }

    public void setData(List<OrderCouponBo> data) {
        this.data = data;
    }
}
