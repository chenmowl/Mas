package com.eme.mas.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.eme.mas.R;
import com.eme.mas.connection.annotation.WLayout;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

/**
 * 订单优惠券页面
 * Created by zulei on 16/8/10.
 */
@WLayout(layoutId = R.layout.activity_coupon_add)
public class CouponAddActivity extends BaseActivity {


    @Bind(R.id.bar_title)
    TextView barTitle;
    @Bind(R.id.bar_right_title)
    TextView barRightTitle;
    @Bind(R.id.ib_coupon_add_etdelete)
    ImageButton ibCouponAddEtdelete;
    @Bind(R.id.et_coupon_add)
    EditText etCouponAdd;
    @Bind(R.id.btn_coupon_add_done)
    Button btnCouponAddDone;
    @Bind(R.id.ll_av_loading_transparent_44)
    LinearLayout llAvLoadingTransparent44;

    private String code;

    private final static String TAG = "CouponAddActivity";

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
        //((BaseImpl)iUserInfo).cancelRequestByTag(TAG);
    }

    @Override
    public void afterContentView() {
        super.afterContentView();
        initView();
        initData();
    }

    private void initView() {
        barTitle.setText(R.string.coupon_title_add);
        barRightTitle.setText(R.string.coupon_title_help);
    }

    private void initData() {
    }

    @OnClick(R.id.back)
    public void back() {
        finish();
    }

    @OnClick(R.id.ib_coupon_add_etdelete)
    public void codeDelete() {
        etCouponAdd.setText("");
        ibCouponAddEtdelete.setVisibility(View.GONE);
        btnCouponAddDone.setBackgroundResource(R.drawable.btn_status_unable);
    }

    @OnTextChanged(R.id.et_coupon_add)
    public void etChanged(CharSequence text) {
        code = text.toString();
        if (TextUtils.isEmpty(code)) {
            ibCouponAddEtdelete.setVisibility(View.GONE);
            btnCouponAddDone.setBackgroundResource(R.drawable.btn_status_unable);
        } else {
            ibCouponAddEtdelete.setVisibility(View.VISIBLE);
            btnCouponAddDone.setBackgroundResource(R.drawable.btn_status_enable);
        }
    }

}
