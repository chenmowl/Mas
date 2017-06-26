package com.eme.mas.model.entity;

/**
 * 标签列表条目（商品详情页面）
 *
 * Created by dijiaoliang on 17/1/18.
 */
public class GoodsLabelShowBo {

    private String labelId;
    private String labelName;
    private String labelImage;
    private String fixposition;

    public String getLabelId() {
        return labelId;
    }

    public void setLabelId(String labelId) {
        this.labelId = labelId;
    }

    public String getLabelName() {
        return labelName;
    }

    public void setLabelName(String labelName) {
        this.labelName = labelName;
    }

    public String getLabelImage() {
        return labelImage;
    }

    public void setLabelImage(String labelImage) {
        this.labelImage = labelImage;
    }

    public String getFixposition() {
        return fixposition;
    }

    public void setFixposition(String fixposition) {
        this.fixposition = fixposition;
    }
}
