package com.eme.mas.model;

import com.eme.mas.model.entity.CategorySearchVo;

/**
 * CategoryActivity页
 * 商品搜索结果实体类
 * <p/>
 * Created by dijiaoliang on 16/7/27.
 */
public class CategorySearchResult extends Result {


    private CategorySearchVo data;

    public CategorySearchVo getData() {
        return data;
    }

    public void setData(CategorySearchVo data) {
        this.data = data;
    }
}
