package com.eme.mas.activity;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.eme.mas.MainActivity;
import com.eme.mas.MasApplication;
import com.eme.mas.R;
import com.eme.mas.adapter.SplashAdapter;
import com.eme.mas.connection.WAction;
import com.eme.mas.connection.annotation.WLayout;
import com.eme.mas.controller.customeInterface.ICart;
import com.eme.mas.controller.customeInterface.IProduct;
import com.eme.mas.data.SqlUtil;
import com.eme.mas.model.CartListResult;
import com.eme.mas.model.CategoryResult;
import com.eme.mas.model.Result;
import com.eme.mas.model.entity.CartBo;
import com.eme.mas.model.entity.CategorySpecVo;
import com.eme.mas.utils.NetworkStatusUtil;
import com.eme.mas.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by zhangxiaoming on 16/6/22.
 */

/*
                   _ooOoo_
                  o8888888o
                  88" . "88
                  (| -_- |)
                  O\  =  /O
               ____/`---'\____
             .'  \\|     |//  `.
            /  \\|||  :  |||//  \
           /  _||||| -:- |||||-  \
           |   | \\\  -  /// |   |
           | \_|  ''\---/''  |   |
           \  .-\__  `-`  ___/-. /
         ___`. .'  /--.--\  `. . __
      ."" '<  `.___\_<|>_/___.'  >'"".
     | | :  `- \`.;`\ _ /`;.`/ - ` : | |
     \  \ `-.   \_ __\ /__ _/   .-` /  /
======`-.____`-.___\_____/___.-`____.-'======
                   `=---='
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
         佛祖保佑       永无BUG
*/

@WLayout(layoutId = R.layout.activity_start)
public class StartActivity extends BaseActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {

    private final static String TAG = StartActivity.class.getSimpleName();

    @Bind(R.id.vp_splash)
    ViewPager vpSplash;
    @Bind(R.id.indicator01)
    ImageView indicator01;
    @Bind(R.id.indicator02)
    ImageView indicator02;
    @Bind(R.id.indicator03)
    ImageView indicator03;
    @Bind(R.id.llyt_indicaters)
    LinearLayout llytIndicaters;


    private IProduct mProController;//商品控制器

    private ICart mCartController;

    @Override
    public void afterContentView() {
        super.afterContentView();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        ImageView image01 = new ImageView(this);
        image01.setLayoutParams(params);
        image01.setScaleType(ImageView.ScaleType.FIT_XY);
        image01.setImageResource(R.mipmap.splashlead_01);
        ImageView image02 = new ImageView(this);
        image02.setLayoutParams(params);
        image02.setScaleType(ImageView.ScaleType.FIT_XY);
        image02.setImageResource(R.mipmap.splashlead_02);
        View splash_lastview = LayoutInflater.from(this).inflate(R.layout.splash_lastitem, null);
        ImageView image03 = (ImageView) splash_lastview.findViewById(R.id.iv_splash);
        image03.setScaleType(ImageView.ScaleType.FIT_XY);
        image03.setImageResource(R.mipmap.splashlead_03);
        Button btn_splash = (Button) splash_lastview.findViewById(R.id.btn_splash);
        btn_splash.setOnClickListener(this);
        List<View> viewList = new ArrayList<View>();
        viewList.add(image01);
        viewList.add(image02);
        viewList.add(splash_lastview);
        vpSplash.setAdapter(new SplashAdapter(viewList));
        vpSplash.setOnPageChangeListener(this);
        //MainFragmentFactory.createFragment(1);

        /**
         * 初始化数据库
         */
        mProController = mController.getProduct(this);//初始化控制器
        mCartController = mController.getIAccount(this);//初始化购物车控制器
        /**判断网络状态*/
        if (NetworkStatusUtil.isNetworkConnected(StartActivity.this)) {
            mProController.getProductCategory(TAG);
            mCartController.getCartList(TAG);
        } else {
            ToastUtil.shortToast(StartActivity.this, "当前网络不可用");
        }

    }


    @Override
    public void onClick(View v) {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    /**
     * viewpager滑动更改指示器的状态
     *
     * @param position
     */
    @Override
    public void onPageSelected(int position) {
        switch (position) {
            case 0:
                indicator01.setImageResource(R.drawable.dot_splash_selected);
                indicator02.setImageResource(R.drawable.dot_splash_normal);
                indicator03.setImageResource(R.drawable.dot_splash_normal);
                break;
            case 1:
                indicator01.setImageResource(R.drawable.dot_splash_normal);
                indicator02.setImageResource(R.drawable.dot_splash_selected);
                indicator03.setImageResource(R.drawable.dot_splash_normal);
                break;
            case 2:
                indicator01.setImageResource(R.drawable.dot_splash_normal);
                indicator02.setImageResource(R.drawable.dot_splash_normal);
                indicator03.setImageResource(R.drawable.dot_splash_selected);
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    @Override
    public void onActionFail(Result result) {
        super.onActionFail(result);
    }

    @Override
    public void onActionSucc(Result result) {
        super.onActionSucc(result);
        if (result != null) {
            String action = result.getAction();
            if (WAction.CATEGORY_ACTIVITY_DATA.equals(action)) {
                List<CategorySpecVo> bean = ((CategoryResult) result).getData();
                if (bean != null) {
                    SqlUtil.clearCategoryData();
                    SqlUtil.initCategoryData(bean);
                    MasApplication.getInstance().setIsInitCategory(true);
                } else {
                    MasApplication.getInstance().setIsInitCategory(false);
                }
            } else if (WAction.GET_SHOPPING_CART_DATA.equals(action)) {
                List<CartBo> cart_list = ((CartListResult) result).getData();
                if (cart_list != null) {
                    //把购物车数据存储到数据库
                    SqlUtil.clearCartData();
                    SqlUtil.initCartData(cart_list);
                    MasApplication.getInstance().setIsInitCart(true);
                } else {
                    MasApplication.getInstance().setIsInitCart(false);
                }
            }
        } else {
            String action = result.getAction();
            if (WAction.CATEGORY_ACTIVITY_DATA.equals(action)) {
                MasApplication.getInstance().setIsInitCategory(false);
            } else if (WAction.GET_SHOPPING_CART_DATA.equals(action)) {
                MasApplication.getInstance().setIsInitCart(false);
            }

        }
    }

}
