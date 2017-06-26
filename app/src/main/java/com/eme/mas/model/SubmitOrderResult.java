package com.eme.mas.model;

import com.eme.mas.model.entity.SubmitOrderBo;

/**
 *
 * 提交订单实体bean
 *
 * Created by dijiaoliang on 16/11/16.
 */

public class SubmitOrderResult extends Result {

    private SubmitOrderBo data;

    public SubmitOrderBo getData() {
        return data;
    }

    public void setData(SubmitOrderBo data) {
        this.data = data;
    }
}
