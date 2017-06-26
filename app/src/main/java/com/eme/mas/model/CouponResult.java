package com.eme.mas.model;

import com.eme.mas.model.entity.CouponVo;

/**
 * 我的地址实体类
 * <p/>
 * Created by zulei on 16/8/1.
 */
public class CouponResult extends Result {

    private CouponVo data;

    public CouponVo getData() {
        return data;
    }

    public void setData(CouponVo data) {
        this.data = data;
    }
}
