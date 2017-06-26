package com.eme.mas.model;

import com.eme.mas.model.entity.ProductBo;

import java.util.List;

/**
 * 商品详情信息实体类（商品详情页）
 * <p>
 * Created by dijiaoliang on 16/7/27.
 */
public class ProDetailHotResult extends Result {


    private List<ProductBo> data;

    public List<ProductBo> getData() {
        return data;
    }

    public void setData(List<ProductBo> data) {
        this.data = data;
    }
}
