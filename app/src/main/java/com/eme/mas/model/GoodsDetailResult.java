package com.eme.mas.model;

import com.eme.mas.model.entity.GoodsDetailVo;

/**
 * 商品详情实体类
 *
 * Created by dijiaoliang on 17/1/18.
 */
public class GoodsDetailResult extends Result {

    private GoodsDetailVo data;

    public GoodsDetailVo getData() {
        return data;
    }

    public void setData(GoodsDetailVo data) {
        this.data = data;
    }
}
