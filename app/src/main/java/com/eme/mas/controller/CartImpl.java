package com.eme.mas.controller;

import com.eme.mas.connection.IActionListener;
import com.eme.mas.connection.WAction;
import com.eme.mas.controller.customeInterface.ICart;

import java.util.HashMap;

/**
 * 购物车控制器实现类
 * <p>
 * Created by zhangxiaoming on 16/6/6.
 */
public class CartImpl extends BaseImpl implements ICart {

    public CartImpl(IActionListener iActionListener) {
        super(iActionListener);

    }

    /**
     * 获取购物车的商品列表
     */
    @Override
    public void getCartList(String tag) {
        request(WAction.GET_SHOPPING_CART_DATA, null, tag);
    }

    /**
     * 添加购物车
     */
    @Override
    public void addToCart(String product_id, String spec_id, String product_num, String product_channel,String tag) {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("product_id", product_id);
        params.put("spec_id", spec_id);
        params.put("product_num", product_num);
        params.put("product_channel", product_channel);
        request(WAction.ADD_TO_SHOPPING_CART, params, tag);
    }

    /**
     * 更新购物车接口
     *
     * @param product_id
     * @param spec_id
     * @param product_num
     */
    @Override
    public void updaterCart(String product_id, String spec_id, String product_num,String tag) {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("product_id", product_id);
        params.put("spec_id", spec_id);
        params.put("product_num", product_num);
        request(WAction.UPDATE_SHOPPING_CART, params, tag);
    }

    /**
     * 批量删除购物车
     */
    @Override
    public void deleteCart(String cartIds,String tag) {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("cartIds", cartIds);
        request(WAction.DELETE_SHOPPING_CART, params, tag);
    }

    /**
     * 获取购物车优惠券
     */
    @Override
    public void getCartCoupon(String cartIds,String tag) {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("cartIds", cartIds);
        request(WAction.GET_CART_COUPON, params, tag);
    }

}
