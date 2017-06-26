package com.eme.mas.model;

import com.eme.mas.model.entity.ProDetailInfoVo;

/**
 * 商品详情信息实体类（商品详情页）
 * <p/>
 * Created by dijiaoliang on 16/7/27.
 */
public class ProDetailResult extends Result {


    private ProDetailInfoVo data;

    public ProDetailInfoVo getData() {
        return data;
    }

    public void setData(ProDetailInfoVo data) {
        this.data = data;
    }
}
