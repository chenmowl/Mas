package com.eme.mas.controller.customeInterface;

import android.widget.ImageView;

import com.eme.mas.model.entity.SearchGoodsBo;

/**
 * 添加购物车的接口回调(搜索结果页)
 *
 * Created by zhangxiaoming on 16/6/23.
 */
public interface SearchAddCartInterface {

    void addCart(ImageView iv_checkbox, SearchGoodsBo productBo);
}
