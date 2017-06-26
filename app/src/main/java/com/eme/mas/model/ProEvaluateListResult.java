package com.eme.mas.model;

import com.eme.mas.model.entity.ProEvaluateBo;

import java.util.List;

/**
 * 商品评价列表实体类（商品详情页）
 * <p>
 * Created by dijiaoliang on 16/7/27.
 */
public class ProEvaluateListResult extends Result {


    private List<ProEvaluateBo> data;

    public List<ProEvaluateBo> getData() {
        return data;
    }

    public void setData(List<ProEvaluateBo> data) {
        this.data = data;
    }
}
