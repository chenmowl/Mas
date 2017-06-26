package com.eme.mas.model.entity;

import java.util.List;

/**
 * 商品评价实体（商品详情页）
 *
 * Created by dijiaoliang on 17/1/18.
 */
public class GoodsEvaluateContentBo {

    private String id;      //评价id
    private String memberId;	//评价人   值为0 时为 匿名用户
    private String skuId;	//商品skuId
    private String orderId;	//商品所属订单
    private String starScore;	//星数
    private String scoreFlag;	//1、好评 2、中评  3、差评
    private String evaluateContent; //评价内容
    private String createTime;//评价时间
    private String createBy;//评价人Id
    private String hasFile;	//是否含有附件
    private List<EvaluateFile> evaluateFile ; //附件列表
    private List<EvaluateReply> evaluateReply; //评价回复内容


    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

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


    public String getEvaluateContent() {
        return evaluateContent;
    }

    public void setEvaluateContent(String evaluateContent) {
        this.evaluateContent = evaluateContent;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStarScore() {
        return starScore;
    }

    public void setStarScore(String starScore) {
        this.starScore = starScore;
    }

    public String getScoreFlag() {
        return scoreFlag;
    }

    public void setScoreFlag(String scoreFlag) {
        this.scoreFlag = scoreFlag;
    }

    public String getHasFile() {
        return hasFile;
    }

    public void setHasFile(String hasFile) {
        this.hasFile = hasFile;
    }

    public List<EvaluateFile> getEvaluateFile() {
        return evaluateFile;
    }

    public void setEvaluateFile(List<EvaluateFile> evaluateFile) {
        this.evaluateFile = evaluateFile;
    }

    public List<EvaluateReply> getEvaluateReply() {
        return evaluateReply;
    }

    public void setEvaluateReply(List<EvaluateReply> evaluateReply) {
        this.evaluateReply = evaluateReply;
    }

    public class EvaluateFile{
        private String id;		//附件ID
        private String fileUrl;	//附件地址

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getFileUrl() {
            return fileUrl;
        }

        public void setFileUrl(String fileUrl) {
            this.fileUrl = fileUrl;
        }
    }
    public class EvaluateReply{
        private String id;		//回复id
        private String replyContent;//回复内容

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getReplyContent() {
            return replyContent;
        }

        public void setReplyContent(String replyContent) {
            this.replyContent = replyContent;
        }
    }
}
