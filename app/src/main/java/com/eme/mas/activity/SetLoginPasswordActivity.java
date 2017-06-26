package com.eme.mas.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.eme.mas.R;
import com.eme.mas.connection.WAction;
import com.eme.mas.connection.annotation.WLayout;
import com.eme.mas.controller.BaseImpl;
import com.eme.mas.controller.customeInterface.IUserInfo;
import com.eme.mas.model.Result;
import com.eme.mas.utils.NetworkStatusUtil;
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
@WLayout(layoutId = R.layout.activity_set_login_password_set)
public class SetLoginPasswordActivity extends BaseActivity {

    private final static String TAG = SetLoginPasswordActivity.class.getSimpleName();

    @Bind(R.id.et_slp_password)
    EditText etSlpPassword;
    @Bind(R.id.et_slp_password_again)
    EditText etSlpPasswordAgain;
    @Bind(R.id.tv_bar_title)
    TextView tvBarTitle;
    @Bind(R.id.arrow_bar_title)
    ImageView arrowBarTitle;
    @Bind(R.id.search)
    LinearLayout search;
    @Bind(R.id.btn_slp_done)
    Button btnSlpDone;
    @Bind(R.id.ib_slp_password_delete)
    ImageButton ibSlpPasswordDelete;
    @Bind(R.id.ib_slp_password_again_delete)
    ImageButton ibSlpPasswordAgainDelete;
    @Bind(R.id.ll_av_loading_transparent_44)
    LinearLayout llAvLoadingTransparent44;

    private IUserInfo iUserInfo;
    private String password, passwordAgain;
    private String phoneNumber;

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
        btnSlpDone.setEnabled(false);
        tvBarTitle.setText(R.string.reset_password);
    }

    private void initData() {
        iUserInfo = mController.getUserInfo(this);
        phoneNumber = getIntent().getStringExtra("phone_number");
    }

    @OnClick(R.id.back)
    public void back() {
        finish();
    }

    @OnClick(R.id.btn_slp_done)
    public void done() {
        if (!isCurrentPassword(password) || !isCurrentPassword(passwordAgain)) {
            ToastUtil.shortToast(this, R.string.toast_password_format_wrong);
        } else if (!password.equals(passwordAgain)) {
            ToastUtil.shortToast(this, R.string.toast_twice_login_password_wrong);
        } else {
            if (NetworkStatusUtil.isNetworkConnected(SetLoginPasswordActivity.this)) {
                isHideLayer(llAvLoadingTransparent44,false);
                iUserInfo.setLoginPassword(password, phoneNumber, TAG);
            } else {
                ToastUtil.shortToast(SetLoginPasswordActivity.this, R.string.net_error);
            }
        }
    }

    @OnClick({R.id.ib_slp_password_delete, R.id.ib_slp_password_again_delete})
    public void etDelete(View view) {
        switch (view.getId()) {
            case R.id.ib_slp_password_delete:
                etSlpPassword.setText("");
                password = "";
                updateView();
                break;
            case R.id.ib_slp_password_again_delete:
                etSlpPasswordAgain.setText("");
                passwordAgain = "";
                updateView();
                break;
        }
    }


    @OnTextChanged(R.id.et_slp_password)
    public void etPassword(CharSequence text) {
        password = text.toString();
        updateView();
    }

    @OnTextChanged(R.id.et_slp_password_again)
    public void etPasswordAgain(CharSequence text) {
        passwordAgain = text.toString();
        updateView();
    }

    private void updateView() {
        if (!TextUtils.isEmpty(password) && !TextUtils.isEmpty(passwordAgain)) {
            openBtnDone();
        } else {
            closeBtnDone();
        }
        if (TextUtils.isEmpty(password)) {
            ibSlpPasswordDelete.setVisibility(View.GONE);
        } else {
            ibSlpPasswordDelete.setVisibility(View.VISIBLE);
        }
        if (TextUtils.isEmpty(passwordAgain)) {
            ibSlpPasswordAgainDelete.setVisibility(View.GONE);
        } else {
            ibSlpPasswordAgainDelete.setVisibility(View.VISIBLE);
        }
    }

    private void openBtnDone() {
        btnSlpDone.setBackgroundResource(R.drawable.btn_status_enable);
        btnSlpDone.setEnabled(true);
    }

    private void closeBtnDone() {
        btnSlpDone.setBackgroundResource(R.drawable.btn_status_unable);
        btnSlpDone.setEnabled(false);
    }


    @Override
    public void onActionFail(Result result) {
        super.onActionFail(result);
        isHideLayer(llAvLoadingTransparent44,true);
        String message = result.getMsg();
        ToastUtil.shortToast(this, message);
    }

    @Override
    public void onActionSucc(Result result) {
        super.onActionSucc(result);
        isHideLayer(llAvLoadingTransparent44,true);
        String action = result.getAction();
        if (WAction.SET_LOGIN_PASSWORD.equals(action)) {
            ToastUtil.shortToast(this, R.string.toast_set_login_password_ok);
            Intent intent=new Intent(this,LoginPasswordActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
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
