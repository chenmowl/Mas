package com.eme.mas.model.entity;

import com.eme.mas.model.entity.home.HomeHotGoodsBo;

import java.util.List;

/**
 * CategoryActivity页
 * 商品搜索结果二级实体类
 *
 * Created by dijiaoliang on 16/7/27.
 */
public class CategorySearchVo {

    private List<HomeHotGoodsBo> product_list;

    private String hasMore;

    public List<HomeHotGoodsBo> getProduct_list() {
        return product_list;
    }

    public void setProduct_list(List<HomeHotGoodsBo> product_list) {
        this.product_list = product_list;
    }

    public String getHasMore() {
        return hasMore;
    }

    public void setHasMore(String hasMore) {
        this.hasMore = hasMore;
    }
}
