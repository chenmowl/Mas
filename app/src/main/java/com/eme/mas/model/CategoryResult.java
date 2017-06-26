package com.eme.mas.model;

import com.eme.mas.model.entity.CategorySpecVo;

import java.util.List;

/**
 * 分类数据实体类（分类页）
 * <p/>
 * Created by dijiaoliang on 16/7/27.
 */
public class CategoryResult extends Result {


    private List<CategorySpecVo> data;

    public List<CategorySpecVo> getData() {
        return data;
    }

    public void setData(List<CategorySpecVo> data) {
        this.data = data;
    }
}
