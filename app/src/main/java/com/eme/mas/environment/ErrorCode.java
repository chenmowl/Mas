package com.eme.mas.environment;

/**
 * 网络请求错误码表
 *
 * Created by dijiaoliang on 16/7/29.
 */
public class ErrorCode {

    /********* 登陆接口***********/
    public static final String LOGIN_CODE_1001="1001";//msg："密码错误"
    public static final String LOGIN_CODE_1002="1001";//msg："用户名错误"


    /********* 发送验证码***********/
    public static final String SEND_NUM_2001="2001";//msg："参数为空"
    public static final String SEND_NUM_2002="2002";//msg："验证码错误"
    public static final String SEND_NUM_2003="2003";//msg："发送验证码失败"
    public static final String SEND_NUM_2004="2004";//msg："手机号未注册"
    public static final String SEND_NUM_2005="2005";//msg："手机号非法格式"

    /********* 验证码登录接口***********/
    public static final String VERIFI_CODE_LOGIN_2001="2001";//msg："参数为空"
    public static final String VERIFI_CODE_LOGIN_2002="2002";//msg："验证码错误"
    public static final String VERIFI_CODE_LOGIN_2003="2003";//msg："登录失败"

    /********* 忘记密码 发送验证码验证接口***********/
    public static final String SEND_NUMBER_2001="2001";//msg："参数为空"
    public static final String SEND_NUMBER_2002="2002";//msg："验证码错误"
    public static final String SEND_NUMBER_2003="2003";//msg："发送验证码失败"

    /********* 忘记密码 重置密码接口***********/
    public static final String CHANGE_PASSWORD_3001="3001";//msg："参数为空"
    public static final String HANGE_PASSWORD_3002="3002";//msg："发生错误"
    public static final String HANGE_PASSWORD_3003="3003";//msg："手机号未注册"

    /********* 商品详情接口***********/
    public static final String PRODUCT_DETAIL_6001="6001";//msg："参数为空"

    /********* 热门推荐商品接口***********/
    public static final String RECOMMOND_2101="2101";//msg："参数为空"
    public static final String RECOMMOND_2102="2102";//msg："参数非法"
    public static final String RECOMMOND_2103="2103";//msg："参数从2开始"


    /********* 获取用户信息接口***********/
    public static final String GET_USERINFO_4001="4001";//msg："参数错误"

    /********* 设置用户信息接口***********/
    public static final String SET_USERINFO_5001="5001";//msg："参数错误"
    public static final String SET_USERINFO_5002="5002";//msg："用户信息上传失败，请先登录！"

    /********* 添加购物车接口***********/
    public static final String ADD_CART_1001="1001";//msg："参数为空"
    public static final String ADD_CART_1002="1002";//msg："发生错误！"

    /********* 个人中心 设置密码接口***********/
    public static final String UPDATE_PASSWORD_3001="3001";//msg："参数为空"
    public static final String UPDATE_PASSWORD_3002="3002";//msg："发生错误！"


    /********* 更新购物车接口***********/
    public static final String UPDATE_CART_1001="1001";//msg："参数为空"
    public static final String UPDATE_CART_1002="1002";//msg："发生错误！"

    /********* 批量删除购物车接口***********/
    public static final String DEL_CARTS_1001="1001";//msg："参数为空"
    public static final String DEL_CARTS_1002="1002";//msg："发生错误！"














}
