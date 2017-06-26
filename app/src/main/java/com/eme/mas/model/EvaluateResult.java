package com.eme.mas.model;

import com.eme.mas.model.entity.EvaluateVo;

/**
 * 评价列表实体类（商品详情页）
 *
 * Created by dijiaoliang on 17/1/19.
 */
public class EvaluateResult extends Result {

    private EvaluateVo data;

    public EvaluateVo getData() {
        return data;
    }

    public void setData(EvaluateVo data) {
        this.data = data;
    }
}
