package com.eme.mas.model;

/**
 * 计算订单的应付金额,优惠金额,邮费,商品总额 (提交订单)
 *
 * Created by dijiaoliang on 16/11/16.
 */

public class OrderAmountPayableResult extends Result {


    /**
     * discountAmount : 100
     * goodsAmount : 1000
     * orderAmount : 900
     * shippingFee : 0
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private String discountAmount;
        private String goodsAmount;
        private String orderAmount;
        private String shippingFee;

        public String getDiscountAmount() {
            return discountAmount;
        }

        public void setDiscountAmount(String discountAmount) {
            this.discountAmount = discountAmount;
        }

        public String getGoodsAmount() {
            return goodsAmount;
        }

        public void setGoodsAmount(String goodsAmount) {
            this.goodsAmount = goodsAmount;
        }

        public String getOrderAmount() {
            return orderAmount;
        }

        public void setOrderAmount(String orderAmount) {
            this.orderAmount = orderAmount;
        }

        public String getShippingFee() {
            return shippingFee;
        }

        public void setShippingFee(String shippingFee) {
            this.shippingFee = shippingFee;
        }
    }
}
