package com.eme.mas.model.entity;

import java.util.List;

/**
 * 搜索结果页二级实体类
 *
 * Created by dijiaoliang on 16/7/27.
 */
public class SearchResultVo {

    private List<SearchGoodsBo> results;

    private boolean hasMore;

    private int pageNo;

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public List<SearchGoodsBo> getResults() {
        return results;
    }

    public void setResults(List<SearchGoodsBo> results) {
        this.results = results;
    }

    public boolean isHasMore() {
        return hasMore;
    }

    public void setHasMore(boolean hasMore) {
        this.hasMore = hasMore;
    }
}
