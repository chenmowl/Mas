package com.eme.mas.model.entity;

/**
 * 商品详情信息接口的二级实体类
 * <p/>
 * Created by dijiaoliang on 16/8/18.
 */
public class ProDetailInfoVo {

    private String is_collect;//是否收藏

    private ProductBo product;

    public String getIs_collect() {
        return is_collect;
    }

    public void setIs_collect(String is_collect) {
        this.is_collect = is_collect;
    }

    public ProductBo getProduct() {
        return product;
    }

    public void setProduct(ProductBo product) {
        this.product = product;
    }
}
