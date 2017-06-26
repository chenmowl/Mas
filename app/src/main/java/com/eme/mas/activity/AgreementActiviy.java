package com.eme.mas.activity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.eme.mas.R;
import com.eme.mas.connection.annotation.WLayout;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 用户服务协议
 * Created by zhaoxin on 16/7/27.
 */
@WLayout(layoutId = R.layout.activity_agreemnet)
public class AgreementActiviy extends BaseActivity {
    @Bind(R.id.back)
    LinearLayout back;
    @Bind(R.id.bar_title)
    TextView barTitle;
    @Bind(R.id.bar_back)
    ImageView barBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }

    @Override
    public void afterContentView() {
        super.afterContentView();
        barTitle.setText("用户服务协议");
        barBack.setImageResource(R.mipmap.agreement);
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
}

