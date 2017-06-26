package com.eme.mas.model.entity;

import java.math.BigDecimal;

/**
 * 总评论数，好评数，怀评数，中评数及好评率
 *
 * Created by dijiaoliang on 17/1/18.
 */
public class ScoreFlagBo {

    private String skuId;  //商品skuId
    private String orderId;//订单编号
    private Integer totalPoints;//总评价数
    private Integer highPoints; //好评数
    private Integer lowPoints; //差评数
    private Integer middllePoints;//中评数
    private BigDecimal favorableRate;//好评率
    private Integer speedStarMean;//发货速度平均的星数
    private Integer serviceStarMean;//服务态度平均得星数

    public String getSkuId() {
        return skuId;
    }

    public void setSkuId(String skuId) {
        this.skuId = skuId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Integer getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(Integer totalPoints) {
        this.totalPoints = totalPoints;
    }

    public Integer getHighPoints() {
        return highPoints;
    }

    public void setHighPoints(Integer highPoints) {
        this.highPoints = highPoints;
    }

    public Integer getLowPoints() {
        return lowPoints;
    }

    public void setLowPoints(Integer lowPoints) {
        this.lowPoints = lowPoints;
    }

    public Integer getMiddllePoints() {
        return middllePoints;
    }

    public void setMiddllePoints(Integer middllePoints) {
        this.middllePoints = middllePoints;
    }

    public BigDecimal getFavorableRate() {
        return favorableRate;
    }

    public void setFavorableRate(BigDecimal favorableRate) {
        this.favorableRate = favorableRate;
    }

    public Integer getSpeedStarMean() {
        return speedStarMean;
    }

    public void setSpeedStarMean(Integer speedStarMean) {
        this.speedStarMean = speedStarMean;
    }

    public Integer getServiceStarMean() {
        return serviceStarMean;
    }

    public void setServiceStarMean(Integer serviceStarMean) {
        this.serviceStarMean = serviceStarMean;
    }
}
