package com.eme.mas.model.entity.home;

import java.math.BigDecimal;

/**
 * HomeFragment 中热门推荐的商品基类
 *
 * Created by dijiaoliang on 17/1/12.
 */
public class HomeHotGoodsBo {

//            "goodsImage": "",
//            "goodsName": "茅台",
//            "goodsSubtitle": "565",
//            "marketPrice": 66,
//            "salePrice": 66,
//            "skuId": "7E9aZq0qQOq3EjV4SR2ORUTiXyDq61w2Tw1uqhuHS8aK/h1NlFHYy4OQy7JeWNubX5tkZE22KDY=",
//            "skuName": "450ML_北京333"

    private String goodsImage;
    private String goodsName;
    private String goodsSubtitle;
    private BigDecimal marketPrice;
    private BigDecimal salePrice;
    private String skuId;
    private String skuName;

    public String getGoodsImage() {
        return goodsImage;
    }

    public void setGoodsImage(String goodsImage) {
        this.goodsImage = goodsImage;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getGoodsSubtitle() {
        return goodsSubtitle;
    }

    public void setGoodsSubtitle(String goodsSubtitle) {
        this.goodsSubtitle = goodsSubtitle;
    }

    public BigDecimal getMarketPrice() {
        return marketPrice;
    }

    public void setMarketPrice(BigDecimal marketPrice) {
        this.marketPrice = marketPrice;
    }

    public BigDecimal getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(BigDecimal salePrice) {
        this.salePrice = salePrice;
    }

    public String getSkuId() {
        return skuId;
    }

    public void setSkuId(String skuId) {
        this.skuId = skuId;
    }

    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }
}
