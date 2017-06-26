package com.eme.mas.model.entity.home;

/**
 *
 * HomeFragment 手机专享的商品基类
 *
 * Created by dijiaoliang on 17/1/12.
 */
public class HomeMobileVipBo {


//            "goodsFixCount": 1,
//            "goodsImg": "",
//            "goodsName": "茅台",
//            "goodsSkuId": "7E9aZq0qQOq3EjV4SR2ORUTiXyDq61w2Tw1uqhuHS8aK/h1NlFHYy4OQy7JeWNubX5tkZE22KDY=",
//            "goodsSkuName": "450ML_北京333",
//            "labelImg": "/upload/img/label/1481868354285.png",
//            "labelPrice": 11,
//            "limitCount": 1,
//            "salePrice": 343

    private String goodsFixCount;
    private String goodsImg;
    private String goodsName;
    private String goodsSkuId;
    private String goodsSkuName;
    private String labelImg;
    private String labelPrice;
    private String limitCount;
    private String salePrice;

    public String getGoodsFixCount() {
        return goodsFixCount;
    }

    public void setGoodsFixCount(String goodsFixCount) {
        this.goodsFixCount = goodsFixCount;
    }

    public String getGoodsImg() {
        return goodsImg;
    }

    public void setGoodsImg(String goodsImg) {
        this.goodsImg = goodsImg;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getGoodsSkuId() {
        return goodsSkuId;
    }

    public void setGoodsSkuId(String goodsSkuId) {
        this.goodsSkuId = goodsSkuId;
    }

    public String getGoodsSkuName() {
        return goodsSkuName;
    }

    public void setGoodsSkuName(String goodsSkuName) {
        this.goodsSkuName = goodsSkuName;
    }

    public String getLabelImg() {
        return labelImg;
    }

    public void setLabelImg(String labelImg) {
        this.labelImg = labelImg;
    }

    public String getLabelPrice() {
        return labelPrice;
    }

    public void setLabelPrice(String labelPrice) {
        this.labelPrice = labelPrice;
    }

    public String getLimitCount() {
        return limitCount;
    }

    public void setLimitCount(String limitCount) {
        this.limitCount = limitCount;
    }

    public String getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(String salePrice) {
        this.salePrice = salePrice;
    }
}
