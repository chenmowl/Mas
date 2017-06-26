package com.eme.mas.model.entity;

/**
 * 商品评价的基实体类（商品详情页）
 * <p>
 * Created by dijiaoliang on 16/4/27.
 */
public class ProEvaluateBo {

//            "gevalContent": "sdfjlskjlfkj",
//            "gevalFrommemberAvatar": "/upload/img/avatar/1466080484697.jpg",
//            "gevalFrommemberid": "f1de9e8252c540f78cd84fc38b79797f",
//            "gevalFrommembername": "匿名用户",
//            "gevalGoodsId": "25c6bfa1a07f47a8bc4804cf6745ef43",
//            "gevalId": "587fec53626e4b038e7d71fa30c6cc2b",
//            "gevalImage": "",
//            "gradename": "铜牌会员",
//            "id": "c935eea8a94a4ff39fb6980e2f56d78e",
//            "replyText": "啦啦啦",
//            "replyTime": 1470995731631,
//            "replyUserId": "1",
//            "replyUserName": "admin",
//            "specInfo": "&aring;&pound;&aring;&sup3;:&aring;&aring;&sup3;&Acirc;&nbsp;"


    private String gevalScore;//评价分数
    private String gevalIsAnonymous;
    private String gevalAddTime;

    /**接口检查以下字段包含,上边字段不包含*/
    private String gevalContent;//评价内容
    private String gevalFrommemberid;//评价人
    private String gevalFrommembername;
    private String gevalFrommemberAvatar;
    private String gevalGoodsId;//商品ID
    private String gevalId;
    private String gevalImage;
    private String gradename;
    private String id;
    private String replyText;//回复内容
    private String replyTime;//回复时间
    private String replyUserId;
    private String replyUserName;
    private String specInfo;

    public String getGevalImage() {
        return gevalImage;
    }

    public void setGevalImage(String gevalImage) {
        this.gevalImage = gevalImage;
    }

    public String getGevalGoodsId() {
        return gevalGoodsId;
    }

    public void setGevalGoodsId(String gevalGoodsId) {
        this.gevalGoodsId = gevalGoodsId;
    }

    public String getGevalId() {
        return gevalId;
    }

    public void setGevalId(String gevalId) {
        this.gevalId = gevalId;
    }

    public String getGevalScore() {
        return gevalScore;
    }

    public void setGevalScore(String gevalScore) {
        this.gevalScore = gevalScore;
    }

    public String getGevalContent() {
        return gevalContent;
    }

    public void setGevalContent(String gevalContent) {
        this.gevalContent = gevalContent;
    }

    public String getGevalIsAnonymous() {
        return gevalIsAnonymous;
    }

    public void setGevalIsAnonymous(String gevalIsAnonymous) {
        this.gevalIsAnonymous = gevalIsAnonymous;
    }

    public String getGevalAddTime() {
        return gevalAddTime;
    }

    public void setGevalAddTime(String gevalAddTime) {
        this.gevalAddTime = gevalAddTime;
    }

    public String getGevalFrommemberid() {
        return gevalFrommemberid;
    }

    public void setGevalFrommemberid(String gevalFrommemberid) {
        this.gevalFrommemberid = gevalFrommemberid;
    }

    public String getGevalFrommembername() {
        return gevalFrommembername;
    }

    public void setGevalFrommembername(String gevalFrommembername) {
        this.gevalFrommembername = gevalFrommembername;
    }

    public String getGevalFrommemberAvatar() {
        return gevalFrommemberAvatar;
    }

    public void setGevalFrommemberAvatar(String gevalFrommemberAvatar) {
        this.gevalFrommemberAvatar = gevalFrommemberAvatar;
    }

    public String getSpecInfo() {
        return specInfo;
    }

    public void setSpecInfo(String specInfo) {
        this.specInfo = specInfo;
    }

    public String getGradename() {
        return gradename;
    }

    public void setGradename(String gradename) {
        this.gradename = gradename;
    }

    public String getReplyUserId() {
        return replyUserId;
    }

    public void setReplyUserId(String replyUserId) {
        this.replyUserId = replyUserId;
    }

    public String getReplyUserName() {
        return replyUserName;
    }

    public void setReplyUserName(String replyUserName) {
        this.replyUserName = replyUserName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getReplyText() {
        return replyText;
    }

    public void setReplyText(String replyText) {
        this.replyText = replyText;
    }

    public String getReplyTime() {
        return replyTime;
    }

    public void setReplyTime(String replyTime) {
        this.replyTime = replyTime;
    }
}
