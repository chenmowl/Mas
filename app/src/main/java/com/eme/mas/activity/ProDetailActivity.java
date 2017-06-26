package com.eme.mas.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eme.mas.MasApplication;
import com.eme.mas.R;
import com.eme.mas.connection.WAction;
import com.eme.mas.connection.annotation.WLayout;
import com.eme.mas.controller.BaseImpl;
import com.eme.mas.controller.customeInterface.IProduct;
import com.eme.mas.customeview.MListView;
import com.eme.mas.customeview.dialog.ProFootMarkDialog;
import com.eme.mas.data.SqlUtil;
import com.eme.mas.data.sql.DataRow;
import com.eme.mas.environment.WValue;
import com.eme.mas.eventbus.LoadingEvent;
import com.eme.mas.fragment.ProDetailOneFragment;
import com.eme.mas.fragment.ProDetailThreeFragment;
import com.eme.mas.model.GoodsDetailResult;
import com.eme.mas.model.Result;
import com.eme.mas.model.entity.GoodsDetailVo;
import com.eme.mas.utils.NetworkStatusUtil;
import com.eme.mas.utils.StringUtil;
import com.eme.mas.utils.ToastUtil;
import com.flyco.tablayout.SlidingTabLayout;
import com.owater.library.CircleTextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 商品详情页面
 * <p/>
 * Created by dijiaoliang on 16/8/5.
 */
@WLayout(layoutId = R.layout.activity_product_detail)
public class ProDetailActivity extends BaseCartActivity {

    private final static String TAG = ProDetailActivity.class.getSimpleName();


    @Bind(R.id.tab_title)
    SlidingTabLayout tabTitle;
    @Bind(R.id.vp)
    ViewPager vp;
    @Bind(R.id.rl_no_network)
    RelativeLayout rlNoNetwork;
    @Bind(R.id.rl_cart_add_loading)
    RelativeLayout rlCartAddLoading;

    private ProDetailOneFragment mProDetailOneFragment;
    private ProDetailThreeFragment mProDetailThreeFragment;
    private MyPagerAdapter mAdapter;

    private IProduct mProController;//商品控制器

    private String product_id;

    public String getProduct_id() {
        return product_id;
    }

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
        ((BaseImpl) mProController).cancelRequestByTag(TAG);
    }

    @Override
    public void beforeContentView() {
        super.beforeContentView();
        mProController = mController.getProduct(this);//初始化控制器
    }

    @Override
    public void afterContentView() {
        super.afterContentView();

        product_id = getIntent().getStringExtra(WValue.PRODUCT_ID);
//        product_id = "7E9aZq0qQOq3EjV4SR2ORUTiXyDq61w2Tw1uqhuHS8bYOOxvvWXWwg==";

        mAdapter = new MyPagerAdapter(getSupportFragmentManager());
        vp.setOffscreenPageLimit(2);
        vp.setAdapter(mAdapter);
        tabTitle.setViewPager(vp);
        if (product_id != null) {
            getProData();
        }
    }

    /**
     * 跳转到评价
     */
    public void toEvaluateFrag() {
        vp.setCurrentItem(2);
    }

    /**
     * 获取数据
     */
    public void getProData() {
        /**判断网络状态*/
        if (NetworkStatusUtil.isNetworkConnected(ProDetailActivity.this)) {
            isHideLayer(rlNoNetwork, true);
            isHideLayer(rlCartAddLoading, false);
            mProController.getProductDetailData(product_id, TAG);
        } else {
            ToastUtil.shortToast(ProDetailActivity.this, R.string.net_error);
        }
    }

    @Override
    public void initCartView() {
        tv_total_price = (TextView) findViewById(R.id.tv_total_price);
        btn_cart = (RelativeLayout) findViewById(R.id.bt);
        ll_dialog = (LinearLayout) findViewById(R.id.dialog);
        iv_all = (ImageView) findViewById(R.id.iv_all);
        bg = findViewById(R.id.rl_bg);
        ctv_badge = (CircleTextView) findViewById(R.id.ctv_badge);
        btn_balance = (Button) findViewById(R.id.btn_balance);
        mListView = (MListView) findViewById(R.id.cartList);
    }


    private class MyPagerAdapter extends FragmentPagerAdapter {

        private final String[] mTitles = {"商品", "详情", "评价"};

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return mTitles.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles[position];
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    if (mProDetailOneFragment == null)
                        mProDetailOneFragment = new ProDetailOneFragment();
                    return mProDetailOneFragment;
                case 1:
//                    if (mProDetailOneFragment == null)
//                        mProDetailOneFragment = new ProDetailOneFragment();
                    return new ProDetailOneFragment();
                case 2:
                    if (mProDetailThreeFragment == null){
                        mProDetailThreeFragment = new ProDetailThreeFragment();
                    }
                    Bundle bundle=new Bundle();
                    bundle.putString(WValue.PRODUCT_ID,product_id);
                    mProDetailThreeFragment.setArguments(bundle);
                    return mProDetailThreeFragment;
            }
            return null;
        }
    }


    @OnClick({R.id.back, R.id.ll_foot_mark})
    void click(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.ll_foot_mark:
                List<DataRow> footmarkData = SqlUtil.getFootmarkData();
                if (footmarkData != null && footmarkData.size() != 0) {
                    final ProFootMarkDialog csDialog = new ProFootMarkDialog(ProDetailActivity.this, footmarkData);
                    csDialog.setOnSkipListener(new ProFootMarkDialog.OnSkipListener() {
                        @Override
                        public void skip(String product_id) {
                            toProDetailActivity(product_id);
                            csDialog.cancel();
                        }
                    });
                    csDialog.showDialog(this, null);
                } else {
                    ToastUtil.shortToast(this, R.string.no_foot_mark);
                }
                break;
        }
    }


    /**
     * 跳转商品详情
     *
     * @param product_id
     */
    private void toProDetailActivity(String product_id) {
        /**判断网络状态*/
        if (NetworkStatusUtil.isNetworkConnected(MasApplication.getInstance())) {
            Intent intent = new Intent(this, ProDetailActivity.class);
            intent.putExtra(WValue.PRODUCT_ID, product_id);
            startActivity(intent);
        } else {
            ToastUtil.shortToast(this, R.string.net_error);
        }
    }


    // This method will be called when a MessageEvent is posted (in the UI thread for Toast)
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(LoadingEvent event) {
//        ToastUtil.shortToast(this,event.getTag());
        if ("1".equals(event.getTag())) {
            isHideLayer(rlCartAddLoading, false);
        } else {
            isHideLayer(rlCartAddLoading, true);
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }


    @Override
    public void onActionFail(Result result) {
        super.onActionFail(result);
        String msg = WValue.STRING_EMPTY;
        switch (result.getAction()){
            case WAction.PRODUCT_DETAIL_ACTIVITY_DATA:
                if(!StringUtil.isEmpty(result.getMsg())){
                    msg=result.getMsg();
                }else{
                    msg=getText(R.string.goods_get_error).toString();
                }
                break;
        }
        ToastUtil.shortToast(this,msg);
        isHideLayer(rlNoNetwork, false);
        isHideLayer(rlCartAddLoading, true);
    }

    @Override
    public void onActionSucc(Result result) {
        super.onActionSucc(result);
        switch (result.getAction()){
            case WAction.PRODUCT_DETAIL_ACTIVITY_DATA:
                GoodsDetailVo goodsDetailVo = ((GoodsDetailResult) result).getData();
//            if (bo != null && bo.getProduct() != null) {
//                //存储本地足迹
//                SqlUtil.addToFootmark(bo.getProduct());
//            }
                mProDetailOneFragment.updateProInfo(goodsDetailVo);
                if(goodsDetailVo!=null)
                mProDetailThreeFragment.updateScore(goodsDetailVo.getScoreFlag(),goodsDetailVo.getSaleQuantity());
                break;
        }
        isHideLayer(rlNoNetwork, true);
        isHideLayer(rlCartAddLoading, true);
    }
}
