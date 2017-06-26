package com.eme.mas.controller.customeInterface;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by zhangxiaoming on 16/6/6.
 */
public interface IOrder {

    /**
     *
     * 提交订单
     *
     * @param cartIds               //  购物车ids  以逗号隔开
     * @param paymentBranch         //  支付分支   1、货到付款 2、在线支付
     * @param orderFrom             //  订单来源	0，电话下单 1、PC   2、手机APP（区分安卓和ios）   3、H5
     * @param location              //  经纬度 (地址ID和经纬度必须至少选择一个)    "经度,纬度"
     * @param addressId             //  地址ID (与Location必传其一)
     * @param provinceName          //  省
     * @param cityName              //  市
     * @param areaName              //  区
     * @param areaPath              //  街道
     * @param address               //  详细地址
     * @param trueName              //  收货人姓名
     * @param mobPhone              //  收货人手机号
     * @param invoiceType           //  发票类型(可选)    1普通发票 2增值税发票(暂时没有)
     * @param invoiceTitle          //  发票抬头(可选)    如果是个人,则传"个人"; 如果是单位,则传用户输入的单位名称
     * @param invoiceBody           //  发票描述(可选)    如果是酒水,传"1"; 如果是明细,则传"明细"
     * @param couponCodeId              //  优惠券id(可选)
     * @param orderShipperTime      //  用户指定配送时间(可选)   格式"yyyy-MM-dd HH:mm:ss"
     * @param buyerMessage          //  用户备注(可选)
     * @param tag
     */
    void  submitOrder(@NonNull String cartIds, @NonNull String paymentBranch, @NonNull String orderFrom, @Nullable String location, @Nullable String addressId,
                      @Nullable String provinceName, @Nullable String cityName, @Nullable String areaName, @NonNull String areaPath, @NonNull String address,
                      @NonNull String trueName, @NonNull String mobPhone, @Nullable String invoiceType, @Nullable String invoiceTitle, @Nullable String invoiceBody,
                      @Nullable String couponCodeId, @Nullable String orderShipperTime, @Nullable String buyerMessage, @NonNull String tag);
    /**
     * 订单详情
     */
    void  orderDetail(String order_id,String tag);
    /**
     * 取消订单
     */
    void  orderCancel(String order_id,String reason,String tag);
    /**
     * 查询订单
     */
    void  orderQuery(String page_no,String status,String tag);
    /**
     * 申请退货
     */
    void  orderReturn(String page_no,String status,String tag);
    /**
     * 申请换货
     */
    void  orderExchange(String page_no,String status,String tag);
    /**
     * 确认签收
     */
    void  orderSigned(String page_no,String status,String tag);
    /**
     * 删除订单
     */
    void  orderDelete(String order_id,String tag);
    /**
     * 订单数量
     */
    void  orderCount(String tag);
    /**
     * 拉取预支付信息
     * payFrom 1、手机  2、web 3、H5
     */
    void  getPrePayInfo(String orderId,String body,int payFrom,String tag);

    /**
     *  计算订单的应付金额,优惠金额,邮费,商品总额 (提交订单)
     * @param cartIds
     * @param couponCodeIds
     * @param tag
     */
    void  getPayAmount(@NonNull String cartIds,@Nullable String couponCodeIds,@NonNull String tag);

    /**
     *  手机端优惠券使用前查询可用优惠券列表--->(用于用户在使用优惠券的时候，供用户对优惠券进行选择),并根据1.过期时间升序2.优惠面额降序 进行排序
     * @param cartIds
     * @param tag
     */
    void  getOrderCoupon(@NonNull String cartIds,@NonNull String tag);
}
