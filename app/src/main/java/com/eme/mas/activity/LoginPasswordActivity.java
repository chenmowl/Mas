package com.eme.mas.activity;

import android.content.Intent;
import android.os.Bundle;
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
import com.eme.mas.data.sp.SPBase;
import com.eme.mas.data.sp.SpConstant;
import com.eme.mas.environment.WValue;
import com.eme.mas.eventbus.FinishPageEvent;
import com.eme.mas.model.Result;
import com.eme.mas.model.UserInfoResult;
import com.eme.mas.model.entity.UserInfo;
import com.eme.mas.model.entity.UserInfoBo;
import com.eme.mas.utils.KeyboardUtils;
import com.eme.mas.utils.NetworkStatusUtil;
import com.eme.mas.utils.PhoneUtil;
import com.eme.mas.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 密码登录
 * Created by zhaoxin on 16/7/27.
 */
@WLayout(layoutId = R.layout.activity_password_login)
public class LoginPasswordActivity extends BaseActivity {

    private final static String TAG = LoginPasswordActivity.class.getSimpleName();

    @Bind(R.id.back)
    LinearLayout back;
    @Bind(R.id.bar_title)
    TextView barTitle;
    @Bind(R.id.et_login_password_phone)
    EditText etLoginPasswordPhone;
    @Bind(R.id.image_password_phone_clear)
    ImageView imagePasswordPhoneClear;
    @Bind(R.id.et_login_password)
    EditText etLoginPassword;
    @Bind(R.id.image_password_clear)
    ImageView imagePasswordClear;
    @Bind(R.id.btn_password_login_done)
    Button btnPasswordLoginDone;
    @Bind(R.id.tv_password_forget)
    TextView tvPasswordForget;
    @Bind(R.id.ll_av_loading_transparent_44)
    LinearLayout llAvLoadingTransparent44;

    private String password;
    private String phoneNumber;
    private boolean isPhone;
    private IUserInfo iUserInfo;

    private static final int FORGETLOGINPASSWORD = 2478;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }

    @Override
    public void afterContentView() {
        super.afterContentView();
        initView();
        initData();

        btnPasswordLoginDone.setEnabled(false);
        etLoginPasswordPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                phoneNumber = etLoginPasswordPhone.getText().toString().trim();
                if (!phoneNumber.isEmpty()) {
                    imagePasswordPhoneClear.setVisibility(View.VISIBLE);
                    imagePasswordPhoneClear.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            etLoginPasswordPhone.setText("");
                        }
                    });
                } else {
                    imagePasswordPhoneClear.setVisibility(View.GONE);
                }
                if (phoneNumber.length() > 11) {
                    ToastUtil.shortToast(LoginPasswordActivity.this, R.string.wrong_phonenumber);
                }
            }
        });

        etLoginPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                isPhone = PhoneUtil.isMobileNumber(phoneNumber);
                if (!isPhone) {
                    ToastUtil.shortToast(LoginPasswordActivity.this, R.string.wrong_phonenumber);
                    etLoginPassword.setEnabled(false);
                } else {
                    etLoginPassword.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                password = etLoginPassword.getText().toString().trim();
                if (!TextUtils.isEmpty(password)) {
                    btnPasswordLoginDone.setSelected(true);
                    btnPasswordLoginDone.setEnabled(true);
                    imagePasswordClear.setVisibility(View.VISIBLE);
                    imagePasswordClear.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            etLoginPassword.setText("");
                        }
                    });
                } else {
                    imagePasswordClear.setVisibility(View.GONE);
                    btnPasswordLoginDone.setSelected(false);
                    btnPasswordLoginDone.setEnabled(false);
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        ((BaseImpl) iUserInfo).cancelRequestByTag(TAG);
    }

    private void initView() {
        barTitle.setText(R.string.password_login);
    }

    private void initData() {
        iUserInfo = mController.getUserInfo(this);
        phoneNumber = UserInfo.getInstance().getPhone();
    }


    @OnClick(R.id.tv_password_forget)
    void setPasswordForget() {
        Intent intent = new Intent(this, ForgetPasswordVerifyActivity.class);
        startActivityForResult(intent, FORGETLOGINPASSWORD);
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

    @OnClick(R.id.btn_password_login_done)
    public void onClick() {
        if (!isPhone) {
            ToastUtil.shortToast(LoginPasswordActivity.this, R.string.wrong_phonenumber);
        } else {
            KeyboardUtils.closeInputMethod(this);
            if (NetworkStatusUtil.isNetworkConnected(MasApplication.getInstance())) {
                isHideLayer(llAvLoadingTransparent44,false);
                iUserInfo.passwordLogin(etLoginPasswordPhone.getText().toString(), etLoginPassword.getText().toString(), TAG);
            } else {
                ToastUtil.shortToast(MasApplication.getInstance(), R.string.net_error);
            }
        }
    }

    @Override
    public void onActionFail(Result result) {
        super.onActionFail(result);
        isHideLayer(llAvLoadingTransparent44,true);
        String message = result.getMsg();
        switch (result.getAction()){
            case WAction.PASSWORD_LOGIN:
                if(TextUtils.isEmpty(message)){
                    message=getText(R.string.toast_login_fail).toString();
                    SPBase.setSPBoolean(this, SpConstant.LOGIN_FILE_NAME, SpConstant.LOGIN_KEY, false);
                }
                break;
        }
        ToastUtil.shortToast(this,message);
    }

    @Override
    public void onActionSucc(Result result) {
        super.onActionSucc(result);
        isHideLayer(llAvLoadingTransparent44,true);
        switch (result.getAction()){
            case WAction.PASSWORD_LOGIN:
                UserInfoResult userInfoResult = (UserInfoResult) result;
                UserInfoBo userInfoBo = userInfoResult.getData();

                String birthday = userInfoBo.getBirthday() ;
                String sex = userInfoBo.getSex();
                String nickName = userInfoBo.getNickname();
                String userID = userInfoBo.getUser_id();
                String imageUrl = userInfoBo.getImageurl();
                String mobile_number=userInfoBo.getMobile();

                UserInfo.getInstance().setBirthday(TextUtils.isEmpty(birthday)? WValue.STRING_EMPTY:birthday);
                UserInfo.getInstance().setGender(TextUtils.isEmpty(sex)? WValue.STRING_EMPTY:sex);
                UserInfo.getInstance().setNickname(TextUtils.isEmpty(nickName)? WValue.STRING_EMPTY:nickName);
                UserInfo.getInstance().setImageUrl(TextUtils.isEmpty(imageUrl)? WValue.STRING_EMPTY:imageUrl);
                UserInfo.getInstance().setUseId(TextUtils.isEmpty(userID)? WValue.STRING_EMPTY:userID);
                UserInfo.getInstance().setPhone(TextUtils.isEmpty(mobile_number)? WValue.STRING_EMPTY:mobile_number);
                UserInfo.getInstance().Save();

                SPBase.setSPBoolean(this, SpConstant.LOGIN_FILE_NAME, SpConstant.LOGIN_KEY, true);

                finish();
                EventBus.getDefault().post(new FinishPageEvent());
                ToastUtil.shortToast(this, R.string.toast_login_success);
                break;
        }
    }
}