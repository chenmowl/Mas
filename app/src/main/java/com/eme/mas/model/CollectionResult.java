package com.eme.mas.model;

import com.eme.mas.model.entity.CollectionVo;

/**
 * 收藏实体类（收藏页面）
 *
 * Created by zhangxiaoming on 16/8/14.
 */
public class CollectionResult extends Result {
    private CollectionVo data;

    public CollectionVo getData() {
        return data;
    }

    public void setData(CollectionVo data) {
        this.data = data;
    }
}
