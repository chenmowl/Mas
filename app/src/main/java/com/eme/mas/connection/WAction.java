package com.eme.mas.connection;


import com.android.volley.Request;
import com.eme.mas.connection.annotation.KRequestConfig;
import com.eme.mas.environment.Environment;
import com.eme.mas.environment.KConfig;
import com.eme.mas.environment.WValue;
import com.eme.mas.exception.WException;
import com.eme.mas.model.AddCartResult;
import com.eme.mas.model.BrotherLocationResult;
import com.eme.mas.model.CartListResult;
import com.eme.mas.model.CategoryFilterResult;
import com.eme.mas.model.CategoryResult;
import com.eme.mas.model.CollectionResult;
import com.eme.mas.model.CouponResult;
import com.eme.mas.model.EvaluateResult;
import com.eme.mas.model.GoodsCategoryResult;
import com.eme.mas.model.GoodsCollectionResult;
import com.eme.mas.model.GoodsDetailResult;
import com.eme.mas.model.HomeRecResult;
import com.eme.mas.model.HomeResult;
import com.eme.mas.model.HotWordResult;
import com.eme.mas.model.MyAddressResult;
import com.eme.mas.model.MyOrderResult;
import com.eme.mas.model.OrderAmountPayableResult;
import com.eme.mas.model.OrderCountResult;
import com.eme.mas.model.OrderCouponResult;
import com.eme.mas.model.OrderDetailResult;
import com.eme.mas.model.ProDetailHotResult;
import com.eme.mas.model.ProEvaluateListResult;
import com.eme.mas.model.ProEvaluateScoreResult;
import com.eme.mas.model.Result;
import com.eme.mas.model.SearchResult;
import com.eme.mas.model.SetUserinfoResult;
import com.eme.mas.model.SubmitOrderResult;
import com.eme.mas.model.UserInfoResult;
import com.eme.mas.model.WXPrepareInfoResult;

import java.lang.reflect.Field;

/**
 * Created by luokaiwen on 15/4/27.
 * <p>
 * 接口请求动作名称
 */
public class WAction implements IAction {

    private static final String URL = Environment.URL_PREFIX;
    private static final int POST = Request.Method.POST;
    private static final int GET = Request.Method.GET;


    private static WAction instance;

    private WAction() {
    }

    public static WAction getInstance() {
        if (instance == null) {
            instance = new WAction();
        }
        return instance;
    }


    //=============================Account Setting===============================//


    //=============================   数据接口配置信息  ===============================//

    /**
     * 获取用户的信息
     */
    @KRequestConfig(method = GET, url = "eme-api/member/accounts/getUserinfo.do", clazz = UserInfoResult.class, index = 1001)
    public static final String GET_USER_INFO = "GET_USER_INFO";

    /**
     * 提交用户的信息
     */
    @KRequestConfig(method = POST, url = "eme-api/member/accounts/setUserinfo.do", clazz = SetUserinfoResult.class, index = 1001)
    public static final String SET_USER_INFO = "SET_USER_INFO";

    /**
     * 获取验证码
     */
    @KRequestConfig(method = POST, url = "eme-api/member/accounts/sendnum.do", clazz = Result.class, index = 1001)
    public static final String GET_VERCODE = "GET_VERCODE";

    /**
     * 校验 验证码
     */
    @KRequestConfig(method = POST, url = "eme-api/member/accounts/checkActivationNumber.do", clazz = Result.class, index = 1001)
    public static final String CHECK_VERCODE = "CHECK_VERCODE";

    /**
     * 验证码登录
     */
    @KRequestConfig(method = POST, url = "eme-api/member/accounts/vlogin.do", clazz = UserInfoResult.class, index = 1001)
    public static final String VERIFY_LOGIN = "VERIFY_LOGIN";

    /**
     * 密码登录
     */
    @KRequestConfig(method = POST, url = "eme-api/member/accounts/login.do", clazz = UserInfoResult.class, index = 1001)
    public static final String PASSWORD_LOGIN = "PASSWORD_LOGIN";

    /**
     * 绑定手机
     */
    @KRequestConfig(method = POST, url = "eme-api/member/accounts/updateMobile.do", clazz = Result.class, index = 1001)
    public static final String BIND_PHONE = "BIND_PHONE";

    /**
     * 个人中心设置登录密码
     */
    @KRequestConfig(method = POST, url = "eme-api/member/accounts/update_password.do", clazz = Result.class, index = 1001)
    public static final String SET_LOGIN_PASSWORD = "SET_LOGIN_PASSWORD";

    /**
     * 获取热搜词
     */
    @KRequestConfig(method = GET, url = "eme-api/hotGoods/searchKeyWords.do", clazz = HotWordResult.class, index = 1001)
    public static final String SEARCH_ACTIVITY_HOT_WORDS = "SEARCH_ACTIVITY_HOT_WORDS";

    /**
     * 获取搜索结果
     */
    @KRequestConfig(method = POST, url = "eme-api/homepage/fulltextSearch", clazz = SearchResult.class, index = 1001)
    public static final String SEARCH_RESULT_ACTIVITY_PROS = "SEARCH_RESULT_ACTIVITY_PROS";

    /**
     * 获取homefragment页面数据
     */
    @KRequestConfig(method = GET, url = "eme-api/homepage/getContent", clazz = HomeResult.class, index = 1001)
    public static final String HOME_FRAGMENT_DATA = "HOME_FRAGMENT_DATA";

    /**
     * 获取homefragment页面热门推荐列表数据
     */
    @KRequestConfig(method = GET, url = "eme-api/homepage/getHotGoods", clazz = HomeRecResult.class, index = 1001)
    public static final String HOME_FRAGMENT_RECDATA = "HOME_FRAGMENT_RECDATA";

    /**
     * 新增我的地址
     */
    @KRequestConfig(method = POST, url = "eme-api/address/saveAddress", clazz = Result.class, index = 1001)
    public static final String ADD_NEW_ADDRESS = "ADD_NEW_ADDRESS";

    /**
     * 获取我的地址列表
     */
    @KRequestConfig(method = GET, url = "eme-api/address/getAddress", clazz = MyAddressResult.class, index = 1001)
    public static final String GET_ADDRESS = "GET_ADDRESS";

    /**
     * 获取小哥位置
     */
    @KRequestConfig(method = GET, url = "eme-api/order/orderLocationByNo.do", clazz = BrotherLocationResult.class, index = 1001)
    public static final String GET_BROTHER_LOCATION = "GET_BROTHER_LOCATION";

    /**
     * 删除我的地址
     */
    @KRequestConfig(method = POST, url = "eme-api/address/delAddress", clazz = Result.class, index = 1001)
    public static final String DELETE_ADDRESS = "DELETE_ADDRESS";

    /**
     * 编辑我的地址
     */
    @KRequestConfig(method = POST, url = "eme-api/address/editAddress", clazz = Result.class, index = 1001)
    public static final String EDIT_ADDRESS = "EDIT_ADDRESS";

    /**
     * 获取优惠券
     */
    @KRequestConfig(method = GET, url = "eme-api/coupon/getCouponMemberList", clazz = CouponResult.class, index = 1001)
    public static final String GET_COUPON = "GET_COUPON";

    /**
     * 获取商品分类数据
     */
    @KRequestConfig(method = GET, url = "eme-api/goods/category/getProductCategory.do", clazz = CategoryResult.class, index = 1001)
    public static final String CATEGORY_ACTIVITY_DATA = "CATEGORY_ACTIVITY_DATA";

    /**
     * 商品筛选项数据
     */
    @KRequestConfig(method = POST, url = "eme-api/goods/getSearchConditions", clazz = CategoryFilterResult.class, index = 1001)
    public static final String CATEGORY_FILTER_DATA = "CATEGORY_FILTER_DATA";

    /**
     * 商品分类搜索结果数据(分类页面)
     */
    @KRequestConfig(method = POST, url = "eme-api/hotGoods/searchGoods.do", clazz = GoodsCategoryResult.class, index = 1001)
    public static final String CATEGORY_ACTIVITY_SEARCH_DATA = "CATEGORY_ACTIVITY_SEARCH_DATA";

    /**
     * 商品详情（商品详情页）
     */
    @KRequestConfig(method = POST, url = "eme-api/goods/getGoodsDetail", clazz = GoodsDetailResult.class, index = 1001)
    public static final String PRODUCT_DETAIL_ACTIVITY_DATA = "PRODUCT_DETAIL_ACTIVITY_DATA";

    /**
     * 评价商品列表（商品详情页，评价列表）
     */
    @KRequestConfig(method = POST, url = "eme-api/goods/getEvaluateByPage", clazz = EvaluateResult.class, index = 1001)
    public static final String EVALUATE_LIST_DATA = "EVALUATE_LIST_DATA";

    /**
     * 评价商品列表（商品详情页，评价列表）
     */
    @KRequestConfig(method = GET, url = "eme-api/evaluate/evaluateGoodsList.do", clazz = ProEvaluateListResult.class, index = 1001)
    public static final String PRODUCT_EVALUATE_LIST_DATA = "PRODUCT_EVALUATE_LIST_DATA";

    /**
     * 商品评价各项指标接口（商品详情页，评价指标结果）
     */
    @KRequestConfig(method = GET, url = "eme-api/evaluate/evaluateScore.do", clazz = ProEvaluateScoreResult.class, index = 1001)
    public static final String PRODUCT_EVALUATE_SCORE_DATA = "PRODUCT_EVALUATE_SCORE_DATA";

    /**
     * 商品详情热门推荐接口（商品详情页，热门推荐）
     */
    @KRequestConfig(method = GET, url = "eme-api/goods/hotGoods.do", clazz = ProDetailHotResult.class, index = 1001)
    public static final String PRODUCT_DETAIL_HOT_DATA = "PRODUCT_DETAIL_HOT_DATA";

    /**
     * 收藏商品接口（商品详情页）
     */
    @KRequestConfig(method = POST, url = "eme-api/goods/saveFavoriteGoods", clazz = GoodsCollectionResult.class, index = 1001)
    public static final String GOODS_COLLECTION= "GOODS_COLLECTION";

    /**
     * 收藏商品接口（商品详情页）
     */
    @KRequestConfig(method = GET, url = "eme-api/favorites/favoritesGoods", clazz = Result.class, index = 1001)
    public static final String COLLECTE_PRODUCT= "COLLECTE_PRODUCT";

    /**
     * 取消收藏商品接口（商品详情页）
     */
    @KRequestConfig(method = GET, url = "eme-api/favorites/cancleFavoritesGoods", clazz = Result.class, index = 1001)
    public static final String CANCEL_COLLECTE_PRODUCT= "CANCEL_COLLECTE_PRODUCT";

    /**
     * 获取收藏商品列表
     */
    @KRequestConfig(method = GET, url = "eme-api/favorites/queryFavoritesGoods", clazz = CollectionResult.class, index = 1001)
    public static final String GET_COLLECTION_DATA = "GET_COLLECTION_DATA";
    /**
     * 商品评价接口
     */
    @KRequestConfig(method = POST, url = "eme-api/evaluate/evaluateOrder", clazz = Result.class, index = 1001)
    public static final String SET_EVALUATE = "SET_EVALUATE";

    /**
     * 添加购物车
     */
    @KRequestConfig(method = POST, url = "eme-api/cart/addCart.do", clazz = AddCartResult.class, index = 1001)
    public static final String ADD_TO_SHOPPING_CART = "ADD_TO_SHOPPING_CART";

    /**
     * 更新购物车
     */
    @KRequestConfig(method = POST, url = "eme-api/cart/updateCart.do", clazz = Result.class, index = 1001)
    public static final String UPDATE_SHOPPING_CART = "UPDATE_SHOPPING_CART";

    /**
     * 批量删除购物车
     */
    @KRequestConfig(method = POST, url = "eme-api/cart/delCarts.do", clazz = Result.class, index = 1001)
    public static final String DELETE_SHOPPING_CART = "DELETE_SHOPPING_CART";

    /**
     * 获取购物车列表
     */
    @KRequestConfig(method = GET, url = "eme-api/cart/getCartList", clazz = CartListResult.class, index = 1001)
    public static final String GET_SHOPPING_CART_DATA = "GET_SHOPPING_CART_DATA";

    /**
     * 获取购物车优惠券
     */
    @KRequestConfig(method = GET, url = "eme-api/coupon/getCouponMemberList", clazz = CouponResult.class, index = 1001)
    public static final String GET_CART_COUPON = "GET_CART_COUPON";

    /**
     * 提交订单
     */
    @KRequestConfig(method = POST, url = "eme-api/order/submitOrderV2.do", clazz = SubmitOrderResult.class, index = 1001)
    public static final String ORDER_SUBMIT = "ORDER_SUBMIT";

    /**
     * 订单详情
     */
    @KRequestConfig(method = GET, url = "eme-api/order/orderDetail", clazz = OrderDetailResult.class, index = 1001)
    public static final String ORDER_DETAIL = "ORDER_DETAIL";

    /**
     * 订单取消
     */
    @KRequestConfig(method = POST, url = "eme-api/order/cancleOrder", clazz = Result.class, index = 1001)
    public static final String ORDER_CANCEL = "ORDER_CANCEL";

    /**
     * 订单查询
     */
    @KRequestConfig(method = GET, url = "eme-api/order/queryOrder", clazz = MyOrderResult.class, index = 1001)
    public static final String ORDER_QUERY = "ORDER_QUERY";

    /**
     * 申请退货
     */
    @KRequestConfig(method = GET, url = "eme-api/order/apply4Return", clazz = Result.class, index = 1001)
    public static final String ORDER_RETURN = "ORDER_RETURN";

    /**
     * 申请换货
     */
    @KRequestConfig(method = GET, url = "eme-api/order/addOrderBarter", clazz = Result.class, index = 1001)
    public static final String ORDER_EXCHANGE = "ORDER_EXCHANGE";

    /**
     * 确认签收
     */
    @KRequestConfig(method = GET, url = "eme-api/order/signOrder", clazz = Result.class, index = 1001)
    public static final String ORDER_SIGNED = "ORDER_SIGNED";

    /**
     * 删除订单
     */
    @KRequestConfig(method = POST, url = "eme-api/order/removeOrder", clazz = Result.class, index = 1001)
    public static final String ORDER_DELETE = "ORDER_DELETE";

    /**
     * 订单数量
     */
    @KRequestConfig(method = GET, url = "eme-api/order/countOrder", clazz = OrderCountResult.class, index = 1001)
    public static final String ORDER_COUNT = "ORDER_COUNT";

    /**
     * 计算订单的应付金额,优惠金额,邮费,商品总额 (提交订单)
     */
    @KRequestConfig(method = POST, url = "eme-api/couponUser/calculateOrderAmount", clazz = OrderAmountPayableResult.class, index = 1001)
    public static final String ORDER_AMOUNT_PAYABLE = "ORDER_AMOUNT_PAYABLE";

    /**
     * 手机端优惠券使用前查询可用优惠券列表--->(用于用户在使用优惠券的时候，供用户对优惠券进行选择),并根据1.过期时间升序2.优惠面额降序 进行排序
     */
    @KRequestConfig(method = POST, url = "eme-api/couponUser/selectCouponForCart", clazz = OrderCouponResult.class, index = 1001)
    public static final String ORDER_AVAILABLE_COUPON = "ORDER_AVAILABLE_COUPON";

    /**
     * 微信-向服务器发送预支付请求
     */
    @KRequestConfig(method = POST, url = "eme-payment/payOrder/generatePrepayInfoForWeChat", clazz = WXPrepareInfoResult.class, index = WValue.INDEX_PREPAY)
    public static final String WX_PREPAY = "WX_PREPAY";


    //=============================   以上是数据接口配置信息  ===============================//


    @Override
    public String getAction(int index) {
        String action = "";
        Field[] fields = getClass().getFields();
        for (Field field : fields) {
            KRequestConfig annotation = field.getAnnotation(KRequestConfig.class);
            if (annotation != null && annotation.index() == index) {
                try {
                    action = (String) field.get(null);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                }
            }
        }
        return action;
    }

    @Override
    public String getUrl(String action) {
        String url = null;
        KRequestConfig annotation = getAnnotation(action);
        if (annotation != null) {
            int index=annotation.index();
            if(WValue.INDEX_PREPAY==index){
                url = KConfig.getPAYHOST() + annotation.url();
            }else{
                url = KConfig.getHOST() + annotation.url();
            }
        } else {
            try {
                throw new WException("url cannot be null, please configure in WAction first!");
            } catch (WException e) {
                e.printStackTrace();
            }
        }
        return url;
    }

    @Override
    public int getIndex(String action) {
        int index = -1;
        KRequestConfig annotation = getAnnotation(action);
        if (annotation != null) {
            index = annotation.index();
        } else {
            try {
                throw new WException("index cannot be null, please configure in WAction first!");
            } catch (WException e) {
                e.printStackTrace();
            }
        }
        return index;
    }

    @Override
    public int getMethod(String action) {
        int method = -1;
        KRequestConfig annotation = getAnnotation(action);
        if (annotation != null) {
            method = annotation.method();
        } else {
            try {
                throw new WException("method cannot be null, please configure in WAction first!");
            } catch (WException e) {
                e.printStackTrace();
            }
        }
        return method;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Class<? extends Result> getResultType(String action) {
        Class<? extends Result> clazz = null;
        KRequestConfig annotation = getAnnotation(action);
        if (annotation != null) {
            clazz = annotation.clazz();
        } else {
            try {
                throw new WException("clazz cannot be null,please configure in WAction first!");
            } catch (WException e) {
                e.printStackTrace();
            }
        }
        return clazz;
    }

    private KRequestConfig getAnnotation(String action) {
        Field[] fields = getClass().getFields();
        for (Field field : fields) {
            try {
                if (action.equals(field.get(field.getName()))) {
                    KRequestConfig annotation = field.getAnnotation(KRequestConfig.class);
                    return annotation;
                }
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static String joinUrl(String host, int port, String service) {
        return host + ":" + port + service;
    }

    public static String joinUrl(String host, String service) {
        return joinUrl(host, Environment.PORT, service);
    }


}
