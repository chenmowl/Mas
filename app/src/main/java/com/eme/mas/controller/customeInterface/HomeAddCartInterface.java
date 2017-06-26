package com.eme.mas.controller.customeInterface;

import android.widget.ImageView;

import com.eme.mas.model.entity.home.HomeMobileVipBo;

/**
 * 添加购物车的接口回调(homefragment的手机专享)
 *
 * Created by zhangxiaoming on 16/6/23.
 */
public interface HomeAddCartInterface {

    void addCart(ImageView iv_checkbox,HomeMobileVipBo productBo);
}
