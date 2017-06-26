package com.eme.mas.model.entity;

import java.util.List;

/**
 * 分类查询二级实体
 *
 * Created by dijiaoliang on 17/1/20.
 */
public class GoodsCategoryVo {

    private boolean hasMore;

    private int pageNo;

    private List<SearchGoodsBo> results;

    public boolean isHasMore() {
        return hasMore;
    }

    public void setHasMore(boolean hasMore) {
        this.hasMore = hasMore;
    }

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
}
