package com.eme.mas.controller.customeInterface;

/**
 * Created by zhangxiaoming on 16/6/6.
 */
public interface ICart {

    /**
     * 获取购物车的商品列表
     */
    void getCartList(String tag);

    /**
     * 添加购物车
     */
    void addToCart(String product_id,String spec_id,String product_num,String product_channel,String tag);

    /**
     * 更新购物车商品数量,包括增加、减少
     */
    void updaterCart(String product_id,String spec_id,String product_num,String tag);

    /**
     * 批量删除购物车
     */
    void deleteCart(String cartIds,String tag);
    /**
     * 获取购物车优惠券
     */
    void  getCartCoupon(String cartIds,String tag);

}
