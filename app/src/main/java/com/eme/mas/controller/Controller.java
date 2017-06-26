package com.eme.mas.controller;

import com.eme.mas.connection.IActionListener;
import com.eme.mas.controller.customeInterface.ICart;
import com.eme.mas.controller.customeInterface.ILocation;
import com.eme.mas.controller.customeInterface.IOrder;
import com.eme.mas.controller.customeInterface.IProduct;
import com.eme.mas.controller.customeInterface.IUserInfo;
import com.eme.mas.environment.Constant;

import java.util.HashMap;

/**
 * 总控制器
 * <p/>
 * Created by zhangxiaoming on 16/6/6.
 */
public class Controller {

    private ICart cart;
    private IOrder iOrder;
    private IUserInfo userInfo;
    private ILocation location;
    private IProduct product;

    public static HashMap<String, String> getParams() {
        HashMap<String, String> params = new HashMap<String, String>();
//        params.put("appID", Constant.APP_ID);
        params.put("deviceType", Constant.DEVICE_TYPE);
        params.put("apiVersion", Constant.API_VERSION);
        return params;
    }

    /**
     * 产品相关：活动信息、热卖、推荐、分类、广告、商品详情、优惠券
     */
    public IProduct getProduct(IActionListener iActionListener) {

        if (null == product) {
            product = new ProductImpl(iActionListener);
        }

        return product;
    }

    /**
     * 地址相关：创建、修改、删除地址，获取地址、配送门店、上传经纬度、地址切换，城市列表、高德
     */
    public ILocation getLocation(IActionListener iActionListener) {

        if (null == location) {
            location = new LocationImpl(iActionListener);
        }

        return location;
    }

    /**
     * 用户相关：登录、注册、退出、上传用户信息、会员、积分、在线反馈、修改密码、绑定邮箱，版本升级
     */
    public IUserInfo getUserInfo(IActionListener iActionListener) {

        if (null == userInfo) {
            userInfo = new UserInfoImpl(iActionListener);
        }

        return userInfo;
    }

    /**
     * 订单相关：订单状态、订单详情、订单物流、订单列表、分享、消息
     */
    public IOrder getOrder(IActionListener iActionListener) {

        if (null == iOrder) {
            iOrder = new OrderImpl(iActionListener);
        }

        return iOrder;
    }


    /**
     * 结算开关：购物车获取数据，增删改查，订单提交
     */

    public ICart getIAccount(IActionListener iActionListener) {

        if (null == cart) {
            cart = new CartImpl(iActionListener);
        }

        return cart;
    }

//    /**
//     * 选中购物车的本地保存：添加、清楚、获取数量
//     */
//    public ICartService getCartFile(Context context, IActionListener iActionListener) {
//
//        if (null == cartService) {
//            cartService = new DefaultFileCartImpl(context, iActionListener);
//        }
//
//        return cartService;
//    }
}
