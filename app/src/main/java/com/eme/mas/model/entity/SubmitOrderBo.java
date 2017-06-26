package com.eme.mas.model.entity;

/**
 * 提交订单成功的二级实体
 *
 * Created by dijiaoliang on 16/11/16.
 */

public class SubmitOrderBo {

//            "addressId": "e52aee4298c640e9b82e40288f202152",
//            "balanceState": 0,
//            "balanceTime": 0,
//            "barterNum": 0,
//            "barterState": 0,
//            "bundlingExplain": "",
//            "bundlingId": "",
//            "buyerEmail": "",
//            "buyerId": "1478254026805",
//            "buyerName": "as",
//            "cancelCause": "",
//            "couponId": "",
//            "couponPrice": 0,
//            "createTime": 1479261300384,
//            "daddressId": "0",
//            "deliverExplain": "",
//            "discount": 0,
//            "evalsellerStatus": 0,
//            "evalsellerTime": 0,
//            "evaluationStatus": 0,
//            "evaluationTime": 0,
//            "finnshedTime": 0,
//            "goodsAmount": 50,
//            "groupCount": 0,
//            "groupId": "",
//            "invoice": "1",
//            "lockState": 0,
//            "mansongExplain": "",
//            "mansongId": "",
//            "orderAmount": 0,
//            "orderFrom": 2,
//            "orderId": "1000000095",
//            "orderMessage": "",
//            "orderPointscount": 100,
//            "orderShipperTime": 0,
//            "orderSn": "1000000095",
//            "orderState": 10,
//            "orderStates": [],
//            "orderTotalPoint": 100,
//            "orderTotalPrice": 50,
//            "orderType": 0,
//            "outPaymentCode": "",
//            "outSn": "",
//            "param1": "",
//            "param2": "",
//            "param3": "",
//            "param4": "",
//            "param5": "",
//            "payId": "",
//            "payMessage": "",
//            "paySn": "",
//            "paymentBranch": "2",
//            "paymentCode": "2",
//            "paymentDirect": "",
//            "paymentId": "0",
//            "paymentName": "在线支付",
//            "paymentState": 0,
//            "paymentTime": 0,
//            "predepositAmount": 0,
//            "promoPrice": 0,
//            "refundAmount": 0,
//            "refundState": 0,
//            "returnNum": 0,
//            "returnState": 0,
//            "shippingCode": "",
//            "shippingExpressCode": "",
//            "shippingExpressId": "",
//            "shippingFee": 0,
//            "shippingName": "",
//            "shippingTime": 0,
//            "storeId": "565",
//            "storeName": "第三仓第一门店",
//            "tradeSn": "",
//            "voucherCode": "",
//            "voucherId": "",
//            "voucherPrice": 0,
//            "xianshiExplain": "",
//            "xianshiId": ""
    
    private String buyerId;
    private String orderId;

    public String getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(String buyerId) {
        this.buyerId = buyerId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
