package com.eme.mas.model;

import com.eme.mas.model.entity.GoodsCategoryVo;

/**
 * 商品分类的实体类
 *
 * Created by dijiaoliang on 17/1/20.
 */
public class GoodsCategoryResult extends Result {

    private GoodsCategoryVo data;

    public GoodsCategoryVo getData() {
        return data;
    }

    public void setData(GoodsCategoryVo data) {
        this.data = data;
    }
}
