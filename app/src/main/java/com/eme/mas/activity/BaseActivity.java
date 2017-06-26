package com.eme.mas.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.android.volley.VolleyError;
import com.eme.mas.connection.ActionListener;
import com.eme.mas.connection.IActionListener;
import com.eme.mas.connection.annotation.WLayout;
import com.eme.mas.controller.Controller;
import com.eme.mas.data.sp.SPBase;
import com.eme.mas.data.sp.SpConstant;
import com.eme.mas.environment.WValue;
import com.eme.mas.exception.NoConfigLayoutIdException;
import com.eme.mas.model.Result;
import com.eme.mas.utils.ViewUtil;

import butterknife.ButterKnife;

/**
 * UI基类
 */
public class BaseActivity extends FragmentActivity implements ActionListener<Result>, IActionListener {

    public Controller mController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        beforeContentView();

        int layoutId = getLayoutId();

        if (layoutId == WValue.NO_LAYOUT_ID) {
            new NoConfigLayoutIdException();
        }
        if (layoutId != -2) {
            setContentView(getLayoutId());
            ViewUtil.setStatusBarColor(this);
        }
        afterContentView();
    }

    public void beforeContentView() {

//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        mController = new Controller();
    }

    public void afterContentView() {
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.bind(this);
    }

    public int getLayoutId() {

        return getClass().getAnnotation(WLayout.class).layoutId();
    }

    @Override
    protected void onResume() {
        super.onResume();


    }

    @Override
    protected void onPause() {
        super.onPause();


    }

    @Override
    public void onErrorResponse(VolleyError error) {

    }

    @Override
    public void onResponse(Result response) {

    }

    @Override
    public void onActionSucc(Result result) {

    }

    @Override
    public void onActionFail(Result result) {

    }

    /**
     * 判断是否登录了
     *
     * @return true：用户已登录 false：用户未登录
     */
    public boolean isLogin() {
        return SPBase.getBoolean(this, SpConstant.LOGIN_FILE_NAME,SpConstant.LOGIN_KEY);
    }

    /**
     * 跳转到登录页面
     */
    public void login() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    /**
     * 如果用户没有登录显示用户登录页面
     */
    public boolean verifyLogin() {
        if(!isLogin()){
            login();
            return  false;
        }else{
            return  true;
        }
    }

    /**
     * 控制图层显示隐藏
     *
     * @param isHide
     */
    public void isHideLayer(View v, boolean isHide) {
        int visiable = v.getVisibility();
        if (isHide) {
            if (visiable == View.VISIBLE) {
                v.setVisibility(View.GONE);
            }
        } else {
            if (visiable == View.GONE || visiable == View.INVISIBLE) {
                v.setVisibility(View.VISIBLE);
            }
        }
    }



}
