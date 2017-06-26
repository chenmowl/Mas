package com.eme.mas.model.entity;

import java.util.List;

/**
 * Created by zulei on 16/8/26.
 */
public class EvaluateBo {

    /**
     * orderId : 0022ea4fccd042529acb3df287b7a161
     * orderNo : 20160706154047511
     * serviceScore : 5
     * deliveryScore : 5
     * gevalIsAnonymous : 1
     * memberId : 1467013031532
     * evaluateGoodsList : [{"goodsId":"1","goodsScore":"5","goodsContent":"很好","gevalImage":"/ddd/ddd"},{"goodsId":"2","goodsScore":"4","goodsContent":"很好","gevalImage":"/ddd/ddd"}]
     */

    private String orderId;
    private String orderNo;
    private String serviceScore;
    private String deliveryScore;
    private String gevalIsAnonymous;
    private String memberId;
    /**
     * goodsId : 1
     * goodsScore : 5
     * goodsContent : 很好
     * gevalImage : /ddd/ddd
     */

    private List<EvaluateGoodsListBean> evaluateGoodsList;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getServiceScore() {
        return serviceScore;
    }

    public void setServiceScore(String serviceScore) {
        this.serviceScore = serviceScore;
    }

    public String getDeliveryScore() {
        return deliveryScore;
    }

    public void setDeliveryScore(String deliveryScore) {
        this.deliveryScore = deliveryScore;
    }

    public String getGevalIsAnonymous() {
        return gevalIsAnonymous;
    }

    public void setGevalIsAnonymous(String gevalIsAnonymous) {
        this.gevalIsAnonymous = gevalIsAnonymous;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public List<EvaluateGoodsListBean> getEvaluateGoodsList() {
        return evaluateGoodsList;
    }

    public void setEvaluateGoodsList(List<EvaluateGoodsListBean> evaluateGoodsList) {
        this.evaluateGoodsList = evaluateGoodsList;
    }

    public static class EvaluateGoodsListBean {
        private String goodsId;
        private String goodsScore;
        private String goodsContent;
        private String gevalImage;

        public String getGoodsId() {
            return goodsId;
        }

        public void setGoodsId(String goodsId) {
            this.goodsId = goodsId;
        }

        public String getGoodsScore() {
            return goodsScore;
        }

        public void setGoodsScore(String goodsScore) {
            this.goodsScore = goodsScore;
        }

        public String getGoodsContent() {
            return goodsContent;
        }

        public void setGoodsContent(String goodsContent) {
            this.goodsContent = goodsContent;
        }

        public String getGevalImage() {
            return gevalImage;
        }

        public void setGevalImage(String gevalImage) {
            this.gevalImage = gevalImage;
        }
    }
}
