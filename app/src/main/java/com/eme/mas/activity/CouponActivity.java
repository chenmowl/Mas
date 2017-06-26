package com.eme.mas.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.eme.mas.MasApplication;
import com.eme.mas.R;
import com.eme.mas.adapter.CouponAdapter;
import com.eme.mas.connection.WAction;
import com.eme.mas.connection.annotation.WLayout;
import com.eme.mas.controller.BaseImpl;
import com.eme.mas.controller.customeInterface.IUserInfo;
import com.eme.mas.customeview.dialog.CouponExplainDialog;
import com.eme.mas.model.CouponResult;
import com.eme.mas.model.Result;
import com.eme.mas.model.entity.CouponBo;
import com.eme.mas.utils.NetworkStatusUtil;
import com.eme.mas.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 我的优惠券页面
 * Created by zulei on 16/8/10.
 */
@WLayout(layoutId = R.layout.activity_coupon)
public class CouponActivity extends BaseActivity {

    @Bind(R.id.bar_title)
    TextView barTitle;
    @Bind(R.id.bar_right_title)
    TextView barRightTitle;
    @Bind(R.id.tv_coupon_expire_count)
    TextView tvCouponExpireCount;
    @Bind(R.id.ll_coupon_expire_count)
    LinearLayout llCouponExpireCount;
    @Bind(R.id.rl_no_network)
    RelativeLayout rlNoNetwork;
    @Bind(R.id.tv_no_data_hint)
    TextView tvNoDataHint;
    @Bind(R.id.iv_no_data_pic)
    ImageView ivNoDataPic;
    @Bind(R.id.rl_no_data)
    RelativeLayout rlNoData;
    @Bind(R.id.rv_coupon)
    RecyclerView rvCoupon;
    @Bind(R.id.ll_av_loading_transparent_44)
    LinearLayout llAvLoading;
    @Bind(R.id.rl_subtitle)
    RelativeLayout rlSubtitle;

    private CouponAdapter cAdapter;
    private IUserInfo iUserInfo;
    private List<CouponBo> couponBos;
    private int aboutToExpire; //即将过期优惠券数量

    private final static String TAG = "CouponActivity";

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
        ((BaseImpl) iUserInfo).cancelRequestByTag(TAG);
    }

    @Override
    public void afterContentView() {
        super.afterContentView();
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    private void initView() {
        barTitle.setText(R.string.coupon_title);
        barRightTitle.setText(R.string.coupon_title_add);
        tvNoDataHint.setText(R.string.coupon_hint_no_data);
        ivNoDataPic.setImageResource(R.mipmap.wu);
        rvCoupon.setLayoutManager(new LinearLayoutManager(this));
    }

    private void initData() {
        iUserInfo = mController.getUserInfo(this);
        couponBos = new ArrayList();

        cAdapter = new CouponAdapter(this, couponBos);
        cAdapter.isFirstOnly(true);
        cAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
        rvCoupon.setAdapter(cAdapter);

        rlSubtitle.setVisibility(View.GONE);
        getDataOnline();

    }

    private void getDataOnline() {
        if (NetworkStatusUtil.isNetworkConnected(this)) {
            isHideLayer(llAvLoading, false);
            iUserInfo.getCoupon(TAG);
        } else {
            ToastUtil.shortToast(MasApplication.getInstance(), R.string.net_error);
        }
    }

    private void updateView() {
        if (null != couponBos && couponBos.size() != 0) {
            rlNoData.setVisibility(View.GONE);
            cAdapter.notifyDataSetChanged();
        } else {
            rlNoData.setVisibility(View.VISIBLE);
        }
        llCouponExpireCount.setVisibility(View.VISIBLE);
        tvCouponExpireCount.setText(aboutToExpire + "");
        rlSubtitle.setVisibility(View.VISIBLE);
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

    @OnClick(R.id.btn_stroll)
    public void loadAgain() {
        getDataOnline();
        rlNoNetwork.setVisibility(View.GONE);
    }

    @OnClick(R.id.btn_coupon_explain)
    public void explain() {
        //优惠券说明
        final CouponExplainDialog csDialog = new CouponExplainDialog();
        csDialog.showDialog(this, null);
    }

    @Override
    public void onActionFail(Result result) {
        super.onActionFail(result);
        isHideLayer(llAvLoading, true);
        rlNoNetwork.setVisibility(View.VISIBLE);
    }

    @Override
    public void onActionSucc(Result result) {
        super.onActionSucc(result);
        isHideLayer(llAvLoading, true);
        if (null != result) {
            String action = result.getAction();
            if (WAction.GET_COUPON.equals(action)) {
                CouponResult couponResult = (CouponResult) result;
                try {
                    couponBos.addAll(couponResult.getData().getList());
                    aboutToExpire = couponResult.getData().getCountMap().getAboutToExpire();
                } catch (Exception e) {

                }
                updateView();
            }

        }

    }

    private CouponBo getTestCouponBo() {
        CouponBo couponBo = new CouponBo();
        couponBo.setCouponName("测试券");
        couponBo.setEnable("0");
        couponBo.setUseLimitAmount(9999);
        couponBo.setUseDiscountAmount(1999);
        couponBo.setUseStartTime(System.currentTimeMillis());
        couponBo.setUseEndTime(System.currentTimeMillis());
        couponBo.setUseRangeProductType(1);
        return couponBo;
    }


}
