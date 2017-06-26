package com.eme.mas.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.eme.mas.R;
import com.eme.mas.connection.annotation.WLayout;
import com.eme.mas.data.sp.SPBase;
import com.eme.mas.data.sp.SpConstant;
import com.eme.mas.model.entity.UserInfo;
import com.eme.mas.utils.DataCleanUtil;
import com.eme.mas.utils.ToastUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 设置页面
 * Created by zulei on 16/7/26.
 */
@WLayout(layoutId = R.layout.activity_setting)
public class SettingActivity extends BaseActivity {

    @Bind(R.id.tv_bar_title)
    TextView tvBarTitle;
    @Bind(R.id.arrow_bar_title)
    ImageView arrowBarTitle;
    @Bind(R.id.search)
    LinearLayout search;
    @Bind(R.id.tv_setting_login_password_status)
    TextView tvSettingLoginPasswordStatus;
    @Bind(R.id.tv_setting_pay_password_status)
    TextView tvSettingPayPasswordStatus;
    @Bind(R.id.tv_setting_cache)
    TextView tvSettingCache;

    private String loginPassword, payPassword;

    private final static int VERIFY_LOGIN_PASSWORD = 1001;
    private final static int VERIFY_PAY_PASSWORD = 1002;
    private final static String TAG = "SettingActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @Override
    protected void onDestroy() {
        ButterKnife.unbind(this);
        super.onDestroy();

    }

    @Override
    public void afterContentView() {
        super.afterContentView();
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    private void initView() {
        tvBarTitle.setText(R.string.setting_title);
        arrowBarTitle.setVisibility(View.GONE);
        search.setVisibility(View.GONE);

    }

    private void initData() {
        //loginPassword = UserInfo.getInstance().getLoginPassword();
        //tvSetContent(tvSettingLoginPasswordStatus, loginPassword);
        //payPassword = UserInfo.getInstance().getPayPassword();
        //tvSetContent(tvSettingPayPasswordStatus, payPassword);
        setCache();

    }



    @OnClick(R.id.back)
    public void onClick() {
        finish();
    }

    private AlertDialog.Builder builder;//退出弹框

    @OnClick({R.id.rl_setting_login_password, R.id.rl_setting_pay_password, R.id.rl_setting_cleancache, R.id.btn_setting_quit})
    public void onClick(View view) {
        String phoneNumber = UserInfo.getInstance().getPhone();
        switch (view.getId()) {
            case R.id.rl_setting_login_password:
                if(TextUtils.isEmpty(phoneNumber)){
                    ToastUtil.shortToast(this,"未绑定手机");
                }else{
                    Intent intentL = new Intent(this, VerifyBeforeSetPasswordActivity.class);
                    intentL.putExtra("flag", VERIFY_LOGIN_PASSWORD);
                    startActivity(intentL);
                }

                break;
            case R.id.rl_setting_pay_password:
                /*if(TextUtils.isEmpty(phoneNumber)){
                    ToastUtil.shortToast(this,"未绑定手机");
                }else{
                    Intent intentP = new Intent(this, VerifyBeforeSetPasswordActivity.class);
                    intentP.putExtra("flag", VERIFY_PAY_PASSWORD);
                    startActivity(intentP);
                }*/
                break;
            case R.id.rl_setting_cleancache:
                DataCleanUtil.cleanInternalCache(this);
                ToastUtil.shortToast(this, R.string.toast_clean_cache_ok);
                setCache();
                break;
            case R.id.btn_setting_quit:
                if(builder==null){
                    builder=new AlertDialog.Builder(this);
                    builder.setMessage(R.string.is_quit).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            SPBase.setSPBoolean(SettingActivity.this, SpConstant.LOGIN_FILE_NAME,SpConstant.LOGIN_KEY,false);
                            UserInfo.getInstance().setBirthday("");
                            UserInfo.getInstance().setGender("");
                            UserInfo.getInstance().setNickname("");
                            UserInfo.getInstance().setUseId("");
                            UserInfo.getInstance().setImageUrl("");
                            UserInfo.getInstance().setPhone("");
                            UserInfo.getInstance().Save();
                            finish();
                        }
                    })
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).create().show();
                }else{
                    builder.show();
                }
                break;
        }
    }


    private void tvSetContent(TextView textView, String string) {
        if (!TextUtils.isEmpty(string)) {
            textView.setText(R.string.seted);
            textView.setTextColor(ContextCompat.getColor(this, R.color.status_text_gray));
        } else {
            textView.setText(R.string.notset);
            textView.setTextColor(ContextCompat.getColor(this, R.color.main_color_blue));
        }

    }

    private void setCache() {
        try {
            String cacheSize = DataCleanUtil.getCacheSize(this.getCacheDir());
            tvSettingCache.setText(cacheSize);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
