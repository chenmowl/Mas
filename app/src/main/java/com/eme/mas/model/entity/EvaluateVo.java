package com.eme.mas.model.entity;

import java.util.List;

/**
 * 评价列表二级实体
 *
 * Created by dijiaoliang on 17/1/19.
 */
public class EvaluateVo {

    private boolean hasMore;

    private int pageNo;

    private List<GoodsEvaluateContentBo> results;

    public boolean isHasMore() {
        return hasMore;
    }

    public void setHasMore(boolean hasMore) {
        this.hasMore = hasMore;
    }

    public List<GoodsEvaluateContentBo> getResults() {
        return results;
    }

    public void setResults(List<GoodsEvaluateContentBo> results) {
        this.results = results;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }
}
