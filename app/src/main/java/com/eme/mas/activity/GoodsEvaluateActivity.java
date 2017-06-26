package com.eme.mas.activity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eme.mas.R;
import com.eme.mas.connection.annotation.WLayout;
import com.hedgehog.ratingbar.RatingBar;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 商品评价页面
 * <p>
 * Created by dijiaoliang on 17/1/17.
 */
@WLayout(layoutId = R.layout.activity_goods_evaluate)
public class GoodsEvaluateActivity extends BaseActivity {


    @Bind(R.id.back)
    LinearLayout back;
    @Bind(R.id.bar_title)
    TextView barTitle;
    @Bind(R.id.rl_title)
    RelativeLayout rlTitle;
    @Bind(R.id.iv_goods)
    ImageView ivGoods;
    @Bind(R.id.tv_score)
    TextView tvScore;
    @Bind(R.id.rb_evaluate_goods)
    RatingBar rbEvaluateGoods;
    @Bind(R.id.et_shopping_feel)
    EditText etShoppingFeel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
