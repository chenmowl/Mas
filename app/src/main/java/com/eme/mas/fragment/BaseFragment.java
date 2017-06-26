package com.eme.mas.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.VolleyError;
import com.eme.mas.activity.LoginActivity;
import com.eme.mas.connection.ActionListener;
import com.eme.mas.connection.IActionListener;
import com.eme.mas.connection.annotation.WLayout;
import com.eme.mas.controller.Controller;
import com.eme.mas.data.sp.SPBase;
import com.eme.mas.data.sp.SpConstant;
import com.eme.mas.environment.WValue;
import com.eme.mas.exception.NoConfigLayoutIdException;
import com.eme.mas.model.Result;

import butterknife.ButterKnife;

/**
 * 碎片基类
 */
public class BaseFragment extends Fragment implements ActionListener<Result>, IActionListener {

    public Controller mController;
    protected View rootView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        int layoutId = getLayoutId();

        if (layoutId == WValue.NO_LAYOUT_ID) {

            new NoConfigLayoutIdException();
        }

        if (null == rootView) {
//            Fresco.initialize(mContext);
            rootView = inflater.inflate(layoutId, container, false);
        }
        ButterKnife.bind(this, rootView);
        mController = new Controller();
        return rootView;
    }

    @Override
    public void onResume() {

        super.onResume();
    }

    @Override
    public void onPause() {

        super.onPause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (null != rootView && null != rootView.getParent()) {
            ((ViewGroup) rootView.getParent()).removeView(rootView);
        }
        ButterKnife.unbind(this);
    }

    public int getLayoutId() {

        return getClass().getAnnotation(WLayout.class).layoutId();
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

        if (null == getActivity()) {
            return;
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

    /**
     * 判断是否登录了
     *
     * @return true：用户已登录 false：用户未登录
     */
    public boolean isLogin() {
        return SPBase.getBoolean(getActivity(), SpConstant.LOGIN_FILE_NAME,SpConstant.LOGIN_KEY);
    }

    /**
     * 跳转到登录页面
     */
    public void login() {
        Intent intent = new Intent(getActivity(), LoginActivity.class);
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


}
