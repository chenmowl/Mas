package com.eme.mas.model;

import com.eme.mas.model.entity.SearchResultVo;

/**
 * 搜索结果实体类（搜索结果页）
 * <p/>
 * Created by dijiaoliang on 16/7/27.
 */
public class SearchResult extends Result {


    private SearchResultVo data;


    public SearchResultVo getData() {
        return data;
    }

    public void setData(SearchResultVo data) {
        this.data = data;
    }
}
