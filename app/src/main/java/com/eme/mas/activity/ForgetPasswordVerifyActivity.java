package com.eme.mas.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.eme.mas.MasApplication;
import com.eme.mas.R;
import com.eme.mas.connection.WAction;
import com.eme.mas.connection.annotation.WLayout;
import com.eme.mas.controller.BaseImpl;
import com.eme.mas.controller.customeInterface.IUserInfo;
import com.eme.mas.environment.WValue;
import com.eme.mas.model.Result;
import com.eme.mas.utils.NetworkStatusUtil;
import com.eme.mas.utils.PhoneUtil;
import com.eme.mas.utils.ToastUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 忘记密码
 * Created by zhaoxin on 16/7/30.
 */
@WLayout(layoutId = R.layout.activity_verify_forget_password)
public class ForgetPasswordVerifyActivity extends BaseActivity {

    @Bind(R.id.back)
    LinearLayout back;
    @Bind(R.id.bar_title)
    TextView barTitle;
    @Bind(R.id.et_login_phoneNumer)
    EditText etLoginPhoneNumer;
    @Bind(R.id.image_phoneNumber_clear)
    ImageView imagePhoneNumberClear;
    @Bind(R.id.et_login_verfiycode)
    EditText etLoginVerfiycode;
    @Bind(R.id.btn_securitycode)
    Button btnSecuritycode;
    @Bind(R.id.ll_av_loading_transparent_44)
    LinearLayout llAvLoadingTransparent44;
    @Bind(R.id.btn_next_done)
    Button btnNextDone;

    private boolean isPhone;
    private String phoneNumber, verCode;
    private IUserInfo iUserInfo;

    private final static String TAG = "ForgetPasswordVerifyActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @Override
    public void afterContentView() {
        super.afterContentView();
        iUserInfo = mController.getUserInfo(this);
        barTitle.setText(R.string.forget_password);
        etLoginPhoneNumer.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {
                phoneNumber = etLoginPhoneNumer.getText().toString().trim();
                if (phoneNumber.length() < 11) {
                    btnSecuritycode.setEnabled(false);
                }
                if (!phoneNumber.isEmpty()) {
                    imagePhoneNumberClear.setVisibility(View.VISIBLE);
                    imagePhoneNumberClear.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            etLoginPhoneNumer.setText("");
                        }
                    });
                } else {
                    imagePhoneNumberClear.setVisibility(View.GONE);
                    btnSecuritycode.setEnabled(false);
                }
                if (phoneNumber.length() != 11 & phoneNumber.length() > 10) {
                    ToastUtil.shortToast(ForgetPasswordVerifyActivity.this, "请输入正确的手机号");
                    btnSecuritycode.setBackgroundColor(getResources().getColor(R.color.login_btn_gray));
                    btnSecuritycode.setEnabled(true);

                } else {
                    btnSecuritycode.setBackgroundColor(getResources().getColor(R.color.login_btn_gray));
                    btnSecuritycode.setEnabled(false);
                }
                if (phoneNumber.length() == 11) {
                    btnSecuritycode.setBackgroundColor(getResources().getColor(R.color.main_color_red));
                    btnSecuritycode.setEnabled(true);
                }
            }
        });

        etLoginVerfiycode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                verCode = etLoginVerfiycode.getText().toString().trim();
                if (!TextUtils.isEmpty(verCode)) {
                    btnNextDone.setEnabled(true);
                    btnNextDone.setSelected(true);
                } else {
                    btnNextDone.setEnabled(false);
                    btnNextDone.setSelected(false);
                }

            }
        });
    }

    @OnClick(R.id.btn_securitycode)
    void sendSecurityCode() {
        isPhone = PhoneUtil.isMobileNumber(phoneNumber);
        if (!isPhone) {
            ToastUtil.shortToast(ForgetPasswordVerifyActivity.this, R.string.wrong_phonenumber);
            dTimer.cancel();
        } else {
            dTimer.start();
            btnSecuritycode.setBackgroundColor(getResources().getColor(R.color.login_btn_gray));
            btnSecuritycode.setEnabled(false);
            if (NetworkStatusUtil.isNetworkConnected(this)) {
                isHideLayer(llAvLoadingTransparent44, false);
                iUserInfo.getVerCode(phoneNumber, WValue.SCENE_FORGETPWD, TAG);
            } else {
                ToastUtil.shortToast(MasApplication.getInstance(), R.string.net_error);
            }

        }
    }

    private CountDownTimer dTimer = new CountDownTimer(60000, 1000) {

        @Override
        public void onTick(long millisUntilFinished) {
            btnSecuritycode.setText(getText(R.string.sended) + "(" + (millisUntilFinished / 1000) + "s)");
            btnSecuritycode.setSelected(false);
        }

        @Override
        public void onFinish() {
//            ifSend = false;
            btnSecuritycode.setEnabled(true);
            btnSecuritycode.setText(R.string.get_vercode);
            btnSecuritycode.setSelected(true);
            btnSecuritycode.setEnabled(false);
        }
    };

    @OnClick(R.id.btn_next_done)
    void setBtnLoginDone() {
        if (NetworkStatusUtil.isNetworkConnected(this)) {
            isPhone = PhoneUtil.isMobileNumber(phoneNumber);
            if (!isPhone) {
                ToastUtil.shortToast(ForgetPasswordVerifyActivity.this, R.string.wrong_phonenumber);
            }else if(TextUtils.isEmpty(verCode)){
                ToastUtil.shortToast(ForgetPasswordVerifyActivity.this, R.string.login_edit_hint_verfiycode);
            }else{
                isHideLayer(llAvLoadingTransparent44, false);
                //TODO 这里验证验证码,成功后跳转页面,进入修改登录密码界面
                iUserInfo.checkVerCode(phoneNumber, verCode,WValue.SCENE_FORGETPWD, TAG);
            }
        } else {
            ToastUtil.shortToast(MasApplication.getInstance(), R.string.net_error);
        }
    }

    @OnClick(R.id.back)
    void back() {
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
//        MobclickAgent.onPause(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
//        MobclickAgent.onResume(this);
    }

    @Override
    public void onActionFail(Result result) {
        super.onActionFail(result);
        isHideLayer(llAvLoadingTransparent44, true);
        switch(result.getAction()){
            case WAction.GET_VERCODE:
            btnSecuritycode.setEnabled(true);
            btnSecuritycode.setText(R.string.login_btn_verfiycode);
            btnSecuritycode.setBackgroundColor(ContextCompat.getColor(ForgetPasswordVerifyActivity.this, R.drawable.btn_login_out));
                break;
            case WAction.CHECK_VERCODE:
                break;
        }
        String message = result.getMsg();
        ToastUtil.shortToast(this, message);
    }

    @Override
    public void onActionSucc(Result result) {
        super.onActionSucc(result);
        isHideLayer(llAvLoadingTransparent44, true);
        switch(result.getAction()){
            case WAction.GET_VERCODE:
                ToastUtil.shortToast(this, R.string.toast_get_vercode_ok);
                break;
            case WAction.CHECK_VERCODE:
                Intent intent = new Intent(ForgetPasswordVerifyActivity.this, SetLoginPasswordActivity.class);
                intent.putExtra("phone_number", phoneNumber);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != dTimer) {
            dTimer.cancel();
        }
        ((BaseImpl) iUserInfo).cancelRequestByTag(TAG);
    }
}