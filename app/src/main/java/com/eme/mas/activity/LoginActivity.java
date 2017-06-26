package com.eme.mas.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
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
import com.eme.mas.customeview.dialog.ChangeHostDialog;
import com.eme.mas.customeview.dialog.ChangeImageHostDialog;
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
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 登录
 * Created by zhaoxin on 16/7/25.
 */
@WLayout(layoutId = R.layout.activity_login)
public class LoginActivity extends BaseActivity {

    @Bind(R.id.back)
    LinearLayout back;
    @Bind(R.id.bar_title)
    TextView barTitle;
    @Bind(R.id.ll_password_login)
    LinearLayout llPasswordLogin;
    @Bind(R.id.et_login_phonenumber)
    EditText etLoginPhonenumber;
    @Bind(R.id.image_phone_clear)
    ImageView imagePhoneClear;
    @Bind(R.id.et_login_verfiycode)
    EditText etLoginVerfiycode;
    @Bind(R.id.btn_getvercode)
    Button btnGetvercode;
    @Bind(R.id.tv_agreement)
    TextView tvAgreement;
    @Bind(R.id.btn_login_done)
    Button btnLoginDone;
    @Bind(R.id.ll_av_loading_transparent_44)
    LinearLayout llAvLoadingTransparent44;

    private IUserInfo iUserInfo;
    private boolean isPhone;
    private String phoneNumber, verCode;
    private static final int LOGINPASSWORD = 2356;
    private static final int AGREEMENT = 2678;
    private final static String TAG = "LoginActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
    }

    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if (null != dTimer) {
            dTimer.cancel();
        }
        ButterKnife.unbind(this);
        ((BaseImpl) iUserInfo).cancelRequestByTag(TAG);

    }

    @Override
    public void afterContentView() {
        super.afterContentView();
        iUserInfo = mController.getUserInfo(this);

        etLoginPhonenumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {
                phoneNumber = etLoginPhonenumber.getText().toString().trim();
                if (phoneNumber.length() < 11) {
                    btnGetvercode.setEnabled(false);
                }
                if (!phoneNumber.isEmpty()) {
                    imagePhoneClear.setVisibility(View.VISIBLE);
                    imagePhoneClear.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            etLoginPhonenumber.setText("");
                        }
                    });
                } else {
                    imagePhoneClear.setVisibility(View.GONE);
                    btnGetvercode.setEnabled(false);
                }
                if (phoneNumber.length() != 11 & phoneNumber.length() > 10) {
                    ToastUtil.shortToast(LoginActivity.this, "请输入正确的手机号");
                    btnGetvercode.setBackgroundColor(getResources().getColor(R.color.login_btn_gray));
                    btnGetvercode.setEnabled(true);

                } else {
                    btnGetvercode.setBackgroundColor(getResources().getColor(R.color.login_btn_gray));
                    btnGetvercode.setEnabled(false);
                }
                if (phoneNumber.length() == 11) {
                    btnGetvercode.setBackgroundColor(getResources().getColor(R.color.main_color_red));
                    btnGetvercode.setEnabled(true);
                }
            }
        });
        String changeText = "温馨提示：未注册码尚到家账号的手机，登录时将自动注册，且代表您已同意《用户服务协议》";
        SpannableString spannableString = new SpannableString(changeText);
        spannableString.setSpan(new ClickableSpan() {
            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(getResources().getColor(R.color.login_agreement_blue));       //设置文件颜色
                ds.setUnderlineText(false);      //设置下划线
            }

            @Override
            public void onClick(View exchange) {
//                try{
//                    Thread.sleep(100);
//                }catch (InterruptedException e){
//                    e.printStackTrace();
//                }
                Intent intent = new Intent(LoginActivity.this, AgreementActiviy.class);
                startActivityForResult(intent, AGREEMENT);
//                startActivity(intentthis);
//                ToastUtil.shortToast(LoginActivity.this,"111111");
            }
        }, changeText.length() - 8, changeText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvAgreement.setText(spannableString);
        tvAgreement.setMovementMethod(LinkMovementMethod.getInstance());


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
                    btnLoginDone.setEnabled(true);
                    btnLoginDone.setSelected(true);
                } else {
                    btnLoginDone.setEnabled(false);
                    btnLoginDone.setSelected(false);
                }

            }
        });
    }

    @OnClick(R.id.btn_getvercode)
    void sendSecurityCode() {
        isPhone = PhoneUtil.isMobileNumber(phoneNumber);
        if (!isPhone) {
            ToastUtil.shortToast(LoginActivity.this, R.string.wrong_phonenumber);
            dTimer.cancel();
        } else {
            dTimer.start();
            btnGetvercode.setBackgroundColor(getResources().getColor(R.color.login_btn_gray));
            btnGetvercode.setEnabled(false);
            if (NetworkStatusUtil.isNetworkConnected(this)) {
                isHideLayer(llAvLoadingTransparent44,false);
                iUserInfo.getVerCode(etLoginPhonenumber.getText().toString(), WValue.SCENE_LOGIN, TAG);
            } else {
                ToastUtil.shortToast(MasApplication.getInstance(), R.string.net_error);
            }

        }
    }

    private CountDownTimer dTimer = new CountDownTimer(60000, 1000) {

        @Override
        public void onTick(long millisUntilFinished) {
            btnGetvercode.setText(getText(R.string.sended) + "(" + (millisUntilFinished / 1000) + "s)");
            btnGetvercode.setSelected(false);
        }

        @Override
        public void onFinish() {
            btnGetvercode.setEnabled(true);
            btnGetvercode.setText(R.string.get_vercode);
            btnGetvercode.setSelected(true);
            btnGetvercode.setEnabled(false);
        }
    };

    @OnClick(R.id.ll_password_login)
    void setLoginpassword() {
        Intent intent = new Intent(this, LoginPasswordActivity.class);
        startActivityForResult(intent, LOGINPASSWORD);
    }

    @OnClick({R.id.ll_change_host,R.id.ll_change_image_host})
    void changeHost(View v) {
        switch (v.getId()){
            case R.id.ll_change_host:
                ChangeHostDialog sbDialog = new ChangeHostDialog();
                sbDialog.showDialog(this, null);
                break;
            case R.id.ll_change_image_host:
                ChangeImageHostDialog sbImageDialog = new ChangeImageHostDialog();
                sbImageDialog.showDialog(this, null);
                break;

        }

    }

    @OnClick(R.id.btn_login_done)
    void setBtnLoginDone() {
        KeyboardUtils.closeInputMethod(this);
        if (NetworkStatusUtil.isNetworkConnected(this)) {
            isHideLayer(llAvLoadingTransparent44,false);
            iUserInfo.vercodeLogin(etLoginPhonenumber.getText().toString(), etLoginVerfiycode.getText().toString(), TAG);
        } else {
            ToastUtil.shortToast(MasApplication.getInstance(), R.string.net_error);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

//if (LOGINPASSWORD==requestCode && resultCode==){
//    data.get
//}
    }

    @OnClick(R.id.back)
    void back() {
        finish();
    }

//    @OnClick(R.id.loginCommit)
//    void setLoginCommit(){
//        finish();
//    }

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
        isHideLayer(llAvLoadingTransparent44,true);
        String message = result.getMsg();
        switch (result.getAction()){
            case WAction.GET_VERCODE:
                btnGetvercode.setEnabled(true);
                btnGetvercode.setText(R.string.login_btn_verfiycode);
//                btnGetvercode.setBackgroundColor(ContextCompat.getColor(LoginActivity.this, R.drawable.btn_login_out));
                if(TextUtils.isEmpty(message)){
                    message=getText(R.string.toast_get_vercode_failue).toString();
                }
                break;
            case WAction.VERIFY_LOGIN:
                SPBase.setSPBoolean(this, SpConstant.LOGIN_FILE_NAME, SpConstant.LOGIN_KEY, false);
                if(TextUtils.isEmpty(message)){
                    message=getText(R.string.toast_login_fail).toString();
                }
                break;
        }
        ToastUtil.shortToast(this, message);
    }

    @Override
    public void onActionSucc(Result result) {
        super.onActionSucc(result);
        isHideLayer(llAvLoadingTransparent44,true);
        switch (result.getAction()){
            case WAction.GET_VERCODE:
                ToastUtil.shortToast(this, R.string.toast_get_vercode_ok);
                break;
            case WAction.VERIFY_LOGIN:
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

                setResult(RESULT_OK, null);
                finish();
                ToastUtil.shortToast(this, R.string.toast_login_success);
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void finishLogin(FinishPageEvent event) {
        setResult(RESULT_OK, null);
        finish();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}


