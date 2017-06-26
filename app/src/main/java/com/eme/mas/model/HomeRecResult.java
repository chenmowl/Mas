package com.eme.mas.model;

import com.eme.mas.model.entity.home.HomeRecListVo;

/**
 * HomeFragment页面
 * 热门推荐的实体类
 * <p/>
 * Created by dijiaoliang on 16/7/27.
 */
public class HomeRecResult extends Result {

    private HomeRecListVo data;

    public HomeRecListVo getData() {
        return data;
    }

    public void setData(HomeRecListVo data) {
        this.data = data;
    }
}
