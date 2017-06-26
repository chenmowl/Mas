package com.eme.mas.activity;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.eme.mas.R;
import com.eme.mas.connection.annotation.WLayout;
import com.hedgehog.ratingbar.RatingBar;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 订单评价页面
 *
 * Created by dijiaoliang on 17/1/17.
 */
@WLayout(layoutId = R.layout.activity_order_evaluate)
public class OrderEvaluateActivity extends BaseActivity {

    @Bind(R.id.bar_title)
    TextView barTitle;
    @Bind(R.id.rv_evaluate_goods)
    RecyclerView rvEvaluateGoods;
    @Bind(R.id.rbar_evaluate_speed)
    RatingBar rbarEvaluateSpeed;
    @Bind(R.id.rbar_evaluate_attitude)
    RatingBar rbarEvaluateAttitude;
    @Bind(R.id.iv_checkbox)
    ImageView ivCheckbox;
    @Bind(R.id.btn_evaluate_submit)
    Button btnEvaluateSubmit;
    @Bind(R.id.ll_av_loading_transparent_44)
    LinearLayout llAvLoadingTransparent44;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @Override
    public void beforeContentView() {
        super.beforeContentView();
    }

    @Override
    public void afterContentView() {
        super.afterContentView();
        barTitle.setText(getText(R.string.evaluate_title));
    }
}
