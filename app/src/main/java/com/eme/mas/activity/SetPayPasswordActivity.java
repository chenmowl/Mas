package com.eme.mas.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.eme.mas.R;
import com.eme.mas.connection.annotation.WLayout;
import com.eme.mas.controller.BaseImpl;
import com.eme.mas.controller.customeInterface.IUserInfo;
import com.eme.mas.customeview.PasswordInputView;
import com.eme.mas.model.Result;
import com.eme.mas.utils.ToastUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

/**
 * 设置登录密码
 * Created by zulei on 16/7/28.
 */
@WLayout(layoutId = R.layout.activity_set_pay_password)
public class SetPayPasswordActivity extends BaseActivity {

    @Bind(R.id.tv_bar_title)
    TextView tvBarTitle;
    @Bind(R.id.arrow_bar_title)
    ImageView arrowBarTitle;
    @Bind(R.id.search)
    LinearLayout search;
    @Bind(R.id.btn_spp_done)
    Button btnSppDone;
    @Bind(R.id.piv_spp_password_first)
    PasswordInputView pivSppPasswordFirst;
    @Bind(R.id.piv_spp_password_second)
    PasswordInputView pivSppPasswordSecond;
    @Bind(R.id.tv_spp_hint)
    TextView tvSppHint;
    @Bind(R.id.ll_av_loading_transparent_44)
    LinearLayout llAvLoadingTransparent44;

    private IUserInfo iUserInfo;
    private String password, passwordAgain;

    private final static String TAG = "SetPayPasswordActivity";

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
        initData();
    }

    private void initView() {
        arrowBarTitle.setVisibility(View.GONE);
        search.setVisibility(View.GONE);
        btnSppDone.setEnabled(false);
        tvBarTitle.setText(R.string.vbs_title_pay_password);
    }

    private void initData() {
        iUserInfo = mController.getUserInfo(this);

    }

    @OnClick(R.id.back)
    public void back() {
        finish();
    }

    @OnClick(R.id.btn_spp_done)
    public void done() {
        Log.i("info", password + "/" + passwordAgain);
        if (!password.equals(passwordAgain)) {
            resetView();
            ToastUtil.shortToast(this, R.string.toast_twice_login_password_wrong);
        } else {

            //userInfo.setPayPassword(xx,xx);
            // if false resetView();
            //ceshi
            //save to local
            finish();

        }


    }


    @OnTextChanged(R.id.piv_spp_password_first)
    public void etPasswordFirst(CharSequence text) {
        String t = text.toString();
        if (t.length() == 6) {
            password = t;
            pivSppPasswordFirst.setVisibility(View.GONE);
            pivSppPasswordSecond.setVisibility(View.VISIBLE);
            tvSppHint.setText(R.string.slp_hint_input_password_6_again);
            pivSppPasswordSecond.requestFocus();
        }

    }

    @OnTextChanged(R.id.piv_spp_password_second)
    public void etPasswordSecond(CharSequence text) {
        String t = text.toString();
        if (t.length() == 6) {
            passwordAgain = t;
            openBtnDone();
        }

    }


    private void openBtnDone() {
        btnSppDone.setBackgroundResource(R.drawable.btn_status_enable);
        btnSppDone.setEnabled(true);
    }

    private void closeBtnDone() {
        btnSppDone.setBackgroundResource(R.drawable.btn_status_unable);
        btnSppDone.setEnabled(false);
    }

    private void resetView() {
        password = "";
        passwordAgain = "";
        pivSppPasswordFirst.setText("");
        pivSppPasswordSecond.setText("");
        pivSppPasswordFirst.setVisibility(View.VISIBLE);
        pivSppPasswordSecond.setVisibility(View.GONE);
        pivSppPasswordFirst.requestFocus();
        tvSppHint.setText(R.string.slp_hint_input_password_6);
        closeBtnDone();
    }


    @Override
    public void onActionFail(Result result) {
        super.onActionFail(result);
        //ViewUtil.deleteProgressAV();
        if (result == null) {
            //请求出错
        } else {
            //判断code定位错误信息
            String message = result.getMsg();
            ToastUtil.shortToast(this, message);
        }
    }

    @Override
    public void onActionSucc(Result result) {
        super.onActionSucc(result);
        //ViewUtil.deleteProgressAV();
        if (null != result) {
            String action = result.getAction();
//            if (WAction.GET_VERCODE.equals(action)) {
//                String hintMessage = result.getMessage();
//                String status = result.getResult();
//            }

            //下一步
        }

    }


    /**
     * 验证密码格式
     */
    public static boolean isCurrentPassword(String nickName) {
        String strPattern = "^[^\\u4e00-\\u9fa5]{4,20}$";
        Pattern p = Pattern.compile(strPattern);
        Matcher m = p.matcher(nickName);
        return m.matches();
    }

}
