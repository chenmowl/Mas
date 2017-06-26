package com.eme.mas.model.entity;

/**
 * 提交订单优惠券二级实体
 *
 * Created by dijiaoliang on 16/11/16.
 */

public class OrderCouponBo {

//            "consumptionAmount": 1000, //最低金额（满多少）
//            "couponAmount": 100,     //优惠金额（减多少）
//            "couponCodeId": 10000055, //券码id
//            "couponType": 2,         //券类型:1. 折扣券  2、优惠券 3、抵金券4. 礼品卡
//            "discountRatio": 0,      //折扣率,当且仅当couponType == 1时,有效
//            "endTime": "2017-01-08 14:12:10", //券的有效结束时间
//            "startTime":"2016-11-11 12:12:12", //券的有效开始时间
//            "activeScene":1         //券的获取场景: 1、注册赠券 2 分享赠券 3 老用户回归大礼包 4 系统赠券 5订单满送券 6 积分兑换7 线下发放 8 第三方平台
//            "useRule" : "仅限酒水类商品试用"   //券使用规则提示

    private String consumptionAmount;
    private String couponAmount;
    private String couponCodeId;
    private String couponType;
    private String discountRatio;
    private String endTime;
    private String startTime;
    private String activeScene;
    private String useRule;

    public String getConsumptionAmount() {
        return consumptionAmount;
    }

    public void setConsumptionAmount(String consumptionAmount) {
        this.consumptionAmount = consumptionAmount;
    }

    public String getCouponAmount() {
        return couponAmount;
    }

    public void setCouponAmount(String couponAmount) {
        this.couponAmount = couponAmount;
    }

    public String getCouponCodeId() {
        return couponCodeId;
    }

    public void setCouponCodeId(String couponCodeId) {
        this.couponCodeId = couponCodeId;
    }

    public String getCouponType() {
        return couponType;
    }

    public void setCouponType(String couponType) {
        this.couponType = couponType;
    }

    public String getDiscountRatio() {
        return discountRatio;
    }

    public void setDiscountRatio(String discountRatio) {
        this.discountRatio = discountRatio;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getActiveScene() {
        return activeScene;
    }

    public void setActiveScene(String activeScene) {
        this.activeScene = activeScene;
    }

    public String getUseRule() {
        return useRule;
    }

    public void setUseRule(String useRule) {
        this.useRule = useRule;
    }
}
