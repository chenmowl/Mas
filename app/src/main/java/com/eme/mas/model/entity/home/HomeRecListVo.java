package com.eme.mas.model.entity.home;

import java.util.List;

/**
 * Fragment页二级实体类
 * 热门推荐列表
 *
 * Created by dijiaoliang on 16/7/27.
 */
public class HomeRecListVo {

    private List<HomeHotGoodsBo> results;

    private String hasMore;

    public List<HomeHotGoodsBo> getResults() {
        return results;
    }

    public void setResults(List<HomeHotGoodsBo> results) {
        this.results = results;
    }

    public String getHasMore() {
        return hasMore;
    }

    public void setHasMore(String hasMore) {
        this.hasMore = hasMore;
    }
}
