package com.eme.mas.model;

import com.eme.mas.model.entity.CartBo;

import java.util.List;

/**
 * 购物车实体类（购物车页）
 * <p/>
 * Created by dijiaoliang on 16/7/27.
 */
public class CartListResult extends Result {


    private List<CartBo> data;

    public List<CartBo> getData() {
        return data;
    }

    public void setData(List<CartBo> data) {
        this.data = data;
    }
}
