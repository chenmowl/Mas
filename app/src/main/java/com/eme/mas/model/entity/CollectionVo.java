package com.eme.mas.model.entity;

import java.util.List;

/**
 * 收藏页面
 *
 * Created by zhangxiaoming on 16/8/14.
 */
public class CollectionVo {

    private String hasMore;

    public String getHasMore() {
        return hasMore;
    }

    public void setHasMore(String hasMore) {
        this.hasMore = hasMore;
    }

    private List<ProductBo> productList;

    public List<ProductBo> getProductList() {
        return productList;
    }

    public void setProductList(List<ProductBo> productList) {
        this.productList = productList;
    }


}
