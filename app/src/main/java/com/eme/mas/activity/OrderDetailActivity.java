package com.eme.mas.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eme.mas.R;
import com.eme.mas.adapter.OrderGoodsAdapter;
import com.eme.mas.connection.WAction;
import com.eme.mas.connection.annotation.WLayout;
import com.eme.mas.controller.BaseImpl;
import com.eme.mas.controller.customeInterface.IOrder;
import com.eme.mas.customeview.FullyLinearLayoutManager;
import com.eme.mas.customeview.dialog.ChooseReasonDialog;
import com.eme.mas.customeview.dialog.OrderDetailAirLineDialog;
import com.eme.mas.model.OrderDetailResult;
import com.eme.mas.model.Result;
import com.eme.mas.model.entity.MyOrderBo;
import com.eme.mas.utils.NetworkStatusUtil;
import com.eme.mas.utils.ToastUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.eme.mas.R.id.bar_right_title;

/**
 * 订单详情页面
 * Created by zulei on 16/8/9.
 */
@WLayout(layoutId = R.layout.activity_order_detail)
public class OrderDetailActivity extends BaseActivity {

    @Bind(R.id.bar_title)
    TextView barTitle;
    @Bind(R.id.rv_order_detail_goods)
    RecyclerView rvOrderDetailGoods;
    @Bind(R.id.tv_order_detail_open_surplus)
    TextView tvOrderDetailOpenSurplus;
    @Bind(R.id.iv_order_detail_open_surplus)
    ImageView ivOrderDetailOpenSurplus;
    @Bind(R.id.ll_order_detail_open_surplus)
    LinearLayout llOrderDetailOpenSurplus;
    @Bind(R.id.tv_order_detail_served_time)
    TextView tvOrderDetailServedTime;
    @Bind(R.id.iv_line_left)
    ImageView ivLineLeft;
    @Bind(R.id.iv_line_right)
    ImageView ivLineRight;
    @Bind(R.id.tv_order_detail_status_one_time)
    TextView tvOrderDetailStatusOneTime;
    @Bind(R.id.tv_order_detail_status_two_time)
    TextView tvOrderDetailStatusTwoTime;
    @Bind(R.id.tv_order_detail_status_three_time)
    TextView tvOrderDetailStatusThreeTime;
    @Bind(R.id.iv_order_detail_point_two)
    ImageView ivOrderDetailPointTwo;
    @Bind(R.id.iv_order_detail_point_three)
    ImageView ivOrderDetailPointThree;
    @Bind(R.id.tv_order_detail_status)
    TextView tvOrderDetailStatus;
    @Bind(R.id.ll_order_detail_served_time)
    LinearLayout llOrderDetailServedTime;
    @Bind(R.id.ll_order_detail_foot_price)
    LinearLayout llOrderDetailFootPrice;
    @Bind(R.id.btn_order_detail_foot_center)
    Button btnOrderDetailFootCenter;
    @Bind(R.id.rl_order_detail_status_1)
    RelativeLayout rlOrderDetailStatus1;
    @Bind(R.id.rl_order_detail_status_2)
    RelativeLayout rlOrderDetailStatus2;
    @Bind(R.id.rl_order_detail_status_3)
    RelativeLayout rlOrderDetailStatus3;
    @Bind(R.id.btn_order_detail_foot_right)
    Button btnOrderDetailFootRight;
    @Bind(R.id.btn_order_detail_foot_left)
    Button btnOrderDetailFootLeft;
    @Bind(R.id.tv_order_detail_other_status)
    TextView tvOrderDetailOtherStatus;
    @Bind(R.id.tv_order_detail_status_two)
    TextView tvOrderDetailStatusTwo;
    @Bind(R.id.tv_order_detail_status_three)
    TextView tvOrderDetailStatusThree;
    @Bind(R.id.ll_order_detail_foot_btns)
    LinearLayout llOrderDetailFootBtns;
    @Bind(R.id.tv_order_detail_id)
    TextView tvOrderDetailId;
    @Bind(R.id.tv_order_detail_goodsmoney)
    TextView tvOrderDetailGoodsmoney;
    @Bind(R.id.tv_order_detail_benefit)
    TextView tvOrderDetailBenefit;
    @Bind(R.id.tv_order_detail_should_pay)
    TextView tvOrderDetailShouldPay;
    @Bind(R.id.tv_order_detail_pay_mode)
    TextView tvOrderDetailPayMode;
    @Bind(R.id.tv_order_detail_distribution_my_name)
    TextView tvOrderDetailDistributionMyName;
    @Bind(R.id.tv_order_detail_distribution_my_phone)
    TextView tvOrderDetailDistributionMyPhone;
    @Bind(R.id.tv_order_detail_distribution_my_address)
    TextView tvOrderDetailDistributionMyAddress;
    @Bind(R.id.tv_order_detail_distribution_brother_name)
    TextView tvOrderDetailDistributionBrotherName;
    @Bind(R.id.tv_order_detail_invoice_content)
    TextView tvOrderDetailInvoiceContent;
    @Bind(R.id.tv_order_deteil_ps_time)
    TextView tvOrderDeteilPsTime;
    @Bind(R.id.tv_order_deteil_renarks)
    TextView tvOrderDeteilRenarks;
    @Bind(R.id.tv_order_detail_money)
    TextView tvOrderDetailMoney;
    @Bind(R.id.tv_order_detail_over_time)
    TextView tvOrderDetailOverTime;
    @Bind(R.id.ll_av_loading_not_transparent_44)
    LinearLayout llAvLoadingNotTransparent44;
    @Bind(R.id.ll_av_loading_transparent_44)
    LinearLayout llAvLoadingTransparent44;
    @Bind(bar_right_title)
    TextView barRightTitle;

    private boolean goodsListIsOpen;
    private OrderGoodsAdapter orderGoodsAdapter;
    private String status, orderID;
    private IOrder iOrder;
    private MyOrderBo orderDetailBo;
    private List<MyOrderBo.ProductListBean> goods;
    private CountDownTimer dTimer;

    private final static String TAG = "OrderDetailActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        ((BaseImpl) iOrder).cancelRequestByTag(TAG);
        if (null != dTimer) {
            dTimer.cancel();
        }
    }

    @Override
    public void afterContentView() {
        super.afterContentView();
        initView();
        initData();
    }

    private void initView() {
        barTitle.setText(R.string.order_detail_title);
        barRightTitle.setText(R.string.order_detail_aftersale);
        rvOrderDetailGoods.setLayoutManager(new FullyLinearLayoutManager(this));
        rvOrderDetailGoods.setNestedScrollingEnabled(false);
        rvOrderDetailGoods.setHasFixedSize(true);
    }

    private void initData() {
        orderID = getIntent().getStringExtra("orderID");

        iOrder = mController.getOrder(this);
//        呃tai

        goodsListIsOpen = false;


    }

    private void updateView() {
        goods = orderDetailBo.getProduct_list();
        status = orderDetailBo.getOrder_status();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String shipperTime = orderDetailBo.getOrder_shipper_time();
        if (!TextUtils.isEmpty(shipperTime) && !"null".equals(shipperTime)) {
            Date date = new Date(Long.parseLong(shipperTime));
            shipperTime = sdf.format(date);
        }
        //status = "10";
        if (goods.size() <= 2) {
            llOrderDetailOpenSurplus.setVisibility(View.GONE);
        } else {
            llOrderDetailOpenSurplus.setVisibility(View.VISIBLE);
        }
        orderGoodsAdapter = new OrderGoodsAdapter(goods, R.layout.item_order_edit_goods);
        rvOrderDetailGoods.setAdapter(orderGoodsAdapter);

        tvOrderDetailId.setText(orderDetailBo.getOrder_number());
        tvOrderDetailGoodsmoney.setText(orderDetailBo.getTotalGoodsPrice());
        tvOrderDetailBenefit.setText(orderDetailBo.getCoupon_price());
        tvOrderDetailShouldPay.setText(orderDetailBo.getTotalPayPrice());
        tvOrderDetailPayMode.setText(orderDetailBo.getPay_way());

        tvOrderDetailDistributionMyName.setText(orderDetailBo.getAddress().getName());
        tvOrderDetailDistributionMyPhone.setText(orderDetailBo.getAddress().getMobile());
        tvOrderDetailDistributionMyAddress.setText(orderDetailBo.getAddress().getAddress());
        tvOrderDetailDistributionBrotherName.setText(orderDetailBo.getShipper_name());

        tvOrderDetailInvoiceContent.setText(orderDetailBo.getInvoice());
        tvOrderDeteilPsTime.setText(orderDetailBo.getCreate_time());
        tvOrderDeteilRenarks.setText(TextUtils.isEmpty(orderDetailBo.getOrder_message()) ? "无" : orderDetailBo.getOrder_message());

        tvOrderDetailMoney.setText(orderDetailBo.getTotalGoodsPrice());


        //20 正在配送
        //30 正在出库
        //40/50 已签收，未评价
        //10 待付款
        //80/100 售后处理中
        //200 已完成
        //510 已取消
        //501 超时未支付
        //102 售后处理完成

        if ("20".equals(status)) {
            tvOrderDetailServedTime.setText(shipperTime);
            ivLineLeft.setBackgroundResource(R.color.main_color_blue);
            ivLineRight.setBackgroundResource(R.color.cline_low);
            tvOrderDetailStatusOneTime.setText(orderDetailBo.getOrder_time());
            tvOrderDetailStatusTwoTime.setText(orderDetailBo.getCreate_time());
            ivOrderDetailPointTwo.setImageResource(R.mipmap.order_dian_blue);
            tvOrderDetailStatusTwo.setTextColor(ContextCompat.getColor(this, R.color.hint_text_gray));
        }

        if ("30".equals(status)) {
            tvOrderDetailServedTime.setText(shipperTime);
            ivLineLeft.setBackgroundResource(R.color.cline_low);
            ivLineRight.setBackgroundResource(R.color.cline_low);
            tvOrderDetailStatusOneTime.setText(orderDetailBo.getOrder_time());

        }

        if ("40".equals(status) || "50".equals(status)) {
            String isE = orderDetailBo.getEvaluation_status();
            if (!TextUtils.isEmpty(isE) && "1".equals(isE)) {//是否已经评价
                btnOrderDetailFootCenter.setVisibility(View.GONE);
            } else {
                btnOrderDetailFootCenter.setVisibility(View.VISIBLE);
            }
            tvOrderDetailStatus.setText("订单已完成");
            llOrderDetailServedTime.setVisibility(View.GONE);
            ivLineLeft.setBackgroundResource(R.color.main_color_blue);
            ivLineRight.setBackgroundResource(R.color.main_color_blue);
            ivOrderDetailPointTwo.setImageResource(R.mipmap.order_dian_blue);
            ivOrderDetailPointThree.setImageResource(R.mipmap.order_dian_blue);
            tvOrderDetailStatusTwo.setTextColor(ContextCompat.getColor(this, R.color.hint_text_gray));
            tvOrderDetailStatusThree.setTextColor(ContextCompat.getColor(this, R.color.hint_text_gray));
            tvOrderDetailStatusOneTime.setText(orderDetailBo.getOrder_time());
            tvOrderDetailStatusTwoTime.setText(orderDetailBo.getCreate_time());
            tvOrderDetailStatusThreeTime.setText(orderDetailBo.getFinnshed_time());
            llOrderDetailFootPrice.setVisibility(View.GONE);
            llOrderDetailFootBtns.setWeightSum(3);
            btnOrderDetailFootLeft.setText("申请售后");
        }

        if ("10".equals(status)) {
            String payWayCode = orderDetailBo.getPay_way_code(); //2货到付款 1在线支付
            //payWayCode = "2";
            if ("1".equals(payWayCode)) {
                rlOrderDetailStatus1.setVisibility(View.GONE);
                rlOrderDetailStatus2.setVisibility(View.VISIBLE);
                btnOrderDetailFootRight.setText("去支付");

                final long time = 90000;//FIXME

                dTimer = new CountDownTimer(time, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        SimpleDateFormat formatter;
                        if (millisUntilFinished > 60000) {
                            formatter = new SimpleDateFormat("mm分ss秒");//初始化Formatter的转换格式。
                        } else {
                            formatter = new SimpleDateFormat("ss秒");//初始化Formatter的转换格式。
                        }

                        String hms = formatter.format(millisUntilFinished);
                        tvOrderDetailOverTime.setText(hms);
                    }

                    @Override
                    public void onFinish() {
                        //超时未支付
                        rlOrderDetailStatus1.setVisibility(View.GONE);
                        rlOrderDetailStatus2.setVisibility(View.GONE);
                        rlOrderDetailStatus3.setVisibility(View.VISIBLE);
                        btnOrderDetailFootLeft.setVisibility(View.GONE);
                        tvOrderDetailOtherStatus.setText("订单已超时");
                        llOrderDetailFootPrice.setVisibility(View.INVISIBLE);
                        btnOrderDetailFootLeft.setVisibility(View.GONE);
                        btnOrderDetailFootRight.setText("再次购买");
                        status = "501";
                    }
                };
                dTimer.start();
            }
            if ("2".equals(payWayCode)) {
                tvOrderDetailServedTime.setText(shipperTime);
                ivLineLeft.setBackgroundResource(R.color.cline_low);
                ivLineRight.setBackgroundResource(R.color.cline_low);
                tvOrderDetailStatusOneTime.setText(orderDetailBo.getOrder_time());
            }

        }

        if ("80".equals(status) || "100".equals(status)) {
            rlOrderDetailStatus1.setVisibility(View.GONE);
            rlOrderDetailStatus2.setVisibility(View.GONE);
            rlOrderDetailStatus3.setVisibility(View.VISIBLE);
            btnOrderDetailFootLeft.setVisibility(View.GONE);
            tvOrderDetailOtherStatus.setText("售后处理中");
            llOrderDetailFootPrice.setVisibility(View.INVISIBLE);
        }

        if ("200".equals(status)) {
            tvOrderDetailServedTime.setText(shipperTime);
            ivLineLeft.setBackgroundResource(R.color.main_color_blue);
            ivLineRight.setBackgroundResource(R.color.main_color_blue);
            tvOrderDetailStatusOneTime.setText(orderDetailBo.getOrder_time());
            tvOrderDetailStatusTwoTime.setText(orderDetailBo.getCreate_time());
            tvOrderDetailStatusThreeTime.setText(orderDetailBo.getFinnshed_time());
            ivOrderDetailPointTwo.setImageResource(R.mipmap.order_dian_blue);
            ivOrderDetailPointThree.setImageResource(R.mipmap.order_dian_blue);
            tvOrderDetailStatusTwo.setTextColor(ContextCompat.getColor(this, R.color.hint_text_gray));
            tvOrderDetailStatusThree.setTextColor(ContextCompat.getColor(this, R.color.hint_text_gray));
            llOrderDetailFootPrice.setVisibility(View.INVISIBLE);
            btnOrderDetailFootLeft.setText("申请售后");
        }

        if ("510".equals(status)) {
            rlOrderDetailStatus1.setVisibility(View.GONE);
            rlOrderDetailStatus2.setVisibility(View.GONE);
            rlOrderDetailStatus3.setVisibility(View.VISIBLE);
            btnOrderDetailFootLeft.setVisibility(View.GONE);
            tvOrderDetailOtherStatus.setText("已取消");
            llOrderDetailFootPrice.setVisibility(View.INVISIBLE);
            btnOrderDetailFootLeft.setVisibility(View.GONE);
        }

        if ("501".equals(status)) {
            rlOrderDetailStatus1.setVisibility(View.GONE);
            rlOrderDetailStatus2.setVisibility(View.GONE);
            rlOrderDetailStatus3.setVisibility(View.VISIBLE);
            btnOrderDetailFootLeft.setVisibility(View.GONE);
            tvOrderDetailOtherStatus.setText("订单已超时");
            llOrderDetailFootPrice.setVisibility(View.INVISIBLE);
            btnOrderDetailFootLeft.setVisibility(View.GONE);
        }

        if ("102".equals(status)) {
            rlOrderDetailStatus1.setVisibility(View.GONE);
            rlOrderDetailStatus2.setVisibility(View.GONE);
            rlOrderDetailStatus3.setVisibility(View.VISIBLE);
            btnOrderDetailFootLeft.setVisibility(View.GONE);
            tvOrderDetailOtherStatus.setText("售后处理完成");
            llOrderDetailFootPrice.setVisibility(View.INVISIBLE);
            btnOrderDetailFootLeft.setVisibility(View.GONE);
        }

    }


    @OnClick(R.id.back)
    public void onClick() {
        finish();
    }

    @OnClick(R.id.btn_order_detail_foot_left)
    public void betLeftClick() {
        if ("20".equals(status) || "30".equals(status) || "10".equals(status)) {
            //取消订单
            final ChooseReasonDialog crDialog = new ChooseReasonDialog(this);
            crDialog.setOnChooseReasonListener(new ChooseReasonDialog.OnChooseReasonListener() {
                @Override
                public void showReason(int position, String reason) {
                    if (NetworkStatusUtil.isNetworkConnected(OrderDetailActivity.this)) {
                        isHideLayer(llAvLoadingTransparent44, false);
                        iOrder.orderCancel(orderDetailBo.getOrder_id(), reason, TAG);
                    } else {
                        ToastUtil.shortToast(OrderDetailActivity.this, R.string.net_error);
                    }

                }
            });

            crDialog.showDialog(this, null);
        }

        //申请售后
        if ("40".equals(status) || "50".equals(status) || "200".equals(status)) {
            ToastUtil.shortToast(this, "申请售后");
        }

    }

    @OnClick(R.id.btn_order_detail_foot_center)
    public void betCenterClick() {
        if ("40".equals(status) || "50".equals(status)) {
            //去评价
            Intent intent = new Intent(this, EvaluateActivity.class);
            intent.putExtra("MyOrderBo", orderDetailBo);
            this.startActivity(intent);
        }


    }

    @OnClick(R.id.bar_right_title)
    public void afterSaleClick() {
        //判断是否售后,再进入
        startActivity(new Intent(this,AftersaleDetailActivity.class));
    }

    @OnClick(R.id.btn_order_detail_foot_right)
    public void betRightClick() {
//        String payWayCode = orderDetailBo.getPay_way_code(); //2货到付款 1在线支付
//        //再次购买
//        if ("20".equals(status) || "30".equals(status) || "40".equals(status) || "50".equals(status)
//                || "80".equals(status) || "100".equals(status) || "200".equals(status) || "510".equals(status)
//                || "501".equals(status) || "102".equals(status) || "10".equals(status) && "2".equals(payWayCode)) {
//
//            //FIXME 添加购物车，目前没有spec_id
//            //goods
//            EventBus.getDefault().post(new SwitchMainFragmentEvent(WValue.MAIN_FRAGMENT_CART));
//            finish();
//
//        }
//
//        //去支付
//        if ("10".equals(status) && "1".equals(payWayCode)) {
//            Intent intent = new Intent(this, PayOnlineActivity.class);
//            startActivity(intent);
//        }
    }

    @OnClick(R.id.btn_order_contact_airline)
    public void betAirlineClick() {
        final OrderDetailAirLineDialog csDialog = new OrderDetailAirLineDialog();
        csDialog.setmOnAirLineListener(new OrderDetailAirLineDialog.OnAirLineListener() {
            @Override
            public void call() {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + "10086-1"));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                csDialog.cancel();
            }
        });
        csDialog.showDialog(this, null);
    }

    @OnClick(R.id.ll_order_detail_open_surplus)
    public void openSurplus() {
        orderGoodsAdapter.openHideList(!goodsListIsOpen);
        if (!goodsListIsOpen) {
            tvOrderDetailOpenSurplus.setText(R.string.order_edit_title_close_surplus);
            ivOrderDetailOpenSurplus.setImageResource(R.mipmap.shangla);
        } else {
            tvOrderDetailOpenSurplus.setText(R.string.order_edit_title_open_surplus);
            ivOrderDetailOpenSurplus.setImageResource(R.mipmap.xiala);
        }
        goodsListIsOpen = !goodsListIsOpen;

    }

    @Override
    public void onActionFail(Result result) {
        super.onActionFail(result);
        //ViewUtil.deleteProgressAV(viewProgress);
        setResult(Activity.RESULT_OK, getIntent());
        ToastUtil.shortToast(this, "订单详情获取失败，请稍后重试");
        //finish();
    }

    @Override
    public void onActionSucc(Result result) {
        super.onActionSucc(result);
        isHideLayer(llAvLoadingNotTransparent44, true);
        isHideLayer(llAvLoadingTransparent44, true);
        if (null != result) {
            String action = result.getAction();
            if (WAction.ORDER_DETAIL.equals(action)) {
                OrderDetailResult orderDetailResult = (OrderDetailResult) result;
                orderDetailBo = orderDetailResult.getData();
                updateView();

            }

            if (WAction.ORDER_CANCEL.equals(action)) {
                if (NetworkStatusUtil.isNetworkConnected(this)) {
                    isHideLayer(llAvLoadingTransparent44, false);
                    iOrder.orderDetail(orderID, TAG);
                } else {
                    ToastUtil.shortToast(OrderDetailActivity.this, R.string.net_error);
                }

            }
        }

    }


}
