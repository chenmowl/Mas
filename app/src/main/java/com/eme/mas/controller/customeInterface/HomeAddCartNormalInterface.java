package com.eme.mas.controller.customeInterface;

import android.widget.ImageView;

import com.eme.mas.model.entity.home.HomeHotGoodsBo;

/**
 * 添加购物车的接口回调(homefragment的热门推荐)
 *
 * Created by zhangxiaoming on 16/6/23.
 */
public interface HomeAddCartNormalInterface {

    void addCart(ImageView iv_checkbox, HomeHotGoodsBo productBo);
}
