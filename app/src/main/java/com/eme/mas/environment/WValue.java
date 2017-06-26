package com.eme.mas.environment;

/**
 * Created by luokaiwen on 15/5/9.
 * <p>
 * 应用中使用的固定常量
 */
public class WValue {

    /**
     * 常量零
     */
    public static final int ZERO = 0;

    /**
     * 常量1
     */
    public static final int ONE = 1;

    /**
     * 没有配置layoutId
     */
    public static final int NO_LAYOUT_ID = -1;

    /**
     * PREPAY
     */
    public static final int INDEX_PREPAY = 2001;

    /**
     * 字符串零
     */
    public static final String ZERO_STR = "0";

    /**
     * 字符串1
     */
    public static final String ONE_STR = "1";

    /**
     * 字符串空
     */
    public static final String STRING_EMPTY = "";

    /**
     * null
     */
    public static final String NULL_LOWER_CASE = "null";

    /**
     * NULL
     */
    public static final String NULL_UPPER_CASE = "NULL";

    /**
     *  ","
     */
    public static final String COMMA = ",";

    /**
     * FLAG
     */
    public static final String FLAG = "flag";

    /**
     * true
     */
    public static final String TRUE_STR = "true";

    /**
     * false
     */
    public static final String FALSE_STR = "false";

    /**
     * 添加购物车的执行动画时间
     */
    public static final int TIME_ADDCART = 500;


    public static final int PAGE_SIZE = 8;


    /**
     * 日期格式
     */
    public static final String FORMAT_DATE_ONE = "yyyy-MM-dd";
    public static final String FORMAT_DATE_TWO = "yyyyMMdd";

    /**
     * 商品规格
     */
    public static final String BRAND = "BRAND";//品牌
    public static final String FLAVOR = "FLAVOR";//香型
    public static final String PLACE = "PLACE";//产地
    public static final String PRICE = "price";//价格

    /**
     * 商品类型: 手机专享、手机普通
     */
    public static final String PHONE_VIP = "2";//手机专享
    public static final String PHONE_NORMAL = "1";//手机普通

    /**
     * 主页FLAG
     */
    public static final int MAIN_FRAGMENT_HOME = 1;//首页
    public static final int MAIN_FRAGMENT_CART = 2;//购物车
    public static final int MAIN_FRAGMENT_ORDER = 3;//订单
    public static final int MAIN_FRAGMENT_MINE = 4;//我的
    public static final int MAIN_FRAGMENT_NONE = 0;//我的

    /**
     * 搜索类型
     *          1 是关键字搜素 2 是品牌搜索
     */
    public static final String BRAND_SEARCH = "2";//品牌搜索
    public static final String KEY_SEARCH = "1";//关键字搜索


    /**
     * 获取验证码的业务场景   1、登录 ==> login  2、设置密码  ==> setPWD 3、修改手机  ==> updateMobile 4、绑定新手机 ==> BindNewMobile 5、修改密码==>updatePWD
     */
    public static final String SCENE_LOGIN = "login";
    public static final String SCENE_SETPWD = "setPWD";
    public static final String SCENE_UPDATEMOBILE = "updateMobile";
    public static final String SCENE_BINDNEWMOBILE = "bindNewMobile";
    public static final String SCENE_FORGETPWD = "forgetPWD";


    /**
     * 绑定手机页面标识
     */
    public static final int VERIFY_MOBILE = 11;//验证手机
    public static final int MODIFY_MOBILE = 12;//修改手机


    /**
     * 个人资料中的更新项
     */
    public static final int UPDATE_OBJ_HEADER_PHOTO = 101;//用户头像
    public static final int UPDATE_OBJ_BIRTHDAY = 102;//出生年月
    public static final int UPDATE_OBJ_SEX = 103;//性别

    public static final String SEX_FEMALE = "0";//女
    public static final String SEX_MALE = "1";//男
    public static final String SEX_SECRET = "2";//保密


    /**
     * 关闭页面EVENTBUS的flag
     */
    public static final String EVENT_SEARCH = "EVENT_SEARCH";//搜索页面标签

    /**
     *  OrderEditSearchPlaceActivity结束时回传的标记
     */
    public static final String MAPITEM = "MAPITEM";//高德地图条目类
    public static final String MYADDRESSBO = "MYADDRESSBO";//我的地址条目类

    /**
     * 我的地址入口标记(提交订单)
     */
    public static final String ORDER_COMMIT = "ORDER_COMMIT";//我的地址条目类

    /**
     * 订单立即送达(提交订单)
     */
    public static final String AT_ONCE = "AT_ONCE";//订单的配送时间,立即送达

    /**
     * 付款方式(提交订单)
     */
    public static final String PAYMODE_OFF_LINE = "1";//货到付款
    public static final String PAYMODE_ON_LINE = "2";//在线支付

    /**
     * 发票(提交订单)
     */
    public static final String INVOICE_TYPE = "INVOICE_TYPE";//发票类型
    public static final String INVOICE_HEAD = "INVOICE_HEAD";//发票抬头
    public static final String INVOICE_CONTENT = "INVOICE_CONTENT";//发票内容
    public static final String INVOICE_TYPE_ORDINARY = "1";//普通发票
    public static final String INVOICE_TYPE_VAT = "2";//增值税发票
    public static final String INVOICE_HEAD_PERSON = "个人";//发票抬头 个人
    public static final String INVOICE_CONTENT_WINE = "1";//发票内容  酒水
    public static final String INVOICE_CONTENT_DETAIL = "明细";//发票内容 明细

    /**
     * 订单来源(提交订单)
     */
    public static final String ORDER_FROM = "2"; //  订单来源	0，电话下单 1、PC   2、手机APP（区分安卓和ios）   3、H5

    /**
     * 提交订单的输入框标记
     */
    public static final String EDIT_ADDRESS="EDIT_ADDRESS";//详细地址
    public static final String EDIT_NAME="EDIT_NAME";//收件人
    public static final String EDIT_PHONE="EDIT_PHONE";//收件人手机


    /**
     * 券类型:1. 折扣券  2、优惠券 3、抵金券4. 礼品卡
     */
    public static final String COUPON_TYPE_Z="1";
    public static final String COUPON_TYPE_Y="2";
    public static final String COUPON_TYPE_D="3";
    public static final String COUPON_TYPE_L="4";

    /**
     * 券的获取场景: 1、新客专享全券(注册赠券) 2 分享券 3 系统券
     *
     */
    public static final String COUPON_ACTIVE_SCENE_REGIST = "1";
    public static final String COUPON_ACTIVE_SCENE_SHARE = "2";
    public static final String COUPON_ACTIVE_SCENE_SYSTEM = "3";

    /**
     * 微信支付回调error_code
     *
     * 0    成功         展示成功页面
     * -1	错误	        可能的原因：签名错误、未注册APPID、项目设置APPID不正确、注册的APPID与设置的不匹配、其他异常等。
     * -2	用户取消	    无需处理。发生场景：用户不支付了，点击取消，返回APP。
     */
    public static final int WXAPI_ERROR_CODE_SUCCESS = 0;
    public static final int WXAPI_ERROR_CODE_MISTAKE = -1;
    public static final int WXAPI_ERROR_CODE_CANCEL = -2;


    /**
     * 商品排序条件
     */
    public static final String WEIGHT = "comprehensive";//综合
    public static final String SALE = "sale";//销量
    public static final String PRICE_DOWN = "price_down";//价格降序
    public static final String PRICE_UP = "price_up";//价格升序

    /**
     * 全文搜索
     * 排序项  0、不指定 1、按销量 2、按价格
     *
     * 排序顺序  0、不指定 1、顺序  2、倒序
     *
     */
    public static final String SEARCH_UNSPECIFIED = "0";
    public static final String SEARCH_SALES="1";
    public static final String SEARCH_PRICE = "2";
    public static final String SEARCH_POSITIVE = "1";//正序
    public static final String SEARCH_REVERSE="2";//倒序

    /**
     * Intent  标签
     */
    public static final String PRODUCT_ID = "PRODUCT_ID";

    /**
     * 收藏标示  0 取消收藏  1 收藏成功
     */
    public static final String collection_cancel = "0";
    public static final String collection_confirm = "1";

}
