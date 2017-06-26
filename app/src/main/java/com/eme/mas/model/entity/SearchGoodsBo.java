package com.eme.mas.model.entity;

import java.util.List;

/**
 * 关键字搜索结果商品的基类
 *
 * Created by dijiaoliang on 17/1/16.
 */
public class SearchGoodsBo {


    /**
     * attribute : 10056:138,10071:325
     * attributeEntity : [{"attributeName":"10056","attributeValue":"138"},{"attributeName":"10071","attributeValue":"325"}]
     * categoryId : 1002
     * categoryName : 白酒
     * goodsId : 689ce9577701b1aad07ff9dece8a3466
     * goodsImage :
     * goodsName : 茅台
     * goodsSubtitle : 565
     * id : c466e567-5921-40ef-9bd2-4105cea36f0f
     * keywords : 55
     * marketPrice : 66
     * salePrice : 66
     * saleQuantity : 0
     * skuId : 7E9aZq0qQOq3EjV4SR2ORUTiXyDq61w2Tw1uqhuHS8aK/h1NlFHYy4OQy7JeWNubX5tkZE22KDY=
     * skuName : 450ML_北京333
     */

    private String attribute;
    private String categoryId;
    private String categoryName;
    private String goodsId;
    private String goodsImage;
    private String goodsName;
    private String goodsSubtitle;
    private String id;
    private String keywords;
    private String marketPrice;
    private String salePrice;
    private String saleQuantity;
    private String skuId;
    private String skuName;
    private List<AttributeEntityBean> attributeEntity;

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getMarketPrice() {
        return marketPrice;
    }

    public void setMarketPrice(String marketPrice) {
        this.marketPrice = marketPrice;
    }

    public String getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(String salePrice) {
        this.salePrice = salePrice;
    }

    public String getSaleQuantity() {
        return saleQuantity;
    }

    public void setSaleQuantity(String saleQuantity) {
        this.saleQuantity = saleQuantity;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
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

    public List<AttributeEntityBean> getAttributeEntity() {
        return attributeEntity;
    }

    public void setAttributeEntity(List<AttributeEntityBean> attributeEntity) {
        this.attributeEntity = attributeEntity;
    }

    public static class AttributeEntityBean {
        /**
         * attributeName : 10056
         * attributeValue : 138
         */

        private String attributeName;
        private String attributeValue;

        public String getAttributeName() {
            return attributeName;
        }

        public void setAttributeName(String attributeName) {
            this.attributeName = attributeName;
        }

        public String getAttributeValue() {
            return attributeValue;
        }

        public void setAttributeValue(String attributeValue) {
            this.attributeValue = attributeValue;
        }
    }
}
