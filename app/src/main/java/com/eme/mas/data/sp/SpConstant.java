package com.eme.mas.data.sp;

/**
 * Created by zhangxiaoming on 16/6/15.
 */
public class SpConstant {

    /**
     * 登录状态
     */
    public static final String LOGIN_FILE_NAME = "LoginState";

    public static final String LOGIN_KEY = "LoginState";


    /**
     * header
     */
    public static final String HEAD_FILE_NAME = "headerInfo";

    public static final String HEAD_VERSION_KEY = "version";
    public static final String HEAD_USER_ID_KEY = "userId";
    public static final String HEAD_Message_ID_KEY = "push_message";
    public static final String HEAD_DEVICE_ID_KEY = "deviceId";

    /**
     * 定位
     */
    public static final String LOCATION_FILE_NAME = "currentLocation";
    public static final String LOCATION_KEY = "location";
    public static final String LOCATION_NAME_KEY = "locationName";
    public static final String LOCATION_CITY_CODE = "CITYCODE";


    /**
     * 记录listView、gridView滑动位置
     */
    public static final String MAIN_PAGE_FILE_NAME = "mainPageFragment";
    public static final String HOT_SALE_FILE_NAME = "hotSaleFragment";
    public static final String X_KEY = "x";
    public static final String Y_KEY = "y";

    /**
     * 记录用户选择的优惠券id与价格
     */
    public static final String CHOICE_COUPON_FILE_NAME = "choiceCoupon";
    public static final String COUPON_ID_KEY = "couponId";
    public static final String COUPON_PRICE_KEY = "couponPrice";

    /**
     * 搜索纪录
     */
    public static final String FILE_SEARCH = "FILE_SEARCH";

    public static final String KEY_SEARCH_RECORD = "KEY_SEARCH_RECORD";
}
