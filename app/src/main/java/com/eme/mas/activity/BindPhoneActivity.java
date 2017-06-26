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
import com.eme.mas.model.entity.UserInfo;
import com.eme.mas.utils.NetworkStatusUtil;
import com.eme.mas.utils.ToastUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

/**
 * 绑定（修改）手机页面
 * Created by zulei on 16/7/27.
 */
@WLayout(layoutId = R.layout.activity_bindphone)
public class BindPhoneActivity extends BaseActivity {

    private final static String TAG = BindPhoneActivity.class.getSimpleName();

    @Bind(R.id.tv_bar_title)
    TextView tvBarTitle;
    @Bind(R.id.arrow_bar_title)
    ImageView arrowBarTitle;
    @Bind(R.id.search)
    LinearLayout search;
    @Bind(R.id.et_bindphone_newnumber)
    EditText etBindphoneNewnumber;
    @Bind(R.id.et_bindphone_vercode)
    EditText etBindphoneVercode;
    @Bind(R.id.btn_bindphone_bind)
    Button btnBindphoneBind;
    @Bind(R.id.btn_bindphone_getvercode)
    Button btnBindphoneGetvercode;
    @Bind(R.id.ll_av_loading_transparent_44)
    LinearLayout llAvLoadingTransparent44;

    private IUserInfo iUserInfo;
    private int flag;
    private String phoneNumber,newPhoneNumber, verCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != dTimer) {
            dTimer.cancel();
        }
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
        btnBindphoneGetvercode.setEnabled(false);
        btnBindphoneBind.setEnabled(false);
    }

    private void initData() {
        iUserInfo = mController.getUserInfo(this);
        flag = getIntent().getIntExtra("flag", 0);
        switch (flag){
            case WValue.VERIFY_MOBILE:
                tvBarTitle.setText(R.string.verify_phone_title);
                btnBindphoneBind.setText(R.string.bindphone_btn_ok_v);
                phoneNumber = UserInfo.getInstance().getPhone();
                etBindphoneNewnumber.setText(getText(R.string.bindphone_bindedphone) + ":" + phoneNumber.substring(0, 3) + "****" + phoneNumber.substring(phoneNumber.length() - 4, phoneNumber.length()));
                etBindphoneNewnumber.setEnabled(false);
                btnBindphoneGetvercode.setEnabled(true);
                btnBindphoneGetvercode.setBackgroundColor(ContextCompat.getColor(this, R.color.main_color_red));
                break;
            case WValue.MODIFY_MOBILE:
                phoneNumber=getIntent().getStringExtra("phoneNumber");
                tvBarTitle.setText(R.string.bindphone_title);
                btnBindphoneBind.setText(R.string.bindphone_btn_ok);
                etBindphoneNewnumber.addTextChangedListener(new BindPhoneTextWatchListener());
                break;
        }

    }

    /**
     * 绑定手机的文本变化监听
     */
    private class BindPhoneTextWatchListener implements TextWatcher{

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            switch (flag){
                case WValue.MODIFY_MOBILE:
                    newPhoneNumber = s.toString().trim();
                    if (!TextUtils.isEmpty(newPhoneNumber)) {
                        btnBindphoneGetvercode.setEnabled(true);
                        btnBindphoneGetvercode.setBackgroundColor(ContextCompat.getColor(BindPhoneActivity.this, R.color.main_color_red));
                    } else {
                        btnBindphoneGetvercode.setEnabled(false);
                        btnBindphoneGetvercode.setBackgroundColor(ContextCompat.getColor(BindPhoneActivity.this, R.color.status_btn_gray));
                    }
                    break;
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

    @OnClick(R.id.back)
    public void onClick() {
        finish();
    }


    @OnClick(R.id.btn_bindphone_bind)
    public void bindPhone() {
        if (NetworkStatusUtil.isNetworkConnected(this)) {
            switch(flag){
                case WValue.MODIFY_MOBILE:
                    isHideLayer(llAvLoadingTransparent44,false);
                    iUserInfo.bindPhone(phoneNumber,newPhoneNumber,verCode,TAG);
                    break;
                case WValue.VERIFY_MOBILE:
                    isHideLayer(llAvLoadingTransparent44,false);
                    iUserInfo.checkVerCode(phoneNumber,verCode,WValue.SCENE_UPDATEMOBILE,TAG);
                    break;
            }
        } else {
            ToastUtil.shortToast(MasApplication.getInstance(), R.string.net_error);
        }
    }

    @OnTextChanged(R.id.et_bindphone_vercode)
    public void etVercodeChanged(CharSequence text) {
        verCode = text.toString().trim();
        if (!TextUtils.isEmpty(verCode)&&!TextUtils.isEmpty(phoneNumber)) {
            btnBindphoneBind.setEnabled(true);
            btnBindphoneBind.setBackgroundColor(ContextCompat.getColor(this, R.color.main_color_red));
        } else {
            btnBindphoneBind.setEnabled(false);
            btnBindphoneBind.setBackgroundColor(ContextCompat.getColor(this, R.color.status_btn_gray));
        }
    }

    @OnClick(R.id.btn_bindphone_getvercode)
    public void getVerCode() {
        if (NetworkStatusUtil.isNetworkConnected(this)) {
            switch(flag){
                case WValue.MODIFY_MOBILE:
                    if (!isMobile(phoneNumber)) {
                        ToastUtil.shortToast(this, R.string.toast_phone_format_wrong);
                        return;
                    } else {
                        dTimer.start();
                        btnBindphoneGetvercode.setEnabled(false);
                        etBindphoneNewnumber.setEnabled(false);
                        btnBindphoneGetvercode.setBackgroundColor(ContextCompat.getColor(BindPhoneActivity.this, R.color.status_btn_gray));
                        iUserInfo.getVerCode(phoneNumber, WValue.SCENE_BINDNEWMOBILE, TAG);
                        isHideLayer(llAvLoadingTransparent44,false);
                    }
                    break;
                case WValue.VERIFY_MOBILE:
                    dTimer.start();
                    btnBindphoneGetvercode.setEnabled(false);
                    etBindphoneNewnumber.setEnabled(false);
                    btnBindphoneGetvercode.setBackgroundColor(ContextCompat.getColor(BindPhoneActivity.this, R.color.status_btn_gray));
                    String phone = UserInfo.getInstance().getPhone();
                    iUserInfo.getVerCode(phone,WValue.SCENE_UPDATEMOBILE, TAG);
                    isHideLayer(llAvLoadingTransparent44,false);
                    break;
            }
        } else {
            ToastUtil.shortToast(MasApplication.getInstance(), R.string.net_error);
        }
    }

    /**
     * 计时器
     */
    private CountDownTimer dTimer = new CountDownTimer(60000, 1000) {

        @Override
        public void onTick(long millisUntilFinished) {
            btnBindphoneGetvercode.setText(getText(R.string.sended) + "(" + (millisUntilFinished / 1000) + "s)");
        }

        @Override
        public void onFinish() {
            btnBindphoneGetvercode.setEnabled(true);
            btnBindphoneGetvercode.setText(R.string.resend);
            btnBindphoneGetvercode.setBackgroundColor(ContextCompat.getColor(BindPhoneActivity.this, R.color.main_color_red));
            if (flag == WValue.MODIFY_MOBILE) {
                etBindphoneNewnumber.setEnabled(true);
            } else {
                etBindphoneNewnumber.setEnabled(false);
            }

        }
    };


    @Override
    public void onActionFail(Result result) {
        super.onActionFail(result);
        isHideLayer(llAvLoadingTransparent44,true);
        String msg=result.getMsg();
        switch(result.getAction()){
            case WAction.CHECK_VERCODE:
                if(TextUtils.isEmpty(msg)){
                    msg=getText(R.string.toast_get_vercode_failue).toString();
                }
                break;
            case WAction.BIND_PHONE:
                if(TextUtils.isEmpty(msg)){
                    msg=getText(R.string.bindphone_failue).toString();
                }
                break;
        }
        ToastUtil.shortToast(this,msg);
    }

    @Override
    public void onActionSucc(Result result) {
        super.onActionSucc(result);
        isHideLayer(llAvLoadingTransparent44,true);
        Intent intent;
        switch(result.getAction()){
            case WAction.CHECK_VERCODE:
                intent=new Intent(this,BindPhoneActivity.class);
                intent.putExtra("flag", WValue.MODIFY_MOBILE);
                intent.putExtra("phoneNumber",phoneNumber);
                startActivity(intent);
                ToastUtil.shortToast(this,R.string.verify_vercode_success);
                break;
            case WAction.BIND_PHONE:
                //保存新手机到本地
                UserInfo.getInstance().setPhone(newPhoneNumber);
                UserInfo.getInstance().Save();

                intent=new Intent(this,PersonalInfoActivity.class);
                intent.putExtra("newPhoneNumber",newPhoneNumber);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                ToastUtil.shortToast(this,R.string.bindphone_success);
                break;
        }
    }

    /**
     * 验证手机号
     */
    public static boolean isMobile(String str) {
        Pattern p = null;
        Matcher m = null;
        boolean b = false;
        p = Pattern.compile("^[1][3,4,5,7,8][0-9]{9}$"); // 验证手机号
        m = p.matcher(str);
        b = m.matches();
        return b;
    }

}
