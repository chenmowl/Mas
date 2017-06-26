package com.eme.mas.activity;

import android.os.Bundle;
import android.view.View;

import com.eme.mas.R;
import com.eme.mas.connection.annotation.WLayout;
import com.eme.mas.customeview.dialog.ProShareDialog;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 订单在线支付提交完毕后展示的分享界面
 *
 * Created by dijiaoliang on 2016/8/3.
 */
@WLayout(layoutId = R.layout.activity_order_share)
public class OrderShareActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }


    @OnClick({R.id.ll_close,R.id.rl_share})
    void click(View v){
        switch (v.getId()){
            case R.id.ll_close:
                finish();
                break;
            case R.id.rl_share:
                //分享弹出框
                final ProShareDialog csDialog = new ProShareDialog();
                csDialog.setOnShareListener(new ProShareDialog.OnShareListener() {
                    @Override
                    public void share() {

                    }
                });
                csDialog.showDialog(this, null);
                break;
        }

    }

}
