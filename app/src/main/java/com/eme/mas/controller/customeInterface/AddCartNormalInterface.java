package com.eme.mas.controller.customeInterface;

import android.widget.RelativeLayout;

import com.eme.mas.model.entity.ProductBo;

/**
 * 添加购物车的接口回调
 *
 * Created by zhangxiaoming on 16/6/23.
 */
public interface AddCartNormalInterface {

    void addCart(RelativeLayout add, ProductBo productBo,RelativeLayout rlPro);
}
