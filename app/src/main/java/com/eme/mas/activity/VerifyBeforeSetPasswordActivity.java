package com.eme.mas.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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
 * 设置登录（支付）密码前的验证手机号页面
 * Created by zulei on 16/7/28.
 */
@WLayout(layoutId = R.layout.activity_verify_before_setpassword)
public class VerifyBeforeSetPasswordActivity extends BaseActivity {

    @Bind(R.id.tv_bar_title)
    TextView tvBarTitle;
    @Bind(R.id.arrow_bar_title)
    ImageView arrowBarTitle;
    @Bind(R.id.search)
    LinearLayout search;
    @Bind(R.id.btn_vbs_getvercode)
    Button btnVbsGetvercode;
    @Bind(R.id.et_vbs_vercode)
    EditText etVbsVercode;
    @Bind(R.id.tv_vbs_sendto)
    TextView tvVbsSendto;
    @Bind(R.id.btn_vbs_next)
    Button btnVbsNext;
    @Bind(R.id.tv_vbs_hint)
    TextView tvVbsHint;
    @Bind(R.id.ll_av_loading_transparent_44)
    LinearLayout llAvLoadingTransparent44;

    private IUserInfo iUserInfo;
    private int flag;
    private boolean isSend;
    private String phoneNumber, verCode;

    private final static int VERIFY_LOGIN_PASSWORD = 1001;
    private final static int VERIFY_PAY_PASSWORD = 1002;
    private final static String TAG = "VerifyBeforeSetPasswordActivity";

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
        btnVbsNext.setEnabled(false);
    }

    private void initData() {
        iUserInfo = mController.getUserInfo(this);
        isSend = false;
        flag = getIntent().getIntExtra("flag", 0);
        if (flag == VERIFY_LOGIN_PASSWORD) {
            tvBarTitle.setText(R.string.vbs_title_login_password);
            tvVbsHint.setText(R.string.vbs_hint_login_password_benefit);
        } else {
            tvBarTitle.setText(R.string.vbs_title_pay_password);
            tvVbsHint.setText(R.string.vbs_hint_pay_password_benefit);
        }

        phoneNumber = UserInfo.getInstance().getPhone();
        tvVbsSendto.setText(getText(R.string.vercode_sendto) + ":" + phoneNumber.substring(0, 3) + "****" + phoneNumber.substring(
                phoneNumber.length() - 4, phoneNumber.length()));

    }

    @OnClick(R.id.back)
    public void back() {
        finish();
    }

    @OnClick({R.id.btn_vbs_getvercode, R.id.btn_vbs_next})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_vbs_getvercode:
                dTimer.start();
                isSend = true;
                btnVbsGetvercode.setEnabled(false);
                btnVbsGetvercode.setBackgroundColor(ContextCompat.getColor(VerifyBeforeSetPasswordActivity.this, R.color.status_btn_gray));
                if (NetworkStatusUtil.isNetworkConnected(this)) {
                    isHideLayer(llAvLoadingTransparent44,false);
                    iUserInfo.getVerCode(phoneNumber, WValue.SCENE_SETPWD, TAG);
                } else {
                    ToastUtil.shortToast(this, R.string.net_error);
                }
                break;
            case R.id.btn_vbs_next:
                if (NetworkStatusUtil.isNetworkConnected(this)) {
                    if (flag == VERIFY_LOGIN_PASSWORD) {
                        isHideLayer(llAvLoadingTransparent44,false);
                        iUserInfo.checkVerCode(phoneNumber, verCode, WValue.SCENE_SETPWD,TAG);
                    } else {
                        //FIXME 修改支付密码验证接口目前未提供
//                        iUserInfo.verifyCodeForSetLoginPassword(phoneNumber, verCode, TAG);
                    }
                } else {
                    ToastUtil.shortToast(this, R.string.net_error);
                }
                break;
        }
    }

    @OnTextChanged(R.id.et_vbs_vercode)
    public void etChanged(CharSequence text) {
        verCode = text.toString();
        if (!TextUtils.isEmpty(verCode)) {
            btnVbsNext.setEnabled(true);
            btnVbsNext.setBackgroundColor(ContextCompat.getColor(this, R.color.main_color_red));
        } else {
            btnVbsNext.setEnabled(false);
            btnVbsNext.setBackgroundColor(ContextCompat.getColor(this, R.color.status_btn_gray));
        }
    }

    private CountDownTimer dTimer = new CountDownTimer(60000, 1000) {

        @Override
        public void onTick(long millisUntilFinished) {
            btnVbsGetvercode.setText(getText(R.string.sended) + "(" + (millisUntilFinished / 1000) + "s)");
        }

        @Override
        public void onFinish() {
            isSend = false;
            btnVbsGetvercode.setEnabled(true);
            btnVbsGetvercode.setText(R.string.resend);
            btnVbsGetvercode.setBackgroundColor(ContextCompat.getColor(VerifyBeforeSetPasswordActivity.this, R.color.main_color_red));
            etVbsVercode.setEnabled(true);

        }
    };


    @Override
    public void onActionFail(Result result) {
        super.onActionFail(result);
        isHideLayer(llAvLoadingTransparent44,true);
        String message = result.getMsg();
        ToastUtil.shortToast(this, message);
        switch (result.getAction()){
            case WAction.GET_VERCODE:
                btnVbsGetvercode.setEnabled(true);
                btnVbsGetvercode.setText(R.string.resend);
                btnVbsGetvercode.setBackgroundColor(ContextCompat.getColor(VerifyBeforeSetPasswordActivity.this, R.color.main_color_red));
                etVbsVercode.setEnabled(true);
                break;
        }
    }

    @Override
    public void onActionSucc(Result result) {
        super.onActionSucc(result);
        isHideLayer(llAvLoadingTransparent44,true);
        switch (result.getAction()){
            case WAction.GET_VERCODE:
                ToastUtil.shortToast(this, R.string.toast_get_vercode_ok);
                break;
            case WAction.CHECK_VERCODE:
                if(VERIFY_LOGIN_PASSWORD==flag){
                    Intent intentL = new Intent(this, MineSetLoginPasswordActivity.class);
                    intentL.putExtra("phone_number",phoneNumber);
                    startActivity(intentL);
                    finish();
                }else{
                    Intent intentP = new Intent(this, SetPayPasswordActivity.class);
                    startActivity(intentP);
                    finish();
                }
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
