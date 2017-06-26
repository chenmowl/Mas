package com.eme.mas.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.eme.mas.R;
import com.eme.mas.adapter.OrderCouponAdapter;
import com.eme.mas.connection.WAction;
import com.eme.mas.connection.annotation.WLayout;
import com.eme.mas.controller.BaseImpl;
import com.eme.mas.controller.customeInterface.IOrder;
import com.eme.mas.model.OrderCouponResult;
import com.eme.mas.model.Result;
import com.eme.mas.model.entity.OrderCouponBo;
import com.eme.mas.utils.NetworkStatusUtil;
import com.eme.mas.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.recyclerview.animators.OvershootInLeftAnimator;

/**
 * 订单优惠券页面
 * Created by zulei on 16/8/10.
 */
@WLayout(layoutId = R.layout.activity_order_coupon)
public class OrderCouponActivity extends BaseActivity {

    private final static String TAG = OrderCouponActivity.class.getSimpleName();

    @Bind(R.id.bar_title)
    TextView barTitle;
    @Bind(R.id.iv_coupon_bia)
    ImageView ivCouponBia;
    @Bind(R.id.tv_order_coupon_enable_count)
    TextView tvOrderCouponEnableCount;
    @Bind(R.id.rv_coupon)
    RecyclerView rvCoupon;
    @Bind(R.id.rl_subtitle)
    RelativeLayout rlSubtitle;
    @Bind(R.id.ll_av_loading_transparent_44)
    LinearLayout llAvLoadingTransparent44;
    @Bind(R.id.bar_right_title)
    TextView barRightTitle;

    private View viewNoData,viewNetErr;

    private IOrder iOrder;
    private OrderCouponAdapter adapter;
    private List<OrderCouponBo> couponBos;
    private String cardIds;


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
    }

    @Override
    public void afterContentView() {
        super.afterContentView();
        initView();
        initData();
        initListener();
    }

    private void initView() {
        barRightTitle.setText(R.string.coupon_title_add);
        barTitle.setText(R.string.coupon_title);
        rvCoupon.setLayoutManager(new LinearLayoutManager(this));
        rlSubtitle.setVisibility(View.GONE);
    }

    private void initData() {
        iOrder = mController.getOrder(this);
        cardIds = getIntent().getStringExtra("cardIds");

        //设置recycleview显示模式
        rvCoupon.setLayoutManager(new LinearLayoutManager(this));
        //设置recycleview添加删除动画 引用jp.wasabeef:recyclerview-animators:2.2.3
        rvCoupon.setItemAnimator(new OvershootInLeftAnimator());

        //初始化空数据页面
        initEmptyView();
        //初始化网络异常页面
        iniNetErrView();

        //初始化Adapter
        couponBos = new ArrayList<>();
        adapter = new OrderCouponAdapter(this,couponBos);
        //设置item加载动画显示模式，true只显示1次，false每次都显示
        adapter.isFirstOnly(true);
        //设置item加载动画样式
        adapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        //设置空页面，显示时无需主动调用，无数据时自动加载
        adapter.setNoDataView(viewNoData);
        //设置网络异常页面，显示时需手动调用showNetErrView
        adapter.setNetErrView(viewNetErr);
        //设置Adapter
        rvCoupon.setAdapter(adapter);

        getCouponOnline();
    }

    private void initEmptyView(){
        viewNoData = getLayoutInflater().inflate(R.layout.layout_no_data_full, (ViewGroup) rvCoupon.getParent(), false);
        viewNoData.findViewById(R.id.rl_no_data).setVisibility(View.VISIBLE);
        viewNoData.findViewById(R.id.btn_stroll_nodata).setVisibility(View.GONE);
        ((TextView)viewNoData.findViewById(R.id.tv_no_data_hint)).setText(R.string.my_address_hint_no_data);
        ((ImageView)viewNoData.findViewById(R.id.iv_no_data_pic)).setImageResource(R.mipmap.address_wudizhi);
    }

    private void iniNetErrView(){
        viewNetErr = getLayoutInflater().inflate(R.layout.no_network_loading_full, (ViewGroup) rvCoupon.getParent(), false);
        viewNetErr.findViewById(R.id.rl_no_network).setVisibility(View.VISIBLE);
        Button btnScroll = (Button)viewNetErr.findViewById(R.id.btn_stroll);
        btnScroll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCouponOnline();
            }
        });
    }

    private void initListener() {
        rvCoupon.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void SimpleOnItemClick(RecyclerView.Adapter adapter, View view, int position) {
                BaseQuickAdapter ad = (BaseQuickAdapter) adapter;
                OrderCouponBo bo = (OrderCouponBo) ad.getItem(position);
                Intent intent = new Intent();
                intent.putExtra("id", bo.getCouponCodeId());
//                    intent.putExtra("amount", bo.getUseDiscountAmount() + "");
                intent.putExtra("couponName", bo.getActiveScene());
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    private void getCouponOnline() {
        if (NetworkStatusUtil.isNetworkConnected(this)) {
            isHideLayer(llAvLoadingTransparent44, false);
            iOrder.getOrderCoupon(cardIds,TAG);
        } else {
            ToastUtil.shortToast(OrderCouponActivity.this, R.string.net_error);
        }
    }

    private void updateView() {
        if (null != couponBos && couponBos.size() != 0) {
            ivCouponBia.setVisibility(View.GONE);
            //adapter.setList(couponBos);
            adapter.notifyDataSetChanged();
//            tvOrderCouponEnableCount.setText(couponBos.size());
        }else{
            rlSubtitle.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.back)
    public void back() {
        finish();
    }

    @OnClick(R.id.bar_right_title)
    public void toAddCoupon() {
        Intent intent = new Intent(this, CouponAddActivity.class);
        startActivity(intent);
    }

    @Override
    public void onActionFail(Result result) {
        super.onActionFail(result);

        isHideLayer(llAvLoadingTransparent44, true);
        String message = result.getMsg();
        switch (result.getAction()){
            case WAction.ORDER_AVAILABLE_COUPON:
                if(TextUtils.isEmpty(message)){
                    message=getText(R.string.toast_order_coupon_failure).toString().trim();
                }
                break;
        }
        ToastUtil.shortToast(this, message);
    }

    @Override
    public void onActionSucc(Result result) {
        super.onActionSucc(result);
        isHideLayer(llAvLoadingTransparent44, true);
        switch (result.getAction()){
            case WAction.ORDER_AVAILABLE_COUPON:
                OrderCouponResult couponResult = (OrderCouponResult) result;
                if(couponResult.getData().size()!=0){
                    try {
                        couponBos.addAll(couponResult.getData());
                    } catch (Exception e) {

                    }
                    updateView();
                }
                break;
        }
    }

}
