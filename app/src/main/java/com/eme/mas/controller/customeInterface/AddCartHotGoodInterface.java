package com.eme.mas.controller.customeInterface;

import android.widget.RelativeLayout;

import com.eme.mas.model.entity.home.HomeHotGoodsBo;

/**
 * 添加购物车的接口回调(商品详情的热门推荐)
 *
 * Created by zhangxiaoming on 16/6/23.
 */
public interface AddCartHotGoodInterface {

    void addCart(RelativeLayout add, HomeHotGoodsBo productBo, RelativeLayout rlPro);
}
