package com.eme.mas.model.entity;

/**
 * Created by dijiaoliang on 16/4/27.
 */
public class OrderCountBo {

    /**
     * historyNum : 1
     * pendingNum : 1
     * receiptNum : 0
     */

    private int historyNum;
    private int pendingNum;
    private int receiptNum;

    public int getHistoryNum() {
        return historyNum;
    }

    public void setHistoryNum(int historyNum) {
        this.historyNum = historyNum;
    }

    public int getPendingNum() {
        return pendingNum;
    }

    public void setPendingNum(int pendingNum) {
        this.pendingNum = pendingNum;
    }

    public int getReceiptNum() {
        return receiptNum;
    }

    public void setReceiptNum(int receiptNum) {
        this.receiptNum = receiptNum;
    }
}
