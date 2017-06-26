package com.eme.mas.activity;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.eme.mas.MasApplication;
import com.eme.mas.R;
import com.eme.mas.adapter.EvaluateGoodsAdapter;
import com.eme.mas.connection.WAction;
import com.eme.mas.connection.annotation.WLayout;
import com.eme.mas.controller.BaseImpl;
import com.eme.mas.controller.customeInterface.IProduct;
import com.eme.mas.customeview.FullyLinearLayoutManager;
import com.eme.mas.model.Result;
import com.eme.mas.model.entity.EvaluateBo;
import com.eme.mas.model.entity.MyOrderBo;
import com.eme.mas.utils.KeyboardUtils;
import com.eme.mas.utils.NetworkStatusUtil;
import com.eme.mas.utils.ToastUtil;
import com.hedgehog.ratingbar.RatingBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 评价页面
 * Created by zulei on 16/8/8.
 */
@WLayout(layoutId = R.layout.activity_evaluate)
public class EvaluateActivity extends BaseActivity {

    private final static String TAG = EvaluateActivity.class.getSimpleName();

    @Bind(R.id.bar_title)
    TextView barTitle;
    @Bind(R.id.rbar_evaluate_speed)
    RatingBar rbarEvaluateSpeed;
    @Bind(R.id.rv_evaluate_goods)
    RecyclerView rvEvaluateGoods;
    @Bind(R.id.rbar_evaluate_attitude)
    RatingBar rbarEvaluateAttitude;
    @Bind(R.id.view_require_nottop_bugs)
    View viewRequireNottopBugs;
    @Bind(R.id.ll_av_loading_transparent_44)
    LinearLayout llAvLoadingTransparent44;

    private IProduct iProduct;
    private EvaluateGoodsAdapter evaluateGoodsAdapter;
    private MyOrderBo myOrderBo;
    private List<MyOrderBo.ProductListBean> goods;
    private float starSpeed, starAttitude;

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
        ((BaseImpl) iProduct).cancelRequestByTag(TAG);
    }

    @Override
    public void afterContentView() {
        super.afterContentView();
        initView();
        initListener();
        initData();
    }

    private void initView() {
        barTitle.setText(R.string.evaluate_title);

        rvEvaluateGoods.setLayoutManager(new FullyLinearLayoutManager(this));
        rvEvaluateGoods.setNestedScrollingEnabled(false);
        rvEvaluateGoods.setHasFixedSize(true);

        viewRequireNottopBugs.setFocusable(true);
        viewRequireNottopBugs.setFocusableInTouchMode(true);
        viewRequireNottopBugs.requestFocus();

        rbarEvaluateSpeed.setStar(5.0f);
        rbarEvaluateAttitude.setStar(5.0f);
    }

    private void initListener() {
        rbarEvaluateSpeed.setOnRatingChangeListener(
                new RatingBar.OnRatingChangeListener() {
                    @Override
                    public void onRatingChange(float ratingCount) {
                        starSpeed = ratingCount;
                    }
                }
        );
        rbarEvaluateAttitude.setOnRatingChangeListener(
                new RatingBar.OnRatingChangeListener() {
                    @Override
                    public void onRatingChange(float ratingCount) {
                        starAttitude = ratingCount;
                    }
                }
        );
    }

    private void initData() {
        iProduct = mController.getProduct(this);
        starSpeed = 5.0f;
        starAttitude = 5.0f;
        myOrderBo = (MyOrderBo) getIntent().getSerializableExtra("MyOrderBo");
        goods = myOrderBo.getProduct_list();

        evaluateGoodsAdapter = new EvaluateGoodsAdapter(goods, R.layout.item_evaluate);
        rvEvaluateGoods.setAdapter(evaluateGoodsAdapter);

    }

    @OnClick(R.id.back)
    public void back() {
        finish();
    }


    @OnClick(R.id.btn_evaluate_submit)
    public void submit() {
        KeyboardUtils.closeInputMethod(this);
        String[] contents = evaluateGoodsAdapter.getmContents();
        float[] stars = evaluateGoodsAdapter.getmStars();

        Log.i("info", "a=" + starSpeed);
        Log.i("info", "b=" + starAttitude);

        for (int i = 0; i < contents.length; i++) {
            Log.i("info", "c=" + contents[i]);
        }

        for (int i = 0; i < stars.length; i++) {
            Log.i("info", "s=" + stars[i] + "");
        }

        EvaluateBo evaluateBo = new EvaluateBo();
        evaluateBo.setOrderId(myOrderBo.getOrder_id());
        evaluateBo.setOrderNo(myOrderBo.getOrder_number());
        evaluateBo.setServiceScore(String.valueOf((int) starAttitude));
        evaluateBo.setDeliveryScore(String.valueOf((int) starSpeed));
        evaluateBo.setGevalIsAnonymous("0"); //是否匿名 1是0不是

        List<EvaluateBo.EvaluateGoodsListBean> listBeen = new ArrayList<>();
        for (int i = 0; i < myOrderBo.getProduct_list().size(); i++) {
            EvaluateBo.EvaluateGoodsListBean bean = new EvaluateBo.EvaluateGoodsListBean();
            bean.setGoodsId(myOrderBo.getProduct_list().get(i).getProduct_id());
            bean.setGoodsContent(TextUtils.isEmpty(contents[i]) ? "" : contents[i]);
            bean.setGoodsScore(String.valueOf((int) stars[i]));
            listBeen.add(bean);
        }
        evaluateBo.setEvaluateGoodsList(listBeen);

        if (NetworkStatusUtil.isNetworkConnected(this)) {
            isHideLayer(llAvLoadingTransparent44,false);
            iProduct.setEvaluate(JSON.toJSONString(evaluateBo), TAG);
        } else {
            ToastUtil.shortToast(MasApplication.getInstance(), R.string.net_error);
        }
    }

    @Override
    public void onActionFail(Result result) {
        super.onActionFail(result);
        isHideLayer(llAvLoadingTransparent44,true);
        ToastUtil.shortToast(this, R.string.evaluate_hint_submit_fail);
    }

    @Override
    public void onActionSucc(Result result) {
        super.onActionSucc(result);
        isHideLayer(llAvLoadingTransparent44,true);
        if (null != result) {
            String action = result.getAction();
            if (WAction.SET_EVALUATE.equals(action)) {
                ToastUtil.shortToast(EvaluateActivity.this, R.string.evaluate_hint_submit_ok);
                finish();
            }

        }

    }
}
