package com.eme.mas.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.eme.mas.R;
import com.eme.mas.connection.annotation.WLayout;
import com.eme.mas.controller.customeInterface.IUserInfo;
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
 * 修改昵称页面
 * Created by zulei on 16/7/25.
 */
@WLayout(layoutId = R.layout.activity_personalinfo_changenick)
public class PersonalInfoChangeNickActivity extends BaseActivity {

    private final static String TAG = PersonalInfoChangeNickActivity.class.getSimpleName();

    @Bind(R.id.tv_bar_title)
    TextView tvBarTitle;
    @Bind(R.id.arrow_bar_title)
    ImageView arrowBarTitle;
    @Bind(R.id.search)
    LinearLayout search;
    @Bind(R.id.btn_personalinfo_changenick_done)
    Button btnPersonalinfoChangenickDone;
    @Bind(R.id.et_personalinfo_changenick)
    EditText etPersonalinfoChangenick;
    @Bind(R.id.ib_personalinfo_changenick_etdelete)
    ImageButton ibPersonalinfoChangenickEtdelete;
    @Bind(R.id.ll_av_loading_transparent_44)
    LinearLayout llAvLoadingTransparent44;

    private String nickName;

    private IUserInfo iUserInfoController;

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
    }

    @Override
    public void beforeContentView() {
        super.beforeContentView();
        iUserInfoController = mController.getUserInfo(this);
    }

    @Override
    public void afterContentView() {
        super.afterContentView();
        initView();
    }

    private void initView() {
        tvBarTitle.setText(R.string.personalinfo_change_nickname_title);
        arrowBarTitle.setVisibility(View.GONE);
        search.setVisibility(View.GONE);

        String nickName =UserInfo.getInstance().getNickname();
        if("".equals(nickName)){
            statusNickNameIsNull();
        }else{
            statusNickNameIsNotNull();
            etPersonalinfoChangenick.setText(nickName);
        }

    }


    @OnClick(R.id.back)
    public void onClick() {
        finish();
    }


    @OnClick({R.id.ib_personalinfo_changenick_etdelete, R.id.btn_personalinfo_changenick_done})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ib_personalinfo_changenick_etdelete:
                etPersonalinfoChangenick.setText("");
                statusNickNameIsNull();
                break;
            case R.id.btn_personalinfo_changenick_done:
//                boolean b = isCurrentNickName(nickName);
//                if(b){
//                    Intent intent = new Intent();
//                    intent.putExtra("nickName",nickName);
//                    setResult(RESULT_OK,intent);
//                    finish();
//                }else{
//                    ToastUtil.shortToast(this,R.string.toast_nickname_format_wrong);
//                }

                if (NetworkStatusUtil.isNetworkConnected(PersonalInfoChangeNickActivity.this)) {
                    isHideLayer(llAvLoadingTransparent44,false);
                    iUserInfoController.setUserInfo(nickName,null,null,null,TAG);
                } else {
                    ToastUtil.shortToast(PersonalInfoChangeNickActivity.this, R.string.net_error);
                }
                break;
        }
    }

    @OnTextChanged(R.id.et_personalinfo_changenick)
    public void onTextChanged(CharSequence text) {
        Log.i("info", text.toString() + "");
        nickName = text.toString();
        if("".equals(nickName)){
            statusNickNameIsNull();
        }else{
            statusNickNameIsNotNull();
        }
    }

    private void statusNickNameIsNotNull(){
        ibPersonalinfoChangenickEtdelete.setVisibility(View.VISIBLE);
        btnPersonalinfoChangenickDone.setBackgroundResource(R.drawable.btn_status_enable);
        btnPersonalinfoChangenickDone.setEnabled(true);
    }

    private void statusNickNameIsNull(){
        ibPersonalinfoChangenickEtdelete.setVisibility(View.GONE);
        btnPersonalinfoChangenickDone.setBackgroundResource(R.drawable.btn_status_unable);
        btnPersonalinfoChangenickDone.setEnabled(false);
    }

    public static boolean isCurrentNickName(String nickName) {
        String strPattern = "^[\\u4e00-\\u9fa5a-zA-Z0-9]{4,20}$";
        Pattern p = Pattern.compile(strPattern);
        Matcher m = p.matcher(nickName);
        return m.matches();
    }

    @Override
    public void onActionFail(Result result) {
        super.onActionFail(result);
        String msg=result.getMsg();
        if(TextUtils.isEmpty(msg)){
            msg=getText(R.string.personalinfo_change_nickname_title_failue).toString();
        }
        ToastUtil.shortToast(this,msg);
    }

    @Override
    public void onActionSucc(Result result) {
        super.onActionSucc(result);
        UserInfo.getInstance().setNickname(nickName);
        UserInfo.getInstance().Save();
        ToastUtil.shortToast(this,R.string.personalinfo_change_nickname_title_success);
        Intent intent=new Intent();
        intent.putExtra("nickName",nickName);
        setResult(RESULT_OK,intent);
        finish();
    }
}
