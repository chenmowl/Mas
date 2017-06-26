package com.eme.mas.model.entity;

/**
 * Created by Nick on 16/8/11.
 */
public class CouponBo {

    /**
     * 优惠券实体类
     *
     * couponMemberId : 3
     * couponId : 3
     * couponName : 系统券
     * picUrl : 111111111
     * couponClassId : 1
     * useLimitAmount : 800
     * useDiscountAmount : 80
     * useStartTime : 1470400002000
     * useEndTime : 1480413002000
     * couponDesc :
     * useRangeProductType : 2
     * enable : 1
     */

    private String couponMemberId;
    private String couponId;
    private String couponName;
    private String picUrl;
    private int couponClassId;
    private int useLimitAmount;
    private int useDiscountAmount;
    private long useStartTime;
    private long useEndTime;
    private String couponDesc;
    private int useRangeProductType;
    private String enable;

    public String getCouponMemberId() {
        return couponMemberId;
    }

    public void setCouponMemberId(String couponMemberId) {
        this.couponMemberId = couponMemberId;
    }

    public String getCouponId() {
        return couponId;
    }

    public void setCouponId(String couponId) {
        this.couponId = couponId;
    }

    public String getCouponName() {
        return couponName;
    }

    public void setCouponName(String couponName) {
        this.couponName = couponName;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public int getCouponClassId() {
        return couponClassId;
    }

    public void setCouponClassId(int couponClassId) {
        this.couponClassId = couponClassId;
    }

    public int getUseLimitAmount() {
        return useLimitAmount;
    }

    public void setUseLimitAmount(int useLimitAmount) {
        this.useLimitAmount = useLimitAmount;
    }

    public int getUseDiscountAmount() {
        return useDiscountAmount;
    }

    public void setUseDiscountAmount(int useDiscountAmount) {
        this.useDiscountAmount = useDiscountAmount;
    }

    public long getUseStartTime() {
        return useStartTime;
    }

    public void setUseStartTime(long useStartTime) {
        this.useStartTime = useStartTime;
    }

    public long getUseEndTime() {
        return useEndTime;
    }

    public void setUseEndTime(long useEndTime) {
        this.useEndTime = useEndTime;
    }

    public String getCouponDesc() {
        return couponDesc;
    }

    public void setCouponDesc(String couponDesc) {
        this.couponDesc = couponDesc;
    }

    public int getUseRangeProductType() {
        return useRangeProductType;
    }

    public void setUseRangeProductType(int useRangeProductType) {
        this.useRangeProductType = useRangeProductType;
    }

    public String getEnable() {
        return enable;
    }

    public void setEnable(String enable) {
        this.enable = enable;
    }
}
