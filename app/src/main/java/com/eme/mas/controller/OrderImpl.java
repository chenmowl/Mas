package com.eme.mas.controller;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.eme.mas.connection.IActionListener;
import com.eme.mas.connection.WAction;
import com.eme.mas.controller.customeInterface.IOrder;

import java.util.HashMap;

/**
 * Created by zhangxiaoming on 16/6/6.
 */
public class OrderImpl extends BaseImpl implements IOrder {

    public OrderImpl(IActionListener iActionListener) {
        super(iActionListener);
    }


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
    @Override
    public void submitOrder(@NonNull String cartIds, @NonNull String paymentBranch, @NonNull String orderFrom, @Nullable String location, @Nullable String addressId,
                            @Nullable String provinceName, @Nullable String cityName, @Nullable String areaName, @NonNull String areaPath, @NonNull String address,
                            @NonNull String trueName, @NonNull String mobPhone, @Nullable String invoiceType, @Nullable String invoiceTitle, @Nullable String invoiceBody,
                            @Nullable String couponCodeId, @Nullable String orderShipperTime, @Nullable String buyerMessage, @NonNull String tag) {
        HashMap<String, String> params = new HashMap<>();
        params.put("cartIds", cartIds);
        params.put("paymentBranch", paymentBranch);
        params.put("orderFrom", orderFrom);
        params.put("areaPath", areaPath);
        params.put("address", address);
        params.put("trueName", trueName);
        params.put("mobPhone", mobPhone);
        if(!TextUtils.isEmpty(location)){
            params.put("location", location);
        }
        if(!TextUtils.isEmpty(addressId)){
            params.put("addressId", addressId);
        }
        if(!TextUtils.isEmpty(provinceName)){
            params.put("provinceName", provinceName);
        }
        if(!TextUtils.isEmpty(cityName)){
            params.put("cityName", cityName);
        }
        if(!TextUtils.isEmpty(areaName)){
            params.put("areaName", areaName);
        }
        if(!TextUtils.isEmpty(invoiceType)){
            params.put("invoiceType", invoiceType);
        }
        if(!TextUtils.isEmpty(invoiceTitle)){
            params.put("invoiceTitle", invoiceTitle);
        }
        if(!TextUtils.isEmpty(invoiceBody)){
            params.put("invoiceBody", invoiceBody);
        }
        if(!TextUtils.isEmpty(couponCodeId)){
            params.put("couponCodeId", couponCodeId);
        }
        if(!TextUtils.isEmpty(orderShipperTime)){
            params.put("orderShipperTime", orderShipperTime);
        }
        if(!TextUtils.isEmpty(buyerMessage)){
            params.put("buyerMessage", buyerMessage);
        }
        request(WAction.ORDER_SUBMIT,params,tag);
    }

    @Override
    public void orderDetail(String order_id,String tag) {
        HashMap<String, String> params = new HashMap<>();
        params.put("order_id", order_id);
        request(WAction.ORDER_DETAIL,params,tag);
    }

    @Override
    public void orderCancel(String order_id,String reason,String tag) {
        HashMap<String, String> params = new HashMap<>();
        params.put("order_id", order_id);
        params.put("reason", reason);
        request(WAction.ORDER_CANCEL,params,tag);
    }

    @Override
    public void orderQuery(String page_no, String status,String tag) {
        HashMap<String, String> params = new HashMap<>();
        params.put("page_no", page_no);
        params.put("status", status);
        request(WAction.ORDER_QUERY,params,tag);
    }

    @Override
    public void orderReturn(String page_no, String status,String tag) {
        HashMap<String, String> params = new HashMap<>();
        params.put("page_no", page_no);
        params.put("status", status);
        request(WAction.ORDER_RETURN,params,tag);
    }

    @Override
    public void orderExchange(String page_no, String status,String tag) {
        HashMap<String, String> params = new HashMap<>();
        params.put("page_no", page_no);
        params.put("status", status);
        request(WAction.ORDER_EXCHANGE,params,tag);
    }

    @Override
    public void orderSigned(String page_no, String status,String tag) {
        HashMap<String, String> params = new HashMap<>();
        params.put("page_no", page_no);
        params.put("status", status);
        request(WAction.ORDER_SIGNED,params,tag);

    }

    @Override
    public void orderDelete(String order_id, String tag) {
        HashMap<String, String> params = new HashMap<>();
        params.put("order_id", order_id);
        request(WAction.ORDER_DELETE,params,tag);
    }

    @Override
    public void orderCount(String tag) {
        request(WAction.ORDER_COUNT,null,tag);
    }

    @Override
    public void getPrePayInfo(String orderId,String body,int payFrom,String tag) {
        HashMap<String, String> params = new HashMap<>();
        params.put("orderId", orderId);
        params.put("body", body);
        params.put("payFrom", payFrom+"");
        request(WAction.WX_PREPAY,params,tag);
    }

    @Override
    public void getPayAmount(String cartIds, String couponCodeIds, String tag) {
        HashMap<String, String> params = new HashMap<>();
        params.put("cartIds", cartIds);
        if(!TextUtils.isEmpty(couponCodeIds)){
            params.put("couponCodeIds", couponCodeIds);
        }
        request(WAction.ORDER_AMOUNT_PAYABLE,params,tag);
    }

    @Override
    public void getOrderCoupon(String cartIds, String tag) {
        HashMap<String, String> params = new HashMap<>();
        params.put("cartIds", cartIds);
        request(WAction.ORDER_AVAILABLE_COUPON,params,tag);
    }
}
