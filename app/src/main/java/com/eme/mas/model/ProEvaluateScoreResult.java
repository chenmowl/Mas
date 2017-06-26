package com.eme.mas.model;

import com.eme.mas.model.entity.ProEvaluateScoreBo;

/**
 * 商品评价各项指标接口实体类（商品详情页）
 * <p>
 * Created by dijiaoliang on 16/8/16.
 */
public class ProEvaluateScoreResult extends Result {

    private ProEvaluateScoreBo data;

    public ProEvaluateScoreBo getData() {
        return data;
    }

    public void setData(ProEvaluateScoreBo data) {
        this.data = data;
    }
}
