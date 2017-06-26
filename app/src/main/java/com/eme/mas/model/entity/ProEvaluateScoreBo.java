package com.eme.mas.model.entity;

/**
 * 商品评价各项指标接口 基实体类（商品详情页）
 * <p>
 * Created by dijiaoliang on 16/8/16.
 */
public class ProEvaluateScoreBo {

//            "salesVolume": 25,
//            "goodsScore": null,
//            "deliveryScore": null,
//            "serviceScore": null,
//            "favorableRate": null

    private String salesVolume;//售出数量
    private String goodsScore;//商品评价分数
    private String deliveryScore;//发货速度分数
    private String serviceScore;//服务态度分数
    private String favorableRate;//好评率

    public String getSalesVolume() {
        return salesVolume;
    }

    public void setSalesVolume(String salesVolume) {
        this.salesVolume = salesVolume;
    }

    public String getGoodsScore() {
        return goodsScore;
    }

    public void setGoodsScore(String goodsScore) {
        this.goodsScore = goodsScore;
    }

    public String getDeliveryScore() {
        return deliveryScore;
    }

    public void setDeliveryScore(String deliveryScore) {
        this.deliveryScore = deliveryScore;
    }

    public String getServiceScore() {
        return serviceScore;
    }

    public void setServiceScore(String serviceScore) {
        this.serviceScore = serviceScore;
    }

    public String getFavorableRate() {
        return favorableRate;
    }

    public void setFavorableRate(String favorableRate) {
        this.favorableRate = favorableRate;
    }
}
